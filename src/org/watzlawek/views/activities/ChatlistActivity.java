package org.watzlawek.views.activities;

import org.watzlawek.R;
import org.watzlawek.views.adapters.ChatlistAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * Activity to display the chatlist
 * 
 * @author Safran Quader
 * 
 * @version 2015-01-08
 */
public class ChatlistActivity extends Activity{
	
	//private String test="mal sehen";
	private int groupnumber=5;
	
	/**
	 * Creates the options menu if the menu button is clicked (<= API 10) or onCreate is called (> API 10).
	 * 
	 * @return true if creation succeeded
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
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
	        case R.id.chatlistmenuNewGroup:                                     // neue gruppe erstellen schickt auf Groupeditactivity
	        	Intent intent = new Intent(this, GroupadminActivity.class);
	       // 	intent.putExtra("testding", test);
	        	intent.putExtra("groupnr", groupnumber);                     
	        	startActivity(intent);

	        	return true;
	        default:
	        	//return false
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	/**
	 * Initilize the activity at first start or resume
	 */
	@Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatlist);
        
        //groupnumber= ...?                //anzahl gruppen?
        
        ChatlistAdapter chatlistAdapter = new ChatlistAdapter(getBaseContext());        
        ListView listView = (ListView) findViewById(R.id.chatlist_list_listview);
        listView.setAdapter(chatlistAdapter);
        listView.setOnItemClickListener(new OnItemClickListener() 
        {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				
				Intent intent = new Intent(ChatlistActivity.this, GroupchatActivity.class);
				intent.putExtra("groupnr", groupnumber);
	        	startActivity(intent);
			}
		});
	}
	
}
