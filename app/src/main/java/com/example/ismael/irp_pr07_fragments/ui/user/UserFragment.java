package com.example.ismael.irp_pr07_fragments.ui.user;

import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.ismael.irp_pr07_fragments.R;
import com.example.ismael.irp_pr07_fragments.data.AvatarDatabase;
import com.example.ismael.irp_pr07_fragments.data.UserDatabase;
import com.example.ismael.irp_pr07_fragments.data.local.model.Avatar;
import com.example.ismael.irp_pr07_fragments.data.local.model.User;
import com.example.ismael.irp_pr07_fragments.databinding.FragmentUserBinding;
import com.example.ismael.irp_pr07_fragments.ui.activity.MainViewModel;
import com.example.ismael.irp_pr07_fragments.utils.KeyboardUtils;
import com.example.ismael.irp_pr07_fragments.utils.SnackbarUtils;
import com.example.ismael.irp_pr07_fragments.utils.ValidationUtils;
import com.google.android.material.snackbar.Snackbar;
import java.util.Objects;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class UserFragment extends Fragment {

    private UserFragmentViewModel viewModel;
    private MainViewModel viewModelAct;

    private Avatar avatar;

    private static final String EXTRA_DISABLED = "EXTRA_DISABLED";

    private GenericTextWatcher nameWatcher;
    private GenericTextWatcher emailWatcher;
    private GenericTextWatcher phoneWatcher;
    private GenericTextWatcher addressWatcher;
    private GenericTextWatcher webWatcher;

    private boolean[] fieldsDisableds;
    private User user;

    private FragmentUserBinding b;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        b = DataBindingUtil.inflate(inflater, R.layout.fragment_user, container, false);
        return b.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        initViews();
        viewModel = ViewModelProviders.of(this).get(UserFragmentViewModel.class);
        final Observer<Avatar> observerAvatar = this::restoreStateAvatar;
        viewModel.getAvatar().observe(this, observerAvatar);
        viewModelAct = ViewModelProviders.of(requireActivity()).get(MainViewModel.class);

        if (savedInstanceState != null) {
            restoreDisableds(Objects.requireNonNull(savedInstanceState.getBooleanArray(EXTRA_DISABLED)));
        }

        checkDisableds();
        fillFields();
        setupToolbar();
        setupActionBar();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBooleanArray(EXTRA_DISABLED, fieldsDisableds);
        super.onSaveInstanceState(outState);
    }

    private void checkDisableds() {
        fieldsDisableds[0] = !b.includeForm.lblName.isEnabled();
        fieldsDisableds[1] = !b.includeForm.lblEmail.isEnabled();
        fieldsDisableds[2] = !b.includeForm.lblPhonenumber.isEnabled();
        fieldsDisableds[3] = !b.includeForm.lblAddress.isEnabled();
        fieldsDisableds[4] = !b.includeForm.lblWeb.isEnabled();
    }

    private void restoreDisableds(boolean[] disableds) {

        b.includeForm.lblName.setEnabled(!disableds[0]);

        b.includeForm.lblEmail.setEnabled(!disableds[1]);
        b.includeForm.imgEmail.setEnabled(!disableds[1]);

        b.includeForm.lblPhonenumber.setEnabled(!disableds[2]);
        b.includeForm.imgPhonenumber.setEnabled(!disableds[2]);

        b.includeForm.lblAddress.setEnabled(!disableds[3]);
        b.includeForm.imgAddress.setEnabled(!disableds[3]);

        b.includeForm.lblWeb.setEnabled(!disableds[4]);
        b.includeForm.imgWeb.setEnabled(!disableds[4]);
    }

    @Override
    public void onResume() {
        super.onResume();
        b.includeForm.txtName.addTextChangedListener(nameWatcher);
        b.includeForm.txtEmail.addTextChangedListener(emailWatcher);
        b.includeForm.txtPhonenumber.addTextChangedListener(phoneWatcher);
        b.includeForm.txtAddress.addTextChangedListener(addressWatcher);
        b.includeForm.txtWeb.addTextChangedListener(webWatcher);

        if (viewModelAct.obtain){
            avatar = viewModelAct.getAvatarLiveData().getValue();
            viewModelAct.obtain = false;
            viewModel.setAvatar(avatar);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        b.includeForm.txtName.removeTextChangedListener(nameWatcher);
        b.includeForm.txtEmail.removeTextChangedListener(emailWatcher);
        b.includeForm.txtPhonenumber.removeTextChangedListener(phoneWatcher);
        b.includeForm.txtAddress.removeTextChangedListener(addressWatcher);
        b.includeForm.txtWeb.removeTextChangedListener(webWatcher);
    }

    private void initViews() {
        b.imgAvatar.setOnClickListener(v -> openAvatars());

        b.lblAvatar.setOnClickListener(v -> openAvatars());

        b.includeForm.txtName.setOnFocusChangeListener((v, hasFocus) -> txtSwapBold(b.includeForm.lblName));

        b.includeForm.txtEmail.setOnFocusChangeListener((v, hasFocus) -> txtSwapBold(b.includeForm.lblEmail));

        b.includeForm.txtPhonenumber.setOnFocusChangeListener((v, hasFocus) -> txtSwapBold(b.includeForm.lblPhonenumber));

        b.includeForm.txtAddress.setOnFocusChangeListener((v, hasFocus) -> txtSwapBold(b.includeForm.lblAddress));

        b.includeForm.txtWeb.setOnFocusChangeListener((v, hasFocus) -> txtSwapBold(b.includeForm.lblWeb));

        b.includeForm.txtWeb.setOnEditorActionListener((v, actionId, event) -> {
            save();
            return true;
        });

        b.includeForm.imgEmail.setOnClickListener(v -> sendEmail());

        b.includeForm.imgPhonenumber.setOnClickListener(v -> dial());

        b.includeForm.imgAddress.setOnClickListener(v -> maps());

        b.includeForm.imgWeb.setOnClickListener(v -> searchWeb());

        nameWatcher = new GenericTextWatcher(b.includeForm.txtName);
        emailWatcher = new GenericTextWatcher(b.includeForm.txtEmail);
        phoneWatcher = new GenericTextWatcher(b.includeForm.txtPhonenumber);
        addressWatcher = new GenericTextWatcher(b.includeForm.txtAddress);
        webWatcher = new GenericTextWatcher(b.includeForm.txtWeb);

        fieldsDisableds = new boolean[5];
    }

    private void openAvatars() {
        viewModelAct.fix = true;
        viewModelAct.setAvatar(avatar);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        viewModelAct.fix = false;
    }

    private void showLabel(String text) {
        b.lblAvatar.setText(text);
    }

    private void restoreStateAvatar(Avatar newAvatar) {
        showAvatar(newAvatar);
        showLabel(newAvatar.getName());
    }

    private void fillFields() {
        AvatarDatabase avatarDatabase = AvatarDatabase.getInstance();
        if (viewModelAct.obtain){
            showAvatar(avatarDatabase.queryAvatar(viewModelAct.getAvatarLiveData().getValue().getId()));
            viewModelAct.obtain = false;
            viewModel.setAvatar(avatar);
        }else if (viewModelAct.fix){
            viewModelAct.fix = false;
            user = viewModelAct.getUserLiveData().getValue();
            viewModel.setAvatar(user.getAvatar());
        }

        if (user != null) {
            b.includeForm.txtName.setText(user.getName());
            b.includeForm.txtEmail.setText(user.getEmail());
            b.includeForm.txtPhonenumber.setText(user.getPhone());
            b.includeForm.txtAddress.setText(user.getAddress());
            b.includeForm.txtWeb.setText(user.getWeb());
            b.imgAvatar.setImageResource(user.getAvatar().getImageResId());
        }

    }

    private void showAvatar(Avatar newAvatar) {
        b.imgAvatar.setImageResource(newAvatar.getImageResId());
        b.imgAvatar.setTag(newAvatar.getImageResId());
        this.avatar = newAvatar;

    }

    private void sendEmail() {
        Intent intent;
        String address = b.includeForm.txtEmail.getText().toString();

        if (!isWrongEmail()) {
            intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:" + address));
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject));
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.email_text));
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                KeyboardUtils.hideSoftKeyboard(Objects.requireNonNull(getActivity()));
                SnackbarUtils.snackbar(b.includeForm.imgEmail, getString(R.string.error_email), Snackbar.LENGTH_SHORT);
            }
        } else {
           setErrorEmail(isWrongEmail());
        }
    }

    private void dial() {
        Intent intent;
        String phoneNumber = b.includeForm.txtPhonenumber.getText().toString();

        if (!isWrongPhonenumber()) {
            intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + phoneNumber));
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                KeyboardUtils.hideSoftKeyboard(Objects.requireNonNull(getActivity()));
                SnackbarUtils.snackbar(b.includeForm.imgPhonenumber, getString(R.string.error_phonenumber), Snackbar.LENGTH_SHORT);
            }
        } else {
            setErrorPhonenumber(isWrongPhonenumber());
        }
    }

    private void maps() {
        Intent intent;
        String address = b.includeForm.txtAddress.getText().toString();

        if (!isWrongAddress()) {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("geo:0,0?q=" + address));
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                KeyboardUtils.hideSoftKeyboard(Objects.requireNonNull(getActivity()));
                SnackbarUtils.snackbar(b.includeForm.imgAddress, getString(R.string.error_address), Snackbar.LENGTH_SHORT);
            }
        } else {
            setErrorAddress(isWrongAddress());
        }
    }

    private void searchWeb() {
        Intent intent;
        String web = b.includeForm.txtWeb.getText().toString();

        if (!isWrongWeb()) {
            if (web.substring(0, 8).matches("https://") || web.substring(0, 7).matches("http://")) {
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(web));
            } else {
                intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, web);
            }

            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                KeyboardUtils.hideSoftKeyboard(Objects.requireNonNull(getActivity()));
                SnackbarUtils.snackbar(b.includeForm.imgWeb, getString(R.string.error_web), Snackbar.LENGTH_SHORT);
            }
        } else {
            setErrorWeb(isWrongWeb());
        }
    }


    private void txtSwapBold(TextView txt) {
        if (txt.getTypeface().isBold()) {
            txt.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        } else {
            txt.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        }
    }

    private class GenericTextWatcher implements TextWatcher {

        private final View view;

        private GenericTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.lblName:
                    setErrorName(isWrongName());
                    break;
                case R.id.txtEmail:
                    setErrorEmail(isWrongEmail());
                    break;
                case R.id.txtPhonenumber:
                    setErrorPhonenumber(isWrongPhonenumber());
                    break;
                case R.id.txtAddress:
                    setErrorAddress(isWrongAddress());
                    break;
                case R.id.txtWeb:
                    setErrorWeb(isWrongWeb());
                    break;
            }

        }
    }

    private boolean isWrongName() {
        boolean isWrong = false;
       if (b.includeForm.txtName.getText().toString().length() <= 0) {
            isWrong = true;
        }
        fieldsDisableds[0] = isWrong;
        return isWrong;
    }

    private void setErrorName(boolean wrong) {
        if (wrong) {
           b.includeForm.txtName.setError((getString(R.string.main_invalid_data)));
           b.includeForm.lblName.setEnabled(false);
        } else {
           b.includeForm.txtName.setError(null);
            b.includeForm.lblName.setEnabled(true);
       }
    }

    private boolean isWrongEmail() {
        boolean isWrong = false;
       if (!ValidationUtils.isValidEmail(b.includeForm.txtEmail.getText().toString())) {
            isWrong = true;
       }
       fieldsDisableds[1] = isWrong;
        return isWrong;
    }

    private void setErrorEmail(boolean wrong) {
        if (wrong) {
            b.includeForm.txtEmail.setError((getString(R.string.main_invalid_data)));
            b.includeForm.imgEmail.setEnabled(false);
            b.includeForm.lblEmail.setEnabled(false);
        } else {
            b.includeForm.txtAddress.setError(null);
            b.includeForm.imgEmail.setEnabled(true);
            b.includeForm.lblEmail.setEnabled(true);
        }
    }

    private boolean isWrongPhonenumber() {
        boolean isWrong = false;
        if (!ValidationUtils.isValidPhone(b.includeForm.txtPhonenumber.getText().toString())) {
            isWrong = true;
        }
        fieldsDisableds[2] = isWrong;
        return isWrong;
    }

    private void setErrorPhonenumber(boolean wrong) {
        if (wrong) {
            b.includeForm.txtPhonenumber.setError((getString(R.string.main_invalid_data)));
            b.includeForm.imgPhonenumber.setEnabled(false);
           b.includeForm.lblPhonenumber.setEnabled(false);
        } else {
            b.includeForm.txtPhonenumber.setError(null);b.includeForm.imgPhonenumber.setEnabled(true);
            b.includeForm.lblPhonenumber.setEnabled(true);
        }
    }

    private boolean isWrongAddress() {
        boolean isWrong = false;
        if (b.includeForm.txtAddress.getText().toString().length() <= 0) {
            isWrong = true;
        }
        fieldsDisableds[3] = isWrong;
        return isWrong;
    }

    private void setErrorAddress(boolean wrong) {
        if (wrong) {
            b.includeForm.txtAddress.setError((getString(R.string.main_invalid_data)));
            b.includeForm.imgAddress.setEnabled(false);
            b.includeForm.lblAddress.setEnabled(false);
        } else {
            b.includeForm.txtAddress.setError(null);
            b.includeForm.imgAddress.setEnabled(true);
            b.includeForm.lblAddress.setEnabled(true);
        }
    }

    private boolean isWrongWeb() {
        boolean isWrong = false;
        if (!ValidationUtils.isValidUrl(b.includeForm.txtWeb.getText().toString())) {
            isWrong = true;
        }
        fieldsDisableds[4] = isWrong;
        return isWrong;
    }

    private void setErrorWeb(boolean wrong) {
        if (wrong) {
            b.includeForm.txtWeb.setError((getString(R.string.main_invalid_data)));
            b.includeForm.imgWeb.setEnabled(false);
            b.includeForm.lblWeb.setEnabled(false);
        } else {
            b.includeForm.txtWeb.setError(null);
            b.includeForm.imgWeb.setEnabled(true);
            b.includeForm.lblWeb.setEnabled(true);
        }
    }

    private void save() {
        UserDatabase userDatabase = UserDatabase.getInstance();
        boolean valid;
        KeyboardUtils.hideSoftKeyboard(Objects.requireNonNull(getActivity()));

        valid = isFormValid();

        if (!valid) {
            SnackbarUtils.snackbar(b.imgAvatar, getString(R.string.main_error_saving), Snackbar.LENGTH_SHORT);
        }else{
            updateUser();
            if (userDatabase.existUserById(user)){
                userDatabase.editUser(user);
            }else{
                userDatabase.addUser(user);
            }
            getActivity().getSupportFragmentManager().popBackStack();
        }

    }

    private void updateUser() {
        if (user == null){
            user = viewModelAct.getUserLiveData().getValue();
        }
        user.setAvatar(avatar);
        user.setName(b.includeForm.txtName.getText().toString());
        user.setEmail(b.includeForm.txtEmail.getText().toString());
        user.setPhone(b.includeForm.txtPhonenumber.getText().toString());
        user.setAddress(b.includeForm.txtAddress.getText().toString());
        user.setWeb(b.includeForm.txtWeb.getText().toString());

        viewModelAct.setUser(user);
    }

    private boolean isFormValid() {
        boolean valid = true;

        if (isWrongName()) {
            valid = false;
            setErrorName(isWrongName());
        }
        if (isWrongEmail()) {
            valid = false;
            setErrorEmail(isWrongEmail());
        }
        if (isWrongPhonenumber()) {
            valid = false;
            setErrorPhonenumber(isWrongPhonenumber());
        }
        if (isWrongAddress()) {
            valid = false;
            setErrorAddress(isWrongAddress());
        }

        if (isWrongWeb()) {
            valid = false;
            setErrorWeb(isWrongWeb());
        }

        return valid;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.mnuSave) {
            save();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupToolbar() {
        requireActivity().setTitle(this.getClass().getSimpleName());
    }

    private void setupActionBar() {
        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setIcon(R.drawable.ic_arrow_back_black_24dp);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_user, menu);
    }
}
