package com.htnguyen.healthy.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.htnguyen.healthy.R;
import com.htnguyen.healthy.model.User;
import com.htnguyen.healthy.util.Constants;
import com.htnguyen.healthy.util.NetworkConnectionUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignInDialog extends Dialog {

    private final onLoginListener onLoginListener;
    private final Activity mActivity;
    private final DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private SharedPreferences.Editor editer;
    private SharedPreferences loginPreferences;
    private Boolean saveLogin;
    private String email;
    private String password;
    private LoadingDialog loadingDialog;

    @BindView(R.id.email)
    EditText emailView;
    @BindView(R.id.password)
    EditText passwordView;
    @BindView(R.id.txt_email)
    TextInputLayout txtEmailView;
    @BindView(R.id.txt_password)
    TextInputLayout txtPasswordView;
    @BindView(R.id.chbRemember)
    CheckBox chbRemember;

    public SignInDialog(@NonNull Context context, SignInDialog.onLoginListener onLoginListener,
                        Activity mActivity, FirebaseAuth mAuth, SharedPreferences loginPreferences,
                        DatabaseReference mDatabase) {
        super(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        this.onLoginListener = onLoginListener;
        this.mActivity = mActivity;
        this.mDatabase = mDatabase;
        this.loginPreferences = loginPreferences;
        this.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_sign_in);
        this.mAuth = mAuth;
        ButterKnife.bind(this, getWindow().getDecorView());
        setCancelable(false);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        saveLogin = this.loginPreferences.getBoolean("saveLogin", false);
        loadingDialog = new LoadingDialog(mActivity, getContext().getString(R.string.authenticating));
        if (saveLogin) {
            emailView.setText(this.loginPreferences.getString("username", ""));
            chbRemember.setChecked(true);
        }
    }

    @OnClick(R.id.btn_register)
    public void showRegisterDialog() {
        new RegisterDialog(getContext(), mAuth, mActivity, mDatabase).show();
    }

    @OnClick(R.id.exit)
    public void exit() {
        onLoginListener.onCancelLogin();
        this.dismiss();
    }

    @OnClick(R.id.signIn)
    public void Login() {
        if (!inputValidate()) return;
        if(!checkConnection()) return;
        loadingDialog.show();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(mActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            getUserName(mAuth.getCurrentUser().getUid());
                        } else {
                            Toast.makeText(getContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            loadingDialog.dismiss();
                        }
                    }
                });

    }

    private boolean inputValidate() {
        boolean validate = true;
        email = emailView.getText().toString().trim();
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            txtEmailView.setError(getContext().getString(R.string.errEmail));
            validate = false;
        } else {
            txtEmailView.setError(null);
        }
        password = passwordView.getText().toString().trim();
        if (password.isEmpty()) {
            txtPasswordView.setError(getContext().getString(R.string.errPassword));
            validate = false;
        }else {
            txtPasswordView.setError(null);
        }
        return validate;
    }
    public interface onLoginListener {
        void onLoginSuccess(FirebaseUser firebaseUser);
        void onCancelLogin();
    }

    private void getUserName(String id){
        final DatabaseReference getUser = mDatabase.child(Constants.TABLE_USER).child(id);
        getUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (chbRemember.isChecked()) {
                    editer = loginPreferences.edit();
                    editer.putString("username", user.getUserName());
                    editer.putBoolean("saveLogin", true);
                    editer.apply();
                } else {
                    editer = loginPreferences.edit();
                    editer.clear();
                    editer.commit();
                }
                onLoginListener.onLoginSuccess(mAuth.getCurrentUser());
                loadingDialog.dismiss();
                dismiss();
                Toast.makeText(getContext(), "Authentication success.",
                        Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), "Get user name error.",
                        Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
            }
        });
    }

    //Check connecttion
    private boolean checkConnection() {
        if(!NetworkConnectionUtil.isConnectedToMobileNetwork(getContext()) && !NetworkConnectionUtil.isConnectedToWifi(getContext())
                && !NetworkConnectionUtil.isConnectedToInternet(getContext())){
            showSnack();
            return false;
        }
        return true;
    }

    //Show snack bar
    private void showSnack() {
        Snackbar snackbar = Snackbar
                .make(getWindow().getDecorView(), getContext().getString(R.string.errConnection), Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.
                snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.show();
    }
}
