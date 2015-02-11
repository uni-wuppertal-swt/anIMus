package encryption.org.watzlawek;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Vector;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import net.java.otr4j.OtrEngineImpl;
import net.java.otr4j.OtrPolicy;
import net.java.otr4j.io.OtrOutputStream;
import net.java.otr4j.session.SessionID;
import net.java.otr4j.session.SessionStatus;

import org.jivesoftware.smack.XMPPException;
import org.watzlawek.Encryption;
import org.watzlawek.IMApp;
import org.watzlawek.IMChatMessageEvent;
import org.watzlawek.IMChatMessageListener;
import org.watzlawek.IMServer;
import org.watzlawek.R;
import org.watzlawek.XMPPServer;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build.VERSION;
import android.util.Base64;
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


 	public void startOTRSession(){
/**
 		
 		if (!otrEnabled) { 			
			IMApp app = (IMApp)context.getApplicationContext();
			String ownJID = ((XMPPServer)app.getServerManager().getConnectedServer()).getConnection().getUser();	
			sessionID = new SessionID(ownJID, jid, "Scytale");			
			if (this.getKeyPairFromDB() == null) { 
				MakeAKeyPair(false); 
				messages = messages + SystemMessageFormat(context.getText(R.string.chatOtrKeyGen).toString());
			}
			ownfingerprint =  this.getFingerprint(getKeyPair(sessionID).getPublic());
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
		**/
	}

	/**
	 * Generates a DSA keypair for OTR message encryption.
	 * A new keypair for a connected server will be generated if there is no generated keypair yet or if the input parameter is set to true.
	 * If the input parameter is set to true and the app is not connected to a server an error message will be shown.
	 * The generated or retrieved (from database) keypair will stored in the keypair object of this class.
	 * 
	 * @param generatenew A new keypair for the connected server will be created if set to true.
	 *
	 * @see Encryption#keypair Keypair object which holds the generated keypair.
	 */
	public void MakeAKeyPair(boolean generatenew) {
		IMApp app = (IMApp)context.getApplicationContext();
		
		IMServer connectedServer = app.getServerManager().getConnectedServer();
		if(connectedServer != null ) {
			if (connectedServer.getKeypair() == null || generatenew) {	
				KeyPairGenerator kg = null;			
				try {
					kg = KeyPairGenerator.getInstance("DSA");

				} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();			
				}	
				keypair = kg.genKeyPair();					
				connectedServer.setKeypair(keypair);	
				
				if (generatenew) {
					Resources res = context.getResources();
					Toast toast = Toast.makeText(context, res.getText(R.string.chatOtrKeyGen), Toast.LENGTH_LONG);
					toast.show();
				}
			}
			else 
				keypair = connectedServer.getKeypair();
		}
		else {
			Resources res = context.getResources();
			Toast toast = Toast.makeText(context, res.getText(R.string.serverErrorNotConnectedTo), Toast.LENGTH_LONG);
			toast.show();
		}
	}

	/**
	 * Decodes Base64 encoded public and private key and generates a keypair object for the keys.
	 * 
	 * @param in_priv Base64 encoded private key.
	 * @param in_pub Base64 encoded public key.
	 * 
	 * @return Generated keypair object.
	 */
	public KeyPair getKeyPairFromBase64(String in_priv, String in_pub) {
		PKCS8EncodedKeySpec privSpec = new PKCS8EncodedKeySpec(Base64.decode(in_priv, Base64.DEFAULT));
		X509EncodedKeySpec pubSpec = new X509EncodedKeySpec(Base64.decode(in_pub, Base64.DEFAULT));
		
		PrivateKey privkey = null;
		PublicKey pubkey = null;
		
		try {
			KeyFactory keyfactory = KeyFactory.getInstance("DSA");
			privkey = keyfactory.generatePrivate(privSpec);
			pubkey = keyfactory.generatePublic(pubSpec);
		} 
		catch (Exception e) {
			Log.v("Encryption", e.getMessage());
		}
	
		return new KeyPair(pubkey, privkey);
	}

 	
	
	/**
	 * Getter for the keypair object.
	 * 
	 * @return The stored keypair or null if no keypair is stored yet.
	 */
	public KeyPair getKeyPair(){		
		return keypair;
	}
	
	/**
	 * This method returns a keypair for the connected server which is stored inside the local database.
	 * 
	 * @return The stored keypair retrieved from database.
	 */
	public KeyPair getKeyPairFromDB() {
		IMApp app = (IMApp)context.getApplicationContext();		
		IMServer connectedServer = app.getServerManager().getConnectedServer();
		if(connectedServer != null ) {					
			keypair = connectedServer.getKeypair();			
			return keypair;
		}
		else return null;
	}	

	/**
	 * This method generates the SHA-1 fingerprint of an given DSA public key. 
	 * 
	 * @param pubkey
	 * 
	 * @return A string which contains the SHA-1 fingerprint of a given DSA public key. 
	 */
	public String getFingerprint(PublicKey pubkey) {		
		final char[] hexchars = "0123456789ABCDEF".toCharArray();
		MessageDigest md = null;
		
		// Source take from src/net/java/otr4j/io/SerializationUtils.java		
		// public static byte[] writePublicKey(PublicKey pubKey) throws IOException 
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		OtrOutputStream oos = new OtrOutputStream(out);
		try {
			oos.writePublicKey(pubkey);			
			oos.close();
		} catch (IOException e) {			
			e.printStackTrace();
		}
		byte[] bytekey = out.toByteArray();
		byte[] trimmed = bytekey;
		
		if (pubkey.getAlgorithm().equals("DSA")) {
			trimmed = new byte[bytekey.length - 2];
			System.arraycopy(bytekey, 2, trimmed, 0, trimmed.length);
		}		
		
		try {
			md = MessageDigest.getInstance("SHA-1");		
			byte[] buf = md.digest(trimmed);			
			char[] chars = new char[2 * buf.length];
			
			for (int i = 0; i < buf.length; ++i) {
				chars[2 * i] = hexchars[(buf[i] & 0xF0) >>> 4];
				chars[2 * i + 1] = hexchars[buf[i] & 0x0F];
			}
			return new String(chars);	
		}
		catch(NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return new String("");
	}

	/**
	 * Groups a fingerprint (SHA-1 hash) into five parts with eight character per part.
	 * 
	 * @param fingerprint
	 * 
	 * @return The fingerprint grouped in five parts with eight character per part.
	 */
	public String MakeFingerprintHumanReadable(String fingerprint){
		String fingerprint_space = "";
		
		for (int i = 0; i < fingerprint.length(); i++) {			
			fingerprint_space += fingerprint.charAt(i);
			if ((i+1) % 8 == 0)	fingerprint_space += " ";
		}		
		return fingerprint_space;
	}

	/**
	 * Get the own internal fingerprint from own public key. This function is a bit smarter to call.
	 * @return The own fingerprint from public key grouped. 
	 */
	public String getOwnInternalFingerprint() {
		if (getKeyPairFromDB() != null)
			return MakeFingerprintHumanReadable(getFingerprint(getKeyPairFromDB().getPublic()));	
		return "NULL";
	}

	
	public void stopOTRSession() {		
/**
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
**/
	}

}