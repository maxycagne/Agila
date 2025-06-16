package com.example.transparency.Citizen.Activities;

import static androidx.constraintlayout.motion.widget.TransitionBuilder.validate;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.transparency.Adapter.RatingAdapter;
import com.example.transparency.Adapter.RecordLogsAdapter;
import com.example.transparency.Database.Citizen;
import com.example.transparency.Database.Projects;
import com.example.transparency.Database.Ratings;
import com.example.transparency.Database.RecordLogs;
import com.example.transparency.DialogsToActivities.Rate;
import com.example.transparency.DialogsToActivities.Report;
import com.example.transparency.Login.RegisterActivity;
import com.example.transparency.Politician.Activities.AddProject;
import com.example.transparency.Politician.Activities.ViewProjectAsPolitician;
import com.example.transparency.R;
import com.example.transparency.databinding.ActivityViewProjectAsCitizenBinding;
import com.example.transparency.databinding.DialogReportBinding;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ViewProjectAsCitizen extends AppCompatActivity {

    private ActivityViewProjectAsCitizenBinding root;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        root = ActivityViewProjectAsCitizenBinding.inflate(getLayoutInflater());
        setContentView(root.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        Projects projects = (Projects) getIntent().getSerializableExtra("projects");

        Picasso.get()
                .load(projects.getProjectImage())
                .resize(1024, 1024)
                .into(root.imgBanner);
        root.txtEndDate.setText(projects.getEndDate());
        root.txtStartDate.setText(projects.getStartDate());
        root.txtProjectBudget.setText(String.valueOf(projects.getBudget()));
        root.txtProjectName.setText(projects.getProjectName());
        root.txtProjectLocation.setText(projects.getProjectLocation());
        root.txtProjectRating.setText(String.valueOf(projects.getProjectRating()));
        root.txtProjectDescription.setText(projects.getDescription());



        root.btnBack.setOnClickListener(v -> {
            startActivity(new Intent(this, CitizenDashboard.class));
            finish();
        });


        root.btnReport.setOnClickListener(view -> {
            startActivity(new Intent(this, Report.class));
            finish();
        });


        root.btnRate.setOnClickListener(view -> {
            startActivity(new Intent(this, Rate.class));
            finish();
        });

        retrieveLatestRating();
        retrieveLatestProject();
    }

    public void retrieveLatestProject() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("recordLogs");


        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<RecordLogs> recordLogsList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    RecordLogs recordLogs = dataSnapshot.getValue(RecordLogs.class);
                    if (recordLogs != null) {
                        recordLogsList.add(recordLogs);
                    }
                }

                if (!recordLogsList.isEmpty()) {
                    RecordLogsAdapter adapter = new RecordLogsAdapter(ViewProjectAsCitizen.this, recordLogsList, new RecordLogsAdapter.RecordLogsClick() {
                        @Override
                        public void onClick(RecordLogs recordLogs) {

                        }
                    });

                    root.recentLogs.setAdapter(adapter);
                } else {
                    root.recentLogs.setAdapter(null); // Or an empty adapter
                    Toast.makeText(ViewProjectAsCitizen.this, "No projects found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewProjectAsCitizen.this, "Failed to retrieve latest project: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                System.err.println("Database error: " + error.getCode());
            }
        });
    }

    public void retrieveLatestRating() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("rate");


        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Ratings> ratingsList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Ratings ratings = dataSnapshot.getValue(Ratings.class);
                    if (ratings != null) {
                        ratingsList.add(ratings);
                    }
                }

                if (!ratingsList.isEmpty()) {
                    RatingAdapter adapter = new RatingAdapter(ViewProjectAsCitizen.this, ratingsList, new RatingAdapter.RatingClick() {
                        @Override
                        public void onClick(Ratings ratings) {

                        }
                    });

                    root.recentRating.setAdapter(adapter);
                } else {
                    root.recentRating.setAdapter(null); // Or an empty adapter
                    Toast.makeText(ViewProjectAsCitizen.this, "No projects found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewProjectAsCitizen.this, "Failed to retrieve latest project: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                System.err.println("Database error: " + error.getCode());
            }
        });
    }


}