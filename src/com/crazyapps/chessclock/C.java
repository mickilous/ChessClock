package com.crazyapps.chessclock;

public final class C {

	public static final int	MODE_NORMAL		= 0;
	public static final int	MODE_FISHER		= 1;
	public static final int	MODE_BRONSTEIN	= 2;

	public static final class prefs {
		public static final String	PREFERENCES			= "preferences";

		public static final String	TIME_P1				= "time_p1";
		public static final String	TIME_P2				= "time_p2";
		public static final int		TIME_DEFAULT		= 3600;
		public static final String	TIME_EQUALS			= "time_equals";
		public static final boolean	TIME_EQUALS_DEFAULT	= true;
		public static final String	STATUS_P1			= "status_p1";
		public static final String	STATUS_P2			= "status_p2";

		public final static String	SOUNDS_ON_CLICK		= "sound_on_click";
		public final static String	VIBRATE_ON_CLICK	= "vibrate_on_click";
		public final static String	SOUNDS_ON_GAMEOVER	= "sound_on_gameover";
		public final static String	VIBRATE_ON_GAMEOVER	= "vibrate_on_click";

		public static final String	MODE				= "mode";
		public static final String	MODE_TIME			= "mode_time";
	}

}
