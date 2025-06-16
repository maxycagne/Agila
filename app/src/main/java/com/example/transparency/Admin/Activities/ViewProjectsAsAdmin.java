package com.example.transparency.Admin.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.transparency.Citizen.Activities.CitizenDashboard;
import com.example.transparency.Database.Projects;
import com.example.transparency.R;
import com.example.transparency.databinding.ActivityViewProjectsAsAdminBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class ViewProjectsAsAdmin extends AppCompatActivity {

    private ActivityViewProjectsAsAdminBinding root;

    private Projects projects;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        root = ActivityViewProjectsAsAdminBinding.inflate(getLayoutInflater());
        setContentView(root.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

         projects = (Projects) getIntent().getSerializableExtra("projects");

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


        root.btnAccept.setOnClickListener(view -> {
            approved();

        });

        root.btnDeny.setOnClickListener(view -> {
            declined();

        });


        root.btnBack.setOnClickListener(view -> {
            startActivity(new Intent(this, DashboardAdmin.class));
            finish();
        });

    }

    public void declined()
    {
        Toast.makeText(ViewProjectsAsAdmin.this, "Please wait for a while thank you", Toast.LENGTH_LONG).show();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("project");
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("status", "Declined");

        databaseReference.child(projects.getProjectId()).updateChildren(objectMap)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "You declined the project!", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to update: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();

                });





    }

    public void approved()
    {
        Toast.makeText(ViewProjectsAsAdmin.this, "Please wait for a while thank you", Toast.LENGTH_LONG).show();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("project");
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("status", "Active");

        databaseReference.child(projects.getProjectId()).updateChildren(objectMap)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "You activeted the projects!", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to update: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();

                });


    }

}