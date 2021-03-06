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

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.text.Spanned;
import android.content.pm.PackageManager;
import android.text.format.Formatter;
import android.util.Log;
import com.jaredrummler.android.processes.models.AndroidAppProcess;
import com.jaredrummler.android.processes.models.Stat;
import com.jaredrummler.android.processes.models.Statm;
import com.jaredrummler.android.util.HtmlBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class ProcessInfoDialog extends DialogFragment {

    private long trafficPackageRx;
    private long trafficPackageTx;
    private long allTrafficRx;
    private long allTraffixTx;

    private long networkPackageRx;
    private long networkPackageTx;
    private long allNetworkRx;
    private long allNetworkTx;

    private int position;

    private static final String TAG = "ProcessInfoDialog";



  @Override public Dialog onCreateDialog(Bundle savedInstanceState) {


    requestPermissions();
    AndroidAppProcess process = getArguments().getParcelable("process");
    position = getArguments().getInt("position");


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
    //show the total RAM available
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
    }
    catch (IOException e) {
      Log.d(TAG, String.format("Error reading /proc/%d/statm.", process.pid));
    }

      fillData(process.getPackageName());

      if (networkPackageRx !=0 && networkPackageTx!=0) {
          html.p().strong("TRAFFIC RECEIVED: ").append(Formatter.formatFileSize(getActivity(), networkPackageRx));
          html.p().strong("TRAFFIC SENT: ").append(Formatter.formatFileSize(getActivity(), networkPackageTx));
      }else
      {
          html.p().strong("TRAFFIC RECEIVED: ").append(Formatter.formatFileSize(getActivity(), trafficPackageRx));
          html.p().strong("TRAFFIC SENT: ").append(Formatter.formatFileSize(getActivity(), trafficPackageTx));
      }



      //call arraylist of addresses

      ProcessRemoteIP p = LogRemoteIPIntentService.processesRemoteIPs.get(position);

      html.p().u().strong("TRAFFIC DESTINATIONS").br().close();
      final String item1;
      final String item2_type;
      final String item2_value;
      if (p.getRemoteAddresses().size()>0) {

          for (RemoteAddress address : p.getRemoteAddresses()) {
              html.p().strong("HOST NAME: ").font(Color.BLUE, address.getRemoteAdd().getHostName());
              html.p().strong("IP Address: ").append(address.getRemoteAdd().getHostAddress());
              html.p().strong("PORT: ").append(address.getRemotePort());
              html.p().strong("Connection Info: ").append(KnownPorts.CompileConnectionInfo(address.getRemotePort(), address.getType()));
              String ip = address.getRemoteAdd().getHostAddress();
              String country = getCountry(ip);
              html.p().strong("Country: ").append(country);
          }

      }else
          html.p().strong().font(Color.RED, "No addresses are registered");
    return html.build();
  }

    public static int getPackageUid(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        int uid = -1;
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_META_DATA);
            Log.d(ProcessInfoDialog.class.getSimpleName(), packageInfo.packageName);
            uid = packageInfo.applicationInfo.uid;
        }
        catch (PackageManager.NameNotFoundException e) {
        }
        return uid;
    }

    private void fillData(String packageName) {
        int uid = getPackageUid(getActivity(), packageName);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            NetworkStatsManager networkStatsManager = (NetworkStatsManager) getActivity().getSystemService(Context.NETWORK_STATS_SERVICE);
            NetworkStatsHelper networkStatsHelper = new NetworkStatsHelper(networkStatsManager, uid, getActivity());
            fillNetworkStatsAll(networkStatsHelper);
            fillNetworkStatsPackage(uid, networkStatsHelper);
        }
        fillTrafficStatsAll();
        fillTrafficStatsPackage(uid);
    }

    private void fillNetworkStatsAll(NetworkStatsHelper networkStatsHelper) {
        long mobileWifiRx = networkStatsHelper.getAllRxBytesMobile(getActivity()) + networkStatsHelper.getAllRxBytesWifi();
        long mobileWifiTx = networkStatsHelper.getAllTxBytesMobile(getActivity()) + networkStatsHelper.getAllTxBytesWifi();

        allNetworkRx = mobileWifiRx;
        allNetworkTx = mobileWifiTx;
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void fillNetworkStatsPackage(int uid, NetworkStatsHelper networkStatsHelper) {
        long mobileWifiRx = networkStatsHelper.getPackageRxBytesMobile(getActivity()) + networkStatsHelper.getPackageRxBytesWifi();
        long mobileWifiTx = networkStatsHelper.getPackageTxBytesMobile(getActivity()) + networkStatsHelper.getPackageTxBytesWifi();

        networkPackageRx = mobileWifiRx;
        networkPackageTx = mobileWifiTx;
    }

    private void fillTrafficStatsAll() {
        allTrafficRx = TrafficStatsHelper.getAllRxBytes();
        allTraffixTx = TrafficStatsHelper.getAllTxBytes();
    }

    private void fillTrafficStatsPackage(int uid) {
        trafficPackageRx = TrafficStatsHelper.getPackageRxBytes(uid);
        trafficPackageTx = TrafficStatsHelper.getPackageTxBytes(uid);
    }

    private boolean hasPermissionToReadPhoneStats() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED) {
            return false;
        }
        else {
            return true;
        }
    }

    private void requestPhoneStateStats() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
    }

    private boolean hasPermissionToReadNetworkHistory() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        final AppOpsManager appOps = (AppOpsManager) getActivity().getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(), getActivity().getPackageName());
        if (mode == AppOpsManager.MODE_ALLOWED) {
            return true;
        }
        appOps.startWatchingMode(AppOpsManager.OPSTR_GET_USAGE_STATS,
                getActivity().getApplicationContext().getPackageName(),
                new AppOpsManager.OnOpChangedListener() {
                    @Override
                    @TargetApi(Build.VERSION_CODES.M)
                    public void onOpChanged(String op, String packageName) {
                        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                                android.os.Process.myUid(), getActivity().getPackageName());
                        if (mode != AppOpsManager.MODE_ALLOWED) {
                            return;
                        }
                        appOps.stopWatchingMode(this);
                    }
                });
        requestReadNetworkHistoryAccess();
        return false;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void requestReadNetworkHistoryAccess() {
        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        startActivity(intent);
    }

    private void requestPermissions() {
        if (!hasPermissionToReadNetworkHistory()) {
            return;
        }
        if (!hasPermissionToReadPhoneStats()) {
            requestPhoneStateStats();
        }
    }

    private String getCountry(String ip) {
      String country = "";
      String line;
      String address = "https://freegeoip.net/json/"+ip;

      try {
          URL url = new URL(address);
          HttpURLConnection connection = (HttpURLConnection) url.openConnection();
          connection.setRequestMethod("GET");

          InputStream in = new BufferedInputStream(connection.getInputStream());
          BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
          StringBuilder sb = new StringBuilder();
          while((line = bufferedReader.readLine()) != null) {
              sb.append(line).append("\n");
          }
          String response = sb.toString();
          JSONObject jsonObject = new JSONObject(response);
          country = jsonObject.getString("country_name");
      }  catch (MalformedURLException e) {
          Log.e("Error", "Malformed URL Exception");
      } catch (IOException io) {
          Log.e("Error: ", "IO Exception");
      } catch (JSONException jso) {
          Log.e("Error: ", "JSON Exception");
      }
        
      return country;
    }

    private boolean hasPermissions() {
        return hasPermissionToReadNetworkHistory() && hasPermissionToReadPhoneStats();
    }
}



