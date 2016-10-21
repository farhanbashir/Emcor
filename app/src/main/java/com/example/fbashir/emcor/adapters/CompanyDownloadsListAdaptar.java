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
import com.example.fbashir.emcor.models.companyDetails.Download;

import java.util.ArrayList;

/**
 * Created by fbashir on 7/25/2016.
 */

public class CompanyDownloadsListAdaptar extends ArrayAdapter<Download>{

    //String[] color_names;
    Context context;
    ArrayList<Download> downloads;

    public CompanyDownloadsListAdaptar(Activity context, ArrayList<Download> downloads){
        super(context, R.layout.company_listview_layout, downloads);
        // TODO Auto-generated constructor stub
        //this.color_names = text;
        this.context = context;
        this.downloads = downloads;

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View single_row;
        single_row = inflater.inflate(R.layout.company_downloads_listview_layout, null, true);
        TextView textView = (TextView) single_row.findViewById(R.id.company_download_item);

        textView.setText(downloads.get(position).pdf_name);


        return single_row;
    }


}
