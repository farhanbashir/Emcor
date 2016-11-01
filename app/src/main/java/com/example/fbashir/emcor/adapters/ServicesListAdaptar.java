package com.example.fbashir.emcor.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fbashir.emcor.R;
import com.example.fbashir.emcor.models.divisions.DivisionsBasic;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.example.fbashir.emcor.R.id.toolbar;

/**
 * Created by fbashir on 7/25/2016.
 */

public class ServicesListAdaptar extends ArrayAdapter<DivisionsBasic>{

    //String[] color_names;
    Context context;
    ArrayList<DivisionsBasic> divisions;
    boolean show_arrow;
    public ServicesListAdaptar(Activity context, ArrayList<DivisionsBasic> divisions, boolean show_arrow){
        super(context, R.layout.company_listview_layout, divisions);
        // TODO Auto-generated constructor stub
        //this.color_names = text;
        this.context = context;
        this.divisions = divisions;
        this.show_arrow = show_arrow;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View single_row;
        single_row = inflater.inflate(R.layout.company_filter_listview_layout, null, true);
        TextView textView = (TextView) single_row.findViewById(R.id.divisions_textView);
        ImageView imageView = (ImageView) single_row.findViewById(R.id.divisions_image);
        ImageView arrowImage = (ImageView) single_row.findViewById(R.id.division_arrow);

        textView.setText(divisions.get(position).title);
        textView.setTag(divisions.get(position).division_id);
        Picasso.with(getContext())
                .load(divisions.get(position).image)
                .resize(190,190)
                .centerCrop()
                .into(imageView);

        if(show_arrow)
        {
            arrowImage.setVisibility(View.VISIBLE);
        }



        return single_row;
    }


}
