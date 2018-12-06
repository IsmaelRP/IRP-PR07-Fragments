package com.example.ismael.irp_pr07_fragments.ui.activity;

import com.example.ismael.irp_pr07_fragments.data.local.model.Avatar;
import com.example.ismael.irp_pr07_fragments.data.local.model.User;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {

    private MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Avatar> avatarMutableLiveData = new MutableLiveData<>();
    public boolean fix = true;
    public boolean obtain = false;

    public LiveData<User> getUserLiveData() {
        return userMutableLiveData;
    }

    public void setUser(User user) {
        this.userMutableLiveData.setValue(user);
    }

    public LiveData<Avatar> getAvatarLiveData() {
        return avatarMutableLiveData;
    }

    public void setAvatar(Avatar avatar) {
        this.avatarMutableLiveData.setValue(avatar);
    }
}
