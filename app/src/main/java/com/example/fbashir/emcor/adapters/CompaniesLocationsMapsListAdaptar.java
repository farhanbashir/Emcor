package com.example.fbashir.emcor.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.fbashir.emcor.R;
import com.example.fbashir.emcor.models.companies.CompaniesBasic;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * Created by fbashir on 7/25/2016.
 */

public class CompaniesLocationsMapsListAdaptar extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<CompaniesBasic> companieslist = null;
    private ArrayList<CompaniesBasic> arraylist;

    public CompaniesLocationsMapsListAdaptar(Context context, List<CompaniesBasic> companieslist){
        mContext = context;
        this.companieslist = companieslist;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<CompaniesBasic>();
        this.arraylist.addAll(companieslist);
    }

    public class ViewHolder {
        TextView company_id;
        TextView name;
        TextView logo;
        TextView address;
        TextView city;
        TextView state;
        TextView zip;
        TextView latitude;
        TextView longitude;
        TextView phone;
        TextView fax;

    }


    @Override
    public int getCount() {
        return companieslist.size();
    }

    @Override
    public CompaniesBasic getItem(int position) {
        return companieslist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final CompaniesLocationsMapsListAdaptar.ViewHolder holder;
        if (view == null) {
            holder = new CompaniesLocationsMapsListAdaptar.ViewHolder();
            view = inflater.inflate(R.layout.company_locations_maps_listview_layout, null);
            // Locate the TextViews in listview_item.xml
            holder.name = (TextView) view.findViewById(R.id.companies_textView);

            view.setTag(holder);
        } else {
            holder = (CompaniesLocationsMapsListAdaptar.ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.name.setText(companieslist.get(position).getName());

        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        companieslist.clear();
        if (charText.length() == 0) {
            companieslist.addAll(arraylist);
        }
        else
        {
            for (CompaniesBasic wp : arraylist)
            {
                if (wp.getName().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    companieslist.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }


}
