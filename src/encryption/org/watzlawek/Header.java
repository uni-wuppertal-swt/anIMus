package encryption.org.watzlawek;

import org.jivesoftware.smack.packet.Message;

public class Header {

	private byte[] hash;
	private Key key;
	private short manyOfKeysLeft;
	private short manyOfKeysToSend;
	private String coreID;
	private String expectNextCore; 
	
	
	
	public Header(KeySetDB keyset, Message msg) {
		
	}

	public Header(KeySetDB keyset){
		
	}
	
}
