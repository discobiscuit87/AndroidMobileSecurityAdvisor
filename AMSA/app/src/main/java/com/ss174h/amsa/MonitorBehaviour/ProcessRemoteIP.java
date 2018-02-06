package com.ss174h.amsa.MonitorBehaviour;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by jianwen on 6/2/18.
 */

public class ProcessRemoteIP {

    private int processID;

    private Set<String> remoteAddresses;

    public ProcessRemoteIP(int processID)
    {
        this.processID  =processID;
        remoteAddresses = new TreeSet<String>();
    }

    public void addAddress(String address)
    {
        remoteAddresses.add(address);
    }

    public Set<String> getRemoteAddresses()
    {
        return remoteAddresses;
    }
}
