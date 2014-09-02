package com.skolton.bikeye.bluetooth;

/**
 * Created by x220 on 2014-07-14.
 */
public interface NavigationAPI {

    public void onNavigationChanged(Object direction);
    public void onDistanceToDestination(int distance);
    public void onDistanceReached();

}
