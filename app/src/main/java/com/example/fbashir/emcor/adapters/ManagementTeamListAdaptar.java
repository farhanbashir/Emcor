package com.example.fbashir.emcor.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fbashir.emcor.R;
import com.example.fbashir.emcor.helpers.MyUtils;
import com.example.fbashir.emcor.models.businessServiceDetails.Team;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * Created by fbashir on 7/25/2016.
 */

public class ManagementTeamListAdaptar extends BaseAdapter{

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<Team> teamlist = null;
    private ArrayList<Team> arraylist;

    public ManagementTeamListAdaptar(Context context, List<Team> teamlist) {
        mContext = context;
        this.teamlist = teamlist;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<Team>();
        this.arraylist.addAll(teamlist);
    }

    public class ViewHolder {
        TextView name;
        TextView email;
        TextView email_heading;
        TextView contact_number_heading;
        TextView contact_number;
        TextView designation;
        ImageView photo;
    }

    @Override
    public int getCount() {
        return teamlist.size();
    }

    @Override
    public Team getItem(int position) {
        return teamlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.management_team_listview_layout, null);
            // Locate the TextViews in listview_item.xml
            holder.name = (TextView) view.findViewById(R.id.name);
            holder.designation = (TextView) view.findViewById(R.id.designation);
            holder.email = (TextView) view.findViewById(R.id.company_team_email);
            holder.email_heading = (TextView) view.findViewById(R.id.company_team_email_heading);
            holder.contact_number = (TextView) view.findViewById(R.id.company_team_phone);
            holder.contact_number_heading = (TextView) view.findViewById(R.id.company_team_phone_heading);
            holder.photo = (ImageView) view.findViewById(R.id.member_photo);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.name.setText(teamlist.get(position).getName());
        holder.email.setText(teamlist.get(position).getEmail());
        holder.contact_number.setText(teamlist.get(position).getContact_number());
        holder.designation.setText(teamlist.get(position).getDesignation());

        if(!teamlist.get(position).getEmail().equals(""))
        {
            holder.email_heading.setVisibility(View.VISIBLE);
            holder.email.setVisibility(View.VISIBLE);
        }
        if(!teamlist.get(position).getContact_number().equals(""))
        {
            holder.contact_number_heading.setVisibility(View.VISIBLE);
            holder.contact_number.setVisibility(View.VISIBLE);

            holder.contact_number.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyUtils.openDialer(mContext, teamlist.get(position).getContact_number());
                }
            });


        }

        if(!teamlist.get(position).getPhoto().equals(""))
        {
            holder.photo.setVisibility(View.VISIBLE);

            Picasso.with(mContext)
                    .load(teamlist.get(position).getPhoto())
                    .resize(60,60)
                    .centerCrop()
                    .into(holder.photo);
        }



        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        teamlist.clear();
        if (charText.length() == 0) {
            teamlist.addAll(arraylist);
        }
        else
        {
            for (Team wp : arraylist)
            {
                if (wp.getName().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    teamlist.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

}
