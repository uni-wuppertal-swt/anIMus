package encryption.org.watzlawek;


import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Vector;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Message;

import android.content.Context;
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
	
	
	/**
	 * Constructor 
	 * @param context
	 * @param connectionConfig : config to create a new XMPPConnection
	 */
	public Encryption (Context context, ConnectionConfiguration connectionConfig){


		this(context, new XMPPConnection(connectionConfig));

	}

	/**
	 * Constructor 
	 * @param context
	 * @param connection : gets a smack Connection Interface
	 */
	
	public Encryption (Context context, Connection connection){
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
		

		
	        
		//Toast.makeText(context.getApplicationContext(), "I am your encryption and i have " + cores.size() + " ways to do it!", Toast.LENGTH_LONG).show();
		// ref in XMPPChat:122
	}
	
	public void setEncryption(boolean on){
		encryption_on = on;
	}
	
	public void receiveMessage(String str)
	{Toast.makeText(this.context.getApplicationContext(), str, Toast.LENGTH_LONG).show();}
	
	
	public void setMemberList(Vector<String> mMemberList){

		

		this.mMemberList = new Vector<JID>();

	        if(this.connection.isConnected())
	        {

	  	      Iterator<String> iter = mMemberList.iterator();
		        while (iter.hasNext()) {
		        	this.mMemberList.add(new JID( this, iter.next(), this.connection ));
		        	iter.remove();
		            
		        }
	        	
	        	 //chat.sendMessage("Howdy!");
	        	
	        }
		
//test
	        /*
	         StringBuffer stbu = new StringBuffer("Es existieren folgende JIDs :");
		      Iterator<JID> iter2 = this.mMemberList.iterator();
		        while (iter2.hasNext()) {
		        	stbu.append(iter2.next().getJID() + ", ");
		        	 
		            iter2.remove();
		            
		        } 
*/
	        
		       // Toast.makeText(context.getApplicationContext(), stbu.toString() , Toast.LENGTH_LONG).show();
		        
		        
	        Toast.makeText(context.getApplicationContext(), "anzahl Nr 1:" + cores.size() , Toast.LENGTH_LONG).show();  
		
	        
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
		        

	        Toast.makeText(context.getApplicationContext(), "anzahl Nr 2:" + cores.size() , Toast.LENGTH_LONG).show();
	        //core1 = cores.elementAt(1);
		        //core2 = cores.elementAt(0);
		        
		        Toast.makeText(context.getApplicationContext(), "Kern 1:" + core1.getid() , Toast.LENGTH_LONG).show();
		        Toast.makeText(context.getApplicationContext(), "Kern 2:" + core2.getid() , Toast.LENGTH_LONG).show();
 
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
	
	public Message decryptMessage( Message cipher) throws EncryptionFaultException {
	
	return cipher;	
		/*if(encryption_on){
			core2.setCipherMessage(cipher);
			return core2.getTextMessage();
		}
		else
		{
			core1.setCipherMessage(cipher);
			return core1.getTextMessage();

		}*/
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

	public Message encryptMessage(Message plain) throws EncryptionFaultException {
		//Toast.makeText(context.getApplicationContext(), "Toast test" , Toast.LENGTH_LONG).show();
		

		/*		if(encryption_on){
			core2.setTextMessage(plain);
			return core2.getCipherMessage();
		}
		else
		{ */
			core1.setTextMessage(plain);
			return core1.getCipherMessage();

//		}	
		
		
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
