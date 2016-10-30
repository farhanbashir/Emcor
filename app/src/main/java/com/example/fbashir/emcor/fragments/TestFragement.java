package com.example.fbashir.emcor.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ScrollingView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
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
import static com.example.fbashir.emcor.R.id.imageView;


/**
 * Created by fbashir on 7/21/2016.
 */

public class TestFragement extends Fragment {
    View myView;
    TextView title_textView;
    ImageView splash_image;
    ScrollView scroll;
    TextView t1;
    TextView t2;
    TextView t3;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.test_layout,container,false);
        title_textView = (TextView) getActivity().findViewById(R.id.toolbar_title);
        title_textView.setText(R.string.company_name);


        return myView;

    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        scroll = (ScrollView) view.findViewById(R.id.scroller);
        t1 = (TextView) view.findViewById(R.id.t1);
        t2 = (TextView) view.findViewById(R.id.t2);
        t3 = (TextView) view.findViewById(R.id.t3);

        scroll.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {

                Rect scrollBounds = new Rect();
                scroll.getHitRect(scrollBounds);
                if (t1.getLocalVisibleRect(scrollBounds)&& t1.getVisibility() == View.INVISIBLE) {

                    Rect r = new Rect();
                    t1.getGlobalVisibleRect(r);
                    double sVisible = r.width() * r.height();
                    double sTotal = t1.getWidth() * t1.getHeight();
                    int visible = (int) (100 * sVisible / sTotal);

                    if(visible > 30)
                    {
                        t1.setAlpha(0.0f);

                        t1.setVisibility(View.VISIBLE);

                        t1.animate().setDuration(1000).alpha(1.0f);
                    }

                    // Any portion of the imageView, even a single pixel, is within the visible window
                }
                if (t2.getLocalVisibleRect(scrollBounds)&& t2.getVisibility() == View.INVISIBLE) {
                    if(getVisiblePercent(t2) > 30)
                    {
                        Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.test);
                        t2.startAnimation(anim);
                        t2.setVisibility(View.VISIBLE);
//                        t2.setAlpha(0.0f);
//                        t2.setVisibility(View.VISIBLE);
//                        t2.animate().setDuration(1000).alpha(1.0f);
                    }

                    // Any portion of the imageView, even a single pixel, is within the visible window
                }
                if (t3.getLocalVisibleRect(scrollBounds)&& t3.getVisibility() == View.INVISIBLE) {

                    if(getVisiblePercent(t3) > 30)
                    {
                        t3.setAlpha(0.0f);
                        t3.setVisibility(View.VISIBLE);
                        t3.animate().setDuration(1000).alpha(1.0f);
                    }

                    // Any portion of the imageView, even a single pixel, is within the visible window
                }

            }


        });
    }

    public static int getVisiblePercent(View v) {
            Rect r = new Rect();
            v.getGlobalVisibleRect(r);
            double sVisible = r.width() * r.height();
            double sTotal = v.getWidth() * v.getHeight();
            return (int) (100 * sVisible / sTotal);

    }

    private boolean isViewVisible(View view) {
        Rect scrollBounds = new Rect();
        scroll.getDrawingRect(scrollBounds);

        float top = view.getY();
        float bottom = top + view.getHeight();

        if (scrollBounds.top < top && scrollBounds.bottom > bottom) {
            return true;
        } else {
            return false;
        }
    }




}
