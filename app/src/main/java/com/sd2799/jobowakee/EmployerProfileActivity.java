package com.sd2799.jobowakee;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

/**
 * Created by fexofenadine180mg on 11/19/15.
 */
public class EmployerProfileActivity extends AppCompatActivity
        implements DrawerFragment.DrawerFragmentListener,
                   EmployerProfileFormFragment.OnProfileSubmissionListener {

    public static final String TO_EMPLOYER_FORM = "to_employer_form";

    private Toolbar toolbar;
    private DrawerFragment drawerFragment;

    private String mEmployerProfileTitle;

    public boolean sendToProfile;

    public User user = null;
    public UserHandler userHandler = UserHandler.get(EmployerProfileActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // If logged in, grab the data, otherwise send to login
        Bundle args = getIntent().getExtras();
        sendToProfile = args.getBoolean(TO_EMPLOYER_FORM);

        if (user == null) {
            user = args.getParcelable(MainActivity.USER_DATA);
            userHandler.getUsers().add(user);
        }

        if (user == null) {
            Intent login = new Intent(EmployerProfileActivity.this, LoginActivity.class);
            startActivity(login);
            finish();
        }

        mEmployerProfileTitle = "Welcome, " + user.getUsername();

        // Set up the Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(mEmployerProfileTitle);

        // Set up the Nav Drawer
        drawerFragment = (DrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_nav_drawer);
        drawerFragment.setUp(R.id.fragment_nav_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
        drawerFragment.setDrawerListener(this);

        // Set up the initial fragment

        if (findViewById(R.id.container_body) != null) {

            // if we're being restored, then we don't need to do anything
            if (savedInstanceState != null) {
                return;
            }

            Fragment fragment = null;
            String title = "";

            // create a new fragment to be placed in the activity layout
            if (sendToProfile) {
                fragment = new EmployerProfileFormFragment();
                title = getString(R.string.title_employer_profile_form);
            } else {
                Intent intent = new Intent(EmployerProfileActivity.this, MainActivity.class);
                intent.putExtra(MainActivity.USER_DATA, user);
                startActivity(intent);
                finish();
            }

            if (fragment != null) {
                setFragment(fragment, title);
            }
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }


    /**
     * Interface Requirements
     */

    public void onProfileSubmission(User user) {
        // once the new fragment's been set let's temporarily remove the
        // employer form fragment so that users can't go back to it
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentByTag("EmployerProfileFormFragment");
        if (fragment != null) {
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.remove(fragment).commit();
        }
        // now we will go to the home page until we get the code for the
        // job listing creation views
        Intent intent = new Intent(EmployerProfileActivity.this, MainActivity.class);
        intent.putExtra(MainActivity.USER_DATA, user);
        startActivity(intent);
    }


    /**
     * Nav Drawer Item Selected
     */

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }


    /**
     * displayView Handler
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
                logout();
                break;
        }

        if (fragment != null) {
            setFragment(fragment, title);
        }
    }


    /**
     * Set the Fragment
     */

    private void setFragment(Fragment fragment, String title) {
        // pass the user data to the fragment
        Bundle args = new Bundle();
        if (user != null) {
            args.putParcelable(MainActivity.USER_DATA, user);
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
     * Logout
     */

    public void logout() {
        Intent logout = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(logout);
        finish();
    }
}