package com.ss174h.amsa.DetectNewAPK;

import android.os.FileObserver;
import android.util.Log;

/**
 * Created by jianwen on 16/1/18.
 */

public class MyFileObserver extends FileObserver {

    public String absolutePath;

    public MyFileObserver(String path) {
        super(path, FileObserver.ALL_EVENTS);
        absolutePath = path;
    }

    @Override
    public void onEvent(int event, String path) {
        if (path == null) {
            return;
        }
        //a new file or subdirectory was created under the monitored directory
        if ((FileObserver.CREATE & event)!=0) {
            //FileAccessLogStatic.accessLogMsg += absolutePath + "/" + path + " is createdn";
            Log.i("Downloaded", "a new file or subdirectory was created under the monitored directory");

            Log.i("path dsds", path);

            String filename = path.toLowerCase();

            if(filename.toLowerCase().contains("apk") || filename.toLowerCase().contains("APK")) {
                Log.i("DETECTED", "An apk file is being downloaded");

            }



        }
        //a file or directory was opened
        if ((FileObserver.OPEN & event)!=0) {
            //FileAccessLogStatic.accessLogMsg += path + " is openedn";
            Log.i("Downloaded", "a file or directory was opened");
        }
        //data was read from a file
        if ((FileObserver.ACCESS & event)!=0) {
            //FileAccessLogStatic.accessLogMsg += absolutePath + "/" + path + " is accessed/readn";
            Log.i("Downloaded", "data was read from a file");
        }
        //data was written to a file
        if ((FileObserver.MODIFY & event)!=0) {
            //FileAccessLogStatic.accessLogMsg += absolutePath + "/" + path + " is modifiedn";
            Log.i("Downloaded", "data was written to a file");
        }
        //someone has a file or directory open read-only, and closed it
        if ((FileObserver.CLOSE_NOWRITE & event)!=0) {
            //FileAccessLogStatic.accessLogMsg += path + " is closedn";
            Log.i("Downloaded", "someone has a file or directory open read-only, and closed it");
        }
        //someone has a file or directory open for writing, and closed it
        if ((FileObserver.CLOSE_WRITE & event)!=0) {
            //FileAccessLogStatic.accessLogMsg += absolutePath + "/" + path + " is written and closedn";
            Log.i("Downloaded", "someone has a file or directory open for writing, and closed it");
        }
        //[todo: consider combine this one with one below]
        //a file was deleted from the monitored directory
        if ((FileObserver.DELETE & event)!=0) {
            Log.i("Downloaded", "A file was deleted from the monitored directory");
            //for testing copy file
// FileUtils.copyFile(absolutePath + "/" + path);
            //FileAccessLogStatic.accessLogMsg += absolutePath + "/" + path + " is deletedn";
        }
        //the monitored file or directory was deleted, monitoring effectively stops
        if ((FileObserver.DELETE_SELF & event)!=0) {
            Log.i("Downloaded", "the monitored file or directory was deleted, monitoring effectively stops");
            //FileAccessLogStatic.accessLogMsg += absolutePath + "/" + " is deletedn";
        }
        //a file or subdirectory was moved from the monitored directory
        if ((FileObserver.MOVED_FROM & event)!=0) {
            Log.i("Downloaded", "a file or subdirectory was moved from the monitored directory");
            //FileAccessLogStatic.accessLogMsg += absolutePath + "/" + path + " is moved to somewhere " + "n";
        }
        //a file or subdirectory was moved to the monitored directory
        if ((FileObserver.MOVED_TO & event)!=0) {
            Log.i("Downloaded", "a file or subdirectory was moved to the monitored directory");
            //FileAccessLogStatic.accessLogMsg += "File is moved to " + absolutePath + "/" + path + "n";
        }
        //the monitored file or directory was moved; monitoring continues
        if ((FileObserver.MOVE_SELF & event)!=0) {
            Log.i("Downloaded", "the monitored file or directory was moved; monitoring continues");
            //FileAccessLogStatic.accessLogMsg += path + " is movedn";
        }
        //Metadata (permissions, owner, timestamp) was changed explicitly
        if ((FileObserver.ATTRIB & event)!=0) {
            Log.i("Downloaded", "Metadata (permissions, owner, timestamp) was changed explicitly");
            //FileAccessLogStatic.accessLogMsg += absolutePath + "/" + path + " is changed (permissions, owner, timestamp)n";
        }
    }
}
