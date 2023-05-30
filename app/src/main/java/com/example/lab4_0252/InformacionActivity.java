package com.example.lab4_0252;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lab4_0252.databinding.ActivityInformacionBinding;

public class InformacionActivity extends AppCompatActivity {

    ActivityInformacionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInformacionBinding.inflate((getLayoutInflater()));
        setContentView(binding.getRoot());


    }
}
