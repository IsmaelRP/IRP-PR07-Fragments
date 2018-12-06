package com.example.ismael.irp_pr07_fragments.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import com.example.ismael.irp_pr07_fragments.R;
import com.example.ismael.irp_pr07_fragments.ui.avatar.AvatarFragment;
import com.example.ismael.irp_pr07_fragments.ui.list.ListFragment;
import com.example.ismael.irp_pr07_fragments.ui.user.UserFragment;
import com.example.ismael.irp_pr07_fragments.utils.FragmentUtils;

public class MainActivity extends AppCompatActivity {

    MainViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vm = ViewModelProviders.of(this).get(MainViewModel.class);

        if (getSupportFragmentManager().findFragmentByTag(ListFragment.class.getSimpleName()) == null) {
            FragmentUtils.replaceFragment(getSupportFragmentManager(), R.id.flContent, new ListFragment(), ListFragment.class.getSimpleName());
        }

        vm.getUserLiveData().observe(this, user -> replaceListToUser());
        vm.getAvatarLiveData().observe(this, avatar -> replaceUserToAvatar());
    }


    public void replaceListToUser(){
        if (vm.fix && getSupportFragmentManager().findFragmentByTag(UserFragment.class.getSimpleName()) == null){
            FragmentUtils.replaceFragmentAddToBackstack(getSupportFragmentManager(), R.id.flContent, new UserFragment(), UserFragment.class.getSimpleName(), UserFragment.class.getSimpleName(), FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        }
    }
    

    public void replaceUserToAvatar(){
        if (vm.fix && getSupportFragmentManager().findFragmentByTag(AvatarFragment.class.getSimpleName()) == null){
            FragmentUtils.replaceFragmentAddToBackstack(getSupportFragmentManager(), R.id.flContent, new AvatarFragment(), AvatarFragment.class.getSimpleName(), AvatarFragment.class.getSimpleName(), FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
