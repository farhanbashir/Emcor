package com.example.fbashir.emcor.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
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
import com.google.gson.Gson;

import java.util.Calendar;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.fbashir.emcor.MainActivity.db;


/**
 * Created by fbashir on 7/21/2016.
 */

public class SafetyStatsFragement extends Fragment {
    View myView;
    TextView title_textView;
    LinearLayout preview_linear_layout;
    LinearLayout email_linear_layout;
    //LinearLayout download_linear_layout;
    Uri pdf_uri = null;
    String pdf_link;
    AppConfig appconfig;
    private static final String HOME_KEY = "HOME_DATA";
    private static final String DIVISIONS_KEY = "SERVICES_DATA";
    private static final String APPCONFIG_KEY = "APPCONFIG_DATA";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.safety_stats_layout,container,false);
        title_textView = (TextView) getActivity().findViewById(R.id.toolbar_title);
        title_textView.setText(R.string.safety_stats_text);
        getAppConfigData();
        return myView;

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
            pdf_link = appconfig.pdf_safety_stats;
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
                        pdf_link = appconfig.pdf_safety_stats;
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

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        //download options
        preview_linear_layout = (LinearLayout) myView.findViewById(R.id.download_preview_button);
        email_linear_layout = (LinearLayout) myView.findViewById(R.id.download_email_button);
        //download_linear_layout = (LinearLayout) myView.findViewById(R.id.download_download_button);

        preview_linear_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preview_linear_layout.setBackgroundColor(getResources().getColor(R.color.black));
                email_linear_layout.setBackgroundColor(getResources().getColor(R.color.tab_background_unselected));
                //download_linear_layout.setBackgroundColor(getResources().getColor(R.color.tab_background_unselected));

                MyUtils.openViewer(getContext(), pdf_link);
//                                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.test_pdf)));
//                                        startActivity(browserIntent);

            }
        });

        email_linear_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preview_linear_layout.setBackgroundColor(getResources().getColor(R.color.tab_background_unselected));
                email_linear_layout.setBackgroundColor(getResources().getColor(R.color.black));
                //download_linear_layout.setBackgroundColor(getResources().getColor(R.color.tab_background_unselected));

                MyUtils.openMailer(getContext(), pdf_link);

            }
        });

//        download_linear_layout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                preview_linear_layout.setBackgroundColor(getResources().getColor(R.color.tab_background_unselected));
//                email_linear_layout.setBackgroundColor(getResources().getColor(R.color.tab_background_unselected));
//                download_linear_layout.setBackgroundColor(getResources().getColor(R.color.black));
//
//
//
//            }
//        });


        WebView wv = (WebView) view.findViewById(R.id.webview);
        wv.getSettings().setJavaScriptEnabled(true);
        //wv.getSettings().setPluginsEnabled(true);
        wv.getSettings().setAllowFileAccess(true);
        //wv.loadUrl(doc);
        wv.loadUrl("https://docs.google.com/gview?embedded=true&url="+pdf_link);

        final Spinner spinner = new Spinner(getContext());
        spinner.getProgressDialog().show();

        wv.setWebViewClient(new WebViewClient(){
            public void onPageFinished(WebView view, String url) {
                // do your stuff here
                if(spinner.getProgressDialog().isShowing())
                {
                    spinner.getProgressDialog().dismiss();
                }
            }
        });

    }

    public void download()
    {
        final Spinner spinner = new Spinner(getContext());
        spinner.getProgressDialog().show();
        String end_point = getResources().getString(R.string.server_base_url);
        RestClient restClient = new RestClient(end_point);
        EmcorPagesAPI service = restClient.getService().create(EmcorPagesAPI.class);
        Call<ResponseBody> call = service.downloadFileWithDynamicUrlSync(getResources().getString(R.string.test_pdf));//pdf_link

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(spinner.getProgressDialog().isShowing())
                {
                    spinner.getProgressDialog().dismiss();
                }

                if(response.code() == 200)
                {
                    Log.d("success","server success");

                    pdf_uri = MyUtils.writeResponseBodyToDisk(response.body(), pdf_link, pdf_uri);
                }
                else
                {
                    Log.d("fault","server fault");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if(spinner.getProgressDialog().isShowing())
                {
                    spinner.getProgressDialog().dismiss();
                }
                Log.d("fault","server fault failure");
            }
        });
    }
}
