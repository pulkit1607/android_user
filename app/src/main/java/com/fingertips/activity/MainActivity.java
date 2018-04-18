package com.fingertips.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.fingertips.R;
import com.fingertips.app.Constants;
import com.fingertips.fragment.HomeFragment;
import com.fingertips.fragment.MenuFragment;
import com.fingertips.fragment.RestaurantFragment;
import com.fingertips.helper.XClass;
import com.fingertips.util.AppSingleton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private LocationManager locationManager;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private double current_latitude;
    private double current_longitude;
    private XClass obj;
    private String currentFragment;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 2;
    int count = 1;
    private LinearLayout llProgress;
    private ImageView ivMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        llProgress = (LinearLayout) findViewById(R.id.ll_progress);
        ivMenu = (ImageView) findViewById(R.id.iv_menu);

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        checkGps();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (currentFragment.equalsIgnoreCase(Constants.FragmentTag.homeFragment)) {
                super.onBackPressed();
            } else if (currentFragment.equalsIgnoreCase(Constants.FragmentTag.menuFragment)) {
                setFragment(Constants.FragmentTag.restaurantFragment, null);
            } else {
                count = 0;
                if(mGoogleApiClient!=null) {
                    mGoogleApiClient.connect();
                }
                setHomeFragment();

            }
        }
    }

    public void showLoader() {
        llProgress.setVisibility(View.VISIBLE);
    }

    public void hideLoader() {
        llProgress.setVisibility(View.GONE);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Intent intent = new Intent(this, SettingActivity.class);
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            intent.putExtra(Constants.WHICH, Constants.Navigation.PROFILE);
            startActivity(intent);
        } else if (id == R.id.nav_order) {
            intent.putExtra(Constants.WHICH, Constants.Navigation.ORDER);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setFragment(String fragment, Bundle bundle) {
        currentFragment = fragment;
        FragmentTransaction localFragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (fragment.equalsIgnoreCase(Constants.FragmentTag.homeFragment)) {
            HomeFragment mainFragment = new HomeFragment();
            mainFragment.setArguments(bundle);
            localFragmentTransaction.replace(R.id.container, mainFragment, Constants.FragmentTag.homeFragment);
            localFragmentTransaction.commit();
            if (mGoogleApiClient.isConnected()) {
                mGoogleApiClient.disconnect();
            }

        } else if (fragment.equalsIgnoreCase(Constants.FragmentTag.restaurantFragment)) {
            RestaurantFragment restaurantFragment = new RestaurantFragment();
            restaurantFragment.setArguments(bundle);
            localFragmentTransaction.replace(R.id.container, restaurantFragment, Constants.FragmentTag.restaurantFragment);
            localFragmentTransaction.commit();

        }else if (fragment.equalsIgnoreCase(Constants.FragmentTag.menuFragment)) {
            MenuFragment menuFragment = new MenuFragment();
            menuFragment.setArguments(bundle);
            localFragmentTransaction.replace(R.id.container, menuFragment, Constants.FragmentTag.menuFragment);
            localFragmentTransaction.commit();
        }

    }

    void checkGps() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // notify user
            final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage(getString(R.string.enable_gps));
            dialog.setCancelable(false);
            dialog.setPositiveButton("ENABLE", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivityForResult(myIntent, 1);
                }
            });
            dialog.setNegativeButton("CLOSE", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    finish();
                }
            });
            dialog.show();
        } else {
            getLocationPermission();
        }
    }

    public void getLocation() {
        locationManager = (LocationManager) MainActivity.this.getSystemService(Context.LOCATION_SERVICE);
        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(MainActivity.this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    public void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            AppSingleton.getInstance().setmLocationPermissionGranted(true);
            getLocation();

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            getLocationPermission();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        if (count == 1) {
            setHomeFragment();
        }
    }

    private void setHomeFragment() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            current_latitude = mLastLocation.getLatitude();
            current_longitude = mLastLocation.getLongitude();
            Log.e("LatLong", String.valueOf(mLastLocation.getLongitude()) + "     ," + String.valueOf(mLastLocation.getLatitude()));
            Bundle locationBundle = new Bundle();
            locationBundle.putDouble(Constants.Extras.LATITUDE, current_latitude);
            locationBundle.putDouble(Constants.Extras.LONGITUDE, current_longitude);
            setFragment(Constants.FragmentTag.homeFragment, locationBundle);
            count++;
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void onStart() {
        if(mGoogleApiClient!=null) {
            mGoogleApiClient.connect();
        }
        super.onStart();
    }

    public void onStop() {
        if(mGoogleApiClient!=null) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    public void onLocationChanged(Location location) {

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            Log.d("onLocationChanged", "Removing Location Updates");
        }

    }

}
