package com.example.campingmaster;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import com.example.campingmaster.api.RetrofitClient;
import com.example.campingmaster.api.RetrofitService;
import com.example.campingmaster.api.member.dto.LogInRequestDto;
import com.example.campingmaster.api.member.dto.LogInResponseDto;
import com.example.campingmaster.databinding.ActivityLoginBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private TextView textView_signup;
    private Button btn_login;
    private EditText edit_login_id;
    private EditText edit_login_pw;
    private RetrofitService service;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.Base_Theme_CampingMaster);
        super.onCreate(savedInstanceState);
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        service = RetrofitClient.getClient().create(RetrofitService.class);

        textView_signup = binding.SignUp;
        btn_login = (Button)binding.LogInButton;
        edit_login_id = binding.loginID;
        edit_login_pw = binding.loginPW;

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String memberIdText = edit_login_id.getText().toString();
                String memberPwText = edit_login_pw.getText().toString();

                LogIn(new LogInRequestDto(memberIdText, memberPwText));
            }
        });


        textView_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });

    }

    private void LogIn(LogInRequestDto data){
        // enqueue()에 파라미터로 넘긴 콜백 - 통신이 성공/실패 했을 때 수행할 동작을 재정의
        service.userLogIn(data).enqueue(new Callback<LogInResponseDto>() {
            @Override
            public void onResponse(Call<LogInResponseDto> call, Response<LogInResponseDto> response) {
                LogInResponseDto result = response.body();
                Toast.makeText(LoginActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();

                if (result.getCode() == 200) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<LogInResponseDto> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "로그인 에러 발생", Toast.LENGTH_SHORT).show();
                Log.e("로그인 에러 발생", t.getMessage());
            }
        });
    }
}

