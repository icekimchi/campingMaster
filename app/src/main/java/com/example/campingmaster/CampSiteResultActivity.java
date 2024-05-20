package com.example.campingmaster;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campingmaster.adapter.CardViewAdapter;
import com.example.campingmaster.api.RetrofitClient;
import com.example.campingmaster.api.RetrofitService;
import com.example.campingmaster.api.gocamping.dto.CampingSiteDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CampSiteResultActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RetrofitService service;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campsite_result);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get query and param from intent
        String sqlQuery = getIntent().getStringExtra("sqlQuery");
        String param = getIntent().getStringExtra("param");
        searchCategory(sqlQuery, param);
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