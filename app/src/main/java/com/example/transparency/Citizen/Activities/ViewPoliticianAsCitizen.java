package com.example.transparency.Citizen.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.transparency.Adapter.ProjectAdapter;
import com.example.transparency.Citizen.Fragments.HomeFragment;
import com.example.transparency.Database.Politicians;
import com.example.transparency.Database.Projects;
import com.example.transparency.DialogsToActivities.Report;
import com.example.transparency.R;
import com.example.transparency.databinding.ActivityViewPoliticianAsCitizenBinding;
import com.example.transparency.databinding.ActivityViewProjectAsCitizenBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ViewPoliticianAsCitizen extends AppCompatActivity {

    private ActivityViewPoliticianAsCitizenBinding root;

    private Politicians politicians;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        root = ActivityViewPoliticianAsCitizenBinding.inflate(getLayoutInflater());
        setContentView(root.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        politicians = (Politicians) getIntent().getSerializableExtra("politicians");

        retrieve();

        Picasso.get()
                .load(politicians.getProfile())
                .resize(1024, 1024)
                .into(root.imgAccountProfile);

        root.txtFName.setText(politicians.getFirstName());
        root.txtMName.setText(politicians.getMiddleName());
        root.txtLName.setText(politicians.getLastName());


        root.editPosition.setText(politicians.getPosition());
        root.editBirthdate.setText(politicians.getBirthdate());
        root.editPhoneNumber.setText(politicians.getContactNum());

        root.editEmail.setText(politicians.getEmail());

        root.btnReport.setOnClickListener(v -> {
            startActivity(new Intent(this, Report.class));
            finish();
        });

        root.btnBack.setOnClickListener(v -> {
            startActivity(new Intent(this, CitizenDashboard.class));
            finish();
        });

    }

    public void retrieve() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("project");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Projects> projectsList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Projects projects = dataSnapshot.getValue(Projects.class);
                    if (projects != null) {
                        projectsList.add(projects);
                    }
                }

                ProjectAdapter adapter = new ProjectAdapter(ViewPoliticianAsCitizen.this, projectsList, new ProjectAdapter.ProjectClick() {
                    @Override
                    public void onClick(Projects projects) {
                        startActivity(new Intent(ViewPoliticianAsCitizen.this, ViewProjectAsCitizen.class)
                                .putExtra("projects",projects));
                    }
                });

                root.recView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });
    }

    public void onResume()
    {
        super.onResume();
        retrieve();
    }
}