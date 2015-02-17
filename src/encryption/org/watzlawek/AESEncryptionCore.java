package encryption.org.watzlawek;

import org.jivesoftware.smack.packet.Message;
import android.content.Context;
import java.util.Vector;

public class AESEncryptionCore extends NullEncryption_Core implements Secure_Core  {

	private Context con;
	private Vector<JID> jid;
	
	public AESEncryptionCore() {
		// TODO Auto-generated constructor stub
	}

	private Context context;
	public String getid() {
		// TODO Auto-generated method stub
		return "TextSecure-Encryption";
	}

	
	public void init(Context con, Vector<JID> jid) {
		
		this.con = con;
		this.jid = jid;

		
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
