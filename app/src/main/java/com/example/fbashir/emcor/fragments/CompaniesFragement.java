package com.example.fbashir.emcor.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fbashir.emcor.R;
import com.example.fbashir.emcor.adapters.CompaniesListAdaptar;
import com.example.fbashir.emcor.adapters.ServicesListAdaptar;
import com.example.fbashir.emcor.helpers.DBHandler;
import com.example.fbashir.emcor.helpers.MyUtils;
import com.example.fbashir.emcor.helpers.RestClient;
import com.example.fbashir.emcor.helpers.Spinner;
import com.example.fbashir.emcor.interfaces.EmcorPagesAPI;
import com.example.fbashir.emcor.models.companies.CompaniesClass;
import com.example.fbashir.emcor.models.divisions.DivisionsBasic;
import com.example.fbashir.emcor.models.divisions.DivisionsClass;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.fbashir.emcor.MainActivity.db;
import static com.example.fbashir.emcor.MainActivity.fragmentManager;


/**
 * Created by fbashir on 7/21/2016.
 */

public class CompaniesFragement extends Fragment {
    View myView;
    TextView title_textView;
    CompaniesListAdaptar companies_adapter;
    ServicesListAdaptar services_adapter;
    LinearLayout filteroptions_textview;
    ImageButton filter_button;
    EditText searchBox;
    String division_id = "";
    String location_latitide = "";
    String location_longitude = "";
    DivisionsClass divisions;
    CompaniesClass companies;
    LinearLayout filter_white_view;
    ListView services_listView;
    String[] alphabaticalList = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q",  "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };
    ListView sideList;
    ArrayList<String> choices = new ArrayList<String>();
    private static final String DATA_KEY = "SERVICES_DATA";
    private static final String COMPANIES_KEY = "COMPANIES_DATA";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.companies_layout,container,false);
        //getActivity().setTitle(R.string.menu_companies);
        title_textView = (TextView) getActivity().findViewById(R.id.toolbar_title);
        title_textView.setText(R.string.menu_companies);

        //check if industry recognition is selected
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        division_id = sharedPref.getString("selected_division", "");
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove("selected_division");
        editor.commit();

        getCompaniesData();
        getFiltersData();

        return myView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        final View searchbox_view = (View) view.findViewById(R.id.companies_searchbox);
        final View mainoptions_view = (View) view.findViewById(R.id.companies_main_options);

        Button search_button = (Button) view.findViewById(R.id.companies_search_button);
        final ImageButton cancelsearch_button = (ImageButton) view.findViewById(R.id.companies_cancel_search);
        filter_button = (ImageButton) view.findViewById(R.id.companies_filter_button);
        final Button companies_location_button = (Button) view.findViewById(R.id.companies_location_button);

        filter_white_view = (LinearLayout) view.findViewById(R.id.filter_white_view);
        filteroptions_textview = (LinearLayout) view.findViewById(R.id.companies_filter_options);
        searchBox =(EditText) myView.findViewById(R.id.companies_searchbox_edittext);
        //final TextView searchtext_holder_textview = (TextView) view.findViewById(R.id.companies_searchtext_holder);

        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchbox_view.setVisibility(View.VISIBLE);
                mainoptions_view.setVisibility(View.GONE);
                Drawable background_filter = (Drawable) getResources().getDrawable(R.drawable.button_border);
                filter_button.setBackground(background_filter);
                filteroptions_textview.setVisibility(View.GONE);
                filter_white_view.setVisibility(View.GONE);
                searchBox.requestFocus();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                //imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                //searchtext_holder_textview.setVisibility(View.GONE);
            }
        });

        cancelsearch_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchBox =(EditText) myView.findViewById(R.id.companies_searchbox_edittext);
                String searchText = searchBox.getText().toString();
                searchBox.setText("");
                searchbox_view.setVisibility(View.GONE);
                mainoptions_view.setVisibility(View.VISIBLE);
                Drawable background_filter = (Drawable) getResources().getDrawable(R.drawable.button_border);
                filter_button.setBackground(background_filter);
                filteroptions_textview.setVisibility(View.GONE);
                filter_white_view.setVisibility(View.GONE);
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                //imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

                //searchtext_holder_textview.setText(searchText);
                //searchtext_holder_textview.setVisibility(View.VISIBLE);
            }
        });

        filter_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(filteroptions_textview.getVisibility() == View.VISIBLE)
                {
                    Drawable background_filter = (Drawable) getResources().getDrawable(R.drawable.button_border);
                    filter_button.setBackground(background_filter);
                    filteroptions_textview.setVisibility(View.GONE);
                    filter_white_view.setVisibility(View.GONE);
                }
                else
                {
                    filter_white_view.setVisibility(View.VISIBLE);
                    filter_button.setBackgroundColor(Color.WHITE);
                    filteroptions_textview.setVisibility(View.VISIBLE);
                    //filteroptions_textview.animate().alpha(1.0f);
//                    MyUtils utils = new MyUtils();
//                    utils.SlideUP(filteroptions_textview, getContext());



                    //filteroptions_textview.bringToFront();
                    //filteroptions_textview.setZ(100);
                }


            }
        });


        companies_location_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                final Spinner spinner = new Spinner(getContext());
//                spinner.getProgressDialog().show();
                fragmentManager.beginTransaction().replace(R.id.content_frame, new LocationsFragement()).addToBackStack(null).commit();
            }
        });


        //searchbox

        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(companies.body.info.size() > 0)
                {
                    companies_adapter.filter(s.toString());
                }

                //companies_adapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    public void setFiltersData(final ArrayList<DivisionsBasic> divisions)
    {
        //services list adapter
        //if(services_adapter == null)
        {
            services_adapter = new ServicesListAdaptar(getActivity(), divisions, false);
            services_listView = (ListView) myView.findViewById(R.id.company_filter_list);

            services_listView.setAdapter(services_adapter);


            if(!division_id.equals(""))
            {
                for(int i=0;i<divisions.size();i++)
                {
                    if(division_id.equals(divisions.get(i).division_id))
                    {
                        services_listView.setItemChecked(i,true);
                    }
                }
            }

            services_listView.setOnItemClickListener(new OnItemClickListener() {
                //private Set<Integer> hasClickedSet = new HashSet<Integer>();
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    TextView textView = (TextView) view.findViewById(R.id.divisions_textView);
                    //Log.d("division_id",textView.getTag().toString());
                    String temp_division_id = textView.getTag().toString();

                    //if(temp_division_id.equals(division_id))
                    if(MyUtils.valueExists(choices, temp_division_id))
                    {
                        choices = MyUtils.removeValue(choices, temp_division_id);
                        division_id = "";
                        View temp_view = services_listView.getChildAt(position);
                        temp_view.setSelected(false);
                        //services_listView.clearChoices();
                        services_adapter.notifyDataSetChanged();
                    }
                    else
                    {
                        choices.add(temp_division_id);
                        division_id = temp_division_id;
                        view.setSelected(true);
                    }

                    //hideServiceFilter();
                    //if(companies.body.info.size() > 0)
                    if(companies_adapter.getTotalCompaniesCount() > 0)
                    {
                        companies_adapter.filterByDivision(choices);
                    }


                }
            });
        }
    }

    public void hideServiceFilter()
    {
        Drawable background_filter = (Drawable) getResources().getDrawable(R.drawable.button_border);
        filter_button.setBackground(background_filter);
        filteroptions_textview.setVisibility(View.GONE);
        filter_white_view.setVisibility(View.GONE);
    }

    //final CompaniesClass companies
    public void setCompaniesData()
    {
        //companies list adapter
        companies_adapter = new CompaniesListAdaptar(getActivity(), companies.body.info);
        final ListView listView = (ListView) myView.findViewById(R.id.company_list);

        listView.setOnItemClickListener(new OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {

                SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                TextView textView = (TextView) v.findViewById(R.id.companies_textView);
                editor.putString("selected_company_id", textView.getTag().toString());
                editor.commit();
                fragmentManager.beginTransaction().replace(R.id.content_frame, new CompanyDetailsFragement()).addToBackStack(null).commit();

            }
        });

        listView.setAdapter(companies_adapter);

        listView.setFastScrollEnabled(true);

        if(!division_id.equals(""))
        {
            choices.add(division_id);
            companies_adapter.filterByDivision(choices);
        }


        sideList = (ListView) myView.findViewById(R.id.alphabet_list);

        ArrayAdapter<String> alphabaticalListAdapter = new ArrayAdapter<String>( getContext(), R.layout.alphabet_listview_layout, alphabaticalList);
        sideList.setAdapter(alphabaticalListAdapter);

        sideList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,final int position, long id) {

                sideList.post(new Runnable() {
                    @Override
                    public void run() {
                        companies_adapter.notifyDataSetChanged();
                        String alphabet = alphabaticalList[position];
                        int sectionPosition = companies_adapter.getPositionForAlphabet(alphabet);
                        if(sectionPosition != -1)
                        {
                            listView.smoothScrollToPosition(sectionPosition);
                        }

                    }
                });
            }
        });

    }

    public void getCompaniesData()
    {
        //main work start
        final Spinner spinner = new Spinner(getContext());
        spinner.getProgressDialog().show();


        final Gson gson = new Gson();
        DBHandler.LocalData localData = db.getData(COMPANIES_KEY);
        Calendar oneWeekAhead = Calendar.getInstance();
        oneWeekAhead.add(Calendar.DATE, 7);

        if(localData != null && oneWeekAhead.getTimeInMillis() > localData.time)
        {
            companies = gson.fromJson(localData.data, CompaniesClass.class);
            setCompaniesData();
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
            String temp_division_id = "";
            Call<CompaniesClass> call = service.getCompanies(temp_division_id,
                    location_latitide,
                    location_longitude);

            call.enqueue(new Callback<CompaniesClass>() {
                @Override
                public void onResponse(Call<CompaniesClass> call, Response<CompaniesClass> response) {

                    if(spinner.getProgressDialog().isShowing())
                    {
                        spinner.getProgressDialog().dismiss();
                    }

                    int code = response.code();
                    companies = response.body();
                    if(code == 200)
                    {
                        setCompaniesData();
                        db.addData(COMPANIES_KEY,gson.toJson(companies), System.currentTimeMillis());
                    }
                    else
                    {
                        MyUtils.showAlert(getContext(), response.body().header.message);
                        //Toast.makeText(getContext(),response.body().header.message.toString(),  Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<CompaniesClass> call, Throwable t) {

                    if(spinner.getProgressDialog().isShowing())
                    {
                        spinner.getProgressDialog().dismiss();
                    }
                    MyUtils.showAlert(getContext(), getResources().getString(R.string.some_error));
                    //Toast.makeText(getContext(), t.getMessage() , Toast.LENGTH_LONG).show();

                }
            });
        }
    }

    public void getFiltersData()
    {
        final Gson gson = new Gson();
        DBHandler.LocalData localData = db.getData(DATA_KEY);
        Calendar oneWeekAhead = Calendar.getInstance();
        oneWeekAhead.add(Calendar.DATE, 7);

        if(localData != null && oneWeekAhead.getTimeInMillis() > localData.time)
        {
            //divisions = gson.fromJson(localData.data, DivisionsClass.class);
            ArrayList<DivisionsBasic> divisionsArray = gson.fromJson(localData.data, new TypeToken<ArrayList<DivisionsBasic>>(){}.getType());
            setFiltersData(divisionsArray);
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

                    int code = response.code();
                    if(code == 200)
                    {
                        divisions = response.body();
                        db.addData(DATA_KEY,gson.toJson(divisions.body.info), System.currentTimeMillis());
                        setFiltersData(divisions.body.info);
                    }
                    else
                    {
                        //Toast.makeText(getContext(),divisions.header.message.toString(),  Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onFailure(Call<DivisionsClass> call, Throwable t) {
                    //Toast.makeText(getContext(), "Some error" , Toast.LENGTH_LONG).show();
                }
            });


        }

    }
}
