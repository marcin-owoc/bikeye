package com.skolton.bikeye.bluetooth;

/**
 * Created by x220 on 2014-07-14.
 */
public interface PhoneStateNotifications {

    public void onIncomingCall(Object incomingCall);
    public void onRejectedCall(Object incomingCall);
    public void onMessageReceived(Object message);
    public void onNotificationReceived(Object notification);

}
