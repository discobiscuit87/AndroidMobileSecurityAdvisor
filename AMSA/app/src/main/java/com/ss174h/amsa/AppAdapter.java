package com.ss174h.amsa;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class AppAdapter extends ArrayAdapter<String> {

    private final Context con;
    private final ArrayList<String> packages;

    public AppAdapter(Context context, ArrayList<String> list) {
        super(context,0,list);

        this.con = context;
        this.packages = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.simple_list_item_1,viewGroup,false);
        }

        try {
            Drawable icon = getContext().getPackageManager().getApplicationIcon(packages.get(position));
            ImageView imageView = convertView.findViewById(R.id.app_icon);
            imageView.setImageDrawable(icon);
        } catch (PackageManager.NameNotFoundException nnf) {
            return convertView;
        }

        PackageManager pm = con.getPackageManager();
        ApplicationInfo applicationInfo = null;

        try {
            applicationInfo = pm.getApplicationInfo(packages.get(position), 0);
        } catch (final PackageManager.NameNotFoundException e) {
        }

        final String appName = (String)pm.getApplicationLabel(applicationInfo);

        TextView textView = convertView.findViewById(R.id.app_name);
        textView.setText(appName);

        return convertView;
    }
}
