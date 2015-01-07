package org.watzlawek;

import org.watzlawek.R;

import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Class of the activity for displaying the server list.
 * 
 * @author Klaus-Peter Watzlawek
 * @author Moritz Lipfert
 * 
 * @version 2012-11-14
 */
public class ServerlistActivity extends ListActivity {
	
	/**
	 * Costume server List Adapter: for Server, display name, state of connection und protocol icon
	 */
	private serverAdapter listadapter;
	
	/**
	 * Holds an identifier for the user defined theme.
	 * 
	 * @see AppPrefHandler#getTheme() Possible theme identifiers.
	 */
	private String theme;
	
	
	/**
	 * Handler used for refreshing the Serverlist
	 */
	private Handler handler;	
	
	/**
	 * Runnable function Object to refresh the Serverlist every second, 
	 * interval is set to one second (1000ms)
	 */
	private final Runnable runnable = new Runnable() {
	    public void run()   {	    	
	    	handler.postDelayed(runnable,1000);
	    	 //IMApp app = (IMApp)getApplicationContext(); 
	    	 //if (app.getServerManager().getConnectedServer() != null)
	    		// refreshList();	
	    	if (listadapter != null) 
	    		listadapter.notifyDataSetChanged();
	    }
	};
	
	/**
	 * Getter method for the server list element layout depending on the theme.
	 * 
	 * @return Layout identifier:
	 * - R.layout.serverlistelementlight when the light theme is chosen.
	 * - R.layout.serverlistelementdark when the dark theme is chosen.
	 */
	private int getServerlistElementLayout() {
		if(theme.equals("_light")) return R.layout.serverlistelementlight;
		else return R.layout.serverlistelementdark;
	}
	
	/**
	 * Getter for the server list layout depending on the theme.
	 * 
	 * @return Layout identifier:
	 * - R.layout.serverlistlight when the light theme is chosen.
	 * - R.layout.serverlistdark when the dark theme is chosen.
	 */
	private int getServerlistLayout() {
		if(theme.equals("_light")) return R.layout.serverlistlight;
		else return R.layout.serverlistdark;
	}
	
	/**
	 * Refreshs the server list and displays new or edited servers.
	 */
	private void refreshList() {  
		IMApp app = (IMApp)getApplicationContext();
    	if(app.getServerManager().getVectorServerArray().length > 0) {
    		listadapter = new serverAdapter(this, getServerlistElementLayout(), app.getServerManager().getVectorServerArray());
    		setListAdapter(listadapter);
    	}
    	else setListAdapter(null);  
	}
	
    /**
     * This method is called on resume of this activity.
     * It sets the theme the context menu on the list view and refreshs the server list.
     */
	@Override
    protected void onResume() {
    	super.onResume();
    	
    	IMApp app = (IMApp)getApplicationContext();
    	app.getServerManager().DatabaseEncryptionValidator();    	
    	
		AppPrefHandler appprefhandler = new AppPrefHandler(this);
		theme = appprefhandler.getTheme();
		
		setContentView(getServerlistLayout());
		
    	ListView listview = getListView();
    	listview.setOnCreateContextMenuListener(this);   	
    	
    	refreshList();  	
    }
	
	/**
	 * Defines what happens when a context menu item of the server list is clicked.
	 * 
	 * Possible actions are:
	 * - Connecting / Disconnecting to a server.
	 * - Editing the parameters of an existing server.
	 * - Deletes a server from server list.
	 * 
	 * @param item The selected menu item.
	 * 
	 * @return Returns true after successful execution.
	 */
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo contextMenuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		IMApp app =(IMApp)getApplicationContext();
		int selectedServer = (int)contextMenuInfo.id;
		
		switch (item.getItemId()) {
			// Connect
			case 1: {// No Connection was established since last start of the app, or it was completely disconnected
				if (app.getServerManager().getConnectedServer() == null) {		
					//only try to connect, if we got internet connection
					if( app.haveNetworkConnection() ) {
						//if the password is empty, ask for the password.
						//Log.v("PW-Crash = ", app.getServerManager().getVectorServerArray()[selectedServer].getPassword());
						if (!app.getServerManager().getVectorServerArray()[selectedServer].getPassword().equals("")) {						
							app.getServerManager().connect(selectedServer);	
							app.startSystemService();
							this.refreshList();
						}
						else {								
							/*if (app.getServerManager().getConnectedServer() != null) 
								if (app.getServerManager().getConnectedServer().isOffline())
									app.getServerManager().getConnectedServer().setPassword("");*/
							showLoginDialog(selectedServer);													
						}
						
					} else {
			    		Toast toast = Toast.makeText(app, R.string.IMAppNoInternet, Toast.LENGTH_LONG);
						toast.show();
					} 
				}
				else { // Connection is established...
					
					if (app.getServerManager().getConnectedServer() != null) {
						if (app.getServerManager().getConnectedServer().isOffline()) { // ...but aborted due network connection lost
							// before go from off to online mode, disconnect
							app.getServerManager().disconnect();
							app.killSystemService();
							if (!app.getServerManager().getVectorServerArray()[selectedServer].getPassword().equals("")) {															
								app.getServerManager().connect(selectedServer);	
								app.startSystemService();
							}
							else
								showLoginDialog(selectedServer);
						} 											
						else { // Regular Kill Connection, if we wanted to kill the connection
							app.getServerManager().disconnect();
							app.killSystemService();
						}
					}
					this.refreshList();
				}
				refreshList();	
				return true;
			}
			// Edit
			case 2: {
    			app.getServerManager().disconnect();
    			Bundle intentParameter = new Bundle();
    			intentParameter.putString("mode", "edit");
    			intentParameter.putInt("position", selectedServer);
    			Intent intent = new Intent(this, NewEditXMPPServerActivity.class);
    			intent.putExtras(intentParameter);
    			startActivityForResult(intent, 0);
				return true;
			}
			// Delete
			case 3: {				
				app.getServerManager().deleteServer(selectedServer);
				refreshList();
				return true;				
			}
			default: {
				return true;
			}
		}
	}
	
	/**
	 * Shows the Login dialog which asks for the password of the stored user account.
	 * It catches the password from a text field and will connect to the target server.
	 * @param selectedserver The id of the server we want to connect.
	 */
	private void showLoginDialog(final int selectedserver) {
		
        final Dialog dialog = new Dialog(this);
        final IMApp app =(IMApp)getApplicationContext();
        
        dialog.setContentView(R.layout.serverlogindialog);
        dialog.setTitle(R.string.serverloginDialogPassword);
        dialog.setCancelable(true);
        dialog.show();
        
        final TextView textViewPassword = (TextView) dialog.findViewById(R.id.serverLoginPassword);
        final CheckBox checkBoxSaveSession = (CheckBox) dialog.findViewById(R.id.serverLoginSaveForSession);
        
        
        //Connect Button which establishes the server connection with the fetched password from the dialog
        Button buttonConnect = (Button) dialog.findViewById(R.id.serverLoginConnect);  
       
        buttonConnect.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		app.getServerManager().getVectorServerArray()[selectedserver].setPassword(textViewPassword.getText().toString());
        		app.getServerManager().connect(selectedserver);	
        			if (app.getServerManager().getConnectedServer() != null) {
        				startService(new Intent(app, ServerManager.class));
        				//Save password only for this session? default false
        				if (!checkBoxSaveSession.isChecked()) 
        					app.getServerManager().getConnectedServer().setPassword("");				

        				dialog.dismiss();
        			}
        			else
        				app.getServerManager().getVectorServerArray()[selectedserver].setPassword("");
        		}
        });        
        
        //Cancel Button to close the dialog
        Button buttonCancel = (Button) dialog.findViewById(R.id.serverLoginCancel);    
        buttonCancel.setOnClickListener(new OnClickListener() {
        public void onClick(View v) {
        		dialog.dismiss();
            }
        });
        
	}
	
	/**
	 * Creates the preferences menu based on a specific XML file.
	 * 
	 * @return True on successful creation.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.serverlistmenu, menu);
		return true;
	}
	
	/**
	 * Specifies the preferences menu actions.
	 * 
	 * Menu actions are:
	 * - Adding a server to server list.
	 * - Disconnecting from a server.
	 * - Showing the app's preferences.
	 * - Exiting the application.
	 * 
	 * @return True, when action is successful executed.
	 */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()) {
    		case R.id.serverlistmenuNew:
    			IMApp app = (IMApp)getApplicationContext();
    			app.getServerManager().disconnect();
    			Bundle intentParameter = new Bundle();
    			intentParameter.putString("mode", "new");
    			Intent intent = new Intent(this, NewEditXMPPServerActivity.class);
    			intent.putExtras(intentParameter);   	    	
    	    	startActivityForResult(intent, 0);
    			return true;
    		case R.id.serverlistmenuDisconnect:
    			app = (IMApp)getApplicationContext();
    			app.getServerManager().disconnect();
    			app.killSystemService();
    			refreshList();
    			return true;
    		case R.id.serverlistmenuPrefs:
    			Intent intentPref = new Intent(this, AppPrefActivity.class);
    			startActivity(intentPref);
    			return true;
    		case R.id.serverlistmenuExit:
    			finish();
    			return true;
    		default:
    			return super.onOptionsItemSelected(item);
    	}
    }
    
    /**
     * Creates the context menu of the server list.
     */
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
    	IMApp app =(IMApp)getApplicationContext();
    	menu.setHeaderTitle(R.string.serverlistContextmenuHeader);
    	
    	if (app.getServerManager().getConnectedServer() == null || (app.getServerManager().getConnectedServer().isOffline()))    		
    		menu.add(ContextMenu.NONE, 1, ContextMenu.NONE, R.string.serverlistContextmenuConnect);
    	else 
    		menu.add(ContextMenu.NONE, 1, ContextMenu.NONE, R.string.serverlistContextmenuDisconnect);
    	menu.add(ContextMenu.NONE, 2, ContextMenu.NONE, R.string.serverlistContextMenuChange);
    	menu.add(ContextMenu.NONE, 3, ContextMenu.NONE, R.string.serverlistContextmenuDelete);
    }  
    
    /**
     * Custom ArrayAdapter class for inserting servers identified by IMServer objects into the server list.
     */
    private class serverAdapter extends ArrayAdapter<IMServer> {
    	
    	/**
    	 * IMServer-Array of servers retrieved from database.
    	 */
    	private IMServer[] mServerArray;    	
    	
    	/** 
    	 * Constructor for this custom ArrayAdapter.
    	 * 
    	 * @param context The activity's context.
    	 * @param i Layout identifier for the contact list element layout.
    	 * @param serverArray The Array of IMServer objects.
    	 */
        public serverAdapter(Context context, int i, IMServer[] serverArray) {
            super(context, i, serverArray);            
            mServerArray = serverArray;
        }
        
        /** 
         * Returns the ID of a contact in the contact list.
         * 
         * @param position Position of an item in the contact list. 
         * 
         * @return Item iD of an item in the contact list.
         */
        @Override
    	public long getItemId(int position) {
    		return position;
    	}
        
        /** 
         *  Creates a view for a server list element.
         *  It contains an icon depending on the connection status, the server domain and an information text for the server status.
         *  
         *  @param position Position of a server in the server array.
         *  @param v View of the server list element.
         *  @param parent ViewGroup object which contains the elements views.
         *  
         *  @return Customized View which contains an icon, the connection status and the server domain.
         */
        @Override
        public View getView(int position, View v, ViewGroup parent) {
            if (v == null) {
                LayoutInflater vi = (LayoutInflater)getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE); 
                v = vi.inflate(getServerlistElementLayout(), null);       
            }                        
            
            
            TextView TextViewServerName = (TextView) v.findViewById(R.id.ServerListelementServerNameTextView);
            TextView TextViewServerConnectionStatus = (TextView) v.findViewById(R.id.ServerListelementConnectionStatusTextView);
            
            ImageView ImageViewIcon = (ImageView) v.findViewById(R.id.ServerListelementServerIconImageView);
            
            TextViewServerName.setText(mServerArray[position].toString());     
            TextViewServerConnectionStatus.setText(mServerArray[position].getConnectionStatus());      
            
            ImageViewIcon.setImageDrawable(mServerArray[position].getIcon());
            
            return v;
        }
    }
    
    /**
	 * onCreate process to start our timer which will refresh the serverlist
	 */
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	this.handler = new Handler();
    	this.handler.postDelayed(runnable,1000);     	
    }
}
