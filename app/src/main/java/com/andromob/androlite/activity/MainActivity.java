package com.andromob.androlite.activity;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.andromob.androlite.BuildConfig;
import com.andromob.androlite.R;
import com.andromob.androlite.fragments.AboutFragment;
import com.andromob.androlite.fragments.PrivacyFragment;
import com.andromob.androlite.fragments.WebviewFragment;
import com.andromob.androlite.utils.AdsManager;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private RelativeLayout no_internet;
    private Button Retrybtn;
    MyApplication myApplication;
    FrameLayout adContainerView;
    AdsManager.InterAdListener interAdListener;
    AdsManager adsManager;
    NavigationView navigationView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        adsManager = AdsManager.getInstance();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_item_1);
        no_internet = findViewById(R.id.no_internet);
        Retrybtn = findViewById(R.id.Retrybtn);
        adContainerView = findViewById(R.id.adContainerView);
        checkConnection();
        myApplication = MyApplication.getInstance();

        if (savedInstanceState == null) {
            Fragment fragment = null;
            fragment = WebviewFragment.newInstance(getResources().getString(R.string.web_link_home));
            displaySelectedFragment(fragment);
        }

        Retrybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkConnection();
            }
        });

        adsManager.loadInterAd(this);
        setAdsListener();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        Fragment fragment = null;
        if (id == R.id.nav_item_1) {
            adsManager.showInterAdOnClick(this, interAdListener,"nav_item_1");
        } else if (id == R.id.nav_item_2) {
            adsManager.showInterAdOnClick(this, interAdListener,"nav_item_2");
        } else if (id == R.id.nav_item_3) {
            adsManager.showInterAdOnClick(this, interAdListener,"nav_item_3");
        } else if (id == R.id.nav_item_4) {
            adsManager.showInterAdOnClick(this, interAdListener,"nav_item_4");
        } else if (id == R.id.nav_item_5) {
            adsManager.showInterAdOnClick(this, interAdListener,"nav_item_5");
        } else if (id == R.id.nav_item_6) {
            adsManager.showInterAdOnClick(this, interAdListener,"nav_item_6");
        } else if (id == R.id.nav_item_7) {
            adsManager.showInterAdOnClick(this, interAdListener,"nav_item_7");
        } else if (id == R.id.nav_item_8) {
            adsManager.showInterAdOnClick(this, interAdListener,"nav_item_8");
        } else if (id == R.id.nav_item_9) {
            finish();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    void setAdsListener(){
        interAdListener = new AdsManager.InterAdListener() {
            @Override
            public void onClick(String type) {
                Fragment fragment = null;
                switch (type){
                    case "nav_item_1":
                        fragment = WebviewFragment.newInstance(getResources().getString(R.string.web_link_home));
                        displaySelectedFragment(fragment);
                        break;
                    case "nav_item_2":
                        fragment = WebviewFragment.newInstance(getResources().getString(R.string.web_link_item2));
                        displaySelectedFragment(fragment);
                        break;
                    case "nav_item_3":
                        fragment = WebviewFragment.newInstance(getResources().getString(R.string.web_link_item3));
                        displaySelectedFragment(fragment);
                        break;
                    case "nav_item_4":
                        fragment = WebviewFragment.newInstance(getResources().getString(R.string.web_link_item4));
                        displaySelectedFragment(fragment);
                        break;
                    case "nav_item_5":
                        Uri uri = Uri.parse("market://details?id=" + getApplication().getPackageName());
                        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        try {
                            startActivity(goToMarket);
                        } catch (ActivityNotFoundException e) {
                            startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("http://play.google.com/store/apps/details?id=" + getApplication().getPackageName())));
                        }
                        break;
                    case "nav_item_6":
                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setType("text/plain");
                        shareIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
                        String shareMessage = "Let me recommend you this application\n\n";
                        shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID;
                        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                        startActivity(Intent.createChooser(shareIntent, "choose one"));
                        break;
                    case "nav_item_7":
                        fragment = new PrivacyFragment();
                        displaySelectedFragment(fragment);
                        break;
                    case "nav_item_8":
                        fragment = new AboutFragment();
                        displaySelectedFragment(fragment);
                        break;
                }

            }
        };
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            openQuitDialog();
        }
    }

    public void openQuitDialog() {
        androidx.appcompat.app.AlertDialog.Builder alert;
        alert = new androidx.appcompat.app.AlertDialog.Builder(MainActivity.this);
        alert.setTitle(R.string.app_name);
        alert.setIcon(R.mipmap.ic_launcher);
        alert.setMessage(getString(R.string.sure_quit));

        alert.setPositiveButton(R.string.exit, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                finish();
            }
        });

        alert.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alert.show();
    }

    private void displaySelectedFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    public void checkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileNetwork = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (wifi.isConnected()) {
            no_internet.setVisibility(View.GONE);
        } else if (mobileNetwork.isConnected()) {
            no_internet.setVisibility(View.GONE);
        } else {
            no_internet.setVisibility(View.VISIBLE);
        }

    }

    private void updateAds() {
        adsManager.showBannerAd(this, adContainerView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        adsManager.destroyBannerAds();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateAds();
    }

    @Override
    protected void onDestroy() {
        adsManager.destroyInterAds();
        super.onDestroy();
    }
}