package com.example.fbashir.emcor.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fbashir.emcor.R;
import com.example.fbashir.emcor.helpers.DBHandler;
import com.example.fbashir.emcor.helpers.MyUtils;
import com.example.fbashir.emcor.helpers.RestClient;
import com.example.fbashir.emcor.helpers.Spinner;
import com.example.fbashir.emcor.interfaces.EmcorPagesAPI;
import com.example.fbashir.emcor.models.startup.StartupClass;
import com.google.gson.Gson;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.fbashir.emcor.MainActivity.db;
import static com.example.fbashir.emcor.MainActivity.fragmentManager;


/**
 * Created by fbashir on 7/21/2016.
 */

public class SplashFragement extends Fragment {
    View myView;
    TextView title_textView;
    ImageView splash_image;

    private static final String HOME_KEY = "HOME_DATA";
    private static final String DIVISIONS_KEY = "SERVICES_DATA";
    private static final String APPCONFIG_KEY = "APPCONFIG_DATA";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.splash_layout,container,false);
        title_textView = (TextView) getActivity().findViewById(R.id.toolbar_title);
        title_textView.setText(R.string.company_name);

        getAppConfigData();

        return myView;

    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {

    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }
    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }

    public void getAppConfigData()
    {
        final Gson gson = new Gson();
        final Spinner spinner = new Spinner(getContext());
        spinner.getProgressDialog().show();

        DBHandler.LocalData appConfigData = db.getData(APPCONFIG_KEY);
        DBHandler.LocalData divisionsData = db.getData(DIVISIONS_KEY);
        DBHandler.LocalData homeData = db.getData(HOME_KEY);
        Calendar oneWeekAhead = Calendar.getInstance();
        oneWeekAhead.add(Calendar.DATE, 7);

        if(appConfigData != null && oneWeekAhead.getTimeInMillis() > appConfigData.time)
        {
            if(spinner.getProgressDialog().isShowing())
            {
                spinner.getProgressDialog().dismiss();
            }
            fragmentManager.beginTransaction().replace(R.id.content_frame, new HomeFragement()).commit();
        }
        else
        {
            String end_point = getResources().getString(R.string.server_base_url);
            RestClient restClient = new RestClient(end_point);

            EmcorPagesAPI service = restClient.getService().create(EmcorPagesAPI.class);
            Call<StartupClass> call = service.startup();

            call.enqueue(new Callback<StartupClass>() {
                @Override
                public void onResponse(Call<StartupClass> call, Response<StartupClass> response) {

                    if(spinner.getProgressDialog().isShowing())
                    {
                        spinner.getProgressDialog().dismiss();
                    }

                    StartupClass startupClass = response.body();
                    if(response.code() == 200)
                    {
                        db.addData(HOME_KEY,gson.toJson(startupClass.body.home), System.currentTimeMillis());
                        db.addData(DIVISIONS_KEY,gson.toJson(startupClass.body.divisions), System.currentTimeMillis());
                        db.addData(APPCONFIG_KEY,gson.toJson(startupClass.body.app_config), System.currentTimeMillis());
                        fragmentManager.beginTransaction().replace(R.id.content_frame, new HomeFragement()).commit();

                    }
                    else
                    {
                        MyUtils.showAlert(getContext(), response.body().header.error);
                    }


                }

                @Override
                public void onFailure(Call<StartupClass> call, Throwable t) {
                    title_textView = (TextView) getActivity().findViewById(R.id.toolbar_title);
                    title_textView.setText(R.string.menu_home);

                    if(spinner.getProgressDialog().isShowing())
                    {
                        spinner.getProgressDialog().dismiss();
                    }
                    MyUtils.showAlert(getContext(), getResources().getString(R.string.some_error));
                }
            });
    }


        }


}
