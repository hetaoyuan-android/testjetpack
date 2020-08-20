package com.yuan.testjetpack;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.yuan.testjetpack.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ViewModelWithLiveData modelWithLiveData;
    TextView textView;
    ImageView imageButtonLike, imageButtonDisLike;
    ActivityMainBinding mainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        modelWithLiveData = ViewModelProviders.of(this).get(ViewModelWithLiveData.class);
        mainBinding.setData(modelWithLiveData);
        mainBinding.setLifecycleOwner(this);
        mainBinding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, BasketBallActivity.class));
            }
        });

//        textView = findViewById(R.id.textView);
//        imageButtonLike = findViewById(R.id.imageView);
//        imageButtonDisLike = findViewById(R.id.imageView2);
//
//        modelWithLiveData = ViewModelProviders.of(this).get(ViewModelWithLiveData.class);
//        modelWithLiveData.getLikedNumber().observe(this, new Observer<Integer>() {
//            @Override
//            public void onChanged(Integer integer) {
//                textView.setText(String.valueOf(integer));
//            }
//        });
//
//        imageButtonLike.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                modelWithLiveData.addLikedNumber(1);
//            }
//        });
//        imageButtonDisLike.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            modelWithLiveData.addLikedNumber(-1);
//            }
//        });
    }
}