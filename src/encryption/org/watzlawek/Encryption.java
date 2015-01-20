package encryption.org.watzlawek;


import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Vector;

import java.lang.String;
import android.content.*;
import org.jivesoftware.smack.packet.*;

import android.widget.Toast;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
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
		
		 try {
	            connection.connect();
	        } catch (XMPPException ex) {
	            //chatClient.setConnection(null);
	        }
		
		Collections.sort(cores, new Comparator<Secure_Core>() {
			   public int compare(Secure_Core s1, Secure_Core s2){
			     return s1.security_level() -  s2.security_level();
			   }
			});
		
		
		
		

		
	        
		Toast.makeText(context.getApplicationContext(), "I am your encryption and i have " + cores.size() + " ways to do it!", Toast.LENGTH_LONG).show();
		// ref in XMPPChat:122
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

		
	}
		/**
		 * MessageKey MK,
		 * 
		 * 
		 * Muessen noch deklariert werden:

		message_key MK;
		Np  : Purported message number
		PNp : Purported previous message number
		CKp : Purported new chain key
		DHp : Purported new DHr
		RKp : Purported new root key
		NHKp, HKp : Purported new header keys*/
		
		/**
		 * Attempt to decrypt the message
		  with skipped-over message keys (and their associated header keys) from
		  persistent storage.
		  */
		//try_skipped_header_and_message_keys();
		
		/**
		 * Given a current header key,
		  a current message number, a future message number, and a chain key,
		  calculates and stores all skipped-over message keys (if any) in a
		  staging area where they can later be committed, along with their
		  associated header key.  Returns the chain key and message key
		  corresponding to the future message number.  If passed a chain key
		  with value <none>, this function does nothing.
		*/
		//stage_skipped_header_and_message_keys();
		
		/**
		 * Commits any skipped-over
		  message keys from the staging area to persistent storage (along 
		  with their associated header keys).
		*/
		/*commit_skipped_header_and_message_keys();
		
		if (plaintext = try_skipped_header_and_message_keys())
			return plaintext;
		
		if (HKr != 0 && Dec(HKr, header)){
			Np = read();
			MK = stage_skipped_header_and_message_keys(HKr, Nr, Np, CKr);
			CKp = MK;
			if (!Dec(MK, ciphertext))
				raise undecryptable;
		}
		else{
			if (ratchet_flag || !Dec(NHKr, header))
				raise undecryptable();
			Np = read();
			PNp = read();
			DHRp = read();
			stage_skipped_header_and_message_keys(HKr, Nr, PNp, CKr);
			HKp = NHKr;
			CKp = KDF( HMAC-HASH(RK, DH(DHRp, DHRs)) );
			RKp = CKp;
			NHKp = CKp; 
			MK = stage_skipped_header_and_message_keys(HKp, 0, Np, CKp);
			CKp = MK;
			if (!Dec(MK, ciphertext))
				raise undecryptable();
			RK = RKp;
			HKr = HKp;
			NHKr = NHKp;
			DHRr = DHRp;
			erase(DHRs);
			ratchet_flag = true;
			commit_skipped_header_and_message_keys();
			Nr = Np + 1;
			CKr = CKp;
			return read();
		}
	}*/
	/*
	public Message encryptMessage(MessageKey MK, Message text) throws EncryptionFaultException {
		message_key MK;

		if (ratchet_flag) {
			DHRs = generateECDH();
			HKs = NHKs;
			CKs = KDF( HMAC-HASH(RK, DH(DHRs, DHRr)) );
			RK = CKs;
			NHKs = CKs;
			PNs = Ns;
			Ns = 0;
			ratchet_flag = false;
		}

		MK = HMAC-HASH(CKs, "0");
		text = Enc(HKs, Ns || PNs || DHRs) || Enc(MK, plaintext);
		Ns = Ns + 1;
		CKs = HMAC-HASH(CKs, "1");

		return text;
	}
	*/
	public void setMemberList(Vector<String> mMemberList){

		
		
		this.mMemberList = new Vector<JID>();
		
	      Iterator<String> iter = mMemberList.iterator();
	        while (iter.hasNext()) {
	        	this.mMemberList.add( new JID( iter.next() ));
	            iter.remove();
	            
	        } 
		

		
		/*
	      Iterator<Secure_Core> iter = cores.iterator();
	        while (iter.hasNext()) {
	        	core = iter.next();
	            iter.remove();
	            
	        } 
		*/
 
	}
	
	
	
}
