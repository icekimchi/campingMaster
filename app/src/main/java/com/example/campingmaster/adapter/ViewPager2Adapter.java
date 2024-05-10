package com.example.campingmaster.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.campingmaster.fragment.InfoFragment;
import com.example.campingmaster.fragment.MainFragment;
import com.example.campingmaster.fragment.ChatFragment;
import com.example.campingmaster.fragment.SearchFragment;

import java.util.ArrayList;

public class ViewPager2Adapter extends FragmentStateAdapter {
    private ArrayList<Fragment> fragmentList = new ArrayList<>();

    public ViewPager2Adapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getItemCount() {
        return fragmentList.size();
    }

    public ViewPager2Adapter addFragment(Fragment fragment) {
        fragmentList.add(fragment);
        return this;
    }

    public boolean hasFragment(Fragment fragment) {
        return fragmentList.contains(fragment);
    }
}
