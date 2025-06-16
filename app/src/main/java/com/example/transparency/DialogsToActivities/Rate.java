package com.example.transparency.DialogsToActivities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.transparency.Citizen.Activities.CitizenDashboard;
import com.example.transparency.Database.Ratings;
import com.example.transparency.Database.ReportDetails;
import com.example.transparency.R;
import com.example.transparency.databinding.ActivityRateBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Rate extends AppCompatActivity {

    private ActivityRateBinding root;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        root = ActivityRateBinding.inflate(getLayoutInflater());
        setContentView(root.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String[] rate ={"⭐","⭐⭐","⭐⭐⭐","⭐⭐⭐⭐","⭐⭐⭐⭐⭐"};

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, rate);
        root.editRating.setAdapter(arrayAdapter);

        root.editRating.setOnClickListener(view -> {
            root.editRating.showDropDown();
        });

        root.btnBack.setOnClickListener(v -> {
            startActivity(new Intent(this, CitizenDashboard.class));
            finish();
        });

        root.btnCancel.setOnClickListener(v -> {
            startActivity(new Intent(this, CitizenDashboard.class));
            finish();
        });

        root.btnRate.setOnClickListener(v -> {
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = firebaseDatabase.getReference();
            String rateId = databaseReference.push().getKey();
            if (! validate())
            {
                return;
            }
            String rating = root.editRating.getText().toString();
            String ratingByID = "ODasdDSdasa";
            String ratingToI ="OQdgfdfSSdasa";
            String message = root.editReportMessage.getText().toString();


            Ratings ratings = new Ratings(rateId,rating,ratingByID,ratingToI,message);




            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Rating ").setMessage("Would you like to rate this?").setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    databaseReference.child("rate").child(rateId).setValue(ratings)
                            .addOnSuccessListener(unused -> {
                                Toast.makeText(Rate.this, "Rate sent successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Rate.this, CitizenDashboard.class));
                                finish();})
                            .addOnFailureListener(e -> {
                                Toast.makeText(Rate.this, "Failed: " +e.getMessage(), Toast.LENGTH_SHORT).show();
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
        });

    }

    public boolean validate()
    {
        if(root.editRating.getText().toString().equals(""))
        {
            root.layoutRating.setErrorEnabled(true);
            root.layoutRating.setError("Please fill this field");
            return false;
        }

        if(root.editReportMessage.getText().toString().equals(""))
        {
            root.layoutReportMessage.setErrorEnabled(true);
            root.layoutReportMessage.setError("Please fill this field");
            return false;
        }

        return true;
    }
}