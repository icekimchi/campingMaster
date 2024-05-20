package com.example.campingmaster.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campingmaster.adapter.CampingSiteAdapter;
import com.example.campingmaster.databinding.FragmentCampsiteResultBinding;

public class CampsiteResultFragment extends Fragment {
    private FragmentCampsiteResultBinding myBinding;
    private RecyclerView recyclerView;
    private CampingSiteAdapter campingSiteAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myBinding = FragmentCampsiteResultBinding.inflate(inflater);
        return myBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = myBinding.campsiteList;
        
    }

}
