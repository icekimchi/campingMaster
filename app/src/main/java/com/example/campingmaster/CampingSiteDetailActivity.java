package com.example.campingmaster;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.campingmaster.api.gocamping.dto.CampingSiteDto;

public class CampingSiteDetailActivity extends AppCompatActivity {
    private TextView nameTextView;
    private TextView featureTextView;
    private TextView addressTextView;
    private TextView descriptionTextView;
    private TextView phoneNumberTextView;
    private TextView homepageTextView;
    private TextView reserveTextView;
    private ImageView siteImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camping_site_detail);

        nameTextView = findViewById(R.id.nameTextView);
        featureTextView = findViewById(R.id.featureTextView);
        addressTextView = findViewById(R.id.addressTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);
        phoneNumberTextView = findViewById(R.id.phoneNumberTextView);
        homepageTextView = findViewById(R.id.homepageTextView);
        siteImageView = findViewById(R.id.siteImageView);
        reserveTextView = findViewById(R.id.reserveTextView);

        CampingSiteDto campingSite = (CampingSiteDto) getIntent().getSerializableExtra("campingSite");

        if (campingSite != null) {
            nameTextView.setText(campingSite.getName() != null ? campingSite.getName() : "Name not available");
            featureTextView.setText(campingSite.getFeatureNm() != null ? campingSite.getFeatureNm() : "Feature not available");
            addressTextView.setText(campingSite.getAddress() != null ? campingSite.getAddress() : "Address not available");
            descriptionTextView.setText(campingSite.getDescription() != null ? campingSite.getDescription() : "Description not available");
            phoneNumberTextView.setText(campingSite.getPhoneNumber() != null ? campingSite.getPhoneNumber() : "Phone number not available");
            homepageTextView.setText(campingSite.getHomepageUrl() != null ? campingSite.getHomepageUrl() : "Homepage not available");
            reserveTextView.setText(campingSite.getReserveUrl() != null ? campingSite.getHomepageUrl() : "ReserveURl not available");
            if (campingSite.getImgUrl() != null) {
                loadImageFromUrl(campingSite.getImgUrl(), siteImageView);
            } else {
                siteImageView.setImageResource(R.drawable.logo_108x82);
            }
        }
    }

    private void loadImageFromUrl(String imgUrl, ImageView imageView) {
        // Use your preferred image loading library (e.g., Glide, Picasso)
        Glide.with(this).load(imgUrl).into(imageView);
    }
}
