package com.ss174h.amsa.DetectNewAPK;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.FileObserver;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.ss174h.amsa.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

/**
 * Created by jianwen on 16/1/18.
 */

public class MyFileObserver extends FileObserver {

    public String absolutePath;
    private Context context;
    private ArrayList<String> score;


    public MyFileObserver(String path, Context con) {
        super(path, FileObserver.ALL_EVENTS);
        this.absolutePath = path;
        this.context = con;
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
            score = new ArrayList<>();

            if(filename.toLowerCase().contains("apk") && !filename.toLowerCase().contains("crdownload")) {
                Log.i("DETECTED", "An apk file is being downloaded");
                File apk = new File("/sdcard/Download/"+path);
                String[] out = new String[1];

                try {
                    MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
                    InputStream inputStream = new FileInputStream(apk);

                    byte[] buffer = new byte[8192];
                    int read;
                    try {
                        while ((read = inputStream.read(buffer)) > 0) {
                            messageDigest.update(buffer, 0, read);
                        }
                        byte[] bytes = messageDigest.digest();
                        BigInteger bigInteger = new BigInteger(1, bytes);
                        String output = bigInteger.toString(16);
                        out[0] = output;
                        new GetData().execute(out);
                    } catch (IOException e) {
                        throw new RuntimeException("Unable to process file for SHA-256");
                    } finally {
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            Log.e("Error: ", "Exception on closing input stream!");
                        }
                    }
                } catch (NoSuchAlgorithmException nsa) {
                    Log.e("Error: ", "No such algorithm!");
                } catch (FileNotFoundException fnf) {
                    Log.e("Error: ", "File not found!");
                }
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

    private class GetData extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... arg0) {
            String line;
            String positives;
            String responseCode;
            String api = "a4c4c0eac6eef58293c39ee8db121802f586ac520360bd5a24bec8e93cf157c8";

            String address = "https://www.virustotal.com/vtapi/v2/file/report?apikey=" + api + "&resource=" + arg0[0];
            try {
                URL url = new URL(address);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                InputStream in = new BufferedInputStream(connection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
                StringBuilder sb = new StringBuilder();
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                String response = sb.toString();

                JSONObject jsonObject = new JSONObject(response);
                responseCode = jsonObject.getString("response_code");
                if(!responseCode.equals("0")) {
                    positives = jsonObject.getString("positives");
                    score.add(positives);
                } else {
                    positives = "-1";
                    score.add(positives);
                }
            } catch (MalformedURLException e) {
                Log.e("Error", "Malformed URL Exception");
            } catch (IOException io) {
                Log.e("Error: ", "IO Exception");
            } catch (JSONException jso) {
                Log.e("Error: ", "JSON Exception");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "M_CH_ID");
            mBuilder.setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.logo)
                    .setPriority(Notification.PRIORITY_MAX)
                    .setContentTitle("Malware Check");

            if(score.get(0).equals("0")) {
                mBuilder.setTicker("Downloaded file is clear of malware!");
                mBuilder.setContentText("Downloaded file is clear of malware!");
            } else if(score.get(0).equals("-1")) {
                mBuilder.setTicker("Downloaded file is not in database, proceed with caution!");
                mBuilder.setContentText("Downloaded file is not in database, proceed with caution!");
            } else {
                mBuilder.setTicker("Downloaded file contains malware, do not install!");
                mBuilder.setContentText("Downloaded file contains malware, do not install!");
            }

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(1, mBuilder.build());
        }
    }
}
