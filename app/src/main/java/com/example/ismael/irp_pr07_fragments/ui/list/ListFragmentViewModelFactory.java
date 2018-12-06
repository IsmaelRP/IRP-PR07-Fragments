package com.example.ismael.irp_pr07_fragments.ui.list;

import com.example.ismael.irp_pr07_fragments.data.UserDatabase;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

class ListFragmentViewModelFactory implements ViewModelProvider.Factory {

    private final UserDatabase database;

    public ListFragmentViewModelFactory(UserDatabase database) {
        this.database = database;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new ListFragmentViewModel(database);
    }
}
