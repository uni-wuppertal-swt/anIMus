package org.watzlawek.views.activities;

import org.watzlawek.R;
import org.watzlawek.views.adapters.GroupadminContactAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

/**
 * Activity to display the groupadministration.
 * 
 * @author Safran Quader
 * 
 * @version 2015-01-09
 */ 
public class GroupadminActivity extends Activity {
	
	@Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupadmin);

        GroupadminContactAdapter gAdapter = new GroupadminContactAdapter(getBaseContext());
        
        ListView listView = (ListView) findViewById(R.id.groupadmin_contactlist);
        listView.setAdapter(gAdapter); 
	} 
	
}
