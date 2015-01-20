package encryption.org.watzlawek;

import java.util.Vector;

import org.jivesoftware.smack.packet.Message;

import android.content.Context;
import javax.crypto.*;


public class TextSecure_Core extends NullEncryption_Core implements Secure_Core {

	public TextSecure_Core() {
		// TODO Auto-generated constructor stub
	}

	public void init(Context con, Vector<String> jid) {
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
		// TODO Auto-generated method stub

	}

	public Message getCipherMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setTextMessage(Message message) {
		// TODO Auto-generated method stub

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

}
