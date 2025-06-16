package com.example.transparency.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.transparency.Database.Ratings;
import com.example.transparency.databinding.CardRatingsBinding;

import java.util.List;

public class RatingAdapter extends RecyclerView.Adapter<RatingAdapter.ViewHolder> {
    private Context context;

    private List<Ratings> ratingsList;

    private RatingClick ratingClick;

    public RatingAdapter(Context context, List<Ratings> ratingsList, RatingClick ratingClick) {
        this.context = context;
        this.ratingsList = ratingsList;
        this.ratingClick = ratingClick;
    }

    public interface RatingClick
    {
        void onClick(Ratings ratings);
    }

    @NonNull
    @Override
    public RatingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(CardRatingsBinding.inflate(LayoutInflater.from(context),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RatingAdapter.ViewHolder holder, int position) {
        Ratings rating = ratingsList.get(position);

        holder.root.txtRating.setText(rating.getRating());
        holder.root.txtRatingDescription.setText(rating.getMessage());

        holder.itemView.setOnClickListener(view -> {
            ratingClick.onClick(rating);
        });
    }

    @Override
    public int getItemCount() {
        return ratingsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CardRatingsBinding root;
        public ViewHolder(@NonNull CardRatingsBinding root) {
            super(root.getRoot());
            this.root = root;
        }
    }
}
