package com.richjavalabs.codemeanings;

import android.app.Application;
import android.content.res.Configuration;

/**
 * A class for accessing app engine application name.
 * Created by richard_lovell on 1/22/2015.
 */
public class CMApplication extends Application {

    public static final String APP_ENGINE_APP_NAME = "global-matrix-825";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}