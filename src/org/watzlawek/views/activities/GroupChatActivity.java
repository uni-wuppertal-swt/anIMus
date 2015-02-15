package org.watzlawek.views.activities;

import org.watzlawek.BubbleChatListAdapter;
import org.watzlawek.IMApp;
import org.watzlawek.IMChat;
import org.watzlawek.IMChatMessageEvent;
import org.watzlawek.IMChatMessageListener;
import org.watzlawek.MessageItem;
import org.watzlawek.R;
import org.watzlawek.models.Group;
import org.watzlawek.views.adapters.GroupChatAdapter;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class GroupChatActivity extends Activity {
	/**
	 * Handler object which is called by IMChat when a new message arrives.
	 * It is used to call the method updateMessages() which will show the new message inside the respective EditText object.
	 */
	private Handler updateMessageHandler;

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
	
	
	
	public void updateMessages() {				
		if (messageitem_same != currentGroupChat.getMessagelog().getLastMessageItem() ) 
		{
				if (currentGroupChat.getMessagelog().getLastMessageItem() != null)
				{					
					this.GAdapter.add(currentGroupChat.getMessagelog().getLastMessageItem());					
				}
			messageitem_same = currentGroupChat.getMessagelog().getLastMessageItem();	
		}
			
	}
	
	
	/** 					// ChatActivity
	 * This method gets called when the chat activity disappears.
	 * It clears the message history when the specific setting is set.
	 */
	@Override
	protected void onPause() 
	{
		currentGroupChat.setMessageListener(null);		
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
		if (app.getServerManager().getConnectedServer() != null && app.haveNetworkConnection()) 
		{
		currentGroupChat.setMessageListener(new IMChatMessageListener() {
			public void newMessage(IMChatMessageEvent message) {
				updateMessageHandler.post(new Runnable() {
					public void run() {
						updateMessages();
					}
				});
			}
		});
updateMessages();
		/* Removes notification from notification list. */
		NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancel(3133788);
		}
		this.onCreate(null);
	}
	
	/**
	 * Initialize the activity at first start or resume
	 */
	@Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupchat);
        init();
        setTitle();
        currentGroupChat.setMessageListener(new IMChatMessageListener() {
			public void newMessage(IMChatMessageEvent message) {
				updateMessageHandler.post(new Runnable() {
					public void run() {
						updateMessages();
					}
				});
			}
		});
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
