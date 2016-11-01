package com.example.fbashir.emcor.fragments;


import android.content.Context;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.fbashir.emcor.R;
import com.example.fbashir.emcor.adapters.ServicesListAdaptar;
import com.example.fbashir.emcor.helpers.DBHandler;
import com.example.fbashir.emcor.helpers.MyUtils;
import com.example.fbashir.emcor.helpers.RestClient;
import com.example.fbashir.emcor.helpers.Spinner;
import com.example.fbashir.emcor.interfaces.EmcorPagesAPI;
import com.example.fbashir.emcor.models.divisions.DivisionsBasic;
import com.example.fbashir.emcor.models.divisions.DivisionsClass;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.fbashir.emcor.MainActivity.db;
import static com.example.fbashir.emcor.MainActivity.fragmentManager;


/**
 * Created by fbashir on 7/21/2016.
 */

public class ServicesFragement extends Fragment {
    View myView;
    private static final String DATA_KEY = "SERVICES_DATA";
    TextView title_textView;
    DivisionsClass divisions;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.services_layout,container,false);
        //getActivity().setTitle("Divisions");
        title_textView = (TextView) getActivity().findViewById(R.id.toolbar_title);
        title_textView.setText(R.string.divisions_text);

        getServicesData();

        return myView;

    }

    public void getServicesData()
    {
        //main work start
        final Gson gson = new Gson();
        final Spinner spinner = new Spinner(getContext());
        spinner.getProgressDialog().show();

        DBHandler.LocalData localData = db.getData(DATA_KEY);
        final Calendar oneWeekAhead = Calendar.getInstance();
        oneWeekAhead.add(Calendar.DATE, 7);

        if(localData != null && oneWeekAhead.getTimeInMillis() > localData.time)
        {
            ArrayList<DivisionsBasic> divisionsArray = gson.fromJson(localData.data, new TypeToken<ArrayList<DivisionsBasic>>(){}.getType());
            setServicesData(divisionsArray);
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
            Call<DivisionsClass> call = service.getDivisoins();

            call.enqueue(new Callback<DivisionsClass>() {
                @Override
                public void onResponse(Call<DivisionsClass> call, Response<DivisionsClass> response) {

                    if(spinner.getProgressDialog().isShowing())
                    {
                        spinner.getProgressDialog().dismiss();
                    }

                    int code = response.code();
                    divisions = response.body();
                    if(code == 200)
                    {
                        db.addData(DATA_KEY,gson.toJson(divisions.body.info), System.currentTimeMillis());
                        setServicesData(divisions.body.info);

//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("divisions", divisions);
//                    ServicesFragement fragment = new ServicesFragement();
//                    fragment.setArguments(bundle);
//                    fragmentManager = getSupportFragmentManager();
//                    fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).addToBackStack(null).commit();
                    }
                    else
                    {
                        MyUtils.showAlert(getContext(), divisions.header.message);
                        //Toast.makeText(getContext(),divisions.header.message.toString(),  Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onFailure(Call<DivisionsClass> call, Throwable t) {
                    if(spinner.getProgressDialog().isShowing())
                    {
                        spinner.getProgressDialog().dismiss();
                    }
                    MyUtils.showAlert(getContext(), getResources().getString(R.string.some_error));
                    //Toast.makeText(getContext(), "Some error" , Toast.LENGTH_LONG).show();
                }
            });
            //main work end

        }



    }

    public void setServicesData(final ArrayList<DivisionsBasic> divisions)
    {
        ServicesListAdaptar services = new ServicesListAdaptar(getActivity(), divisions, true);

        ListView listView = (ListView) myView.findViewById(R.id.services_list);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                v.setSelected(true);
                SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("selected_division", divisions.get(position).division_id);
                editor.commit();
                fragmentManager.beginTransaction().replace(R.id.content_frame, new CompaniesFragement()).addToBackStack(null).commit();
            }
        });




        listView.setAdapter(services);

    }
}
