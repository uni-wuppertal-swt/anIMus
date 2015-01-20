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
	
	public JID( String jid ) {
		this.jid = jid;
	}

	
	String getJID(){
		return jid;
	}
	
	void createChat(Connection con){
    	this.chat = con.getChatManager().createChat(this.jid, new MessageListener() {
   	     public void processMessage(Chat chat, Message message) {
   	         // Print out any messages we get back to standard out.
   	         System.out.println("Received message: " + message);
   	     }
   	 });

		
	}
}
