package com.ss174h.amsa.MonitorBehaviour;

import android.net.TrafficStats;

public class TrafficStatsHelper {


    public static long getAllRxBytes() {
        return TrafficStats.getTotalRxBytes();
    }

    public static long getAllTxBytes() {
        return TrafficStats.getTotalTxBytes();
    }

    public static long getPackageRxBytes(int uid) {
        return TrafficStats.getUidRxBytes(uid);
    }

    public static long getPackageTxBytes(int uid) {
        return TrafficStats.getUidTxBytes(uid);
    }
}
