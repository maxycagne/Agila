package com.example.transparency.Politician.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.health.connect.datatypes.Record;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.transparency.Database.Citizen;
import com.example.transparency.Database.Projects;
import com.example.transparency.Database.RecordLogs;
import com.example.transparency.R;
import com.example.transparency.databinding.ActivityCreateAccountBinding;
import com.example.transparency.databinding.ActivityLogRequestBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LogRequest extends AppCompatActivity {

    ActivityLogRequestBinding root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        root = ActivityLogRequestBinding.inflate(getLayoutInflater());
        setContentView(root.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String[] status ={"Withdraw Budget","Deposit Budget","Assign Budget"};

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, status);
        root.editRequestType.setAdapter(arrayAdapter);

        root.editRequestType.setOnClickListener(view -> {
            root.editRequestType.showDropDown();
        });


        //TODO: POPULATE AUTOCOMPLETE TEXT VIEW AS ("Withdraw Budget", "Deposit Budget", "Assign Budget")
        root.editRequestType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    //Add Budget
                    root.layoutMoneyPlaceholder.setVisibility(View.VISIBLE);
                    root.layoutSpendingRecord.setVisibility(View.GONE);
                } else if (position == 1) {
                    //Get Budget
                    root.layoutMoneyPlaceholder.setVisibility(View.VISIBLE);
                    root.layoutSpendingRecord.setVisibility(View.GONE);
                } else if (position == 2) {
                    //Assign Budget
                    root.layoutMoneyPlaceholder.setVisibility(View.GONE);
                    root.layoutSpendingRecord.setVisibility(View.VISIBLE);
                }
            }
        });

        root.btnBack.setOnClickListener(view -> {
            startActivity(new Intent(this,PoliticianDashboard.class));
            finish();
        });

        root.btnRequest.setOnClickListener(view -> {
            uploadLogs();
        });


    }

    private void uploadLogs(){


        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        String recordLogsID = databaseReference.push().getKey();


        String statusType = root.editRequestType.getText().toString();
        String requestAmmount = root.editMoneyPlaceholder.getText().toString();
        String spendingType = root.editSpendingType.getText().toString();
        String desc = root.editProjectDescription.getText().toString();
        String politicianID = "";
        String recordLogStatus = "Pending";
        String recordLogFeedback = "";
        String staus = "";
        RecordLogs recordLogs = new RecordLogs(recordLogsID,statusType,requestAmmount,
                spendingType,desc,politicianID,recordLogStatus,recordLogFeedback,staus);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Log Request").setMessage("Do you want to request this?").setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                databaseReference.child("recordLogs").child(recordLogsID).setValue(recordLogs)
                        .addOnSuccessListener(unused -> {
                            Toast.makeText(LogRequest.this, "Request send successfully", Toast.LENGTH_SHORT).show();
                            finish();})
                        .addOnFailureListener(e -> {
                            Toast.makeText(LogRequest.this, "Failed: " +e.getMessage(), Toast.LENGTH_SHORT).show();
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



    }


}