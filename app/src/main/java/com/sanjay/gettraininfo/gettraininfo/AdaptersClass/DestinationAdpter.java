package com.sanjay.gettraininfo.gettraininfo.AdaptersClass;

import android.widget.BaseAdapter;

/**
 * Created by Dinesh on 10/4/2015.
 */

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.sanjay.gettraininfo.gettraininfo.Appcontroler.AppController;
import com.sanjay.gettraininfo.gettraininfo.ModelClass.Destinationitems;
import com.sanjay.gettraininfo.gettraininfo.R;


import java.util.List;


public class DestinationAdpter extends BaseAdapter{
    private Activity activity;
    private LayoutInflater inflater;
    private static final String TAG = DestinationAdpter.class.getSimpleName();

    private List<Destinationitems> mCategoryfeeditems;
    private Context mContext;

    public DestinationAdpter(Activity activity, List<Destinationitems> mCategoryfeeditems) {
        this.mCategoryfeeditems = mCategoryfeeditems;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return mCategoryfeeditems.size();
    }

    @Override
    public Object getItem(int position) {
        return mCategoryfeeditems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.destinationlistitem, null);



        TextView name = (TextView) convertView.findViewById(R.id.textviewitem);

        Destinationitems catfeeditems = mCategoryfeeditems.get(position);

        name.setText(catfeeditems.getDestinationname());




        return convertView;
    }

    public String getFilter(int position) {
        Destinationitems catfeeditems = mCategoryfeeditems.get(position);

       String destinationname=catfeeditems.getDestinationname();

        return destinationname;
    }
}
