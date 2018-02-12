package com.ss174h.amsa.MonitorBehaviour;

import android.util.Log;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import static com.ss174h.amsa.MonitorBehaviour.TLType.tcp;
import static com.ss174h.amsa.MonitorBehaviour.TLType.tcp6;

/**
 * Created by user1 on 2/12/18.
 */



public class RemoteAddress {
    private static final char[] hexCode = "0123456789ABCDEF".toCharArray();
    private byte[] hexInfo;
    public byte[] remoteAddHex;
    public InetAddress remoteAdd;
    public int remotePort;
    private TLType type;

    public RemoteAddress(ByteBuffer bb, TLType type){
        //this.hexInfo = hexInfo;
        hexInfo = new byte[bb.remaining()];
        bb.get(hexInfo);

        this.type = type;
        if (type == TLType.tcp || type == TLType.udp) {
            initIP4(bb);
        } else {
            initIP6(bb);
        }
        //Init InetAddresses
        try {
            if (type == TLType.tcp ||type == TLType.udp){
                remoteAdd = InetAddress.getByName(
                        hexToIp4(printHexBinary(remoteAddHex)));
            } else {
                remoteAdd = InetAddress.getByName(
                        hexToIp6(printHexBinary(remoteAddHex)));
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }

    //Convert byte[] to HexString
    public String printHexBinary(byte[] data) {
        StringBuilder r = new StringBuilder(data.length * 2);
        for (byte b : data) {
            r.append(hexCode[(b >> 4) & 0xF]);
            r.append(hexCode[(b & 0xF)]);
        }
        return r.toString();
    }

    public String hexToIp6(String hexaIP){
        StringBuilder result = new StringBuilder();
        for(int i=0;i<hexaIP.length();i=i+8){
            String word = hexaIP.substring(i,i+8);
            for (int j = word.length() - 1; j >= 0; j = j - 2) {
                result.append(word.substring(j - 1, j + 1));
                result.append((j==5)?":":"");//in the middle
            }
            result.append(":");
        }
        return result.substring(0,result.length()-1).toString();
    }

    public String hexToIp4 (String hexa) {
        StringBuilder result = new StringBuilder();
        //reverse Little to Big
        for (int i = hexa.length() - 1; i >= 0; i = i - 2) {
            String wtf = hexa.substring(i - 1, i + 1);
            result.append(Integer.parseInt(wtf, 16));
            result.append(".");
        }
        //remove last ".";
        return result.substring(0,result.length()-1).toString();
    }

    public byte[] getHexInfo() {
        return hexInfo;
    }

    public InetAddress getRemoteAdd() {
        return remoteAdd;
    }

    public int getRemotePort() {
        return remotePort;
    }

    public TLType getType() {
        return type;
    }

    //Fill report with Ip4 - tcp/udp connection data from bytebuffer readin
    private void initIP4(ByteBuffer bb) {
        bb.position(0);
        byte[] b = new byte[2];
        remoteAddHex = new byte[4];
        bb.get(remoteAddHex);
        bb.get(b);
        remotePort = twoBytesToInt(b);
    }

    //Convert two bytes to a Java int
    public int twoBytesToInt(byte[] b){
        ByteBuffer bb = ByteBuffer.allocate(4);
        bb.position(2);
        bb.put(b);
        bb.position(0);
        return bb.getInt();
    }
    //Fill report with Ip6 - tcp/udp connection data from bytebuffer readin
    private void initIP6(ByteBuffer bb) {
        bb.position(0);
        byte[] b = new byte[2];
        remoteAddHex = new byte[16];
        bb.get(remoteAddHex);
        bb.get(b);
        remotePort = twoBytesToInt(b);
    }

    /*
    //======================================
    public String convertHexTCP(String hex) {
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

        return ip.substring(0,ip.length()-1);

    }
    */
}


