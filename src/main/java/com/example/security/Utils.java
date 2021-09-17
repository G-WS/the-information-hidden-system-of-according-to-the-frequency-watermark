package com.example.security;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class Utils {
    /*用途
     * 1.获取当前应用的名称：getAppName
     * 2.获取当前应用的版本号：getVersionCode
     * 3.获取当前应用的版本名称：getVersionName
     */

        /*
         * 获取当前应用的名称
         */
        public static String getAppName(Context context) {
            //获取 PackageManager
            PackageManager pm = context.getPackageManager();
            try {
                //通过PackageManager这个Api可以拿到应用的一些信息
                //packgeName：包名   flag：获取额外信息的标识
                PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
                int labelRes = packageInfo.applicationInfo.labelRes;
                return context.getResources().getString(labelRes);

            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        }
        /*
         * 获取当前应用的版本号
         */
        public static int getVersionCode(Context context) {
            //获取 PackageManager
            PackageManager pm = context.getPackageManager();
            //通过PackageManager这个Api可以拿到应用的一些信息
            //packgeName：包名   flag：获取额外信息的标识
            try {
                PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
                //拿到版本号
                return packageInfo.versionCode;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            return -1;
        }

        /*
         * 获取当前应用的版本名称
         */
        public static String getVersionName(Context context) {
            PackageManager pm= context.getPackageManager();
            try {
                PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
                //拿到版本名称
                return packageInfo.versionName;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        }



}
