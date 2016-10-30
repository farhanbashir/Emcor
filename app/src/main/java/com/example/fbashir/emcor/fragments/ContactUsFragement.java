package com.example.fbashir.emcor.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.telephony.PhoneNumberUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fbashir.emcor.R;
import com.example.fbashir.emcor.helpers.DBHandler;
import com.example.fbashir.emcor.helpers.MyUtils;
import com.example.fbashir.emcor.helpers.RestClient;
import com.example.fbashir.emcor.helpers.Spinner;
import com.example.fbashir.emcor.interfaces.EmcorPagesAPI;
import com.example.fbashir.emcor.models.businessServiceDetails.BusinessServiceDetailsBasic;
import com.example.fbashir.emcor.models.businessServiceDetails.BusinessServiceDetailsClass;
import com.example.fbashir.emcor.models.contactUs.ContactUsClass;
import com.google.gson.Gson;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.fbashir.emcor.MainActivity.db;
import static com.example.fbashir.emcor.R.id.company_fax_textview;
import static com.example.fbashir.emcor.R.id.company_image;
import static com.example.fbashir.emcor.R.id.contactpage_fax_textview;

/**
 * Created by fbashir on 7/21/2016.
 */

public class ContactUsFragement extends Fragment {
    View myView;
    private static final String HOME_KEY = "HOME_DATA";
    TextView title_textView;
    EditText first_name;
    EditText last_name;
    EditText email;
    EditText contact;
    EditText comments;
    TextView company_phone_textview;
    TextView company_fax_textview;
    TextView company_tollfree_textview;
    BusinessServiceDetailsBasic businessService;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.contactus_layout,container,false);
        //getActivity().setTitle(R.string.menu_contact_us);
        title_textView = (TextView) getActivity().findViewById(R.id.toolbar_title);
        title_textView.setText(R.string.menu_contact_us);


        getContactData();

        return myView;

    }

    public void setContactData()
    {
        TextView company_name_textview = (TextView) myView.findViewById(R.id.company_name_textview);
        TextView company_address_textview = (TextView) myView.findViewById(R.id.company_address_textview);
        company_fax_textview = (TextView) myView.findViewById(contactpage_fax_textview);
        company_phone_textview = (TextView) myView.findViewById(R.id.contactpage_phone_textview);
        TextView company_website_textview = (TextView) myView.findViewById(R.id.contactpage_website_textview);
        company_tollfree_textview = (TextView) myView.findViewById(R.id.contactpage_tollfree_textview);
        ImageView company_image = (ImageView) myView.findViewById(R.id.company_image);

        company_name_textview.setText(businessService.title);
        company_address_textview.setText(businessService.address);
        company_fax_textview.setText(businessService.fax);
        company_phone_textview.setText(businessService.phone);
        company_website_textview.setText(businessService.website);
        company_tollfree_textview.setText(businessService.toll_free);

        company_image.setTag(R.string.lat_tag, businessService.latitude);
        company_image.setTag(R.string.lng_tag, businessService.longitude);


        company_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object latitude = v.getTag(R.string.lat_tag);
                Object longitude = v.getTag(R.string.lng_tag);
                MyUtils.openMap(getContext(), latitude.toString(), longitude.toString(), businessService.title);
            }
        });

    }

    public void getContactData()
    {
        final Gson gson = new Gson();
        final Spinner spinner = new Spinner(getContext());
        spinner.getProgressDialog().show();

        DBHandler.LocalData localData = db.getData(HOME_KEY);
        Calendar oneWeekAhead = Calendar.getInstance();
        oneWeekAhead.add(Calendar.DATE, 7);



        if(localData != null && oneWeekAhead.getTimeInMillis() > localData.time)
        {
            businessService = gson.fromJson(localData.data, BusinessServiceDetailsBasic.class);
            setContactData();
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

                    BusinessServiceDetailsClass businessServiceDetails = response.body();
                    if(response.code() == 200)
                    {
                        db.addData(HOME_KEY,gson.toJson(businessServiceDetails), System.currentTimeMillis());
                        businessService = businessServiceDetails.body.info;
                        setContactData();
                    }
                    else
                    {
                        MyUtils.showAlert(getContext(), response.body().header.error);
                        //Toast.makeText(getContext(),response.body().header.error,Toast.LENGTH_LONG).show();
                    }


                }

                @Override
                public void onFailure(Call<BusinessServiceDetailsClass> call, Throwable t) {
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

        company_phone_textview.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MyUtils.openDialer(getContext(),businessService.phone );
            }
        });



        company_tollfree_textview.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MyUtils.openDialer(getContext(),businessService.toll_free );
            }
        });


        Button button = (Button) myView.findViewById(R.id.buttonContactUs);
        button.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // do something

                int error = 0;
                first_name = (EditText) myView.findViewById(R.id.textViewFirstName);
                last_name = (EditText) myView.findViewById(R.id.textViewLastName);
                email = (EditText) myView.findViewById(R.id.textViewEmail);
                contact = (EditText) myView.findViewById(R.id.textViewContact);
                comments = (EditText) myView.findViewById(R.id.textViewComments);

                String first_name_value = first_name.getText().toString();

                if(first_name_value.equals(""))
                {
                    error++;
                    first_name.setError(getResources().getString(R.string.first_name_error));
                }

                String last_name_value = last_name.getText().toString();
                if(last_name_value.equals(""))
                {
                    error++;
                    last_name.setError(getResources().getString(R.string.last_name_error));
                }


                String email_value = email.getText().toString();
                if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email_value).matches())
                {
                    error++;
                    email.setError(getResources().getString(R.string.email_error));
                }

                String contact_value = contact.getText().toString();
                if(!PhoneNumberUtils.isGlobalPhoneNumber(contact_value))
                {
                    error++;
                    contact.setError(getResources().getString(R.string.phone_error));
                }


                String comments_value = comments.getText().toString();
                if(comments_value.equals(""))
                {
                    error++;
                    comments.setError(getResources().getString(R.string.comments_error));
                }

                if(error == 0)
                {
                    final Spinner spinner = new Spinner(getContext());
                    spinner.getProgressDialog().show();

                    String end_point = getResources().getString(R.string.server_base_url);
                    RestClient restClient = new RestClient(end_point);

                    EmcorPagesAPI service = restClient.getService().create(EmcorPagesAPI.class);

                    Call<ContactUsClass> call = service.contactUs(first_name_value,
                            last_name_value,
                            email_value,
                            contact_value,
                            comments_value,
                            getResources().getString(R.string.device_type));


                    call.enqueue(new Callback<ContactUsClass>() {
                        @Override
                        public void onResponse(Call<ContactUsClass> call, Response<ContactUsClass> response) {
                            if(spinner.getProgressDialog().isShowing())
                            {
                                spinner.getProgressDialog().dismiss();
                            }

                            if(response.code() == 200)
                            {
                                ContactUsClass contactUsResponse = response.body();
                                MyUtils.showAlert(getContext(), contactUsResponse.header.message);
                                //Toast.makeText(getContext(), contactUsResponse.header.message, Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                MyUtils.showAlert(getContext(), response.body().header.message);
                                //Toast.makeText(getContext(), response.body().header.message, Toast.LENGTH_LONG).show();
                            }

                        }

                        @Override
                        public void onFailure(Call<ContactUsClass> call, Throwable t) {
                            if(spinner.getProgressDialog().isShowing())
                            {
                                spinner.getProgressDialog().dismiss();
                            }
                            MyUtils.showAlert(getContext(), getResources().getString(R.string.some_error));
                            //Toast.makeText(getContext(), "Some error", Toast.LENGTH_LONG).show();
                        }
                    });
                }


            }
        });
    }


}
