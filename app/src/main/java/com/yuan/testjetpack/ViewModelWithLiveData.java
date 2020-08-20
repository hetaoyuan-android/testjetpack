package com.yuan.testjetpack;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ViewModelWithLiveData extends ViewModel {

    private MutableLiveData<Integer> likedNumber;

//    ViewModelWithLiveData() {
//        likedNumber = new MutableLiveData<>();
//        likedNumber.setValue(0);
//    }

    public MutableLiveData<Integer> getLikedNumber() {
        if (likedNumber == null) {
            likedNumber = new MutableLiveData<>();
            likedNumber.setValue(0);
        }
        return likedNumber;
    }

    public void addLikedNumber(int n) {
        likedNumber.setValue(likedNumber.getValue() + n);
    }

}
