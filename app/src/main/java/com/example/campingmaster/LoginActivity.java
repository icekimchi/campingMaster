package com.example.campingmaster;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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



}

