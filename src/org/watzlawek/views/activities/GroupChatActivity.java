package org.watzlawek.views.activities;

import org.watzlawek.R;
import org.watzlawek.models.Group;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class GroupChatActivity extends Activity {

	private Group group; 
	
	/**
	 * Initilize the activity at first start or resume
	 */
	@Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupchat);
        
        Intent intent = getIntent();
        int id = intent.getIntExtra("id", 0);
        
        Log.d("", "Intent erhalten: " + id + " - " + Group.getList().get(id).getTitle());
	}
}