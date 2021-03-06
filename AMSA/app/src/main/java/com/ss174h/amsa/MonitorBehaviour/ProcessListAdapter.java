/*
 * Copyright (C) 2015. Jared Rummler <jared.rummler@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.ss174h.amsa.MonitorBehaviour;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.jaredrummler.android.processes.models.AndroidAppProcess;
import com.squareup.picasso.Picasso;
import com.ss174h.amsa.R;
import java.util.List;


public class ProcessListAdapter extends BaseAdapter {

  public static List<AndroidAppProcess> processes;
  private final LayoutInflater inflater;
  private final Context context;
  private final Picasso picasso;
  private final int iconSize;

  public ProcessListAdapter(Context context, List<AndroidAppProcess> processes) {
    this.context = context.getApplicationContext();
    this.inflater = LayoutInflater.from(context);
    this.iconSize = Utils.toPx(context, 46);
    this.picasso = Picasso.with(context);
    this.processes = processes;
  }


  @Override public int getCount() {
    return processes.size();
  }

  @Override public AndroidAppProcess getItem(int position) {
    return processes.get(position);
  }

  @Override public long getItemId(int position) {
    return position;
  }

  @Override public View getView(int position, View convertView, ViewGroup parent) {
    final ViewHolder holder;
    if (convertView == null) {
      convertView = inflater.inflate(R.layout.list_item_process, parent, false);
      holder = new ViewHolder(convertView);
    }
    else {
      holder = (ViewHolder) convertView.getTag();
    }

    AndroidAppProcess process = getItem(position);

    ImageView imageView = holder.find(R.id.imageView);
    TextView textView = holder.find(R.id.textView);


    Drawable icon = null;
    try {
      icon = context.getPackageManager().getApplicationIcon(process.getPackageName());
      imageView.setImageDrawable(icon);
    }
    catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }

    textView.setText(Utils.getName(context, process));

    return convertView;
  }

  static class ViewHolder {

    private final SparseArray<View> views = new SparseArray<>();

    private final View view;

    public ViewHolder(View view) {
      this.view = view;
      view.setTag(this);
    }

    public <T extends View> T find(int id) {
      View v = views.get(id);
      if (v == null) {
        v = view.findViewById(id);
        views.put(id, v);
      }
      //noinspection unchecked
      return (T) v;
    }
  }
}
