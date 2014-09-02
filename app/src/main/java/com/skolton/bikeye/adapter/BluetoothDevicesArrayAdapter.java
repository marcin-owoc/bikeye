package com.skolton.bikeye.adapter;

/**
 * Created by x220 on 2014-07-29.
 */
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.skolton.bikeye.R;

import java.util.ArrayList;


public class BluetoothDevicesArrayAdapter extends ArrayAdapter<BluetoothDevice> {

    Context mContext;
    int layoutResourceId;
    private ArrayList<BluetoothDevice> data = null;

    public BluetoothDevicesArrayAdapter(Context mContext, int layoutResourceId, ArrayList<BluetoothDevice> data) {

        super(mContext, layoutResourceId, data);

        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        /*
         * The convertView argument is essentially a "ScrapView" as described is Lucas post
         * http://lucasr.org/2012/04/05/performance-tips-for-androids-listview/
         * It will have a non-null value when ListView is asking you recycle the row layout.
         * So, when convertView is not null, you should simply update its contents instead of inflating a new row layout.
         */
        if(convertView==null){
            // inflate the layout
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
        }

        // object item based on the position
        BluetoothDevice bluetoothDevice = data.get(position);

        // get the TextView and then set the text (item name) and tag (item ID) values
        TextView blName = (TextView) convertView.findViewById(R.id.tvDevicesName);
        TextView blAddress = (TextView) convertView.findViewById(R.id.tvDevicesAddress);
        blName.setText(bluetoothDevice.getName());
        blAddress.setText(bluetoothDevice.getAddress());

        return convertView;

    }

}