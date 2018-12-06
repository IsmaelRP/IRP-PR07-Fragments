package com.example.ismael.irp_pr07_fragments.ui.avatar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.example.ismael.irp_pr07_fragments.R;
import com.example.ismael.irp_pr07_fragments.data.AvatarDatabase;
import com.example.ismael.irp_pr07_fragments.data.local.model.Avatar;
import com.example.ismael.irp_pr07_fragments.databinding.FragmentAvatarBinding;
import com.example.ismael.irp_pr07_fragments.ui.activity.MainViewModel;
import com.example.ismael.irp_pr07_fragments.utils.ResourcesUtils;
import java.util.Objects;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class AvatarFragment extends Fragment {

    private MainViewModel viewModelAct;

    private Avatar avatar;

    private AvatarDatabase database;

    private AvatarFragmentViewModel viewModel;
    private FragmentAvatarBinding b;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(AvatarFragmentViewModel.class);
        final Observer<Avatar> observerAvatar = v -> updateActivityViewModel();
        viewModel.getAvatar().observe(this, observerAvatar);
        viewModelAct = ViewModelProviders.of(requireActivity()).get(MainViewModel.class);
        initViews();
        setupActionBar();
        setupToolbar();
    }

    private void setupActionBar() {
        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setIcon(R.drawable.ic_arrow_back_black_24dp);
        }
    }

    private void setupToolbar() {
        requireActivity().setTitle(AvatarFragment.class.getSimpleName());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        b = DataBindingUtil.inflate(inflater, R.layout.fragment_avatar, container, false);
        return b.getRoot();
    }

    private void initViews() {
        database = AvatarDatabase.getInstance();

        b.imgAvatar1.setOnClickListener(v -> configureAvatar(b.imgAvatar1));
        b.imgAvatar2.setOnClickListener(v -> configureAvatar(b.imgAvatar2));
        b.imgAvatar3.setOnClickListener(v -> configureAvatar(b.imgAvatar3));
        b.imgAvatar4.setOnClickListener(v -> configureAvatar(b.imgAvatar4));
        b.imgAvatar5.setOnClickListener(v -> configureAvatar(b.imgAvatar5));
        b.imgAvatar6.setOnClickListener(v -> configureAvatar(b.imgAvatar6));

        b.lblAvatar1.setOnClickListener(v -> configureAvatar(b.imgAvatar1));
        b.lblAvatar2.setOnClickListener(v -> configureAvatar(b.imgAvatar2));
        b.lblAvatar3.setOnClickListener(v -> configureAvatar(b.imgAvatar3));
        b.lblAvatar4.setOnClickListener(v -> configureAvatar(b.imgAvatar4));
        b.lblAvatar5.setOnClickListener(v -> configureAvatar(b.imgAvatar5));
        b.lblAvatar6.setOnClickListener(v -> configureAvatar(b.imgAvatar6));

        if (viewModelAct.getAvatarLiveData().getValue() == null){
            viewModelAct.setAvatar(database.getDefaultAvatar());
        }

        avatar = viewModelAct.getAvatarLiveData().getValue();
        selectCatSelected(avatar.getImageResId());
    }

    private void configureAvatar(View v) {
        setAvatar(v.getId());
        selectCatSelected(avatar.getImageResId());
        viewModel.setAvatar(avatar);
    }

    private void updateActivityViewModel(){
        viewModelAct.setAvatar(avatar);
    }

    private void setAvatar(int id) {
        switch (id) {
            case R.id.imgAvatar1:
                avatar = database.queryAvatar(1);
                break;
            case R.id.imgAvatar2:
                avatar = database.queryAvatar(2);
                break;
            case R.id.imgAvatar3:
                avatar = database.queryAvatar(3);
                break;
            case R.id.imgAvatar4:
                avatar = database.queryAvatar(4);
                break;
            case R.id.imgAvatar5:
                avatar = database.queryAvatar(5);
                break;
            case R.id.imgAvatar6:
                avatar = database.queryAvatar(6);
                break;
        }
        selectCatSelected(avatar.getImageResId());
    }

    private void selectCatSelected(int resId) {
        AvatarDatabase database = AvatarDatabase.getInstance();
        ImageView imgAvatars[] = new ImageView[6];
        int i = 1;

        imgAvatars[0] = b.imgAvatar1;
        imgAvatars[1] = b.imgAvatar2;
        imgAvatars[2] = b.imgAvatar3;
        imgAvatars[3] = b.imgAvatar4;
        imgAvatars[4] = b.imgAvatar5;
        imgAvatars[5] = b.imgAvatar6;

        for (ImageView image : imgAvatars) {
            if (resId == database.queryAvatar(i).getImageResId()) {
                selectImageView(image);
            } else {
                deselectImageView(image);
            }
            i++;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.mnuSelect) {
            returnAvatar(avatar);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void selectImageView(ImageView imageView) {
        imageView.setAlpha(ResourcesUtils.getFloat(Objects.requireNonNull(getContext()), R.dimen.avatar_selected_image_alpha));
    }

    private void deselectImageView(ImageView imageView) {
        imageView.setAlpha(ResourcesUtils.getFloat(Objects.requireNonNull(getContext()), R.dimen.avatar_deselect_image_alpha));
    }

    private void returnAvatar(Avatar avatar){
        viewModelAct.obtain = true;
        viewModelAct.setAvatar(avatar);
        getActivity().getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_avatar, menu);
    }

}
