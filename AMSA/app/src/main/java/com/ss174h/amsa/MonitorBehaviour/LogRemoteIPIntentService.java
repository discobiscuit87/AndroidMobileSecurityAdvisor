package com.ss174h.amsa.MonitorBehaviour;

import android.app.IntentService;
import android.content.Intent;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import com.jaredrummler.android.processes.models.AndroidAppProcess;
import static java.lang.Thread.sleep;

public class LogRemoteIPIntentService extends IntentService {

    private ArrayList<RemoteAddress> addresses;
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
                String filepathUDP6 = "/proc/net/udp6";
                addresses = new ArrayList<>();

                AndroidAppProcess process = ProcessListAdapter.processes.get(i);


                try {
                    addresses.addAll(getStringFromFile(filepathTCP, process.uid, TLType.tcp));
                    addresses.addAll(getStringFromFile(filepathTCP6, process.uid, TLType.tcp6));
                    addresses.addAll(getStringFromFile(filepathUDP, process.uid, TLType.udp));
                    addresses.addAll(getStringFromFile(filepathUDP6, process.uid, TLType.udp6));

                    for (RemoteAddress address : addresses) {

                        processesRemoteIPs.get(i).addAddress(address);
                    }
                } catch (Exception e) {

                }
            }


            try {
                sleep(1000);
            }

            catch(InterruptedException ie)
            {

            }

        }while(true);

    }

    public String convertStreamToString(InputStream is) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length = 0;
        while ((length = is.read(buffer)) != -1) {
            baos.write(buffer, 0, length);
        }
        return baos.toString("UTF-8");
    }

    public List<RemoteAddress> getStringFromFile (String filePath, int uid, TLType type) throws Exception {
        File fl = new File(filePath);
        FileInputStream fin = new FileInputStream(fl);
        LinkedList<RemoteAddress> reportList = new LinkedList<>();
        RemoteAddress r;
        String splitTabs[];
        String[] splitLines=convertStreamToString(fin).split("\\n");
        for (int i = 1; i < splitLines.length; i++) {
            splitLines[i] = splitLines[i].trim();

            while (splitLines[i].contains("  ")) {
                splitLines[i] = splitLines[i].replace("  ", " ");
            }
            splitTabs = splitLines[i].split("\\s");
            if(Integer.parseInt(splitTabs[7]) == uid) {
                if (type == TLType.tcp || type == TLType.udp) {
                    //Init IPv4 values
                    r = initReport4(splitTabs, type);
                } else {
                    //Init IPv6 values
                    r = initReport6(splitTabs, type);
                }
                reportList.add(r);
            }
        }
        //Make sure you close all streams.
        fin.close();

        return reportList;
    }
    //Init parsed data to IPv4 connection report
    private RemoteAddress initReport4(String[] splitTabs, TLType type){
        int pos;
        pos = 0;
        //Allocating buffer for 4 Bytes add and 2 bytes port each + 4 bytes UID
        ByteBuffer bb = ByteBuffer.allocate(17);
        bb.position(0);

        //remote address
        pos = 0;
        String hexStr = splitTabs[2].substring(pos, pos + 8);
        bb.put(hexStringToByteArray(hexStr));

        //local port
        pos = splitTabs[2].indexOf(":");
        hexStr = splitTabs[2].substring(pos +1, pos + 5);
        bb.put(hexStringToByteArray(hexStr));


        return new RemoteAddress(bb, type);
    }

    //Convert HexString to byte[]
    public byte[] hexStringToByteArray(String s) {
        byte[] b = new byte[s.length() / 2];
        for (int i = 0; i < b.length; i++) {
            int index = i * 2;
            int v = Integer.parseInt(s.substring(index, index + 2), 16);
            b[i] = (byte) v;
        }
        return b;
    }

    //Init parsed data to IPv6 connection report
    private RemoteAddress initReport6(String[] splitTabs, TLType type){
        int pos;
        //Allocating buffer for 16 Bytes add and 2 bytes port each + 4 bytes UID
        ByteBuffer bb = ByteBuffer.allocate(41);
        bb.position(0);

        //remote address
        pos = 0;
        String hexStr = splitTabs[2].substring(pos, pos + 32);
        bb.put(hexStringToByteArray(hexStr));

        //local port
        pos = splitTabs[2].indexOf(":");
        hexStr = splitTabs[2].substring(pos +1, pos + 5);
        bb.put(hexStringToByteArray(hexStr));

        return new RemoteAddress(bb, type);
    }

}
