package com.ss174h.amsa.MonitorBehaviour;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.app.Activity;
import android.os.Debug;
import android.os.Environment;
import android.os.StatFs;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ss174h.amsa.APKScanner.APKScannerService;
import com.ss174h.amsa.R;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static android.content.Context.ACTIVITY_SERVICE;


public class MonitorBehaviourFragment extends Fragment {
    Context mContext;
    public static final String PROCESS_RESPONSE = "com.ss174h.app_scanner.intent.action.PROCESS_RESPONSE";
    private ArrayList<Process_ID> processIDLIst;

    public MonitorBehaviourFragment() {
        processIDLIst = new ArrayList<Process_ID>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		
		// for the GUI created by Android studio 
        View view = layoutInflater.inflate(R.layout.fragment_monitor_behaviour, container, false);
        TextView textView = view.findViewById(R.id.fragment);
        TextView totalMemoryView = view.findViewById(R.id.totalmemoryfragment);
        TextView totalStorageView = view.findViewById(R.id.totalstoragefragment);
        TextView totalAppRunningView = view.findViewById(R.id.displaytotalappfragment);

		//for the sideloaded apps 
        IntentFilter intentFilter = new IntentFilter(PROCESS_RESPONSE);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        Intent intent = new Intent(getContext(), APKScannerService.class);
        getContext().startService(intent);
        AppReceiver appReceiver = new AppReceiver();
        getContext().registerReceiver(appReceiver, intentFilter);

        //Test
        //readUsage();
        float test = getCpuPer();
        Log.d("TEST ANDREW", String.valueOf(test));

        ActivityManager am = (ActivityManager) getActivity().getSystemService(mContext.ACTIVITY_SERVICE);

        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(memoryInfo);
		// to get the total memory RAM of the phone 
        Long totalMemory = memoryInfo.totalMem / (1024 * 1024); //In KB Form

		// only displays one process 
        //totalMemoryView.setText("Total Memory Size: " + totalMemory + "MB" + " Process Info Size: " + am.getRunningAppProcesses().size() + " Recent Tasks: " + recentTasks.size());

        return view;
	}

	
	//show the total RAM availableable 
    public void displayTotalRAM() {
        String line;
        String[] tempStringArray;

        try {
            BufferedReader reader = new BufferedReader(new FileReader("/proc/meminfo"));
            line = reader.readLine();

            tempStringArray = line.split("\\s+");
            String totalMemory = tempStringArray[1];

            Log.d("Total Memory", totalMemory);
        }
        catch (FileNotFoundException e) {
            Log.d("Exception", "Unable To Read /proc/meminfo");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

	//get the CPU percentage 
    public String getProcessCPU(String Process_name) {
        try {
            ArrayList<String> list2 = new ArrayList<String>();
            Process p2 = Runtime.getRuntime().exec("top -m 150  -d 1 -n 1",null,null);//top -m 150  -d 1 -n 1",null,null);
            BufferedReader reader2 = new BufferedReader(new InputStreamReader(p2.getInputStream()));
            int i2 = 0;
            String line2 = reader2.readLine();
            String a[]={""};

            while (line2 != null) {
                list2.add(line2);
                line2 = reader2.readLine();
                i2++ ;
            }
            p2.waitFor();
            for (int i=0 ;i<=list2.size();i++) {
                if(list2.get(i).contains(Process_name)==true) {
                    a=list2.get(i).toString().split(" ");
                    break;
                }
            }

            for(int i=0;i<=a.length;i++) {
                if(a[i].contains("%")==true){
                    System.out.println("cpu utilization" + a[i]);
                    return a[i];
                }
            }

        }
        catch (Throwable t) {
            System.out.println("Unable to fetch cpu data ---\n"+t.fillInStackTrace());
        }

        return null;
    }

    private float getCpuPer() { //for single process
        int noOfLines = 0;
        float cpuPer = 0;
        String tempLines[];
        try {
            String[] cmd = {"ps"};
            Process process = Runtime.getRuntime().exec(cmd);
            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(process.getInputStream()));

            BufferedReader stdError = new BufferedReader(new
                    InputStreamReader(process.getErrorStream()));

            String s = null;


            if((s = stdInput.readLine()) != null) {
                while ((s = stdInput.readLine()) != null) {
                    tempLines = s.split("\\s+");

                    Log.d("PID", tempLines[1]);
                    //Log.d("PPID", tempLines[2]);
                    //Log.d("VSIZE", tempLines[3]);
                    //Log.d("Fifth Line", tempLines[4]);
                    //Log.d("Sixth Line", tempLines[5]);
                    //Log.d("Seventh Line", tempLines[6]);
                    //Log.d("Eigth Line", tempLines[7]);
                    Log.d("Process Names", tempLines[8]);

                    

                    //processIDLIst.add(new Process_ID(tempLines[1], tempLines[8]));
					//log.d("Process ID",
                    noOfLines++; //To Know Number of Processes
                }
           }


            // read the output from the command
            //System.out.println("Here is the standard output of the command:\n");

            // read any errors from the attempted command
            //System.out.println("Here is the standard error of the command (if any):\n");
            //while ((s = stdError.readLine()) != null) {
            //System.out.println(s);
            //}
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cpuPer;
    }


    //Kill Process
    public void killProcess(String processName) {
        ActivityManager am = (ActivityManager) mContext.getSystemService(mContext.ACTIVITY_SERVICE);

        for(ActivityManager.RunningAppProcessInfo PID : am.getRunningAppProcesses()) {
            am.killBackgroundProcesses(PID.processName);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public class AppReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            ArrayList<String> array = intent.getStringArrayListExtra("array");

            if (array.isEmpty()) {
                Toast.makeText(context, "No sideloaded applications installed!", Toast.LENGTH_LONG).show();
            } else {
                for (String line : array) {
                    Log.d("Sidedloaded", line);
                    //Integer PIDNo = android.os.Process.getUidForName(line);
                    //Log.d("Sided Loaded PID", String.valueOf(PIDNo));

                }
            }
        }
    }
}
