package com.example.ismael.irp_pr07_fragments.ui.user;

import com.example.ismael.irp_pr07_fragments.data.local.model.Avatar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class UserFragmentViewModel extends ViewModel {

    private MutableLiveData<Avatar> avatar;

    public LiveData<Avatar> getAvatar() {
        if (avatar == null) {
            avatar = new MutableLiveData<>();
        }
        return avatar;
    }

    public void setAvatar(Avatar avatar) {
        this.avatar.setValue(avatar);
    }

}
