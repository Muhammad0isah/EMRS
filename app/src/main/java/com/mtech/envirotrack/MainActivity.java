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
import android.content.SharedPreferences;
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
import android.view.MotionEvent;
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
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.mtech.envirotrack.report.EnvironmentalReport;
import com.mtech.envirotrack.report.ReportFragment;
import com.mtech.envirotrack.weather.SearchFragment;
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
    View line_view;
    BottomAppBar bottonAppBar;

    private MenuItem notificationItem;


    TextView tv_lat, tv_long, tv_alt , tv_accu, tv_address;

  // location request
    LocationRequest locationRequest;

    // current location
    Location currentLocation;

    // google api location services
    FusedLocationProviderClient fusedLocationProviderClient;

    private LocationCallback locationCallback;

    // list of saved locations
    List<Location> savedLocations;






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
        bottonAppBar = findViewById(R.id.bottomAppBar);
        line_view = findViewById(R.id.line_view);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
//        changeStatusBarColor(getResources().getColor(com.google.android.material.R.color.background_material_dark));
        View rootView = findViewById(android.R.id.content);
        rootView.setBackgroundColor(Color.WHITE);
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
       navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
           @Override
           public boolean onNavigationItemSelected(@NonNull MenuItem item) {
               Fragment fragment = null;
               int itemId = item.getItemId();
               if (itemId == R.id.nav_home){
                   fragment = new HomeFragment();
               } else if (itemId == R.id.nav_report) {
                   fragment = new ReportFragment();
               }
               else if(itemId == R.id.nav_map){
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
               } else if (itemId == R.id.logoutButton) {

               }
               return false;
           }
       });

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
                    fragment = new ReportFragment();
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

        tv_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddressDialog();
            }
        });

        // event that triggered whenever the update interval is met
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                // save the location
                updateUIValues(locationResult.getLastLocation());
            }
        };

//        // add the gps location to the global list;
//        MyApplication myApplication = (MyApplication) getApplicationContext();
//        savedLocations = myApplication.getLocations();
//        savedLocations.add(currentLocation);

        updateGPS();
    }

    private  void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        notificationItem = menu.findItem(R.id.action_notification);

        notificationItem.setVisible(false);
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
        else if (id == R.id.action_search) {
            // Handle the click event for the search icon
            // You can show a search dialog or perform any other action here
            replaceFragment(new SearchFragment());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateGPS() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000); // Set the desired interval for active location updates, in milliseconds.
        locationRequest.setFastestInterval(5000); // Set the fastest rate for active location updates, in milliseconds.
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY); // Set the priority of the request.

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    updateUIValues(location);
                    currentLocation = location;

                    MyApplication myApplication = (MyApplication) getApplicationContext();
                    savedLocations = myApplication.getLocations();
                    savedLocations.add(currentLocation);
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
        Geocoder geocoder = new Geocoder(MainActivity.this);
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (!addresses.isEmpty()) {
                Address address = addresses.get(0);
                String cityName = address.getLocality();
                String stateName = address.getAdminArea();
                tv_address.setText(String.format("%s, %s", cityName, stateName));

                // save the city name to shared preferences for later use in home fragment
                SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("cityName", cityName);
                editor.putString("stateName", stateName);

                editor.apply();
            }
        }catch (Exception e){
            tv_address.setText("Unable to get Location");
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (fusedLocationProviderClient != null) {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        }
    }

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
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
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
    @Override
    protected void onResume() {
        super.onResume();
        if (notificationItem != null) {
            notificationItem.setVisible(isLoggedIn());
        }
    }
    public boolean isLoggedIn() {
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }

}