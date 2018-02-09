package com.ss174h.amsa.MonitorBehaviour;

import android.app.IntentService;
import android.content.Intent;
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

public class LogRemoteIPIntentService extends IntentService {

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

    @Override
    protected void onHandleIntent(Intent intent) {

        do {
            for(int i=0;i<ProcessListAdapter.processes.size();i++) {

                String filepathTCP = "/proc/net/tcp";
                String filepathTCP6 = "/proc/net/tcp6";
                String filepathUDP = "/proc/net/udp";
                addresses = new ArrayList<>();

                AndroidAppProcess process = ProcessListAdapter.processes.get(i);


                try {
                    getStringFromFile(filepathTCP);
                    getAddresses(lines, process.uid);

                    getStringFromFile(filepathTCP6);
                    getAddressesTCP6(lines, process.uid);

                    getStringFromFile(filepathUDP);
                    getAddressesUDP(lines, process.uid);

                    for (String address : addresses) {
                        processesRemoteIPs.get(i).addAddress(address);
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

    public void convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line;
        lines = new ArrayList<>();
        while ((line = reader.readLine()) != null) {
            lines.add(line);
        }
        reader.close();
    }

    public void getStringFromFile (String filePath) throws Exception {
        File fl = new File(filePath);
        FileInputStream fin = new FileInputStream(fl);
        convertStreamToString(fin);
        //Make sure you close all streams.
        fin.close();
    }

    public void getAddresses(ArrayList<String> arrayList, int uid) {

        for(int i=1;i<arrayList.size();i++) {
            if(arrayList.get(i).contains(" "+Integer.toString(uid)+" ")) {
                String hex = arrayList.get(i).substring(20,28);
                String port = arrayList.get(i).substring(29,33);
                convertHexTCP(hex,port);
            }
        }
    }

    public void convertHexTCP(String hex, String port) {
        String ip = "";
        String port1 = "";
        String four = hex.substring(0,2);
        String three = hex.substring(2,4);
        String two = hex.substring(4,6);
        String one = hex.substring(6,8);
        String flip = one+two+three+four;

        for(int i = 0; i < flip.length(); i = i + 2) {
            ip = ip + Integer.valueOf(flip.substring(i, i+2), 16) + ".";
        }
        port1 += Integer.valueOf(port,16);
        if(port1.contains("443")) {
            addresses.add(ip.substring(0,ip.length()-1) + " - Port " + port1 + " (HTTPS, TCP)");
        } else {
            addresses.add(ip.substring(0,ip.length()-1) + " - Port " + port1 + " (TCP)");
        }

    }

    public void getAddressesTCP6(ArrayList<String> arrayList, int uid) {

        for(int i=1;i<arrayList.size();i++) {
            if(arrayList.get(i).contains(" "+Integer.toString(uid)+" ")) {
                String hex = arrayList.get(i).substring(44,76);
                String port = arrayList.get(i).substring(77,81);
                convertHexTCP6(hex,port);
            }
        }
    }

    public void convertHexTCP6(String hex, String port) {
        hex = new StringBuilder(hex).reverse().toString();
        StringBuilder ip = new StringBuilder();
        String port1 = "";
        for(int i=0;i<hex.length();i=i+8){
            String word = hex.substring(i,i+8);
            for (int j = word.length() - 1; j >= 0; j = j - 2) {
                ip.append(word.substring(j - 1, j + 1));
                ip.append((j==5)?":":"");//in the middle
            }
            ip.append(":");
        }

        port1 += Integer.valueOf(port,16);
        if(port1.contains("443")) {
            addresses.add(ip.substring(0,ip.length()-1) + " - Port " + port1 + " (HTTPS, TCPv6)");
        } else {
            addresses.add(ip.substring(0,ip.length()-1) + " - Port " + port1 + " (TCPv6)");
        }
    }

    public void getAddressesUDP(ArrayList<String>arrayList, int uid) {

        for(int i=1;i<arrayList.size();i++) {
            if(arrayList.get(i).contains(" "+Integer.toString(uid)+" ")) {
                String hex = arrayList.get(i).substring(22,30);
                String port = arrayList.get(i).substring(31,35);
                convertHexUDP(hex,port);
            }
        }
    }

    public void convertHexUDP(String hex, String port) {
        String ip = "";
        String port1 = "";
        String four = hex.substring(0,2);
        String three = hex.substring(2,4);
        String two = hex.substring(4,6);
        String one = hex.substring(6,8);
        String flip = one+two+three+four;

        for(int i = 0; i < flip.length(); i = i + 2) {
            ip = ip + Integer.valueOf(flip.substring(i, i+2), 16) + ".";
        }

        port1 += Integer.valueOf(port,16);
        addresses.add(ip.substring(0,ip.length()-1) + " - Port " + port1 + " (UDP)");
    }

}
