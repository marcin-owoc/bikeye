package com.skolton.bikeye.bluetooth;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;


import com.skolton.bikeye.R;
import com.skolton.bikeye.adapter.BluetoothDevicesArrayAdapter;
import com.skolton.bikeye.util.Constants;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by x220 on 2014-07-16.
 */
public class BluetoothManager {
    private BluetoothAdapter BA;
    private Activity mActivity;
    private static BroadcastReceiver mReceiver;
    private static ArrayList<BluetoothDevice> nearbyDevices;
    private static ArrayAdapter<BluetoothDevice> nearbyDevicesAdapter;
    private Handler mHandler;
    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 10000;
    private boolean mScanning = false;
    ProgressDialog ringProgressDialog ;

    public BluetoothManager(Activity activity) {
        BA = BluetoothAdapter.getDefaultAdapter();
        mActivity = activity;
        if(nearbyDevices == null)
            nearbyDevices = new ArrayList<BluetoothDevice>();
        if(nearbyDevicesAdapter == null)
            nearbyDevicesAdapter = new BluetoothDevicesArrayAdapter(mActivity, R.layout.nearby_devices_listitem,nearbyDevices);
        mHandler = new Handler();
        turnOn();
       // showNearbyDialog();
        //getPairedDevices();
    }

    public void setVisible(){
        Intent getVisible = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        mActivity.startActivityForResult(getVisible, 0);
    }
    public void turnOff(){
        BA.disable();
        Toast.makeText(mActivity, "Turned off", Toast.LENGTH_LONG).show();
    }
    public void turnOn(){
        if (!BA.isEnabled()) {
            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            mActivity.startActivityForResult(turnOn, 0);
            Toast.makeText(mActivity,"Turned on" ,Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(mActivity,"Already on",Toast.LENGTH_LONG).show();
        }
    }

    public void startDiscovery() {
        BA.startDiscovery();
    }
    public void stopDiscovery() {
        BA.cancelDiscovery();
    }
    public Set<BluetoothDevice> getPairedDevices(){
        Set<BluetoothDevice> availableDevices = BA.getBondedDevices();
        ArrayList list = new ArrayList();
        for(BluetoothDevice bt : availableDevices) {
            Log.d(Constants.APP_TAG,"Paired device " + bt.getName() + " : " + bt.getAddress());
            list.add(bt.getName());
        }

        return availableDevices;
    }

    public ArrayList<BluetoothDevice> getAvailableNearbyDevices() {
        return nearbyDevices;
    }
    public ArrayAdapter<BluetoothDevice> getAvailableNearbyDevicesAdapter() { return nearbyDevicesAdapter;}

    public static BroadcastReceiver getBroadcastReceiver() {

        if(mReceiver == null)
            mReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();
                    Log.d(Constants.APP_TAG, "onReceive " + action);
                    // When discovery finds a device
                    if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                        Log.d(Constants.APP_TAG, "Found " + device.getName() + " : "+device.getAddress());
                        if(!nearbyDevices.contains(device)) {
                            nearbyDevices.add(device);
                            nearbyDevicesAdapter.notifyDataSetChanged();
                        }
                    }
                }
            };

        return mReceiver;
    }

    /*private void showNearbyDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mActivity);
        LayoutInflater inflater = mActivity.getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.nearby_devs_list, null);
        alertDialog.setView(convertView);
        alertDialog.setTitle("Nearby Devices");
        ListView lv = (ListView) convertView.findViewById(R.id.lvNearbyDevices);
        nearbyDevicesAdapter = new ArrayAdapter<BluetoothDevice>(mActivity,android.R.layout.simple_list_item_1,getAvailableNearbyDevices());
        lv.setAdapter(nearbyDevicesAdapter);
        alertDialog.show();
    }*/

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            Log.d(Constants.APP_TAG,"Start scanning BLE");
            mHandler.postDelayed(new Runnable() {
                @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
                @Override
                public void run() {
                    mScanning = false;
                    ringProgressDialog.dismiss();
                    BA.stopLeScan(mLeScanCallback);
                }
            }, SCAN_PERIOD);
            ringProgressDialog= ProgressDialog.show(mActivity, "Please wait ...", "Searching Bluetooth Devices ...", true);
            mScanning = true;
            BA.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            BA.stopLeScan(mLeScanCallback);
        }

    }
    public void scanDevices(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            Log.d(Constants.APP_TAG,"Start scanning Bluetooth Devices");
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    ringProgressDialog.dismiss();
                    BA.cancelDiscovery();
                }
            }, SCAN_PERIOD);

            ringProgressDialog= ProgressDialog.show(mActivity, "Searching Bluetooth Devices", "Please wait ...", true);
            mScanning = true;
            BA.startDiscovery();
        } else {
            mScanning = false;
            BA.cancelDiscovery();
        }

    }
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {

                @Override
                public void onLeScan(final BluetoothDevice device, int rssi,
                                     byte[] scanRecord) {
                    Log.d(Constants.APP_TAG,"Found BLE " + device.getName());
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            nearbyDevices.add(device);
                            nearbyDevicesAdapter.notifyDataSetChanged();
                        }
                    });
                }
            };



}
