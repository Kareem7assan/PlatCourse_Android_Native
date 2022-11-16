package com.rowaad.app.base.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.display.DisplayManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rowaad.app.base.BuildConfig;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.content.Context.DISPLAY_SERVICE;
import static android.content.Context.WIFI_SERVICE;


/**
 * Created by admin on 10/12/2015.
 */
public class UtilitySecurity {


    public static String DefaultPackage = "com.platCourse.platCourseAndroid";


    public static String[] appBlockPackageName = {

            "me.weishu.exp",
            "fi.veetipaananen.android.disableflagsecure",

            "com.applisto.appcloner",
            "com.applisto.appcloneR",
            "com.applisto.appcloner.premium",
            "com.cloneapp.parallelspace.dualspace.arm32",
            "com.cloneapp.parallelspace.dualspace",
            "com.polestar.super.clone",
            "multi.parallel.dualspace.cloner",

            "com.platcourse.platcourseapplicatioa",
            "com.platcourse.platcourseapplicatiob",
            "com.platcourse.platcourseapplicatioc",
            "com.platcourse.platcourseapplicatiod",
            "com.platcourse.platcourseapplicatioe",
            "com.platcourse.platcourseapplicatiof",
            "com.platcourse.platcourseapplicatiog",
            "com.platcourse.platcourseapplicatioh",
            "com.platcourse.platcourseapplicatioi",
            "com.platcourse.platcourseapplicatioj",
            "com.platcourse.platcourseapplicatiok",
            "com.platcourse.platcourseapplicatiol",
            "com.platcourse.platcourseapplicatiom",
           // "com.platcourse.platcourseapplication", // this commented because this package is app package
            "com.platcourse.platcourseapplicatioo",
            "com.platcourse.platcourseapplicatiop",
            "com.platcourse.platcourseapplicatioq",
            "com.platcourse.platcourseapplicatior",
            "com.platcourse.platcourseapplicatios",
            "com.platcourse.platcourseapplicatiot",
            "com.platcourse.platcourseapplicatiou",
            "com.platcourse.platcourseapplicatiov",
            "com.platcourse.platcourseapplicatiow",
            "com.platcourse.platcourseapplicatiox",
            "com.platcourse.platcourseapplicatioy",
            "com.platcourse.platcourseapplicatioz",

            "com.lbe.parallel.intl",
            "com.lbe.parallel.intl.arm64",
            "com.parallel.space.lite",
            "com.excelliance.multiaccounts",
            "com.parallel.space.pro",
            "com.app.hider.master.dual.app",
            "com.ludashi.dualspace",
            "com.cloner.android",
    };



    private static UtilitySecurity ourInstance = new UtilitySecurity();
    private UtilitySecurity() {}
    public static UtilitySecurity getInstance() {
        return ourInstance;
    }


    public static String getCurrentSsid(Context context) {


        String ssid = null;
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission") NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (networkInfo.isConnected()) {
            final WifiManager wifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);
            final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
            if (connectionInfo != null && !TextUtils.isEmpty(connectionInfo.getSSID())) {
                ssid = connectionInfo.getSSID();
            }
        }
        if(ssid == null)
        {
            return "known";
        }
        else
        {
            return ssid;
        }

    }
    public static String DeviceManufacturer()
    {
        return Build.MANUFACTURER;
    }
    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }
    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;

        StringBuilder phrase = new StringBuilder();
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase.append(Character.toUpperCase(c));
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase.append(c);
        }

        return phrase.toString();
    }


    // i add local block app
    // if any app founded installed block button to login user
    public static boolean doesPackageExist(Context context, String packageName) {
            PackageManager pm = context.getPackageManager();
            return isPackageInstalled(packageName, pm);
    }
    private static boolean isPackageInstalled(String packageName, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
    public static boolean checkHaveAppBlock(Context context) {

        for (int i = 0; i < appBlockPackageName.length; i++)
        {
            if(doesPackageExist(context, appBlockPackageName[i]))
            {
                return true;
            }
        }
        return false;
    }


    public static boolean checkDefaultPackage(Context context) {
        String PACKAGE_NAME1 = "com.platCourse.platCourseAndroid";/*context.getPackageName();*/
        String PACKAGE_NAME2 = "com.platcourse.platcourseapplication"/*BuildConfig.APPLICATION_ID*/;

        if(PACKAGE_NAME1.equals(DefaultPackage)&&PACKAGE_NAME2.equals(DefaultPackage))
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public static boolean verifyInstallerId(Context context) {
        // A list with valid installers package name
        List<String> validInstallers = new ArrayList<>(Arrays.asList("com.android.vending", "com.google.android.feedback"));
        // The package name of the app that has installed your app
        final String installer = context.getPackageManager().getInstallerPackageName(context.getPackageName());

        if(DeviceManufacturer().equals("HUAWEI"))
        {
            // when he send apk for HUAWEI phone app not stop
            return true;
        }
        else
        {
            // true if your app has been downloaded from Play Store
            return installer != null && validInstallers.contains(installer);
        }
    }


    public static boolean isRealDevice(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String networkOperator = tm.getNetworkOperatorName();
        if("Android".equals(networkOperator))
        {
            // Emulator
            return false;
        }
        else
        {
            // Device
            return true;
        }
    }




    public static boolean isEmulatorBuild() {
        Log.e("model",Build.FINGERPRINT+","+Build.BRAND+","+ Build.MANUFACTURER);

        return Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.HARDWARE.contains("goldfish")
                || Build.HARDWARE.contains("ranchu")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || Build.PRODUCT.contains("sdk_google")
                || Build.PRODUCT.contains("google_sdk")
                || Build.PRODUCT.contains("sdk")
                || Build.PRODUCT.contains("sdk_x86")
                || Build.PRODUCT.contains("vbox86p")
                || Build.PRODUCT.contains("emulator")
                || Build.PRODUCT.contains("simulator")
                || Build.MANUFACTURER.contains("CMDC")
                || Build.MANUFACTURER.contains("BlueStacks")
                || Build.MANUFACTURER.contains("com.koplay.launcher")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || "google_sdk".equals(Build.PRODUCT);
    }



    // function check all every thing is true
    //if any thing not clear return false if not return true
    public static boolean CheckIsRealPhoneMain(Context context) {
        // this is popular emulator that try to install app lick real device
        if(UtilitySecurity.getCurrentSsid(context).equals("\"BlueStacks\"")|| UtilitySecurity.getCurrentSsid(context).equals("BlueStacks")) {return false;}
        if(UtilitySecurity.getCurrentSsid(context).equals("\"WiredSSID\"")|| UtilitySecurity.getCurrentSsid(context).equals("WiredSSID")) {return false;}
        if(UtilitySecurity.getCurrentSsid(context).equals("\"AndroidWifi\"")|| UtilitySecurity.getCurrentSsid(context).equals("AndroidWifi")) {return false;}
        if(UtilitySecurity.getCurrentSsid(context).equals("\"tplink\"")|| UtilitySecurity.getCurrentSsid(context).equals("tplink")) {return false;}
        if(UtilitySecurity.DeviceManufacturer().equals("AndyOS, Inc.")) {return false;}
        if(UtilitySecurity.getDeviceName().equals("AndyOS, Inc. AndyWin")) {return false;}
        if(checkHaveAppBlock(context)){return false;}   // check if app have any block app
        if(haveManyDisplays(context)) {return false;}
        if(!isRealDevice(context)){return false;}       // check if this phone is real device
        if(isEmulatorBuild()){return false;}            // check if app is installed in emulator
        if(EmulatorDetector.isEmulator(context)){return false;}            // check if app is installed in emulator
        //if (!BuildConfig.DEBUG) if(!verifyInstallerId(context)) {return false;} // check if this app installed from google play or apk
        //if(checkDefaultPackage(context)){return false;} // check if package name is changed by any other app

       // if(EmulatorDetector.isEmulator(context)|| EmulatorDetector.checkPackageName(context)){return false;}

        return true;
    }

    @SuppressLint("NewApi")
     public static boolean haveManyDisplays(Context context) {
        @SuppressLint({"NewApi", "LocalSuppress"}) DisplayManager systemService = (DisplayManager) context.getSystemService(DISPLAY_SERVICE);

        return systemService.getDisplays().length > 1 ;
    }

    //------------------------------------------------------ save block app ----------------------------------------------------------------------------
    public void save_block_app(Context context, String key, ArrayList<itemData_get_all_block_app> itemData) {
        //mode privet for only this application can access to this shared preferences
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(itemData);
        editor.putString(key, json);
        editor.apply();
    }
//------------------------------------------------------ get block app ----------------------------------------------------------------------------
    public ArrayList<itemData_get_all_block_app> get_block_app(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(key, null);
        Type type = new TypeToken<ArrayList<itemData_get_all_block_app>>() {}.getType();
        ArrayList<itemData_get_all_block_app> arrayList = gson.fromJson(json, type);
        return arrayList;
    }
}
