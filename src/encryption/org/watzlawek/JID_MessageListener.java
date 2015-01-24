package encryption.org.watzlawek;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;

public class JID_MessageListener implements MessageListener {

		public JID jid = null; 
		

	
	public JID_MessageListener(JID jid) {
		this.jid = jid;
	}

    public void processMessage(Chat chat, Message message) {
	         // Print out any messages we get back to standard out.
	         System.out.println("Received message: " + message);
	     }

}
