package com.example.transparency.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.transparency.Database.Projects;
import com.example.transparency.Database.RecordLogs;
import com.example.transparency.databinding.CardRecordLogsBinding;

import java.util.List;

public class RecordLogsAdapter extends RecyclerView.Adapter<RecordLogsAdapter.ViewHolder> {

    private Context context;

    private List<RecordLogs> recordLogsList;

    private RecordLogsClick recordLogsClick;


    public RecordLogsAdapter(Context context, List<RecordLogs> recordLogsList, RecordLogsClick recordLogsClick) {
        this.context = context;
        this.recordLogsList = recordLogsList;
        this.recordLogsClick = recordLogsClick;
    }

    public interface RecordLogsClick{
        void onClick(RecordLogs recordLogs);
    }

    @NonNull
    @Override
    public RecordLogsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(CardRecordLogsBinding.inflate(LayoutInflater.from(context),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecordLogsAdapter.ViewHolder holder, int position) {
        RecordLogs recordLogs = recordLogsList.get(position);

        holder.root.txtByWhom.setText("Clarence Garcia");
        holder.root.txtDate.setText("May 15 2025");
        holder.root.txtStatus.setText(recordLogs.getStatus());
        holder.root.txtLogName.setText(String.valueOf(recordLogs.getRequestType()));

        holder.itemView.setOnClickListener(view -> {
            recordLogsClick.onClick(recordLogs);
        });
    }

    @Override
    public int getItemCount() {
        return recordLogsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CardRecordLogsBinding root;
        public ViewHolder(@NonNull CardRecordLogsBinding root) {
            super(root.getRoot());
            this.root = root;
        }
    }
}
