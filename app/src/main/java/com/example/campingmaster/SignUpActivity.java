package com.example.campingmaster;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import com.example.campingmaster.api.RetrofitClient;
import com.example.campingmaster.api.RetrofitService;
import com.example.campingmaster.api.dto.SignUpRequestDto;
import com.example.campingmaster.api.dto.SignUpResponseDto;
import com.example.campingmaster.databinding.ActivitySignupBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SignUpActivity extends AppCompatActivity {
    private ActivitySignupBinding binding;
    private Button btn_signup;
    private EditText memberId;
    private EditText memberPw;
    private EditText email;
    private RetrofitService service;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        service = RetrofitClient.getClient().create(RetrofitService.class);

        btn_signup = binding.SignUpButton;
        memberId = binding.signupId;
        memberPw = binding.signupPw;
        email = binding.signupEmail;

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String memberIdText = memberId.getText().toString();
                String memberPwText = memberPw.getText().toString();
                String emailText = email.getText().toString();

                System.out.println(memberId.toString() + memberPw.toString() + email.toString());
                SignUp(new SignUpRequestDto(memberIdText, memberPwText, emailText));
            }
        });
    }

    private void SignUp(SignUpRequestDto data){
        // enqueue()에 파라미터로 넘긴 콜백 - 통신이 성공/실패 했을 때 수행할 동작을 재정의
        service.userSignUp(data).enqueue(new Callback<SignUpResponseDto>() {
            @Override
            public void onResponse(Call<SignUpResponseDto> call, Response<SignUpResponseDto> response) {
                SignUpResponseDto result = response.body();
                Toast.makeText(SignUpActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();

                if (result.getCode() == 200) {
                    finish();
                }
            }

            @Override
            public void onFailure(Call<SignUpResponseDto> call, Throwable t) {
                Toast.makeText(SignUpActivity.this, "회원가입 에러 발생", Toast.LENGTH_SHORT).show();
                Log.e("회원가입 에러 발생", t.getMessage());
            }
        });
    }
}
