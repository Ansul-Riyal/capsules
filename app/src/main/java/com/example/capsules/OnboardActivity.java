package com.example.capsules;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.capsules.databinding.ActivityOnboardBinding;

public class OnboardActivity extends AppCompatActivity {

    ActivityOnboardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOnboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnGetStartedOnBoard.setOnClickListener(v ->
                startActivity(new Intent(OnboardActivity.this, SignupActivity.class))
        );
        binding.toLoginPage.setOnClickListener(v ->
                startActivity(new Intent(OnboardActivity.this, LoginActivity.class))
        );
    }

    public void closeScreen() {
        finish();
    }
}