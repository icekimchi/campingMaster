package com.example.campingmaster;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campingmaster.adapter.CardViewAdapter;
import com.example.campingmaster.api.RetrofitClient;
import com.example.campingmaster.api.RetrofitService;
import com.example.campingmaster.api.gocamping.dto.CampingSiteDto;
import com.example.campingmaster.databinding.ToolbarBinding;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CampSiteResultActivity extends ToolBarActivity {
    private RecyclerView recyclerView;
    private RetrofitService service;
    private Spinner spinnerCategory, spinnerNature, spinnerTheme;
    private List<String> selectedCategories = new ArrayList<>();
    private List<String> selectedNatures = new ArrayList<>();
    private List<String> selectedThemes = new ArrayList<>();
    private ChipGroup chipGroupSelectedCategories;
    private String category;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campsite_result);

        Intent intent = getIntent();
        category = intent.getStringExtra("keyword");
        setupToolbar(category); // 툴바 설정

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        chipGroupSelectedCategories = findViewById(R.id.chipGroupSelectedCategories);

        // Get query and param from intent
        String sqlQuery = getIntent().getStringExtra("sqlQuery");
        String param = getIntent().getStringExtra("param");
        searchCategory(sqlQuery, param);

        recyclerView = findViewById(R.id.recyclerView);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        spinnerNature = findViewById(R.id.spinnerNature);
        spinnerTheme = findViewById(R.id.spinnerTheme);

        // 스피너 어댑터 설정
        setupSpinner(spinnerCategory, R.array.category_options, selectedCategories, "카테고리");
        setupSpinner(spinnerNature, R.array.nature_options, selectedNatures, "자연환경");
        setupSpinner(spinnerTheme, R.array.theme_options, selectedThemes, "테마");
    }

    private void setupSpinner(Spinner spinner, int arrayResId, List<String> selectedList, String defaultOption) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                arrayResId, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();

                // Check if selected item is not the default option and is not already in the list
                if (!selectedItem.equals(defaultOption) && !selectedList.contains(selectedItem)) {
                    selectedList.add(selectedItem);
                    addChip(selectedItem, selectedList);
                }
                filterData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void addChip(String text, List<String> selectedList) {
        Chip chip = new Chip(this);
        chip.setText(text);
        chip.setCloseIconVisible(true);
        chip.setOnCloseIconClickListener(view -> {
            selectedList.remove(text);
            chipGroupSelectedCategories.removeView(chip);
            filterData();
        });
        chipGroupSelectedCategories.addView(chip);
    }

    private void filterData() {
        // 필터 맵 생성
        Map<String, List<String>> filters = new HashMap<>();

        // 선택된 카테고리, 자연환경, 테마를 각각 추가
        if (!selectedCategories.isEmpty() && !selectedCategories.equals("카테고리")) {
            filters.put("category", new ArrayList<>(selectedCategories));
        }
        if (!selectedNatures.isEmpty() && !selectedNatures.equals("자연환경")) {
            filters.put("nature", new ArrayList<>(selectedNatures));
        }
        if (!selectedThemes.isEmpty()&& !selectedThemes.equals("테마")) {
            filters.put("theme", new ArrayList<>(selectedThemes));
        }

        Log.d("Filters", "Selected categories: " + selectedCategories);
        Log.d("Filters", "Selected categories: " + selectedNatures);
        Log.d("Filters", "Selected categories: " + selectedThemes);

        // 서버로 필터 요청
        sendFilterRequest(filters);
    }

    private void sendFilterRequest(Map<String, List<String>> filters) {
        service.searchWithFilters(filters).enqueue(new Callback<List<CampingSiteDto>>() {
            @Override
            public void onResponse(Call<List<CampingSiteDto>> call, Response<List<CampingSiteDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<CampingSiteDto> campingSites = response.body();
                    CardViewAdapter adapter = new CardViewAdapter(campingSites);
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(CampSiteResultActivity.this, "No data found for this filter", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<CampingSiteDto>> call, Throwable t) {
                Toast.makeText(CampSiteResultActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchCategory(String sqlQuery, String param) {
        service = RetrofitClient.getClient().create(RetrofitService.class);
        Map<String, Object> query = new HashMap<>();
        query.put("sqlQuery", sqlQuery);
        query.put("param", param);

        service.searchQuery(query).enqueue(new Callback<List<CampingSiteDto>>() {
            @Override
            public void onResponse(Call<List<CampingSiteDto>> call, Response<List<CampingSiteDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<CampingSiteDto> campingSites = response.body();
                    CardViewAdapter adapter = new CardViewAdapter(campingSites);
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(CampSiteResultActivity.this, "No data found for this category", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<CampingSiteDto>> call, Throwable t) {
                Toast.makeText(CampSiteResultActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}