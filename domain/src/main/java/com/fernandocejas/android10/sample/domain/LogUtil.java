package com.fernandocejas.android10.sample.domain;


import android.util.Log;

/**
 * Created by pointphoton on 15/05/2017.
 */

public class LogUtil {

    public static void logTest(final String message){

            // do something for a debug build

            final StackTraceElement stackTrace = new Exception().getStackTrace()[1];

            String fileName = stackTrace.getFileName();
            if (fileName == null)
                fileName = "";  // It is necessary if you want to use proguard obfuscation.

            final String info = stackTrace.getMethodName() + " (" + fileName + ":"
                    + stackTrace.getLineNumber() + ")";

            Log.i("", info + " : *** : " + message);
        }

}
