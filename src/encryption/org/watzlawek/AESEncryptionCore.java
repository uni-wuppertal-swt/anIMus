package encryption.org.watzlawek;

import org.jivesoftware.smack.packet.Message;
import android.content.Context;
import android.os.Build.VERSION;
import android.util.Log;
import java.io.UnsupportedEncodingException;
import android.widget.Toast;


import java.security.SecureRandom;
import java.util.Vector;
import java.util.Random;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class AESEncryptionCore extends NullEncryption_Core implements Secure_Core  {


	
	public AESEncryptionCore() {
		// TODO Auto-generated constructor stub
	}

	private Context context;
	public String getid() {
		// TODO Auto-generated method stub
		return "TextSecure-Encryption";
	}

	
	private SaltedAndPepperedKey createKey(String seed){
		
		byte[] justSalt = new byte[(this.encryption.config.getSaltLength() / 8)];
		new Random().nextBytes(justSalt);

		KeyGenerator keygenerator = null;
		
		try{

		keygenerator = KeyGenerator.getInstance("AES");
		
		SecureRandom securerandom = null;
		// Jelly Bean workaround
		if (VERSION.SDK_INT >= 16) {
			securerandom = SecureRandom.getInstance("SHA1PRNG", "Crypto");
		}
		else {
			securerandom = SecureRandom.getInstance("SHA1PRNG");
		}
		
		securerandom.setSeed(seed.getBytes());
		keygenerator.init(this.encryption.config.getKeyLength(), securerandom);

		
	} catch (Exception e) {
		Log.v("Encryption", e.getMessage());
	}

		return new SaltedAndPepperedKey(keygenerator.generateKey(),justSalt,this.getid());
		} 
	
	
	
	public void init(Context con, Vector<JID> jid, Encryption encryption) {
		super.init(con,jid,encryption);
		
	
		int many = encryption.getManyOfKeys(this.getid());
		if(many < 1 ){
			
			
			
			Vector<SaltedAndPepperedKey> keys = new Vector<SaltedAndPepperedKey>();
			keys.add(createKey("seed"));
			encryption.storeReceivedKeys(keys);
			
		}
		int max = encryption.config.getMaximumKeys();
		if(max > many ){
		
		Vector<SaltedAndPepperedKey> keys = new Vector<SaltedAndPepperedKey>();

		for(int i = many;i < max;i++)
		{
		try{
		keys.add(createKey(new String(SecureRandom.getSeed(8),"UTF-8")));
		}
		catch(UnsupportedEncodingException e){
			Log.v("Encryption", e.getMessage());
		}
		}
		
		encryption.storeNewKeys(keys);
		}
		
	}
	
	/**
	 * @param
	 */
	@Override
	public void setCipherMessage(Message message, Header header) {
		Message plain = new Message();
		String cipher	= message.getBody();
		
		
		plain.setBody(header.stripHash(cipher));
		

		plain.setBody( header.stripHeader( cipher) );
		
	
		this.message_decrypt = plain;
	}

	/**
	 * @param message
	 * @param header : Benoetigt einen Header, um Metainformationen auszulesen
	 * 
	 * Verschluesselt eine Nachricht und legt sie zum abholen in eine interne Variable
	 */
	
	public void setTextMessage(Message message, Header header) {
		// TODO Auto-generated method stub
		
		Message cipher = new Message();
		String text	= message.getBody();

		
		//cipher.setBody(message.getBody());
		Log.v("Encryption","Es koennen " + header.addKeysToMessage(encryption.config.getRecommendedKeysinOneMessage()) + " versendet werden");
		
		text = header.addHeader(text);
		cipher.setBody(header.getHash()  + text);
		
		this.message_encrypt = cipher;
		
		//this.message_encrypt = message;

	}

	
	public boolean supports(EncryptionModeENUM mode) {
		// TODO Auto-generated method stub
		return true;
	}

	public short security_level() {
		// TODO Auto-generated method stub
		return 100;
	}
	
	/*
	public String getid() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean supports(EncryptionModeENUM mode) {
		// TODO Auto-generated method stub
		return false;
	}

	public short security_level() {
		// TODO Auto-generated method stub
		return 1;
	}

	public boolean isReady() {
		// TODO Auto-generated method stub
		return false;
	}

	public void refresh_connection() {
		// TODO Auto-generated method stub

	}
*/
	/*
	public Message getTextMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setHeader(Header header) {
		// TODO Auto-generated method stub

	}

	public Header getHeader() {
		// TODO Auto-generated method stub
		return null;
	}

	public void nextMessage() {
		// TODO Auto-generated method stub

	}
*/
	
}
