package com.example.transparency.Politician.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.transparency.Adapter.MyProjectAdapter;
import com.example.transparency.Adapter.RatingAdapter;
import com.example.transparency.Adapter.RecordLogsAdapter;
import com.example.transparency.Database.Projects;
import com.example.transparency.Database.Ratings;
import com.example.transparency.Database.RecordLogs;
import com.example.transparency.R;
import com.example.transparency.databinding.ActivityViewProjectAsPoliticianBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ViewProjectAsPolitician extends AppCompatActivity {

    private ActivityViewProjectAsPoliticianBinding root;

    private Projects projects;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        root =ActivityViewProjectAsPoliticianBinding.inflate(getLayoutInflater());
        setContentView(root.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        projects = (Projects) getIntent().getSerializableExtra("projects");

        retrieveLatestProject();
        retrieveLatestRating();

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

        root.btnBack.setOnClickListener(view -> {
            startActivity(new Intent(this,PoliticianDashboard.class));
            finish();
        });

        root.btnRequest.setOnClickListener(view -> {
            startActivity(new Intent(this,LogRequest.class));
            finish();
        });
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
                    RecordLogsAdapter adapter = new RecordLogsAdapter(ViewProjectAsPolitician.this, recordLogsList, new RecordLogsAdapter.RecordLogsClick() {
                        @Override
                        public void onClick(RecordLogs recordLogs) {

                        }
                    });

                            root.rvrecenLog.setAdapter(adapter);
                } else {
                    root.rvrecenLog.setAdapter(null); // Or an empty adapter
                    Toast.makeText(ViewProjectAsPolitician.this, "No projects found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewProjectAsPolitician.this, "Failed to retrieve latest project: " + error.getMessage(), Toast.LENGTH_SHORT).show();
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
                    RatingAdapter adapter = new RatingAdapter(ViewProjectAsPolitician.this, ratingsList, new RatingAdapter.RatingClick() {
                        @Override
                        public void onClick(Ratings ratings) {

                        }
                    });

                    root.recentRating.setAdapter(adapter);
                } else {
                    root.recentRating.setAdapter(null); // Or an empty adapter
                    Toast.makeText(ViewProjectAsPolitician.this, "No projects found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewProjectAsPolitician.this, "Failed to retrieve latest project: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                System.err.println("Database error: " + error.getCode());
            }
        });
    }
}