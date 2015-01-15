package encryption.org.watzlawek;

import java.util.Vector;
import android.content.Context;
import org.jivesoftware.smack.packet.Message;

/**
 * This interface will be used for all Classes,
 * which offers any ways to encrypt and decrypt messages 
 * 
 * 
 * @author Frederick Bettray
 * @author Stefan Wegerhoff
 * 
 * @version 2015-01-15
 */

public interface Secure_Core {

	/**
	 * exchance keys with other participants 
	 * @param con
	 * @param jid : jids to interact with
	 */
	
	void init(Context con, Vector<String> jid);

	/**
	 * returns a id to specify the service 
	 * @return id-String
	 */
	String getid();
	
	/**
	 * shows all services, which will be offered
	 * @param mode Enums
	 * @return
	 */
	boolean supports( EncryptionModeENUM mode);

	/**
	 * rates the encryption service for its security
	 * @return
	 */
	short security_level();

	/**
	 * Service is ready to use
	 * @return
	 */
	boolean isReady();
	
	/**
	 * Build up a new encryptionconnnection, an old have broke
	 */
	void refresh_connection();
	
	/**
	 * set ciphermessage for decryption
	 * @param message
	 */
	void setCipherMessage(Message message);

	/**
	 * get ciphermessage from encryption
	 * @param message
	 */
	Message getCipherMessage();

	/**
	 * set readable textmessage
	 * @param message
	 */
	void setTextMessage(Message message);

	/**
	 * get readable textmassage
	 * @param message
	 */

	Message getTextMessage();

	/**
	 * set a header for settings
	 * @param header
	 */
	void setHeader(Header header);
	Header getHeader();
	
	/**
	 * 
	 */
	void nextMessage();
	
}
