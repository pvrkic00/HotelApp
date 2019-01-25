package com.example.pvrki.bazaprobav2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapterRezervacije extends ArrayAdapter<RezevacijaDataModel> {

    private ArrayList<RezevacijaDataModel> dataSet;
    Context mContext;

    private static class ViewHolder {
        TextView txtID;
        TextView txtNapomena;

    }

    public CustomAdapterRezervacije(ArrayList<RezevacijaDataModel> data, Context context) {
        super(context, R.layout.listtemplate, data);
        this.dataSet = data;
        this.mContext=context;

    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        RezevacijaDataModel dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.listtemplate, parent, false);
            viewHolder.txtID = (TextView) convertView.findViewById(R.id.id_rezervacije);
            viewHolder.txtNapomena = (TextView) convertView.findViewById(R.id.napomena_rezrvacije);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        if (String.valueOf(dataModel.getID())=="")
        {
            viewHolder.txtID.setText("");
            viewHolder.txtNapomena.setText("Trenutno nemate rezervaija");
        }else {
            viewHolder.txtID.setText(String.valueOf(dataModel.getID()));
            viewHolder.txtNapomena.setText(dataModel.getNapomena());
        }

        // Return the completed view to render on screen
        return convertView;
    }

}

