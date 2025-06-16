package com.example.transparency.Politician.Activities;


import static android.content.ContentValues.TAG;

import android.app.DatePickerDialog;

import android.app.Dialog;
import android.content.DialogInterface;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import androidx.recyclerview.widget.SortedList;

import com.example.transparency.Database.Citizen;
import com.example.transparency.Database.Politicians;
import com.example.transparency.Database.Projects;
import com.example.transparency.Login.RegisterActivity;
import com.example.transparency.R;
import com.example.transparency.databinding.ActivityAddProjectBinding;
import com.example.transparency.databinding.DialogAddCollaboratorsBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import android.Manifest;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;

import androidx.core.content.ContextCompat;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;

public class AddProject extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap gMap;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;

    private Uri selectedImageUri;
    private ActivityAddProjectBinding root;

    private Politicians politicians;


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
        root = ActivityAddProjectBinding.inflate(getLayoutInflater());
        setContentView(root.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        root.editTimeframeStart.setOnClickListener(view -> {
            showDatePickerDialogstart();
        });

        root.editTimeframeEnd.setOnClickListener(view -> {
            showDatePickerDialogEnd();
        });

        ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
                registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                    if (uri != null) {
                        Log.d("PhotoPicker", "Selected URI: " + uri);
                        selectedImageUri = uri;
                        root.imgBanner.setImageURI(uri);
                    } else {
                        Log.d("PhotoPicker", "No media selected");
                    }
                });

        root.btnUploadBanner.setOnClickListener(view -> {
            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
        });

        root.btnBack.setOnClickListener(view -> {
            startActivity(new Intent(this,PoliticianDashboard.class));
            finish();
        });

        root.btnRequest.setOnClickListener(view -> {
            saveUser();
        });



    }

    public void saveUser() {
        if (!validate())
        {
            return;
        }
        if (selectedImageUri != null) {
            uploadFile(selectedImageUri);
        }
        else {
            Toast.makeText(this, "Please select an image first",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadFile(Uri fileUri) {
        Toast.makeText(AddProject.this, "Please wait for a while thank you", Toast.LENGTH_LONG).show();
        if (fileUri != null) {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            StorageReference fileRef = storageRef.child("uploads/" + System.currentTimeMillis() + ".jpg");
            fileRef.putFile(fileUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            String downloadUrl = uri.toString();


                            String projectName = root.editProjectName.getText().toString();
                            String projectImage = downloadUrl;
                            String projectLocation = root.editProjectLocation.getText().toString();
                            String startDate = root.editTimeframeStart.getText().toString();
                            String endDate = root.editTimeframeEnd.getText().toString();
                            String dateOfCompletion = "";
                            String description = root.editProjectDescription.getText().toString();
                            int projectRating = 0;
                            String status = "Pending";
                            String projectOf = "Clarence Garcia";
                            String feedback = "";
                            Double budget = Double.valueOf(root.editProjectBudget.getText().toString());


                            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                            DatabaseReference databaseReference = firebaseDatabase.getReference();
                            String projectId = databaseReference.push().getKey();

                            Projects projects = new Projects(projectId,projectName,projectImage,projectLocation,startDate,endDate,dateOfCompletion,description,
                                    projectRating,status,projectOf,feedback,budget);



                            AlertDialog.Builder builder = new AlertDialog.Builder(this);
                            builder.setTitle("Project Request").setMessage("Do you want to request this project?").setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    databaseReference.child("project").child(projectId).setValue(projects)
                                            .addOnSuccessListener(unused -> {
                                                Toast.makeText(AddProject.this, "Request send successfully please wait for confirmation", Toast.LENGTH_SHORT).show();
                                                finish();})
                                            .addOnFailureListener(e -> {
                                                Toast.makeText(AddProject.this, "Failed: " +e.getMessage(), Toast.LENGTH_SHORT).show();
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


                            loadImageWithPicasso(downloadUrl, root.imgBanner);

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


    private void showDatePickerDialogstart() {
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

            root.editTimeframeStart.setText(simpleDateFormat.format(calendar1.getTimeInMillis()));

        }, year, month, day);

        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        datePickerDialog.show();
    }

    private void showDatePickerDialogEnd() {
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

            root.editTimeframeEnd.setText(simpleDateFormat.format(calendar1.getTimeInMillis()));

        }, year, month, day);

        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        datePickerDialog.show();

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
                root.editProjectLocation.append("\n" + addressText);             }
        } catch (IOException e) {
            Log.e("Geocoding", "Error getting address", e);
        }
    }

    public boolean validate()
    {
        if (root.editProjectName.getText().toString().equals(""))
        {
            root.layoutProjectName.setErrorEnabled(true);
            root.layoutProjectName.setError("This field is required to be filled");
            return false;
        }

        if (root.editProjectBudget.getText().toString().equals(""))
        {
            root.layoutProjectBudget.setErrorEnabled(true);
            root.layoutProjectBudget.setError("This field is required to be filled");
            return false;
        }

        if (root.editProjectLocation.getText().toString().equals(""))
        {
            root.layoutProjectLocation.setErrorEnabled(true);
            root.layoutProjectLocation.setError("This field is required to be filled");
            return false;
        }

        if (root.editProjectDescription.getText().toString().equals(""))
        {
            root.layoutProjectDescription.setErrorEnabled(true);
            root.layoutProjectDescription.setError("This field is required to be filled");
            return false;
        }

        if (root.editTimeframeStart.getText().toString().equals(""))
        {
            root.layoutTimeframeStart.setErrorEnabled(true);
            root.layoutTimeframeStart.setError("This field is required to be filled");
            return false;
        }

        if (root.editTimeframeEnd.getText().toString().equals(""))
        {
            root.layoutTimeframeEnd.setErrorEnabled(true);
            root.layoutTimeframeEnd.setError("This field is required to be filled");
            return false;
        }
        return  true;
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (fusedLocationClient != null && locationCallback != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }

    void addCollaborators()
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("politicians");

        ArrayList<String> politicianNames = new ArrayList<String>();
        Query collaboratorsQuery = myRef.child("politician").child("priv").equalTo("2");
        Query query = myRef.orderByChild("firstName");

        // My top posts by number of stars
        collaboratorsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                   politicianNames.add(postSnapshot.toString());
                    Log.w(TAG, postSnapshot.toString());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        });

    }

}