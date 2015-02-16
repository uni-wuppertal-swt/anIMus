package org.watzlawek;

import org.watzlawek.R;
import org.watzlawek.contactmanager.ContactDatabaseHandler;
import org.watzlawek.contactmanager.ContactEditActivity;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Class of the activity for displaying the contact list.
 * Contacts which are displayed in the contact list are downloaded from the specific instant messenger server by IMServer.
 * 
 * @author Klaus-Peter Watzlawek
 * @author Moritz Lipfert
 * 
 * @version 2013-09-17
 */
public class ContactlistActivity extends ListActivity {
	
	public ProgressBar progessbar;
	
	/**
	 * Costume List Adapter: Username, Icon/Avater, Status is shown
	 */
	private Adapter_Icon_Contacts listadapter;
	
	/**
	 * Handler used for refreshing the contactlist
	 */
	private Handler handler;	
	
	/**
	 * Holds the theme identifier.
	 * 
	 * @see AppPrefHandler#getTheme() Possible values of the theme identifier.
	 */
	private String theme;
	
	
	private Context context;
	
	/**
	 * Getter for the contact list element layout depending on the theme.
	 * The layout is stored in a XML file.
	 * 
	 * @return Layout identifier as an integer value.
	 */
	private int getContactlistElementLayout() {
		if(theme.equals("_light")) return R.layout.contactlistelementlight;
		else return R.layout.contactlistelementdark;
	}
	
	/**
	 * Getter for the contact list layout depending on the theme.
	 * The layout is stored in a XML file.
	 * 
	 * @return Layout identifier as an integer value.
	 */
	private int getContactlistLayout() {
		if(theme.equals("_light")) return R.layout.contactlistlight;
		else return R.layout.contactlistdark;
	}
	
	/**
	 * Creates the context menu based on a XML file.
	 * 
	 * @param menu Menu object which holds the menu based on the XML file.
	 * 
	 * @return True, when creation was successful.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.contactlistmenu, menu);
		return true;
	}
	
	
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	
    	IMApp app = (IMApp)getApplicationContext();
    	
    	switch(item.getItemId()) {
			case R.id.contactlistmenuStatus:
    			Intent intent = new Intent(this, StatusActivity.class);
    			startActivity(intent);    		
				return true;
    		//case R.id.contactlistmenuExit:
    			//finish();
    			//return true;
    		case R.id.contactlistmenuFriends:
    			
    			app.getAutoDiscover().progressbar = this.progessbar;
    			app.getAutoDiscover().findFriends();
    			return true;
    		case R.id.contactlistmenuCreate:
    			Intent intent2 = new Intent(this, org.watzlawek.contactmanager.ContactCreateActivity.class);   	    	
    	    	startActivityForResult(intent2, 0);  //
    	    	XMPPServer sv = (XMPPServer) app.getServerManager().getConnectedServer();
    	    	sv.clearRoster();
				sv.pullRoster();
				sv.pullContacts();
				refreshContactlist();
    			return true;
    		default:
    			return super.onOptionsItemSelected(item);
    	}
    }
    
    /**
     * Creates the context menu of the server list.
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
    	IMApp app =(IMApp)getApplicationContext();
    	
    	
    	if (!(app.getServerManager().getConnectedServer() == null || (app.getServerManager().getConnectedServer().isOffline()))) {     		
    		menu.add(ContextMenu.NONE, 1, ContextMenu.NONE, R.string.conactlistContextmenuValidate);   
    		menu.add(ContextMenu.NONE, 2, ContextMenu.NONE, R.string.conactlistContextmenuDelete);
    		menu.add(ContextMenu.NONE, 3, ContextMenu.NONE, R.string.conactlistContextmenuEdit);
    		menu.add(ContextMenu.NONE, 4, ContextMenu.NONE, R.string.conactlistContextmenuShow);
    	}
    } 
    
    @Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo contextMenuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		IMApp app =(IMApp)getApplicationContext(); // WICHTIG !!! APP CONTEXT
		
		AdapterView.AdapterContextMenuInfo cmi =
		        (AdapterView.AdapterContextMenuInfo) item.getMenuInfo ();
		
		XMPPChat ic = (XMPPChat) this.listadapter.getItem(cmi.position);
		XMPPServer sv = (XMPPServer) app.getServerManager().getConnectedServer();
		
		switch (item.getItemId()) {		
			//Validate
			case 1:		
				sv.validateBuddy(ic.get_jid());
				sv.pullRoster();
				sv.pullContacts();
				refreshContactlist();
				return true;
			//Deleted	
			case 2:				
				sv.deleteBuddy(ic.get_jid());
				// set visible false in DB
				//ContactDatabaseHandler cdbh = new ContactDatabaseHandler(context); 
				//cdbh.updateContact(ic.get_jid(), ic.get_username(), ic.get_note(), ic.get_serverId(), false);				
				//cdbh.close();
				app.getContactDatabasehandler().updateContact(ic.get_jid(), ic.get_username(), ic.get_note(), ic.get_serverId(), false);	
				//refreshContactlist();
				sv.clearRoster();
				sv.pullRoster();
				sv.pullContacts();
				refreshContactlist();
				//this.listadapter.notify();
				return true;
			// Edit
			case 3:
				Bundle intentPar = new Bundle();
    			intentPar.putString("jid", ic.get_jid());
    			intentPar.putString("name", ic.get_username());
    			intentPar.putString("note", ic.get_note());
				Intent intent3 = new Intent(this, org.watzlawek.contactmanager.ContactEditActivity.class); 
				intent3.putExtras(intentPar);
    	    	startActivityForResult(intent3, 0);
    	    	sv.clearRoster();
				sv.pullRoster();
				sv.pullContacts();
				refreshContactlist();
				return true;
			// Show
			case 4:
    			Bundle intentParameter = new Bundle();
    			intentParameter.putString("jid", ic.get_jid());
    			intentParameter.putString("name", ic.get_username());
    			intentParameter.putString("note", ic.get_note());
				Intent intent4 = new Intent(this, org.watzlawek.contactmanager.ContactShowActivity.class); 
				intent4.putExtras(intentParameter);
    	    	startActivityForResult(intent4, 0);
				return true;
			default: {
				return true;
			}
		}
		
    }
    
    
    
    /**
     * This method is called when the activity is resumed.
     * It loads the current contact list and the set theme on resume.
     */
    @Override
    public void onResume() {
    	super.onResume(); 

		AppPrefHandler appprefhandler = new AppPrefHandler(this);
		theme = appprefhandler.getTheme();
		
		setContentView(getContactlistLayout());		   	
		this.progessbar = (ProgressBar) findViewById(R.id.progresscontactlist);
    	ListView lv = getListView();
    	//lv.setOnItemClickListener(new OnItemClickListener() {
    	
    		/**
    		 * Opens the chat activity on item click. Keep in mind that the right right activity for the connected server has to be started.
    		 * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)
    		 */
    		/* public void onItemClick(AdapterView<?> av, View v, int position,
					long id) {
				IMApp app = (IMApp)getApplicationContext();
				//if (app.getServerManager().getConnectedServer() != null && app.haveNetworkConnection()) {
					String connectedServerType = app.getServerManager().getConnectedServer().getType();
					if(connectedServerType.equals("XMPP")) {
						Bundle intentParameter = new Bundle();
						intentParameter.putInt("user", position);
						Intent intent = new Intent(v.getContext(), ChatActivity.class);
						intent.putExtras(intentParameter);
						startActivityForResult(intent, 0);
					}
				//}
			}*/
    		
    	//});  
    	
    	registerForContextMenu(lv);

    	refreshContactlist();
    } 
    
    /**
     * Custom ArrayAdapter class for inserting contacts identified by IMChat objects into the contact list.
     * 
     * @author Klaus-Peter Watzlawek
     * @author Moritz Lipfert
     */
    private class Adapter_Icon_Contacts extends ArrayAdapter<IMChat> implements OnCreateContextMenuListener {
    	
    	/**
    	 * IMChat-Array of contacts downloaded from the connected instant messenger server.
    	 */
    	private IMChat[] mContactArray;    	
    	
    	/** 
    	 * Loads the reference of a pre-built contact array into mContactArray.
    	 * 
    	 * @param context The activity's context.
    	 * @param i Layout identifier for the contact list element layout.
    	 * @param ContactArray The Array of IMChat objects.
    	 */
        public Adapter_Icon_Contacts(Context context, int i, IMChat[] ContactArray) {
            super(context, i, ContactArray);            
            mContactArray = ContactArray;
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
         *  Creates a view for a contact which is display as an element in the contact list.
         *  It contains the contact's (nick-)name, the status and an icon which represents the status.
         *  
         *  @param position Position of a contact in the contact array.
         *  @param vs View of the contact list element.
         *  @param parent ViewGroup object which contains the elements views.
         *  
         *  @return Customized View which contains StatusIcon, ContactName, StatusText.
         */
        @Override
        public View getView(final int position, View vs, ViewGroup parent) {
        	View row = vs;
        	if (row == null) {
                LayoutInflater vi = (LayoutInflater)getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
                row = vi.inflate(getContactlistElementLayout(), null);                
            }                        
                   
            
            TextView tvContact = (TextView) row.findViewById(R.id.ContactlistelementContactTextView);
            TextView tvTextStatus = (TextView) row.findViewById(R.id.ContactlistelementStatusTextView);
            
            ImageView ivStatusIcon = (ImageView) row.findViewById(R.id.ContactlistelementStatusIconImageView);
            
            tvContact.setText(mContactArray[position].toString());     
            tvTextStatus.setText(mContactArray[position].get_textstatus());      
            
            ivStatusIcon.setImageDrawable(mContactArray[position].get_statusicon());
            
            //final IMChat messageitem = this.getItem(position);
            
            row.setOnClickListener(new View.OnClickListener() {
    			public void onClick(View v) {
    				IMApp app = (IMApp)getApplicationContext();
    				//if (app.getServerManager().getConnectedServer() != null && app.haveNetworkConnection()) {
    					String connectedServerType = app.getServerManager().getConnectedServer().getType();
    					if(connectedServerType.equals("XMPP")) {
    						Bundle intentParameter = new Bundle();
    						intentParameter.putInt("user", position);
    						Intent intent = new Intent(v.getContext(), ChatActivity.class);
    						intent.putExtras(intentParameter);
    						startActivityForResult(intent, 0);
    					}
    			}
            });
            
            row.setOnCreateContextMenuListener(this);	
                     
            return row;
        }
        
       

		public void onCreateContextMenu(ContextMenu menu, View v,
				ContextMenuInfo menuInfo) {
			menu.setHeaderTitle(R.string.conactlistContextmenuHeader);			
		}
    }     
    
    /**
     * Refreshs the displayed contact list based on a contact array gathered from the connected IMServer object.
     */
	public void refreshContactlist() {
    	IMApp app = (IMApp)getApplicationContext();
    	TextView tvEmptyList;
    	if(app.getServerManager().getConnectedServer() != null) { 
    		/*if (app.getServerManager().getConnectedServer().getContactsArray().length == 0) {
    			tvEmptyList = (TextView) findViewById(R.id.emptycontact);
    			tvEmptyList.setText(getText(R.string.contactlistisEmpty).toString());
    		}*/
    		
    		listadapter = new Adapter_Icon_Contacts(this,getContactlistElementLayout(),app.getServerManager().getConnectedServer().getContactsArray());
    		setListAdapter(listadapter);    		
    	}
    	else setListAdapter(null);    	
	}
	
	/**
	 * Runnable function Object to refresh the contact list every second, 
	 * interval is set to one second (1000ms)
	 */
	private final Runnable runnable = new Runnable() {
	    public void run()   {	    	
	    		handler.postDelayed(runnable,1000);
	    		if (listadapter != null) {
	    			listadapter.sort(null);
	    			listadapter.notifyDataSetChanged();	   	    			
	    		}
	    }
	};	
	
	/**
	 * onCreate process to start our timer which will refresh the contactlist
	 */
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	this.handler = new Handler();
    	this.handler.postDelayed(runnable,100); 
    }    
}
