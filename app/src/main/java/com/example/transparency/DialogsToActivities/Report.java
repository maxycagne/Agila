package com.example.transparency.DialogsToActivities;

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

import com.example.transparency.Citizen.Activities.CitizenDashboard;
import com.example.transparency.Database.Projects;
import com.example.transparency.Database.ReportDetails;
import com.example.transparency.Politician.Activities.AddProject;
import com.example.transparency.R;
import com.example.transparency.databinding.ActivityReportBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.core.Repo;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class Report extends AppCompatActivity {

    private ActivityReportBinding root;
    private Uri selectedImageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        root = ActivityReportBinding.inflate(getLayoutInflater());
        setContentView(root.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        String[] report ={"Corruption","MisLeading information","Fraud","Others"};

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, report);
        root.editManageReport.setAdapter(arrayAdapter);

        root.editManageReport.setOnClickListener(view -> {
            root.editManageReport.showDropDown();
        });


        ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
                registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                    if (uri != null) {
                        Log.d("PhotoPicker", "Selected URI: " + uri);
                        selectedImageUri = uri;
                        root.evidenc.setImageURI(uri);
                    } else {
                        Log.d("PhotoPicker", "No media selected");
                    }
                });
        root.btnBack.setOnClickListener(v -> {
            startActivity(new Intent(this, CitizenDashboard.class));
            finish();
        });

        root.btnCancel.setOnClickListener(v -> {
            startActivity(new Intent(this, CitizenDashboard.class));
            finish();
        });

        root.btnUpload.setOnClickListener(v -> {
            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
        });

        root.btnReport.setOnClickListener(v -> {
            saveUser();
        });
    }

    public void saveUser() {
        if (! validate()){
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
        Toast.makeText(Report.this, "Please wait for a while thank you", Toast.LENGTH_LONG).show();
        if (fileUri != null) {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            StorageReference fileRef = storageRef.child("uploads/" + System.currentTimeMillis() + ".jpg");
            fileRef.putFile(fileUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            String downloadUrl = uri.toString();


                            String reportFrom = "Clarence Garcia";
                            String reportedTo = "Jethro Damaso";
                            String typeOfReport = root.editManageReport.getText().toString();
                            String desc = root.editReportDescription.getText().toString();
                            String image = downloadUrl;



                            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                            DatabaseReference databaseReference = firebaseDatabase.getReference();
                            String reportId = databaseReference.push().getKey();

                            ReportDetails reportDetails = new ReportDetails(reportId,reportFrom,reportedTo,typeOfReport,
                                    desc,image);



                            AlertDialog.Builder builder = new AlertDialog.Builder(this);
                            builder.setTitle("Report ").setMessage("Would you like to report this?").setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    databaseReference.child("reportDetails").child(reportId).setValue(reportDetails)
                                            .addOnSuccessListener(unused -> {
                                                Toast.makeText(Report.this, "Report sent successfully", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(Report.this, CitizenDashboard.class));
                                                finish();})
                                            .addOnFailureListener(e -> {
                                                Toast.makeText(Report.this, "Failed: " +e.getMessage(), Toast.LENGTH_SHORT).show();
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


                            loadImageWithPicasso(downloadUrl, root.evidenc);

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

    public boolean validate()
    {
        if (root.editManageReport.getText().toString().equals(""))
        {
            root.layoutManageReport.setErrorEnabled(true);
            root.layoutManageReport.setError("Please fill this field.");
            return false;
        }

        if (root.editReportDescription.getText().toString().equals(""))
        {
            root.layoutReportDescription.setErrorEnabled(true);
            root.layoutReportDescription.setError("Please fill this field");
            return false;
        }

        return true;
    }



}