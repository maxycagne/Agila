package com.example.transparency.Citizen.Activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.transparency.Adapter.ProjectAdapter;
import com.example.transparency.Database.Citizen;
import com.example.transparency.Database.Politicians;
import com.example.transparency.Database.Projects;
import com.example.transparency.Login.LoginActivity;
import com.example.transparency.R;
import com.example.transparency.databinding.ActivityLoginBinding;
import com.example.transparency.databinding.ActivityViewOwnProfileBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewOwnProfile extends AppCompatActivity {

    ActivityViewOwnProfileBinding root;
    private Citizen citizens;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        root = ActivityViewOwnProfileBinding.inflate(getLayoutInflater());
        setContentView(root.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        citizens = (Citizen) getIntent().getSerializableExtra("citizens");

        // TODO

        root.btnlogout.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();

        });

        root.btnBack.setOnClickListener(v -> {
            startActivity(new Intent(this, CitizenDashboard.class));
            finish();
        });

    }




}