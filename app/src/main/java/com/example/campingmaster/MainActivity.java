package com.example.campingmaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.campingmaster.adapter.ViewPager2Adapter;
import com.example.campingmaster.databinding.ActivityMainBinding;
import com.example.campingmaster.fragment.ChatFragment;
import com.example.campingmaster.fragment.InfoFragment;
import com.example.campingmaster.fragment.MainFragment;
import com.example.campingmaster.fragment.SearchFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

public class MainActivity extends AppCompatActivity
{
    private GoogleMap map;
    private ActivityMainBinding binding;
    private ViewPager2Adapter pagerAdapter;
    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private MainFragment mainFragment;
    private SearchFragment searchFragment;
    private ChatFragment chatFragment;
    private InfoFragment infoFragment;

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);

        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initFragment();
        setViewPager();
    }

    private void initFragment() {
        Log.i("INFO","MainActivity -> initUi()");
        mainFragment = new MainFragment();
        searchFragment = new SearchFragment();
        chatFragment = new ChatFragment();
        infoFragment = new InfoFragment();
    }

    private void setViewPager() {
        Log.i("INFO","MainActivity -> setViewPager()");
        pagerAdapter = new ViewPager2Adapter(this);

        pagerAdapter
                .addFragment(mainFragment)
                .addFragment(searchFragment)
                .addFragment(chatFragment)
                .addFragment(infoFragment);

        binding.pager.setAdapter(pagerAdapter);
        binding.pager.setUserInputEnabled(false);
        binding.pager.setOffscreenPageLimit(pagerAdapter.getItemCount());

        binding.pager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                binding.menuTab.selectTab(binding.menuTab.getTabAt(position));
            }
        });

        binding.menuTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        binding.pager.setCurrentItem(0);
                        break;
                    case 1:
                        binding.pager.setCurrentItem(1);
                        break;
                    case 2:
                        binding.pager.setCurrentItem(2);
                        break;
                    case 3:
                        binding.pager.setCurrentItem(3);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
}
}