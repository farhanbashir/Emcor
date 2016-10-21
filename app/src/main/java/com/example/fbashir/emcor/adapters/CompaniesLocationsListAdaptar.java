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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;


/**
 * Created by fbashir on 7/25/2016.
 */

public class CompaniesLocationsListAdaptar extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<CompaniesBasic> companieslist = null;
    private ArrayList<CompaniesBasic> arraylist;

    public CompaniesLocationsListAdaptar(Context context, List<CompaniesBasic> companieslist){
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
        TextView toll_free;
        TextView division_id;

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
        final CompaniesLocationsListAdaptar.ViewHolder holder;
        if (view == null) {
            holder = new CompaniesLocationsListAdaptar.ViewHolder();
            view = inflater.inflate(R.layout.company_locations_listview_layout, null);
            // Locate the TextViews in listview_item.xml
            holder.name = (TextView) view.findViewById(R.id.companies_textView);
            holder.address = (TextView) view.findViewById(R.id.companies_address);

            view.setTag(holder);
        } else {
            holder = (CompaniesLocationsListAdaptar.ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.name.setText(companieslist.get(position).getName());
        holder.name.setTag(companieslist.get(position).getCompany_id());
        holder.address.setText(companieslist.get(position).getAddress());

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

    public int getTotalCompaniesCount()
    {
        return arraylist.size();
    }

    public void filterByDivision(ArrayList<String> divisions)
    {
        //division = division.toLowerCase(Locale.getDefault());
        companieslist.clear();
        if (divisions.size() == 0) {
            companieslist.addAll(arraylist);
        }
        else
        {
            for(int i=0; i< divisions.size(); i++)
            {
                String division = divisions.get(i);
                for (CompaniesBasic wp : arraylist)
                {
                    if (wp.getDivision().equals(division))
                    {
                        companieslist.add(wp);
                    }
                }
            }
            Collections.sort(companieslist, new Comparator<CompaniesBasic>() {
                @Override
                public int compare(final CompaniesBasic object1, final CompaniesBasic object2) {
                    return object1.getName().compareTo(object2.getName());
                }
            });
        }
        notifyDataSetChanged();
    }

//    public void filterByDivision(String division)
//    {
//        division = division.toLowerCase(Locale.getDefault());
//        companieslist.clear();
//        if (division.length() == 0) {
//            companieslist.addAll(arraylist);
//        }
//        else
//        {
//            for (CompaniesBasic wp : arraylist)
//            {
//                if (wp.getDivision().equals(division))
//                {
//                    companieslist.add(wp);
//                }
//            }
//        }
//        notifyDataSetChanged();
//    }


}
