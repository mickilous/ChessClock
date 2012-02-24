package com.crazyapps.chessclock.util;

import android.util.Log;

public class Xlog {

	private final static String	DEFAULT_TAG	= "ChessClock";

	// To enable a Log Level (DEBUG for example) :
	// In eclipse : set "-prop log.tag.ChessClock=DEBUG" to the 'additional emulator command line options' field on the
	// 'target' tab of the run configuration of your app.
	// OR
	// In shell : run "adb shell setprop log.tag.ChessClock DEBUG"
	public static void debug(String message, Object... args) {
		if (Log.isLoggable(DEFAULT_TAG, Log.DEBUG))
			Log.d(DEFAULT_TAG, String.format(message, args));
	}

}
