package com.example.campingmaster.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.campingmaster.CampingSiteDetailActivity;
import com.example.campingmaster.R;
import com.example.campingmaster.api.gocamping.dto.CampingSiteDto;

import java.util.ArrayList;
import java.util.List;

public class CardViewAdapter extends RecyclerView.Adapter<CardViewAdapter.CardViewHolder> {
    private List<CampingSiteDto> mCampsiteList;

    public CardViewAdapter(List<CampingSiteDto> campsiteList) {
        this.mCampsiteList = campsiteList != null ? campsiteList : new ArrayList<>();
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_campsite, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        holder.onBind(mCampsiteList.get(position));
    }

    @Override
    public int getItemCount() {
        return mCampsiteList.size();
    }

    class CardViewHolder extends RecyclerView.ViewHolder {
        ImageView imageArea;
        TextView titleArea;
        TextView categoryArea;
        TextView lineintroItem;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);

            imageArea = itemView.findViewById(R.id.imageArea);
            titleArea = itemView.findViewById(R.id.titleArea);
            categoryArea = itemView.findViewById(R.id.categoryArea);
            lineintroItem = itemView.findViewById(R.id.lineintro_item);
        }

        void onBind(CampingSiteDto item) {
            Context context = itemView.getContext();  // itemView에서 context를 가져옵니다.

            if (item.getImgUrl() != null) {
                Glide.with(context)
                        .load(item.getImgUrl())
                        .placeholder(R.drawable.logo_img) // 로딩 중 표시할 이미지
                        .error(R.drawable.logo_108x82) // 오류 시 표시할 이미지
                        .into(imageArea);
            } else {
                imageArea.setImageResource(R.drawable.logo_108x82);
            }

            titleArea.setText(item.getName() != null ? item.getName() : "Name not available");
            categoryArea.setText(item.getCategory() != null ? item.getCategory() : "Category not available");
            lineintroItem.setText(item.getDescription() != null ? item.getDescription() : "Description not available");

            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, CampingSiteDetailActivity.class);
                intent.putExtra("campingSite", item);
                context.startActivity(intent);
            });
        }
    }
}
