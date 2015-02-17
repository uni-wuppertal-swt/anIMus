package encryption.org.watzlawek;

import java.util.Formatter;

import org.jivesoftware.smack.packet.Message;

public class Header {

	private byte[] hash;
	private SaltedAndPepperedKey key;
	private short manyOfKeysLeft;
	private short manyOfKeysToSend = 2;
	private String coreID;
	private String expectNextCore = "booho"; 
	private KeySetDB keyset;
	
	
	Header(){}
	
	Header(KeySetDB keyset, Message msg) {
		this.keyset = keyset;
		String str = msg.getBody();
		String hashpart = str.substring(0, 40);
		this.hash = Encryption.hexStringToByteArray(  hashpart );
		this.key = keyset.getKey(Encryption.hexStringToByteArray(  hashpart ));
		
		
		
	}

	
	
	Header(KeySetDB keyset, String coreid){
		this.keyset = keyset;
		key = keyset.getKey(coreid);
		this.hash = Encryption.hashByte( key.getSaltedEncoded());
		
	}
	
	
	
	String addHeader(String text){
		
		String needkey = "needkeys=" + manyOfKeysToSend;
		String strkey = "nextCore=" + this.expectNextCore;
		
		
		return "(" + needkey + ";" + strkey + ")" + text;
	}
	
	String stripHeader(String text){
		
		int pos = text.indexOf(")");

		String myheader = text.substring(0,pos);
		
		return text.substring(pos) + "  keyset id:" + keyset.getid();
	}
	
	String stripHash(String text){
		return text.substring(40);
	}
	
	String getHash(){
		return Encryption.byteToHex(hash);
	}
	
	
}
