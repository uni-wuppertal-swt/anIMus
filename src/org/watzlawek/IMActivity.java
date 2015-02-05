package org.watzlawek;

import org.watzlawek.views.activities.GroupManagementActivity;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;

/**
 * This class represents the main activity with it's TabHost for switching between server and contact list.
 * 
 * @author Klaus-Peter Watzlawek
 * @author Moritz Lipfert
 * 
 * @version 2013-09-10
 */
public class IMActivity extends TabActivity {
	
	/**
	 * Saves the last connected server id if connection gets lost.
	 */
	int lastconnectedserverid;
	/**
	 * TextView that shows the Status of the current user account
	 */
	private TextView textViewAppName;
	
	/**
	 * Handler used for refreshing the Connection State und Status
	 */
	private Handler handler;	
	
	/**
	 * Runnable function Object to refresh the Connection State und Status list every second, 
	 * interval is set to one second (1000ms)
	 */
	private final Runnable runnable = new Runnable() {
	    public void run()   {	    	
	    		handler.postDelayed(runnable,1000);
	    		CheckStateAndStatus();	    		
	    }
	};
	
	/**
	 * Checks state of network connection. Handles auto reconnect.
	 * If connection gets down, app switches to offline mode.
	 */
	private void CheckStateAndStatus() {
		Resources res = getResources();
		IMApp app = (IMApp)getApplicationContext();
		//if (app.getServerManager().isServiceRunning())
			//Log.v("Service runs", "RUN");
		
		if (app.getServerManager().getConnectedServer() != null) {
			String[] status = new String[] { res.getString(R.string.statusAvailable), res.getString(R.string.statusAway), res.getString(R.string.statusExtendedAway), res.getString(R.string.statusFreeToChat), res.getString(R.string.statusDnd) };
			if (!app.getServerManager().getConnectedServer().isOffline())				
				textViewAppName.setText(status[app.getServerManager().getStatusInt()]);
			
			if (!app.haveNetworkConnection()) {
				lastconnectedserverid = app.getServerManager().getCurrentPosition();
				app.getServerManager().offlineMode();				
				textViewAppName.setText(res.getString(R.string.statusOffline));
				//app.killSystemService();
			
			} else { //Added Auto Reconnect. anIMus 2.0 BA-Thesis
				AppPrefHandler appprefhandler = new AppPrefHandler(this);   
		    	if (appprefhandler.autoReconnectEnabled() && app.getServerManager().isOffline()) {	
					app.getServerManager().disconnect();
					app.getServerManager().connect(lastconnectedserverid);					
				} 		    	
			}
			
		} 
		else {
			textViewAppName.setText(res.getString(R.string.app_name));
		}			
	}

	/**
	 * This method is called by the app on creation of the activity.
	 * It initializes the TabHost object and assigns the respective activities to the server and contacts tabs.
	 * 
	 * @param savedInstanceState Saved instance of this activity. This parameter is only used internally by the application.
	 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);    
        
        Intent intent;          
        
        TabHost tabHost = getTabHost();
        TabHost.TabSpec spec;
        
        Resources res = getResources();
        
        textViewAppName = (TextView)findViewById(R.id.textViewAppName);   
        CheckStateAndStatus();
        
        intent = new Intent().setClass(this, ServerlistActivity.class);
        spec = tabHost.newTabSpec("serverlist").setIndicator(res.getText(R.string.indServerlist), res.getDrawable(R.drawable.ic_tab_server)).setContent(intent);
        tabHost.addTab(spec);
        
        intent = new Intent().setClass(this, ContactlistActivity.class);
        spec = tabHost.newTabSpec("contactlist").setIndicator(res.getText(R.string.indContactlist),res.getDrawable(R.drawable.ic_tab_contact)).setContent(intent);
        tabHost.addTab(spec);
        
        intent = new Intent().setClass(this, GroupManagementActivity.class);
        spec = tabHost.newTabSpec("chatlist").setIndicator(res.getText(R.string.indChatlist),res.getDrawable(R.drawable.ic_tab_chat)).setContent(intent);
		tabHost.addTab(spec);
		
		this.handler = new Handler();
    	this.handler.postDelayed(runnable,1000);    
    	
    	lastconnectedserverid = -1;          
        
        //anIMus 2.0 BA-Thesis
        //Added Receiving Content from Other Apps
        //see: http://developer.android.com/training/sharing/receive.html
        intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        	
        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
            	 // Receiving Content 
            	 // tabHost is set to Contactlist to share data
            	 tabHost.setCurrentTab(1); 
            	 EditText editTextNewMessage = (EditText)findViewById(R.id.editTextNewMessage);
            	 
            	 String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
            	    if (sharedText != null) {
            	    	IMApp app = (IMApp)getApplicationContext();
            	    	app.getServerManager().setShareMessage(sharedText);
            	    }

            	
            }
            
        } 
        else {
        	// otherwise app was started by Launcher 
        	// tabHost is set to Serverlist
        	tabHost.setCurrentTab(0); 
        	IMApp app = (IMApp)getApplicationContext();
        	//If Server is connected, switch to Contactlist.
        	if (app.getServerManager().getConnectedServer() != null)
        		tabHost.setCurrentTab(1); 
        }
    }
   
}
