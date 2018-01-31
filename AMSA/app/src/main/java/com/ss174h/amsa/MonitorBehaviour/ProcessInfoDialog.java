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

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.text.Spanned;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.text.format.Formatter;
import android.util.Log;
import com.jaredrummler.android.processes.models.AndroidAppProcess;
import com.jaredrummler.android.processes.models.Stat;
import com.jaredrummler.android.processes.models.Statm;
import com.jaredrummler.android.processes.models.Status;
import com.jaredrummler.android.util.HtmlBuilder;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProcessInfoDialog extends DialogFragment {

    private static final String TAG = "ProcessInfoDialog";

  @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
    AndroidAppProcess process = getArguments().getParcelable("process");


      return new AlertDialog.Builder(getActivity())
        .setTitle(Utils.getName(getActivity(), process))
        .setMessage(getProcessInfo(process))
        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

          @Override public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
          }
        })
        .create();
  }
    //show the total RAM availableable
    public long totalRAM() {

        ActivityManager actManager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
        actManager.getMemoryInfo(memInfo);
        long totalMemory = memInfo.totalMem;

        return (memInfo.totalMem);

    }

  private Spanned getProcessInfo(AndroidAppProcess process) {
    HtmlBuilder html = new HtmlBuilder();

    html.p().strong("PACKAGE NAME: ").append(process.name).close();
    html.p().strong("POLICY: ").append(process.foreground ? "foreground process" : "background process").close();
    html.p().strong("PID: ").append(process.pid).close();

    // should probably be run in a background thread.
    try {
      Stat stat = process.stat();
      long bootTime = System.currentTimeMillis() - SystemClock.elapsedRealtime();
      long startTime = bootTime + (10 * stat.starttime());
      SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy KK:mm:ss a", Locale.getDefault());
      html.p().strong("START TIME: ").append(sdf.format(startTime)).close();
      long upTime = System.currentTimeMillis() ;
      double pencentageCPU = (stat.stime() + stat.utime())/((upTime - startTime)/1000);
      html.p().strong("CPU USED: ").append(pencentageCPU +"%").close();
      if (pencentageCPU > 60.0)
       html.p().strong("This application using a large percentage of CPU");
    } catch (IOException e) {
      Log.d(TAG, String.format("Error reading /proc/%d/stat.", process.pid));
    }

    try {
      Statm statm = process.statm();
      html.p().strong("Size in RAM: ").append(Formatter.formatFileSize(getActivity(), statm.getResidentSetSize())).close();
      double percentageRAM = statm.getResidentSetSize()*100/totalRAM();

      html.p().strong("RAM USED: ").append(percentageRAM  +"%").close();

      if (percentageRAM>30)
          html.p().strong("This application using a large percentage of RAM");
    } catch (IOException e) {
      Log.d(TAG, String.format("Error reading /proc/%d/statm.", process.pid));
    }

    return html.build();
  }


}
