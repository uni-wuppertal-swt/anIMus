package org.watzlawek.views.activities;

import org.watzlawek.R;
import org.watzlawek.models.Group;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class GroupChatActivity extends Activity {

	private int id;
	
	/**
	 * Initialize the activity at first start or resume
	 */
	@Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupchat);
        init();
        setTitle();
	}
	
	private void init(){
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
	}
	
	private void setTitle(){
		TextView tv = (TextView) findViewById(R.id.groupchat_tv_title);
        tv.setText(Group.getList().get(id).getTitle());
	}
	
	public void editButtonOnClick(View v){
		switch(v.getId()){
		case R.id.groupchat_bt_edit:
			Log.d("", "editButtonOnClick()");
			break;
		default:
			break;
		}
	}
	
	public void sendButtonOnClick(View v){
		switch(v.getId()){
		case R.id.groupchat_bt_send:
			Log.d("", "sendButtonOnClick()");
			break;
		default:
			break;
		}
	}
}