package com.example.fbashir.emcor.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fbashir.emcor.R;
import com.example.fbashir.emcor.adapters.CompaniesLocationsListAdaptar;
import com.example.fbashir.emcor.adapters.PlacesAutoCompleteAdapter;
import com.example.fbashir.emcor.adapters.ServicesListAdaptar;
import com.example.fbashir.emcor.helpers.DBHandler;
import com.example.fbashir.emcor.helpers.MyUtils;
import com.example.fbashir.emcor.helpers.RestClient;
import com.example.fbashir.emcor.helpers.Spinner;
import com.example.fbashir.emcor.interfaces.EmcorPagesAPI;
import com.example.fbashir.emcor.models.companies.CompaniesClass;
import com.example.fbashir.emcor.models.divisions.DivisionsBasic;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.fbashir.emcor.MainActivity.db;
import static com.example.fbashir.emcor.MainActivity.fragmentManager;



/**
 * Created by fbashir on 7/21/2016.
 */

public class LocationsFragement extends Fragment  implements OnMapReadyCallback {
    View myView;
    TextView title_textView;
    Spinner spinner;
    private static GoogleMap mMap;
    private static Double latitude, longitude;
    SupportMapFragment mapFragment;
    CompaniesLocationsListAdaptar companies_locations_adapter;
    ServicesListAdaptar services_adapter;
    ImageButton filter_button;
    LinearLayout filteroptions_textview;
    CompaniesClass companies;
    String division_id = "";
    String location_latitide = "";
    String location_longitude = "";
    ArrayList<DivisionsBasic> divisions;
    LinearLayout filter_white_view;
    ArrayList<String> choices = new ArrayList<String>();
    private static final String DATA_KEY = "SERVICES_DATA";

    private PlacePicker.IntentBuilder builder;
    private PlacesAutoCompleteAdapter mPlacesAdapter;
    private AutoCompleteTextView myLocation;


    private static final LatLngBounds BOUNDS_GREATER_SYDNEY = new LatLngBounds(
            new LatLng(-34.041458, 150.790100), new LatLng(-33.682247, 151.383362));
    protected GoogleApiClient mGoogleApiClient;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (myView != null) {
            ViewGroup parent = (ViewGroup) myView.getParent();
            if (parent != null)
                parent.removeView(myView);
        }

        try{
            myView = inflater.inflate(R.layout.locations_layout,container,false);
        }
        catch(InflateException e){
            Log.d("inflate error", e.getMessage());
        }


        //getActivity().setTitle(R.string.menu_locations);
        title_textView = (TextView) getActivity().findViewById(R.id.toolbar_title);
        title_textView.setText(R.string.menu_locations);

        spinner = new Spinner(getContext());
        if(!spinner.getProgressDialog().isShowing())
        {
            spinner.getProgressDialog().show();
        }
        getCompaniesData();
        getFiltersData();
        return myView;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        //tabs work
        final TabLayout tabLayout = (TabLayout) view.findViewById(R.id.locations_tabs);
        final ListView locations_list = (ListView) view.findViewById(R.id.locations_list);
        final LinearLayout maps_list = (LinearLayout) view.findViewById(R.id.maps_list);
        filter_white_view = (LinearLayout) view.findViewById(R.id.filter_white_view);

        if(tabLayout.getTabCount() == 0)
        {
            View tab1 = (View) LayoutInflater.from(getContext()).inflate(R.layout.locations_custom_tab, null);
            tab1.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            //tab1.setBackground(getResources().getDrawable(R.drawable.locations_tab_colors));


            tabLayout.addTab(tabLayout.newTab().setCustomView(tab1));
            tabLayout.getTabAt(0).setText("List View");
            tabLayout.getTabAt(0).setIcon(R.drawable.nav_icon_gray);


            View tab2 = (View) LayoutInflater.from(getContext()).inflate(R.layout.locations_custom_tab, null);
            tab2.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            //tab2.setBackground(getResources().getDrawable(R.drawable.locations_tab_colors));

            tabLayout.addTab(tabLayout.newTab().setCustomView(tab2));
            tabLayout.getTabAt(1).setText("Map View");
            tabLayout.getTabAt(1).setIcon(R.drawable.map_marker_white);
        }





        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tabLayout.getSelectedTabPosition() == 0)
                {
                    tabLayout.getTabAt(0).setIcon(R.drawable.nav_icon_gray);
                    tabLayout.getTabAt(1).setIcon(R.drawable.map_marker_white);
                    locations_list.setVisibility(View.VISIBLE);
                    maps_list.setVisibility(View.GONE);
                }
                else if(tabLayout.getSelectedTabPosition() == 1)
                {
                    tabLayout.getTabAt(0).setIcon(R.drawable.nav_icon_white);
                    tabLayout.getTabAt(1).setIcon(R.drawable.map_marker_gray);
                    locations_list.setVisibility(View.GONE);
                    maps_list.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View v = (View) tab.getCustomView();

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        final View searchbox_view = (View) view.findViewById(R.id.maps_searchbox);
        final View mainoptions_view = (View) view.findViewById(R.id.locations_main_options);

        Button search_button = (Button) view.findViewById(R.id.locations_search_button);
        ImageButton cancelsearch_button = (ImageButton) view.findViewById(R.id.locations_cancel_search);
        filter_button = (ImageButton) view.findViewById(R.id.locations_filter_button);

        filteroptions_textview = (LinearLayout) view.findViewById(R.id.companies_filter_options);

        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchbox_view.setVisibility(View.VISIBLE);
                mainoptions_view.setVisibility(View.GONE);
                Drawable background_filter = (Drawable) getResources().getDrawable(R.drawable.button_border);
                filter_button.setBackground(background_filter);
                filteroptions_textview.setVisibility(View.GONE);
                filter_white_view.setVisibility(View.GONE);
            }
        });

        cancelsearch_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchbox_view.setVisibility(View.GONE);
                mainoptions_view.setVisibility(View.VISIBLE);
                Drawable background_filter = (Drawable) getResources().getDrawable(R.drawable.button_border);
                filter_button.setBackground(background_filter);
                filteroptions_textview.setVisibility(View.GONE);
                filter_white_view.setVisibility(View.GONE);
            }
        });

        filter_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(filteroptions_textview.getVisibility() == View.VISIBLE)
                {
                    filter_white_view.setVisibility(View.GONE);
                    Drawable background_filter = (Drawable) getResources().getDrawable(R.drawable.button_border);
                    filter_button.setBackground(background_filter);
                    filteroptions_textview.setVisibility(View.GONE);
                }
                else
                {
                    filter_white_view.setVisibility(View.VISIBLE);
                    filter_button.setBackgroundColor(Color.WHITE);
                    filteroptions_textview.setVisibility(View.VISIBLE);
                }


            }
        });


        //google places autocomplete
        mGoogleApiClient = new GoogleApiClient.Builder(getContext()).addApi(Places.GEO_DATA_API).build();


        builder = new PlacePicker.IntentBuilder();
        myLocation = (AutoCompleteTextView) myView.findViewById(R.id.maps_searchbox_edittext);
        mPlacesAdapter = new PlacesAutoCompleteAdapter(getContext(),
                android.R.layout.simple_list_item_1,
                mGoogleApiClient,
                BOUNDS_GREATER_SYDNEY,
                null);
        myLocation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final PlacesAutoCompleteAdapter.PlaceAutocomplete item = mPlacesAdapter.getItem(position);
                final String placeId = String.valueOf(item.placeId);
                PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                        .getPlaceById(mGoogleApiClient, placeId);
                placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
                    @Override
                    public void onResult(PlaceBuffer places) {
                        if (!places.getStatus().isSuccess()) {
                            Log.e("place", "Place query did not complete. Error: " +
                                    places.getStatus().toString());
                            return;
                        }
                        // Selecting the first object buffer.
                        final Place place = places.get(0);
                        LatLng latLng = place.getLatLng();

                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

                        location_latitide = Double.toString(latLng.latitude);
                        location_longitude = Double.toString(latLng.longitude);
                        getCompaniesData();
                    }
                });
            }
        });
        myLocation.setAdapter(mPlacesAdapter);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//
//
//
//        // Add a marker in Sydney, Australia, and move the camera.
//        LatLng sydney = new LatLng(-34, 151);
//        LatLng location = new LatLng(latitude, longitude);
//        mMap.addMarker(new MarkerOptions().position(location).title("Marker in Pakistan"));
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
    }

    public void setCompaniesData(final CompaniesClass companies)
    {
        companies_locations_adapter = new CompaniesLocationsListAdaptar(getActivity(), companies.body.info);
        ListView listView_locations = (ListView) myView.findViewById(R.id.locations_list);
        listView_locations.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {

                SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("selected_company_id", companies.body.info.get(position).company_id);
                editor.commit();
                fragmentManager.beginTransaction().replace(R.id.content_frame, new CompanyDetailsFragement()).addToBackStack(null).commit();

            }
        });
        listView_locations.setAdapter(companies_locations_adapter);


        //services list adapter
        if(services_adapter == null)
        {
            services_adapter = new ServicesListAdaptar(getActivity(), divisions, false);
            final ListView services_listView = (ListView) myView.findViewById(R.id.company_filter_list);

            services_listView.setAdapter(services_adapter);
            services_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TextView textView = (TextView) view.findViewById(R.id.divisions_textView);
                    Log.d("division_id",textView.getTag().toString());
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
                    if(companies_locations_adapter.getTotalCompaniesCount() > 0)
                    {
                        companies_locations_adapter.filterByDivision(choices);
                        mMap.clear();
                        setupMap(companies);
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

    public void getCompaniesData()
    {
//        latitude = 26.78;
//        longitude = 72.56;

        mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.location_map);
        //mapFragment.getMapAsync(this);

        //main work start
        if(!spinner.getProgressDialog().isShowing())
        {
            spinner.getProgressDialog().show();
        }

        String end_point = getResources().getString(R.string.server_base_url);
        RestClient restClient = new RestClient(end_point);

        EmcorPagesAPI service = restClient.getService().create(EmcorPagesAPI.class);
        Call<CompaniesClass> call = service.getCompanies(division_id, location_latitide, location_longitude);

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
                    //set companies data
                    setCompaniesData(companies);

                    //set map data
                    setupMap(companies);

                }
                else
                {
                    MyUtils.showAlert(getContext(), companies.header.message);
                    //Toast.makeText(getContext(),companies.header.message.toString(),  Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<CompaniesClass> call, Throwable t) {

                if(spinner.getProgressDialog().isShowing())
                {
                    spinner.getProgressDialog().dismiss();
                }
                MyUtils.showAlert(getContext(), getResources().getString(R.string.some_error));
                //Toast.makeText(getContext(), "Some error" , Toast.LENGTH_LONG).show();

            }
        });
    }

    public void setupMap(final CompaniesClass companies)
    {
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                    @Override
                    public View getInfoWindow(Marker marker) {
                        // Getting view from the layout file info_window_layout
                        View v = getActivity().getLayoutInflater().inflate(R.layout.custom_map_info_view, null);

                        String str = marker.getTitle();
                        final String[] str2= str.split("_");

                        // Getting the position from the marker
                        LatLng latLng = marker.getPosition();


                        TextView name = (TextView) v.findViewById(R.id.map_company_name);
                        TextView address = (TextView) v.findViewById(R.id.map_company_address);
                        TextView telephone = (TextView) v.findViewById(R.id.map_company_telephone);
                        TextView fax = (TextView) v.findViewById(R.id.map_company_fax);
                        TextView toll = (TextView) v.findViewById(R.id.map_company_toll);

                        name.setText(str2[1]);
                        address.setText(str2[2]);


                        if(str2.length > 3)
                        {
                            telephone.setText("Tel: "+str2[3]);
                        }
                        if(str2.length > 4)
                        {
                            fax.setText("Fax: "+str2[4]);
                        }
                        if(str2.length > 5)
                        {
                            toll.setText("Toll free: "+str2[5]);
                        }


                        // Returning the view containing InfoWindow contents
                        return v;
                    }

                    @Override
                    public View getInfoContents(Marker marker) {
                        return null;
                    }
                });

                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        String str = marker.getTitle();
                        final String[] str2= str.split("_");

                        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("selected_company_id", str2[0]);
                        editor.commit();
                        fragmentManager.beginTransaction().replace(R.id.content_frame, new CompanyDetailsFragement()).addToBackStack(null).commit();

                    }
                });

                LatLng location = null;
                for(int i=0;i<companies.body.info.size();i++)
                {
                    if((division_id.equals("") || division_id.equals(companies.body.info.get(i).division_id)) && (!companies.body.info.get(i).latitude.equals("0") && !companies.body.info.get(i).longitude.equals("0")))
                    {
                        location = new LatLng(Double.parseDouble(companies.body.info.get(i).latitude), Double.parseDouble(companies.body.info.get(i).longitude));

                        String marker_title = companies.body.info.get(i).company_id
                                +"_"+companies.body.info.get(i).name
                                +"_"+companies.body.info.get(i).address
                                +"_"+companies.body.info.get(i).phone
                                +"_"+companies.body.info.get(i).fax
                                +"_"+companies.body.info.get(i).toll_free;

                        mMap.addMarker(new MarkerOptions().position(location).title(marker_title));
                    }

                }

                //move camera
                if(location != null)
                {
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
                }

            }
        });
    }

    @Override
    public void onDestroyView() {


//        try {
////            mapFragment =  (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.maps_list);
//            FragmentTransaction ft = fragmentManager.beginTransaction();
//            ft.remove(mapFragment);
//            ft.commit();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        super.onDestroyView();
    }

    @Override
    public void onStart() {
        ImageView back = (ImageView) getActivity().findViewById(R.id.toolbar_back);
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction().replace(R.id.content_frame, new CompaniesFragement()).addToBackStack(null).commit();
            }
        });
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        ImageView back = (ImageView) getActivity().findViewById(R.id.toolbar_back);
        back.setVisibility(View.INVISIBLE);
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    public void getFiltersData()
    {
        final Gson gson = new Gson();

        DBHandler.LocalData localData = db.getData(DATA_KEY);

        divisions = gson.fromJson(localData.data, new TypeToken<ArrayList<DivisionsBasic>>(){}.getType());
        //divisions = gson.fromJson(localData.data, DivisionsClass.class);

    }

}
