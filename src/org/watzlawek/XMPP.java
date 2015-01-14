package org.watzlawek;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import android.os.AsyncTask;

/**
 * Plain XMPP client. Just for developement.
 * Animus 2.0 uses IMServer
 * @see IMServer
 * @author safran
 *
 */
public class XMPP {

	private String mDomain;
	private XMPPConnection mConnection;
	private String mUsername;
	private String mPassword;
	
	public XMPP(String domain, String username, String password){
		mDomain = domain;
		mUsername = username;
		mPassword = password;
	}
	
	public void connect(){
		AsyncTask<Void, Void, Boolean> connectionThread = new AsyncTask<Void, Void, Boolean>(){

			@Override
			protected Boolean doInBackground(Void... params) {
				boolean isConnected = false;
				
				ConnectionConfiguration connectionConfiguration = new ConnectionConfiguration(mDomain);
				connectionConfiguration.setReconnectionAllowed(true);
				
				mConnection = new XMPPConnection(connectionConfiguration);
				
				XMPPConnectionListener connectionListener = new XMPPConnectionListener();
				mConnection.addConnectionListener(connectionListener);
	
				try {
					mConnection.connect();
					login(mConnection, mUsername, mPassword);
				} catch (XMPPException e) {
					e.printStackTrace();
				}
				
				return isConnected;
			}
			
		};
		connectionThread.execute();
	}
	
	private void login(XMPPConnection connection, final String username, final String password){
		try {
			connection.login(username, password);
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public class XMPPConnectionListener implements ConnectionListener{		
		public void connectionClosed() {
			// TODO Auto-generated method stub
			
		}

		public void connectionClosedOnError(Exception arg0) {
			// TODO Auto-generated method stub
			
		}

		public void reconnectingIn(int arg0) {
			// TODO Auto-generated method stub
			
		}

		public void reconnectionFailed(Exception arg0) {
			// TODO Auto-generated method stub
			
		}

		public void reconnectionSuccessful() {
			// TODO Auto-generated method stub
			
		}
		
	}
	
}
