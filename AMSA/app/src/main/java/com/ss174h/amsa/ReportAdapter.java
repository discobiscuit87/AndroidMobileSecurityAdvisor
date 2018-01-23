package com.ss174h.amsa;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ReportAdapter extends ArrayAdapter<Pair<String,String>> {

    private final Context con;
    private final ArrayList<Pair<String,String>> packs;

    public ReportAdapter(Context context, ArrayList<Pair<String,String>> arrayList) {
        super(context,0,arrayList);
        this.con = context;
        this. packs = arrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.simple_list_item_2,viewGroup,false);
        }

        try {
            Drawable icon = convertView.getContext().getPackageManager().getApplicationIcon(packs.get(position).second);
            ImageView imageView = convertView.findViewById(R.id.app_icon);
            imageView.setImageDrawable(icon);
        } catch (PackageManager.NameNotFoundException nnf) {
            return convertView;
        }

        PackageManager pm = con.getPackageManager();
        ApplicationInfo applicationInfo = null;

        try {
            applicationInfo = pm.getApplicationInfo(packs.get(position).second, 0);
        } catch (final PackageManager.NameNotFoundException e) {
        }

        final String appName = (String)pm.getApplicationLabel(applicationInfo);

        TextView t1 = convertView.findViewById(R.id.app_name);
        t1.setText(appName);

        TextView t2 = convertView.findViewById(R.id.status);
        if(packs.get(position).first.equals("0")) {
            t2.setText("Application is all clear!");
            t2.setTextColor(Color.GREEN);
        } else if(packs.get(position).first.equals("-1")) {
            t2.setText("Application is not in database!");
            t2.setTextColor(Color.YELLOW);
        } else {
            t2.setText("Application contains malware!");
            t2.setTextColor(Color.RED);
        }

        return convertView;
    }
}
