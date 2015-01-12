package org.watzlawek.views.activities;

import org.watzlawek.R;
import org.watzlawek.views.adapters.GroupchatAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

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
        
        Button button = (Button) findViewById(R.id.groupchat_send_button);
        button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
			}
		});
	}

}
