package com.example.transparency.Politician.Activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.transparency.Citizen.Activities.ViewOwnProfile;
import com.example.transparency.Citizen.Fragments.HomeFragment;
import com.example.transparency.Citizen.Fragments.Menu3Fragment;
import com.example.transparency.Citizen.Fragments.PoliticianFragment;
import com.example.transparency.Database.Citizen;
import com.example.transparency.Database.Politicians;
import com.example.transparency.Politician.Fragments.HomePoliticianFragment;
import com.example.transparency.R;
import com.example.transparency.databinding.ActivityPoliticianDashboardBinding;

public class PoliticianDashboard extends AppCompatActivity {

    private ActivityPoliticianDashboardBinding root;

    public Politicians politicians;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        root= ActivityPoliticianDashboardBinding.inflate(getLayoutInflater());
        setContentView(root.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });
        politicians = (Politicians) getIntent().getSerializableExtra("politicians");

        root.btnAccount.setOnClickListener(v -> {
            startActivity(new Intent(this, ViewOwnProfile.class));
            finish();
        });

        ReplaceFragment(new HomePoliticianFragment());
        root.bottomNavBar.setOnItemSelectedListener(item ->{

            if(item.getItemId() == R.id.menu_home)
            {
                ReplaceFragment(new HomePoliticianFragment());
            }
            else if(item.getItemId() == R.id.menu_politicians)
            {
                ReplaceFragment(new PoliticianFragment());
            }
            else if(item.getItemId() == R.id.menu_reviewed)
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