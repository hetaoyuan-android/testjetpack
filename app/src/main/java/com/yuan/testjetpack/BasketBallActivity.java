package com.yuan.testjetpack;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import com.yuan.testjetpack.databinding.ActivityBasketBallBinding;
import com.yuan.testjetpack.databinding.ActivityMainBinding;

public class BasketBallActivity extends AppCompatActivity {

    MyBasketBallViewModel viewModel;
    ActivityBasketBallBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_basket_ball);
        viewModel = ViewModelProviders.of(this).get(MyBasketBallViewModel.class);
        binding.setData(viewModel);
        binding.setLifecycleOwner(this);
    }
}