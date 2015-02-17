package encryption.org.watzlawek;


import net.sqlcipher.database.SQLiteException;
import android.database.sqlite.SQLiteDatabase;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.MessageDigest;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Vector;
import java.util.Formatter;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;



import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Message;

import android.content.Context;
import android.os.Build.VERSION;
import android.util.Log;
import android.widget.Toast;
//import org.jivesoftware.smack.packet.Presence;
import encryption.org.watzlawek.AES256Cipher;


public class Encryption {
	private Vector<JID> mMemberList;
	private Vector<Secure_Core> cores;
	private Secure_Core core1,core2 ;
	private Connection connection;
	private Context context;
	private boolean encryption_on = false;
	private KeySetDB keyset = null;
	private EncryptionEngineConfig config = null;
	
	
	/**
	 * Constructor 
	 * @param context
	 * @param connectionConfig : config to create a new XMPPConnection
	 */
	public Encryption (Context context, ConnectionConfiguration connectionConfig){


		this(context, new XMPPConnection(connectionConfig));

	}

	public Encryption (Context context, Connection connection){	
		this(context, connection, new EncryptionEngineConfig(){
			public int getKeyLength(){return 128; }
			public int getSaltLength(){return 16;}
			public String getKeyExchangeCoreID(){return "";}
			public String getForcedCoreID(){return "";}
			public int getMinimumKeys(){return 20;}
			public int getMaximumKeys(){return 40;}
			public int getRecommendedKeysinOneMessage(){return 2;} 
			});
		
		
	}
	
	/**
	 * Constructor 
	 * @param context
	 * @param connection : gets a smack Connection Interface
	 */
	
	public Encryption (Context context, Connection connection, EncryptionEngineConfig conf){
		this.config = conf;
		cores = new Vector<Secure_Core>();
		cores.add(new NullEncryption_Core());
		cores.add(new TextSecure_Core());
		this.connection = connection;
		this.context = context;
		

		//if(this.connection==null)Toast.makeText(context.getApplicationContext(), "Wir haben kein Objekt, von dem wir wuesten!", Toast.LENGTH_LONG).show();
		//if(this.connection.isConnected())Toast.makeText(context.getApplicationContext(), "Verbindung steht!", Toast.LENGTH_LONG).show();
		
/*		
		 try {
			 this.connection.connect();
	        } catch (XMPPException ex) {
	            //chatClient.setConnection(null);
	        }
	*/
		
		Collections.sort(cores, new Comparator<Secure_Core>() {
			   public int compare(Secure_Core s1, Secure_Core s2){
			     return s1.security_level() -  s2.security_level();
			   }
			});
		
		//if(this.cores==null)Toast.makeText(context.getApplicationContext(), "Oh no,leaf the core alone!" , Toast.LENGTH_LONG).show();
		//int seclevel = cores.elementAt(0).security_level();
		//Toast.makeText(context.getApplicationContext(), "Der erste Kern hat den Sicherheitswert" + Integer.toString(seclevel) +  "!" , Toast.LENGTH_LONG).show();
		StringBuffer strbuff = new StringBuffer(); 
		byte[] bites;
		byte salt = 0x34;
		SecretKey key;
		KeyGenerator keygenerator = null;
		
		try{
		String seed = "seed";
		keygenerator = KeyGenerator.getInstance("AES");
		
		SecureRandom securerandom = null;
		// Jelly Bean workaround
		if (VERSION.SDK_INT >= 16) {
			securerandom = SecureRandom.getInstance("SHA1PRNG", "Crypto");
		}
		else {
			securerandom = SecureRandom.getInstance("SHA1PRNG");
		}
		
		securerandom.setSeed(seed.getBytes());
		keygenerator.init(128, securerandom);

		
//		secretkeyspec = new SecretKeySpec(key.getEncoded(), "AES");
//		cipher = Cipher.getInstance("AES");
	} catch (Exception e) {
		Log.v("Encryption", e.getMessage());
	}
		
		key = keygenerator.generateKey();
		bites = key.getEncoded();
		
		
		for (byte theByte : bites)
		{
		  strbuff.append(Integer.toHexString(theByte) + ",");
		}
		
		strbuff.append(key.getFormat() + ", " + key.getAlgorithm());
		
		 byte[] sha = new byte[bites.length + 1];
		 int i = 0;
			for (byte theByte : bites)
			{
			  sha[i++] = theByte;
			}

			sha[i] = salt;
			
		 
		   String sha1 = "";
		   String sha2 = "";
		   MessageDigest crypt = null;
		    try
		    {
		        crypt = MessageDigest.getInstance("SHA-1");
		        crypt.reset();
		        crypt.update(sha);
		        sha1 = byteToHex(crypt.digest());
		        byte[] sha_byte = hexStringToByteArray(sha1);
		        sha2 = byteToHex(sha_byte);
		    }
		    catch(NoSuchAlgorithmException e)
		    {
		        e.printStackTrace();
		    }
		
		
		//Toast.makeText(context.getApplicationContext(), strbuff.toString(), Toast.LENGTH_LONG).show();
		//Toast.makeText(context.getApplicationContext(), sha1 + " und " + sha2 + " mit " + sha1.length() + " mit " + crypt.digest().length, Toast.LENGTH_LONG).show();
		//Toast.makeText(context.getApplicationContext(), "I am your encryption and i have " + cores.size() + " ways to do it!", Toast.LENGTH_LONG).show();
		// ref in XMPPChat:122
	}
	
	public static byte[] hashByte(final byte[] hash)
	{
			byte[] result = null;
		   MessageDigest crypt = null;
		    try
		    {
		        crypt = MessageDigest.getInstance("SHA-1");
		        crypt.reset();
		        crypt.update(hash);
		        result = crypt.digest();
		    }
		    catch(NoSuchAlgorithmException e)
		    {
		        e.printStackTrace();
		    }

	    return result;
	}
	
	public static String byteToHex(final byte[] hash)
	{
	    Formatter formatter = new Formatter();
	    for (byte b : hash)
	    {
	        formatter.format("%02x", b);
	    }
	    String result = formatter.toString();
	    formatter.close();
	    return result;
	}
	
	public static byte[] hexStringToByteArray(String s) {
	    int len = s.length();
	    byte[] data = new byte[len / 2];
	    for (int i = 0; i < len; i += 2) {
	        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
	                             + Character.digit(s.charAt(i+1), 16));
	    }
	    return data;
	}
	
	public void setEncryption(boolean on){
		encryption_on = on;
	}
	
	public void receiveMessage(String str)
	{
		Toast.makeText(this.context.getApplicationContext(), str, Toast.LENGTH_LONG).show();
		
	}
	
	
	public void setMemberList(Vector<String> mMemberList){

		
		Collections.sort(mMemberList, new Comparator<String>() {
			   public int compare(String s1, String s2){
			      return s1.compareTo(s2);
			   }
			});
		

		this.mMemberList = new Vector<JID>();
	    StringBuffer stbu = new StringBuffer("");
		
	        if(this.connection.isConnected())
	        {

	  	      Iterator<String> iter = mMemberList.iterator();

		      String tmp = null;
		      
		        while (iter.hasNext()) {
		        	tmp=iter.next();
		        	this.mMemberList.add(new JID( this, tmp , this.connection ));
		        	stbu.append(tmp);
		        	iter.remove();
		            
		        }
	        	
	        	 //chat.sendMessage("Howdy!");
	        	
	        }

			try{
				keyset = new KeySetDB(this.context, stbu.toString() );
				SQLiteDatabase db = keyset.getWritableDatabase();

				db.close();
			}
			catch(EncryptionFaultException e){}
			catch(SQLiteException e){}
	        

	        
	        
		       // Toast.makeText(context.getApplicationContext(), stbu.toString() , Toast.LENGTH_LONG).show();
		        
		        
	        //Toast.makeText(context.getApplicationContext(), "anzahl Nr 1:" + cores.size() , Toast.LENGTH_LONG).show();  
		
	        
	      Iterator<Secure_Core> iter = cores.iterator();
	        while (iter.hasNext()) {
	        	core1 = iter.next();
	        		if(this.mMemberList.size() < 2)
	        		{
	        			if(core1.supports(EncryptionModeENUM.SINGLEUSER_DIRECT))break;
	        		}
	        		else
	        		{
	        			if(core1.supports(EncryptionModeENUM.MULTIUSER_DIRECT))break;
	        		}
	        				
	        				
	           // iter.remove();
	            
	        }
	        
	       Collections.reverse(cores);
	        
	        
	        
		     iter = cores.iterator();
		        while (iter.hasNext()) {
		        	core2 = iter.next();
		        		if(this.mMemberList.size() < 2)
		        		{
		        			if(core2.supports(EncryptionModeENUM.SINGLEUSER_DIRECT))break;
		        		}
		        		else
		        		{
		        			if(core2.supports(EncryptionModeENUM.MULTIUSER_DIRECT))break;
		        		}
		        				
		        				
		     
		            
		        }
		        
		        boolean help = encryption_on;
		        encryption_on = true;
		        
		        String text = "Das ist ein Testtext!";
		        String cipher = "fehlschlag";
		        
		        try{
		        cipher = this.encryptMessage(text);
		        }
		        catch(EncryptionFaultException e){
		        	Toast.makeText(context.getApplicationContext(), "Exception geworfen" , Toast.LENGTH_LONG).show();
		        }

		        try{
		        text = this.decryptMessage(cipher);
		        }
		        catch(EncryptionFaultException e){
		        	Toast.makeText(context.getApplicationContext(), "Exception geworfen" , Toast.LENGTH_LONG).show();
		        }
		        
		        Toast.makeText(context.getApplicationContext(), cipher + " " + cipher.substring(0,40) + " " +  core1.getid() + " wird zu " + text , Toast.LENGTH_LONG).show();
		        
	        //Toast.makeText(context.getApplicationContext(), "anzahl Nr 2:" + cores.size() , Toast.LENGTH_LONG).show();
	        //core1 = cores.elementAt(1);
		        //core2 = cores.elementAt(0);
		        
		        //Toast.makeText(context.getApplicationContext(), "Kern 1:" + core1.getid() , Toast.LENGTH_LONG).show();
		        //Toast.makeText(context.getApplicationContext(), "Kern 2:" + core2.getid() , Toast.LENGTH_LONG).show();
		        encryption_on = help; 
	}
	
	/**
	 * On-/Offline Schluesseltausch
	 */
	private String PreKeyWisperMessage() {
		return "";
	}
	
	private String PreKey() {
		return "";
	}
	
	private String KeyExchangeMessage() {
		return "";
	}
	/*
	private void KeyAgreement(){*/
		/**Alice*/
		/*RK = nullptr;
		HKs = nullptr;
		HKr = nullptr;
		NHKs = nullptr;
		NHKr = nullptr;
		CKs = nullptr;
		//CKr;
		DHIs = A;
		DHIr = B;
		DHRs = nullptr;
		DHRr = B1;
		Ns = 0;
		Nr = 0;
		PNs = 0;
		ratchet_flag = true;
		*/
		/**Bob*/
		/*RK = nullptr;
		HKr = nullptr;
		HKs = nullptr;
		NHKr = nullptr;
		NHKs = nullptr;
		CKr = nullptr;
		//CKs;
		DHIs = B;
		DHIr = A;
		DHRs = B1;
		DHRr = nullptr;
		Ns = 0;
		Nr = 0;
		PNs = 0;
		ratchet_flag = false;
	}*/

	public String decryptMessage(String cipher) throws EncryptionFaultException {
		Message mes = new Message();
		mes.setBody(cipher);
		return this.decryptMessage(mes).getBody();
		
	}
	
	
	public Message decryptMessage( Message cipher) throws EncryptionFaultException {
		
		if(encryption_on){
			Header header = new Header(this.keyset, cipher);
			core1.setCipherMessage(cipher, header);
			return core1.getTextMessage();
		}
		else
		{
			Header header = new Header();
			core2.setCipherMessage(cipher, header);
			return core2.getTextMessage();

		}
		/*
		Message plain = null;
		String iv	= "";
		String key	= "";
		String text	= cipher.getBody();
		AES256Cipher DecryptObject = null;
		
		try {
			plain.setBody(DecryptObject.decrypt(iv.getBytes(),key.getBytes(),text.getBytes()).toString());
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		//return plain;
		
	}

	public String encryptMessage(String plain) throws EncryptionFaultException {
		Message mes = new Message();
		mes.setBody(plain);
		return this.encryptMessage(mes).getBody();
		
	}
	
	
	public Message encryptMessage(Message plain) throws EncryptionFaultException {
		//Toast.makeText(context.getApplicationContext(), "Toast test" , Toast.LENGTH_LONG).show();
		

		if(encryption_on){
			
			Header header = new Header(this.keyset, core1.getid());
			
			core1.setTextMessage(plain, header);
			return core1.getCipherMessage();
		}
		else
		{
			Header header = new Header();
			core2.setTextMessage(plain, header);
			return core2.getCipherMessage();

		}	
		
		
		
		/*
		
		Message cipher = null;
		String iv	= "";
		String key	= "";
		String text	= plain.getBody();
		AES256Cipher EncryptObject = null;

		try {
			cipher.setBody(EncryptObject.encrypt(iv.getBytes(),key.getBytes(),text.getBytes()).toString());
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return cipher;
		*/
	}

	
	
	
}
