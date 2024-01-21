package com.mtech.envirotrack;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.mtech.envirotrack.user.Login;
import com.mtech.envirotrack.user.Profile;
import com.mtech.envirotrack.weather.HomeFragment;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_FINE_LOCATION =99 ;
    private static final int DEFAULT_UPDATE_INTERVAL = 30;
    private static final int FASTEST_UPDATE_INTERVAL = 5;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    NavigationView navigationView;
    Toolbar toolbar;
    FloatingActionButton fab_add_report;
    DrawerLayout drawerLayout;
    BottomNavigationView bottomNavigationView;

    TextView tv_lat, tv_long, tv_alt , tv_accu, tv_address;

  // location request
    LocationRequest locationRequest;


    // google api location services
    FusedLocationProviderClient fusedLocationProviderClient;

    private LocationCallback locationCallback;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // This is the layout file for the main activity

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        fab_add_report = findViewById(R.id.fab_add_report);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        View customToolbar = LayoutInflater.from(this).inflate(R.layout.search_toolbar, toolbar, false);
        toolbar.addView(customToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
//        changeStatusBarColor(getResources().getColor(com.google.android.material.R.color.background_material_dark));
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        tv_lat = toolbar.findViewById(R.id.tv_latitude);
        tv_long = toolbar.findViewById(R.id.tv_longitude);
        tv_alt = toolbar.findViewById(R.id.tv_altitude);
        tv_accu = toolbar.findViewById(R.id.tv_accuracy);
        tv_address = toolbar.findViewById(R.id.tv_address);

       if(savedInstanceState == null){
           getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new HomeFragment()).commit();
           navigationView.setCheckedItem(R.id.nav_home);
       }
       fab_add_report.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
//               showBottomDialog();
           }
       });

        ImageView ivSearchIcon = customToolbar.findViewById(R.id.ivSearchIcon);
        ivSearchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the search fragment when the search icon is clicked
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                int itemId = item.getItemId();
                if (itemId == R.id.nav_daily) {
                    fragment = new HomeFragment();
                } else if (itemId == R.id.nav_report) {
                    fragment = new HomeFragment();
                } else if (itemId == R.id.nav_map) {
                    fragment = new MapsFragment();
                } else if (itemId == R.id.nav_notification) {
                    fragment = new MapsFragment();
                } else if (itemId == R.id.nav_profile) {
                    if (mAuth.getCurrentUser() != null) {
                        // If the user is logged in, navigate to the profile activity
                        Intent intent = new Intent(MainActivity.this, Profile.class);
                        startActivity(intent);
                    } else {
                        // If the user is not logged in, navigate to the login activity
                        Intent intent = new Intent(MainActivity.this, Login.class);
                        startActivity(intent);
                    }
                }
                // debug if fragment is null
                if (fragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, fragment).commit();
                }
                return true;
            }
        });


        // location request
//        locationRequest = new LocationRequest();
//        locationRequest.setInterval(1000 * DEFAULT_UPDATE_INTERVAL);
//        locationRequest.setFastestInterval(1000 * FASTEST_UPDATE_INTERVAL);
//        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        tv_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddressDialog();

            }
        });
        updateGPS();
    }


    private void changeStatusBarColor(int color) {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(color);
    }

    private  void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    private void showBottomDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.buttom_sheet_layout);

        LinearLayout videoLayout = dialog.findViewById(R.id.layoutVideo);
        LinearLayout shortsLayout = dialog.findViewById(R.id.layoutShorts);
        LinearLayout liveLayout = dialog.findViewById(R.id.layoutLive);
        ImageView cancelButton = dialog.findViewById(R.id.cancelButton);

        videoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                Toast.makeText(MainActivity.this,"Upload a Video is clicked",Toast.LENGTH_SHORT).show();

            }
        });

        shortsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                Toast.makeText(MainActivity.this,"Create a short is Clicked",Toast.LENGTH_SHORT).show();

            }
        });

        liveLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                Toast.makeText(MainActivity.this,"Go live is Clicked",Toast.LENGTH_SHORT).show();

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_notification) {
            // Handle the click event for the notification icon
            // You can show a notification or perform any other action here
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateGPS() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    updateUIValues(location);

                }
            });
        }
        else {
            // permission not granted
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_FINE_LOCATION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch ((requestCode)){
            case PERMISSION_FINE_LOCATION:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    updateGPS();
                }
                else {
                    Toast.makeText(this, "This app requires permission to be granted in order to work properly", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }
    }

    private void updateUIValues(Location location) {
        // update all of the text view objects with a new location.
//        tv_lat.setText(String.valueOf(location.getLatitude()));
//        tv_long.setText(String.valueOf(location.getLongitude()));
//        tv_alt.setText(String.valueOf(location.getAltitude()));
//        tv_accu.setText(String.valueOf(location.getAccuracy()));
//        tv_address.setText(String.valueOf(location.getLatitude()));
        Geocoder geocoder = new Geocoder(MainActivity.this);
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (!addresses.isEmpty()) {
                Address address = addresses.get(0);
                String cityName = address.getLocality();
                String stateName = address.getAdminArea();
                tv_address.setText(String.format("%s, %s", cityName, stateName));
            }
        }catch (Exception e){
            tv_address.setText("Unable to get street address");
        }

    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        stopLocationUpdates();
//    }
//
//    private void stopLocationUpdates() {
//        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
//    }
    private void showAddressDialog(){
        Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.location_details);

        TextView tvLongitude = dialog.findViewById(R.id.tv_longitude);
        TextView tvLatitude = dialog.findViewById(R.id.tv_latitude);
        TextView tvAltitude = dialog.findViewById(R.id.tv_altitude);
        TextView tvAccuracy = dialog.findViewById(R.id.tv_accuracy);
        TextView tvCityName = dialog.findViewById(R.id.tv_city_name);
        TextView tvFullAddress = dialog.findViewById(R.id.tv_full_address);
        TextView tvCountryCode = dialog.findViewById(R.id.tv_country_code);
        TextView tvCountryName = dialog.findViewById(R.id.tv_country_name);

        // Request the current location
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    // Update the UI with the location details
                    Geocoder geocoder = new Geocoder(MainActivity.this);
                    try {
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        if (!addresses.isEmpty()) {
                            Address address = addresses.get(0);
                            tvLongitude.setText("Longitude: " + location.getLongitude());
                            tvLatitude.setText("Latitude: " + location.getLatitude());
                            tvAltitude.setText("Altitude: " + location.getAltitude());
                            tvAccuracy.setText("Accuracy: " + location.getAccuracy());
                            tvCityName.setText("City Name: " + address.getLocality());
                            tvFullAddress.setText("Full Address: " + address.getAddressLine(0));
                            tvCountryCode.setText("Country Code: " + address.getCountryCode());
                            tvCountryName.setText("Country Name: " + address.getCountryName());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        dialog.show();
    }


}