package com.ss174h.amsa.EnvCondition;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.widget.Toast;
import android.content.Intent;

/**
 * Created by jianwen on 9/1/18.
 */

public class SystemUpdateClass extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent){
        if (intent.getAction().equals("android.settings.SYSTEM_UPDATE_SETTINGS")){
            Toast.makeText(context,
                    "Yup! Received a system update broadcast",
                    Toast.LENGTH_SHORT).show();
        }

        else
        {
            Toast.makeText(context,
                    "System is already the latest version",
                    Toast.LENGTH_SHORT).show();
        }


    }

}
