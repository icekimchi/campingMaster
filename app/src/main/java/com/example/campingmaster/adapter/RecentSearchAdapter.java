package com.example.campingmaster.adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
//RecentSearchAdapter: RecyclerView에 최근 검색어 목록을 표시하는 어댑터- SearchFragment에서 사용됨
public class RecentSearchAdapter extends RecyclerView.Adapter<RecentSearchAdapter.ViewHolder> {
    private List<String> recentSearches;
    public RecentSearchAdapter(List<String> recentSearches) {
        this.recentSearches = recentSearches;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.recentSearchTextView.setText(recentSearches.get(position));
    }
    @Override
    public int getItemCount() {
        return recentSearches.size();
    }
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView recentSearchTextView;
        ViewHolder(View itemView) {
            super(itemView);
            recentSearchTextView = itemView.findViewById(android.R.id.text1);
        }
    }
    public void updateData(List<String> newRecentSearches) {
        recentSearches.clear();
        recentSearches.addAll(newRecentSearches);
        notifyDataSetChanged();
    }
}
