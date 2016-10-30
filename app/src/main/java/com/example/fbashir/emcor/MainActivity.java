package com.example.fbashir.emcor;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.fbashir.emcor.fragments.AboutUsFragement;
import com.example.fbashir.emcor.fragments.BusinessStatsFragement;
import com.example.fbashir.emcor.fragments.CompaniesFragement;
import com.example.fbashir.emcor.fragments.ContactUsFragement;
import com.example.fbashir.emcor.fragments.HomeFragement;
import com.example.fbashir.emcor.fragments.LocationsFragement;
import com.example.fbashir.emcor.fragments.SafetyStatsFragement;
import com.example.fbashir.emcor.fragments.ServicesFragement;
import com.example.fbashir.emcor.fragments.SplashFragement;
import com.example.fbashir.emcor.fragments.TestFragement;
import com.example.fbashir.emcor.helpers.DBHandler;
import com.example.fbashir.emcor.helpers.MyUtils;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static FragmentManager fragmentManager;
    public static DBHandler db;
    private static final String HOME_KEY = "HOME_DATA";
    private static final String DIVISIONS_KEY = "SERVICES_DATA";
    private static final String APPCONFIG_KEY = "APPCONFIG_DATA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        db = new DBHandler(this);

        DBHandler.LocalData appConfigData = db.getData(APPCONFIG_KEY);
        DBHandler.LocalData divisionsData = db.getData(DIVISIONS_KEY);
        DBHandler.LocalData homeData = db.getData(HOME_KEY);
        Calendar oneWeekAhead = Calendar.getInstance();
        oneWeekAhead.add(Calendar.DATE, 7);

        if(appConfigData != null && divisionsData != null && homeData != null && oneWeekAhead.getTimeInMillis() > appConfigData.time)
        {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new HomeFragement()).commit();
        }
        else
        {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new SplashFragement()).commit();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            public void onClick(View view)
            {
                gotoHome(view);
            }
        });
        //toolbar.setOverflowIcon(getResources().getDrawable(R.drawable.home_icn));

//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();
        toolbar.setNavigationIcon(R.drawable.home_icon_transparent);

//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);
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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        fragmentManager = getSupportFragmentManager();
        View view = getWindow().getDecorView().getRootView();

        if(id == R.id.nav_test)
        {
            //goto_test(view);
        }
        else if (id == R.id.nav_home)
        {
            // Handle the camera action
            fragmentManager.beginTransaction().replace(R.id.content_frame, new HomeFragement()).addToBackStack(null).commit();
        } else if (id == R.id.nav_companies) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new CompaniesFragement()).addToBackStack(null).commit();
        } else if (id == R.id.nav_locations)
        {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new LocationsFragement()).addToBackStack(null).commit();
        }
        else if(id == R.id.nav_services)
        {
            gotoDivisions(view);
        }
        else if(id == R.id.nav_contact_us)
        {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new ContactUsFragement()).addToBackStack(null).commit();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void gotoBusinessStats(View view)
    {
        // Tracking Event
        MyApplication.getInstance().trackEvent("Home", "Business Stats", "Tracking business stats");

          fragmentManager = getSupportFragmentManager();
          fragmentManager.beginTransaction().replace(R.id.content_frame, new BusinessStatsFragement()).addToBackStack(null).commit();
    }

    public void gotoSafetyStats(View view)
    {
        if(MyUtils.ifNetworkPresent(this))
        {
            fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, new SafetyStatsFragement()).addToBackStack(null).commit();
        }
        else
        {
            MyUtils.showAlert(this, getResources().getString(R.string.network_not_available));
        }

    }

    public void gotoAboutUs(View view)
    {
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                //.setCustomAnimations( R.anim.slide_in_left, 0, 0, R.anim.slide_out_left)
                .replace(R.id.content_frame, new AboutUsFragement()).addToBackStack(null).commit();
    }

    public void gotoDivisions(View view)
    {
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, new ServicesFragement()).addToBackStack(null).commit();
    }

    public void gotoCompanies(View view)
    {
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, new CompaniesFragement()).addToBackStack(null).commit();
    }

    public void gotoLocations(View view)
    {
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, new LocationsFragement()).addToBackStack(null).commit();
    }

    public void gotoHome(View view)
    {
        fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fragmentManager.beginTransaction().replace(R.id.content_frame, new HomeFragement()).commit();
    }

    public void gotoContactUs(View view)
    {
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, new ContactUsFragement()).addToBackStack(null).commit();
    }

    public void gotoIndustryRecognition(View view)
    {
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("about_selected_tab", "industry_recognition");
        editor.commit();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, new AboutUsFragement()).addToBackStack(null).commit();
    }

}
