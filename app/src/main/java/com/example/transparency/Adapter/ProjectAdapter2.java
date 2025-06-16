package com.example.transparency.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.transparency.Database.Projects;
import com.example.transparency.databinding.CardShowProjectRequestsBinding;
import com.example.transparency.databinding.CardShowProjectsBinding;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProjectAdapter2 extends RecyclerView.Adapter<ProjectAdapter2.ViewHolder> {

    private Context context;
    private List<Projects> projectsList;

    public ProjectClick projectClick;

    public ProjectAdapter2(Context context, List<Projects> projectsList, ProjectClick projectClick) {
        this.context = context;
        this.projectsList = projectsList;
        this.projectClick = projectClick;
    }

    public interface ProjectClick{
        void onClick(Projects projects);
    }
    @NonNull
    @Override
    public ProjectAdapter2.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(CardShowProjectRequestsBinding.inflate(LayoutInflater.from(context),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectAdapter2.ViewHolder holder, int position) {
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
        private CardShowProjectRequestsBinding root;
        public ViewHolder(@NonNull CardShowProjectRequestsBinding root) {
            super(root.getRoot());
            this.root = root;
        }
    }
}
