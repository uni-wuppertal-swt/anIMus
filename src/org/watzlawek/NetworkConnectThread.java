package org.watzlawek;

/**
* New Class for NetworkConnectThread
* anIMus 2.0 BA Thesis
* @author Klaus-Peter Watzlawek
* @version 2013-09-10
*/
import java.util.concurrent.ExecutionException;

import org.jivesoftware.smack.AndroidConnectionConfiguration;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.ServiceDiscoveryManager;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;


/**
 * NetworkConnectThread for Connection to a XMPP Server.
 * There are two implementions. 
 * Only NetworkConnectAsyncThread is working and it is used today.
 * @author Klaus-Peter Watzlawek
 * 
 * @version 2013-09-10
 */
public class NetworkConnectThread extends Service {		
	
	/**
	 * Holds the NetworkConnectAsyncThread Object for establishing xmpp connection via thread. 
	 */
	protected NetworkConnectAsyncThread thread;
	
	/**
	 * Holds the NetworkConnectThreadAlternative object as alternative thread implementation.
	 * Current status: not working
	 */
	protected NetworkConnectThreadAlternative thread_alternative;
	
	/**
	 * Stores the domain of a wished server.
	 */
	String domain;
	
	/**
	 * Stores the port who belongs to domain.
	 */
	int port;
	
	/**
	 * Stores a handler for threading.
	 */
	private Handler handler;	
	
	/**
	 * Stores the username to login on a xmpp server.
	 */
	String user;
	String password;
	Context context;
	boolean encryption;
	
	Connection connection;
	
	boolean service_started;
	
	/**
	 * Prints a human readable error message.
	 * 
	 * @param raw_message The message which should be put out in a readable manner.
	 * @return Returns a string without characters.
	 */
	public String GetHumanReadableErrorMessage(String raw_message) {
		if (raw_message.contains("SASL")) {				
			return context.getText(R.string.IMServerLoginError).toString();		
		}
		else 
			if (raw_message.contains("XMPPError connecting")) {
				return context.getText(R.string.IMServerCannotConnect).toString();
			}
		
		return new String(context.getText(R.string.IMServerNotReachable).toString());
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Getter for thread
	 * @return instance of thread.
	 */
	public NetworkConnectAsyncThread getThread() {
		return thread;
	}
	
	/**
	 * Initializes all internal vars. 
	 * @param in_domain
	 * @param in_port
	 * @param in_user
	 * @param in_password
	 * @param in_context
	 * @param in_encryption
	 */
	public void init(String in_domain, int in_port, String in_user, String in_password, Context in_context, boolean in_encryption) {
		this.domain = in_domain;
		this.port = in_port;	
		this.user = in_user;
		this.password = in_password;
		this.context = in_context;
		this.encryption = in_encryption;
		service_started = true;
		Log.v("DOMAIN", domain.toString());
	}
	
	@Override
	public void onCreate() {		    	
		Log.v("NetworkConnectThread", "started");
	}
	
	/**
	 * Getter for XMPP Connection
	 * @return the established connection.
	 */
	public Connection getConnectionfromThread() {
		return this.connection;
	}
	
	/**
	 * Kills connection. Connection to a xmpp server is killed.
	 */
	public void kill() {
		NetworkDisconnectThreadInternal disconnThread = new NetworkDisconnectThreadInternal();
		disconnThread.execute(new Connection[] {connection});		
	}
	
	/**
	 * Prepares config and executes thread. 
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public void loader() throws InterruptedException, ExecutionException {
		
		AndroidConnectionConfiguration config = null;
		
		
		config = new AndroidConnectionConfiguration(domain, port, domain);
		config.setSendPresence(false);		
		
		if(encryption) {
			config.setSecurityMode(ConnectionConfiguration.SecurityMode.required);
			config.setCompressionEnabled(true);
		}
		else {
			config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);			
		}
		
		thread =  new NetworkConnectAsyncThread();	
		thread.init(user, password, context);		
		thread.execute(new AndroidConnectionConfiguration[] {config});
		
		connection = thread.get();	
	}
	
	/*anIMus 2.0 Alternative Thread Interface*/
	
	/*	TO-DO:
	 * 	HandlerThread thread = new HandlerThread(SERVICE_THREAD_NAME);
		thread.start();
		handlerThreadId = thread.getId();
		serviceLooper = thread.getLooper();
		serviceHandler = new ServiceHandler(serviceLooper);
	 * */
	
	/**
	 * Alternative Implemenation for more stability. Broken, not working,
	 * Still in development.
	 */
	public void loader_thread_alternative() {
		AndroidConnectionConfiguration config = null;
				
		config = new AndroidConnectionConfiguration(domain, port, domain);
		config.setSendPresence(false);	
		//Trigger Xabber 317:ConnectionThread.java
		if(encryption) {
			config.setSecurityMode(ConnectionConfiguration.SecurityMode.required);
			config.setCompressionEnabled(true);
		}
		else {
			config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);			
		}
		
		thread_alternative = new NetworkConnectThreadAlternative("SERVICE_CONNECTION", config);
		thread_alternative.init(user, password, context);
		thread_alternative.start();
		Long handlerThreadId = thread_alternative.getId();		
		Handler serviceHandler = new Handler(thread_alternative.getLooper());
		
		
		connection = thread_alternative.getConnection();
	}
	
	@Override
	public void onDestroy() {		
		thread = null;		
		Log.v("NetworkConnectThread", "killed");
		service_started = false;
	}
	
/**
 * Public class of a thread for connecting to a server.
 * Modified for anIMus 2.0 BA-Thesis	 * 
 * Added old NetworkConnectAsyncThread --> not stable connection, if UI thread dies
 * Added new NetworkConnectThreadAlternative --> hopefully more stable connection
 */
	public class NetworkConnectThreadAlternative extends HandlerThread  {
		AndroidConnectionConfiguration config;
		
		String user;
		String password;
		Context context;
		
		/**
		 * Initializes all internal vars. 
		 * @param in_user
		 * @param in_password
		 * @param in_context
		 */
		public void init(String in_user, String in_password, Context in_context) {
			this.user = in_user;
			this.password = in_password;
			this.context = in_context;		
		}
		
		/**
		 * Constructor for NetworkConnectThreadAlternative. Sets vars.
		 * @param serice_name
		 * @param in_config
		 */
		public NetworkConnectThreadAlternative(String serice_name, AndroidConnectionConfiguration in_config) {
			super(serice_name);
		// TODO Auto-generated constructor stub
			this.config = in_config;
		}
		
		
		/**
		 * Service Start Function. 
		 * Makes connection and logon in xmpp server with given user account data.
		 */
		@Override
		public void start() {
			connection = new XMPPConnection(config);
			try {
				connection.connect();
				connection.login(user, password, context.getText(R.string.app_name).toString());				
				ServiceDiscoveryManager.setIdentityName(context.getText(R.string.app_name).toString());				
				
			}
			catch(Exception e) {
				//publishProgress(new Exception[] {e});
				Log.v("XMPP", e.toString());
				//return null;
			}
		}
		
		public Connection getConnection() {
			return connection;
		}
		
	}		
/**
 * Working Class to establishing connection to a xmpp server with AsyncTask thread.
 * @author Klaus-Peter Watzlawek
 * @version 2013-09-09
 */
public class NetworkConnectAsyncThread extends AsyncTask<AndroidConnectionConfiguration, Exception, Connection>{	
	
	String user;
	String password;
	Context context;
	
	public void init(String in_user, String in_password, Context in_context) {
		this.user = in_user;
		this.password = in_password;
		this.context = in_context;		
	}
	/**
	 * Method which is called by the thread. It establishes the connection.
	 */
	@Override		
	public Connection doInBackground(AndroidConnectionConfiguration... connConfig) {			
		Connection connection = null;
		for(ConnectionConfiguration conf : connConfig) {
			
			connection = new XMPPConnection(conf);
			try {
				connection.connect();
				connection.login(user, password, context.getText(R.string.app_name).toString());				
				ServiceDiscoveryManager.setIdentityName(context.getText(R.string.app_name).toString());				
				
			}
			catch(Exception e) {
				publishProgress(new Exception[] {e});
				Log.v("XMPP", e.toString());
				return null;
			}
		}
		return connection;
	}
	
	/**
	 * Method which will be called when an exception occurs on connecting.
	 */
	protected void onProgressUpdate(Exception... e) {
		Toast toast = Toast.makeText(context, GetHumanReadableErrorMessage(e[0].getMessage()), Toast.LENGTH_LONG);
		toast.show();
	}	
}

/**
 * Private class of a thread for disconnecting from a server.
 */
public class NetworkDisconnectThreadInternal extends AsyncTask<Connection, XMPPException, Void> {
	/**
	 * Method which will be called by the thread. It disconnects from a server.
	 */
	@Override
	public Void doInBackground(Connection... connection) {
		for(Connection conn : connection) {
			conn.disconnect();
		}
		return null;
	}
}

}