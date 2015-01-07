package org.watzlawek;

import java.security.KeyPair;
import java.util.Collections;
import java.util.Vector;

import android.app.Application;
import android.content.Context;

import android.graphics.drawable.Drawable;

import android.os.Handler;
import android.util.Base64;

/**
 * This is an abstract base class for derived server classes.
 * It defines objects which hold basic connection parameters and method definitions for connection related functions.
 * An example for an implementation of a derived server class is XMPPServer.
 * 
 * @author Klaus-Peter Watzlawek
 * @author Moritz Lipfert
 * 
 * @version 2012-11-14
 */

public abstract class IMServer extends Application implements Comparable<IMServer>  {	
	
	/**
	 * Flag which indicates if the app is conntected to this server or not.
	 */
	protected boolean connected;
	
	/**
	 * Flag which indicates if the app is switched to offline mode or not.	 * 
	 */
	protected boolean offlinemode;
	
	/**
	 * Flag for indication that the server connected will be established with encryption enabled.
	 */
	protected boolean encryption;
	
	/**
	 * An activity's context.
	 */
	protected Context context;
	
	/**
	 * Dynamic list of contacts retrieved from the server.
	 */
	protected Vector<IMChat> contacts;
	
	/**
	 * The AutoDiscover Service Account for Registration and Validation of an anIMa account.
	 */
	protected IMChat autodiscover_serviceaccount;
	
	/**
	 * The port of the server.
	 */
	protected int port;
	
	/**
	 * Server ID of the server used as the primary key for storage inside the local SQLite database.
	 */
	protected int server_id;
	
	/**
	 * OTR keypair for encrypted messages.
	 */
	protected KeyPair keypair;
	
	/**
	 * Domain of the server.
	 */
	protected String domain;
	
	/**
	 * Password for login into the server.
	 */
	protected String password;
	
	/**
	 * Identifies the type of instant messaging server.
	 * 
	 * Possible values are:
	 * - xmpp
	 */
	protected String type;
	
	/**
	 * The username for server login.
	 */
	protected String user;
	
	/**
	 * Defines the possible user status'.
	 * 
	 * - available: User is online.
	 * - freeToChat: user is online and ready to chat.
	 * - away: User is away from keyboard.
	 * - extendedAway: User is away from keyboard for an extended time.
	 * - dnd: Do not disturb.
	 * - unavailable: The user is offline.
	 */
	public enum Status { available,  away, extendedAway, freeToChat, dnd, unavailable }
	
	protected Status savedStatus;
	
	/**
	 * Constructor for initialization of a new server objects base variables.
	 * 
	 * @param input_context The application's context.
	 * @param input_enc Encryption flag: True for SSL secured servers. Otherwise false.
	 * @param input_port The port for the server socket.
	 * @param input_id The primary key for object identification. This parameter has to be read from database.
	 * @param input_domain The server domain to connect to.
	 * @param input_password The user's password for login.
	 * @param input_type The server type, e.g. "xmpp".
	 * @param input_user The username for login.
	 * @param input_otrpriv Base64 encoded private key for OTR encryption.
	 * @param input_otrpub Base64 encoded public key for OTR encryption.
	 */
	public IMServer(Context input_context, boolean input_enc, int input_port, int input_id, String input_domain, String input_password, String input_type, String input_user, String input_otrpriv, String input_otrpub) {
		connected = false;
		context = input_context;
		encryption = input_enc;
		contacts = new Vector<IMChat>();
		port = input_port;
		server_id = input_id;
		domain = input_domain;
		password = input_password;
		type = input_type;
		user = input_user;
		savedStatus = Status.available;
		
		if(!input_otrpriv.equals("") && !input_otrpub.equals("")) {
			Encryption encHandler = new Encryption(context);			
			keypair = encHandler.getKeyPairFromBase64(input_otrpriv, input_otrpub);
			
		}
		else keypair = null;
	}
	
	/**
	 * Returns the dynamic contact list as a non-dynamic array.
	 * 
	 * @return The generated array of contacts.
	 */
	public IMChat[] getContactsArray() {
		Collections.sort(contacts);
		IMChat[] contactsArray = new IMChat[contacts.size()];
		for(int i = 0; i < contacts.size(); i++) {
			contactsArray[i] = contacts.elementAt(i);
		}
		return contactsArray;
	}
	
	/**
	 * Abstract getter for the server icon.
	 * 
	 * @return Server icon of type Drawable..
	 */
	public abstract Drawable getIcon();
	
	/**
	 * Abstract method for establishing the server connection.
	 * 
	 * @return Has to return a reference to this object.
	 */
	public abstract IMServer connect();
	
	/**
	 * Abstract getter for the connection status.
	 * 
	 * @return Connection status as plain text.
	 */
	public abstract String getConnectionStatus();
	
	/**
	 * Abstract definition of the toString method.
	 * 
	 * @return Returns a string representation for this server. 
	 */
	public abstract String toString();
	
	/**
	 * Abstract method for disconnecting the server connection.
	 */
	public abstract void disconnect();
	
	/**
	 * OfflineMode Method if internet connections dies	 * 
	 */
	public abstract void offlineMode();
	
	/**
	 * This abstract method creates the contact vector.
	 * The derived method has to load the contact roaster from the server and generate the contact list.
	 */
	public abstract void pullContacts();
	
	/**
	 * Sets the AutoDiscover Service Account for anIMa Account Registration and Validation.
	 * This account will send the validation link to anIMus.
	 */
	public abstract void setAutoDiscoverServiceAccount(String userid);	
	
	public IMChat getAutoDiscoverServiceAccount() {
		return this.autodiscover_serviceaccount;
	}
	
	
	/**
	 * This method has to clear the contacts vector.
	 * It has to get called on contact list refresh and on connection closing.
	 */
	public abstract void clearContacts();
	
	/**
	 * Sets the own status on the server.
	 * 
	 * @param status The status to be set.
	 * 
	 * @see Status Possible values of status.
	 */
	public abstract void setStatus(Status status);
	
	/**
	 * Gets the internal saved status.	 
	 * 
	 * @return saved status state.
	 */
	public Status getStatus() {
		return savedStatus;
	}
	
	/**
	 * Getter for the server domain.
	 * 
	 * @return Server domain as string.
	 */
	public String getDomain() {
		return domain;
	}
	
	/**
	 * Getter for the connected flag.
	 * 
	 * @return The connection flag:
	 * - True, if the connection is established.
	 * - False, if the connection is not established.
	 */
	public boolean getConnected () {
		return connected;
	}
	
	public abstract boolean isConnected();
	
	/**
	 * Getter for offlinemode flag.
	 * @return The offline mode flag:
	 * - True, if app was switched from online to offline mode.
	 * - False, if connection is disconnected or online.
	 */
	public boolean isOffline() {
		return offlinemode;
	}
	
	public void resetofflineMode() {
		this.offlinemode = false;
	}
	
	/**
	 * Getter for the encryption flag.
	 * 
	 * @return The encryption flag:
	 * - True, if encryption is enabled.
	 * - False, if encryption is not enabled.
	 */
	public boolean getEncryption() {
		return encryption;
	}
	
	/**
	 * Compares two IMServer objects.
	 * A server will be displayed in the server list above another server if server A's connection is established and server B's connection not.
	 * If their connection status' are equal the ordering is by domain in ascending order.
	 * 
	 * @param input The IMServer object which will be compared to this object.
	 * 
	 * @return Integer code:
	 * - -1: This object is lower than the input parameter object.
	 * - 0: Both objects are of same value for ordering.
	 * - 1: This object is greater than the input parameter object.
	 */
	public int compareTo(IMServer input) {
		if(connected && !input.getConnected()) return -1;
		else if(!connected && input.getConnected()) return 1;
		else return domain.compareTo(input.getDomain());
	}
	
	/**
	 * Returns the size of the contacts vector.
	 * 
	 * @return Size of contacts vector.
	 */
	public int getContactsSize() {
		return contacts.size();
	}
	
	/**
	 * Getter for port.
	 * @return Server port.
	 */
	public int getPort() {
		return port;
	}
	
	/**
	 * Getter for the unique server id.
	 * @return Server ID.
	 */
	public int getServer_id() {
		return server_id;
	}
	
	/**
	 * Getter for OTR keypair.
	 * @return KeyPair
	 */
	public KeyPair getKeypair() {
		return keypair;
	}
	
	/**
	 * Getter for user.
	 * @return The username.
	 */
	public String getUser() {
		return user;
	}
	
	/**
	 * Getter for password.
	 * @return The password.
	 */
	public String getPassword(){
		return password;
	}
	
	
	/**
	 * Setter for the password. Only needed for ServerDialog-Login.
	 * @param in_password
	 */
	public void setPassword(String in_password) {
		this.password = in_password;
	}
	
	/**
	 * Getter for the server type.
	 * @return The server type.
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * Getter for contacts.
	 * @return Contacts vector.
	 */
	public Vector<IMChat> getContacts() {
		return contacts;
	}
	
	/**
	 * Sets a new OTR keypair and saves it into the database table with Base64 encoding.
	 * @param in_kp OTR keypair
	 */
	public void setKeypair(KeyPair in_kp) {
		keypair = in_kp;
		
		String privKey = Base64.encodeToString(keypair.getPrivate().getEncoded(), Base64.DEFAULT);
		String pubKey = Base64.encodeToString(keypair.getPublic().getEncoded(), Base64.DEFAULT);
		
		//Log.e("privKey",privKey );
		//Log.e("pubKey",pubKey );
		
		ServerDatabaseHandler dbhandler = new ServerDatabaseHandler(context);
		dbhandler.updateKeypair("" + server_id, privKey, pubKey);
	}	
	
	public abstract void addNewBuddyToContact(String in_nameid, String nickname);
	
	public abstract void deleteBuddy(String in_nameid);
	
	public abstract void validateBuddy(String in_nameid);
	
}
