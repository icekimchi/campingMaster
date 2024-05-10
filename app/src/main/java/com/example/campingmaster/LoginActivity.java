package com.example.campingmaster;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import com.example.campingmaster.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private TextView textView_signup;                 // 회원가입 버튼
    private Button btn_login;                // 로그인 버튼
    private EditText edit_login_id;                // id 에디트
    private EditText edit_login_pw;                // pw 에디트


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        boolean isLoggedIn = checkLoginStatus();

        if (isLoggedIn) {
            // 로그인되어 있으면 MainActivity로 이동
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        } else {
            // 로그인되어 있지 않으면 LoginActivity에 남아있음
        }

        textView_signup = binding.SignUp;
        btn_login = (Button)binding.LogInButton;
        edit_login_id = binding.loginID;
        edit_login_pw = binding.loginPW;

        textView_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });

    }

    private boolean checkLoginStatus() {
        // 여기에 로그인 여부를 확인하는 로직을 구현
        // 예시로 항상 true 반환
        return false;
    }

}

