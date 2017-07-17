package com.htnguyen.healthy.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.htnguyen.healthy.R;
import com.htnguyen.healthy.dialog.ConfirmDialog;
import com.htnguyen.healthy.dialog.DialogResult;
import com.htnguyen.healthy.dialog.SignInDialog;
import com.htnguyen.healthy.model.Category;
import com.htnguyen.healthy.model.User;
import com.htnguyen.healthy.presenter.HealthyPresenter;
import com.htnguyen.healthy.util.Constants;
import com.htnguyen.healthy.util.NetworkConnectionUtil;
import com.htnguyen.healthy.util.Tools;
import com.htnguyen.healthy.view.HealthyView;
import com.htnguyen.healthy.view.fragment.MainFragment;
import com.htnguyen.healthy.view.fragment.TimerFragment;
import com.htnguyen.healthy.view.fragment.TrackerFragment;
import com.htnguyen.healthy.view.fragment.UserFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.htnguyen.healthy.R.id.txt_user_name;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, HealthyView,
        SignInDialog.onLoginListener, View.OnClickListener ,
        ConfirmDialog.OnConfirmListener{

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private SharedPreferences.Editor editer;
    private SharedPreferences loginPreferences;
    TextView userNameView;
    TextView emailUserView;
    ImageView imgUserView;
    MenuItem navSignOut;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    @Inject
    HealthyPresenter healthyPresenter = new HealthyPresenter();

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        this.healthyPresenter.setView(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Menu menu = navigationView.getMenu();
        navSignOut = menu.findItem(R.id.nav_logOut);
        View header = navigationView.getHeaderView(0);
        userNameView = (TextView)header.findViewById(txt_user_name);
        emailUserView = (TextView)header.findViewById(R.id.email_user);
        imgUserView = (ImageView)header.findViewById(R.id.img_user);
        userNameView.setOnClickListener(this);
        emailUserView.setOnClickListener(this);
        imgUserView.setOnClickListener(this);
        ButterKnife.bind(this);
        fab.hide();
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        //Setting
        mDatabase = FirebaseDatabase.getInstance().getReference();
        if(!loginPreferences.getBoolean("saveLogin", false)){
            try {
                mAuth.signOut();
            }catch (NullPointerException e){
                e.printStackTrace();
            }
            mAuth = null;
            userNameView.setText(getResources().getString(R.string.offline));
            imgUserView.setImageBitmap(null);
            emailUserView.setText("");
        }
        mAuth = FirebaseAuth.getInstance();
//        initHome();
        replaceFragment(R.id.content_main, new MainFragment());
    }

    @Override
    public void onClick(View v) {
        if(!loginPreferences.getString("username", "").equals("")){
        navigator.navigateToSettingUser(MainActivity.this);
        }else {
            Toast.makeText(MainActivity.this, getString(R.string.errSetting), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        healthyPresenter.resume();
        if(loginPreferences.getString("username", "").equals("")){
            navSignOut.setTitle(getResources().getString(R.string.SignIn));
            if(checkConnection()){
                new SignInDialog(MainActivity.this, this, this, mAuth, loginPreferences, mDatabase).show();
            }
        }else {
            if(checkConnection()){
                onLoginSuccess(mAuth.getCurrentUser());
            }else {
                userNameView.setText(loginPreferences.getString("username", ""));
                emailUserView.setText(mAuth.getCurrentUser().getEmail());
                imgUserView.setImageResource(R.drawable.user_default);
                navSignOut.setTitle(getResources().getString(R.string.log_out));
            }


        }
        super.onResume();
    }



    @Override
    protected void onPause() {
        healthyPresenter.pause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        healthyPresenter.destroy();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            new ConfirmDialog(MainActivity.this,this, getString(R.string.exit), new Category()).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.healthy, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            if(!loginPreferences.getString("username", "").equals("")){
                navigator.navigateToSettingUser(MainActivity.this);
            }else {
                Toast.makeText(MainActivity.this, getString(R.string.errSetting), Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()){
            case R.id.nav_home:
                replaceFragment(R.id.content_main, new MainFragment());
                break;

            case R.id.nav_timer:
                replaceFragment(R.id.content_main, new TimerFragment());
                break;

            case R.id.nav_chat:
                if(!loginPreferences.getString("username", "").equals("")){
                    replaceFragment(R.id.content_main, new UserFragment());
                }else {
                    Toast.makeText(MainActivity.this, getString(R.string.errSetting), Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.nav_tracker:
                replaceFragment(R.id.content_main, new TrackerFragment());
                break;

            case R.id.nav_settings:
                if(!loginPreferences.getString("username", "").equals("")){
                    navigator.navigateToSettingUser(MainActivity.this);
                }else {
                    Toast.makeText(MainActivity.this, getString(R.string.errSetting), Toast.LENGTH_SHORT).show();
                }
                break;

            case  R.id.nav_heart:
                navigateToHeartRateActivity();
                break;

            case R.id.nav_logOut:
                editer = loginPreferences.edit();
                editer.clear();
                editer.apply();
                userNameView.setText(getResources().getString(R.string.offline));
                navSignOut.setTitle(getResources().getString(R.string.SignIn));
                imgUserView.setImageBitmap(null);
                emailUserView.setText("");
                mAuth.signOut();
                mAuth = null;
                mAuth = FirebaseAuth.getInstance();
                new SignInDialog(MainActivity.this, this, this, mAuth, loginPreferences, mDatabase).show();
                break;

            case R.id.nav_contact:
                new DialogResult(MainActivity.this, getString(R.string.contact_me)).show();
                break;

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void navigateToHeartRateActivity() {
        navigator.navigateToHeartRate(MainActivity.this);
    }

    public void hideFab() {
        fab.hide();
    }

    public Bitmap getImgUser(){
        return Tools.convertImageViewToBitmap(imgUserView);
    }

    @Override
    public void onLoginSuccess(FirebaseUser firebaseUser) {
        replaceFragment(R.id.content_main, new MainFragment());
        final DatabaseReference user = mDatabase.child(Constants.TABLE_USER).child(firebaseUser.getUid());
        user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user1 = dataSnapshot.getValue(User.class);
                if (user1 !=null){
                    userNameView.setText(user1.getUserName());
                    emailUserView.setText(user1.getUserEmail());
                }
                if (user1.getImage()!=null){
                    byte[] byte1 = Tools.stringToByteArray(user1.getImage().trim());
                    Bitmap bitmap = Tools.convertByteArrayToBitmap(byte1);
                    imgUserView.setImageBitmap(bitmap);
                }else {
                    imgUserView.setImageResource(R.drawable.user_default);
                }
                navSignOut.setTitle(getResources().getString(R.string.log_out));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onCancelLogin() {
        replaceFragment(R.id.content_main, new MainFragment());
    }

    //Check connecttion
    private boolean checkConnection() {
        if(!NetworkConnectionUtil.isConnectedToMobileNetwork(this) && !NetworkConnectionUtil.isConnectedToWifi(this)
                && !NetworkConnectionUtil.isConnectedToInternet(this)){
            showSnack();
            return false;
        }
        return true;
    }

    //Show snack bar
    private void showSnack() {
        Snackbar snackbar = Snackbar
                .make(findViewById(R.id.fab), getResources().getString(R.string.errConnection), Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.
                snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.show();
    }

    public Fragment currentFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.content_main);
    }

    private void initHome() {
        MainFragment mainFragment = new MainFragment();
        UserFragment userFragment = new UserFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_main, userFragment).commit();
    }

//    @Override
//    public void onUserClick(ChatFragment chatFragment) {
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction().replace(R.id.content_main, chatFragment).commit();
//    }


    @Override
    public void onConfirm(Category category) {
        finish();
    }
}
