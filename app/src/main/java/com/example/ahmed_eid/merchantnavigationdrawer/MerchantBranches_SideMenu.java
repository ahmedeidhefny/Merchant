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

public class MerchantBranches_SideMenu extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    SharedPreferences sharedPreferences ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_branches__side_menu);
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
        TextView TV_branchName = header.findViewById(R.id.branchHName);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (sharedPreferences.getBoolean("loginBranch",false)){
            String name = sharedPreferences.getString("BName",null);
            TV_branchName.setText(name);
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
        getMenuInflater().inflate(R.menu.merchant_branches__side_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        FragmentManager fragmentManager = getFragmentManager();

        if (id == R.id.action_B_help) {

        }else  if (id == R.id.action_B_profile) {
            fragmentManager.beginTransaction().replace(R.id.BranchContent_frame,
                    new Branch_Fragment_Profile()).commit();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fragmentManager = getFragmentManager();
        if (id == R.id.nav_Bhome) {
            fragmentManager.beginTransaction().replace(R.id.BranchContent_frame,
                    new Branch_Fragment_offers()).commit();

        } else if (id == R.id.nav_offers) {
            fragmentManager.beginTransaction().replace(R.id.BranchContent_frame,
                    new Branch_Fragment_offers()).commit();

        } else if (id == R.id.nav_addPoint) {
            fragmentManager.beginTransaction().replace(R.id.BranchContent_frame,
                    new Branch_Fragment_Points()).commit();

       // } else if (id == R.id.nav_report) {

        } else if (id == R.id.nav_Blogout) {
            Intent ino = new Intent(this,MerchantND.class);
            ino.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(ino);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
