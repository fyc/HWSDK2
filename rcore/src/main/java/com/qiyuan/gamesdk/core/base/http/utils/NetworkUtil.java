package com.qiyuan.gamesdk.core.base.http.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.List;

/**
 * @desc
 * @auth 方毅超
 * @time 2017/10/27 14:29
 */

public class NetworkUtil {
    private static final String TAG = "NetworkUtil";

    private NetworkUtil() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 判断网络是否连接
     *
     * @param context
     * @return
     */
    public static boolean isConnected(Context context) {

        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (null != connectivity) {

            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (null != info && info.isConnected()) {
                return true;
            }
        }
        return false;
    }

    /**
     * wifi是否打开
     */
    public static boolean isWifiEnabled(Context context) {
        ConnectivityManager mgrConn = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        TelephonyManager mgrTel = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return ((mgrConn.getActiveNetworkInfo() != null && mgrConn
                .getActiveNetworkInfo().getState() == NetworkInfo.State.CONNECTED) || mgrTel
                .getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS);
    }

    /**
     * 判断是否是wifi连接
     */
    public static boolean isWifi(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm == null)
            return false;
        return cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;

    }

    /**
     * Gps是否打开
     *
     * @param context
     * @return
     */
    public static boolean isGpsEnabled(Context context) {
        LocationManager locationManager = ((LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE));
        List<String> accessibleProviders = locationManager.getProviders(true);
        return accessibleProviders != null && accessibleProviders.size() > 0;
    }

    /**
     * 判断当前网络是否3G网络
     *
     * @param context
     * @return boolean
     */
    public static boolean is3G(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
            return true;
        }
        return false;
    }

    /**
     * 打开网络设置界面
     */
    public static void openSetting(Activity activity) {
        Intent intent = new Intent("/");
        ComponentName cm = new ComponentName("com.android.settings",
                "com.android.settings.WirelessSettings");
        intent.setComponent(cm);
        intent.setAction("android.intent.action.VIEW");
        activity.startActivityForResult(intent, 0);
    }

    /**
     * 获取连接网络类型(3G/4G/wifi,包含运营商信息)
     *
     * @param context
     * @return 返回连接网络类型(运营商3G / 4G / wifi)，如果网络未连接，返回"";
     */
    public static String getNetworkType(Context context) {
        String networkType = "";
        networkType = getNetworkTypeNoProvider(context);

        // 如果使用的数据流量，则添加运营商信息
        if (networkType.contains("G")) {
            networkType = getProvider(context) + networkType;
        }

        return networkType;
    }

    /**
     * 获取运营商
     *
     * @return 中国移动/中国联通/中国电信/未知
     */
    private static String getProvider(Context context) {
        String provider = "未知";
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String IMSI = telephonyManager.getSubscriberId();
            Log.d(TAG, "getProvider.IMSI:" + IMSI);
            if (IMSI == null) {
                if (TelephonyManager.SIM_STATE_READY == telephonyManager.getSimState()) {
                    String operator = telephonyManager.getSimOperator();
                    Log.d(TAG, "getProvider.operator:" + operator);
                    if (operator != null) {
                        if (operator.equals("46000") || operator.equals("46002") || operator.equals("46007")) {
                            provider = "中国移动";
                        } else if (operator.equals("46001")) {
                            provider = "中国联通";
                        } else if (operator.equals("46003")) {
                            provider = "中国电信";
                        }
                    }
                }
            } else {
                if (IMSI.startsWith("46000") || IMSI.startsWith("46002") || IMSI.startsWith("46007")) {
                    provider = "中国移动";
                } else if (IMSI.startsWith("46001")) {
                    provider = "中国联通";
                } else if (IMSI.startsWith("46003")) {
                    provider = "中国电信";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return provider;
    }

    /**
     * 获取连接网络类型(3G/4G/wifi,不包含运营商信息)
     *
     * @param context
     * @return 返回结果中，不包含运营商，返回连接网络类型(3G/4G/wifi)，如果网络未连接，返回"";
     */
    private static String getNetworkTypeNoProvider(Context context) {
        String strNetworkType = "";

        NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE))
                .getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                strNetworkType = "wifi";
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                String _strSubTypeName = networkInfo.getSubtypeName();
                Log.d(TAG, "Network getSubtypeName : " + _strSubTypeName);

                // TD-SCDMA networkType is 17
                int networkType = networkInfo.getSubtype();
                switch (networkType) {
                    case TelephonyManager.NETWORK_TYPE_GPRS: // 联通2G
                    case TelephonyManager.NETWORK_TYPE_EDGE: // 移动2G
                    case TelephonyManager.NETWORK_TYPE_CDMA: // 电信2G
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN: // api<8 : replace by 11
                        strNetworkType = "2G";
                        break;

                    case TelephonyManager.NETWORK_TYPE_EVDO_A: // 电信3G
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B: // api<9 : replace by 14
                    case TelephonyManager.NETWORK_TYPE_EHRPD: // api<11 : replace by 12
                    case TelephonyManager.NETWORK_TYPE_HSPAP: // api<13 : replace by 15
                        strNetworkType = "3G";
                        break;

                    case TelephonyManager.NETWORK_TYPE_LTE: // api<11 : replace by 13
                        strNetworkType = "4G";
                        break;

                    default:
                        if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA") ||
                                _strSubTypeName.equalsIgnoreCase("WCDMA") ||
                                _strSubTypeName.equalsIgnoreCase("CDMA2000")) {
                            strNetworkType = "3G";
                        } else {
                            strNetworkType = _strSubTypeName;
                        }
                        break;
                }
                Log.d(TAG, "Network getSubtype : " + Integer.valueOf(networkType).toString());
            }
        }

        Log.d(TAG, "Network Type : " + strNetworkType);
        return strNetworkType;
    }

    /**
     * 运营商代码​ ​MCC+MNC 的组合
     *
     * @return 中国移动/中国联通/中国电信/未知
     */
    public static String getProviderCode(Context context) {
        String providerCode = "0";//未知
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String IMSI = telephonyManager.getSubscriberId();
            Log.d(TAG, "getProvider.IMSI:" + IMSI);
            if (IMSI == null) {
                if (TelephonyManager.SIM_STATE_READY == telephonyManager.getSimState()) {
                    String operator = telephonyManager.getSimOperator();
                    Log.d(TAG, "getProvider.operator:" + operator);
                    if (operator != null) {
                        providerCode = operator.substring(0, 5);
                    }
                }
            } else {
                providerCode = IMSI.substring(0, 5);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return providerCode;
    }

    public static int getNetworkTypeInt(Context context) {
        String networkType = "";
        int networkTypeInt = 0;
        networkType = getNetworkTypeNoProvider(context);

        switch (networkType) {
            case "wifi":
                networkTypeInt = 5;
                break;
            case "2G":
                networkTypeInt = 2;
                break;
            case "3G":
                networkTypeInt = 3;
                break;
            case "4G":
                networkTypeInt = 4;
                break;
            default:
                networkTypeInt = 0;
                break;
        }

        return networkTypeInt;
    }

}
