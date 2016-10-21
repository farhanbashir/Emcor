package com.example.fbashir.emcor.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.fbashir.emcor.R;
import com.example.fbashir.emcor.models.companyDetails.Advantage;
import com.example.fbashir.emcor.models.companyDetails.Market;

import java.util.ArrayList;

/**
 * Created by fbashir on 7/25/2016.
 */

public class CompanyAdvantagesListAdaptar extends ArrayAdapter<Advantage>{

    Context context;
    ArrayList<Advantage> advantages;

    public CompanyAdvantagesListAdaptar(Activity context, ArrayList<Advantage> advantages){
        super(context, R.layout.company_listview_layout, advantages);
        // TODO Auto-generated constructor stub
        this.context = context;
        this.advantages = advantages;

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View single_row;
        single_row = inflater.inflate(R.layout.company_advantages_listview_layout, null, true);
        TextView textView = (TextView) single_row.findViewById(R.id.company_advantage_item);

        textView.setText("\u2022 " + advantages.get(position).title);


        return single_row;
    }


}
