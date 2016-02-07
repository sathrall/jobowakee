package com.sd2799.jobowakee;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity
        implements DrawerFragment.DrawerFragmentListener {

    public static final String USER_DATA = "user_data";
    public static final String GO_TO_VIEW = "go_to_view";

    public SharedPreferences pref;
    public SharedPreferences.Editor editor;

    private Toolbar toolbar;
    private DrawerFragment drawerFragment;

    public User user = null;
    public UserHandler userHandler;

    public boolean isLoggedIn;
    public boolean sendToProfile;
    public boolean goToView;
    public int viewNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pref = getApplicationContext().getSharedPreferences("UserStatus", 0);
        editor = pref.edit();

        isLoggedIn = pref.getBoolean(LoginRegisterActivity.IS_LOGGED_IN, false);

        if (! isLoggedIn) {
            Intent login = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(login);
            finish();
        }

        Intent intent = getIntent();
        Bundle args = getIntent().getExtras();

        if (intent.hasExtra(EmployerProfileActivity.TO_EMPLOYER_FORM)) {
            sendToProfile = args.getBoolean(EmployerProfileActivity.TO_EMPLOYER_FORM);
        }

        if (intent.hasExtra(USER_DATA) && user == null) {
            user = args.getParcelable(USER_DATA);
            userHandler = UserHandler.get(getApplicationContext());
            userHandler.getUsers().add(user);
        }

        if (intent.hasExtra(GO_TO_VIEW)) {
            viewNumber = args.getInt(GO_TO_VIEW);
            goToView = true;
        }

        // Set up the Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(this.getTitle());

        // Set up the Nav Drawer
        drawerFragment = (DrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_nav_drawer);
        drawerFragment.setUp(R.id.fragment_nav_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
        drawerFragment.setDrawerListener(this);

        // Set up the initial fragment

        if(findViewById(R.id.container_body) != null) {

            // if we're being restored, then we don't need to do anything
            if (savedInstanceState != null) {
                return;
            }

            // create a new fragment to be placed in the activity layout
            if (sendToProfile) {
                Intent profile = new Intent(MainActivity.this, EmployerProfileActivity.class);
                profile.putExtra(USER_DATA, user);
                startActivity(profile);
                finish();
            } else if (goToView) {
                displayView(viewNumber);
            } else {
                displayView(0);
            }
        }
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getActiveFragment();
        if (! (fragment instanceof Fragment)) {
            // if we don't have a fragment, set up home
            displayView(0);
        }
    }

    public Fragment getActiveFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            return null;
        }
        String tag = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName();
        return getSupportFragmentManager().findFragmentByTag(tag);
    }


    /**
     *  Interface Requirements
     */

    // no interfaces implemented yet


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
        Fragment fragment = null;
        String title = getString(R.string.app_name);

        switch (position) {
            case 0:
                fragment = new HomeFragment();
                title = getString(R.string.title_home);
                break;
            case 1:
                fragment = new SettingsFragment();
                title = getString(R.string.title_settings);
                break;
            case 2:
                Intent map = new Intent(MainActivity.this, MapsActivity.class);
                map.putExtra(MainActivity.USER_DATA, user);
                startActivity(map);
                break;
            case 3:
                logout();
                break;
        }

        if (fragment != null) {
            setFragment(fragment, title);
        }
    }


    /**
     *  Set the Fragment
     */

    private void setFragment(Fragment fragment, String title) {
        // pass the user data to the fragment
        Bundle args = new Bundle();
        if (user != null) {
            args.putParcelable("user_data", user);
        }
        fragment.setArguments(args);

        // create a new fragment manager
        FragmentManager fm = getSupportFragmentManager();

        // initialize a transaction
        FragmentTransaction transaction = fm.beginTransaction();

        if (fragment instanceof Fragment) {
            // replace the current fragment in the fragment_container
            transaction.replace(R.id.container_body, fragment);
        } else {
            transaction.add(R.id.container_body, fragment);
        }

        transaction.addToBackStack(null);

        // finally, commit the transaction
        transaction.commit();

        // Set the toolbar title
        getSupportActionBar().setTitle(title);
    }

    /**
     *  Logout
     */

    public void logout() {
        // remove the logged in session
        editor.remove(LoginRegisterActivity.IS_LOGGED_IN);
        editor.commit();
        Intent logout = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(logout);
        (MainActivity.this).finish();
    }

}
