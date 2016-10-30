package com.example.fbashir.emcor.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fbashir.emcor.R;
import com.example.fbashir.emcor.helpers.MyUtils;
import com.example.fbashir.emcor.models.businessServiceDetails.CorporateOffice;
import com.example.fbashir.emcor.models.businessServiceDetails.IndustryRecognition;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by fbashir on 7/25/2016.
 */

public class CorporateOfficesListAdaptar extends BaseAdapter{

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<CorporateOffice> corporateofficeslist = null;
    private ArrayList<CorporateOffice> arraylist;

    public CorporateOfficesListAdaptar(Context context, List<CorporateOffice> corporateofficeslist) {
        mContext = context;
        this.corporateofficeslist = corporateofficeslist;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<CorporateOffice>();
        this.arraylist.addAll(corporateofficeslist);
    }

    public class ViewHolder {
        TextView address;
        TextView state;
        TextView city;
        TextView phone;
        TextView fax;
        TextView website;
        ImageButton company_image;

    }

    @Override
    public int getCount() {
        return corporateofficeslist.size();
    }

    @Override
    public CorporateOffice getItem(int position) {
        return corporateofficeslist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.corporate_offices_listview_layout, null);
            // Locate the TextViews in listview_item.xml
            holder.address = (TextView) view.findViewById(R.id.company_name_textview);
            holder.state = (TextView) view.findViewById(R.id.company_address_textview);
            holder.phone = (TextView) view.findViewById(R.id.corporate_offices_phone);
            holder.fax = (TextView) view.findViewById(R.id.corporate_offices_fax);
            holder.website = (TextView) view.findViewById(R.id.corporate_offices_website);
            holder.company_image = (ImageButton) view.findViewById(R.id.company_image);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.address.setText(corporateofficeslist.get(position).getAddress());
        holder.state.setText(corporateofficeslist.get(position).getState());
        holder.phone.setText(corporateofficeslist.get(position).getPhone());
        holder.fax.setText(corporateofficeslist.get(position).getFax());
        holder.website.setText(corporateofficeslist.get(position).getWebsite());
        holder.company_image.setTag(R.string.lat_tag,corporateofficeslist.get(position).latitude);
        holder.company_image.setTag(R.string.lng_tag,corporateofficeslist.get(position).longitude);
        holder.company_image.setTag(R.string.address_tag,corporateofficeslist.get(position).getAddress());

        holder.company_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object latitude = v.getTag(R.string.lat_tag);
                Object longitude = v.getTag(R.string.lng_tag);
                Object address = v.getTag(R.string.address_tag);
                MyUtils.openMap(mContext, latitude.toString(), longitude.toString(), address.toString());
            }
        });

        holder.phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyUtils.openDialer(mContext, corporateofficeslist.get(position).getPhone());
            }
        });


        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        corporateofficeslist.clear();
        if (charText.length() == 0) {
            corporateofficeslist.addAll(arraylist);
        }
        else
        {
            for (CorporateOffice wp : arraylist)
            {
                if (wp.getAddress().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    corporateofficeslist.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

}
