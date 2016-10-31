package com.example.fbashir.emcor.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.fbashir.emcor.R;
import com.example.fbashir.emcor.helpers.MyUtils;
import com.example.fbashir.emcor.models.businessServiceDetails.IndustryRecognition;
import com.example.fbashir.emcor.models.businessServiceDetails.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by fbashir on 7/25/2016.
 */

public class IndustryRecognitionListAdaptar extends BaseAdapter{

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<IndustryRecognition> industryrecognitionlist = null;
    private ArrayList<IndustryRecognition> arraylist;

    public IndustryRecognitionListAdaptar(Context context, List<IndustryRecognition> industryrecognitionlist) {
        mContext = context;
        this.industryrecognitionlist = industryrecognitionlist;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<IndustryRecognition>();
        this.arraylist.addAll(industryrecognitionlist);
    }

    public class ViewHolder {
        TextView title;
        TextView description;

    }

    @Override
    public int getCount() {
        return industryrecognitionlist.size();
    }

    @Override
    public IndustryRecognition getItem(int position) {
        return industryrecognitionlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.industry_recognition_listview_layout, null);
            // Locate the TextViews in listview_item.xml
            holder.title = (TextView) view.findViewById(R.id.industry_recognition_title);
            holder.description = (TextView) view.findViewById(R.id.industry_recognition_description);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.title.setText(industryrecognitionlist.get(position).getTitle());
        holder.description.setText(industryrecognitionlist.get(position).getDescription());

        if(!industryrecognitionlist.get(position).getFile().equals(""))
        {
            holder.title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyUtils.openViewer(mContext, industryrecognitionlist.get(position).getFile());
                }
            });
        }




        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        industryrecognitionlist.clear();
        if (charText.length() == 0) {
            industryrecognitionlist.addAll(arraylist);
        }
        else
        {
            for (IndustryRecognition wp : arraylist)
            {
                if (wp.getTitle().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    industryrecognitionlist.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

}
