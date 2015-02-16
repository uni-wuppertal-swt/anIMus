package org.watzlawek;

import org.watzlawek.contactmanager.ContactDatabaseHandler;

import net.sqlcipher.database.SQLiteDatabase;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings.Secure;
import android.util.Log;

/**
 * This class extends the default Android application class for the purpose of providing global variables which can be accessed from every activity.
 * Specially the object of the class ServerManager has to be accessed by nearly every activity.
 * It provides access to the servers stored inside the local SQLite database and the contacts of the connected server.
 * 
 * @author Klaus-Peter Watzlawek
 * @author Moritz Lipfert
 * 
 * @version 2014-02-16
 */
public class IMApp extends Application {
	/**
	 * This object holds the ServerManager object which provides access to the applications main logic and algorithms.
	 */
	private ServerManager servermanager;
	
	/**
	 * This object holds the AppPrefHandler object which  provides access to preferences.
	 */
	private AppPrefHandler appprefhandler;
	
	/**
	 * This object holds the Autodiscover object which provides reading of contacts stored in the device.
	 * Further it connects to auto discover management server. 
	 */
	private AutoDiscover autodiscover;
	
	/**
	 * Holds a reference to the databackup handler class object.
	 */
	private DataBackupHandler databackuphandler;
	
	/**
	 * Holds a reference to the Contact Database handler class object.
	 */
	private ContactDatabaseHandler contactdatabasehandler;
	
	/**
	 * Getter for the servermanager object.
	 * 
	 * @return servermanager object of type ServerManager.
	 */
	public ServerManager getServerManager() {
		return servermanager;
	}	
	
	/**
	 * anIMus 2.0 BA-Thesis
	 * Getter for autodiscover object.
	 * 
	 * @return autodiscover object of type AutoDiscover.
	 */
	public AutoDiscover getAutoDiscover() {
		return autodiscover;
	}
	/**
	 * anIMus 2.0 BA-Thesis
	 * Getter for databackuphandler object.
	 * 
	 * @return databackuphandler object of type DataBackupHandler.
	 */
	public DataBackupHandler getDataBackupHandler() {
		return databackuphandler;
	}
	
	/**
	 * Getter for contactdatabasehandler
	 * @return contactdatabasehanlder object of Type ContactDatabaseHandler.
	 */
	public ContactDatabaseHandler getContactDatabasehandler() {
		return contactdatabasehandler;
	}
	
	
	/**
	 * Getter for the appprefhandler object.
	 * 
	 * @return appprefhandler object of type AppPrefHandler.
	 */
	public AppPrefHandler getAppPrefHandler() {
		return appprefhandler;
	}
	
	/** 
	 * This method is called on the application's start.
	 * It creates the servermanager, apprefhandler and autodiscover object.
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		
		//Start SQLICiper anIMus 2.0 BA-Thesis		
		SQLiteDatabase.loadLibs(this);
		
		appprefhandler = new AppPrefHandler(this);
		
		servermanager = new ServerManager();
		servermanager.InitAndsetContext(this);		
		
		autodiscover = new AutoDiscover(this);
		
		databackuphandler = new DataBackupHandler(this,"org.watzlawek.animus", servermanager.getServerDatabaseName(), "org.watzlawek_preferences.xml");
		
		contactdatabasehandler = new ContactDatabaseHandler(this);
	}
	
	/**
	 * This method is called on termination of the application.
	 * It assures that the connection to a server will become closed if established.
	 */
	@Override
	public void onTerminate() {
		super.onTerminate();
		
		servermanager.disconnect();
	}
	
	/**
	 * Check if internet connection, e.g wifi or gprs, is available
	 * Idea found on http://stackoverflow.com/questions/4238921/android-detect-whether-there-is-an-internet-connection-available
	 * @return true if internet connection is available
	 */
	public boolean haveNetworkConnection() {
	    boolean haveConnectedWifi = false;
	    boolean haveConnectedMobile = false;

	    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo[] netInfo = cm.getAllNetworkInfo();
	    for (NetworkInfo ni : netInfo) {
	        if (ni.getTypeName().equalsIgnoreCase("WIFI"))
	            if (ni.isConnected())
	                haveConnectedWifi = true;
	        if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
	            if (ni.isConnected())
	                haveConnectedMobile = true;
	    }
	    return haveConnectedWifi || haveConnectedMobile;
	}
	
	/**
	 * Starts the Service, ServerManger is the Service Class
	 */
	public void startSystemService() {	
		startconnectionservice();
		if (servermanager.getConnectedServer() != null)
			startService(new Intent(this, ServerManager.class));		
	}
	
	/**
	 *  Kills the Service, ServerManger is the Service Class
	 */
	public void killSystemService() {		
		stopService(new Intent(this, ServerManager.class));
		stopService(new Intent(this, NetworkConnectThread.class));	
	}
	
	/**
	 * Starts Service for NetworkConnectThread.
	 */
	public void startconnectionservice() {
		startService(new Intent(this, NetworkConnectThread.class));	
	}
	
	/**
	 * Forces a reload of the Servermanager and AppPrefHanlder. It closes database and reload data.
	 * Used for importing data backup files.
	 */
	public void ReloadServerManager() {			
		this.killSystemService();
		this.servermanager.closeDBfromDBHandler();		
		servermanager = new ServerManager();
		servermanager.InitAndsetContext(this);	
	}
	
	public void ReloadPrefManager() {
		appprefhandler = new AppPrefHandler(this);
	}
	
	/**
	 * Gets the unique Android ID from the target device.
	 * @return android_id as String.
	 */
	public String getAndroid_id() {		 
		return Secure.getString(getContentResolver(), Secure.ANDROID_ID);
	}
}
