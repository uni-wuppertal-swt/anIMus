package org.watzlawek;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.net.ParseException;
import android.util.Log;

/**
 * Class for storing Backup Data. It stores paths and friendly names for listviews.
 * Also it handles file deletion.
 * @author Klaus-Peter Watzlawek
 * @version 2013-09-10
 *
 */
public class DataBackupItem {
	
	/**
	 * Stores an id for each backup up.
	 */
	private int id;
	/**
	 * Holds filepath variable that stores the absolute path of a file.
	 */
	private String filepath;
	
	/**
	 * Holds the name of the backup.
	 */
	private String backupname;
	
	/**
	 * Getter for filepath.
	 * @return filepath
	 */
	public String getFilepath() {
		return filepath;
	}
	
	/**
	 * Setter for filepath.
	 * @param filepath
	 */
	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}
	
	/**
	 * Constructor DataBackupItem
	 * @param in_filepath Path of file.
	 * @param in_id ID for selection in view.
	 */
	public DataBackupItem(String in_filepath, int in_id) {
		this.filepath = in_filepath;
		
		String parts[] = filepath.split("/");
		this.id = in_id;
		this.backupname = parts[parts.length-1];
		
	}
	/**
	 * Getter for id.
	 * @return the id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Setter for if
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Getter for friendly formated backupname as datetime from foldername.
	 * @return backupname
	 */	
	public String getBackupname() {		
		SimpleDateFormat s_in = new SimpleDateFormat("yyyyMMddHHmmss");
		SimpleDateFormat s_new = new SimpleDateFormat("dd.MM.yyyy, HH:mm:ss");
		Date timestamp_as_date = null;
		String friendlydate = backupname;
		try {								
			try {				
				timestamp_as_date = s_in.parse(RemovePatternFromString(friendlydate,"'.*'|\\D"));				
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
	 * Setter for backupname.
	 * @param backupname
	 */
	public void setBackupname(String backupname) {
		this.backupname = backupname;
	}

	/**
	 * Deletes a complete backup by chosen filepath.
	 */ 
	@SuppressWarnings("static-access")
	public void removefile() {
		File todelete = new File(filepath);
		if (todelete.exists())
			this.deleteRecursive(todelete);
	}
	/**
	 * Deletes files recursive.
	 * @param path
	 */
	public static void deleteRecursive(File path) {
		for (File file : path.listFiles()) { 
			if (file.isDirectory()) 
				deleteRecursive(file); 
				file.delete(); 
		} path.delete(); 
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
