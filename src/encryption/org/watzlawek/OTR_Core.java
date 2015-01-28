package encryption.org.watzlawek;

import java.security.KeyPair;
import java.security.SecureRandom;
import java.util.Vector;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import net.java.otr4j.OtrEngineImpl;
import net.java.otr4j.OtrPolicy;
import net.java.otr4j.session.SessionID;
import net.java.otr4j.session.SessionStatus;

import org.jivesoftware.smack.XMPPException;
import org.watzlawek.IMApp;
import org.watzlawek.IMChatMessageEvent;
import org.watzlawek.IMChatMessageListener;
import org.watzlawek.IMServer;
import org.watzlawek.R;
import org.watzlawek.XMPPServer;

import android.content.Context;
import android.os.Build.VERSION;
import android.util.Log;
import android.widget.Toast;

public class OTR_Core extends NullEncryption_Core implements Secure_Core {

	
	/**
	 * Cipher objects which defines the used encryption algorithm: AES 128-Bit.
	 */
	private Cipher cipher;
	

	
	/**
	 * Object which holds the secret key specification.
	 */
	private SecretKeySpec secretkeyspec;
	
	/**
	 * Object which holds a generated keypair consisting of a private and a public key.
	 */
	private KeyPair keypair;
	
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
	
	
	
	public OTR_Core() {
		// TODO Auto-generated constructor stub
	}

	public void init(Context con, Vector<JID> jid){
			
		//String in_seed, Context in_context) {
			context = con;
			try {
				KeyGenerator keygenerator = KeyGenerator.getInstance("AES");
				
				SecureRandom securerandom = null;
				// Jelly Bean workaround
				if (VERSION.SDK_INT >= 16) {
					securerandom = SecureRandom.getInstance("SHA1PRNG", "Crypto");
				}
				else {
					securerandom = SecureRandom.getInstance("SHA1PRNG");
				}
				
				securerandom.setSeed(seed.getBytes());
				keygenerator.init(128, securerandom);
				SecretKey key = keygenerator.generateKey();
				secretkeyspec = new SecretKeySpec(key.getEncoded(), "AES");
				cipher = Cipher.getInstance("AES");
			} catch (Exception e) {
				Log.v("Encryption", e.getMessage());
			}
		
		}
	
	}
	
/*
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
 */

/*
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

 */

/*
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

*/

/*
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

*/

/*

*/