package com.skolton.bikeye.activity;

import android.app.ActionBar;

import android.app.FragmentTransaction;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.skolton.bikeye.R;
import com.skolton.bikeye.bluetooth.BluetoothManager;
import com.skolton.bikeye.fragments.BatteryNotificationFragment;
import com.skolton.bikeye.fragments.NavigationFragment;
import com.skolton.bikeye.fragments.NearByDevicesFragment;
import com.skolton.bikeye.fragments.PhoneCallsFragment;
import com.skolton.bikeye.util.Constants;


public class PrototypeActivity extends FragmentActivity implements ActionBar.TabListener{

   SectionsPagerAdapter mSectionsPagerAdapter;
   final int NUMBER_OF_PAGES=4;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    BluetoothManager bleutoothManager;
    boolean BLEScanning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prototype);

        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);


        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        mPager.setOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        // When swiping between pages, select the
                        // corresponding tab.
                        getActionBar().setSelectedNavigationItem(position);
                    }
                });


        // Add 3 tabs, specifying the tab's text and TabListener
        for (int i = 0; i < NUMBER_OF_PAGES; i++) {
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mPagerAdapter.getPageTitle(i)).setTabListener(this));
        }

        Log.d(Constants.APP_TAG, " Registering BroadcastReceiver");
        bleutoothManager = new BluetoothManager(this);
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(BluetoothManager.getBroadcastReceiver(), filter);
        startScan();

    }

    @Override
    protected void onStop() {
        super.onStop();
        bleutoothManager.stopDiscovery();
        this.unregisterReceiver(BluetoothManager.getBroadcastReceiver());
    }
    public void startScan(){
        if(!BLEScanning)
            bleutoothManager.scanDevices(true);
        else
            bleutoothManager.scanLeDevice(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        /*int id = item.getItemId();
        if (id == R.id.action_bluetooth) {
            Intent intent = new Intent(this,MapActivity.class);
            startActivity(intent);
            BLEScanning = false;
            return true;
        } else if(id == R.id.action_ble){
            BLEScanning = true;
            return true;
        } else if(id == R.id.action_scan){
            startScan();
            return true;
        }*/
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return new NearByDevicesFragment();
                case 1:
                    return new NavigationFragment();
                case 2:
                    return new PhoneCallsFragment();
                case 3:
                    return new BatteryNotificationFragment();
                default:
                    return new BatteryNotificationFragment();
            }
        }

        @Override
        public int getCount() {

            return NUMBER_OF_PAGES;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {
                case 0:
                    return "Nearby BL devices";
                case 1:
                    return "Navigation";
                case 2:
                    return "Phone Calls";
                case 3:
                    return "BatteryStatus";
            }
            return null;
        }
    }


}
