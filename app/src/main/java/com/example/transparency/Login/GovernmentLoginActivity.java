package com.example.transparency.Login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.transparency.Admin.Activities.DashboardAdmin;
import com.example.transparency.Database.Government;
import com.example.transparency.Database.Politicians;
import com.example.transparency.Politician.Activities.PoliticianDashboard;
import com.example.transparency.R;
import com.example.transparency.databinding.ActivityGovernmentLoginBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class GovernmentLoginActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private ActivityGovernmentLoginBinding root;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        root = ActivityGovernmentLoginBinding.inflate(getLayoutInflater());
        setContentView(root.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        root.btnLogin.setOnClickListener(view -> {
        login();
        });

        root.btnSwitchLogin1.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
        root.btnSwitchLogin2.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginPolitician.class));
            finish();
        });


    }

    public void login() {
        String enteredPhoneNum = "+63" + root.editPhoneNumber.getText().toString();
        String enteredPassword = root.editPassword.getText().toString();


        if (enteredPhoneNum.isEmpty() || enteredPassword.isEmpty()) {
            Toast.makeText(GovernmentLoginActivity.this, "Please enter phone number and password.", Toast.LENGTH_SHORT).show();
            return;
        }

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("gov");

        Query query = databaseReference.orderByChild("contactNum").equalTo(enteredPhoneNum);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getChildrenCount() > 0) {

                    DataSnapshot userSnapshot = snapshot.getChildren().iterator().next();
                    String storedPassword = userSnapshot.child("password").getValue(String.class);

                    if (storedPassword != null && storedPassword.equals(enteredPassword)) {
                        Toast.makeText(GovernmentLoginActivity.this, "Login Successfully.", Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(GovernmentLoginActivity.this, DashboardAdmin.class));

                        finish();
                    } else {
                        Toast.makeText(GovernmentLoginActivity.this, "Incorrect password.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(GovernmentLoginActivity.this, "Phone number not registered.", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(GovernmentLoginActivity.this, "Login failed. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }


}