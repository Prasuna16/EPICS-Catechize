package com.example.epics.ui.discuss;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DiscussViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public DiscussViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is discuss fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}