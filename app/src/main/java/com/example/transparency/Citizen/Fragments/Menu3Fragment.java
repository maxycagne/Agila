package com.example.transparency.Citizen.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.transparency.Adapter.ProjectAdapter;
import com.example.transparency.Citizen.Activities.ViewProjectAsCitizen;
import com.example.transparency.Database.Projects;
import com.example.transparency.R;
import com.example.transparency.databinding.FragmentMenu3Binding;
import com.example.transparency.databinding.FragmentPoliticianBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class Menu3Fragment extends Fragment {


    private FragmentMenu3Binding root;
    public Menu3Fragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = FragmentMenu3Binding.inflate(inflater,container,false);
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

        return root.getRoot();


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
                Toast.makeText(getContext(), "Invalid format", Toast.LENGTH_SHORT).show();
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

                ProjectAdapter adapter = new ProjectAdapter(getContext(), projectsList, new ProjectAdapter.ProjectClick() {
                    @Override
                    public void onClick(Projects projects) {
                        startActivity(new Intent(getContext(), ViewProjectAsCitizen.class)
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
        retrieve(root.editSearch.getText().toString());
    }
}