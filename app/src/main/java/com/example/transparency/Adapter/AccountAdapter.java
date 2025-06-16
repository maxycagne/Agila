package com.example.transparency.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.transparency.Database.Citizen;
import com.example.transparency.databinding.CardCollaboratorsViewBinding;
import com.squareup.picasso.Picasso;

import android.content.Context;

import java.util.List;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.ViewHolder> {

    private Context context;

    private List<Citizen> citizenList;

    private CitizenClick citizenClick;

    public AccountAdapter(Context context, List<Citizen> citizenList, CitizenClick citizenClick) {
        this.context = context;
        this.citizenList = citizenList;
        this.citizenClick = citizenClick;
    }

    public interface CitizenClick
    {
        void onClick(Citizen citizen);
    }
    @NonNull
    @Override
    public AccountAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(CardCollaboratorsViewBinding.inflate(LayoutInflater.from(context),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AccountAdapter.ViewHolder holder, int position) {
        Citizen citizen = citizenList.get(position);
        Picasso.get()
                .load(citizen.getProfilePicture())
                .resize(1024, 1024)
                .into(holder.root.imgAccountProfile);

        holder.root.txtCollaboratorName.setText(citizen.getFirstName()+" "+citizen.getMiddleName()+" "+ citizen.getLastName());


        holder.itemView.setOnClickListener(view -> {
            citizenClick.onClick(citizen);
        });
    }

    @Override
    public int getItemCount() {
        return citizenList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CardCollaboratorsViewBinding root;
        public ViewHolder(@NonNull CardCollaboratorsViewBinding root) {
            super(root.getRoot());
            this.root = root;
        }
    }
}
