package com.example.capsules;

import static com.example.capsules.Constants.REQ_GOOGLE_AUTH;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.capsules.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity implements UserAuthListener {

    ActivityLoginBinding binding;
    Authentication authentication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        authentication = new Authentication(this);

        ActivityResultLauncher<Intent> googleSignInLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult o) {
                authentication.handleActivityLaunchResult(REQ_GOOGLE_AUTH, o.getResultCode(), o.getData());
            }
        });

        binding.btnLogin.setOnClickListener(v -> {
            String email = binding.etUsernameEmail.getText().toString();
            String password = binding.etPassword.getText().toString();
            if (!email.isEmpty() && !password.isEmpty()) {
                authentication.signInWithEmailPassword(email, password, this);
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            }


        });

        binding.toSignupPage.setOnClickListener(v -> {
                    startActivity(new Intent(LoginActivity.this, SignupActivity.class));
                    finish();
                });
        
        binding.btnGoogleLogin.setOnClickListener(v -> {
            authentication.loginWithGoogle(googleSignInLauncher, LoginActivity.this);
        });


        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                startActivity(new Intent(LoginActivity.this,OnboardActivity.class));
                finish();
            }
        });
    }

    @Override
    public void onResult(UserAuthListener.AuthResult result) {
        if (result.isSuccess()) {
            // Authentication successful, proceed to next activity or perform necessary actions
            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        } else {
            // Authentication failed, show error message
            Toast.makeText(this, "Authentication failed: " + result.getErrorMsg(), Toast.LENGTH_SHORT).show();
        }
    }
}