package com.example.transparency.Admin.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.transparency.Adapter.MyProjectAdapter;
import com.example.transparency.Adapter.ProjectAdapter;
import com.example.transparency.Admin.Activities.CreateAccount;
import com.example.transparency.Admin.Activities.ViewAllAccountRequests;
import com.example.transparency.Admin.Activities.ViewAllAccounts;
import com.example.transparency.Admin.Activities.ViewAllProjects;
import com.example.transparency.Admin.Activities.ViewAllReports;
import com.example.transparency.Admin.Activities.ViewProjectsAsAdmin;
import com.example.transparency.Citizen.Activities.ViewProjectAsCitizen;
import com.example.transparency.Database.Projects;
import com.example.transparency.R;
import com.example.transparency.databinding.FragmentHomeAdminBinding;
import com.example.transparency.databinding.FragmentHomeBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class HomeAdmin extends Fragment {

    private FragmentHomeAdminBinding root;

    public HomeAdmin() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root = FragmentHomeAdminBinding.inflate(inflater,container,false);


        root.btnManageAccounts.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), ViewAllAccounts.class));
        });
        root.btnCreateAccounts.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), CreateAccount.class));
        });
        root.btnViewProjects.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), ViewAllProjects.class));
        });
        root.btnAccountRequests.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), ViewAllAccountRequests.class));
        });
        root.btnProjectRequests.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), ViewAllProjects.class));
        });
        root.btnReport.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), ViewAllReports.class));
        });

        root.btnFilter.setOnClickListener(v -> {
            if(root.layoutFilter.getVisibility() == View.GONE){
                root.layoutFilter.setVisibility(View.VISIBLE);
            } else {
                root.layoutFilter.setVisibility(View.GONE);
            }
        });

        String[] status ={"Pending","Active","Completed"};

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, status);
        root.editStatus.setAdapter(arrayAdapter);

        root.editStatus.setOnClickListener(view -> {
            root.editStatus.showDropDown();
        });

        retrieve();
        return root.getRoot();
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

                MyProjectAdapter adapter = new MyProjectAdapter(getContext(), projectsList, new MyProjectAdapter.MyProjectClick() {
                    @Override
                    public void onClick(Projects projects) {
                        startActivity(new Intent(getContext(), ViewProjectsAsAdmin.class)
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