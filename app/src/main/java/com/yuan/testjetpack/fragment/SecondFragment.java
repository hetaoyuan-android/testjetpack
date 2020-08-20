package com.yuan.testjetpack.fragment;

import androidx.lifecycle.ViewModelProviders;

import android.animation.ObjectAnimator;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yuan.testjetpack.R;

public class SecondFragment extends Fragment {

    private SecondViewModel mViewModel;
    private ImageView imageView;

    public static SecondFragment newInstance() {
        return new SecondFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.second_fragment, container, false);
        imageView = view.findViewById(R.id.imageView);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(SecondViewModel.class);
        imageView.setScaleX(imageView.getScaleX() + mViewModel.scaleFactor);
        imageView.setScaleY(imageView.getScaleY() + mViewModel.scaleFactor);
        final ObjectAnimator objectAnimatorX = ObjectAnimator.ofFloat(imageView, "scaleX", 0, 0);
        final ObjectAnimator objectAnimatorY = ObjectAnimator.ofFloat(imageView, "scaleY", 0, 0);
        objectAnimatorX.setDuration(500);
        objectAnimatorY.setDuration(500);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!objectAnimatorX.isRunning()) {
                    mViewModel.scaleFactor += 0.1;
                    objectAnimatorX.setFloatValues(imageView.getScaleX() + 0.1f);
                    objectAnimatorY.setFloatValues(imageView.getScaleY() + 0.1f);
                    objectAnimatorX.start();
                    objectAnimatorY.start();
                }
            }
        });

    }

}