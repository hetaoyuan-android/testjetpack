package com.yuan.testjetpack.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.yuan.testjetpack.R;
import com.yuan.testjetpack.databinding.ActivityViewBindingBinding;

public class ViewBindingActivity extends AppCompatActivity {

    private ActivityViewBindingBinding bindingBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindingBinding = ActivityViewBindingBinding.inflate(getLayoutInflater());
        setContentView(bindingBinding.getRoot());
        bindingBinding.textView.setText("ViewBindingTest");
    }
}