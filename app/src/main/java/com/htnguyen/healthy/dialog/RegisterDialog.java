package com.htnguyen.healthy.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.util.Patterns;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.htnguyen.healthy.R;
import com.htnguyen.healthy.model.Category;
import com.htnguyen.healthy.model.User;
import com.htnguyen.healthy.util.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterDialog extends Dialog {

    private final FirebaseAuth mAuth;
    private final Activity mActivity;
    private String email;
    private String password;
    private String name;
    private LoadingDialog loadingDialog;
    private final DatabaseReference mDatabase;

    @BindView(R.id.name)
    EditText nameView;
    @BindView(R.id.email)
    EditText emailView;
    @BindView(R.id.password)
    EditText passwordView;
    @BindView(R.id.txt_email)
    TextInputLayout txtEmailView;
    @BindView(R.id.txt_password)
    TextInputLayout txtPasswordView;
    @BindView(R.id.txt_name)
    TextInputLayout txtNameView;
    @BindView(R.id.password2)
    EditText password2View;

    public RegisterDialog(@NonNull Context context, FirebaseAuth mAuth, Activity mActivity, DatabaseReference mDatabase) {
        super(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        this.mAuth = mAuth;
        this.mActivity = mActivity;
        this.mDatabase = mDatabase;
        this.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_register);
        ButterKnife.bind(this, getWindow().getDecorView());
        setCancelable(false);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loadingDialog = new LoadingDialog(mActivity, getContext().getString(R.string.createAccount));
    }

    @OnClick(R.id.create)
    public void Create() {
        if (!inputValidate()) return;
        if(!password2View.getText().toString().trim().equals(passwordView.getText().toString().trim())){
            txtPasswordView.setError(getContext().getString(R.string.err_password));
            return;
        }
        loadingDialog.show();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(mActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Authentication sucess.",
                                    Toast.LENGTH_SHORT).show();
                            createUser(mAuth.getCurrentUser());
                        } else {
                            // If sign in fails, display a message to the user.
                            try{
                                throw task.getException();
                            } catch(FirebaseAuthWeakPasswordException e) {

                            } catch(FirebaseAuthInvalidCredentialsException e) {

                            } catch(FirebaseAuthUserCollisionException e) {
                                txtEmailView.setError(getContext().getString(R.string.errEmail2));
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            Toast.makeText(getContext(), "Authentication failed." + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                            loadingDialog.dismiss();
                        }

                    }
                });
    }

    @OnClick(R.id.exit)
    public void exit() {
        this.dismiss();
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
        } else if (password.length() < 6 || password.length() > 12) {
            txtPasswordView.setError(getContext().getString(R.string.errPassword2));
            validate = false;
        } else {
            txtPasswordView.setError(null);
        }
        name = nameView.getText().toString().trim();
        if (name.isEmpty()) {
            txtNameView.setError(getContext().getString(R.string.errName));
            validate = false;
        } else {
            txtNameView.setError(null);
        }

        return validate;
    }

    private void createUser(FirebaseUser user) {
        User user1 = new User(user.getUid(), name, user.getEmail());
        DatabaseReference dataUser = mDatabase.child(Constants.TABLE_USER).child(user.getUid());
        dataUser.setValue(user1);
        dataUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                loadingDialog.dismiss();
                exit();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), "Create user failed" + databaseError.getCode(),
                        Toast.LENGTH_SHORT).show();

                loadingDialog.dismiss();
            }
        });
    }

    private void createCategory(DatabaseReference dataUser){
        Category category = new Category("Okhttp", "Khong co gi", "Weight");
        DatabaseReference dataCategory =  dataUser.child(Constants.TABLE_CATEGORY);
        dataCategory.push().setValue(category);
    }
}
