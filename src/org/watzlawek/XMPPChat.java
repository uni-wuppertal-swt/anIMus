package org.watzlawek;

import java.util.StringTokenizer;

import net.java.otr4j.OtrException;
import net.java.otr4j.session.SessionID;
import net.java.otr4j.session.SessionStatus;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.packet.VCard;
import org.watzlawek.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

/**
 * This class is the backend for ChatActivity.
 * It holds all specific information of a XMPP contact like the Jabber ID.
 * Furthermore it implements the abstract methods from IMChat.
 * 
 * @author Klaus-Peter Watzlawek und Blub
 * @author Moritz Lipfert
 * 
 * @version 2013-09-10
 */
public class XMPPChat extends IMChat {
	
	/**
	 * False if OTR Request was sent, or True if OTR Request was received
	 */
	private boolean auto_otr;
	
	/**
	 * Locks only new information messages about secured connections
	 */		
	private boolean securedchatmessage;
	
	/**
	 * Avatar picture of the contact or null if none set.
	 */
	private byte[] avatar;
	
	/**
	 * XMPP related chat object.
	 */
	private Chat chat;
	
	/**
	 * aSmack related object which manages chat objects.
	 */
	private ChatManager chatmanager;
	
	/**
	 * Jabber ID of the contact.
	 */
	private String jid;	
	
	/**
	 * VCard of the contact with additional information about him.
	 */
	private VCard vcard;
	
	/**
	 * Needed for initial comparison with a server.
	 */
	private String phonenumber;
	
	
	/**
	 * Special Constructor for Auto Discover Service Account
	 * This listener will receive data from the Service Account and will start the process to validate the anIMa User account.
	 * @param in_context The application's context.
	 * @param in_jid The Jabber ID of the service account.
	 * @param connection aSmack connection object which represents the connected server.
	 * @param in_serverid The ID of the server in database.
	 * @param in_server The ID of the server in database.
	 */
	public XMPPChat(Context in_context, String in_jid, Connection connection, int in_serverid, XMPPServer in_server) {
		super(in_context, in_jid, IMServer.Status.available, in_serverid, in_server);
		jid = in_jid;
		final IMApp app = (IMApp)context.getApplicationContext();		
		chatmanager = connection.getChatManager();
		chat = chatmanager.createChat(jid, new MessageListener() {
			public void processMessage(Chat in_chat, Message in_message) {
				if(!in_message.getBody().equals("")) {	
					app.getAutoDiscover().doAuthtoken_request(in_message.getBody());					
				}
			}
		});
	}
	
	/**
	 * Default constructor of this class.
	 * It sets the Jabber ID and initializes the chat object.
	 * In addition to that the message listener will be initialized and the history will be read from database.
	 * 
	 * @param in_context The application's context.
	 * @param in_username The username of the contact.
	 * @param in_status The status of the user.
	 * @param in_jid The Jabber ID of the contact.
	 * @param connection aSmack connection object which represents the connected server.
	 * @param in_serverid The ID of the server in database.
	 * @param in_server The ID of the server in database.
	 */
	public XMPPChat(Context in_context, String in_username, IMServer.Status in_status, String in_jid, Connection connection, int in_serverid, XMPPServer in_server) {
		super(in_context, in_username, in_status, in_serverid, in_server);
		jid = in_jid;	
		auto_otr = false;
		final IMApp app = (IMApp)context.getApplicationContext();			 
		final Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		// Creates message listener and specifies the handling of new messages.
		chatmanager = connection.getChatManager();
		//Trigger for anon chat	
		chat = chatmanager.createChat(jid, new MessageListener() {
			public void processMessage(Chat in_chat, Message in_message) {
				if(!in_message.getBody().equals("")) {
					boolean sessiondown = false;
					boolean locked_for_OTR = false;
					
					//Strips HTML Tags
					in_message.setBody(removeHTML(in_message.getBody()));
					
					
					//anIMus 2.0 BA-Thesis AddOn OTR Behavior States
					if (app.getAppPrefHandler().getOTRBehaviourState().equals("_automatic")) {
						if ( otrRequestState < 3 && in_message.getBody().length() > 4) { // Auto OTR Accept Request REMOVED: !isOTREnabled() &&
							if (in_message.getBody().substring(0, 4).equals("?OTR") && !sessiondown) {
								locked_for_OTR = true;
								auto_otr = true;
								startOTRSession();		
								otrRequestState++;
							}  
						}
					} else if (app.getAppPrefHandler().getOTRBehaviourState().equals("_forced")) {
						if (otrRequestState < 3) {
							locked_for_OTR = true;
							auto_otr = true;
							startOTRSession();
							otrRequestState++;
						}
					}
					
					Log.v("otrRequestState", otrRequestState+"");
					
					//OTR Decryption				
					if (in_message.getBody() != null && isOTREnabled() ) {
						String oldmesg = in_message.getBody();
						in_message.setBody(null);
						String mesg = null;
						if (oldmesg.length() > 2) {						
							mesg = engine.transformReceiving(sessionID, oldmesg);						
						}
						
						if (isOTRFinished()) { //Clear all after OTR Session				
							stopOTRSession();
							sessiondown = true;		
							otrRequestState = 0;
						}
						
						if (isOTRSecured()  && isOTREnabled() ) {						
							in_message.setBody(mesg);
							remotefingerprint = encryption.getFingerprint(engine.getRemotePublicKey(sessionID));						
	                    } 
						else { // Accept encrypted OTR Messages / KeyExchange								
	                        	if (oldmesg.length() > 3 && oldmesg.substring(0, 4).equals("?OTR")  && !sessiondown) {     					
	                        		oldmesg = null;
	                        		if (!securedchatmessage) {
	                        			messages = messages + SystemMessageFormat(context.getText(R.string.chatOtrSecured).toString());                        			
	                        			securedchatmessage = true;
	                        		}
	                        		
	                        	} 
	                        	else 
	                        		oldmesg = null;                   
	                        	in_message.setBody(oldmesg);	                        	
	                    } 
						
					} 				
					
					if (!locked_for_OTR && !(in_message.getBody().trim()).equals(""))				
					messages = messages + MessageFormat(get_username(), in_message.getBody().toString(), "red", 0);				
						
					if(messageListener != null) messageListener.newMessage(new IMChatMessageEvent(this));
					else {
																		
						
						if(!unreadMessages && app.getAppPrefHandler().notificationsEnabled()) {
							Resources res = context.getResources();
							Notification notification = new Notification(R.drawable.ic_statusbar, res.getText(R.string.IMChatMessagePendingTicker), System.currentTimeMillis());
							
							Bundle intentParameter = new Bundle();
							intentParameter.putInt("mode", 1);
							intentParameter.putString("jid", get_jid());
							Intent intent = new Intent(context, ChatActivity.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP); 
							intent.putExtras(intentParameter);
							
							PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
							
							notification.setLatestEventInfo(context, res.getText(R.string.IMChatMessagePendingTitle), get_username() + " " + res.getText(R.string.IMChatMessagePendingText), pIntent);
							
							//LED Support						
							notification.ledARGB = app.getAppPrefHandler().getnotificationLEDLight(); // Color.argb(255, 0, 255, 0);
							notification.flags |= Notification.FLAG_SHOW_LIGHTS;
							notification.ledOnMS = 200;
							notification.ledOffMS = 300;
							
							NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
							notificationManager.notify(3133788, notification);							
						} 
						
						if (app.getAppPrefHandler().getnotificationAlarm().equals("_sound")  || app.getAppPrefHandler().getnotificationAlarm().equals("_soundandvibration")) {							
							Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
					        Ringtone r = RingtoneManager.getRingtone(context.getApplicationContext(), notification);
					        r.play();
						}
						
						if (app.getAppPrefHandler().getnotificationAlarm().equals("_vibration") || app.getAppPrefHandler().getnotificationAlarm().equals("_soundandvibration")) {					     
					        vibrator.vibrate(500);	
						}
						unreadMessages = true;
					}
				}
			}
			
		});
		
		/* Load VCard of the contact if avatars are enabled in settins. */
		AppPrefHandler apppref = new AppPrefHandler(context);
		
		if(apppref.avatarEnabled()) {
			vcard = new VCard();
			try{
				vcard.load(connection, jid);
				avatar = vcard.getAvatar();
			}
			catch(Exception e) {
				Log.v("VCard", e.getMessage());
			}
		}
		
		/* Load history from database. */
		ServerDatabaseHandler dbhandler = new ServerDatabaseHandler(context);		
		
		this.messagelog = dbhandler.getHistory(jid, server.getUser()+ "@" + server.getDomain(),30);	
		
	}
	
	/**
	 * Getter method for the Jabber ID.
	 * 
	 * @return Returns the Jabber ID of the contact.
	 */	
	public String get_jid() {
		return jid;
	}
	
	
	public int get_serverId() {
		return serverId;
	}
	
	/**
	 * Getter method for the username.
	 * 
	 * @return The username if set, else the Jabber ID as a fall back value.
	 */
	@Override
	public String get_username() {
		String name;
		
		if(username == null) {
			StringTokenizer stringtokenizer = new StringTokenizer(jid, "@");
			if(stringtokenizer.hasMoreTokens()) name = stringtokenizer.nextToken();
			else name = jid;
		}
		else name = username;
		
		if(unreadMessages) name += " (*)";
		
		return name;
	}
	
	
	/** Getter for the note to a contact.
	 * 
	 * @return the not as a String.
	 */
	public String get_note() {
		return note;
	};
	
	
	
	/**
	 * Getter method for the status icon depending on the contact's status.
	 * If avatars are enabled and the user has one set it will be returned instead.
	 * 
	 * @return The status icon or avatar as Drawable object.
	 */
	@Override
	public Drawable get_statusicon() {
		AppPrefHandler apppref = new AppPrefHandler(context);
		
		if((avatar != null) && (apppref.avatarEnabled())) {
			Drawable image = new BitmapDrawable(BitmapFactory.decodeByteArray(avatar, 0, avatar.length));
			return image;
		}
		else {
			Resources res = context.getResources();
			switch(status) {
				case available: {
					return res.getDrawable(R.drawable.status_green_available);
				}
				case away: {
					return res.getDrawable(R.drawable.status_yellow_away);
				}
				case dnd: {
					return res.getDrawable(R.drawable.status_red_dnd);
				}
				case extendedAway: {
					return res.getDrawable(R.drawable.status_yellow_away);
				}
				case freeToChat: {
					return res.getDrawable(R.drawable.status_green_available);
				}
				case unavailable: {
					return res.getDrawable(R.drawable.status_grey_offline);
				}
				default: {
					return res.getDrawable(R.drawable.status_green_available);
				}
			}
		}
	}
	
	/**
	 * Getter method for the contact's status as plain text.
	 * 
	 * @return The status as plain text.
	 */
	@Override
	public String get_textstatus() {
		Resources res = context.getResources();
		switch(status) {
			case available: {
				return res.getText(R.string.statusAvailable).toString();
			}
			case away: {
				return res.getText(R.string.statusAway).toString();
			}
			case dnd: {
				return res.getText(R.string.statusDnd).toString();
			}
			case extendedAway: {
				return res.getText(R.string.statusExtendedAway).toString();
			}
			case freeToChat: {
				return res.getText(R.string.statusFreeToChat).toString();
			}
			case unavailable: {
				return res.getText(R.string.statusUnavailable).toString();
			}
			default: {
				return res.getText(R.string.statusAvailable).toString();
			}
		}
	}
	
	
	/**
	 * Getter for the visibility.
	 * 
	 * @return The visibility for this contact (true, if visible).
	 */
	public boolean isVisible() {
		return visible;
	};
	
	
	/**
	 * Returns a string representation for the contact.
	 * The contact will be representated by it's username.
	 * 
	 * @return Username of the contact.
	 */
	@Override
	public String toString() {
		return get_username();
	}
	
	/**
	 * Sends a message to the contact's Jabber ID.
	 * 
	 * @param message The message to be sent.
	 */
	@Override
	public void send(String message) {	
		IMApp app = (IMApp)context.getApplicationContext();		
    	if( app.haveNetworkConnection() ) { // Auto Reconnect if try to send a message / verbuggt
    		if (!app.getServerManager().getConnectedServer().isOffline()) {
    			//int lastserver = app.getServerManager().getCurrentPosition();
    			//app.getServerManager().disconnect();
    			//app.killSystemService();
    			//app.getServerManager().connect(0); // verbuggt
    			//app.startSystemService();
    		
    		try {			
    			if (otrEnabled) {
					String newOTRmesg = engine.transformSending(sessionID, message);					
					chat.sendMessage(newOTRmesg);		
    			} else {    				
    				chat.sendMessage(message);	
    			}
    		}
    		catch(XMPPException e) {
    			Toast toast = Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG);
    			toast.show();
    		}
		
    		//Remove cryptic OTR Messages from Chat
    		if (!(message.length() > 3 && message.substring(0, 4).equals("?OTR"))) 			
    			messages = messages + MessageFormat(context.getText(R.string.IMChatMe).toString(), message, "blue", 1);
    		} else {
    			Toast toast = Toast.makeText(context, R.string.chatOfflineMode, Toast.LENGTH_LONG);
    			toast.show();
    		}    		
    	} 
    	else {
    		Toast toast = Toast.makeText(context, R.string.IMAppNoInternet, Toast.LENGTH_LONG);
			toast.show();			
    	}
	}
	
	/**
	 * Starts an OTR session.
	 * If there is no keypair for the server in database, new keys will be created.
	 */
	@Override
	public void startOTRSession(){
		if (!otrEnabled) { 			
			IMApp app = (IMApp)context.getApplicationContext();
			String ownJID = ((XMPPServer)app.getServerManager().getConnectedServer()).getConnection().getUser();	
			sessionID = new SessionID(ownJID, jid, "Scytale");			
			if (this.encryption.getKeyPairFromDB() == null) { 
				MakeAKeyPair(false); 
				messages = messages + SystemMessageFormat(context.getText(R.string.chatOtrKeyGen).toString());
			}
			ownfingerprint =  encryption.getFingerprint(getKeyPair(sessionID).getPublic());
			otrEnabled = true;	
			engine.startSession(sessionID);					
			securedchatmessage = false;
			if (auto_otr) messages = messages + SystemMessageFormat(context.getText(R.string.chatOtrAccept).toString());
			else {
				messages = messages + SystemMessageFormat(context.getText(R.string.chatOtrRequest).toString());
				refreshOTRSession();
				
			}
			if(messageListener != null) messageListener.newMessage(new IMChatMessageEvent(this));
			
			if (this.isOTRSecured()) {
				messages = messages + SystemMessageFormat(context.getText(R.string.chatOtrSecured).toString()); 
				securedchatmessage = false;			
			}
		}
	}
	
	@Override
	public void refreshOTRSession() {
		if (otrEnabled)
			this.engine.refreshSession(sessionID);
	}
	
	/**
	 * Kills an OTR session.
	 */
	@Override
	public void stopOTRSession() {		
		if (otrEnabled) { 
			otrEnabled = false;	
			auto_otr = false;
			securedchatmessage = false;
			messages = messages + SystemMessageFormat(context.getText(R.string.chatOtrEnded).toString());
			if(messageListener != null) messageListener.newMessage(new IMChatMessageEvent(this));
			if (engine.getSessionStatus(sessionID).equals(SessionStatus.ENCRYPTED)) {			
			engine.endSession(sessionID);			
			engine.endSession(sessionID);			
			otrRequestState = 0;
			}
		}
	}
	
	/**
	 * Stores the current message history in database.
	 */
	@Override
	public void storeHistory() {
		ServerDatabaseHandler dbhandler = new ServerDatabaseHandler(context);
		IMApp app = (IMApp)context.getApplicationContext();
		String ownID = app.getServerManager().getConnectedServer().getUser() + "@" +app.getServerManager().getConnectedServer().getDomain();
		//dbhandler.storeHistory(jid, "" + serverId, messages);
		if (this.messagelog.getLastMessageItem() != null)
			dbhandler.storeHistory(jid, ownID, this.messagelog.getLastMessageItem());
	}
	
	/**
	 * Clears History from only the contact with the choosen jid.
	 */
	@Override
	public void clearHistoryFromDB() {
		ServerDatabaseHandler dbhandler = new ServerDatabaseHandler(context);
		IMApp app = (IMApp)context.getApplicationContext();
		String ownID = app.getServerManager().getConnectedServer().getUser() + "@" +app.getServerManager().getConnectedServer().getDomain();		
		dbhandler.clearHistoryByUserID(jid, ownID);
	
	}
}
