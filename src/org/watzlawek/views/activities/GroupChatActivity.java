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

import android.widget.ListView;

import android.widget.Button;

import android.widget.TextView;

/**
 * Zeigt den Gesprächsverlauf eines Gruppenchats an und ermölicht es Nachrichten an eine Gruppe zu versenden.
 * 
 * @author Karsten Klaus
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
	public void updateMessages() {				
//		if (messageitem_same != currentGroupChat.getMessagelog().getLastMessageItem() ) 
//		{
	//			if (currentGroupChat.getMessagelog().getLastMessageItem() != null)
		//		{					
			//		this.GAdapter.add(currentGroupChat.getMessagelog().getLastMessageItem());					
				//}
		//	messageitem_same = currentGroupChat.getMessagelog().getLastMessageItem();	
	//	}
			
	}
	
	
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
		IMApp app = (IMApp)getApplicationContext();
	
	}
	
	protected void prepareAdaptersAndMessage()  
	{
		GAdapter = new GroupChatAdapter(getApplicationContext(), R.layout.chatlistitemdark);
        	this.listViewChat = (ListView) findViewById(R.id.ChatNewlistChatLog);
        	this.listViewChat.setAdapter(GAdapter);
        	this.listViewChat.setStackFromBottom(true);
        	this.listViewChat.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);   
        	registerForContextMenu(listViewChat);
        
	}
	
	
	/**
	 * Initialize the activity at first start or resume
	 */
	@Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        prepareAdaptersAndMessage();
        setContentView(R.layout.activity_groupchat);
        init();
       
	}
	
	
	/**
	 * 
	 */
	private void init(){
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        
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
			break;
		default:
			break;
		}
	}
}
