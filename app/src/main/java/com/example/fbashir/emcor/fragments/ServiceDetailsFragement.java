package com.example.fbashir.emcor.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fbashir.emcor.R;

/**
 * Created by fbashir on 7/21/2016.
 */

public class ServiceDetailsFragement extends Fragment {
    View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.service_details_layout,container,false);
        getActivity().setTitle(R.string.menu_about_us);
        return myView;

    }
}
