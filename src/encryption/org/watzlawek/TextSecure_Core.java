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

	public void setCipherMessage(Message message) {
		Message plain = null;
		String iv	= "";
		String key	= "";
		String text	= message.getBody();
		AES256Cipher DecryptObject = null;
		
		try {
			plain.setBody(DecryptObject.decrypt(iv.getBytes(),key.getBytes(),text.getBytes()).toString());
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}
*/
	/*
	public Message getCipherMessage() {
		// TODO Auto-generated method stub
		return null;
	}
*/
	/*
	public void setTextMessage(Message message) {
		// TODO Auto-generated method stub
		
		Message cipher = null;
		String iv	= "";
		String key	= "";
		String text	= message.getBody();
		AES256Cipher EncryptObject = null;

		try {
			cipher.setBody(EncryptObject.encrypt(iv.getBytes(),key.getBytes(),text.getBytes()).toString());
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

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
