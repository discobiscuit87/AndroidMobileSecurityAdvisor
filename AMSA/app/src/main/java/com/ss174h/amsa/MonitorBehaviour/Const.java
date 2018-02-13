package com.ss174h.amsa.MonitorBehaviour;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Storing constant values for various usages within the app
 */

public interface Const {

    //App constants
    boolean IS_DEBUG = false;
    String LOG_TAG = "NetMonitor";
    String FILE_IF_LIST = "iflist";

    //SSL LABS CONSTANTS
    String SSLLABS_URL = "https://www.ssllabs.com/ssltest/analyze.html?d=";

    //Detector constants
    long REPORT_TTL_DEFAULT = 10000;
    Integer[] TLS_PORT_VALUES = new Integer[] { 993, 443, 995, 995, 614, 465, 587, 22 };
    Set<Integer> TLS_PORTS = new HashSet<>(Arrays.asList(TLS_PORT_VALUES));
    Integer[] INCONCLUSIVE_PORT_VALUES = new Integer[] { 25, 110, 143 };
    Set<Integer> INCONCUSIVE_PORTS = new HashSet<>(Arrays.asList(INCONCLUSIVE_PORT_VALUES));
    Integer[] UNSECURE_PORT_VALUES = new Integer[] { 21, 23, 80, 109, 137, 138 ,139, 161, 992 };
    Set<Integer> UNSECURE_PORTS = new HashSet<>(Arrays.asList(UNSECURE_PORT_VALUES));

    //String Builder Constants
    String STATUS_TLS = "Encrypted";
    String STATUS_UNSECURE = "Unencrypted";
    String STATUS_INCONCLUSIVE = "Inconclusive";
    String STATUS_UNKNOWN = "Unknown";

    //SharedPrefs identifiers
    String REPORT_TTL = "REPORT_TTL";
    String IS_DETAIL_MODE = "IS_DETAIL_MODE";
    String IS_FIRST_START = "IS_FIRST_START";
    String IS_LOG = "IS_LOG";
    String IS_CERTVAL = "IS_CERTVAL";
    String PREF_NAME = "PREF_NAME";

}
