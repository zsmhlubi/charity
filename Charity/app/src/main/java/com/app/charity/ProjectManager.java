package com.app.charity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

public class ProjectManager {

    public static String prevActivity (Intent intent){
        Bundle extras = intent.getExtras();
        if (extras!=null) {
            return extras.getString("previousActivity");
        }
        return "";
    }

}
