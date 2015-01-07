package org.watzlawek;

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

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import net.java.otr4j.io.OtrOutputStream;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build.VERSION;
import android.provider.Settings.Secure;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

/**
 * Main class for encryption related methods.
 * This class is used for the encryption and decryption of passwords to be stored in the local server database.
 * It also generates the DSA keypair for OTR encryption.
 * All key input parameters only accept Base64 encoded keys. If a key is returned by a method than it is encoded in Base64.
 * 
 * @author Klaus-Peter Watzlawek
 * @author Moritz Lipfert
 * 
 * @version 2012-11-14
 */
public class Encryption {
	/**
	 * Cipher objects which defines the used encryption algorithm: AES 128-Bit.
	 */
	private Cipher cipher;
	
	/**
	 * The context of the activity which creates an object of this class.
	 */
	private Context context;
	
	/**
	 * Object which holds the secret key specification.
	 */
	private SecretKeySpec secretkeyspec;
	
	/**
	 * Object which holds a generated keypair consisting of a private and a public key.
	 */
	private KeyPair keypair;
	
	/**
	 * Constructor which initializes an Encryption object with the Android ID as the passphrase.
	 * The Android ID is a random number generated at the first boot of the android operation system after a fresh install of Android.
	 * 
	 * @param in_context An activity's context.
	 */
	public Encryption(Context in_context) {
		this(Secure.getString(in_context.getContentResolver(), Secure.ANDROID_ID), in_context);
		keypair = null;
	}
	
	/**
	 * Constructor which initializes an Encryption object with a user-defined encryption passphrase.
	 * 
	 * @param in_seed User-defined passphrase.
	 * @param in_context An activity's context.
	 */
	public Encryption(String in_seed, Context in_context) {
		context = in_context;
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
			
			securerandom.setSeed(in_seed.getBytes());
			keygenerator.init(128, securerandom);
			SecretKey key = keygenerator.generateKey();
			secretkeyspec = new SecretKeySpec(key.getEncoded(), "AES");
			cipher = Cipher.getInstance("AES");
		} catch (Exception e) {
			Log.v("Encryption", e.getMessage());
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
	 * Decrypts a Base64 encoded and AES 128-Bit encrypted string.
	 * 
	 * @param input The encrypted string which has to be decrypted.
	 * 
	 * @return Decrypted string in plain text.
	 */
	public String decrypt(String input) {
		byte[] decInput = Base64.decode(input, Base64.DEFAULT);
		String plainText = "";
		
		try {
			cipher.init(Cipher.DECRYPT_MODE, secretkeyspec);			
			plainText = new String(cipher.doFinal(decInput));			
		} catch (Exception e) {
			Log.v("Encryption", e.getMessage());
		}
		return plainText;
	}
	
	/**
	 * Encrypts a String with AES 128-Bit and encode it with Base64.
	 * 
	 * @param input The string which has to be encrypted and encoded.
	 * 
	 * @return The encrypted and Base64 encoded string.
	 */
	public String encrypt(String input) {
		byte[] encInput = null;
		try {
			cipher.init(Cipher.ENCRYPT_MODE, secretkeyspec);
			encInput = cipher.doFinal(input.getBytes());
		} catch (Exception e) {
			Log.v("Encryption", e.getMessage());
		}
		return Base64.encodeToString(encInput, Base64.DEFAULT);
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
	
}
