package com.example.lab6_iot_20200839;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.lab6_iot_20200839.databinding.ActivityMainBinding;
import com.example.lab6_iot_20200839.databinding.ActivityMemoryClassicBinding;

public class MemoryClassicActivity extends AppCompatActivity {
    private ActivityMemoryClassicBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMemoryClassicBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }
}