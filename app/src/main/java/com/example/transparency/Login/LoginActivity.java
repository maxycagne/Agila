package com.example.transparency.Login;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.transparency.Citizen.Activities.CitizenDashboard;
import com.example.transparency.Database.Citizen;
import com.example.transparency.R;
import com.example.transparency.databinding.ActivityLoginBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    private ActivityLoginBinding root;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        root = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(root.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        root.btnLogin.setOnClickListener(view -> {
            login();
        });
        root.btnRegister.setOnClickListener(view -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });

        root.btnSwitchLogin.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginPolitician.class));
            finish();
        });

        root.btnSwitchLoginAdmin.setOnClickListener(v -> {
            startActivity(new Intent(this, GovernmentLoginActivity.class));
            finish();
        });

    }

    public void login() {
        String enteredPhoneNum = "+63" + root.editPhoneNumber.getText().toString();
        String enteredPassword = root.editPassword.getText().toString();

        if (enteredPhoneNum.isEmpty() || enteredPassword.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Please enter phone number and password.", Toast.LENGTH_SHORT).show();
            return;
        }

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("citizen");

        Query query = databaseReference.orderByChild("phoneNumber").equalTo(enteredPhoneNum);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getChildrenCount() > 0) {


                    DataSnapshot userSnapshot = snapshot.getChildren().iterator().next();
                    String storedPassword = userSnapshot.child("password").getValue(String.class);

                    String accountStatus = userSnapshot.child("status").getValue(String.class);


                    if (storedPassword != null && storedPassword.equals(enteredPassword)) {
                        if (accountStatus != null && accountStatus.equalsIgnoreCase("pending")) {
                            new AlertDialog.Builder(LoginActivity.this)
                                    .setTitle("Account Pending")
                                    .setMessage("Your account is pending approval. Please wait for your account to be activated.")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_info)
                                    .show();

                        }
                        else if (accountStatus != null && accountStatus.equalsIgnoreCase("declined")) {
                            new AlertDialog.Builder(LoginActivity.this)
                                    .setTitle("Account Declined")
                                    .setMessage("Your request for account creation is declined. We're terribly sorry.")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_info)
                                    .show();

                        } else {
                            Toast.makeText(LoginActivity.this, "Login Successfully.", Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(LoginActivity.this,CitizenDashboard.class)
                                    .putExtra("citizen",userSnapshot.getValue(Citizen.class)));
                            finish();
                        }
                    } else {
                        // Password does not match
                        Toast.makeText(LoginActivity.this, "Incorrect password.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Phone number not found
                    Toast.makeText(LoginActivity.this, "Phone number not registered.", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle potential database errors
                Toast.makeText(LoginActivity.this, "Login failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}