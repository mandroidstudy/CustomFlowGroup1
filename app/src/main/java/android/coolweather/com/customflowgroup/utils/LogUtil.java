package android.coolweather.com.customflowgroup.utils;

import android.util.Log;

/**
 * Created by Mao on 2018/2/20.
 */

public class LogUtil {
    private static String TAG="TAG";
    private static boolean DEBAG=false;
    private LogUtil(){

    }
    public static void d(String msg){
        if (DEBAG==true){
            Log.d(TAG,msg);
        }
    }
}
