package org.watzlawek;

import java.util.Vector;

//import org.jivesoftware.smack.packet.Message;
import org.watzlawek.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.ClipboardManager;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.method.TextKeyListener;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.KeyEvent;

/**
 * Class of the activity which shows the chat view.
 * It contains methods for displaying EditText objects which shows the sent and typed in messages.
 * For BA-Thesis it now includes chat bubble. Old vanilla log can be switch on in settings.
 * @author Klaus-Peter Watzlawek
 * @author Moritz Lipfert
 * 
 * @version 2013-09-10
 */
public class ChatActivity extends Activity {
	
	boolean enabledBubbleChat;
	/**
	 * EditText object which displays all messages sent and received so far.
	 */
	private TextView textViewMessages;
	
	/**
	 * EditText object which holds the message which is currently typed in and to be sent.
	 */
	private EditText editTextNewMessage;
	
	/**
	 * Handler object which is called by IMChat when a new message arrives.
	 * It is used to call the method updateMessages() which will show the new message inside the respective EditText object.
	 */
	private Handler updateMessageHandler;
	
	/**
	 * IMChat object which is linked to a specific contact.
	 * It is used to send and receive messages and to get further information like the contacts status.
	 */
	private IMChat currentChat;
	
	/**
	 * TextView object which displays the contacts (nick-)name above the EditText objects.
	 */
	private TextView textViewBuddyname;
	
	/**
	 * Saves the last message as MessageItem. Used to compare with new messages. 
	 */
	private MessageItem messageitem_same;	
	
	/**
	 * Saves the last message as string.  Used to compare with new messages.
	 */
	
	private String message_same;
	
	/** anIMus 2.0 BA-Thesis
	 * Listview to display new chat bubbles
	 */
	private ListView listViewChat;
	
	private BubbleChatListAdapter adapterBubbles;
	
	protected void prepareAdaptersAndMessage() {
		if (enabledBubbleChat) {        	       	
        	this.adapterBubbles = new BubbleChatListAdapter(getApplicationContext(), R.layout.chatlistitemdark);
        	this.listViewChat = (ListView) findViewById(R.id.ChatNewlistChatLog);
        	this.listViewChat.setAdapter(adapterBubbles);
        	this.listViewChat.setStackFromBottom(true);
        	this.listViewChat.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);   
        	registerForContextMenu(listViewChat);
        }
        else  {        	
            textViewMessages = (TextView)findViewById(R.id.textViewMessages);
         	textViewMessages.setMovementMethod(LinkMovementMethod.getInstance());
         	registerForContextMenu(textViewMessages);
         	textViewMessages.setText("");        	
        } 
	}
	
	/**
	 * This method is called on creation of the activity.
	 * It creates EditText- and Button-Elements and links this activity to the contacts IMChat object.
	 * Furthermore updateMessageHandler becomes initialized and the key listener for editTextNewMessage gets set.
	 *
	 * There are currently two methods to get the correct IMChat object for the activity call:
	 * - By array parameter for the contact array: This method is primarily used when the activity gets called on a click on a contact via contact list.
	 * - By Jabber ID: This method is the slower one and not to be used if the use of the first method is possible. It is primarily used on a click on an incoming notification.
	 * 
	 * @param savedInstanceState Represents a saved instance of the activity. This parameter is only used internally by the application.
	 */		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final IMApp app = (IMApp)getApplicationContext();
        
        /* Switch for Bubble Chat or anIMus 1.0 Stock Chat */
        AppPrefHandler appprefhandler = new AppPrefHandler(this);        
        enabledBubbleChat = appprefhandler.bubblesEnabled();
        message_same = "";   
        
        if (enabledBubbleChat) 
        	setContentView(R.layout.chatnewdark);      
        else  
        	setContentView(R.layout.chat);               	
        
        prepareAdaptersAndMessage();
       
        /* Initialize EditTexts and TextViews. */
       
        editTextNewMessage = (EditText)findViewById(R.id.editTextNewMessage);
        textViewBuddyname = (TextView)findViewById(R.id.textViewBuddyname);       
      
        updateMessageHandler = new Handler();
        
        /* Set a onKeyListener for sending a message by pressing enter on the virtual keyboard. */
        editTextNewMessage.setOnKeyListener(new View.OnKeyListener() {
        	
        	public boolean onKey(View v, int keyCode, KeyEvent event) {
        		 if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
        		     (keyCode == KeyEvent.KEYCODE_ENTER)) {       		
        			 
        			 String MsgInput = TidyUpStr(editTextNewMessage.getText().toString());       			
        			 
        			 if (!MsgInput.isEmpty() && MsgInput != "" ) {
        				if (app.getAppPrefHandler().getOTRBehaviourState().equals("_forced")) 
        					if (!currentChat.isOTREnabled())
        						currentChat.startOTRSession();
        				
     					currentChat.send(MsgInput);     					
     					//editTextNewMessage.setText("");   
     					TextKeyListener.clear(editTextNewMessage.getText());
     					updateMessages();
     					return true;
        			 }
        			
        		 }
        		 return false;
        	}
        });      
        
		
		Bundle intentParameter = getIntent().getExtras();
		
		/* Modes for setting the IMChat object:
		 * 0: Element in Array
		 * 1: Jabber ID
		 */
		switch(intentParameter.getInt("mode")) {
		case 0: {
			currentChat = app.getServerManager().getConnectedServer().getContacts().elementAt(intentParameter.getInt("user"));
			break;
		}
		case 1: {
			int i=0;
			Vector<IMChat> contacts = (Vector<IMChat>)app.getServerManager().getConnectedServer().getContacts().clone();
			while((currentChat == null) && i < contacts.size()) {
				if(((XMPPChat)contacts.elementAt(i)).get_jid().equals(intentParameter.getString("jid"))) currentChat = contacts.elementAt(i);
				i++;
			}
			break;
		}
		}
		
		/* Set the MessageListener for this activity on the IMChat object. */
		currentChat.setMessageListener(new IMChatMessageListener() {
			public void newMessage(IMChatMessageEvent message) {
				updateMessageHandler.post(new Runnable() {
					public void run() {
						updateMessages();
					}
				});
			}
		});
		
		textViewBuddyname.setText(currentChat.get_username());
		
		
		/* Removes notification from notification list. */
		NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancel(3133788);		
		
		if (currentChat.messagelog.getLastMessageItem() != null) {
			messageitem_same = currentChat.messagelog.getLastMessageItem();		
			if (enabledBubbleChat) {		
				for (int i = 0; i < currentChat.messagelog.getVectorMessage().size(); i++)
					this.adapterBubbles.add(currentChat.messagelog.getVectorMessage().get(i));
				
			}	
			else	
				textViewMessages.append(Html.fromHtml(currentChat.messagelog.getMessagesAsString()));
		}
		
		//anIMus 2.0 BA-Thesis Share Data
		  if (app.getServerManager().getSharedMessage() != null) {
	        	if(!app.getServerManager().getSharedMessage().equals("")) {
	        		editTextNewMessage.setText(app.getServerManager().getSharedMessage());		        		
	        		String MsgInput = TidyUpStr(editTextNewMessage.getText().toString());    		
	        		currentChat.send(MsgInput);	
	        		app.getServerManager().setShareMessage("");	 
	        		TextKeyListener.clear(editTextNewMessage.getText());
					updateMessages();
	        		}	        	
	        }
		  // Refresh State of OTR-Icon		  
		  this.setOTRIcon();
	}
	
	/**
	 * On Destroy method.
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	/**
	 * This method gets called when the chat activity disappears.
	 * It clears the message history when the specific setting is set.
	 */
	@Override
	protected void onPause() {
		AppPrefHandler apppref = new AppPrefHandler(this);
		if(apppref.clearHistoryOnClose()) {
			currentChat.flushMessages();
		}
		
		currentChat.setMessageListener(null);		
		super.onPause();
	}
	
	/**
	 * On resume re-set the MessageListener and gather new arrived messages.
	 */
	@Override
	protected void onResume() {
		super.onResume();
		IMApp app = (IMApp)getApplicationContext();	
		
		if (app.getServerManager().getConnectedServer() != null && app.haveNetworkConnection()) {
		currentChat.setMessageListener(new IMChatMessageListener() {
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
	 * Removes control characters and other invalid characters from a message.
	 * 
	 * @param input The message which is to be cleaned.
	 * 
	 * @return Cleaned message as a string.
	 */
	public String TidyUpStr(String input) {
		// Remove invalid chars
		String tidyString = input.replaceAll("[\u0000-\u001f]", "");
		
		// Make string object empty if it contains only whitespaces
		int counter = 0;		
		for (int i = 0; i < input.length(); i++) {
			if (input.charAt(i) == ' ' || input.charAt(i) == '\n')
				counter++;
		}	

		if (counter == input.length() )
			tidyString = "";
		
		return tidyString;
	}
	
	
	/**
	 * Sets the OTR indicator icon at the bottom of the activities view.
	 * If OTR is enabled and only if enabled a yellow lock with character R (Request) will be displayed.
	 * If OTR is enabled AND secured a green lock with character S (Secure) will be displayed.
	 * Otherwise a red lock with character O (Off) will be displayed.
	 */
	public void setOTRIcon() {
		ImageView ivOTR = (ImageView) findViewById(R.id.imageViewOTRStatus);
		
		
		if (currentChat.isOTREnabled()) {
			if (currentChat.isOTRSecured()) {
				ivOTR.setImageResource(R.drawable.ic_ico_otr_on);
				Log.v("OTR_GREEN", "ENABLED");
			}
			else {
				ivOTR.setImageResource(R.drawable.ic_ico_otr_request);
				Log.v("OTR_Yellow", "REQUEST");
			}
		}
		else {
			ivOTR.setImageResource(R.drawable.ic_ico_otr_off);			
		}
	}
	
	
	/**
	 * Sets the Messages EditText field to the current sent and received messages stored inside the IMChat object.
	 */
	public void updateMessages() {				
		//Stack Variante
		AppPrefHandler apppref = new AppPrefHandler(this);
		if (messageitem_same != currentChat.messagelog.getLastMessageItem() ) {
			enabledBubbleChat = apppref.bubblesEnabled();
			if (enabledBubbleChat) 
				if (currentChat.messagelog.getLastMessageItem() != null) {					
					this.adapterBubbles.add(currentChat.messagelog.getLastMessageItem());					
				}
			if(!apppref.clearHistoryOnClose()) {
				currentChat.storeHistory();				
			}
			messageitem_same = currentChat.messagelog.getLastMessageItem();	
		}
		if (!enabledBubbleChat) {
			if (!(currentChat.messagelog.getLastMessage()).isEmpty()){				
				
				if (!message_same.equals(currentChat.messagelog.getLastMessage())) {
					
					textViewMessages.append(Html.fromHtml(currentChat.messagelog.getLastMessage()));
					Log.v("MSG",message_same);
				}
				this.message_same = currentChat.messagelog.getLastMessage();
			}
		}
		this.setOTRIcon();		
	}
	
	/**
	 * Creates the context menu based on a XML file.
	 * 
	 * @param menu Menu object which holds the XML based menu.
	 * 
	 * @return True, when creation was successful.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.chatlistmenu, menu);
		return true;
	}
	
	/**
	 * Specifies the available menu actions.
	 * 
	 * Menu actions are:
	 * - Enable OTR: Enables OTR if the contacts client also support OTR message encryption.
	 * - Disable OTR: Disables an enabled OTR session.
	 * - Show Fingerprint: If OTR is enabled, the fingerprint of you and your friend are displayed
	 * @return True, when an action is successful executed.
	 */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) { 
    	
    	switch(item.getItemId()) {
    	case R.id.chatlistmenuOtrEnabled: 
    		if (!currentChat.isOTREnabled()) {    			
    			currentChat.startOTRSession();      			
    			setOTRIcon();	
    			item.setTitle(this.getText(R.string.chatlistmenuOtrDisabled));
    		}
    		else {
    			currentChat.stopOTRSession();
    			setOTRIcon();
    			item.setTitle(this.getText(R.string.chatlistmenuOtrEnabled));
    		}
    		return true;
    	case R.id.chatlistmenuOtrFingerprint:    		
    		if (currentChat.isOTREnabled()) {
    			AlertDialog alertDialog = new AlertDialog.Builder(this).create();
    			alertDialog.setTitle(this.getText(R.string.chatlistmenuOtrFingerprintAlertTitle).toString());
    			String myname = this.getText(R.string.IMChatMe).toString();
    			String friendsname = this.getText(R.string.IMChatBuddy).toString(); 
    			alertDialog.setMessage( myname + ": " 
    			+ currentChat.getOwnFingerprint() + "\n\n"
    			+ friendsname + ": " + currentChat.getRemoteFingerprint());
    			alertDialog.setButton(-2, "OK", new DialogInterface.OnClickListener() {
    			   public void onClick(DialogInterface dialog, int which) {
    			     //nothing to do or verify function in later versions of anIMus
    			   }
    			});
    			alertDialog.setIcon(R.drawable.ic_launcher);
    			alertDialog.show();      			
    		}
    		else {
    			Toast toast = Toast.makeText(this, this.getText(R.string.chatlistmenuOtrFingerprintToast).toString(), Toast.LENGTH_LONG);
    			toast.show();
    		}
			return true;
    	case R.id.chatlistmenuClearHistory:  
    		//Only for precaution
    		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
    		alertDialog.setTitle(this.getText(R.string.chatlistmenuClearHistoryAlertDialogTitle).toString());    			
    		alertDialog.setMessage(this.getText(R.string.chatlistmenuClearHistoryAlertDialogMessage).toString());
    		alertDialog.setIcon(R.drawable.ic_launcher);
    		alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, this.getText(R.string.AlertDialogYes).toString(), new DialogInterface.OnClickListener() {
    			public void onClick(DialogInterface dialog, int which) {
    				if (enabledBubbleChat) {
    					adapterBubbles.clear();    			
    				} else {
    					textViewMessages = (TextView)findViewById(R.id.textViewMessages);
    					textViewMessages.setText(""); 
    				}
    				currentChat.flushMessages();
    				currentChat.clearHistoryFromDB();   
    				prepareAdaptersAndMessage();
    			}
    		});
    		alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, this.getText(R.string.AlertDialogNo).toString(), new DialogInterface.OnClickListener() {
    			public void onClick(DialogInterface dialog, int which) {/* nothing to do */}		
    		});
    		alertDialog.show();
    		return true;
    	default:
    		return super.onOptionsItemSelected(item);
    		
    	}	
    }
    /**
     * Create the context menu for copying the current chat log to the clipboard.
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
    	super.onCreateContextMenu(menu, view, menuInfo);
    	menu.setHeaderTitle(R.string.chatContextMenuChatLog);  
        menu.add(ContextMenu.NONE, 1, ContextMenu.NONE, R.string.chatContextMenuChatCopy);
        menu.add(ContextMenu.NONE, 2, ContextMenu.NONE, R.string.chatContextMenuBubbleChatMessageDetails); 
    }

    /**
     * Register for selected bubble list item the context menu commands AND get the text from the chat log for stock version.
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
    	if (!enabledBubbleChat) {
    		((ClipboardManager) getSystemService(CLIPBOARD_SERVICE)).setText(textViewMessages.getText());    	
    		return true;
    	}    	
    	//anIMus 2.0 BA-Thesis 
    	//AddOn: ContextMenu for Bubble List Items
    	AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
    	MessageItem msgitem = this.adapterBubbles.bubblesList.get(info.position);
    	switch(item.getItemId()) { 
    		case 1:
    			String copy_message = msgitem.ShowMessagePlain();
    			((ClipboardManager) getSystemService(CLIPBOARD_SERVICE)).setText(copy_message);       			 
    			return true;
    		case 2:    			
    			String message_timestamp = msgitem.ShowFriendlyTimeStamp();
    			String message_username = msgitem.ShowUsernamePlain();
    			AlertDialog alertDialog = new AlertDialog.Builder(this).create();
    			alertDialog.setTitle(this.getText(R.string.chatContextMenuChatMessageDetailstAlertTitle).toString());    			
    			alertDialog.setMessage(
    					this.getText(R.string.chatContextMenuChatMessageDetailsTimestamp).toString() + " "+ message_timestamp + 
    					"\n\n" +
    					this.getText(R.string.chatContextMenuChatMessageDetailsUsername).toString() + " " + message_username);
    			
    			alertDialog.setButton(-2, "OK", new DialogInterface.OnClickListener() {
    			   public void onClick(DialogInterface dialog, int which) {    			    
    			   }
    			});
    			alertDialog.setIcon(R.drawable.ic_launcher);
    			alertDialog.show();
    			
    			return true;
    		default:
    			return true;
    	}    	
    }
}
