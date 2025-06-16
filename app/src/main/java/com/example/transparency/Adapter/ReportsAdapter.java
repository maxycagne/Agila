package com.example.transparency.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.transparency.Database.RecordLogs;
import com.example.transparency.Database.ReportDetails;
import com.example.transparency.databinding.CardReportsBinding;

import java.util.List;

public class ReportsAdapter extends RecyclerView.Adapter<ReportsAdapter.ViewHolder> {

    private Context context;

    private List<ReportDetails> reportDetailsList;

    private ReportDetailsClick reportDetailsClick;

    public ReportsAdapter(Context context, List<ReportDetails> reportDetailsList, ReportDetailsClick reportDetailsClick) {
        this.context = context;
        this.reportDetailsList = reportDetailsList;
        this.reportDetailsClick = reportDetailsClick;
    }

    public interface ReportDetailsClick{
        void onClick(ReportDetails reportDetails);
    }
    @NonNull
    @Override
    public ReportsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(CardReportsBinding.inflate(LayoutInflater.from(context),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ReportsAdapter.ViewHolder holder, int position) {
        ReportDetails reportDetails = reportDetailsList.get(position);

        holder.root.txtCollaboratorName.setText(reportDetails.getReportedTo());
        holder.root.txtReportReason.setText(reportDetails.getTypeOfReport());

        holder.itemView.setOnClickListener(view -> {
            reportDetailsClick.onClick(reportDetails);
        });
    }

    @Override
    public int getItemCount() {
        return reportDetailsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CardReportsBinding root;
        public ViewHolder(@NonNull CardReportsBinding root) {
            super(root.getRoot());
            this.root = root;
        }
    }
}
