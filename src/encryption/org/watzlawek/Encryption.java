package encryption.org.watzlawek;

import java.util.Vector;
import java.lang.String;
import android.content.*;
import org.jivesoftware.smack.packet.*;


public class Encryption {
	private Vector<String> mMemberList;
	private String context;
	 
	
	
	/**
	 * Constructor 
	 * @param context
	 */
	public Encryption (Context context){
		
	}
	
	public Message decryptMessage(Message cipher) throws EncryptionFaultException {
		return cipher;
	}
	
	public Message encryptMessage(Message text) throws EncryptionFaultException {
		return text;
	}
	
	public void setMemberList(Vector<String> mMemberList){
		this.mMemberList = mMemberList; 
	}
	
	
	
}
