package com.example.lab6_iot_20200839;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.lab6_iot_20200839.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private Button iniciarMemoryClassic;
    private Button iniciarPuzzleSimplif;
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        iniciarMemoryClassic=findViewById(R.id.btnMemoryClass);
        iniciarPuzzleSimplif=findViewById(R.id.btnPuzzleSimplif);
        iniciarMemoryClassic.setOnClickListener(view ->  {

            Intent intent = new Intent(MainActivity.this, MemoryClassicActivity.class);
            startActivity(intent);

        });
        iniciarPuzzleSimplif.setOnClickListener(view ->  {

            Intent intent = new Intent(MainActivity.this, PuzzleSimplifiedActivity.class);
            startActivity(intent);

        });


    }
}