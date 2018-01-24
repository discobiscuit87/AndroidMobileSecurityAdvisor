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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ProcessInfoDialog extends DialogFragment {

    //Wiping Cache, Clearing all Settings, Basically Starting all Over Again If Cleaned
    public String printDataSize = "";

    //Cache Size (Stuff Loaded Once App is Used or Launched..)
    public String printCacheSize = "";

    //APK Size
    public String printAPKSize = "";

    AppDetails cAppDetails;
    public ArrayList<AppDetails.PackageInfoStruct> packagesList;


    private static final String TAG = "ProcessInfoDialog";

  @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
    AndroidAppProcess process = getArguments().getParcelable("process");

    //Get Package Size
      getpackageSize(process.getPackageName(), getActivity());

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
    public Long totalRAM() {
        String line;
        String[] tempStringArray;
        Long totalMemory=0L;

        try {
            BufferedReader reader = new BufferedReader(new FileReader("/proc/meminfo"));
            line = reader.readLine();

            tempStringArray = line.split("\\s+");
            totalMemory = Long.parseLong(tempStringArray[1]);


        }
        catch (FileNotFoundException e) {
            Log.d("Exception", "Unable To Read /proc/meminfo");
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return (totalMemory/1024);
    }

  private Spanned getProcessInfo(AndroidAppProcess process) {

      getpackageSize(process.getPackageName(), getActivity());
      HtmlBuilder html = new HtmlBuilder();

    html.p().strong("NAME: ").append(process.name).close();
    html.p().strong("DATA SIZE: ").append(printDataSize).close();
    html.p().strong("CACHE SIZE: ").append(printCacheSize).close();
    html.p().strong("APK SIZE: ").append(printAPKSize).close();
    html.p().strong("POLICY: ").append(process.foreground ? "foreground process" : "background process").close();
    html.p().strong("PID: ").append(process.pid).close();

    try {
      Status status = process.status();
      html.p().strong("UID/GID: ").append(status.getUid()).append('/').append(status.getGid()).close();
    } catch (IOException e) {
      Log.d(TAG, String.format("Error reading /proc/%d/status.", process.pid));
    }

    // should probably be run in a background thread.
    try {
      Stat stat = process.stat();
      //html.p().strong("PPID: ").append(stat.ppid()).close();
      long bootTime = System.currentTimeMillis() - SystemClock.elapsedRealtime();
      long startTime = bootTime + (10 * stat.starttime());
      SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy KK:mm:ss a", Locale.getDefault());
      html.p().strong("START TIME: ").append(sdf.format(startTime)).close();
      html.p().strong("PROCESS CPU TIME (IN SEC): ").append((stat.stime() + stat.utime()) / 100).close(); // get into seconds

      long userModeTicks = stat.utime();
      long kernelModeTicks = stat.stime();
      long percentOfTimeUserMode;
      long percentOfTimeKernelMode;
      if ((kernelModeTicks + userModeTicks) > 0) {
        percentOfTimeUserMode = (userModeTicks * 100) / (userModeTicks + kernelModeTicks);
        percentOfTimeKernelMode = (kernelModeTicks * 100) / (userModeTicks + kernelModeTicks);
        html.p().strong("TIME EXECUTED IN USER MODE: ").append(percentOfTimeUserMode + "%").close();
        html.p().strong("TIME EXECUTED IN KERNEL MODE: ").append(percentOfTimeKernelMode + "%").close();
      }
    } catch (IOException e) {
      Log.d(TAG, String.format("Error reading /proc/%d/stat.", process.pid));
    }

    try {
      Statm statm = process.statm();
     // html.p().strong("SIZE: ").append(Formatter.formatFileSize(getActivity(), statm.getSize())).close();
      html.p().strong("Size of App in RAM: ").append(Formatter.formatFileSize(getActivity(), statm.getResidentSetSize())).close();
      html.p().strong("TOTAL RAM2: ").append(Formatter.formatFileSize(getActivity(), totalRAM())).close();
      //html.p().strong("TOTAL RAM: ").append(totalRAM()).close();
    } catch (IOException e) {
      Log.d(TAG, String.format("Error reading /proc/%d/statm.", process.pid));
    }


    return html.build();
  }

    public void getpackageSize(String pckgName, Context context) {
        cAppDetails = new AppDetails((Activity) context);
        packagesList = cAppDetails.getPackages();

        // Create Object to Access Package Manager
        PackageManager pm = context.getPackageManager();
        Method getPackageSizeInfo;
        String test = "";

        try {
            getPackageSizeInfo = pm.getClass().getMethod(
                    "getPackageSizeInfo", String.class,
                    IPackageStatsObserver.class);

            //Get Package List of Name, PName =
            getPackageSizeInfo.invoke(pm, pckgName,
                    new cachePackState());
        }
        catch (SecurityException e) {
            e.printStackTrace();
        }
        catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }


    private class cachePackState extends IPackageStatsObserver.Stub {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onGetStatsCompleted(PackageStats pStats, boolean succeeded)
                throws RemoteException {
            displayStatsMB(pStats);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void displayStatsMB(PackageStats pStats) {
        String cacheSizeNo = Formatter.formatFileSize(getContext(), pStats.cacheSize);
        String dataSizeNo = Formatter.formatFileSize(getContext(), pStats.dataSize);
        String APKSizeNo = Formatter.formatFileSize(getContext(), pStats.codeSize);
        String packageName = pStats.packageName;

        printDataSize = dataSizeNo;
        printCacheSize = cacheSizeNo;
        printAPKSize = APKSizeNo;

        Log.d("PACKAGE NAME", packageName);
        Log.d("CACHE SIZE", cacheSizeNo);
        Log.d("DATA SIZE", dataSizeNo);
        Log.d("APK SIZE", APKSizeNo);
    }
}
