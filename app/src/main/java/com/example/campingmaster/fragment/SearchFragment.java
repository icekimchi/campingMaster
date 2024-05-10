package com.example.campingmaster.fragment;

<<<<<<< HEAD
import com.example.campingmaster.CampSiteResultActivity;
import com.example.campingmaster.R;

import android.content.Intent;
=======
>>>>>>> 60f6660 ([FEAT] 탭레이아웃 추가)
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
<<<<<<< HEAD
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campingmaster.adapter.RecentSearchAdapter;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class SearchFragment extends Fragment {

    private static final String PREFS_NAME = "recent_searches";
    private static final String KEY_RECENT_SEARCHES = "key_recent_searches";
    private TextInputEditText searchEditText;
    private TextInputLayout textInputLayout;
    private RecyclerView recentSearchesRecyclerView;
    private RecyclerView popularSearchesRecyclerView;
    private LinearLayout recentSearchesLayout;
    private LinearLayout popularSearchesLayout;
    private RecentSearchAdapter recentSearchAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        searchEditText = view.findViewById(R.id.search_edit_text);
        textInputLayout = view.findViewById(R.id.textInputLayout);
        recentSearchesRecyclerView = view.findViewById(R.id.recyclerViewRecentSearch);
        popularSearchesRecyclerView = view.findViewById(R.id.recyclerViewPopularSearch);
        recentSearchesLayout = view.findViewById(R.id.layout_recent_searches);
        popularSearchesLayout = view.findViewById(R.id.layout_popular_searches);

        // RecyclerView 설정
        recentSearchesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recentSearchAdapter = new RecentSearchAdapter(new ArrayList<>());
        recentSearchesRecyclerView.setAdapter(recentSearchAdapter);

        textInputLayout.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = searchEditText.getText().toString();
                if (!query.isEmpty()) {
                    saveRecentSearch(query); // 최근 검색어 저장
                    Intent intent = new Intent(getActivity(), CampSiteResultActivity.class);
                    intent.putExtra("sqlQuery", "SELECT * FROM campsite WHERE address LIKE ?");
                    intent.putExtra("param", "%" + query + "%"); // query를 %로 감싸서 전달
                    startActivity(intent);
                }
            }
        });
        // 최근 검색어 로드
        loadRecentSearches();

        // 키보드가 열리거나 닫힐 때 이벤트 처리(키보드가 밑에 중첩되어서 설정해줌)
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = view.getRootView().getHeight() - view.getHeight();
                if (heightDiff > dpToPx(getContext(), 200)) { // 키보드가 열림
                    recentSearchesLayout.setVisibility(View.GONE);
                    popularSearchesLayout.setVisibility(View.GONE);
                } else { // 키보드가 닫힘
                    recentSearchesLayout.setVisibility(View.VISIBLE);
                    popularSearchesLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        return view;
    }

    private void performSearch(String query) {
        // 검색 로직 - 여기선 토스트 팝업창 설정
        Toast.makeText(getContext(), "검색어: " + query, Toast.LENGTH_SHORT).show();
        hideKeyboard();
    }

    private void saveRecentSearch(String query) {
        // SharedPreferences(안드로이드로 저장하는 것 .. 서버로 하기 전 임시로 해본것 ,,)
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Set<String> recentSearches = sharedPreferences.getStringSet(KEY_RECENT_SEARCHES, new HashSet<>());
        recentSearches.add(query);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(KEY_RECENT_SEARCHES, recentSearches);
        editor.apply();

        loadRecentSearches();
    }
    private void loadRecentSearches() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Set<String> recentSearches = sharedPreferences.getStringSet(KEY_RECENT_SEARCHES, new HashSet<>());
        recentSearchAdapter.updateData(new ArrayList<>(recentSearches));
    }
    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchEditText.getWindowToken(), 0);
    }
    private int dpToPx(Context context, int dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }
=======

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.campingmaster.databinding.FragmentMainBinding;
import com.example.campingmaster.databinding.FragmentSearchBinding;

public class SearchFragment extends Fragment {
    private FragmentSearchBinding myBinding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myBinding= FragmentSearchBinding.inflate(inflater);
        return myBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

>>>>>>> 60f6660 ([FEAT] 탭레이아웃 추가)
}
