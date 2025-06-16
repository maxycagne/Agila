package com.example.transparency.Citizen.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.transparency.Citizen.Fragments.HomeFragment;
import com.example.transparency.Citizen.Fragments.Menu3Fragment;
import com.example.transparency.Citizen.Fragments.PoliticianFragment;
import com.example.transparency.Database.Citizen;
import com.example.transparency.Database.Politicians;
import com.example.transparency.DialogsToActivities.Report;
import com.example.transparency.R;
import com.example.transparency.databinding.ActivityCitizenDashboardBinding;

public class CitizenDashboard extends AppCompatActivity {

    private ActivityCitizenDashboardBinding root;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        root = ActivityCitizenDashboardBinding.inflate(getLayoutInflater());
        setContentView(root.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        Citizen citizen = (Citizen) getIntent().getSerializableExtra("citizen");


        root.btnAccount.setOnClickListener(v -> {
            startActivity(new Intent(this, ViewOwnProfile.class)
                    .putExtra("citizen",citizen));
            finish();
        });

        ReplaceFragment(new HomeFragment());
        root.bottomNavBar.setOnItemSelectedListener(item ->{

            if(item.getItemId() == R.id.menu_home)
            {
                ReplaceFragment(new HomeFragment());
            }
            else if(item.getItemId() == R.id.menu_politicians)
            {
                ReplaceFragment(new PoliticianFragment());
            }
            else if(item.getItemId() == R.id.menu_menu3)
            {
                ReplaceFragment(new Menu3Fragment());
            }

            return true;
        });
    }

    public void ReplaceFragment(Fragment fragment)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
    }
}