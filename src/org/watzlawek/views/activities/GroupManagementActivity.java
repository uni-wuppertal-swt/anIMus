package org.watzlawek.views.activities;

import java.util.ArrayList;

import org.watzlawek.R;
import org.watzlawek.models.Group;
import org.watzlawek.views.adapters.GroupManagerAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

public class GroupManagementActivity extends Activity{
	
	private ArrayList<Group> mItemList;
	 
	private GroupManagerAdapter mAdapter;
	private ListView mListView;
	
	/**
	 * Initilize the activity at first start or resume
	 */
	@Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupmanagement);
        
        getData();
        mAdapter = new GroupManagerAdapter(this, mItemList);
        mListView = (ListView) findViewById(R.id.chatmanager_lv_chatlist);
        
        mListView.setAdapter(mAdapter);
	}
	
	@Override
	protected void onRestart() {
	    super.onRestart();  
	    
	    mAdapter.notifyDataSetChanged();
	}
	
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
	        case R.id.chatlistmenuNewGroup:                               
	        	//start GroupadminActivity
	        	Intent intent = new Intent(this, GroupAdministrationActivity.class);                    
	        	startActivity(intent);

	        	return true;
	        default:
	        	//return false
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	public void getData(){
		mItemList = Group.getList();
	}
	
}
