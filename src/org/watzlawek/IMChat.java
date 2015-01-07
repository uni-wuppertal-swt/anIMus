package org.watzlawek;

import java.security.KeyPair;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.java.otr4j.OtrEngineHost;
import net.java.otr4j.OtrEngineImpl;

import net.java.otr4j.OtrPolicy;
import net.java.otr4j.OtrPolicyImpl;
import net.java.otr4j.session.SessionID;
import net.java.otr4j.session.SessionStatus;

import android.content.Context;
import android.graphics.drawable.Drawable;

import android.text.format.DateFormat;
import android.util.Log;


/**
 * Abstract class which holds the data for a contact. This includes the username, the status of the user and the sent and received messages.
 * Furthermore some objects for OTR message encryption are included.
 * This class has to be derived when a new instant messaging protocol should be added to anIMus.
 * 
 * @author Klaus-Peter Watzlawek
 * @author Moritz Lipfert
 * 
 * @version 2013-09-10
 */
public abstract class IMChat implements Comparable<IMChat>, OtrEngineHost {	
	/**
	 * This objects defines how OTR requests should be treated.
	 */
	protected OtrPolicy policy;	
	
	/**
	 * This objects holds the reference to the object with the methods that define the OTR handling.
	 */
	protected OtrEngineImpl engine;	
	
	/**
	 * This object holds a unique session ID for the chat.
	 */
	protected SessionID sessionID; 	
	
	/**
	 * Flag for determination that OTR is enabled or not.
	 */
	protected boolean otrEnabled;
	
	/**
	 * Flag for determination that the chat is secured with OTR.
	 */
	protected boolean securedchat;
	
	/**
	 * Flag for determination that there are unread messages pending.
	 */
	protected boolean unreadMessages;
	
	/**
	 * Object for holding the applications context.
	 */
	protected Context context;
	
	/**
	 * IMChatMessageListener object which is called when new messages arrive.
	 */
	protected IMChatMessageListener messageListener;
	
	/**
	 * Holds the user's current status.
	 */
	protected IMServer.Status status;
	
	/**
	 * The server ID of the server in database;
	 */
	protected int serverId;
	
	/**
	 * String which stores all messages sent and received in this session.
	 */
	protected String messages; // DEPRECATED old anIMus 1.0
	
	/**
	 * Holds the complete messages.
	 */
	protected MessageLog messagelog;
	
	/**
	 * Holds the user's (nick-)name.
	 */
	protected String username;
	
	/**
	 * Give access to the IMServer Object.
	 */
	protected IMServer server;
	
	/**
	 * Holds the remote fingerprint from a given public key of a chat partner.
	 */
	protected String remotefingerprint;
	
	/**
	 * Holds the own fingerprint from a given public key.
	 */
	protected String ownfingerprint;
	
	/**
	 * This object holds a keypair for secure chatting via OTR.
	 */
	protected KeyPair keypair;
	
	/**
	 * Encryption class instance used for gathering the OTR keypair.
	 */
	protected Encryption encryption;
	
	/**
	 * State:  OTR is enabled and a request to start a session was omitted.
	 * 0: started
	 * 1: request pending
	 * 2: started secured session
	 */
	protected int otrRequestState; 	 
	
	/**
	 * Method for getting the current timestamp.
	 * The format is Hour:Minutes:Seconds, e.g. 10:13:37.
	 * 
	 * @return Timestamp as a string.
	 */
	public String currentTimeStamp() {
		//Date d = new Date();	
		//CharSequence timestamp  = DateFormat.format("kk:mm:ss", d.getTime());
		
		//anIMus 2.0 BA-Thesis New TimeStamp Format
		SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String timestamp = s.format(new Date());
		Log.v("format",timestamp);
		return timestamp.toString();		
	}	
	
	/**
	 * Constructor which initializes the basic objects of this abstract class.
	 * 
	 * @param in_context The activity's context.
	 * @param in_username Nickname of a contact.
	 * @param in_status The current status of the user.
	 * @param in_serverid The ID of the server in database.
	 * @param in_server The ID of the server in database.
	 */
	public IMChat(Context in_context, String in_username, IMServer.Status in_status, int in_serverid, IMServer in_server) {
		context = in_context;
		messages = "";
		status = in_status;
		unreadMessages = false;
		username = in_username;
		policy = new OtrPolicyImpl(OtrPolicy.ALLOW_V2| OtrPolicy.ERROR_START_AKE);
		engine = new OtrEngineImpl(this);
		encryption = new Encryption(context);
		otrEnabled = false;	
		securedchat = false;
		keypair = encryption.getKeyPairFromDB();
		ownfingerprint = "NULL";
		remotefingerprint = "NULL";
		serverId = in_serverid;
		messagelog = new MessageLog();
		server = in_server;
		otrRequestState = 0;
	}
	
	/**
	 * Compares two IMChat objects. The users status' will be compared first and then their usernames.
	 * Users with status online will be above users with status away or disconnected.
	 * If two users status are equal then their sorting will be ascending by username.
	 * 
	 * @param input The IMChat object which will be compared to this object.
	 * 
	 * @return Integer code:
	 * - -1: This object is lower than the input parameter object.
	 * - 0: Both objects are equal.
	 * - 1: This object is greater than the input parameter object.
	 */
	public int compareTo(IMChat input) {
		if(status.equals(input.get_status())) return get_username().compareTo(input.get_username());
		else return status.compareTo(input.get_status());
	}
	
	/**
	 * Returns the keypair for the connected server and the current session ID.
	 * At the moment this method is not fully implemented.
	 * So it will only return the stored keypair for the connected server from the database.
	 * 
	 * @param in_SessionID Session ID of the chat session.
	 * 
	 * @return KeyPair The keypair for the connected server.
	 */
	public KeyPair getKeyPair(SessionID in_SessionID) {
		return encryption.getKeyPairFromDB();
	}
	
	/**
	 * Returns the fingerprint from a given public key of a remote chat partner.
	 * 
	 * @return String The string contains the fingerprint of the remote chat partner.
	 */
	public String getRemoteFingerprint() {
		if (remotefingerprint != "NULL")
			return encryption.MakeFingerprintHumanReadable(remotefingerprint);
		return "---";
	}
	
	/**
	 * Returns the fingerprint from the own public key.
	 *  
	 * @return String The string contains the own fingerprint.
	 */
	public String getOwnFingerprint() {
		return encryption.MakeFingerprintHumanReadable(ownfingerprint);
	}
	
	/**
	 * Generates an OTR keypair for the connected server if there is no keypair stored in database yet.
	 * The reference to the keypair will be set to the variable "keypair".
	 * 
	 * @param mode True, when a the stored keypair shoud be replaced by a new one. Most of the time false is the right choice.
	 */
	public void MakeAKeyPair(boolean mode) {		
		encryption.MakeAKeyPair(mode);		
		keypair = encryption.getKeyPair();		
	}
	
	/**
	 * Returns the value of the policy object.
	 * 
	 * @return policy Set OTR policy.
	 */
	public OtrPolicy getSessionPolicy(SessionID in_SessionID) {		
		return policy;
	}

	/**
	 * Method for starting an OTR session. The OTR request will be sent to the contact.
	 * 
	 * @param in_SessionID Session ID of the current chat session.
	 * @param in_message OTR request message.
	 */
	public void injectMessage(SessionID in_SessionID, String in_message) {					
		send(in_message);		
	}

	/**
	 * OTR error function which is not implemented yet.
	 * Nothing will be done in this method.
	 * 
	 * @param in_SessionID Session ID of the current chat session.
	 * @param in_error OTR error message.
	 */
	public void showError(SessionID in_SessionID, String in_error) {
	}

	/**
	 * OTR warning function which is not implemented yet.
	 * Nothing will be done in this method.
	 * 
	 * @param in_SessionID Session ID of the current chat session.
	 * @param in_warning OTR warning message.
	 */
	public void showWarning(SessionID in_SessionID, String in_warning) {	
	}
	
	/**
	 * Defines the function to store the current message history in database.
	 */
	public abstract void storeHistory();
	
	/**
	 * Getter for sent and received messages.
	 * 
	 * @return Sent and received messages.
	 */
	public String get_messages() {
		return messages;
	}
	
	/**
	 * Getter for the user's status.
	 * 
	 * @return User status of type IMServer.Status.
	 */
	public IMServer.Status get_status() {
		return status;
	}
	
	/**
	 * Abstract getter for the username.
	 * 
	 * @return The username as a string.
	 */
	public abstract String get_username();
	
	/**
	 * Abstract getter for the status icon or avatar if enabled.
	 * The icon depends on the user's status and if the user has an avatar set.
	 * 
	 * @return The status icon or avatar for the user.
	 */
	public abstract Drawable get_statusicon();
	
	
	/**
	 * Abstract getter for the status as plain text.
	 * 
	 * @return The status as plain text.
	 */
	public abstract String get_textstatus();
	
	/**
	 * Returns a string representation for a contact.
	 * This method is abstract and has to be overwritten in the derived classes.
	 */
	@Override
	public abstract String toString();
	
	/**
	 * Abstract method for sending a message to the contact.
	 * 
	 * @param message The message to send.
	 */
	public abstract void send(String message);
	
	/**
	 * Sets a new message listener and marks unreadMessages as false.
	 * 
	 * @param input Object with implemented IMChatMessageListener interface.
	 */
	public void setMessageListener(IMChatMessageListener input) {
		unreadMessages = false;
		messageListener = input;
	}
	
	/**
	 * Sets a new status when the contact's status changes.
	 * 
	 * @param status The new status of the contact.
	 */
	public void setStatus(IMServer.Status status) {
		if(this.status != status) {
			this.status = status;
			if(messageListener != null) {							
				messages = messages + StatusChangeMessageFormat(get_username(), this.get_textstatus());
				messageListener.newMessage(new IMChatMessageEvent(this));
			}
		}
	}
	
	/**
	 * Sets new username when it changes.
	 * 
	 * @param name The new username.
	 */
	public void setUsername(String name) {
		username = name;
	}
	
	/**
	 * Abstract method to start an OTR session.
	 */
	public abstract void startOTRSession(); 		
	
	
	/**
	 * Abstract method to stop a started OTR session.
	 */
	public abstract void stopOTRSession(); 
	
	/**
	 * Abstract method to refresh a OTR session.
	 */
	public abstract void refreshOTRSession();
		
	/**
	 * Getter for otrEnabled.
	 * 
	 * @return True, if OTR is enabled. Otherwise false.
	 */
	public boolean isOTREnabled() {
		return otrEnabled;		
	}
	
	/**
	 * Reports if OTR has been successfully switched to encrypted mode
	 * @return true if OTR Session is established and everything is encrypted
	 */
	public boolean isOTRSecured() {
		return engine.getSessionStatus(sessionID).equals(SessionStatus.ENCRYPTED);	
	}
	
	/**
	 * Reports the OTR state.
	 * 
	 * @return True, if the OTR session is closed. Otherwise false.
	 */
	public boolean isOTRFinished() {
		return engine.getSessionStatus(sessionID).equals(SessionStatus.FINISHED);
	}
	
	/**
	 * Strips HTML tags from a string.
	 * 
	 * @param input The string from which the tags should be stripped.
	 * 
	 * @return String with stripped HTML tags.
	 */
	public String removeHTML(String input) {
		return input;
	}
	
	/**
	 * Deletes all messages
	 */
	public void flushMessages() {
		this.messages = "";
		this.messagelog.clearMessageRAM();			
	}
	
	/** anIMus 2.0 Upgrade
	 * Colourizes the incoming or outgoing message and adds a timestamp in front of it.	 * 
	 * @param username The user's nickname.
	 * @param message The message to be formatted.
	 * @param color The colour the message should be set to.
	 * @param leftside Orientation of bubble. left 0, center 1  or right 2
	 * @return Formatted message with colour and timestamp.
	 */
	public String MessageFormat(String username, String message, String color, int leftside) {
		/*String output = "<font color='"+ color + "'>" + "(" + currentTimeStamp() + ") "
				+ "<b>" + username + "</b>"+ ": " + "</font><font color='black'>" + MakeLinksClickAble(EscapeHTMLFromMsg(message)) + "</font>" + "<br>";
		Log.v("MessageLog", output);
		return output;*/
		String tmp_msg ="<font color='black'>" + MakeLinksClickAble(EscapeHTMLFromMsg(message)) + "</font> <br>";
		String tmp_stamp = "<font color='"+ color + "'>" + "(" + currentTimeStamp() + ") </font>";
		String tmp_usr = "<font color='"+ color + "'>" + "<b>" + username + "</b>"+ ": </font>";
				
		this.messagelog.addMessage(tmp_msg, tmp_stamp, tmp_usr, leftside);
		return "";
	}
	
	/**
	 * Formats status change messages colour and timestamp.
	 * 	
	 * @param in_user The user who changed the status.
	 * @param in_status The user's new status.
	 * 
	 * @return The formatted message.
	 */
	public String StatusChangeMessageFormat(String in_user, String in_status) {
		String output = "<font color='black'>" + "(" + currentTimeStamp() + ") "
				+ "<b>" + in_user + " " + context.getText(R.string.chatNewStatus).toString() + " " + in_status + ".</b></font><br>";
		return output;
	}
	
	/**
	 * Formats system messages like OTR status messages with colour and timestamp.
	 * 	
	 * @param message The message which should be formatted.
	 * 
	 * @return The formatted message.
	 */
	public String SystemMessageFormat(String message) {
		/*String output = "<font color='black'>" + "(" + currentTimeStamp() + ") "
				+ "<b>" + "anIMus" + "</b>"+ ": " + "</font><font color='black'>" + message + "</font>" + "<br>";
		return output;*/
		
		String tmp_msg ="<font color='black'>" + MakeLinksClickAble(EscapeHTMLFromMsg(message)) + "</font> <br>";
		String tmp_stamp = "<font color='black'>" + "(" + currentTimeStamp() + ") </font>";
		String tmp_usr = "<font color='black'>" + "<b>" + "anIMus" + "</b>"+ ": </font>";
		this.messagelog.addMessage(tmp_msg, tmp_stamp, tmp_usr, 2);
		return "";
	}
	
	/**
	 * Sets the message string to a specific value.
	 * 
	 * @param in_message The value the message string should be set to.
	 */
	public void setMessages(String in_message) {
		messages = in_message;
	}	
	
	/**
	 * Substitutes HTML Code from a string to html entities representation.
	 * Futher this functions escapes html code to prevent html code injections.
	 * 
	 * @param input A string which may contains html code.
	 * 
	 * @return A cleared string which consists the right html entities.
	 */
	public String EscapeHTMLFromMsg(String input) {		
		String res = input;		
		res = res.replaceAll("<", "&lt;");
		res = res.replaceAll(">", "&gt;");				
		return res;	
	}
	

	/**
	 * Matches a input string with a regular expression pattern.
	 * 
	 * @param input A String to match with the pattern.
	 * 
	 * @param pattern A regular expression pattern.
	 * 
	 * @return true if pattern matches with string.
	 */
	private static boolean match_expression(String input, String pattern) {
        try {
        	Pattern p = Pattern.compile(pattern);
            Matcher matcher = p.matcher(input);
            return matcher.matches();
        } 
        catch (RuntimeException e) {
        	return false;
        } 
	}
	
	/**
	 * This function makes hyperlinks clickable.
	 * The input will be parsed and converted to right html format.
	 */
	private String MakeLinksClickAble(String input) {
		String res = input;
		String regex = "\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";	       
	    
		//String matches a hyperlink format
		if (match_expression(input,regex)) {
			res = "<a href ='" + res + "'>"+ res+ "</a>";
			return res;
		}				
			
		return res;
	}
	
	public void clearHistoryFromDB() {
	}
	
}
