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
import com.skolton.bikeye.adapter.TripsArrayAdapter;

/**
 * Created by x220 on 2014-08-27.
 */
public class TripsFragment extends Fragment {

    TripsArrayAdapter tripsArrayAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_trips, container, false);

        ListView lv = (ListView) rootView.findViewById(R.id.lvMyTrips);
        tripsArrayAdapter = new TripsArrayAdapter(getActivity());
        tripsArrayAdapter.addSectionHeaderItem("April 1");
        for (int i = 1; i < 30; i++) {
            tripsArrayAdapter.addItem(i+ " km");
            if (i % 4 == 0) {
                tripsArrayAdapter.addSectionHeaderItem("April " + i);
            }
        }

        lv.setAdapter(tripsArrayAdapter);

        return rootView;
    }
}