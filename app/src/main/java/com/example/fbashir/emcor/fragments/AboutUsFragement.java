package com.example.fbashir.emcor.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.example.fbashir.emcor.R;
import com.example.fbashir.emcor.adapters.CorporateOfficesListAdaptar;
import com.example.fbashir.emcor.adapters.IndustryRecognitionListAdaptar;
import com.example.fbashir.emcor.adapters.ManagementTeamListAdaptar;
import com.example.fbashir.emcor.helpers.DBHandler;
import com.example.fbashir.emcor.helpers.MyUtils;
import com.example.fbashir.emcor.helpers.RestClient;
import com.example.fbashir.emcor.helpers.Spinner;
import com.example.fbashir.emcor.interfaces.EmcorPagesAPI;
import com.example.fbashir.emcor.models.businessServiceDetails.BusinessServiceDetailsBasic;
import com.example.fbashir.emcor.models.businessServiceDetails.BusinessServiceDetailsClass;
import com.example.fbashir.emcor.models.businessServiceDetails.CorporateOffice;
import com.example.fbashir.emcor.models.businessServiceDetails.IndustryRecognition;
import com.example.fbashir.emcor.models.businessServiceDetails.Team;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.fbashir.emcor.MainActivity.db;


/**
 * Created by fbashir on 7/21/2016.
 */

public class AboutUsFragement extends Fragment {
    View myView;
    private static final String DATA_KEY = "ABOUT_DATA";
    TextView title_textView;
    SliderLayout sliderShow;
    TabLayout tabLayout;
    View about_view;
    View team_view;
    View industry_view;
    View corporate_view;
    LinearLayout about_layout;
    LinearLayout team_layout;
    LinearLayout industry_layout;
    LinearLayout corporate_layout;
    int selected_tab = 1;
    int previous_selected_tab = 1;
    ImageView about_image;
    ImageView team_image;
    ImageView recognition_image;
    ImageView map_image;

    public BusinessServiceDetailsBasic businessService;
    public BusinessServiceDetailsClass businessServiceDetails;
    public LinearLayout main_layout;
    ArrayList<Team> arraylist_team = new ArrayList<Team>();
    ArrayList<IndustryRecognition> arraylist_industry = new ArrayList<IndustryRecognition>();
    ArrayList<CorporateOffice> arraylist_corporate_offices = new ArrayList<CorporateOffice>();

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.aboutus_layout,container,false);
        //getActivity().setTitle(R.string.menu_about_us);
        title_textView = (TextView) getActivity().findViewById(R.id.toolbar_title);
        title_textView.setText(R.string.menu_about_us);


        getAboutUsData();

        return myView;

    }

    public void changeTabIconUnselect()
    {
        if(selected_tab !=0)
        {
            if(previous_selected_tab == 1)
            {
                about_image.setImageResource(R.drawable.building_icon_transparent);
            }
            else if(previous_selected_tab == 2)
            {
                team_image.setImageResource(R.drawable.users_icontransparent);
            }
            else if(previous_selected_tab == 3)
            {
                recognition_image.setImageResource(R.drawable.trophy_icon_transparent);
            }
            else if(previous_selected_tab == 4)
            {
                map_image.setImageResource(R.drawable.marker_icon_transparent);
            }
        }
        else
        {

        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        //check if industry recognition is selected
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String about_selected_tab = sharedPref.getString("about_selected_tab", "");

        //getting all views
        about_view = view.findViewById(R.id.about_view);
        team_view = view.findViewById(R.id.team_view);
        industry_view = view.findViewById(R.id.industry_view);
        corporate_view = view.findViewById(R.id.corporate_view);
        title_textView = (TextView) getActivity().findViewById(R.id.toolbar_title);

        about_image = (ImageView) view.findViewById(R.id.building_icon);
        team_image = (ImageView) view.findViewById(R.id.managment_icon);
        recognition_image = (ImageView) view.findViewById(R.id.recognition_icon);
        map_image = (ImageView) view.findViewById(R.id.map_icon);


        about_layout = (LinearLayout) view.findViewById(R.id.tab_about);
        team_layout = (LinearLayout) view.findViewById(R.id.tab_team);
        industry_layout = (LinearLayout) view.findViewById(R.id.tab_industry);
        corporate_layout = (LinearLayout) view.findViewById(R.id.tab_corporate);


        if(about_selected_tab.equals("industry_recognition"))
        {

            about_layout.setBackgroundColor(getResources().getColor(R.color.tab_background_unselected));
            team_layout.setBackgroundColor(getResources().getColor(R.color.tab_background_unselected));
            industry_layout.setBackgroundColor(getResources().getColor(R.color.tab_background_selected));
            corporate_layout.setBackgroundColor(getResources().getColor(R.color.tab_background_unselected));



            title_textView.setText(R.string.industry_recognition_text);
            about_view.setVisibility(View.INVISIBLE);
            team_view.setVisibility(View.INVISIBLE);
            corporate_view.setVisibility(View.INVISIBLE);
            industry_view.setVisibility(View.VISIBLE);

            recognition_image.setImageResource(R.drawable.traophy_icon_white);

            //previous_selected_tab = 1;
            selected_tab = 3;
            changeTabIconUnselect();

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.remove("about_selected_tab");
            editor.commit();
        }

        about_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(selected_tab != 1)
                {
                    about_layout.setBackgroundColor(getResources().getColor(R.color.tab_background_selected));
                    team_layout.setBackgroundColor(getResources().getColor(R.color.tab_background_unselected));
                    industry_layout.setBackgroundColor(getResources().getColor(R.color.tab_background_unselected));
                    corporate_layout.setBackgroundColor(getResources().getColor(R.color.tab_background_unselected));



                    title_textView.setText(businessService.title);
                    about_view.setVisibility(View.VISIBLE);
                    team_view.setVisibility(View.INVISIBLE);
                    corporate_view.setVisibility(View.INVISIBLE);
                    industry_view.setVisibility(View.INVISIBLE);

                    about_image.setImageResource(R.drawable.building_icon_white);

                    previous_selected_tab = selected_tab;
                    selected_tab = 1;
                    changeTabIconUnselect();
                }
            }
        });

        team_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(selected_tab != 2)
                {
                    about_layout.setBackgroundColor(getResources().getColor(R.color.tab_background_unselected));
                    team_layout.setBackgroundColor(getResources().getColor(R.color.tab_background_selected));
                    industry_layout.setBackgroundColor(getResources().getColor(R.color.tab_background_unselected));
                    corporate_layout.setBackgroundColor(getResources().getColor(R.color.tab_background_unselected));



                    title_textView.setText(R.string.management_team_text);
                    about_view.setVisibility(View.INVISIBLE);
                    team_view.setVisibility(View.VISIBLE);
                    corporate_view.setVisibility(View.INVISIBLE);
                    industry_view.setVisibility(View.INVISIBLE);

                    team_image.setImageResource(R.drawable.users_icon_white);

                    previous_selected_tab = selected_tab;
                    selected_tab = 2;
                    changeTabIconUnselect();
                }
            }
        });

        industry_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(selected_tab != 3)
                {
                    about_layout.setBackgroundColor(getResources().getColor(R.color.tab_background_unselected));
                    team_layout.setBackgroundColor(getResources().getColor(R.color.tab_background_unselected));
                    industry_layout.setBackgroundColor(getResources().getColor(R.color.tab_background_selected));
                    corporate_layout.setBackgroundColor(getResources().getColor(R.color.tab_background_unselected));



                    title_textView.setText(R.string.industry_recognition_text);
                    about_view.setVisibility(View.INVISIBLE);
                    team_view.setVisibility(View.INVISIBLE);
                    corporate_view.setVisibility(View.INVISIBLE);
                    industry_view.setVisibility(View.VISIBLE);

                    recognition_image.setImageResource(R.drawable.traophy_icon_white);

                    previous_selected_tab = selected_tab;
                    selected_tab = 3;
                    changeTabIconUnselect();
                }

            }
        });

        corporate_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(selected_tab != 4)
                {
                    about_layout.setBackgroundColor(getResources().getColor(R.color.tab_background_unselected));
                    team_layout.setBackgroundColor(getResources().getColor(R.color.tab_background_unselected));
                    industry_layout.setBackgroundColor(getResources().getColor(R.color.tab_background_unselected));
                    corporate_layout.setBackgroundColor(getResources().getColor(R.color.tab_background_selected));



                    title_textView.setText(R.string.corporate_offices_text);
                    about_view.setVisibility(View.INVISIBLE);
                    team_view.setVisibility(View.INVISIBLE);
                    corporate_view.setVisibility(View.VISIBLE);
                    industry_view.setVisibility(View.INVISIBLE);

                    map_image.setImageResource(R.drawable.marker_icon_white);

                    previous_selected_tab = selected_tab;
                    selected_tab = 4;
                    changeTabIconUnselect();
                }
            }
        });



//        //making tabs for each view
//        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
//
//        //home tab
//        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.building_icn));
//
//        //management team tab
//        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.managment_icn));
//
//        //industry recognition tab
//        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.recognition_icn));
//
//        if(about_selected_tab.equals("industry_recognition"))
//        {
//            tabLayout.getTabAt(2).select();
//            //getActivity().setTitle(R.string.industry_recognition_text);
//            title_textView.setText(R.string.industry_recognition_text);
//
//            about_view.setVisibility(View.INVISIBLE);
//            team_view.setVisibility(View.INVISIBLE);
//            corporate_view.setVisibility(View.INVISIBLE);
//            industry_view.setVisibility(View.VISIBLE);
//
//            SharedPreferences.Editor editor = sharedPref.edit();
//            editor.remove("about_selected_tab");
//            editor.commit();
//        }
//
//        //corporate offices tab
//        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.map_icn));
//
//
//
//        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                if(tabLayout.getSelectedTabPosition() == 0)
//                {
//                    //getActivity().setTitle(businessServiceDetails.body.info.title);
//                    title_textView.setText(businessServiceDetails.body.info.title);
//
//                    about_view.setVisibility(View.VISIBLE);
//                    team_view.setVisibility(View.INVISIBLE);
//                    corporate_view.setVisibility(View.INVISIBLE);
//                    industry_view.setVisibility(View.INVISIBLE);
//                }
//                else if(tabLayout.getSelectedTabPosition() == 1)
//                {
//                    //getActivity().setTitle(R.string.management_team_text);
//                    title_textView.setText(R.string.management_team_text);
//
//                    about_view.setVisibility(View.INVISIBLE);
//                    team_view.setVisibility(View.VISIBLE);
//                    corporate_view.setVisibility(View.INVISIBLE);
//                    industry_view.setVisibility(View.INVISIBLE);
//
//                }
//                else if(tabLayout.getSelectedTabPosition() == 2)
//                {
//                    //getActivity().setTitle(R.string.industry_recognition_text);
//                    title_textView.setText(R.string.industry_recognition_text);
//
//                    about_view.setVisibility(View.INVISIBLE);
//                    team_view.setVisibility(View.INVISIBLE);
//                    corporate_view.setVisibility(View.INVISIBLE);
//                    industry_view.setVisibility(View.VISIBLE);
//                }
//                else if(tabLayout.getSelectedTabPosition() == 3)
//                {
//                    //getActivity().setTitle(R.string.corporate_offices_text);
//                    title_textView.setText(R.string.corporate_offices_text);
//
//                    about_view.setVisibility(View.INVISIBLE);
//                    team_view.setVisibility(View.INVISIBLE);
//                    corporate_view.setVisibility(View.VISIBLE);
//                    industry_view.setVisibility(View.INVISIBLE);
//                }
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });



    }

    public void getAboutUsData()
    {
        main_layout = (LinearLayout) myView.findViewById(R.id.aboutus_layout);
        main_layout.setVisibility(View.INVISIBLE);

        //main work start
        final Gson gson = new Gson();
        final Spinner spinner = new Spinner(getContext());
        spinner.getProgressDialog().show();

        DBHandler.LocalData localData = db.getData(DATA_KEY);
        Calendar oneWeekAhead = Calendar.getInstance();
        oneWeekAhead.add(Calendar.DATE, 7);

        if(localData != null && oneWeekAhead.getTimeInMillis() > localData.time)
        {
            businessService = gson.fromJson(localData.data, BusinessServiceDetailsBasic.class);
            setAboutUsData(businessService);
            main_layout.setVisibility(View.VISIBLE);
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

            Call<BusinessServiceDetailsClass> call = service.getBusinessServiceDetails();

            call.enqueue(new Callback<BusinessServiceDetailsClass>() {
                @Override
                public void onResponse(Call<BusinessServiceDetailsClass> call, Response<BusinessServiceDetailsClass> response) {
                    if(spinner.getProgressDialog().isShowing())
                    {
                        spinner.getProgressDialog().dismiss();
                    }
                    main_layout.setVisibility(View.VISIBLE);

                    businessServiceDetails = response.body();

                    if(response.code() == 200)
                    {
                        businessService = businessServiceDetails.body.info;
                        db.addData(DATA_KEY,gson.toJson(businessService), System.currentTimeMillis());
                        setAboutUsData(businessService);
                    }
                    else
                    {
                        MyUtils.showAlert(getContext(), response.body().header.error);
                        //Toast.makeText(getContext(),response.body().header.error,Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<BusinessServiceDetailsClass> call, Throwable t) {
                    if(spinner.getProgressDialog().isShowing())
                    {
                        spinner.getProgressDialog().dismiss();
                    }
                    main_layout.setVisibility(View.VISIBLE);
                    MyUtils.showAlert(getContext(), getResources().getString(R.string.some_error));
                    //Toast.makeText(getContext(),"Some error",Toast.LENGTH_LONG).show();
                }
            });
        }


        //main work end
    }

    public void setAboutUsData(BusinessServiceDetailsBasic businessService)
    {
        //getActivity().setTitle(businessServiceDetails.body.info.title);
        title_textView.setText(businessService.title);

        //about tab
//                    ImageView imageView = (ImageView) myView.findViewById(R.id.aboutus_image);
//                    Picasso.with(getContext()).load(businessServiceDetails.body.info.logo).into(imageView);

        sliderShow = (SliderLayout) myView.findViewById(R.id.aboutus_image);

        DefaultSliderView[] sliderViews = new DefaultSliderView[businessService.images.length];
        for(int i=0;i<businessService.images.length;i++)
        {
            sliderViews[i] = new DefaultSliderView(getContext());
            sliderViews[i]
                    .image(businessService.images[i]);

            sliderShow.addSlider(sliderViews[i]);
        }

        TextView textView = (TextView) myView.findViewById(R.id.aboutus_text);
        textView.setText(businessService.description);


        //management team tab
//                    for (int i = 0; i < businessServiceDetails.body.info.team.size(); i++)
//                    {
//                        Team wp = new Team(businessServiceDetails.body.info.team.get(i).first_name,
//                                            businessServiceDetails.body.info.tea
//                                businessServiceDetails.body.info.team.get(i).designation);
//                        // Binds all strings into an array
//                        arraylist_team.add(wp);
//                    }

        final ManagementTeamListAdaptar team = new ManagementTeamListAdaptar(getActivity(), businessService.team);

        ListView listView = (ListView) myView.findViewById(R.id.team_list);

        listView.setAdapter(team);


        //industry recornition tab
        for (int i = 0; i < businessService.industry_recognition.size(); i++)
        {
            IndustryRecognition wp = new IndustryRecognition(businessService.industry_recognition.get(i).title,
                    businessService.industry_recognition.get(i).description, businessService.industry_recognition.get(i).file);
            // Binds all strings into an array
            arraylist_industry.add(wp);
        }

        final IndustryRecognitionListAdaptar industry_recognition = new IndustryRecognitionListAdaptar(getActivity(), arraylist_industry);

        ListView listView_industry = (ListView) myView.findViewById(R.id.industry_recognition_list);

        listView_industry.setAdapter(industry_recognition);


        //corporate offices tab
        for (int i = 0; i < businessService.corporate_offices.size(); i++)
        {
            CorporateOffice wp = new CorporateOffice(businessService.corporate_offices.get(i).address,
                    businessService.corporate_offices.get(i).city,
                    businessService.corporate_offices.get(i).state,
                    businessService.corporate_offices.get(i).phone,
                    businessService.corporate_offices.get(i).fax,
                    businessService.corporate_offices.get(i).website,
                    businessService.corporate_offices.get(i).latitude,
                    businessService.corporate_offices.get(i).longitude);
            // Binds all strings into an array
            arraylist_corporate_offices.add(wp);
        }

        final CorporateOfficesListAdaptar corporate_office = new CorporateOfficesListAdaptar(getActivity(), arraylist_corporate_offices);

        ListView listView_corporate = (ListView) myView.findViewById(R.id.corporate_offices_list);

        listView_corporate.setAdapter(corporate_office);
    }

//    @Override
//    public void onStop() {
//        sliderShow.stopAutoCycle();
//        super.onStop();
//    }
}
