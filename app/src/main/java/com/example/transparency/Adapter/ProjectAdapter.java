package com.example.transparency.Adapter;

import android.content.Context;
import android.view.ContentInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.transparency.Database.Projects;
import com.example.transparency.databinding.CardShowProjectsBinding;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ViewHolder> {

    private Context context;
    private List<Projects> projectsList;

    public ProjectClick projectClick;

    public ProjectAdapter(Context context, List<Projects> projectsList, ProjectClick projectClick) {
        this.context = context;
        this.projectsList = projectsList;
        this.projectClick = projectClick;
    }

    public interface ProjectClick{
        void onClick(Projects projects);
    }
    @NonNull
    @Override
    public ProjectAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(CardShowProjectsBinding.inflate(LayoutInflater.from(context),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectAdapter.ViewHolder holder, int position) {
        Projects projects = projectsList.get(position);

        Picasso.get()
                .load(projects.getProjectImage())
                .resize(1024, 1024)
                .into(holder.root.imgBanner);

        holder.root.txtProjectLocation.setText(projects.getProjectLocation());
        holder.root.txtProjectName.setText(projects.getProjectName());
        holder.root.txtProjectStatus.setText(projects.getStatus());
        holder.root.txtProjectRating.setText(String.valueOf(projects.getProjectRating()));

        holder.itemView.setOnClickListener(view -> {
            projectClick.onClick(projects);
        });
    }

    @Override
    public int getItemCount() {
        return projectsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CardShowProjectsBinding root;
        public ViewHolder(@NonNull CardShowProjectsBinding root) {
            super(root.getRoot());
            this.root = root;
        }
    }
}
