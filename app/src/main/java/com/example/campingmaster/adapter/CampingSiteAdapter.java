package com.example.campingmaster.adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campingmaster.R;
import com.example.campingmaster.api.gocamping.dto.CampingSiteDto;

import java.util.ArrayList;
import java.util.List;

public class CampingSiteAdapter extends RecyclerView.Adapter<CampingSiteAdapter.ViewHolder> {

    private List<CampingSiteDto> campingSites;

    public CampingSiteAdapter(List<CampingSiteDto> campingSites) {
        this.campingSites = campingSites;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_campsite_result, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CampingSiteViewHolder holder, int position) {
        CampingSiteDto campsite = campingSites.get(position);
        holder.itemTitle.setText(campsite.getSiteName());
        holder.itemDetail.setText(campsite.getLineIntro());
        Glide.with(context).load(campsite.getImgUrl()).into(holder.itemImage);
    }

    @Override
    public int getItemCount() {
        return campingSites.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        // Other views...

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize other views...
        }
    }
}