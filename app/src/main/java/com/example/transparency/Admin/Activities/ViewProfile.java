package com.example.transparency.Admin.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.transparency.Database.Citizen;
import com.example.transparency.Login.RegisterActivity;
import com.example.transparency.R;
import com.example.transparency.databinding.ActivityViewProfileBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class ViewProfile extends AppCompatActivity {

    private ActivityViewProfileBinding root;

    private Citizen citizen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        root = ActivityViewProfileBinding.inflate(getLayoutInflater());
        setContentView(root.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

         citizen = (Citizen) getIntent().getSerializableExtra("citizen");
        Picasso.get()
                .load(citizen.getBackImage())
                .resize(1024, 1024)
                .into(root.imgBackId);
        Picasso.get()
                .load(citizen.getFrontImage())
                .resize(1024, 1024)
                .into(root.imgFrontId);

        root.editSelectId.setText(citizen.getTypeOfId());
        root.editEmail.setText(citizen.getEmail());
        root.editPhoneNumber.setText(citizen.getPhoneNumber());
        root.editBirthdate.setText(citizen.getBirthDate());


        Picasso.get()
                .load(citizen.getProfilePicture())
                .resize(1024, 1024)
                .into(root.imgAccountProfile);


        root.txtFName.setText(citizen.getFirstName());

        root.txtMName.setText(citizen.getMiddleName());

        root.txtLName.setText(citizen.getLastName());

        root.txtAddress.setText(citizen.getAddress());


        root.btnAccept.setOnClickListener(v ->{
            approved();
            root.layoutButtons.setVisibility(View.GONE);
        });

        root.btnDecline.setOnClickListener(view -> {
            declined();
            root.layoutButtons.setVisibility(View.GONE);
        });

        root.btnBack.setOnClickListener(v -> {
            finish();
        });


    }

    public void declined()
    {
        Toast.makeText(ViewProfile.this, "Please wait for a while thank you", Toast.LENGTH_LONG).show();

            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = firebaseDatabase.getReference("citizen");
            Map<String, Object> objectMap = new HashMap<>();
            objectMap.put("status", "Declined");

            databaseReference.child(citizen.getCitizenId()).updateChildren(objectMap)
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(this, "You declined the account!", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Failed to update: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show();

                    });





    }

    public void approved()
    {
        Toast.makeText(ViewProfile.this, "Please wait for a while thank you", Toast.LENGTH_LONG).show();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("citizen");
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("status", "Activated");

        databaseReference.child(citizen.getCitizenId()).updateChildren(objectMap)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "You activeted the account!", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to update: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();

                });


    }
}