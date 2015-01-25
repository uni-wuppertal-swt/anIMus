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

import org.watzlawek.IMApp;
import org.watzlawek.IMChatMessageEvent;
import org.watzlawek.IMChatMessageListener;
import org.watzlawek.IMServer;
import org.watzlawek.R;
import org.watzlawek.XMPPServer;

import android.content.Context;
import android.os.Build.VERSION;
import android.util.Log;

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
	

