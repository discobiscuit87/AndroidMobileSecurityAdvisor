package com.ss174h.amsa.MonitorBehaviour;

import java.io.UnsupportedEncodingException;
import java.util.Comparator;

/**
 * Created by user1 on 2/12/18.
 */

public class HexIPComp implements Comparator<RemoteAddress> {
    public int compare(RemoteAddress r1, RemoteAddress r2){
        byte[] b1 = (byte[])r1.getHexInfo();
        byte[] b2 = (byte[])r2.getHexInfo();
        String s1 = null;
        String s2 = null;
            s1 = new String(b1);
            s2 = new String(b2);

        return s1.compareTo(s2);
    }
}
