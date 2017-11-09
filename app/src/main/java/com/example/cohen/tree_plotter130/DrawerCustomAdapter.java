package com.example.cohen.tree_plotter130;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by cohen on 11/09/2017.
 */

public class DrawerCustomAdapter extends ArrayAdapter<DrawerObjectDefiner> {

    Context context;
    int layoutResourceId;
    DrawerObjectDefiner data[] = null;

    public DrawerCustomAdapter(Context context, int layoutResourceId, DrawerObjectDefiner[] data){
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        listItem = inflater.inflate(layoutResourceId, parent, false);

        ImageView imageView = (ImageView)listItem.findViewById(R.id.imageViewItem);
        TextView textViewName = (TextView)listItem.findViewById(R.id.textViewName);

        DrawerObjectDefiner folder = data[position];

        imageView.setImageResource(folder.icon);
        textViewName.setText(folder.name);

        return listItem;
    }
}
