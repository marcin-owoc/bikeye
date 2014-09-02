package com.skolton.bikeye.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;;


import com.skolton.bikeye.R;
import com.skolton.bikeye.bluetooth.BluetoothManager;

import java.util.ArrayList;

/**
 * Created by x220 on 2014-07-15.
 */
public class NavigationFragment extends Fragment {
    BluetoothManager bleutoothManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(bleutoothManager == null)
            bleutoothManager = new BluetoothManager(getActivity());

        View rootView = inflater.inflate(R.layout.fragment_navigation, container, false);

        return rootView;
    }


}
