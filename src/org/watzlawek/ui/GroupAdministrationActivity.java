package org.watzlawek.ui;

import org.watzlawek.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
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
        
        String[] user = new String[]{"Benutzer 1", "Benutzer 2"};
        
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, user);
        ListView listView = (ListView) findViewById(R.id.GroupadministrationContactlist);
        listView.setAdapter(arrayAdapter); 
	}
	
}
