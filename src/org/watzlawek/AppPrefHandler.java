package org.watzlawek;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Class for read operations on the default shared preferences file which is created or modified by "AppPrefActivity".
 * This class is used to read the currently set theme setting.
 * 
 * @author Klaus-Peter Watzlawek
 * @author Moritz Lipfert
 * 
 * @version 2013-09-10
 */
public class AppPrefHandler {
	/**
	 * This object holds the SharedPreferences file to read the settings.
	 */
	private SharedPreferences sharedPreferences;
	
	/**
	 * Default constructor for initializing the preferences object.
	 * 
	 * @param context The Activity's context object.
	 */
	public AppPrefHandler(Context context) {
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);			
	}
	
	/**
	 * This method reports back if user avatar images should be displayed in contact list.
	 * 
	 * The default value is "false".
	 * 
	 * @return True, when avatars should be displayed. Otherwise false.
	 */
	public boolean avatarEnabled() {
		return sharedPreferences.getBoolean("avatarDisplay", false);
	}
	
	/**
	 * This method reports back if the new chat style, bubble chat, is enabled. This is a anIMus 2.0 feature. 
	 * 
	 * The default value is "true".
	 * 
	 * @return True, when chat bubble should be displayed. Otherwise false.
	 */
	public boolean bubblesEnabled() {
		return sharedPreferences.getBoolean("bubbleChat", true);
	}
	
	/**
	 * This method reports if connection should be re-established after connection lost.
	 * anIMus 2.0 BA-Thesis
	 * 
	 * The default value is "true".
	 * @return True, when auto reconnect should be used. Otherwise false.
	 */
	public boolean autoReconnectEnabled() {
		return sharedPreferences.getBoolean("autoReconnect", true);
	}
	
	/**
	 * This method reports back if the chat history should be cleared when the chat activity become closed.
	 * 
	 * The default value is "false".
	 * 
	 * @return True, when the history should be cleared. Otherwise false.
	 */
	public boolean clearHistoryOnClose() {
		return sharedPreferences.getBoolean("historyClear", false);
	}
	
	/**
	 * This method reports back if notifications are enabled.
	 * Notifications appear on new message arrival when the specific contacts ChatActivity of the contact who sends the new message is currently not displayed.
	 * 
	 * "True" is the default value.
	 * 
	 * @return True, when notifications are enabled. Otherwise false.
	 */
	public boolean notificationsEnabled() {
		return sharedPreferences.getBoolean("notifications", true);
	}
	
	/**
	 * Returns the currently set alarm notification.
	 * anIMus 2.0 BA-Thesis AddOn
	 * The possibilities are:
	 * - _sound: Only Sound.
	 * - _vibration: Only vibration.
	 * - _soundandvibration: Sound and vibration.
	 * 
	 * "_soundandvibration" is the default value if no user made setting is set.
	 * 
	 * @return One of the theme identifiers described above.
	 */
	public String getnotificationAlarm() {
		return sharedPreferences.getString("listAlarm", "_soundandvibration");
	}
	
	/**
	 * Returns the currently set LED color.
	 * anIMus 2.0 BA-Thesis AddOn
	 * The possibilities are:
	 * - _disabled: No LED
	 * - _white: White
	 * - _red: Red
	 * - _yellow: Yellow
	 * - _green: Green
	 * - _cyan: Cyan
	 * - _blue: Blue
	 * - _purple: Purple
	 * @return color type as integer.
	 */
	public int getnotificationLEDLight() {
		String color_as_string = sharedPreferences.getString("listLED", "_disabled");		
		
		String color_str[] = {"_disabled", "_white", "_red","_yellow","_green","_cyan","_blue","_purple"};
		int color_int[] = {Color.BLACK,Color.WHITE,Color.RED,Color.YELLOW,Color.GREEN, Color.CYAN, Color.BLUE, Color.MAGENTA};
		
		for (int i = 0; i < color_str.length; i++ )
			if (color_as_string.equals(color_str[i]))
				return color_int[i];		
		
		return Color.BLACK;  // black light --> _disabled
	}
	/**
	 * Returns the currently set OTR behavior.
	 * anIMus 2.0 BA-Thesis AddOn
	 * The possibilities are:
	 * - _manual: 0
	 * - _automatic: 1
	 * - _forced: 2
	 * @return behavior state as string value.
	 */
	public String getOTRBehaviourState() {
		String otrbehavior_as_string = sharedPreferences.getString("listOTRBehavior", "_manual");
		
		String behavior_str[] = {"_manual", "_automatic", "_forced"};		
		
		for (int i = 0; i < behavior_str.length; i++ ) {
			if (otrbehavior_as_string.equals(behavior_str[i])) {				
				return behavior_str[i].toString();
			}
		}
		return "_manual";		
	}	
	
	/**
	 * Returns the database encryption state.
	 * anIMus 2.0 BA-Thesis AddOn	 * 
	 * The possibilities are:
	 * - _disabled: 0
	 * - _internal: 1
	 * - _passwordmode: 2
	 * @return db crypt state as string value.
	 */
	public String getDBCryptState() {
		String dbcryptstate_as_string = sharedPreferences.getString("listDBCryptMode", "_disabled");
		
		String dbcrpyt_str[] = {"_disabled", "_internal", "_passwordmode"};	
		
		for (int i = 0; i < dbcrpyt_str.length; i++ ) {
			if (dbcryptstate_as_string.equals(dbcrpyt_str[i])) {
				return dbcrpyt_str[i].toString();
			}
		}
		
		return "_disabled";
	}
	
	/**
	 * Returns the currently set theme.
	 * 
	 * The possibilities are:
	 * - _light: The light theme.
	 * - _dark: The dark theme.
	 * 
	 * "_light" is the default value if no user made setting is set.
	 * 
	 * @return One of the theme identifiers described above.
	 */
	public String getTheme() {
		return sharedPreferences.getString("listTheme", "_light");
	}
	
}
