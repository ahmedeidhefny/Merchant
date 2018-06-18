package com.example.ahmed_eid.merchantnavigationdrawer;

import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MerchantND extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    SharedPreferences sharedPreferences ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_nd);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        onNavigationItemSelected(navigationView.getMenu().getItem(0));
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        TextView TV_placeName = header.findViewById(R.id.placeName);
        TextView TV_placeEmail = header.findViewById(R.id.placeEmail);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if(sharedPreferences.getBoolean("logged in",false)) {
            String name = sharedPreferences.getString("PName",null);
            String email = sharedPreferences.getString("PEmail",null);

            TV_placeName.setText(name);
            TV_placeEmail.setText(email);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.merchant_nd, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        FragmentManager fragmentManager = getFragmentManager();

         if (id == R.id.action_help) {

        }else  if (id == R.id.action_profile) {
            fragmentManager.beginTransaction().replace(R.id.content_frame,
                    new Merchant_Fragment_Profile()).commit();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fragmentManager = getFragmentManager();

        switch (id) {
            case R.id.nav_home :
            {
                fragmentManager.beginTransaction().replace(R.id.content_frame,
                        new Merchant_Fragment_Menu()).commit();
                break;
            }case R.id.nav_branches:
            {
                fragmentManager.beginTransaction().replace(R.id.content_frame,
                        new Merchant_Fragment_Branches() ).commit();
                break;
            }case R.id.nav_Menu:
            {
                fragmentManager.beginTransaction().replace(R.id.content_frame,
                        new Merchant_Fragment_Menu()).commit();
                break;
            }case R.id.nav_logout:
            {
                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                Intent ino = new Intent(this,MerchantSignIn.class);
                ino.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(ino);
                break;
            }default:
            {
                fragmentManager.beginTransaction().replace(R.id.content_frame,
                        new Merchant_Fragment_Menu()).commit();
            }

        }
       /* if (id == R.id.nav_home) {
            fragmentManager.beginTransaction().replace(R.id.content_frame,
                    new Merchant_Fragment_Menu()).commit();
        } else if (id == R.id.nav_branches) {
            fragmentManager.beginTransaction().replace(R.id.content_frame,
                    new Merchant_Fragment_Branches() ).commit();
        } else if (id == R.id.nav_Menu) {
            fragmentManager.beginTransaction().replace(R.id.content_frame,
                    new Merchant_Fragment_Menu()).commit();
        } else if (id == R.id.nav_Setting) {

        } else if (id == R.id.nav_logout) {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.commit();
            Intent ino = new Intent(this,MerchantSignIn.class);
            ino.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(ino);

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
