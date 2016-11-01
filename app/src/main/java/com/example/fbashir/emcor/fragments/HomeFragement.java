package com.example.fbashir.emcor.fragments;

//import android.app.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.example.fbashir.emcor.R;
import com.example.fbashir.emcor.helpers.DBHandler;
import com.example.fbashir.emcor.helpers.MyUtils;
import com.example.fbashir.emcor.helpers.RestClient;
import com.example.fbashir.emcor.helpers.Spinner;
import com.example.fbashir.emcor.interfaces.EmcorPagesAPI;
import com.example.fbashir.emcor.models.businessServiceDetails.BusinessServiceDetailsBasic;
import com.example.fbashir.emcor.models.startup.StartupClass;
import com.google.gson.Gson;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.fbashir.emcor.MainActivity.db;

/**
 * Created by fbashir on 7/21/2016.
 */

public class HomeFragement extends Fragment {
    View myView;
    SliderLayout sliderShow;
    TextView title_textView;
    private static final String HOME_KEY = "HOME_DATA";
    private static final String DIVISIONS_KEY = "SERVICES_DATA";
    private static final String APPCONFIG_KEY = "APPCONFIG_DATA";

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.home_layout,container,false);
        //getActivity().setTitle("");

        getHomeData();

        return myView;

    }

    public void getHomeData()
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
            BusinessServiceDetailsBasic businessService = gson.fromJson(homeData.data, BusinessServiceDetailsBasic.class);
            setHomeData(businessService);
            if(spinner.getProgressDialog().isShowing())
            {
                spinner.getProgressDialog().dismiss();
            }
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
                        setHomeData(startupClass.body.home);
                    }
                    else
                    {
                        MyUtils.showAlert(getContext(), response.body().header.error);
                        //Toast.makeText(getContext(),response.body().header.error,Toast.LENGTH_LONG).show();
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
                    //Toast.makeText(getContext(),"Some error",Toast.LENGTH_LONG).show();
                }
            });
        }
    }

//    public void getHomeDataOld()
//    {
//        final Gson gson = new Gson();
//        final Spinner spinner = new Spinner(getContext());
//        spinner.getProgressDialog().show();
//
//        DBHandler.LocalData localData = db.getData(HOME_KEY);
//        Calendar oneWeekAhead = Calendar.getInstance();
//        oneWeekAhead.add(Calendar.DATE, 7);
//
//
//
//        if(localData != null && oneWeekAhead.getTimeInMillis() > localData.time)
//        {
//            BusinessServiceClass businessService = gson.fromJson(localData.data, BusinessServiceClass.class);
//            setHomeData(businessService);
//            if(spinner.getProgressDialog().isShowing())
//            {
//                spinner.getProgressDialog().dismiss();
//            }
//        }
//        else
//        {
//            String end_point = getResources().getString(R.string.server_base_url);
//            RestClient restClient = new RestClient(end_point);
//
//            EmcorPagesAPI service = restClient.getService().create(EmcorPagesAPI.class);
//            Call<BusinessServiceClass> call = service.getBusinessServiceInfo();
//
//            call.enqueue(new Callback<BusinessServiceClass>() {
//                @Override
//                public void onResponse(Call<BusinessServiceClass> call, Response<BusinessServiceClass> response) {
//
//                    if(spinner.getProgressDialog().isShowing())
//                    {
//                        spinner.getProgressDialog().dismiss();
//                    }
//
//                    BusinessServiceClass businessService = response.body();
//                    if(response.code() == 200)
//                    {
//                        db.addData(HOME_KEY,gson.toJson(businessService), System.currentTimeMillis());
//                        setHomeData(businessService);
//                    }
//                    else
//                    {
//                        Toast.makeText(getContext(),response.body().header.error,Toast.LENGTH_LONG).show();
//                    }
//
//
//                }
//
//                @Override
//                public void onFailure(Call<BusinessServiceClass> call, Throwable t) {
//                    title_textView = (TextView) getActivity().findViewById(R.id.toolbar_title);
//                    title_textView.setText(R.string.menu_home);
//
//                    if(spinner.getProgressDialog().isShowing())
//                    {
//                        spinner.getProgressDialog().dismiss();
//                    }
//                    Toast.makeText(getContext(),"Some error",Toast.LENGTH_LONG).show();
//                }
//            });
//        }
//
//    }

    public void setHomeData(BusinessServiceDetailsBasic businessService)
    {
        //getActivity().setTitle(businessService.body.info.title);
        title_textView = (TextView) getActivity().findViewById(R.id.toolbar_title);
        title_textView.setText(businessService.title);

        sliderShow = (SliderLayout) myView.findViewById(R.id.home_image);

        DefaultSliderView[] sliderViews = new DefaultSliderView[businessService.images.length];
        for(int i=0;i<businessService.images.length;i++)
        {
            sliderViews[i] = new DefaultSliderView(getContext());
            sliderViews[i]
                    .image(businessService.images[i]);

            sliderShow.addSlider(sliderViews[i]);
        }

//                    ImageView imageView = (ImageView) myView.findViewById(R.id.home_image);
//                    Picasso.with(getContext()).load(businessService.body.info.images[0]).into(imageView);

        TextView textView = (TextView) myView.findViewById(R.id.home_text);
        textView.setText(businessService.description);
    }

//    @Override
//    public void onStop() {
//        sliderShow.stopAutoCycle();
//        super.onStop();
//    }


}
