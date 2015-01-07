package org.watzlawek;

import java.util.Collections;
import java.util.Vector;

import net.sqlcipher.database.SQLiteDatabase;

import org.jivesoftware.smack.SmackAndroid;
import org.jivesoftware.smack.SmackConfiguration;
import org.watzlawek.R;

import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;

import android.content.Context;
import android.content.res.Resources;

import android.util.Log;
import android.widget.Toast;

/**
 * Class for managing the servers and it's connections.
 * It handles the database commands for adding, editing or removing servers.
 * Furthermore it delegates the connection / disconnection commands on a server.
 * When connected to a server the own status can be set by this class.
 * Now it is implemented as an Android System Service to prevent Android from killing.
 * 
 * @author Klaus-Peter Watzlawek
 * @author Moritz Lipfert
 * 
 * @version 2012-11-14
 */
public class ServerManager extends Service {
	
	/**
	 * indication if services was started 
	 */
	
	private boolean service_started;
	
	/**
	 * Shared Message is used to give data from external apps to the other parts of anIMus.
	 */
	private String shared_message;
	
	/**
	 * offlinemode variable for connected server on lost connection
	 */
	private boolean offline;
	
	/**	 
	 * The current position of a selected server
	 */
	private int position;
	
	/**
	 * The NotificationManager, used for the service icon.
	 */
	private NotificationManager mNM;
	
	/**
	 * A unique number to identify the Notification.
	 */
	private int NOTIFICATION = R.string.iconSrvNotificationID;
	
	/**
	 * Creates an anIMus Service Notification Icon in the Android taskbar.
	 */
	
	
	@Override
	public void onCreate() {
		mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		showNotification();		
		this.offline = false;	
		this.shared_message = null;		
		this.service_started = true;
	}
	
	/**
	 * Removes the Service Notification Icon. 
	 */
	@Override
	public void onDestroy() {
		mNM.cancel(NOTIFICATION);	
		service_started = false;
		
	}
	
	/**
	 * Creates the Notification Service Icon
	 */
	private void showNotification() {		
		CharSequence text = getText(R.string.IMServerConnected);
    	Notification notification = new Notification(R.drawable.ic_launcher, null, System.currentTimeMillis());
    	notification.flags |= Notification.FLAG_NO_CLEAR;     	
    	Intent intent = new Intent(this,IMActivity.class);			
    	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);   	
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);  
        notification.setLatestEventInfo(this, getText(R.string.app_name), text, contentIntent);     			
		mNM.notify(NOTIFICATION, notification);			
	}	
	
	/**
	 * Binds nothing to our service, has be implemented
	 */
	@Override
	public IBinder onBind(Intent intent) {
		 return null;
	}
	
	/**
	 * The application's context object.
	 */
	private Context context;
	
	/**
	 * A reference to the connected server.
	 * Null when no server is connected.
	 */
	private IMServer connectedServer;
	
	/**
	 * The ServerDatabaseHandler object for operations on the SQLite database.
	 */
	private ServerDatabaseHandler dbHandler;
	
	/**
	 * A dynamic list of servers of type IMServer.
	 */
	private Vector<IMServer> vectorServer;
	
	/**
	 * Gets the server list from database.
	 */
	private void refreshVectorServer() {
		vectorServer = dbHandler.getServers();
	}
	
	/**
	 * This function initializes the database handler object.
	 * It loads the server list from database.
	 * Furthermore the aSmack timeout is set to 10 seconds and some workarounds get applied.
	 * 
	 * @param in_context The activity's contexts4.
	 */	
	public void InitAndsetContext(Context in_context) {
		this.context = in_context;
		connectedServer = null;		
		
		dbHandler = new ServerDatabaseHandler(context);
		
		IMApp app = (IMApp)context.getApplicationContext();		
		if (app.getAppPrefHandler().getDBCryptState().equals("_internal")) {
			//dbHandler.ConvertPlain2EncryptedDB(app.getAndroid_id());
			dbHandler = new ServerDatabaseHandler(context, app.getAndroid_id()); 			
		} else 
			if (app.getAppPrefHandler().getDBCryptState().equals("_disabled")) 
				dbHandler = new ServerDatabaseHandler(context);
		
		refreshVectorServer();
		
		SmackConfiguration.setPacketReplyTimeout(10000);
		
		/*
		 * Some workarounds for aSmack and XMPP.
		 */
		SmackAndroid.init(context);
		System.setProperty("java.net.preferIPv6Addresses", "false");
	}
	
	/**
	 * Getter method for connectedServer.
	 * 
	 * @return A reference to the connected Server.
	 */
	public IMServer getConnectedServer() {
		return connectedServer;
	}
	
	/**
	 * Converts the IMServer vector to a non dynamic array.
	 * 
	 * @return Array of IMServer objects.
	 */
	public IMServer[] getVectorServerArray() {
		//Collections.sort(vectorServer);
		IMServer[] serverarray = new IMServer[vectorServer.size()];
		for(int i = 0; i < vectorServer.size(); i++) {
			serverarray[i] = vectorServer.elementAt(i);
		}
		return serverarray;
	}
	
	/**
	 * Method for getting the dynamic list of servers.
	 * 
	 * @return List of servers of type Vector<IMServer>.
	 */
	public Vector<IMServer> getVectorServer() {
		return vectorServer;
	}
	
	/**
	 * Returns The current position of the connected server of all registered servers.
	 * @return The current position.
	 */
	public int getCurrentPosition() {
		return this.position;
	}
	
	/**
	 * Adds new server to the database.
	 * 
	 * @param input_type The server type: XMPP, ICQ etc..
	 * @param input_domain The domain of the server.
	 * @param input_port The port of the server.
	 * @param input_user The username for login.
	 * @param input_pw The password of the user.
	 * @param in_serviceaddress The anIMa Service Address.
	 * @param input_enc If the server supports encryption this value has to be true.
	 */
	public void addServer(String input_type, String input_domain, int input_port, String input_user, String input_pw, String in_serviceaddress, boolean input_enc) {		
		dbHandler.insertServer(input_type, input_domain, input_port, input_user, input_pw, input_enc);
		if (!in_serviceaddress.equals("")) {
			dbHandler.insertTokenSystemAccount(input_user + "@" + input_domain, AutoDiscover.getSHA256FromOwnPhoneNumber(), AutoDiscover.randomPassword(16), in_serviceaddress);
			Log.v("TokenSystemAccount", "CREATED");
		}
		refreshVectorServer();
	}
	
	
	/**
	 * Updates the parameters of an exisiting server in database.
	 * 
	 * @param in_position Server's position in array.
	 * @param in_domain The domain of the server.
	 * @param in_port The port of the server.
	 * @param in_user The username for login.
	 * @param in_pw The password of the user.
	 * @param in_serviceaddress The anIMa Service Address.
	 * @param in_enc If the server supports encryption this value has to be true.
	 */
	public void commitUpdate(int in_position, String in_domain, int in_port, String in_user, String in_pw, String in_serviceaddress,  boolean in_enc) {		
		dbHandler.updateServer(Integer.toString(vectorServer.elementAt(in_position).getServer_id()) , in_domain, in_port, in_user, in_pw, in_enc);
		dbHandler.updateTokenSystemAccount(in_user + "@" + in_domain, AutoDiscover.getSHA256FromOwnPhoneNumber(), "", "", "", in_serviceaddress);
		refreshVectorServer();
	}
	
	/**
	 * This method establishes the connection to a server.
	 * If a server connection is already established, an error message will be shown.
	 * 
	 * @param position Position of server in vectorServer.
	 */
	public void connect(int position) {	
		IMApp app = (IMApp)context.getApplicationContext();		
    	if( app.haveNetworkConnection() ) {
			if(connectedServer == null) {
				//Start IMServer as Services				
				connectedServer = vectorServer.elementAt(position).connect();
				this.position = position;
			}
			else {
				Resources res = context.getResources();
				Toast toast = Toast.makeText(context, res.getText(R.string.serverlistAlreadyConnected), Toast.LENGTH_SHORT);
				toast.show();
			}
    	}
    	else {
    		Toast toast = Toast.makeText(context, R.string.IMAppNoInternet, Toast.LENGTH_LONG);
			toast.show();
		}
	}
	
	/**
	 * This method deletes a server from database.
	 * 
	 * @param position Position of server in vectorServer.
	 */
	public void deleteServer(int position) {
		  
			if(connectedServer == null) {		
				dbHandler.deleteServer("" + vectorServer.elementAt(position).getServer_id());
				refreshVectorServer();
			}
			else {
				Resources res = context.getResources();
				Toast toast = Toast.makeText(context, res.getText(R.string.serverlistConnectedDelete), Toast.LENGTH_SHORT);
				toast.show();			
			}
	}
	
	/**
	 * Disconnects the currently connected server.
	 */
	public void disconnect() {
		if(connectedServer != null) {
			connectedServer.disconnect();
			connectedServer = null;		
			service_started = false;
			this.offline = false;
		}
	}
	
	/**
	 * if internet connections dies, we switch to the secure offline mode
	 */
	public void offlineMode() {
		if(connectedServer != null) {
			connectedServer.offlineMode();
			this.offline = true;			
		}
	}
	
	/**
	 * Passes the status to be set to the connected server instance.
	 * 
	 * @param status The status to be set.
	 */
	public void setStatus(IMServer.Status status) {
		if(connectedServer != null) {
			connectedServer.setStatus(status);
		}
		else {
			Resources res = context.getResources();
			Toast toast = Toast.makeText(context, res.getText(R.string.statusErrorNotConnected), Toast.LENGTH_LONG);
			toast.show();
		}
	}
	
	/**
	 * Get Integer Representation of Status
	 */
	public int getStatusInt() {		
		//Resources res = getResources();
		//String[] status = new String[] { res.getString(R.string.statusAvailable), res.getString(R.string.statusAway), res.getString(R.string.statusExtendedAway), res.getString(R.string.statusFreeToChat), res.getString(R.string.statusDnd) };
		//String[] status = new String[] {"Verfügbar", "Abwesend", "weiter weg", "frei", "nixnix"};
		if(connectedServer != null) {
			return connectedServer.getStatus().ordinal();			 
		}
		return 0;		
	}
	
	/**
	 * Get for Offlinemode State
	 * @return true if offline or false if online
	 */
	public boolean isOffline() {
		return offline;
	}
	
	/**
	 * Gets the shared message which was set by the IMAcitivity
	 */
	public String getSharedMessage() {
		return this.shared_message;
	}
	
	public void setShareMessage(String message) {
		shared_message = new String(message);
	}	
	/**
	 * Returns the database name from dbhandler.
	 * @return database name as string.
	 */
	public String getServerDatabaseName() {
		return this.dbHandler.getServerDatabaseName();
	}
	
	/**
	 * Closes current database connections.
	 */
	public void closeDBfromDBHandler() {
		this.dbHandler.closeDB();
	}
	
	/**
	 * Indicates if ServerManager Service is running.
	 * @return
	 * - true if Service is started.
	 * - false if Service is shutdown.
	 */
	public boolean isServiceRunning() {
		return this.service_started;
	}
	
	public void DatabaseEncryptionValidator() {
		IMApp app = (IMApp)context.getApplicationContext();	
		if (app.getAppPrefHandler().getDBCryptState().equals("_internal")) {
			//dbHandler.setPassword(app.getAndroid_id());			
			dbHandler.ConvertPlain2EncryptedDB(app.getAndroid_id());			
		} 
		else 
			if (app.getAppPrefHandler().getDBCryptState().equals("_disabled")) {
				dbHandler.setPassword("");
			}		
	}
	
	/**
	 * Getter for ServerDatabaseHandler
	 * @return instance of ServerDatabaseHandler;
	 */
	public ServerDatabaseHandler getServerDatabaseHandler() {
		return this.dbHandler;
	}
	
	
	
}
