package org.watzlawek.views.activities;

import org.watzlawek.IMApp;
import org.watzlawek.MessageItem;
import org.watzlawek.R;
import org.watzlawek.models.Group;
import org.watzlawek.views.adapters.GroupChatAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;

import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import android.widget.Button;

import android.widget.TextView;

/**
 * Zeigt den Gesprächsverlauf eines Gruppenchats an und ermölicht es Nachrichten an eine Gruppe zu versenden.
 * 
 * @author Karsten Klaus
 * @author Safran Quader
 * 
 * @version 2015-02-16
 *
 */

public class GroupChatActivity extends Activity {
	
	/**
	 * Handler object which is called by IMChat when a new message arrives.
	 * It is used to call the method updateMessages() which will show the new message inside the respective EditText object.
	 */
	private Handler updateMessageHandler;
	
	private ListView listViewChat;
	private EditText editText;

	private int id;
	/**
	 * Saves the last message as MessageItem. Used to compare with new messages. 
	 */
	private MessageItem messageitem_same;
	/**
	 * IMChat object which is linked to a specific contact.
	 * It is used to send and receive messages and to get further information like the contacts status.
	 */
	private Group currentGroupChat;
	
	private GroupChatAdapter GAdapter;
	
	
	/**
	 * 
	 */
	
	
	
	/** 					// ChatActivity
	 * This method gets called when the chat activity disappears.
	 * It clears the message history when the specific setting is set.
	 */
	@Override
	protected void onPause() 
	{
	//	currentGroupChat.setMessageListener(null);		
		super.onPause();
	}
	


	/**                                  
	 * On resume re-set the MessageListener and gather new arrived messages.
	 */
	@Override
	protected void onResume() 
	{
		super.onResume();
	
	}
	
	
	
	
	/**
	 * Initialize the activity at first start or resume
	 */
	@Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupchat);
        init();
        listViewChat = (ListView) findViewById(R.id.groupchat_lv_conversation);
        //GAdapter=new GroupChatAdapter(getbaseContext(),);
        listViewChat.setAdapter(GAdapter);
        editText = (EditText) findViewById(R.id.groupchat_et_message);
        
	}
	
	
	/**
	 * 
	 */
	private void init(){
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
     //   Toast toast = Toast.makeText(getBaseContext(), String.valueOf(id), 10);
     //   toast.show();
        currentGroupChat=Group.getList().get(id) ;
        Button bt = (Button) findViewById(R.id.groupchat_bt_edit);
        bt.setTag(id);
	}
	
	/**
	 * 
	 */
	private void setTitle(){
		TextView tv = (TextView) findViewById(R.id.groupchat_tv_title);
        tv.setText(Group.getList().get(id).getTitle());
	}
	
	/**
	 * 
	 * @param v
	 */
	public void editButtonOnClick(View v){
		int groupID = (Integer) v.getTag();

		switch(v.getId()){
		case R.id.groupchat_bt_edit:
			Intent intent = new Intent(this, GroupAdministrationActivity.class);
			intent.putExtra(GroupAdministrationActivity.MODE, GroupAdministrationActivity.MODE_EDIT);
			intent.putExtra(GroupAdministrationActivity.GROUP_ID, groupID);
			startActivity(intent);
			break;
		default:
			break;
		}
	}
	
	/**
	 * 
	 * @param v
	 */
	public void sendButtonOnClick(View v){
		switch(v.getId()){
		case R.id.groupchat_bt_send:
			Log.d("", "sendButtonOnClick()");  
			Group.getList().get(id).getMessagelog().addMessage(editText.getText().toString(), "time", "name",0);
			    Toast toast = Toast.makeText(getBaseContext(), currentGroupChat.getMessagelog().getLastMessageItem().getMessage(), 10);
		        toast.show();
			break;
		default:
			break;
		}
	}
}
