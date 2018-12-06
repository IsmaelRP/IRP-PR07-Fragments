package com.example.ismael.irp_pr07_fragments.ui.list;


import com.example.ismael.irp_pr07_fragments.data.UserDatabase;
import com.example.ismael.irp_pr07_fragments.data.local.model.User;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class ListFragmentViewModel extends ViewModel {

    private final UserDatabase database;

    private LiveData<List<User>> users;

    public ListFragmentViewModel(UserDatabase database) {
        this.database = database;
    }

    public LiveData<List<User>> getUsers(boolean forceLoad) {
        if (users == null || forceLoad) {
            users = database.getUsers();
        }
        return users;
    }

    public void deleteUser(User user) {
        database.deleteUser(user);
    }

    public void editUser(User user) {
        database.editUser(user);

    }

    public void addUser(User user) {
        database.addUser(user);
    }
}
