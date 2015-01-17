package org.watzlawek.views.activities;

import org.watzlawek.R;
import org.watzlawek.views.adapters.GroupchatAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

/**
 * Activity to display the groupchat
 * 
 * @author Safran Quader
 * 
 * @version 2015-01-12
 */
public class GroupchatActivity extends Activity {

	/**
	 * Initilize the activity at first start or resume
	 */
	@Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupchat);
        
        GroupchatAdapter groupchatAdapter = new GroupchatAdapter(getBaseContext());
        
        ListView listView = (ListView) findViewById(R.id.groupchat_listview);
        listView.setAdapter(groupchatAdapter); 
        
        Button sendbutton = (Button) findViewById(R.id.groupchat_send_button);
        sendbutton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
			}
		});
        Button editbutton = (Button) findViewById(R.id.groupchat_edit_button);
        editbutton.setOnClickListener(new OnClickListener() 
        {
			public void onClick(View v) {
				Intent intent = new Intent(GroupchatActivity.this, GroupadminActivity.class);
	        	startActivity(intent);
			}
		});
        
	}

}
