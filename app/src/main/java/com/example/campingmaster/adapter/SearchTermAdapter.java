package com.example.campingmaster.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.campingmaster.R;
import java.util.List;

public class SearchTermAdapter extends RecyclerView.Adapter<SearchTermAdapter.SearchTermViewHolder> {
    private List<SearchTerm> data; // 데이터 타입 변경

    public SearchTermAdapter(List<SearchTerm> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public SearchTermViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_serach_term, parent, false);
        return new SearchTermViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchTermViewHolder holder, int position) {
        SearchTerm searchTerm = data.get(position);
        // 순위와 검색어를 함께 표시
        String displayText = (position + 1) + ". " + searchTerm.getTerm();
        holder.textView.setText(displayText);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class SearchTermViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public SearchTermViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView_search_term);
        }
    }
}
