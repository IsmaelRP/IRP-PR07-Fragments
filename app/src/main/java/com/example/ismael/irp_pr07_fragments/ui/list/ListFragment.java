package com.example.ismael.irp_pr07_fragments.ui.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.ismael.irp_pr07_fragments.R;
import com.example.ismael.irp_pr07_fragments.data.UserDatabase;
import com.example.ismael.irp_pr07_fragments.data.local.model.User;
import com.example.ismael.irp_pr07_fragments.databinding.FragmentListBinding;
import com.example.ismael.irp_pr07_fragments.ui.activity.MainViewModel;
import com.example.ismael.irp_pr07_fragments.ui.user.UserFragment;
import com.example.ismael.irp_pr07_fragments.utils.FragmentUtils;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;

public class ListFragment extends Fragment {

    private FragmentListBinding b;
    private ListFragmentViewModel viewModel;
    private ListFragmentAdapter listAdapter;
    private MainViewModel viewModelAct;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        b = DataBindingUtil.inflate(inflater, R.layout.fragment_list, container, false);
        return b.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this, new ListFragmentViewModelFactory(UserDatabase.getInstance())).get(ListFragmentViewModel.class);
        viewModelAct = ViewModelProviders.of(requireActivity()).get(MainViewModel.class);
        setupToolbar();
        setupActionBar();
        setupRecyclerView();
        observeStudents();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        viewModelAct.fix = false;
    }

    private void setupToolbar() {
        requireActivity().setTitle(ListFragment.class.getSimpleName());
    }

    private void setupActionBar() {
        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
    }

    private void observeStudents() {
        viewModel.getUsers(false).observe(this, users -> {
            listAdapter.submitList(users);
            b.lblEmptyView.setVisibility(users.size() == 0 ? View.VISIBLE : View.INVISIBLE);
        });
    }

    private void setupRecyclerView() {

        View.OnClickListener listenerAdd;

        listAdapter = new ListFragmentAdapter(this::edit, position -> viewModel.deleteUser(listAdapter.getItem(position)));

        b.rvList.setHasFixedSize(true);
        b.rvList.setLayoutManager(new GridLayoutManager(getContext(), getResources().getInteger(R.integer.rvList_columns)));

        b.rvList.setItemAnimator(new DefaultItemAnimator());
        b.rvList.setAdapter(listAdapter);

        listenerAdd =  v -> addUser();
        b.fabAdd.setOnClickListener(listenerAdd);
        b.lblEmptyView.setOnClickListener(listenerAdd);

    }

    private void addUser() {
        viewModelAct.fix = true;
        viewModelAct.setUser(User.getDefaultUser());
    }

    private void edit(int position) {
        UserDatabase userDatabase = UserDatabase.getInstance();
        viewModelAct.fix = true;
        viewModelAct.setUser(userDatabase.getUsers().getValue().get(position));
    }

}
