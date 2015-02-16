package encryption.org.watzlawek;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Message;


public class JID {

	private String jid;
	private Chat chat;
	private Encryption back; 
	
	public JID( Encryption enc, String jid ) {
		this.jid = jid;
		this.back = enc;
	}

	public JID( Encryption enc, String jid, Connection con) {
		this(enc, jid);
		this.createChat(con);
		
		//this.sendMessagetoEncryption(jid);
		
		
	}
	
	public void sendMessagetoEncryption(String str){
		this.back.receiveMessage(str);
	}
	
	String getJID(){
		return jid;
	}
	
	void createChat(Connection con){
		
		
    	this.chat = con.getChatManager().createChat(this.jid,new JID_MessageListener(this) );

		
	}
}
