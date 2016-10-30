package com.example.fbashir.emcor.fragments;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fbashir.emcor.R;
import com.example.fbashir.emcor.helpers.DBHandler;
import com.example.fbashir.emcor.helpers.MyUtils;
import com.example.fbashir.emcor.helpers.RestClient;
import com.example.fbashir.emcor.helpers.Spinner;
import com.example.fbashir.emcor.interfaces.EmcorPagesAPI;
import com.example.fbashir.emcor.models.startup.AppConfig;
import com.example.fbashir.emcor.models.startup.StartupClass;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.gson.Gson;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.fbashir.emcor.MainActivity.db;



/**
 * Created by fbashir on 7/21/2016.
 */

public class BusinessStatsFragement extends Fragment {
    View myView;
    TextView title_textView;
    TextView first_white_box;
    ScrollView scroll;
    TextView hat_summary;
    ImageView hat_image;
    ImageView missing_image;
    TextView stats_missing_children_recovered;
    TextView missing_small_text;
    TextView missing_summary;
    TextView stats_work_fortune_500;
    TextView text_work_fortune_500;
    ImageView employee_image;
    TextView stats_employees;
    TextView employee_small_text;
    TextView text_employee;
    TextView stats_reduction;
    TextView reduction_small_text;
    TextView text_reduction;
    TextView text_troop_support_program;
    ImageView vigilant_image;
    TextView stats_bim_designers;
    TextView designers_small_text;
    TextView stats_top_admired;
    TextView admired_small_text;
    TextView text_top_admired;
    TextView second_white_text;
    TextView total_locations;
    TextView locations_small_text;
    TextView total_employees;
    TextView employees_heading;
    TextView total_companies;
    TextView companies_heading;
    ImageView flag_image;
    ImageView designers_image;
    ImageView fortune_image;
    ImageView map_image;




    AppConfig appconfig;
    private static final String HOME_KEY = "HOME_DATA";
    private static final String DIVISIONS_KEY = "SERVICES_DATA";
    private static final String APPCONFIG_KEY = "APPCONFIG_DATA";
    private static final Integer VISIBILITY = 99;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.business_stats_layout,container,false);
        title_textView = (TextView) getActivity().findViewById(R.id.toolbar_title);
        title_textView.setText(R.string.emcor_facts_text);

        getAppConfigData();

        return myView;

    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        scroll = (ScrollView) view.findViewById(R.id.scroller);
        hat_image = (ImageView) view.findViewById(R.id.hat_image);
        missing_image = (ImageView) view.findViewById(R.id.missing_image);
        hat_summary = (TextView) view.findViewById(R.id.hat_summary);
        missing_small_text = (TextView) view.findViewById(R.id.missing_small_text);
        stats_missing_children_recovered = (TextView) view.findViewById(R.id.stats_missing_children_recovered);
        missing_summary = (TextView) view.findViewById(R.id.missing_summary);
        stats_work_fortune_500 = (TextView) view.findViewById(R.id.stats_work_fortune_500);
        text_work_fortune_500 = (TextView) view.findViewById(R.id.text_work_fortune_500);
        employee_image = (ImageView) view.findViewById(R.id.employee_image);
        stats_employees = (TextView) view.findViewById(R.id.stats_employees);
        employee_small_text = (TextView) view.findViewById(R.id.employee_small_text);
        text_employee = (TextView) view.findViewById(R.id.text_employee);
        vigilant_image = (ImageView) view.findViewById(R.id.vigilant_image);
        stats_reduction = (TextView) view.findViewById(R.id.stats_reduction);
        reduction_small_text = (TextView) view.findViewById(R.id.reduction_small_text);
        text_reduction = (TextView) view.findViewById(R.id.text_reduction);
        text_troop_support_program = (TextView) view.findViewById(R.id.text_troop_support_program);
        stats_bim_designers = (TextView) view.findViewById(R.id.stats_bim_designers);
        designers_small_text = (TextView) view.findViewById(R.id.designers_small_text);
        stats_top_admired = (TextView) view.findViewById(R.id.stats_top_admired);
        admired_small_text = (TextView) view.findViewById(R.id.admired_small_text);
        text_top_admired = (TextView) view.findViewById(R.id.text_top_admired);
        second_white_text = (TextView) view.findViewById(R.id.second_white_text);
        total_locations = (TextView) view.findViewById(R.id.total_locations);
        locations_small_text = (TextView) view.findViewById(R.id.locations_small_text);
        total_employees = (TextView) view.findViewById(R.id.total_employees);
        employees_heading = (TextView) view.findViewById(R.id.employees_heading);
        total_companies = (TextView) view.findViewById(R.id.total_companies);
        companies_heading = (TextView) view.findViewById(R.id.companies_heading);
        flag_image = (ImageView) view.findViewById(R.id.flag_image);
        designers_image = (ImageView) view.findViewById(R.id.designers_image);
        fortune_image = (ImageView) view.findViewById(R.id.fortune_image);
        map_image = (ImageView) view.findViewById(R.id.map_image);

        scroll.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {

                Rect scrollBounds = new Rect();
                scroll.getHitRect(scrollBounds);
                animateView(hat_image);
                animateView(hat_summary);

                animateView(missing_image);

                animateView(missing_small_text);

                animateView(stats_missing_children_recovered);

                animateView(missing_summary);

                animateView(stats_work_fortune_500);
                animateView(text_work_fortune_500);
                animateView(stats_employees);
                animateView(employee_small_text);
                animateView(text_employee);
                animateView(vigilant_image);
                animateView(stats_reduction);
                animateView(reduction_small_text);
                animateView(text_reduction);
                animateView(flag_image);
                animateView(text_troop_support_program);
                animateView(designers_image);
                animateView(stats_bim_designers);
                animateView(designers_small_text);
                animateView(employee_image);
                animateView(fortune_image);
                animateView(stats_top_admired);
                animateView(admired_small_text);
                animateView(text_top_admired);
                animateView(second_white_text);
                animateView(map_image);
                animateView(total_locations);
                animateView(locations_small_text);
                animateView(total_employees);
                animateView(employees_heading);
                animateView(total_companies);
                animateView(companies_heading);


            }
        });


    }

    public void animateView(View v)
    {
        Rect scrollBounds = new Rect();
        scroll.getHitRect(scrollBounds);
        if (v.getLocalVisibleRect(scrollBounds)&& v.getVisibility() == View.INVISIBLE) {
            if(getVisiblePercent(v) > VISIBILITY)
            {
                Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.slide_up);
                v.startAnimation(anim);
                v.setVisibility(View.VISIBLE);
            }

        }
    }

    public static int getVisiblePercent(View v) {
        Rect r = new Rect();
        v.getGlobalVisibleRect(r);
        double sVisible = r.width() * r.height();
        double sTotal = v.getWidth() * v.getHeight();
        return (int) (100 * sVisible / sTotal);

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
            appconfig = gson.fromJson(appConfigData.data, AppConfig.class);
            setAppConfigData();
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
                        appconfig = startupClass.body.app_config;
                        setAppConfigData();
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

    public void setAppConfigData()
    {
        String first_white_box_string = "With and operations network of 75+ companies stratigically positioned throughout 170+ locations, EMCOR's reach is braod--";
        first_white_box = (TextView) myView.findViewById(R.id.first_white_box);
        first_white_box.setText(Html.fromHtml(first_white_box_string+"<font color=\"#1a7d7f\">but our expertise and execution across industries remains solidly local.</font>"));

        TextView stats_projects = (TextView) myView.findViewById(R.id.stats_projects);
        stats_projects.setText(appconfig.stats_projects);


        TextView stats_projects_summary = (TextView) myView.findViewById(R.id.stats_projects_summary);
        stats_projects_summary.setText(Html.fromHtml("As you read this, EMCOR is involved in <font color=\"#1a7d7f\">"+appconfig.stats_projects+"</font> projects"));

        hat_summary = (TextView) myView.findViewById(R.id.hat_summary);
        hat_summary.setText(Html.fromHtml("<font color=\"#D20878\">"+appconfig.text_pink_hard_hats+"</font> are worn by thousands of employees during October in support of breast cancer awareness. <b>Protect yourself. Get screend today</b>"));

        TextView stats_missing_children_recovered = (TextView) myView.findViewById(R.id.stats_missing_children_recovered);
        stats_missing_children_recovered.setText(appconfig.stats_missing_children_recovered);

        TextView missing_summary = (TextView) myView.findViewById(R.id.missing_summary);
        missing_summary.setText(Html.fromHtml("<font color=\"#009AE2\">Missing Kids:</font> displaying national center for Missing and Exploited childrenTM posters on EMCOR's <font color=\"#009AE2\">"+appconfig.text_missing_children_recovered+"</font> has helped recover "+appconfig.stats_missing_children_recovered+" missing kids As of "+appconfig.date_missing_children_recovered));

        TextView stats_work_fortune_500 = (TextView) myView.findViewById(R.id.stats_work_fortune_500);
        stats_work_fortune_500.setText(appconfig.stats_work_fortune_500);


        TextView text_work_fortune_500 = (TextView) myView.findViewById(R.id.text_work_fortune_500);
        text_work_fortune_500.setText(Html.fromHtml("We work for more than <font color=\"#1a7d7f\">"+appconfig.text_work_fortune_500+" of the Fortune 500</font>"));

        TextView stats_employees = (TextView) myView.findViewById(R.id.stats_employees);
        stats_employees.setText(appconfig.stats_employees);

        TextView text_employee = (TextView) myView.findViewById(R.id.text_employee);
        text_employee.setText(Html.fromHtml("EMCOR Experts: <font color=\"#C50189\">"+appconfig.stats_employees+" skilled employees</font> across the US and UK."));

        stats_reduction = (TextView) myView.findViewById(R.id.stats_reduction);
        stats_reduction.setText(appconfig.stats_reduction);

        text_reduction = (TextView) myView.findViewById(R.id.text_reduction);
        text_reduction.setText(Html.fromHtml("<font color=\"#F88601\">EMCOR group has an injury rate that is"+appconfig.stats_reduction+" lower than compareable industry averages.</font> The company is working 6.5 million more hours with more than 500 fewer injuries per year, for a rate of injury that is 64% lower than 10 years ago."));

        TextView text_troop_support_program = (TextView) myView.findViewById(R.id.text_troop_support_program);
        text_troop_support_program.setText(Html.fromHtml("<font color=\"#DE1A2E\">"+appconfig.text_troop_support_program+"+ EMCOR troop support program</font> sends packages containing useful items and small reminders of home to our employeees and family members serving our country."));


        TextView stats_bim_designers = (TextView) myView.findViewById(R.id.stats_bim_designers);
        stats_bim_designers.setText(appconfig.stats_bim_designers);

        TextView stats_top_admired = (TextView) myView.findViewById(R.id.stats_top_admired);
        stats_top_admired.setText("TOP "+appconfig.stats_top_admired);

        TextView text_top_admired = (TextView) myView.findViewById(R.id.text_top_admired);
        text_top_admired.setText(Html.fromHtml("Named by Fortune for the 8th consecutive year as one of the top "+appconfig.stats_top_admired+"<font color=\"#1a7d7f\">World's more admired companies in the engineering and construction industry</font>"));

        second_white_text = (TextView) myView.findViewById(R.id.second_white_text);
        second_white_text.setText(Html.fromHtml("Expertise across 9 major markets-- <font color=\"#1a7d7f\">Biotech/Healthcare/Commercial/Education/Entertainment/Hospitality/Manufacturing/Industrial, Public/Government, Technology, Financial Service, Transportation</font>"));


        total_locations = (TextView) myView.findViewById(R.id.total_locations);
        total_locations.setText(appconfig.stats_locations);

        total_companies = (TextView) myView.findViewById(R.id.total_companies);
        total_companies.setText(appconfig.stats_companies);

        total_employees = (TextView) myView.findViewById(R.id.total_employees);
        total_employees.setText(appconfig.stats_employees);

    }

}
