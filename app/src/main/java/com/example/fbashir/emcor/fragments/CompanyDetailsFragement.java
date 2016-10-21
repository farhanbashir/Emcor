package com.example.fbashir.emcor.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fbashir.emcor.R;
import com.example.fbashir.emcor.adapters.CompanyAdvantagesListAdaptar;
import com.example.fbashir.emcor.adapters.CompanyMarketsListAdaptar;
import com.example.fbashir.emcor.adapters.CompanyServicesListAdaptar;
import com.example.fbashir.emcor.adapters.CompanyStaffListAdaptar;
import com.example.fbashir.emcor.helpers.MyUtils;
import com.example.fbashir.emcor.helpers.RestClient;
import com.example.fbashir.emcor.helpers.Spinner;
import com.example.fbashir.emcor.interfaces.EmcorPagesAPI;
import com.example.fbashir.emcor.models.companyDetails.CompanyDetailsClass;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by fbashir on 7/21/2016.
 */

public class CompanyDetailsFragement extends Fragment {
    View myView;
    TextView title_textView;
//    ImageView pdf_image;
    ListView download_listView;
    Uri pdf_uri = null;
    //LinearLayout download_linear_layout;
    LinearLayout preview_linear_layout;
    LinearLayout email_linear_layout;
    View about_view;
    View advantages_view;
    View markets_view;
    View downloads_view;
    View services_view;
    View team_view;
    LinearLayout about_layout;
    LinearLayout team_layout;
    LinearLayout advantages_layout;
    LinearLayout markets_layout;
    LinearLayout downloads_layout;
    LinearLayout services_layout;
    LinearLayout company_profile_bar;
    LinearLayout options_layout;
    int selected_tab = 1;
    int previous_selected_tab = 1;
    ImageView about_image;
    ImageView team_image;
    ImageView setting_image;
    ImageView pie_image;
    ImageView diamond_image;
    ImageView pdf_image;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.company_detail_layout,container,false);
        title_textView = (TextView) getActivity().findViewById(R.id.toolbar_title);


        //check if industry recognition is selected
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String selected_company_id = sharedPref.getString("selected_company_id", "");
        Log.d("company",selected_company_id);
        if(selected_company_id.equals(""))
        {
            MyUtils.showAlert(getContext(), getResources().getString(R.string.no_company_selected));
            //Toast.makeText(getContext(), "No company selected" , Toast.LENGTH_LONG).show();
        }
        else
        {
            //main work start
            final Spinner spinner = new Spinner(getContext());
            spinner.getProgressDialog().show();
            String end_point = getResources().getString(R.string.server_base_url);
            RestClient restClient = new RestClient(end_point);

            EmcorPagesAPI service = restClient.getService().create(EmcorPagesAPI.class);
            Call<CompanyDetailsClass> call = service.getCompanyDetails(selected_company_id);

            call.enqueue(new Callback<CompanyDetailsClass>() {
                @Override
                public void onResponse(Call<CompanyDetailsClass> call, Response<CompanyDetailsClass> response) {
                    if(spinner.getProgressDialog().isShowing())
                    {
                        spinner.getProgressDialog().dismiss();
                    }

                    int code = response.code();
                    final CompanyDetailsClass company_details = response.body();
                    if(code == 200)
                    {
                        //getActivity().setTitle(company_details.body.info.name);
                        title_textView.setText(company_details.body.info.name);

                        TextView company_name_textview = (TextView) myView.findViewById(R.id.company_name_textview);
                        TextView company_address_textview = (TextView) myView.findViewById(R.id.company_address_textview);
                        ImageView company_image = (ImageView) myView.findViewById(R.id.company_image);
                        TextView company_description_textview = (TextView) myView.findViewById(R.id.company_description_textview);
                        TextView company_division_name = (TextView) myView.findViewById(R.id.company_division_name);
                        ImageButton division_map_image = (ImageButton) myView.findViewById(R.id.company_division_map);
                        TextView company_phone_textview = (TextView) myView.findViewById(R.id.company_phone_textview);
                        TextView company_fax_textview = (TextView) myView.findViewById(R.id.company_fax_textview);
                        TextView company_toll_textview = (TextView) myView.findViewById(R.id.company_toll_textview);
                        TextView company_website_textview = (TextView) myView.findViewById(R.id.company_website_textview);

                        TextView company_division_phone_textview = (TextView) myView.findViewById(R.id.company_division_phone);
                        TextView company_division_fax_textview = (TextView) myView.findViewById(R.id.company_division_fax);
                        TextView company_division_toll_textview = (TextView) myView.findViewById(R.id.company_division_toll);



                        company_name_textview.setText(company_details.body.info.name);
                        company_address_textview.setText(company_details.body.info.address);

                        company_image.setTag(R.string.lat_tag, company_details.body.info.latitude);
                        company_image.setTag(R.string.lng_tag, company_details.body.info.longitude);


                        division_map_image.setTag(R.string.lat_tag, company_details.body.division.latitude);
                        division_map_image.setTag(R.string.lng_tag, company_details.body.division.longitude);

                        division_map_image.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Object latitude = v.getTag(R.string.lat_tag);
                                Object longitude = v.getTag(R.string.lng_tag);
                                MyUtils.openMap(getContext(), latitude.toString(), longitude.toString(), company_details.body.division.title);
                            }
                        });

                        company_image.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Object latitude = v.getTag(R.string.lat_tag);
                                Object longitude = v.getTag(R.string.lng_tag);
                                MyUtils.openMap(getContext(), latitude.toString(), longitude.toString(), company_details.body.info.name);
                            }
                        });


                        company_description_textview.setText(company_details.body.info.description);
                        company_phone_textview.setText(company_details.body.info.phone);
                        company_fax_textview.setText(company_details.body.info.fax);
                        company_toll_textview.setText(company_details.body.info.toll_free);
                        company_website_textview.setText(company_details.body.info.website);

                        company_division_phone_textview.setText(company_details.body.division.phone);
                        company_division_fax_textview.setText(company_details.body.division.fax);
                        company_division_toll_textview.setText(company_details.body.division.toll_free);

                        company_division_name.setText(company_details.body.division.title);


                        //markets
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                CompanyMarketsListAdaptar market_adapter = new CompanyMarketsListAdaptar(getActivity(),company_details.body.our_markets);
                                ListView markets_listView = (ListView) myView.findViewById(R.id.company_markets_list);
                                markets_listView.setAdapter(market_adapter);
                            }
                        }).run();


                        //services
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                CompanyServicesListAdaptar service_adapter = new CompanyServicesListAdaptar(getActivity(),company_details.body.our_services);
                                ListView services_listView = (ListView) myView.findViewById(R.id.company_services_list);
                                services_listView.setAdapter(service_adapter);
                            }
                        }).run();



                        //team
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                CompanyStaffListAdaptar team_adapter = new CompanyStaffListAdaptar(getActivity(), company_details.body.team.staff);
                                ListView team_listView = (ListView) myView.findViewById(R.id.company_team_list);
                                team_listView.setAdapter(team_adapter);
                            }
                        }).run();


                        //advantages
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                CompanyAdvantagesListAdaptar advantages_adapter = new CompanyAdvantagesListAdaptar(getActivity(), company_details.body.advantages);
                                ListView advantages_listView = (ListView) myView.findViewById(R.id.company_advantages_list);
                                advantages_listView.setAdapter(advantages_adapter);
                            }
                        }).run();



                        //download
                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                                company_profile_bar = (LinearLayout) myView.findViewById(R.id.company_profile_bar);
                                final TextView company_download_item = (TextView) myView.findViewById(R.id.company_download_item);
                                options_layout = (LinearLayout) myView.findViewById(R.id.download_item_options);

                                company_profile_bar.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        if(options_layout.getVisibility() == View.VISIBLE)
                                        {
                                            options_layout.setVisibility(View.GONE);
                                            company_download_item.setTextColor(Color.WHITE);
                                            ImageView image_selected = (ImageView) v.findViewById(R.id.company_download_image);
                                            image_selected.setImageResource(R.drawable.pdf_white_icn);
                                            company_profile_bar.setBackgroundColor(getResources().getColor(R.color.locations_tab_background_unselected));
                                        }
                                        else
                                        {
                                            company_profile_bar.setBackgroundColor(getResources().getColor(R.color.locations_tab_background_selected));
                                            company_download_item.setTextColor(Color.BLACK);
                                            ImageView image_selected = (ImageView) v.findViewById(R.id.company_download_image);
                                            image_selected.setImageResource(R.drawable.pdf_blk_icn);
                                            options_layout.setVisibility(View.VISIBLE);
                                        }

                                    }
                                });

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

                                        if(!company_details.body.pdf_link.equals(""))
                                        {
                                            MyUtils.openViewer(getContext(), company_details.body.pdf_link);
                                        }
                                        else
                                        {
                                            MyUtils.showAlert(getContext(), getResources().getString(R.string.profile_not_found));
                                            //Toast.makeText(getContext(),getResources().getString(R.string.profile_not_found), Toast.LENGTH_LONG);
                                        }

                                    }
                                });

                                email_linear_layout.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        preview_linear_layout.setBackgroundColor(getResources().getColor(R.color.tab_background_unselected));
                                        email_linear_layout.setBackgroundColor(getResources().getColor(R.color.black));
                                        //download_linear_layout.setBackgroundColor(getResources().getColor(R.color.tab_background_unselected));

                                        if(!company_details.body.pdf_link.equals(""))
                                        {
                                            MyUtils.openMailer(getContext(), company_details.body.pdf_link);
                                        }
                                        else
                                        {
                                            MyUtils.showAlert(getContext(), getResources().getString(R.string.profile_not_found));
                                            //Toast.makeText(getContext(),getResources().getString(R.string.profile_not_found), Toast.LENGTH_LONG);
                                        }


                                    }
                                });


//                                download_linear_layout.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        preview_linear_layout.setBackgroundColor(getResources().getColor(R.color.tab_background_unselected));
//                                        email_linear_layout.setBackgroundColor(getResources().getColor(R.color.tab_background_unselected));
//                                        download_linear_layout.setBackgroundColor(getResources().getColor(R.color.black));
//
//                                        final Spinner spinner = new Spinner(getContext());
//                                        spinner.getProgressDialog().show();
//                                        final String fileName = company_details.body.pdf_link;
//                                        String end_point = getResources().getString(R.string.server_base_url);
//                                        RestClient restClient = new RestClient(end_point);
//                                        EmcorPagesAPI service = restClient.getService().create(EmcorPagesAPI.class);
//                                        Call<ResponseBody> call = service.downloadFileWithDynamicUrlSync(getResources().getString(R.string.test_pdf));
//
//                                        call.enqueue(new Callback<ResponseBody>() {
//                                            @Override
//                                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                                                if(spinner.getProgressDialog().isShowing())
//                                                {
//                                                    spinner.getProgressDialog().dismiss();
//                                                }
//
//                                                if(response.code() == 200)
//                                                {
//                                                    Log.d("success","server success");
//
//                                                    pdf_uri = MyUtils.writeResponseBodyToDisk(response.body(), fileName, pdf_uri);
//                                                }
//                                                else
//                                                {
//                                                    Log.d("fault","server fault");
//                                                }
//                                            }
//
//                                            @Override
//                                            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                                                if(spinner.getProgressDialog().isShowing())
//                                                {
//                                                    spinner.getProgressDialog().dismiss();
//                                                }
//                                                Log.d("fault","server fault failure");
//                                            }
//                                        });
//
//                                    }
//                                });

                            }
                        }).run();

                    }
                    else
                    {

                    }
                }

                @Override
                public void onFailure(Call<CompanyDetailsClass> call, Throwable t) {
                    if(spinner.getProgressDialog().isShowing())
                    {
                        spinner.getProgressDialog().dismiss();
                    }
                    MyUtils.showAlert(getContext(), getResources().getString(R.string.some_error));
                    //Toast.makeText(getContext(), "Some error" , Toast.LENGTH_LONG).show();
                }
            });
        }


        return myView;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        about_view = view.findViewById(R.id.company_about_view);
        advantages_view = view.findViewById(R.id.company_advantages_view);
        markets_view = view.findViewById(R.id.company_markets_view);
        downloads_view = view.findViewById(R.id.company_downloads_view);
        services_view = view.findViewById(R.id.company_services_view);
        team_view = view.findViewById(R.id.company_team_view);
        title_textView = (TextView) getActivity().findViewById(R.id.toolbar_title);



        about_layout = (LinearLayout) view.findViewById(R.id.tab_about);
        advantages_layout = (LinearLayout) view.findViewById(R.id.tab_advantages);
        markets_layout = (LinearLayout) view.findViewById(R.id.tab_markets);
        downloads_layout = (LinearLayout) view.findViewById(R.id.tab_downloads);
        services_layout = (LinearLayout) view.findViewById(R.id.tab_services);
        team_layout = (LinearLayout) view.findViewById(R.id.tab_team);

        about_image = (ImageView) view.findViewById(R.id.building_icon);
        team_image = (ImageView) view.findViewById(R.id.managment_icon);
        setting_image = (ImageView) view.findViewById(R.id.setting_icon);
        pie_image = (ImageView) view.findViewById(R.id.pie_icon);
        diamond_image = (ImageView) view.findViewById(R.id.diamond_icon);
        pdf_image = (ImageView) view.findViewById(R.id.pdf_icon);


        about_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(selected_tab != 1)
                {
                    about_layout.setBackgroundColor(getResources().getColor(R.color.tab_background_selected));
                    advantages_layout.setBackgroundColor(getResources().getColor(R.color.tab_background_unselected));
                    markets_layout.setBackgroundColor(getResources().getColor(R.color.tab_background_unselected));
                    downloads_layout.setBackgroundColor(getResources().getColor(R.color.tab_background_unselected));
                    services_layout.setBackgroundColor(getResources().getColor(R.color.tab_background_unselected));
                    team_layout.setBackgroundColor(getResources().getColor(R.color.tab_background_unselected));


                    title_textView.setText(R.string.menu_about);

                    about_view.setVisibility(View.VISIBLE);

                    advantages_view.setVisibility(View.INVISIBLE);
                    markets_view.setVisibility(View.INVISIBLE);
                    downloads_view.setVisibility(View.INVISIBLE);
                    services_view.setVisibility(View.INVISIBLE);
                    team_view.setVisibility(View.INVISIBLE);

                    about_image.setImageResource(R.drawable.building_icon_white);

                    previous_selected_tab = selected_tab;
                    selected_tab = 1;
                    changeTabIconUnselect();
                }
            }
        });

        advantages_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(selected_tab != 5)
                {
                    about_layout.setBackgroundColor(getResources().getColor(R.color.tab_background_unselected));
                    advantages_layout.setBackgroundColor(getResources().getColor(R.color.tab_background_selected));
                    markets_layout.setBackgroundColor(getResources().getColor(R.color.tab_background_unselected));
                    downloads_layout.setBackgroundColor(getResources().getColor(R.color.tab_background_unselected));
                    services_layout.setBackgroundColor(getResources().getColor(R.color.tab_background_unselected));
                    team_layout.setBackgroundColor(getResources().getColor(R.color.tab_background_unselected));


                    title_textView.setText(R.string.advantages_text);

                    about_view.setVisibility(View.INVISIBLE);

                    advantages_view.setVisibility(View.VISIBLE);
                    markets_view.setVisibility(View.INVISIBLE);
                    downloads_view.setVisibility(View.INVISIBLE);
                    services_view.setVisibility(View.INVISIBLE);
                    team_view.setVisibility(View.INVISIBLE);

                    diamond_image.setImageResource(R.drawable.diamond_icon_white);

                    previous_selected_tab = selected_tab;
                    selected_tab = 5;
                    changeTabIconUnselect();
                }
            }
        });

        markets_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(selected_tab != 4)
                {
                    about_layout.setBackgroundColor(getResources().getColor(R.color.tab_background_unselected));
                    advantages_layout.setBackgroundColor(getResources().getColor(R.color.tab_background_unselected));
                    markets_layout.setBackgroundColor(getResources().getColor(R.color.tab_background_selected));
                    downloads_layout.setBackgroundColor(getResources().getColor(R.color.tab_background_unselected));
                    services_layout.setBackgroundColor(getResources().getColor(R.color.tab_background_unselected));
                    team_layout.setBackgroundColor(getResources().getColor(R.color.tab_background_unselected));


                    title_textView.setText(R.string.markets_text);

                    about_view.setVisibility(View.INVISIBLE);

                    advantages_view.setVisibility(View.INVISIBLE);
                    markets_view.setVisibility(View.VISIBLE);
                    downloads_view.setVisibility(View.INVISIBLE);
                    services_view.setVisibility(View.INVISIBLE);
                    team_view.setVisibility(View.INVISIBLE);

                    pie_image.setImageResource(R.drawable.pie_icon_white);

                    previous_selected_tab = selected_tab;
                    selected_tab = 4;
                    changeTabIconUnselect();
                }
            }
        });

        downloads_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(selected_tab != 6)
                {
                    about_layout.setBackgroundColor(getResources().getColor(R.color.tab_background_unselected));
                    advantages_layout.setBackgroundColor(getResources().getColor(R.color.tab_background_unselected));
                    markets_layout.setBackgroundColor(getResources().getColor(R.color.tab_background_unselected));
                    downloads_layout.setBackgroundColor(getResources().getColor(R.color.tab_background_selected));
                    services_layout.setBackgroundColor(getResources().getColor(R.color.tab_background_unselected));
                    team_layout.setBackgroundColor(getResources().getColor(R.color.tab_background_unselected));


                    title_textView.setText(R.string.downloads_text);

                    about_view.setVisibility(View.INVISIBLE);

                    advantages_view.setVisibility(View.INVISIBLE);
                    markets_view.setVisibility(View.INVISIBLE);
                    downloads_view.setVisibility(View.VISIBLE);
                    services_view.setVisibility(View.INVISIBLE);
                    team_view.setVisibility(View.INVISIBLE);

                    pdf_image.setImageResource(R.drawable.pdf_icon_white);

                    previous_selected_tab = selected_tab;
                    selected_tab = 6;
                    changeTabIconUnselect();
                }
            }
        });

        services_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(selected_tab != 3)
                {
                    about_layout.setBackgroundColor(getResources().getColor(R.color.tab_background_unselected));
                    advantages_layout.setBackgroundColor(getResources().getColor(R.color.tab_background_unselected));
                    markets_layout.setBackgroundColor(getResources().getColor(R.color.tab_background_unselected));
                    downloads_layout.setBackgroundColor(getResources().getColor(R.color.tab_background_unselected));
                    services_layout.setBackgroundColor(getResources().getColor(R.color.tab_background_selected));
                    team_layout.setBackgroundColor(getResources().getColor(R.color.tab_background_unselected));

                    title_textView.setText(R.string.menu_services);

                    about_view.setVisibility(View.INVISIBLE);

                    advantages_view.setVisibility(View.INVISIBLE);
                    markets_view.setVisibility(View.INVISIBLE);
                    downloads_view.setVisibility(View.INVISIBLE);
                    services_view.setVisibility(View.VISIBLE);
                    team_view.setVisibility(View.INVISIBLE);

                    setting_image.setImageResource(R.drawable.wrench_icon_white);

                    previous_selected_tab = selected_tab;
                    selected_tab = 3;
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
                    advantages_layout.setBackgroundColor(getResources().getColor(R.color.tab_background_unselected));
                    markets_layout.setBackgroundColor(getResources().getColor(R.color.tab_background_unselected));
                    downloads_layout.setBackgroundColor(getResources().getColor(R.color.tab_background_unselected));
                    services_layout.setBackgroundColor(getResources().getColor(R.color.tab_background_unselected));
                    team_layout.setBackgroundColor(getResources().getColor(R.color.tab_background_selected));

                    title_textView.setText(R.string.team_text);

                    about_view.setVisibility(View.INVISIBLE);

                    advantages_view.setVisibility(View.INVISIBLE);
                    markets_view.setVisibility(View.INVISIBLE);
                    downloads_view.setVisibility(View.INVISIBLE);
                    services_view.setVisibility(View.INVISIBLE);
                    team_view.setVisibility(View.VISIBLE);

                    team_image.setImageResource(R.drawable.users_icon_white);

                    previous_selected_tab = selected_tab;
                    selected_tab = 2;
                    changeTabIconUnselect();
                }
            }
        });

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
                setting_image.setImageResource(R.drawable.wrench_icon_transparent);
            }
            else if(previous_selected_tab == 4)
            {
                pie_image.setImageResource(R.drawable.pie_icon_transparent);
            }
            else if(previous_selected_tab == 5)
            {
                diamond_image.setImageResource(R.drawable.diamond_icon_transparent);
            }
            else if(previous_selected_tab == 6)
            {
                pdf_image.setImageResource(R.drawable.pdf_icon_transparent);
            }
        }
        else
        {

        }
    }

}
