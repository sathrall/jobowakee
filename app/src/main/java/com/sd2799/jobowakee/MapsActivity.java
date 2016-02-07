package com.sd2799.jobowakee;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by student on 10/28/2015.
 */

public class MapsActivity extends AppCompatActivity
        implements OnMapReadyCallback,
                   DrawerFragment.DrawerFragmentListener {

    public static final String TAG = "MapsActivity";

    public SharedPreferences pref;
    public SharedPreferences.Editor editor;

    private Toolbar toolbar;
    private DrawerFragment drawerFragment;

    public boolean isLoggedIn;

    public User user = null;
    public UserHandler userHandler = UserHandler.get(MapsActivity.this);

    private GoogleMap mMap;

    public double mLatitude;
    public double mLongitude;
    public LatLng myLocation;
    public String mMarkerTitle;

    public HashMap<UUID, Marker> visibleMarkers = new HashMap<UUID, Marker>();

    private List<MarkerItem> markerItems = new ArrayList<MarkerItem>();

//    private static final long LOCATION_REFRESH_TIME = 15000; // 15 seconds
    private static final long LOCATION_REFRESH_TIME = 300000; // 5 minutes
    private static final float LOCATION_REFRESH_DISTANCE = 1;

    private LocationManager mLocationManager;

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            useLocation(location);
            fillDemoMarkers(15, location.getLatitude(), location.getLongitude(), 100);
            fillDemoMarkers(15, location.getLatitude(), location.getLongitude(), 10);
        }

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onProviderDisabled(String provider) {}

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        pref = getApplicationContext().getSharedPreferences("UserStatus", 0);
        editor = pref.edit();

        isLoggedIn = pref.getBoolean(LoginRegisterActivity.IS_LOGGED_IN, false);

        if (! isLoggedIn) {
            Intent login = new Intent(MapsActivity.this, LoginActivity.class);
            startActivity(login);
            finish();
        }

        // If logged in, grab the data, otherwise send to login
        Bundle args = getIntent().getExtras();
        if (args != null) {
            if (user == null) {
                user = args.getParcelable(MainActivity.USER_DATA);
                userHandler.getUsers().add(user);
            }
        }

        // Set up the Toolbar
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(this.getTitle());

        // Set up the Nav Drawer
        drawerFragment = (DrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_nav_drawer);
        drawerFragment.setUp(R.id.fragment_nav_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
        drawerFragment.setDrawerListener(this);

        // set up location
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (! mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        } else {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
                    LOCATION_REFRESH_DISTANCE, mLocationListener);
        }

        // set up the map fragment
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        // TODO: try to only show the map when the location is grabbed
        // TODO: look into a loading screen to take care of the view in the meantime

        mMap = map;

        mMap.setOnCameraChangeListener(getCameraChangeListener());
    }

    @Override
    public void onPause() {
        mLocationManager.removeUpdates(mLocationListener);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
                LOCATION_REFRESH_DISTANCE, mLocationListener);
    }

    @Override
    public void onStop() {
        mLocationManager.removeUpdates(mLocationListener);
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLocationManager.removeUpdates(mLocationListener);
    }

    private void useLocation(Location location) {
        mLatitude = location.getLatitude();
        mLongitude = location.getLongitude();
        mMarkerTitle = "You Are Here!";

        if (myLocation != null) {
            mMap.clear();
        }

        myLocation = new LatLng(mLatitude, mLongitude);
        mMap.addMarker(new MarkerOptions().position(myLocation)
                .title(mMarkerTitle)
                .icon(BitmapDescriptorFactory.defaultMarker(199)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 14));
    }

    private void fillDemoMarkers(int num, double lat, double lng, int range) {
        for (int i = 0; i < num; i++) {
            double mLat;
            double mLng;

            // generate random numbers for calculations
            double rLat = (Math.random() * 100) / (Math.random() * 100) / range;
            double rLng = (Math.random() * 100) / (Math.random() * 100) / range;

            // select an operator
            boolean add = (Math.random() < 0.5) ? true : false;

            // calculate additional coordinates
            if (add) {
                mLat = (lat + rLat);
                mLng = (lng + rLng);
            } else {
                mLat = (lat - rLat);
                mLng = (lng - rLng);
            }

            // initialize marker items
            MarkerItem marker = new MarkerItem(mLat, mLng);

            marker.setTitle("Place of Business");
            marker.setSnippet(getAddress(mLat, mLng));
            marker.setHue(0);

            // add markers to map
            markerItems.add(marker);
        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public GoogleMap.OnCameraChangeListener getCameraChangeListener() {
        return new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                addItemsToMap(markerItems);
            }
        };
    }

    private void addItemsToMap(List<MarkerItem> items) {

        if (mMap != null) {
            // get the current bounds of the map
            LatLngBounds bounds = mMap.getProjection().getVisibleRegion().latLngBounds;

            // loop through the items
            for (MarkerItem item : items) {
                LatLng position = new LatLng(item.getLatitude(), item.getLongitude());

                // check to see if the item's position is within bounds
                if (bounds.contains(position)) {

                    // if we are in bounds and the item is not already in the HashMap
                    if (!visibleMarkers.containsKey(item.getId())) {

                        if (item.getSnippet() != "") {
                            // add the item to the HashMap and create a marker to reference
                            visibleMarkers.put(item.getId(), mMap.addMarker(new MarkerOptions()
                                    .position(position)
                                    .title(item.getTitle())
                                    .snippet(item.getSnippet())
                                    .icon(BitmapDescriptorFactory.defaultMarker(item.getHue()))));
                        } else {
                            // add the item to the HashMap and create a marker to reference
                            visibleMarkers.put(item.getId(), mMap.addMarker(new MarkerOptions()
                                    .position(position)
                                    .title(item.getTitle())
                                    .icon(BitmapDescriptorFactory.defaultMarker(item.getHue()))));
                        }

                    }

                } else {

                    // if not in bounds and the item is in the HashMap
                    if (visibleMarkers.containsKey(item.getId())) {

                        // remove the marker from the map
                        visibleMarkers.get(item.getId()).remove();

                        // remove the reference from the HashMap
                        visibleMarkers.remove(item.getId());

                    }

                }

            }

        }

    }

    private String getAddress(double latitude, double longitude) {
        StringBuilder result = new StringBuilder();
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                result.append(address.getAddressLine(0)).append("\n");
//                result.append(address.getLocality()).append(", ");
//                result.append(address.getAdminArea()).append(" ");
//                result.append(address.getPostalCode());
            }
        } catch (IOException e) {
            Log.d(TAG, e.getMessage());
        }

        return result.toString();
    }

    class MarkerItem {
        private UUID id;
        private double latitude;
        private double longitude;
        private String title;
        private String snippet;
        private int hue;

        public MarkerItem(double lat, double lng) {
            id = UUID.randomUUID();
            latitude = lat;
            longitude = lng;
            title = "Marker";
            snippet = "";
            hue = 0;
        }

        public UUID getId() {
            return id;
        }

        public void setLatitude(double lat) {
            latitude = lat;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLongitude(double lng) {
            longitude = lng;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setTitle(String t) {
            title = t;
        }

        public String getTitle() {
            return title;
        }

        public void setSnippet(String s) {
            snippet = s;
        }

        public String getSnippet() {
            return snippet;
        }

        public void setHue(int h) {
            hue = h;
        }

        public int getHue() {
            return hue;
        }
    }


    /**
     *  Nav Drawer Item Selected
     */

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }


    /**
     *  displayView Handler
     */

    private void displayView(int position) {
        Intent page = new Intent(MapsActivity.this, MainActivity.class);
        page.putExtra(MainActivity.USER_DATA, user);

        switch (position) {
            case 0:
                page.putExtra(MainActivity.GO_TO_VIEW, 0);
                startActivity(page);
                break;
            case 1:
                page.putExtra(MainActivity.GO_TO_VIEW, 1);
                startActivity(page);
                break;
            case 2:
                break;
            case 3:
                page.putExtra(MainActivity.GO_TO_VIEW, 3);
                startActivity(page);
                (MapsActivity.this).finish();
                break;
        }
    }
}
