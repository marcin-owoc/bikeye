package com.skolton.bikeye.fragments;


import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import com.skolton.bikeye.R;
import com.skolton.bikeye.bluetooth.BluetoothManager;

import java.util.ArrayList;

/**
 * Created by x220 on 2014-07-29.
 */
public class NearByDevicesFragment extends Fragment {

    BluetoothManager bluetoothManager;
    ArrayAdapter<BluetoothDevice> nearbyDevicesAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.nearby_devs_list, container, false);

        if(bluetoothManager == null)
            bluetoothManager = new BluetoothManager(getActivity());
        ListView lv = (ListView) rootView.findViewById(R.id.lvNearbyDevices);
        //nearbyDevicesAdapter = new ArrayAdapter<BluetoothDevice>(getActivity(),android.R.layout.simple_list_item_1,bluetoothManager.getAvailableNearbyDevices());
        lv.setAdapter(bluetoothManager.getAvailableNearbyDevicesAdapter());

        return rootView;
    }
}
