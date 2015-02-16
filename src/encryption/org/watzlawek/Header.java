package encryption.org.watzlawek;

import org.jivesoftware.smack.packet.Message;

public class Header {

	private String hash;
	private SaltedAndPepperedKey key;
	private short manyOfKeysLeft;
	private short manyOfKeysToSend;
	private String coreID;
	private String expectNextCore; 
	
	
	
	Header(KeySetDB keyset, Message msg) {
		String str = msg.getBody();
		this.hash = str.substring(0, 40);
		this.key = keyset.getKey(this.hash);
		
		
		
		
	}

	Header(KeySetDB keyset){
		
	}
	
	
	
}
