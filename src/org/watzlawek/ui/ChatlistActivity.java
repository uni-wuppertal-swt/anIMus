package org.watzlawek.ui;

import org.watzlawek.R;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

/**
 * Activity to display the chatlist.
 * 
 * @author Safran Quader
 * 
 * @version 2015-01-08
 */
public class ChatlistActivity extends ListActivity{
	
	/**
	 * Creates the options menu if the menu button is clicked (<= API 10) or onCreate is called (> API 10).
	 * 
	 * @return true if creation succeeded
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_chatlist, menu);
		return true;
	}
	
	/**
	 * Handles click events from the options menu
	 * 
	 * @return true if else false
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.chatlistmenuNewGroup:
	        	Intent intent = new Intent(this, GroupAdministrationActivity.class);
	        	startActivity(intent);

	        	return true;
	        default:
	        	//return false
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	/**
	 * Initilizes the activity at first start or resume
	 */
	@Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatlistlight);
	}
	
}
