package com.example.fbashir.emcor.adapters;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.example.fbashir.emcor.R;
import com.example.fbashir.emcor.models.companies.CompaniesBasic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;


/**
 * Created by fbashir on 7/25/2016.
 */

public class CompaniesListAdaptar extends BaseAdapter implements SectionIndexer {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<CompaniesBasic> companieslist = null;
    private ArrayList<CompaniesBasic> arraylist;

    String[] sections ;
    public HashMap<String, Integer> alphaIndexer;

    public CompaniesListAdaptar(Context context, List<CompaniesBasic> companieslist){
        mContext = context;
        this.companieslist = companieslist;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<CompaniesBasic>();
        this.arraylist.addAll(companieslist);

        setIndexer();

    }

    public void setIndexer()
    {
        alphaIndexer = new HashMap<String, Integer>();
        int size = companieslist.size();

        for (int x = 0; x < size; x++) {
            String s = companieslist.get(x).name;
            String ch = s.substring(0, 1);
            ch = ch.toUpperCase();
            if (!alphaIndexer.containsKey(ch))
                alphaIndexer.put(ch, x);
        }

        Set<String> sectionLetters = alphaIndexer.keySet();
        ArrayList<String> sectionList = new ArrayList<>(sectionLetters);
        Collections.sort(sectionList);
        sections = new String[sectionList.size()];
        sectionList.toArray(sections);
    }

    @Override
    public Object[] getSections() {

        return sections;
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        return 0;
        //return alphaIndexer.get(sections[sectionIndex]);
    }

    @Override
    public int getSectionForPosition(int position) {
        String ch = companieslist.get(position).getName().substring(0,1).toUpperCase();
        return alphaIndexer.get(ch);
        //return 0;
    }

    public int getPositionForAlphabet(String alphabet)
    {
        if(alphaIndexer.containsKey(alphabet))
        {
            return alphaIndexer.get(alphabet);
        }
        else
        {
            return -1;
        }
    }

    public class ViewHolder {
        TextView company_id;
        TextView name;
        TextView sort_key;
        View divider;
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

    public int getTotalCompaniesCount()
    {
        return arraylist.size();
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
        final CompaniesListAdaptar.ViewHolder holder;
        if (view == null) {
            holder = new CompaniesListAdaptar.ViewHolder();
            view = inflater.inflate(R.layout.company_listview_layout, null);
            // Locate the TextViews in listview_item.xml
            holder.name = (TextView) view.findViewById(R.id.companies_textView);
            holder.sort_key = (TextView) view.findViewById(R.id.sort_key);
            holder.divider = (View) view.findViewById(R.id.divider);
            view.setTag(holder);
        } else {
            holder = (CompaniesListAdaptar.ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.name.setText(companieslist.get(position).getName());
        holder.name.setTag(companieslist.get(position).getCompany_id());

        //int section = getSectionForPosition(position);
//        if (position == getPositionForSection(position)) {
//            holder.sort_key.setText(sections[position]);
//            holder.sort_key.setVisibility(View.VISIBLE);
//        } else {
//            holder.sort_key.setVisibility(View.GONE);
//        }

        String ch = companieslist.get(position).getName().substring(0,1).toUpperCase();
        Log.d("ch","position="+position+" ch="+ch+" companies="+companieslist.size());
        if(position != companieslist.size() -1)
        {
            String ch1 = companieslist.get(position + 1).getName().substring(0,1).toUpperCase();
            if(ch.equals(ch1))
            {
                holder.divider.setVisibility(View.VISIBLE);
                //holder.divider.setVisibility(View.VISIBLE);
            }
        }

        int sectionPosition = alphaIndexer.get(ch);

        if (position == sectionPosition) {
            holder.sort_key.setText(ch);
            holder.sort_key.setVisibility(View.VISIBLE);
        } else {
            holder.sort_key.setVisibility(View.GONE);
        }

//        if(position != companieslist.size())
//        {
//            String ch1 = companieslist.get(position + 1).getName().substring(0,1).toUpperCase();
//            if(ch.equals(ch1))
//            {
//                holder.divider.setVisibility(View.VISIBLE);
//            }
//        }

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

        }

        Collections.sort(companieslist, new Comparator<CompaniesBasic>() {
            @Override
            public int compare(final CompaniesBasic object1, final CompaniesBasic object2) {
                return object1.getName().compareTo(object2.getName());
            }
        });

        setIndexer();

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
