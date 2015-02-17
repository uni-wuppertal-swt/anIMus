package encryption.org.watzlawek;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Vector;

import org.jivesoftware.smack.packet.Message;

import android.content.Context;
import javax.crypto.*;


public class TextSecure_Core extends NullEncryption_Core implements Secure_Core {

	public TextSecure_Core() {
		// TODO Auto-generated constructor stub
	}
	
	public String getid() {
		// TODO Auto-generated method stub
		return "TextSecure-Encryption";
	}
	/*
	public void setTextMessage(Message message) {

		Message mes = new Message();
		mes.setBody(message.getBody() + " textsecure");

	
	this.message_decrpt = mes;
	

}
	*/
	/*
	public void init(Context con, Vector<JID> jid) {
		// TODO Auto-generated method stub

	}

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
		String text	= message.getBody();
		
		
		AES256Cipher DecryptObject = new AES256Cipher();
		
		try {
			plain.setBody(DecryptObject.decrypt(iv.getBytes(),key.getBytes(),text.getBytes()).toString());
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
		AES256Cipher EncryptObject = new AES256Cipher();
		
		//cipher.setBody(message.getBody());
		

		try {
			cipher.setBody(EncryptObject.encrypt(iv.getBytes(),key.getBytes(),text.getBytes()).toString());
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
