package com.example.fbashir.emcor.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.fbashir.emcor.R;
import com.example.fbashir.emcor.helpers.BulletTextUtil;
import com.example.fbashir.emcor.models.companyDetails.Service;

import java.util.ArrayList;

/**
 * Created by fbashir on 7/25/2016.
 */

public class CompanyServicesListAdaptar extends ArrayAdapter<Service>{

    //String[] color_names;
    Context context;
    ArrayList<Service> services;

    public CompanyServicesListAdaptar(Activity context, ArrayList<Service> services){
        super(context, R.layout.company_listview_layout, services);
        this.context = context;
        this.services = services;

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View single_row;
        single_row = inflater.inflate(R.layout.company_services_listview_layout, null, true);
        TextView textView = (TextView) single_row.findViewById(R.id.company_service_item);
        TextView item_details = (TextView) single_row.findViewById(R.id.company_service_item_detail);

        textView.setText(services.get(position).title);
        int len = services.get(position).child.size();
        CharSequence[] cslines = new CharSequence[len];

        for(int a=0;a<len;a++)
        {
            cslines[a] = services.get(position).child.get(a).title;
        }

        CharSequence bulletedList = BulletTextUtil.makeBulletList(10, cslines);//BulletListUtil.makeBulletList("First line", "Second line", "Really long third line that will wrap and indent properly.");
        item_details.setText(bulletedList);

        return single_row;
    }


}
