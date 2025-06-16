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

import com.example.transparency.Adapter.AccountAdapter;
import com.example.transparency.Adapter.ProjectAdapter;
import com.example.transparency.Citizen.Activities.ViewProjectAsCitizen;
import com.example.transparency.Database.Citizen;
import com.example.transparency.Database.Projects;
import com.example.transparency.R;
import com.example.transparency.databinding.ActivityViewAllAccountRequestsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewAllAccountRequests extends AppCompatActivity {

    private ActivityViewAllAccountRequestsBinding root;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        root = ActivityViewAllAccountRequestsBinding.inflate(getLayoutInflater());
        setContentView(root.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
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
        retrieve(root.editSearch.getText().toString());

        root.btnBack.setOnClickListener(v -> {
            startActivity(new Intent(this,DashboardAdmin.class));
            finish();
        });
    }

    public void retrieve(String search) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("citizen");
        Query query;
        if (search.isEmpty()) {
            query = databaseReference;
        } else {
            try {
                query = databaseReference.orderByChild("firstName").equalTo(search);
            } catch (Exception e) {
                Toast.makeText(this, "Invalid format", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Citizen> citizenList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Citizen citizen = dataSnapshot.getValue(Citizen.class);
                    if (citizen != null) {
                        citizenList.add(citizen);
                    }
                }

                AccountAdapter accountAdapter = new AccountAdapter(ViewAllAccountRequests.this, citizenList, new AccountAdapter.CitizenClick() {
                    @Override
                    public void onClick(Citizen citizen) {
                        startActivity(new Intent(ViewAllAccountRequests.this,ViewProfile.class)
                                .putExtra("citizen",citizen));
                    }
                });

                root.rv.setAdapter(accountAdapter);
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