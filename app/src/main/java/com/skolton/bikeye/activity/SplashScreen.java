package com.skolton.bikeye.activity;

import com.skolton.bikeye.BikeyeApplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.skobbler.ngx.SKMaps;
import com.skobbler.ngx.SKMapsInitSettings;
import com.skobbler.ngx.SKPrepareMapTextureListener;
import com.skobbler.ngx.SKPrepareMapTextureThread;
import com.skobbler.ngx.map.SKMapViewStyle;
import com.skobbler.ngx.navigation.SKAdvisorSettings;
import com.skobbler.ngx.util.SKLogging;
import com.skobbler.ngx.versioning.SKMapUpdateListener;
import com.skobbler.ngx.versioning.SKVersioningManager;

import java.io.File;
import java.io.IOException;

import com.skolton.bikeye.R;
import com.skolton.bikeye.util.DemoUtils;

public class SplashScreen extends Activity implements SKPrepareMapTextureListener, SKMapUpdateListener {

    private static final String API_KEY = "e393132a736b8dc385849587fdc2550911c20b96fee6f8bc2abec96f420748ae";


    /**
     * Path to the MapResources directory
     */
    private String mapResourcesDirPath = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);


    }


    @Override
    protected void onResume() {
        super.onResume();



        SKLogging.enableLogs(true);
        File externalDir = getExternalFilesDir(null);

        // determine path where map resources should be copied on the device
        if (externalDir != null) {
            mapResourcesDirPath = externalDir + "/" + "SKMaps/";
        } else {
            mapResourcesDirPath = getFilesDir() + "/" + "SKMaps/";
        }
        ((BikeyeApplication) getApplication()).setMapResourcesDirPath(mapResourcesDirPath);

        final TextView loadingMessage = (TextView) findViewById(R.id.loading_message);
        if (!new File(mapResourcesDirPath).exists()) {

            loadingMessage.setText("initializing library...");
            // if map resources are not already present copy them to
            // mapResourcesDirPath in the following thread
            new SKPrepareMapTextureThread(this, mapResourcesDirPath, "SKMaps.zip", this).start();
            // copy some other resource needed
            copyOtherResources();
            prepareMapCreatorFile();
        } else {
            // map resources have already been copied - start the map activity




//            Toast.makeText(this, "Map resources copied in a previous run", Toast.LENGTH_SHORT).show();

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {

                    loadingMessage.setText("map resources loaded");
                    prepareMapCreatorFile();
                    initializeLibrary();
                    finish();
                    startActivity(new Intent(SplashScreen.this, BikeyeActivity.class));


                }
            }, 500);


        }

    }

    @Override
    public void onMapTexturesPrepared(boolean prepared) {

        initializeLibrary();
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(SplashScreen.this, "map resources were copied", Toast.LENGTH_SHORT).show();


                finish();
                startActivity(new Intent(SplashScreen.this, BikeyeActivity.class));
            }
        });
    }

    /**
     * Copy some additional resources from assets
     */
    private void copyOtherResources() {
        new Thread() {

            public void run() {
                try {
                    String tracksPath = mapResourcesDirPath + "GPXTracks";
                    File tracksDir = new File(tracksPath);
                    if (!tracksDir.exists()) {
                        tracksDir.mkdirs();
                    }
                    DemoUtils.copyAssetsToFolder(getAssets(), "GPXTracks", mapResourcesDirPath + "GPXTracks");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            };
        }.start();
    }

    /**
     * Copies the map creator file from assets to a storage.
     */
    private void prepareMapCreatorFile() {
        final BikeyeApplication app = (BikeyeApplication) getApplication();
        final Thread prepareGPXFileThread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);

                    final String mapCreatorFolderPath = mapResourcesDirPath + "MapCreator";
                    final File mapCreatorFolder = new File(mapCreatorFolderPath);
                    // create the folder where you want to copy the json file
                    if (!mapCreatorFolder.exists()) {
                        mapCreatorFolder.mkdirs();
                    }
                    app.setMapCreatorFilePath(mapCreatorFolderPath + "/mapcreatorFile.json");
                    DemoUtils.copyAsset(getAssets(), "MapCreator", mapCreatorFolderPath, "mapcreatorFile.json");
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        prepareGPXFileThread.start();
    }

    /**
     * Initializes the SKMaps framework
     */
    private void initializeLibrary() {
        final BikeyeApplication app = (BikeyeApplication) getApplication();
        // get object holding map initialization settings
        SKMapsInitSettings initMapSettings = new SKMapsInitSettings();
        // set path to map resources and initial map style
        initMapSettings.setMapResourcesPaths(app.getMapResourcesDirPath(),
                new SKMapViewStyle(app.getMapResourcesDirPath() + "daystyle/", "daystyle.json"));

        final SKAdvisorSettings advisorSettings = initMapSettings.getAdvisorSettings();
        advisorSettings.setLanguage("en");
        advisorSettings.setAdvisorVoice("en");
        advisorSettings.setPlayInitialAdvice(true);
        advisorSettings.setPlayAfterTurnInformalAdvice(true);
        advisorSettings.setPlayInitialVoiceNoRouteAdvice(true);
        initMapSettings.setAdvisorSettings(advisorSettings);

        // EXAMPLE OF ADDING PREINSTALLED MAPS
        // initMapSettings.setPreinstalledMapsPath(app.getMapResourcesDirPath()
        // + "/PreinstalledMaps");
        // initMapSettings.setConnectivityMode(SKMaps.CONNECTIVITY_MODE_OFFLINE);

        // Example of setting light maps
        // initMapSettings.setMapDetailLevel(SKMapsInitSettings.SK_MAP_DETAIL_LIGHT);
        // initialize map using the settings object
        SKVersioningManager.getInstance().setMapUpdateListener(this);
        SKMaps.getInstance().initializeSKMaps(this, initMapSettings, API_KEY);
    }

    @Override
    public void onMapVersionSet() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onNewVersionDetected(int newVersion) {
        // TODO Auto-generated method stub
        Log.e("", "new version " + newVersion);
    }

    @Override
    public void onNoNewVersionDetected() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onVersionFileDownloadTimeout() {
        // TODO Auto-generated method stub

    }



}
