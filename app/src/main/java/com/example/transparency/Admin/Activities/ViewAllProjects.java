package com.example.transparency.Admin.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.transparency.Adapter.ProjectAdapter;
import com.example.transparency.Adapter.ProjectAdapter2;
import com.example.transparency.Citizen.Activities.ViewProjectAsCitizen;
import com.example.transparency.Database.Projects;
import com.example.transparency.R;
import com.example.transparency.databinding.ActivityViewAllProjectsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewAllProjects extends AppCompatActivity {

    private ActivityViewAllProjectsBinding root;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        root = ActivityViewAllProjectsBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(root.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        root.btnBack.setOnClickListener(v -> {
            startActivity(new Intent(this,DashboardAdmin.class));
            finish();
        });
        retrieve(root.editSearch.getText().toString());
        root.editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                retrieve(root.editSearch.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    public void retrieve(String search) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("project");
        Query query;
        if (search.isEmpty()) {
            query = databaseReference;
        } else {
            try {
                query = databaseReference.orderByChild("projectName").equalTo(search);
            } catch (Exception e) {
                Toast.makeText(this, "Invalid format", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Projects> projectsList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Projects projects = dataSnapshot.getValue(Projects.class);
                    if (projects != null) {
                        projectsList.add(projects);
                    }
                }

                ProjectAdapter2 adapter = new ProjectAdapter2(ViewAllProjects.this, projectsList, new ProjectAdapter2.ProjectClick() {
                    @Override
                    public void onClick(Projects projects) {

                    }
                });

                        root.rv.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });
    }

    public void onResume()
    {
        super.onResume();
        retrieve(root.editSearch.getText().toString());
    }

}