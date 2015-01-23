package org.watzlawek;

import java.util.Collection;

import org.jivesoftware.smack.AndroidConnectionConfiguration;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.RosterPacket;
import org.jivesoftware.smackx.ServiceDiscoveryManager;
import org.watzlawek.R;
import org.watzlawek.contactmanager.ContactDatabaseHandler;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

/**
 * This class extends IMServer and represents a XMPP server.
 * It contains XMPP specific methods for getting a contacts status, reading the roaster from server and connecting / disconnecting.
 * 
 * @author Klaus-Peter Watzlawek
 * @author Moritz Lipfert
 * 
 * @version 2013-09-10
 */
public class XMPPServer extends IMServer {
	
	NetworkConnectThread connThread;
	
	/**
	 * aSmack connection object for server communication.
	 */
	private Connection connection;
	
	/**
	 * List of contacts of type RosterEntry.
	 */
	private Collection<RosterEntry> rosterEntries;
	
	/**
	 * The whole contact list, read from server.
	 */
	private Roster roster;	
	
	/**
	 * Gets the user's status by Jabber ID.
	 * 
	 * @param in_jid Jabber ID of the contact.
	 * 
	 * @return The status of the contact.
	 */
	private IMServer.Status getUserStatus(String in_jid) {
		Presence.Mode status = roster.getPresence(in_jid).getMode();
		Presence.Type type = roster.getPresence(in_jid).getType();
		
		if(type.equals(null) || type.equals(Presence.Type.available)) {
			if(status == null || status.equals(Presence.Mode.available)) return IMServer.Status.available;
			else if(status.equals(Presence.Mode.away)) return IMServer.Status.away;
			else if(status.equals(Presence.Mode.chat)) return IMServer.Status.freeToChat;
			else if(status.equals(Presence.Mode.dnd)) return IMServer.Status.dnd;
			else return IMServer.Status.extendedAway;
		}
		else return IMServer.Status.unavailable;
	}
	
	/**
	 * Gets the roster from server.
	 * A roster listener will be set for updating a contact's status on change.
	 */
	public void pullRoster() {		
		if(connected) roster = connection.getRoster();
		roster.addRosterListener(new RosterListener() {
			public void presenceChanged(Presence presence) {
				updateContacts();
			}

			public void entriesAdded(Collection<String> arg0) {
				
			}

			public void entriesDeleted(Collection<String> arg0) {
				
			}

			public void entriesUpdated(Collection<String> arg0) {
				updateContacts();
			}
		});
	}
	
	/**
	 * Updates every contacts status and name.
	 */
	private void updateContacts() {
		if(connected) {
			rosterEntries = roster.getEntries();
			for(RosterEntry entry : rosterEntries) {
				for(int i = 0; i < contacts.size(); i++) {
					if(((XMPPChat)contacts.elementAt(i)).get_jid().equals(entry.getUser())) {
						contacts.elementAt(i).setStatus(getUserStatus(entry.getUser()));
						contacts.elementAt(i).setUsername(entry.getName());
					}
				}
			}
			// If contacts in cDB should be updated regularely, the compareTo() should be calle here ------------------------------------------
		}
	}
	
	
	/**
	 * Constructor of this class.
	 * It calls the constructor of the upper class and set connection and roster to null.
	 * 
	 * @param input_context The application's context object.
	 * @param input_enc Encryption flag. True, when the server supports TLS / SSL encryption.
	 * @param input_port The port of the server.
	 * @param input_id The primary key for object identification.
	 * @param input_domain The server domain.
	 * @param input_password The user's password.
	 * @param input_type The server type.
	 * @param input_user The username for login.
	 * @param input_otrpriv Base64 encoded private key for OTR message encryption.
	 * @param input_otrpub Base64 encoded public key for OTR message encryption.
	 */
	public XMPPServer(Context input_context, boolean input_enc, int input_port, int input_id, String input_domain, String input_password, String input_type, String input_user, String input_otrpriv, String input_otrpub) {
		super(input_context, input_enc, input_port, input_id, input_domain, input_password, input_type, input_user, input_otrpriv, input_otrpub);
		connection = null;
		roster = null;
		offlinemode = false;
		connThread = new NetworkConnectThread();		
	}
	
	/**
	 * Getter method for the XMPP connection object.
	 * 
	 * @return connection XMPP connection object.
	 */
	public Connection getConnection() {
		return connection;
	}
	
	/**
	 * Checks if the smack xmpp connection is still established
	 * @return true if client is connected to a server, otherwise false.
	 */
	@Override
	public boolean isConnected() {
		return this.connection.isConnected();
	}
	
	/**
	 * Getter method for the server icon.
	 * If the application is connected to this server a coloured icon will be returned.
	 * Otherwise the icon is black and white.
	 * 
	 * @return Server icon as Drawable object.
	 */
	public Drawable getIcon() {
		Resources res = context.getResources();
		if(connected) return res.getDrawable(R.drawable.jabber_connected);
		else return res.getDrawable(R.drawable.jabber_disconnected);
	}
	
	/**
	 * This method establishes the XMPP connection.
	 * If the server supports TLS / SSL encryption the data exchange will be compressed too.
	 * 
	 * After connecting the roaster will be pulled from server.
	 * 
	 * @return Returns a reference to this object.
	 */
	@Override
	public IMServer connect() {			
		try {	//hier ersetzen		
			connThread.init(domain, port, user, password, context, encryption);			
			connThread.loader();		
			//connThread.loader_thread_alternative();
			connection = connThread.getConnectionfromThread();	
		
		} 
		catch (Exception e) {
			//Log.v("NetworkConnectThread", e.toString());
		}

		if(connection != null) {
			connected = true;
			
			// I hope this fixes the non complete rosters.
			try {
				Thread.sleep(2000);
			}
			catch(Exception e) {}
			//while(!connection.isConnected()) {} useless	
			pullRoster();
			pullContacts();
			setStatus(Status.available);
			offlinemode = false;
			return this;
		}
		else {
			return null;
		}
	}
	
	
	public int getServerId() { return server_id; }
	/**
	 * Getter method for the connection status.
	 * 
	 * @return Connection status as plain text.
	 */
	@Override
	public String getConnectionStatus() {
		Resources res = context.getResources();
		if(connected) return res.getText(R.string.IMServerConnected).toString();
		else return res.getText(R.string.IMServerDisconnected).toString();
	}
	
	/**
	 * Method which returns a string identifier of an object of this class.
	 * An object of this class will be identified by it's user and domain.
	 * 
	 * @return Returns a string as an object identifier.
	 */
	@Override
	public String toString() {
		return domain + ": " + user;
	}
	
	/**
	 * Method for disconnecting from a server.
	 */
	@Override
	public void disconnect() {
		if(connected == true) {
			//NetworkDisconnectThread disconnThread = new NetworkDisconnectThread();
			//disconnThread.execute(new Connection[] {connection});
			connThread.kill();
			connection = null;
			connected = false;
			offlinemode = false;
			clearContacts();
		}
	}
	
	/**
	 * Method for switching to offline mode.
	 */
	@Override
	public void offlineMode() {
		if(connected == true) {
			//NetworkDisconnectThread disconnThread = new NetworkDisconnectThread();
			//disconnThread.execute(new Connection[] {connection});
			connThread.kill();
			connected = false;
			offlinemode = true;					
		}
	}	
	
	
	/**
	 * This method adds every contact to the contacts vector.	
	 */
	@Override
	public void pullContacts() {
		contacts.clear();
		if(roster != null) {			
			rosterEntries = roster.getEntries();
			for(RosterEntry entry : rosterEntries) {
				contacts.add(new XMPPChat(context, entry.getName(), getUserStatus(entry.getUser()), entry.getUser(), connection, server_id,this));
			}
			
			ContactDatabaseHandler cdbh = new ContactDatabaseHandler(context);
			cdbh.compareContacts(contacts, server_id);
			contacts = cdbh.getVisibleContacts(contacts, server_id);
			cdbh.close();
		}		
	}	
	
	/**
	 * This method clears the contacts vector.
	 */
	@Override
	public void clearContacts() {
		if(roster != null) {
			contacts.clear();
		}
	}
	
	/**
	 * Sends a packet to the XMPP server which sets the own status.
	 * 
	 * @param status The status to be set.
	 */
	@Override
	public void setStatus(Status status) {
		Presence presence;
		switch(status) {
			case available: {
				presence = new Presence(Presence.Type.available);
				presence.setMode(Presence.Mode.available);				
				break;
			}
			case away: {
				presence = new Presence(Presence.Type.available);
				presence.setMode(Presence.Mode.away);
				break;
			}
			case dnd: {
				presence = new Presence(Presence.Type.available);
				presence.setMode(Presence.Mode.dnd);
				break;
			}
			case extendedAway: {
				presence = new Presence(Presence.Type.available);
				presence.setMode(Presence.Mode.xa);
				break;
			}
			case freeToChat: {
				presence = new Presence(Presence.Type.available);
				presence.setMode(Presence.Mode.chat);
				break;
			}
			default: {
				return;
			}
		}
		if(connected) { 
			connection.sendPacket(presence);
			savedStatus = status;
		}
	}

	@Override
	public void setAutoDiscoverServiceAccount(String userid) {		
		this.autodiscover_serviceaccount = new XMPPChat(context, userid, connection, server_id,this);
	}

	@Override
	public void addNewBuddyToContact(String in_nameid, String nickname) {
		Roster.setDefaultSubscriptionMode(Roster.SubscriptionMode.accept_all);
		Roster roster = connection.getRoster();
		try {
			roster.createEntry(in_nameid,nickname,null);
			Log.v(in_nameid, nickname);
			//Make subscribtion
			Presence subscribe = new Presence(Presence.Type.subscribe);
			subscribe.setTo(in_nameid);
			connection.sendPacket(subscribe);
		} catch (XMPPException e) {
			Log.v("XMPPException_createEntry", e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	//taken from http://stackoverflow.com/questions/14317580/delete-a-friend-from-roster-in-xmpp-openfire
	public void deleteBuddy(String in_nameid) {
		RosterPacket packet = new RosterPacket();
		packet.setType(IQ.Type.SET);
		RosterPacket.Item item  = new RosterPacket.Item(in_nameid, null);
		item.setItemType(RosterPacket.ItemType.remove);
		packet.addRosterItem(item);
		connection.sendPacket(packet);	
		connection.sendPacket(packet);	
	}

	@Override
	public void validateBuddy(String in_nameid) {
		Presence subscribed = new Presence(Presence.Type.subscribed);
		subscribed.setTo(in_nameid);
		connection.sendPacket(subscribed);
		
	}
	
	public void clearRoster() {
		this.contacts.clear();
		this.roster.reload();
	}
		
}
