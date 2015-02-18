package encryption.org.watzlawek;
/**
* Encryption core to inherit and to use it as a non Encryption  
*
* @author Frederick Bettray
* @author Stefan Wegerhoff
*
*@version 2015-02-18
*/
import java.util.Vector;

import org.jivesoftware.smack.packet.Message;

import android.content.Context;

public class NullEncryption_Core implements Secure_Core {

	protected String seed = "Seed";
	
	protected Message message_encrypt;
	protected Message message_decrypt;
	protected Context con;
	protected Vector<JID> jid;
	protected Encryption encryption;
	
	public NullEncryption_Core() {
		// TODO Auto-generated constructor stub
	}

	public void init(Context con, Vector<JID> jid, Encryption encryption) {
		this.con = con;
		this.jid = jid;
		this.encryption = encryption;

	}

	public void setSeed(String seed){this.seed = seed;}
	
	public String getid() {
		// TODO Auto-generated method stub
		return "Null-Encryption";
	}

	public boolean supports(EncryptionModeENUM mode) {
		// TODO Auto-generated method stub
		return false;
	}

	public short security_level() {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean isReady() {
		// TODO Auto-generated method stub
		return true;
	}

	public void refresh_connection() {
		// TODO Auto-generated method stub

	}

	public void setCipherMessage(Message message, Header header) {
		this.message_decrypt = message;

	}

	public Message getCipherMessage() {
		return this.message_encrypt;
	}

	public void setTextMessage(Message message, Header header) {

			Message mes = new Message();
			mes.setBody(message.getBody());

		
			this.message_encrypt = mes;
		

	}

	public Message getTextMessage() {
		return this.message_decrypt;
	}

	public void setHeader(Header header) {
		// TODO Auto-generated method stub

	}

	public Header getHeader() {
		// TODO Auto-generated method stub
		return null;
	}

	public void nextMessage() {
		Message message_encrypt = null;
		Message message_decrypt = null;

	}

}
