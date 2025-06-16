package com.example.transparency.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.transparency.Database.Projects;
import com.example.transparency.databinding.CardCollaboratorsViewBinding;
import com.example.transparency.databinding.CardShowMyProjectBinding;
import com.squareup.picasso.Picasso;

import android.view.ViewGroup;

import java.util.List;


public class MyProjectAdapter extends RecyclerView.Adapter<MyProjectAdapter.ViewHolder>{

    private Context context;
    private List<Projects> projectsList;

    public MyProjectClick projectClick;

    public MyProjectAdapter(Context context, List<Projects> projectsList, MyProjectClick projectClick) {
        this.context = context;
        this.projectsList = projectsList;
        this.projectClick = projectClick;
    }

    public interface MyProjectClick{
        void onClick(Projects projects);
    }

    @NonNull
    @Override
    public MyProjectAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(CardShowMyProjectBinding.inflate(LayoutInflater.from(context),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyProjectAdapter.ViewHolder holder, int position) {
        Projects projects = projectsList.get(position);

        holder.root.txtProjectLocation.setText(projects.getProjectLocation());
        holder.root.txtProjectName.setText(projects.getProjectName());
        holder.root.txtProjectStatus.setText(projects.getStatus());
        holder.root.txtProjectBudget.setText(String.valueOf(projects.getBudget()));

        holder.itemView.setOnClickListener(view -> {
            projectClick.onClick(projects);
        });
    }

    @Override
    public int getItemCount() {
        return projectsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CardShowMyProjectBinding root;
        public ViewHolder(@NonNull CardShowMyProjectBinding root) {
            super(root.getRoot());
            this.root = root;
        }
    }
}
