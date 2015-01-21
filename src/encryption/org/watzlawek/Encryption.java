package encryption.org.watzlawek;


import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Vector;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Message;

import android.content.Context;
import android.widget.Toast;
//import org.jivesoftware.smack.packet.Presence;

public class Encryption {
	private Vector<JID> mMemberList;
	private Vector<Secure_Core> cores;
	private Secure_Core core;
	private Connection connection;
	private Context context;
	
	
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
		
		
		

		
	        
		//Toast.makeText(context.getApplicationContext(), "I am your encryption and i have " + cores.size() + " ways to do it!", Toast.LENGTH_LONG).show();
		// ref in XMPPChat:122
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
		        
		        
		        
		/*
	      Iterator<Secure_Core> iter = cores.iterator();
	        while (iter.hasNext()) {
	        	core = iter.next();
	            iter.remove();
	            
	        } 
		*/
 
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
		//Message plain;
		//MessageKey MK;
		//plain = decrypt("",MK,cipher);
		
		//return plain;
		return null;
		
	}

	public Message encryptMessage(Message plain) throws EncryptionFaultException {
		//Message cipher;
		//MessageKey MK;
		//cipher = encrypt("",MK,plain);

		//return cipher;
		return null;
	}

	
	
	
}
