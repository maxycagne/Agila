package com.example.transparency.Admin.Activities;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.transparency.Database.Politicians;
import com.example.transparency.R;
import com.example.transparency.databinding.ActivityCreateAccountBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.support.annotation.NonNull;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.util.List;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Looper;

import androidx.core.content.ContextCompat;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;

public class CreateAccount extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap gMap;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;

    private Uri selectedImageUri1;
    private ActivityCreateAccountBinding root;

    private final ActivityResultLauncher<String[]> locationPermissionRequest =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                Boolean fineLocationGranted = result.get(Manifest.permission.ACCESS_FINE_LOCATION);
                Boolean coarseLocationGranted = result.get(Manifest.permission.ACCESS_COARSE_LOCATION);
                if (fineLocationGranted != null && fineLocationGranted) {
                    // Precise location access granted                     getCurrentLocation();
                } else if (coarseLocationGranted != null && coarseLocationGranted) {
                    // Only approximate location access granted
                    getCurrentLocation();                 } else {
                    // No location access granted
                    Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
                }
            });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        root = ActivityCreateAccountBinding.inflate(getLayoutInflater());
        setContentView(root.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        String[] position = {"Mayor","Vice Mayor","Councilor","Chairman","SK Charmain"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, position);
        root.editPosition.setAdapter(arrayAdapter);

        root.editPosition.setOnClickListener(view -> {
            root.editPosition.showDropDown();
        });
        ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
                registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                    if (uri != null) {
                        Log.d("PhotoPicker", "Selected URI: " + uri);
                        selectedImageUri1 = uri;
                        root.imgAccountProfile.setImageURI(uri);
                    } else {
                        Log.d("PhotoPicker", "No media selected");
                    }
                });
        root.btnUploadProfilePic.setOnClickListener(view -> {
            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
        });

        root.btnBack.setOnClickListener(v -> {
            startActivity(new Intent(this,DashboardAdmin.class));
            finish();
        });

        root.btnCreate.setOnClickListener(v -> {
                saveUser();
        });

        root.editBirhdate.setOnClickListener(v -> {
            showDatePickerDialog();
        });

    }

    public void saveUser() {
        if (!validate())
        {
            return;
        }
        if (selectedImageUri1 != null) {
            uploadFile(selectedImageUri1);
        }

        else {
            Toast.makeText(this, "Please select an image.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadFile(Uri fileUri) {

        Toast.makeText(CreateAccount.this, "Please wait for a while, Thank you", Toast.LENGTH_LONG).show();
        if (fileUri != null) {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            StorageReference fileRef = storageRef.child("uploads/" + System.currentTimeMillis() + ".jpg");
            fileRef.putFile(fileUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            String downloadUrl = uri.toString();

                            String firstName = root.editFName.getText().toString();
                            String middleName = root.editMName.getText().toString();
                            String lastName = root.editLName.getText().toString();
                            String birthDate = root.editBirhdate.getText().toString();
                            String address = root.editBypassAddress.getText().toString();
                            String phoneNumber = "+63" + root.editPhoneNumber.getText().toString();
                            String profilePicture = downloadUrl;
                            String password = root.editLName.getText().toString() +"2025";
                            String email = root.editEmail.getText().toString();
                            String position = root.editPosition.getText().toString();


                            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                            DatabaseReference databaseReference = firebaseDatabase.getReference();
                            String priv = "2";

                            Politicians politicians = new Politicians(firstName,middleName,lastName,position,birthDate,address,phoneNumber,email,password,profilePicture,priv);



                            String politiciansId = databaseReference.push().getKey();


                            AlertDialog.Builder builder = new AlertDialog.Builder(this);
                            builder.setTitle("Create Account").setMessage("Do you want to create this account?").setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    databaseReference.child("politicians").child(politiciansId).setValue(politicians)
                                            .addOnSuccessListener(unused -> {
                                                Toast.makeText(CreateAccount.this, "Created successfully.", Toast.LENGTH_SHORT).show();
                                                finish();})
                                            .addOnFailureListener(e -> {
                                                Toast.makeText(CreateAccount.this, "Failed: " +e.getMessage(), Toast.LENGTH_SHORT).show();
                                            });
                                }
                            });
                            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });

                            AlertDialog dialog = builder.create();
                            dialog.show();


                            loadImageWithPicasso(downloadUrl, root.imgAccountProfile);
                        });
                    }).addOnFailureListener(e -> {
                        Toast.makeText(getApplicationContext(), "Upload Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(getApplicationContext(), "No file selected!", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadImageWithPicasso(String downloadUrl, ImageView imageView) {
        Picasso.get()
                .load(downloadUrl)
                .resize(1024, 1024)
                .into(imageView);
    }


    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (datePicker, i, i1, i2) -> {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM dd yyyy", Locale.ENGLISH);


            Calendar calendar1 = Calendar.getInstance();
            calendar1.set(Calendar.YEAR, i);
            calendar1.set(Calendar.MONTH, i1);
            calendar1.set(Calendar.DAY_OF_MONTH, i2);

            root.editBirhdate.setText(simpleDateFormat.format(calendar1.getTimeInMillis()));

        }, year, month, day);

        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        datePickerDialog.show();

    }

    public boolean validate()
    {
        if (root.editFName.getText().toString().equals(""))
        {
            root.layoutFName.setErrorEnabled(true);
            root.layoutFName.setError("Section is required to be filled");
            return false;
        }

        if (root.editMName.getText().toString().equals(""))
        {
            root.layoutMName.setErrorEnabled(true);
            root.layoutMName.setError("Section is required to be filled");
            return false;
        }

        if (root.editLName.getText().toString().equals(""))
        {
            root.layoutLName.setErrorEnabled(true);
            root.layoutLName.setError("Section is required to be filled");
            return false;
        }

        if (root.editBirhdate.getText().toString().equals(""))
        {
            root.layoutBirthdate.setErrorEnabled(true);
            root.editBirhdate.setError("Section is required to be filled");
            return false;
        }

        if (root.editBypassAddress.getText().toString().equals(""))
        {
            root.layoutBypassAddress.setErrorEnabled(true);
            root.layoutBypassAddress.setError("Section is required to be filled");
            return false;
        }

        if (root.editPosition.getText().toString().equals(""))
        {
            root.layoutPosition.setErrorEnabled(true);
            root.layoutPosition.setError("Section is required to be filled");
            return false;
        }

        if (root.editPhoneNumber.getText().toString().equals(""))
        {
            root.layoutPhoneNumber.setErrorEnabled(true);
            root.layoutPhoneNumber.setError("Section is required to be filled");
            return false;
        }

        return true;
    }
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        gMap = googleMap;
        gMap.getUiSettings().setZoomControlsEnabled(true);
        gMap.setOnMapClickListener(latLng -> {

            gMap.clear();
            gMap.addMarker(new MarkerOptions()
                    .position(latLng)                     .title("Selected Location"));
            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f));
            geocodeLocation(latLng);
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        }
        else {
            requestLocationPermission();
        }
    }

    private void requestLocationPermission() {
        locationPermissionRequest.launch(new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        });
    }

    private void getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        try {
            LocationRequest locationRequest = LocationRequest.create()
                    .setInterval(30000)
                    .setFastestInterval(10000)
                    .setPriority(Priority.PRIORITY_HIGH_ACCURACY);
            locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    if (locationResult == null) return;
                    for (Location location : locationResult.getLocations()) {
                        updateMapWithLocation(location);
                        fusedLocationClient.removeLocationUpdates(this);
                    }
                }
            };
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(location -> {
                        if (location != null) {
                            updateMapWithLocation(location);
                        } else {
                            try {
                                fusedLocationClient.requestLocationUpdates(
                                        locationRequest,    locationCallback,
                                        Looper.getMainLooper()
                                );
                            } catch
                            (SecurityException e)
                            {
                                handleLocationError("Location permission revoked during operation");
                            }
                        }
                    })
                    .addOnFailureListener(e -> {
                        if (e instanceof SecurityException) {
                            handleLocationError("Permission check failed");
                        } else {
                            handleLocationError("Location unavailable: ");
                        }
                    });
        } catch (SecurityException e) {
            handleLocationError("Insufficient permissions for location request");
        }
    }
    private void handleLocationError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        Log.e("LocationError", message);
        LatLng defaultLocation = new LatLng(-34, 151);
        gMap.addMarker(new MarkerOptions()
                .position(defaultLocation)
                .title("Default Location"));
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 10f));

    }
    private void updateMapWithLocation(Location location) {
        LatLng currentLatLng = new LatLng(location.getLatitude(),
                location.getLongitude());

        gMap.clear();
        gMap.addMarker(new MarkerOptions()
                .position(currentLatLng)
                .title("Your Location"));
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f));
        geocodeLocation(currentLatLng);
    }
    private void geocodeLocation(LatLng latLng) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(
                    latLng.latitude,
                    latLng.longitude,
                    1);
            if (!addresses.isEmpty()) {
                Address address = addresses.get(0);
                String addressText = String.format("%s, %s, %s",
                        address.getThoroughfare(),
                        address.getLocality(),
                        address.getCountryName());
                root.editBypassAddress.append("\n" + addressText);             }
        } catch (IOException e) {
            Log.e("Geocoding", "Error getting address", e);
        }
    }




    @Override
    protected void onStop() {
        super.onStop();
        if (fusedLocationClient != null && locationCallback != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }
}