package com.example.transparency.Admin.Activities;

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

import com.example.transparency.Admin.Fragments.HomeAdmin;
import com.example.transparency.Login.GovernmentLoginActivity;
import com.example.transparency.Login.LoginActivity;
import com.example.transparency.Politician.Fragments.HomePoliticianFragment;
import com.example.transparency.R;
import com.example.transparency.databinding.ActivityDashboardAdminBinding;

public class DashboardAdmin extends AppCompatActivity {

    private ActivityDashboardAdminBinding root;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        root = ActivityDashboardAdminBinding.inflate(getLayoutInflater());
        setContentView(root.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });
        ReplaceFragment(new HomeAdmin());
        root.bottomNavBar.setOnItemSelectedListener(item ->{

            if(item.getItemId() == R.id.menu_home)
            {
                ReplaceFragment(new HomeAdmin());
            }
            else if(item.getItemId() == R.id.menu_logout)
            {
                startActivity(new Intent(this, GovernmentLoginActivity.class));
                finish();
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