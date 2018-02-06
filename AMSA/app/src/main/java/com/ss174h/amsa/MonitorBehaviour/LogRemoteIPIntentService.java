package com.ss174h.amsa.MonitorBehaviour;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.jaredrummler.android.processes.models.AndroidAppProcess;

import static java.lang.Thread.sleep;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class LogRemoteIPIntentService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.ss174h.amsa.MonitorBehaviour.action.FOO";
    private static final String ACTION_BAZ = "com.ss174h.amsa.MonitorBehaviour.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.ss174h.amsa.MonitorBehaviour.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.ss174h.amsa.MonitorBehaviour.extra.PARAM2";

    private ArrayList<String> addresses;
    private ArrayList<String> lines;

    public static List<ProcessRemoteIP> processesRemoteIPs = new ArrayList<>();

    public LogRemoteIPIntentService() {
        super("LogRemoteIPIntentService");

        for(int i=0;i<ProcessListAdapter.processes.size();i++){
            ProcessRemoteIP p = new ProcessRemoteIP(ProcessListAdapter.processes.get(i).pid);
            processesRemoteIPs.add(p);
        }

    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, LogRemoteIPIntentService.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, LogRemoteIPIntentService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        do {
            for(int i=0;i<ProcessListAdapter.processes.size();i++) {

                String filepathTCP = "/proc/net/tcp";
                String filepathTCP6 = "/proc/net/tcp6";
                String filepathUDP = "/proc/net/udp";
                String filepathUDP6 = "/proc/net/udp6";
                addresses = new ArrayList<>();

                AndroidAppProcess process = ProcessListAdapter.processes.get(i);


                try {
                    String result = getStringFromFile(filepathTCP);
                    getAddresses(lines, process.uid);

                    result = getStringFromFile(filepathTCP6);
                    getAddressesTCP6(lines, process.uid);

                    result = getStringFromFile(filepathUDP);
                    getAddressesUDP(lines, process.uid);

                    //html.p().strong("TRAFFIC DESTINATIONS:");
                    for (String address : addresses) {
                        processesRemoteIPs.get(i).addAddress(address);
                        //address += "\n";
                        //html.p(address);
                    }
                } catch (Exception e) {

                }
            }

            try {
                sleep(2000);
            }

            catch(InterruptedException ie)
            {

            }

        }while(true);


    }


    public String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        lines = new ArrayList<>();
        while ((line = reader.readLine()) != null) {
            lines.add(line);
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }

    public String getStringFromFile (String filePath) throws Exception {
        File fl = new File(filePath);
        FileInputStream fin = new FileInputStream(fl);
        String ret = convertStreamToString(fin);
        //Make sure you close all streams.
        fin.close();
        return ret;
    }

    public void getAddresses(ArrayList<String> arrayList, int uid) {

        for(int i=1;i<arrayList.size();i++) {
            if(arrayList.get(i).contains(" "+Integer.toString(uid)+" ")) {
                String hex = arrayList.get(i).substring(20,28);
                convertHexTCP(hex);
            }
        }
    }

    public void convertHexTCP(String hex) {
        String ip = "";
        String four = hex.substring(0,2);
        String three = hex.substring(2,4);
        String two = hex.substring(4,6);
        String one = hex.substring(6,8);
        String flip = one+two+three+four;

        for(int i = 0; i < flip.length(); i = i + 2) {
            ip = ip + Integer.valueOf(flip.substring(i, i+2), 16) + ".";
        }

        addresses.add(ip.substring(0,ip.length()-1)+ " (TCP)");
    }

    public void getAddressesTCP6(ArrayList<String> arrayList, int uid) {

        for(int i=1;i<arrayList.size();i++) {
            if(arrayList.get(i).contains(" "+Integer.toString(uid)+" ")) {
                String hex = arrayList.get(i).substring(44,76);
                convertHexTCP6(hex);
            }
        }
    }

    public void convertHexTCP6(String hex) {
        hex = new StringBuilder(hex).reverse().toString();
        StringBuilder ip = new StringBuilder();
        for(int i=0;i<hex.length();i=i+8){
            String word = hex.substring(i,i+8);
            for (int j = word.length() - 1; j >= 0; j = j - 2) {
                ip.append(word.substring(j - 1, j + 1));
                ip.append((j==5)?":":"");//in the middle
            }
            ip.append(":");
        }

        addresses.add(ip.substring(0,ip.length()-1) + " (TCPv6)");
    }

    public void getAddressesUDP(ArrayList<String>arrayList, int uid) {

        for(int i=1;i<arrayList.size();i++) {
            if(arrayList.get(i).contains(" "+Integer.toString(uid)+" ")) {
                String hex = arrayList.get(i).substring(22,30);
                Log.e("Hex",hex);
                convertHexUDP(hex);
            }
        }
    }

    public void convertHexUDP(String hex) {
        String ip = "";
        String four = hex.substring(0,2);
        String three = hex.substring(2,4);
        String two = hex.substring(4,6);
        String one = hex.substring(6,8);
        String flip = one+two+three+four;

        for(int i = 0; i < flip.length(); i = i + 2) {
            ip = ip + Integer.valueOf(flip.substring(i, i+2), 16) + ".";
        }

        addresses.add(ip.substring(0,ip.length()-1)+ " (UDP)");
    }

}
