package org.watzlawek;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;


import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Vector;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

/**
 * DataBackupHandler for anIMus 2.0 BA-Thesis 
 * Class for the managing data backup. It imports and exports data of the app. * 
 * @author Klaus-Peter Watzlawek * 
 * @version 2013-09-10
 */
public class DataBackupHandler {
	
	/**
	 * Lists that stores files for importation.
	 */
	protected Vector<DataBackupItem> filelistvector; 
	
	/**
	 * The applications context variable.
	 */
	protected Context context;
	
	/**
	 * Folder destination for backups.
	 */
	protected String backupfolder;
	
	/**
	 * Stores the database file name.
	 */
	protected String dbname;
	
	/**
	 * Stores the shared_prefs file name.
	 */
	protected String sharedprefsname;
	
	
	/**
	 * DataBackupHandler Constructor
	 * @param in_context Application context
	 * @param in_backup_folder Destionation of backup folder.
	 * @param in_db_name Name of database file.
	 * @param in_sharedprefsname Name of sharedprefs file.
	 */
	public DataBackupHandler(Context in_context, String in_backup_folder, String in_db_name, String in_sharedprefsname) {
		filelistvector = null;
		this.context = in_context;
		this.backupfolder = in_backup_folder;
		this.dbname = in_db_name;
		this.sharedprefsname = in_sharedprefsname; 
	}
	/**
	 * Generates a timestamp to use for backup folder names.
	 * @return current timestamp pattern like yyyy-MM-dd_kk-mm-ss.
	 */
	public String getFileDateStamp() {
		SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String filedate = s.format(new Date());
		return filedate;
	}
	
	/**
	 * Exports shared_prefs of the app to a selected backupfolder.
	 * @throws Exception 
	 */
	public void export_sharedprefs() throws Exception {
		this.export_file_abstracted("shared_prefs", sharedprefsname);
	}
	/**
	 * Exports the current database file stored in /data/data/PATH_TO_APPLICATION to a selected backupfolder.
	 * @throws Exception
	 */
	public void export_db() throws Exception {
		this.export_file_abstracted("databases", dbname);
	}
	
	/**
	 * Abstracted Version of backup process to use for more files.
	 * @param sub_dir directory of the chosen element.
	 * @param filename the destination filename for the file that should be changed.
	 * @throws Exception is thrown if something fails due I/O-Errors.
	 */
	protected void export_file_abstracted(String sub_dir, String filename ) throws Exception {
		//Location of Internal of Database
		File data_dir = new File(getAppDirectory(context));			
		
		File main_backup_dir = new File(Environment.getExternalStorageDirectory(), "/" + backupfolder);
		File backup_dir = new File(Environment.getExternalStorageDirectory(), "/" + backupfolder + "/backup_" + this.getFileDateStamp());
		File backup_subdir_databases = new File(backup_dir,"/" + sub_dir); 
		
		//Create Main Backup Directory	
		if(!main_backup_dir.exists()) {			
			main_backup_dir.mkdir();	
		}
		//Create Backup Directory		
		if(!backup_dir.exists()) {			
			backup_dir.mkdir();	
		}
		//Then Create sub directory
		if(!backup_subdir_databases.exists())
			backup_subdir_databases.mkdir();
				
		String path_to_file  = "//" + sub_dir + "//" + filename;
		this.ImExporterUniversal(data_dir, backup_dir, path_to_file);
	}	
	/**
	 * Function that lists files in a chosen directory.
	 * @param directory path as string.
	 * @return a Vector of String with all files of the directory.
	 */
	protected Vector<DataBackupItem> ListFilesIn(File directory) {
		Vector<DataBackupItem> vectorbackup = new Vector<DataBackupItem>();
		vectorbackup.clear();		
		
		//Read Files from Drive
		File files_in_dir = directory;		
		File[] files = files_in_dir.listFiles();	
	    for (File file : files){
	    	vectorbackup.add(new DataBackupItem(file.getPath(),-1));  	    	    	
	    }
	    
	    //Sort Files A..Z
	    Collections.sort(vectorbackup, new Comparator<DataBackupItem>() {
			public int compare(DataBackupItem lhs, DataBackupItem rhs) {				 
				return lhs.getFilepath().compareTo(rhs.getFilepath());
			}		
		});
	    
	    //Add ID after sort
	    for (int i = 0; i < vectorbackup.size(); i++ )
	    	vectorbackup.elementAt(i).setId(i);
	    return vectorbackup;		
	}	
	
	/**
	 * Lists all backups who are located in directory /sdcard/org.watzlawek/animus
	 * Sorts backups ascending.
	 */
	public void ReadBackupFilesFromDrive() {		
		filelistvector = ListFilesIn(new File(Environment.getExternalStorageDirectory(), "/" + backupfolder));	
	}
	
	/**
	 * Creates a array from a given internal vector and make reversed item order.
	 * @return Array of DataBackupItem in reversed order.
	 */
	public DataBackupItem[] getDataBackupItemArrayReverse() {
		ReadBackupFilesFromDrive();
		Collections.reverse(filelistvector);		
		DataBackupItem[] dbuparray = new DataBackupItem[filelistvector.size()];
		for (int i = 0 ; i <this.filelistvector.size() ; i++ )
			dbuparray[i] = filelistvector.elementAt(i);
		return dbuparray;
		
	}
	
	/**
	 * Returns size of found data backups.
	 * @return size as integer.
	 */
	public int getDatabackupsize() {
		return filelistvector.size();
	}
	
	/**
	 * Helper function for importing files.
	 * @param postionlist the element position of the filelist that should be imported.
	 * @param sub_dir directory of the chosen element.
	 * @param filename the destination filename for the file that should be changed.
	 * @throws Exception is thrown if something fails due I/O-Errors.
	 */
	protected void import_file_abstracted(int postionlist, String sub_dir, String filename ) throws Exception {		
		//Location of Internal of Database
		File data_dir = new File(getAppDirectory(context));							
		File backup_dir_new = null;		
		for (int i = 0; i < filelistvector.size(); i++) {
			if (postionlist == (filelistvector.elementAt(i).getId()))
			backup_dir_new = new File(filelistvector.elementAt(postionlist).getFilepath());
		}
		//File backup_dir_new = new File(filelistvector.elementAt(postionlist).getFilepath());
		
		String path_to_file  = "//" + sub_dir + "//" + filename;
		
		this.ImExporterUniversal(backup_dir_new, data_dir,  path_to_file);
		
		
	}
	/**
	 * Imports database by given id.
	 * @param id
	 * @throws Exception
	 */
	public void import_db(int id) throws Exception {
		this.import_file_abstracted(id, "databases", dbname);
	}
	
	/**
	 * Imports shared prefs by given id.
	 * @param id
	 * @throws Exception
	 */
	public void import_sharefprefs(int id) throws Exception {
		this.import_file_abstracted(id, "shared_prefs", sharedprefsname);
	}	
	
	/**
	 * Copies data stream from source directory to destination directory.
	 * if source directory is null only the file at destination point will be deleted.
	 * @param src_dir
	 * @param dst_dir
	 * @param PathToFile
	 * @throws Exception
	 */
	protected void ImExporterUniversal(File src_dir, File dst_dir, String PathToFile) throws Exception { 						
		if (dst_dir.canWrite()) {			
			File src_file = new File(src_dir, PathToFile);
			File dst_file = new File(dst_dir, PathToFile);
			
			//Makes File Reset
			if (dst_file.exists())
				dst_file.delete();		
				
			if (src_dir != null) {	
				//Transfer Data from source to destination
				FileChannel src = new FileInputStream(src_file).getChannel();            
				FileChannel dst = new FileOutputStream(dst_file).getChannel();
				dst.transferFrom(src, 0, src.size());
				src.close();
				dst.close();
			}			
		} 	
		
    }
	
	/**
	 * Resets application data to factory.
	 * @throws Exception
	 */
	public void ResetInternalData() throws Exception {
		String app_dir = this.getAppDirectory(context);	
		String path_to_shared_prefs  = "//" + "shared_prefs" + "//" + sharedprefsname;
		String path_to_databases  = "//" + "databases" + "//" + dbname;
		this.ImExporterUniversal(null, new File(app_dir), path_to_shared_prefs);
		this.ImExporterUniversal(null, new File(app_dir), path_to_databases);
	}
	
	/**
	 * Get the working directory of the app.
	 * @param context The application context.
	 * @return directory as string.
	 * @throws Exception
	 */
	public static String getAppDirectory(Context context) throws Exception {
	    return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).applicationInfo.dataDir;
	}	
	
}
