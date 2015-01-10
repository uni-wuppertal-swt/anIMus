package org.watzlawek.ui;

import org.watzlawek.GroupAdministrationContactAdapter;
import org.watzlawek.R;

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
public class GroupAdministrationActivity extends Activity {
	
	@Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupadministration);

        GroupAdministrationContactAdapter gAdapter = new GroupAdministrationContactAdapter(getBaseContext());
        
        ListView listView = (ListView) findViewById(R.id.GroupadministrationContactlist);
        listView.setAdapter(gAdapter); 
	} 
	
}
