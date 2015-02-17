package encryption.org.watzlawek;

import org.jivesoftware.smack.packet.Message;
import android.content.Context;
import android.os.Build.VERSION;
import android.util.Log;

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
			encryption.storeNewKeys(keys);
			
		}
		
		

		
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
	public void setCipherMessage(Message message, Header header) {
		Message plain = new Message();
		String iv	= "Deine Mudda_3456";
		String key	= "Deine Mudda_3456";
		String cipher	= message.getBody();
		
		plain.setBody(header.stripHash(cipher));
		
		
		/*
		try {
			plain.setBody(AES256Cipher.decrypt(iv.getBytes(),key.getBytes(),cipher.getBytes()).toString());
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			plain.setBody(e.getMessage());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			plain.setBody(e.getMessage());
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			plain.setBody(e.getMessage());
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			plain.setBody(e.getMessage());
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			plain.setBody(e.getMessage());
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			plain.setBody(e.getMessage());
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			plain.setBody(e.getMessage());
		}
*/
		plain.setBody( header.stripHeader( cipher) );
		
		this.message_decrypt = plain;
	}

	/*
	public Message getCipherMessage() {
		// TODO Auto-generated method stub
		return null;
	}
*/
	
	public void setTextMessage(Message message, Header header) {
		// TODO Auto-generated method stub
		
		Message cipher = new Message();
		String iv	= "Deine Mudda_3456";
		String key	= "Deine Mudda_3456";
		String text	= message.getBody();

		
		//cipher.setBody(message.getBody());
		
		text = header.addHeader(text);
		cipher.setBody(header.getHash()  + text);
		/*
		try {
			cipher.setBody(header.getHash()  +  AES256Cipher.encrypt(iv.getBytes(),key.getBytes(),text.getBytes()).toString());
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			cipher.setBody(e.getMessage());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			cipher.setBody(e.getMessage());
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			cipher.setBody(e.getMessage());
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			cipher.setBody(e.getMessage());
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			cipher.setBody(e.getMessage());
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			cipher.setBody(e.getMessage());
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			cipher.setBody(e.getMessage());
		}
		*/
		this.message_encrypt = cipher;
		
		//this.message_encrypt = message;

	}
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
