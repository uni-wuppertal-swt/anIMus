package encryption.org.watzlawek;

/**
* The header manages sending and receiving of metacontent like keys, wished key and saltlength
* next encryption to use 
*
* @author Frederick Bettray
* @author Stefan Wegerhoff
*
*@version 2015-02-18
*/

import java.util.Formatter;
import java.util.Iterator;
import java.util.Vector;
import org.jivesoftware.smack.packet.Message;

public class Header {

	private byte[] hash;
	private SaltedAndPepperedKey key;
	private short manyOfKeysLeft;
	private short manyOfKeysToSend = 2;
	private String coreID;
	private String expectNextCore = "booho"; 
	private KeySetDB keyset;
	private Vector<SaltedAndPepperedKey> keysToSend;
	
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
		this.coreID = coreid;
		key = keyset.getKey(coreid);
		this.hash = Encryption.hashByte( key.getSaltedEncoded());
		
	}
	
	int addKeysToMessage(int many){

		this.keysToSend = this.keyset.requestKeys(this.coreID, many);
		
		return keysToSend.size();
	}
	
	String addHeader(String text){
		
		String needkey = "needkeys=" + manyOfKeysToSend;
		String strkey = "nextCore=" + this.expectNextCore;
		StringBuffer keys = new StringBuffer();
		
	      Iterator<SaltedAndPepperedKey> iter = this.keysToSend.iterator();

	      SaltedAndPepperedKey key = null;
	      
	        while (iter.hasNext()) {
	        	key = iter.next();
	        	
	        	keys.append("{saltedkey='" + Encryption.byteToHex( key.getSaltedEncoded() ) + "';" +
	        	"keylength='" + key.getKeyLength() + "';" +
	        	"algorithm='" + key.getAlgorithm() + "';" +
	        	"format='" + key.getFormat() + "'" +
	        	"}");
	        }
		

		
		
		
		
		return "(" + needkey + ";" + strkey + keys.toString() + ")" + text;
	}
	
	String stripHeader(String text){
		
		int pos = text.indexOf(")");
		int pos2 = pos + 1;
		String myheader = text.substring(0,pos);
		
		return text.substring(pos2) + "  keyset id:" + keyset.getid();
	}
	
	String stripHash(String text){
		return text.substring(40);
	}
	
	String getHash(){
		return Encryption.byteToHex(hash);
	}
	
	
}
