package com.ss174h.amsa.MonitorBehaviour;

import android.util.Log;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by jianwen on 6/2/18.
 */

public class ProcessRemoteIP {

    private int processID;

    private Set<RemoteAddress> remoteAddresses;

    public ProcessRemoteIP(int processID)
    {
        this.processID  =processID;
        remoteAddresses = new TreeSet<RemoteAddress>(new HexIPComp());
    }

    public void addAddress(RemoteAddress address)
    {
        remoteAddresses.add(address);
        for(RemoteAddress r: remoteAddresses){
        }
    }

    public Set<RemoteAddress> getRemoteAddresses()
    {
        return remoteAddresses;
    }


}
