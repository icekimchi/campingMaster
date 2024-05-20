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

    private List<CampingSiteDto> campingSites = new ArrayList<>();

    public CampingSiteAdapter(List<CampingSiteDto> campingSites) {
        this.campingSites = campingSites;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_camping_site, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CampingSiteDto site = campingSites.get(position);
        // Bind other fields...
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