package com.example.campingmaster.fragment;
import com.example.campingmaster.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.campingmaster.databinding.FragmentSearchBinding;
import com.example.campingmaster.adapter.SearchTermAdapter;
import com.example.campingmaster.adapter.SearchTerm;
import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {
    private FragmentSearchBinding myBinding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myBinding = FragmentSearchBinding.inflate(inflater, container, false);
        return myBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // SearchTerm 객체 리스트 초기화
        List<SearchTerm> searchData = new ArrayList<>();
        searchData.add(new SearchTerm("캠핑", 1));
        searchData.add(new SearchTerm("등산", 2));
        searchData.add(new SearchTerm("낚시", 3));
        // 기타 데이터 항목 추가...

        // 어댑터를 생성하고 SearchTerm 객체 리스트를 설정
        SearchTermAdapter adapter = new SearchTermAdapter(searchData);

        // RecyclerView의 레이아웃 매니저와 어댑터를 설정
        myBinding.recyclerViewRecentSearch.setLayoutManager(new LinearLayoutManager(getContext()));
        myBinding.recyclerViewRecentSearch.setAdapter(adapter);
    }
}
