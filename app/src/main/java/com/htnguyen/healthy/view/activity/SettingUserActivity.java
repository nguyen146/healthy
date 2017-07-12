package com.htnguyen.healthy.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.htnguyen.healthy.R;
import com.htnguyen.healthy.dialog.LoadingDialog;
import com.htnguyen.healthy.dialog.SelectImageDialog;
import com.htnguyen.healthy.model.User;
import com.htnguyen.healthy.util.Constants;
import com.htnguyen.healthy.util.Tools;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.htnguyen.healthy.dialog.SelectImageDialog.CAMERA_REQUEST;
import static com.htnguyen.healthy.dialog.SelectImageDialog.GALLERY_REQUEST;

public class SettingUserActivity extends BaseActivity {

    @BindView(R.id.imgPicture1)
    ImageView imageView;
    @BindView(R.id.imgPicture)
    CircleImageView userImgView;
    @BindView(R.id.edtPersonName)
    EditText nameView;
    @BindView(R.id.edtWeight)
    EditText weightView;
    @BindView(R.id.edtHeart)
    EditText heartView;
    @BindView(R.id.gender)
    RadioGroup genderView;
    @BindView(R.id.edtHeight)
    EditText heightView;
    @BindView(R.id.txt_email)
    TextView emailView;

    @BindView(R.id.tilHeart)
    TextInputLayout txtHeart;
    @BindView(R.id.tilWeight)
    TextInputLayout txtWeight;
    @BindView(R.id.tilPersonName)
    TextInputLayout txtName;
    @BindView(R.id.tilHeight)
    TextInputLayout txtHeight;

    @BindView(R.id.male)
    RadioButton maleView;
    @BindView(R.id.female)
    RadioButton femaleView;

    private LoadingDialog loadingDialog;

    private DatabaseReference mUser;
    private FirebaseAuth mAuth;

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, SettingUserActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);
        mAuth = FirebaseAuth.getInstance();
        mUser = FirebaseDatabase.getInstance().getReference().
                child(Constants.TABLE_USER).
                child(mAuth.getCurrentUser().getUid());
        loadingDialog = new LoadingDialog(this, getString(R.string.progressing));
        ButterKnife.bind(this);
    }

    public void onClickSelectPicture(View v) {
        SelectImageDialog dialogSelectImage = new SelectImageDialog(this,
                SettingUserActivity.this);
        dialogSelectImage.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUser();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Get result request code
        switch (requestCode) {
            case CAMERA_REQUEST:
                if (resultCode == RESULT_OK) {
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
//                    userImgView.setImageBitmap(photo);
                    imageView.setImageBitmap(photo);
                }
                break;

            case GALLERY_REQUEST:
                if (resultCode == RESULT_OK) {
                    Uri photo = data.getData();
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),
                                photo);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

//                    userImgView.setImageBitmap(bitmap);
                    imageView.setImageBitmap(bitmap);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.btnSave)
    public void OnSave() {
        int checkGender = genderView.getCheckedRadioButtonId();
        int gender;
        switch (checkGender) {
            case R.id.male:
                gender = 0;
                break;
            case R.id.female:
                gender = 1;
                break;
            default:
                gender = 0;
                break;
        }
        if (nameView.getText().toString().trim().equals("") || nameView.getText().toString().trim().length() > 20) {
            txtName.setError(getString(R.string.errUser));
            return;
        } else {
            txtName.setError(null);
        }
        loadingDialog.show();
        String userName = nameView.getText().toString().trim();
        int height = heightView.getText().toString().trim().equals("") ? 0 : Integer.parseInt(heightView.getText().toString().trim());
        int heart = heartView.getText().toString().trim().equals("") ? 0 : Integer.parseInt(heartView.getText().toString().trim());
        int weight = weightView.getText().toString().trim().equals("") ? 0 : Integer.parseInt(weightView.getText().toString().trim());
        if (imageView.getDrawable() != null){
            try {
                byte[] img = Tools.convertImageViewToByteArray(imageView);
                String image = Tools.byeArrayToString(img);
                mUser.child("image").setValue(image);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        mUser.child("userName").setValue(userName);
        mUser.child("gender").setValue(gender);
        mUser.child("heart").setValue(heart);
        mUser.child("height").setValue(height);
        mUser.child("weight").setValue(weight);
        mUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                loadingDialog.dismiss();
                Toast.makeText(SettingUserActivity.this, getString(R.string.saved), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                loadingDialog.dismiss();

            }
        });
    }

    public void loadUser() {
        mUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                try {
                    emailView.setText(String.valueOf(user.getUserEmail()));
                    nameView.setText(String.valueOf(user.getUserName()));
                    if (user.getGender() == 0) {
                        maleView.setChecked(true);
                    } else {
                        femaleView.setChecked(true);
                    }
                    heartView.setText(String.valueOf(user.getHeart()));
                    heightView.setText(String.valueOf(user.getHeight()));
                    weightView.setText(String.valueOf(user.getWeight()));

                    byte[] byte1 = Tools.stringToByteArray(user.getImage().trim());
                    Bitmap bitmap = Tools.convertByteArrayToBitmap(byte1);
                    userImgView.setImageBitmap(bitmap);
                    bitmap.recycle();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
