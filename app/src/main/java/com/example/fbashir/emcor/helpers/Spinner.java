package com.example.fbashir.emcor.helpers;

import android.app.*;
import android.content.Context;

import static java.security.AccessController.getContext;

/**
 * Created by fbashir on 8/23/2016.
 */

public class Spinner {
    private static ProgressDialog spinner;

    public Spinner(Context context)
    {
        spinner = new ProgressDialog(context);
        spinner.setIndeterminate(true);
        spinner.setMessage("Loading...");
        spinner.setCanceledOnTouchOutside(false);
    }

    public ProgressDialog getProgressDialog()
    {
        return spinner;
    }


}
