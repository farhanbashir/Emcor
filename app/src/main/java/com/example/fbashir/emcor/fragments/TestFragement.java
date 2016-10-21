package com.example.fbashir.emcor.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fbashir.emcor.MainActivity;
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

public class TestFragement extends Fragment {
    View myView;
    TextView title_textView;
    ImageView splash_image;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.test_layout,container,false);
        title_textView = (TextView) getActivity().findViewById(R.id.toolbar_title);
        title_textView.setText(R.string.company_name);


        return myView;

    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        Button button = (Button) view.findViewById(R.id.dialog_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create a Dialog component

                MyUtils.showAlert(getContext(), "Hello how are you");


                //final Dialog dialog = new Dialog(getContext());
//                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                dialog.setCancelable(false);
//
//                //tell the Dialog to use the dialog.xml as it's layout description
//                dialog.setContentView(R.layout.dialog);
//                //dialog.setTitle("Android Custom Dialog Box");
//
//                TextView txt = (TextView) dialog.findViewById(R.id.txt);
//                txt.setText("This is an Android custom Dialog Box Example! Enjoy!");
//
//
//
//                Button dialogButton = (Button) dialog.findViewById(R.id.dialogButton);
//                dialogButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//                    }
//                });
//                dialog.show();

            }
        });
    }




}
