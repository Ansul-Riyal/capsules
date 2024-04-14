package com.example.capsules;

import static com.example.capsules.Constants.REQ_GOOGLE_AUTH;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.capsules.databinding.ActivitySignupBinding;

public class SignupActivity extends AppCompatActivity implements UserAuthListener {

    ActivitySignupBinding binding;
    Authentication authentication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        authentication = new Authentication(this);

        ActivityResultLauncher<Intent> googleSignInLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult o) {
                authentication.handleActivityLaunchResult(REQ_GOOGLE_AUTH, o.getResultCode(), o.getData());
            }
        });

        binding.toLoginPage.setOnClickListener(v -> {
                    startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                    finish();
                }
        );

        binding.btnSignup.setOnClickListener(v -> {
            String email = binding.etEmail.getText().toString();
            String password = binding.etPassword.getText().toString();
            String userName = binding.etUsername.getText().toString();

            if (!email.isEmpty() && !password.isEmpty() && !userName.isEmpty()) {
                authentication.signUpWithEmailPassword(userName, email, password, SignupActivity.this);
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnGoogleLogin.setOnClickListener(v -> {
            authentication.loginWithGoogle(googleSignInLauncher, SignupActivity.this);
        });

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                startActivity(new Intent(SignupActivity.this,OnboardActivity.class));
                finish();
            }
        });
    }

    @Override
    public void onResult(UserAuthListener.AuthResult result) {
        if (result.isSuccess()) {
            Toast.makeText(this, "Signup successful", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(SignupActivity.this, MainActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Signup failed: " + result.getErrorMsg(), Toast.LENGTH_SHORT).show();
        }
    }
}
