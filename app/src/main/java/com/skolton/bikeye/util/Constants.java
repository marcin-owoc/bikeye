package com.skolton.bikeye.util;

import java.util.UUID;

/**
 * Created by x220 on 2014-07-15.
 */
public class Constants {
    public static String APP_TAG = "Bike+";
    public static String NAME = "Bike+";
    public static UUID MY_UUID = UUID.randomUUID();

    //NAVIGATION
    public static String FORWARD ="NAV100";
    public static String TURN_LEFT ="NAV101";
    public static String TURN_RIGHT ="NAV102";
    public static String TURN_SLIGHTLY_LEFT ="NAV103";
    public static String TURN_SLIGHTLY_RIGHT ="NAV104";
    public static String U_TURN ="NAV105";
    public static String DISTANCE_TO_DESTINATION ="NAV106";
    public static String REROUTING ="NAV150";
    public static String WAITING_FOR_GPS ="NAV180";
    public static String DISTANCE_TO_FINAL_DESTINATION ="NAV190";
    public static String DISTANCE_REACHED ="NAV199";
    //PHONE CALLS
    public static String INCOMING_PHONE_CALL ="NTF400";
    public static String MESSAGE_RECEIVED ="NTF410";
    public static String NOTIFICATION_RECEIVED ="NTF420";
    public static String INCOMING_PHONE_CALL_END ="NTF405";
    //BIKE+ CONTROLLER
    public static String POWER_BUTTON ="BTN500";
    public static String LEFT_BUTTON_CLICKED ="BTN510";
    public static String RIGHT_BUTTON_CLICKED ="BTN520";
    public static String UP_BUTTON_CLICKED ="BTN530";
    public static String DOWN_BUTTON_CLICKED ="BTN540";
    public static String CENTER_BUTTON_CLICKED ="BTN550";
    //BATTERY NOTIFICATIONS
    public static String BATTERY_FULL ="BAT200";
    public static String BATTERY_LOW_30 ="BAT201";
    public static String BATTERY_CRITICAL_10 ="BAT202";
    public static String BATTERY_STATUS ="BAT210";
    public static String BATTERY_STATUS_RESPONSE ="BAT211";


}
