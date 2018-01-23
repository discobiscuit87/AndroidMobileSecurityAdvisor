package com.ss174h.amsa.MonitorBehaviour;

/**
 * Created by Air on 10/1/18.
 */

public class Process_ID {
    private String PID;
    private String processName;

    public Process_ID(String PID, String processName) {
        this.PID = PID;
        this.processName = processName;
    }

    public String getProcessName(String PID) {
        return processName;
    }
}
