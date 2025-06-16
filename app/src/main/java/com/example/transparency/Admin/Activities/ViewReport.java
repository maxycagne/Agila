package com.example.transparency.Admin.Activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.transparency.Database.Citizen;
import com.example.transparency.Database.ReportDetails;
import com.example.transparency.R;
import com.example.transparency.databinding.ActivityViewAllReportsBinding;
import com.example.transparency.databinding.ActivityViewReportBinding;
import com.squareup.picasso.Picasso;

public class ViewReport extends AppCompatActivity {

    private ActivityViewReportBinding root;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        root = ActivityViewReportBinding.inflate(getLayoutInflater());
        setContentView(root.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ReportDetails reportDetails = (ReportDetails) getIntent().getSerializableExtra("reportDetails");


        root.btnBack.setOnClickListener(v -> {
            finish();
        });

        root.txtCollaboratorName.setText(reportDetails.getReportedTo());

        root.editReportName.setText(reportDetails.getTypeOfReport());
        root.editReportDescription.setText(reportDetails.getTypeOfDescription());
        Picasso.get()
                .load(reportDetails.getImageEvidence())
                .resize(1024, 1024)
                .into(root.imgReportPic);

    }
}