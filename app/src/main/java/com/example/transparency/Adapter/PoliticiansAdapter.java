package com.example.transparency.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.transparency.Database.Citizen;
import com.example.transparency.Database.Politicians;
import com.example.transparency.databinding.CardCollaboratorsViewBinding;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PoliticiansAdapter extends RecyclerView.Adapter<PoliticiansAdapter.ViewHolder> {

    private Context context;

    private List<Politicians> politiciansList;

    public PoliticiansClick politiciansClick;

    public PoliticiansAdapter(Context context, List<Politicians> politiciansList, PoliticiansClick politiciansClick) {
        this.context = context;
        this.politiciansList = politiciansList;
        this.politiciansClick = politiciansClick;
    }

    public interface PoliticiansClick{
        void onClick(Politicians politicians);
    }
    @NonNull
    @Override
    public PoliticiansAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(CardCollaboratorsViewBinding.inflate(LayoutInflater.from(context),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull PoliticiansAdapter.ViewHolder holder, int position) {
        Politicians politicians = politiciansList.get(position);
        Picasso.get()
                .load(politicians.getProfile())
                .resize(1024, 1024)
                .into(holder.root.imgAccountProfile);

        holder.root.txtLocationAndPositionLocation.setText(politicians.getAddress()+" "+politicians.getPosition());

        holder.root.txtCollaboratorName.setText(politicians.getFirstName()+" "+politicians.getMiddleName()+" "+ politicians.getLastName());


        holder.itemView.setOnClickListener(view -> {
            politiciansClick.onClick(politicians);
        });
    }

    @Override
    public int getItemCount() {
        return politiciansList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CardCollaboratorsViewBinding root;
        public ViewHolder(@NonNull CardCollaboratorsViewBinding root) {
            super(root.getRoot());
            this.root = root;
        }
    }
}
