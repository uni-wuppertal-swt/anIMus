package org.watzlawek;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.net.ParseException;
import android.text.Html;
import android.util.Log;

/**
 * This class was introduced by anIMus 2.0. An instance of this class stores a message item.
 * It is also used for the new chat bubble history.
 * Relevant data like the message, the timestamp an the username are stored. * 
 * @author Klaus-Peter Watzlawek
 * 
 * @version 2013-09-10
 */
public class MessageItem {
	/**
	 * Save the message.
	 */
	private String message;
	
	/**
	 * Saves the timestamp like pattern of 1988-09-02_09:10:56)
	*/ 	 
	private String timestamp;
	
	/**
	 * Saves the username.
	 */
	private String username;
	
	/**
	 * Saves if a ChatBubble should appear leftside (0) or rightside (1).
	 */
	int leftside;	
	
	/**
	 * Constructor for MessageItem
	 * @param in_message Stores the Message.
	 * @param in_timestamp Stores the Timestamp of of message.
	 * @param in_username Stores the Username of the owner of the Message.
	 */
	public MessageItem(String in_message, String in_timestamp, String in_username) {
		this.message = in_message;
		this.timestamp = in_timestamp;
		this.username = in_username;	
		//Additonal only for Bubble Chat		
		this.leftside = 0;
	}
	
	/**
	 * Sets the message.
	 * @param in_message
	 */
	public void setMessage(String in_message) {
		this.message = in_message;
	}
	
	/**
	 * Sets the timestamp.
	 * @param in_timestamp
	 */
	public void setTimestamp(String in_timestamp) {
		this.timestamp = in_timestamp;
	}
	
	/**
	 * Sets occupation for Chatbubbles: 0 = left, 1 = right.
	 * @param in_left
	 */
	public void setLeftside(int in_left) {
		this.leftside = in_left;
	}
	
	/**
	 * Sets the Username.
	 * @param in_username
	 */
	public void setUsername(String in_username) {
		this.username = in_username;
	}
	
	/**
	 * Gets for Message.
	 * @return the message.
	 */
	public String getMessage() {
		return this.message;
	}
	/**
	 * Gets the Timestamp.
	 * @return the timestamp.
	 */
	String getTimestamp() {
		return this.timestamp;
	}
	
	/**
	 * Sets the Username.
	 * @return the username.
	 */
	String getUsername() {
		return this.username;		
	}	
	
	public int getLeftside() {
		return this.leftside;
	}
	
	/**
	 * Removes Date from TimeStamp 
	 * @return timestamp like pattern of (kk:mm:ss)
	 */
	public String ShowOnlyClockTime() {	
		return RemovePatternFromString(this.getTimestamp(),"[\\d|-]+_");
	}
	
	/**
	 * Makes the Timestamp pattern from yyyyMMddkkmmss to dd.MM.yyyy, kk:mm:ss
	 * @return timestamp like pattern of dd.MM.yyyy, kk:mm:ss
	 */
	public String ShowFriendlyTimeStamp() {
		SimpleDateFormat s_in = new SimpleDateFormat("yyyyMMddHHmmss");
		SimpleDateFormat s_new = new SimpleDateFormat("dd.MM.yyyy, HH:mm:ss");
		Date timestamp_as_date = null;
		String friendlydate = "";
		try {								
			try {
				timestamp_as_date = s_in.parse(RemovePatternFromString(getTimestamp(),"'.*'|\\D"));
			} catch (java.text.ParseException e) {				
				e.printStackTrace();
			}
			friendlydate = s_new.format(timestamp_as_date).toString();
			
		} catch (ParseException e) {					
			e.printStackTrace();
		}		
		
		return friendlydate;
	}
	/**
	 * Shows the Username without html code wrapped arround.
	 * Removes last chat, cause of ":"
	 * @return the plain username. 
	 */
	public String ShowUsernamePlain() {		
		String str = Html.fromHtml(this.getUsername()).toString();
		return str.substring(0,str.length() - 2);	
	}
	
	/**
	 * Shows the Timestamp without html code wrapped arround.
	 * @return the plain Timestamp. 
	 */
	public String ShowTimeStampPlain() {
		return Html.fromHtml(this.getTimestamp()).toString();
	}
	
	/**
	 * Shows the Message without html code wrapped arround.
	 * @return the plain Message. 
	 */
	public String ShowMessagePlain() {
		return Html.fromHtml(this.getMessage()).toString();
	}
	
	/**
	 * Removes unwanted chars from a string with a regular expression pattern
	 * @param str
	 * @param pattern
	 * @return new string with removed pattern chars
	 */
	public static String RemovePatternFromString(String str, String pattern) {		
		Matcher matcher = Pattern.compile(pattern).matcher(str);		 
		StringBuffer temp = new StringBuffer();
		while (matcher.find())
			matcher.appendReplacement(temp, "");
		matcher.appendTail( temp );
		return temp.toString();	
	}
	
}

