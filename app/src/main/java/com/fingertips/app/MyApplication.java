package com.fingertips.app;

import android.app.Application;


import com.fingertips.helper.XClass;

import net.ralphpina.permissionsmanager.PermissionsManager;



/**
 * Created by dinesh3836 on 29/01/17.
 */

public class MyApplication extends Application {

    private static MyApplication mInstance;
    private XClass obj;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        obj = new XClass(this);
        PermissionsManager.init(this);
    }


    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public XClass getSession() {
        return obj;
    }
}