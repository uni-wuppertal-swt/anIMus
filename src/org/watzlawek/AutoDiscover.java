package org.watzlawek;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.http.NameValuePair;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.CursorJoiner.Result;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

/**
 * Class for finding friends on a anIMus Auto Discover Server - BA-Thesis
 * Contacts from given device are read out. Data will be SHA-256 hashed and crypted with public key of the Discover Server  * 
 * 
 * Status: Full Implemented
 *  
 * RequesterAsyncThread
 * 
 * BA-Thesis anIMus 2.0 Addon
 * 
 * @author Klaus-Peter Watzlawek
 * 
 * @version 2013-09-25
 *
 */
public class AutoDiscover {	
	
	/**
	* Internal Class for storing contact data read from device.
	*/
	private class ContactData {
		/**
		* Stores contacts name
		*/
		private String name;
		
		/**
		* Stores contacts phone number
		*/
		private String phoneNumber;
		
		/**
		* Stores contacts xmpp jid.
		*/
		private String xmppjid;
		
		/**
		* Constructor for Class.
		* @param in_name contacts name.
		* @param in_phoneNumber contacts phone number.
		*/
		public ContactData(String in_name, String in_phoneNumber) {
			name = in_name;
			phoneNumber = in_phoneNumber;
			xmppjid = null;
		}
		
		/**
		* 2nd Constructor for Class.
		* @param in_name contacts name.
		* @param in_phoneNumber contacts phone number.
		* @param in_xmppjid contacts  xmpp jid.
		*/
		public ContactData(String in_name, String in_phoneNumber, String in_xmppjid) {
			name = in_name;
			phoneNumber = in_phoneNumber;
			xmppjid = in_xmppjid;
		}
		
		/**
		* Getter for name.
		*/
		public String getName() {
			return this.name;
		}
		
		/**
		* Getter for phone number.
		*/
		public String getPhoneNumber() {
			return this.phoneNumber;
		}
		
		/**
		* Getter for xmpp jid.
		*/		
		public String getXmppjid() {
			return this.xmppjid;
		}
		
		/**
		* Setter for xmpp jid..
		*/		
		public void setXmppjid(String in_xmppjid) {
			this.xmppjid = in_xmppjid;
		}
	}
	
	
	/**
	 * The context of the activity which creates an object of this class.
	 */
	private static Context context;
	
	/**
	 * A dynamic list of contactData of type ContactData.
	 */
	private Vector<ContactData> vectorContactData;
	
	/**
	 * Own Phone Number
	 */	
	private String ownPhoneNumber;
	
	/**
	* Holds references for the progressbar shown on friend request process.
	*/
	public ProgressBar progressbar;
	
	/**
	 * Start Password
	 */
	private String password;	
	
	/**
	 * Own xmmpjid
	 */
	private String xmppjid;	
	/**
	 * Own phonenumber sha256 identifier	 * 
	 */
	private String identifier;
	
	/**
	* Stores the validation token.
	*/
	private String validationtoken;
	
	/**
	* Stores the authtoken.
	*/
	private String authtoken;
	
	/**
	* Stores the password.
	*/
	private String newpassword;
	
	/**
	* Stores the serviceaccount.
	*/
	private String serviceaccount;
	
		/**
	 * Country Calling Code
	 */
	private String countryCallingCode;
	
	/**
	 * Stores data for Service Request.
	 */
	public Hashtable<String, String> htService_Request;
	
	/**
	 * Stores data for Registration Request.
	 */	 
	public Hashtable<String, String> htRegistration_Request;
	
	/**
	 * Stores data for Authtoken Request.
	 */
	public Hashtable<String, String> htAuthtoken_Request;
	
	/**
	 * Stores data for Account Request.
	 */
	public Hashtable<String, String> htAccount_Request;
	
	/**
	 * Stores data for Friend Request.
	 */
	public Hashtable<String, String> htFriend_Request;
	
	/**
	 * Stores data for Delete Request.
	 */
	public Hashtable<String, String> htDelete_Request;
	
	/**
	 * Stores the own Token Data from database.
	 */
	public Hashtable<String, String> htOwnTokenData;
	
	/**
	 * Standard Constructor of AutoDiscover
	 * @param in_context
	 */	
	public AutoDiscover(Context in_context){
		context = in_context;
		ownPhoneNumber = "";
		countryCallingCode = "";	
		htService_Request = new Hashtable<String, String>();	
		htRegistration_Request = new Hashtable<String, String>();
		htAuthtoken_Request = new Hashtable<String, String>();
		htAccount_Request = new Hashtable<String, String>();		
		htFriend_Request = new Hashtable<String, String>();
		htDelete_Request  = new Hashtable<String, String>();
		
		htOwnTokenData = null;
		
		this.newpassword = "";
		this.authtoken = "";
		this.validationtoken = "";
		this.identifier = "";
		this.xmppjid = "";
		this.password = "";
		this.serviceaccount = "";		
		
	}
	
	/**
	*  Makes an authotken request, if successfull authotken will be gotten from anima server.
	*/
	public void doAuthtoken_request(String in_validationtoken) {	
		this.validationtoken = in_validationtoken;
		htAuthtoken_Request.put("authtoken_request", "isset");
		htAuthtoken_Request.put("validationtoken", validationtoken);
		htAuthtoken_Request.put("password", this.password);
	}
		
	/**
	*  Makes an account request, if successfull accould will be enabled.
	*/
	public void doAccount_request(String in_authtoken) {			
		htAccount_Request.put("account_request", "isset");
		htAccount_Request.put("authtoken", in_authtoken);
		htAccount_Request.put("validationtoken", validationtoken);
		htAccount_Request.put("password", this.password);
	}
	
	
	/**
	*  Returns the internal phone number.
	*  @return own phone number.
	*/
	private static String getOwnPhoneNumber() {
		IMApp app = (IMApp)context.getApplicationContext();
		TelephonyManager telephonymanager;
		telephonymanager = (TelephonyManager)app.getSystemService(Context.TELEPHONY_SERVICE); 
    	return telephonymanager.getLine1Number();
	}
	
	/**
	*  Returns the SHA256 hash from internal phone number.
	*  @return own phone number as sha256 hash.
	*/
	public static String getSHA256FromOwnPhoneNumber() {
		try {
			return computeHash(getOwnPhoneNumber());
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	* Generates a random start password.
	*/
	public static String randomPassword(int length) {		
		String alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		List<String> chars = Arrays.asList(alphabet.split(""));
		String randomstr = "";
		
		for(int i = 0; i < length; i++) {
			Collections.shuffle(chars);
			Random rand = new Random();
			int rand_part = rand.nextInt((chars.size()));
			randomstr = randomstr + chars.get(rand_part);
		}			
		return randomstr;
	}
	
	/**
	 * Reads all contact data from Device and stored them to an array.
	 */
	private void  readContactsfromDevice() {		
		IMApp app = (IMApp)context.getApplicationContext();
		Cursor cursor = app.getContentResolver().query( ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null,null, null);				
		vectorContactData = new Vector<ContactData>();		
		
		 while (cursor.moveToNext()) {
			 String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
			 String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
			 
			 if (phoneNumber.charAt(0) == '0')
				 phoneNumber = "+" + this.countryCallingCode + phoneNumber.substring(1);
			 
			 phoneNumber = phoneNumber.replaceAll("\\s","");
			
				try {
					
					phoneNumber = computeHash(phoneNumber);		
					//Log.v(name, phoneNumber );
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			
			 vectorContactData.add(new ContactData(name,phoneNumber));
		 } 		 		
	}
	
	/**
	 * Proof of Concept for the friend_request api, Interface to anIMa Server
	 * Not used any more. Legacy...
	 * @param authtoken
	 * @param password
	 * @param identifier
	 */
	public void getXMPPJID(String authtoken, String password, String identifier) {		
		Hashtable<String, String> ht = new Hashtable<String, String>(); 	
		ht.put("friend_request", "isset");
		ht.put("authtoken", authtoken);		
		ht.put("password", password);		
		ht.put("identifier", identifier);	
		
		RequesterAsyncThread rethread = new RequesterAsyncThread();		
		rethread.setRequestserver("http://anima.watzlawek.org/index.php");
		rethread.execute(ht);		
		//Log.v("Response", rethread.getServerresponse());
	}
	
	/**
	* Makes a registation request.
	* If successfull, a new account will be created, but deactivated.
	*/
	public void doRegistration_request() {
		IMApp app = (IMApp)context.getApplicationContext();	
		if (app.getServerManager().getConnectedServer() != null) {					
			//Hashtable for service_request
			this.password = this.htOwnTokenData.get("password");			
			this.xmppjid = this.htOwnTokenData.get("xmppjid_id");		
			
			
			htService_Request.put("service_request", "isset");
			htService_Request.put("xmppjid", this.xmppjid );			
			
			htRegistration_Request.put("registration_request", "isset");
			htRegistration_Request.put("xmppjid", this.xmppjid);
			
			htRegistration_Request.put("identifier", getSHA256FromOwnPhoneNumber());
			htRegistration_Request.put("password", this.password);
			
			RequesterAsyncThread rethread = new RequesterAsyncThread();		
			rethread.setRequestserver(this.htOwnTokenData.get("serviceaddress"));
			String out;
			//String out;
			try {
				out = rethread.execute(htService_Request).get();
				Log.v("outmsg", ""+ out);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	/**
	* Gets Token Data from databases and stores it to a hashmap.
	*/
	public void getTokenSystemDatafromDB() {
		IMApp app = (IMApp)context.getApplicationContext();			
		if (app.getServerManager().getConnectedServer() != null) {
			this.xmppjid = app.getServerManager().getConnectedServer().getUser() + "@" 
					+ app.getServerManager().getConnectedServer().getDomain();					
			htOwnTokenData = app.getServerManager().getServerDatabaseHandler().getTokenSystemAccount(xmppjid , getSHA256FromOwnPhoneNumber());		
		}
	}
	
	/**
	* Makes a friend request.
	* If successfull, a friends xmpp jid will be added to contact list.
	*/
	public void doFriendRequest() {
		IMApp app = (IMApp)context.getApplicationContext();		
		if (app.getServerManager().getConnectedServer() != null) {
			RequesterAsyncThread rethread = new RequesterAsyncThread();		
			rethread.setRequestserver(this.htOwnTokenData.get("serviceaddress"));
			String out;
			//String out;
			this.htFriend_Request.put("friend_request", "isset");
			this.htFriend_Request.put("authtoken", htOwnTokenData.get("authtoken"));
			this.htFriend_Request.put("password", htOwnTokenData.get("password"));
			this.htFriend_Request.put("identifier", "#!StartIdentifier");
			
			rethread.execute(htFriend_Request);
			/*try {
				out = rethread.execute(htFriend_Request).get();
				//Log.v("outmsg", ""+ out);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
		}
	}
	
	/**
	 * Sets some internal information of SIM Card from our target device.
	 * Sets ownPhoneNumber and countryCallingCode
	 */
	public void findNumbers(){
		IMApp app = (IMApp)context.getApplicationContext();	
		
		TelephonyManager tm = (TelephonyManager)app.getSystemService(Context.TELEPHONY_SERVICE);
		
		ownPhoneNumber = getOwnPhoneNumber();
		//countryCallingCode = tm.getSimCountryIso();
		
		ServerDatabaseHandler dbHandler = new ServerDatabaseHandler(context);
		countryCallingCode = dbHandler.getCallNumberByCountryID(tm.getSimCountryIso().toUpperCase());
	}
	
	/**
	* Wrapper Function to find friends. Used for Contactlist Activity.
	*/
	public void findFriends() {
		//IMApp app = (IMApp)context.getApplicationContext();	
		findNumbers();					
		getTokenSystemDatafromDB();
		
		if (htOwnTokenData.size() > 0) {
			if (this.htOwnTokenData.get("authtoken").equals(""))
				this.doRegistration_request();
			else {
				this.readContactsfromDevice();	
				this.doFriendRequest();
				
			}
		} else 
			Log.v("No Anima-Account", "TRUE");		
	}
	
	
	/**
	 * Generates SHA256 from given input.
	 * @param input
	 * @return sha256 hash from input
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 * //taken from http://stackoverflow.com/questions/9661008/compute-sha256-hash-in-android-java-and-c-sharp
	 */
	public static String computeHash(String input) throws NoSuchAlgorithmException, UnsupportedEncodingException{
	    MessageDigest digest = MessageDigest.getInstance("SHA-256");
	    digest.reset();

	    byte[] byteData = digest.digest(input.getBytes("UTF-8"));
	    StringBuffer sb = new StringBuffer();

	    for (int i = 0; i < byteData.length; i++){
	      sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
	    }
	    return sb.toString();
	}	
	
	/**
	 * Class as Inteface for anIMa Server HTTP Requests
	 * @author Klaus-Peter Watzlawek
	 * @version 2013-09-10
	 */
    private class RequesterAsyncThread extends AsyncTask<Hashtable<String, String>, Integer, String> {
    	
    	
    	/**
    	 * Stores the url to the anIMa Server Interface
    	 */
    	private String requestserver;
    	
    	/**
    	 * Stores the the response from the anIMa Server
    	 */
    	private String serverresponse;
    	
    	/**
    	 * Stores the received the validation link
    	 */
    	private String validationtoken_answer;
    	
    	/**
    	 * Setter for requestserver
    	 * @param input
    	 */
    	public void setRequestserver(String input) {
    		this.requestserver = input;
    	}
    	
    	/**
    	 * Getter for requestserver    	 * 
    	 * @return requestserver The url of server.
    	 */
    	public String getRequestserver() {
    		return this.requestserver;
    	}
    	
    	/**
    	 * Getter for serverresponse
    	 * @return serverresponse as string from given http request.
    	 */
    	public String getServerresponse() {
    		return this.serverresponse;
    	}
    	
		/**
		* Setter for Validation token answer
		*/
    	public void setValidationTokenAnswer(String in_token) {
    		this.validationtoken_answer = in_token;
    	}
    	
    	/**
    	 * Executes API-Request by using a Hashtable. It sends given parameters and values from Hashtable to the requestserver
    	 * @param item a Hashtable with keys and values.
    	 * @return http body response
    	 * Source Idea: http://stackoverflow.com/questions/8603583/sending-integer-to-http-server-using-namevaluepair
    	 */
    	public String requester(Hashtable<String, String> item) {    	  
    	    HttpClient httpclient = new DefaultHttpClient();
    	    HttpPost httppost = new HttpPost(requestserver);    	   
    	   
    	    try {    	    	
    	    	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(item.size());    	        
   	        	 
    	    	for (String element : item.keySet()) {   
    	    		nameValuePairs.add(new BasicNameValuePair(element , item.get(element)));
    	    		//Log.v(element, item.get(element));
    	    	}
    	    	//Log.v("REQUEST_END", "TRUE");
    	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));   
    	        
    	        HttpResponse response = httpclient.execute(httppost);    	      
    	        HttpEntity entity = response.getEntity();
    	        String responseString = EntityUtils.toString(entity, "UTF-8");
    	      
    	        return responseString;
    	         
    	    } 
    	    catch (ClientProtocolException e) {
    	    	Log.v("ClientProtocolException", e.getMessage());
    	    } 
    	    catch (IOException e) {
    	    	Log.v("IOException", e.getMessage());
    	    }
    	    
    	    return "";
    	}	
    	
    	@Override
    	protected void onProgressUpdate(Integer... progress) {
    		super.onProgressUpdate(progress);
    		progressbar.setVisibility(View.VISIBLE);
    		progressbar.setProgress(progress[0]);
    		Log.v("Percent", 1+progress[0] +  "/" + vectorContactData.size());
    		
    	}
    	
    	@Override
    	protected void onPostExecute(String result) {
    		IMApp app = (IMApp)context.getApplicationContext();
    		if (app.getServerManager().getConnectedServer() != null) {    			
    			XMPPServer sv = (XMPPServer) app.getServerManager().getConnectedServer();				
				sv.pullRoster();
				sv.pullContacts();	
				progressbar.setVisibility(View.INVISIBLE);
    		}
    	}
    	
    	/**
    	 * This is the API Point for Registration for an anIMa Account.
    	 * It makes friend request.
    	 * Can delete the user account.
    	 */
		@Override
		protected String doInBackground(Hashtable<String, String>... httpcommands) {		        
	         //for (int i = 0; i < httpcommands.length; i++) {
	        	 //Make API service_request			
			String output = "";
			IMApp app = (IMApp)context.getApplicationContext();
	        if (httpcommands[0].containsKey("service_request")) {
	        	serviceaccount = this.requester(app.getAutoDiscover().htService_Request);	
	        	//Make API registration_request
	        	if (!serviceaccount.equals("#!NotAvailable")) {	        				        				
	        		app.getServerManager().getConnectedServer().setAutoDiscoverServiceAccount(serviceaccount);	        			 	
	        		String state = this.requester(app.getAutoDiscover().htRegistration_Request);	
	        		//Internal XMPP Validation. If successfully validationtoken is saved. 
	        		//See for that: Special Constructor of XMPPChat.java
	        		//Make API authtoken_request
	        		if (!state.equals("#!Error")) {	        				
	        			authtoken = this.requester(app.getAutoDiscover().htAuthtoken_Request);        				 
	        			//Make API account_request
	        			if (!authtoken.equals("#!NotAvailable")) {
	        				app.getAutoDiscover().doAccount_request(authtoken);
		        			newpassword = this.requester(app.getAutoDiscover().htAccount_Request);			        			
		        			if (!newpassword.equals("#!DuplicateIdentifier")) {
		        				//app.getServerManager().getServerDatabaseHandler().storeTokenSystemAccount(xmppjid, serviceaccount, identifier, authtoken, newpassword);	4	        			
		        				app.getServerManager().getServerDatabaseHandler().updateTokenSystemAccount(xmppjid, AutoDiscover.getSHA256FromOwnPhoneNumber(), serviceaccount, authtoken, newpassword, "");
		        			}
		        			output = newpassword;
		        		}
	        		}	        			
	        	}
	       }
	       if (httpcommands[0].containsKey("friend_request")) {
	    	   String friends_xmppjid = "";
	    	   Vector<ContactData> tmp = new Vector<ContactData>();
	    	   tmp.clear();
	    	   for(int i = 0; i < vectorContactData.size(); i++) {	    		   
	    		   app.getAutoDiscover().htFriend_Request.put("identifier", vectorContactData.get(i).getPhoneNumber());
	    		   friends_xmppjid = this.requester(app.getAutoDiscover().htFriend_Request);
	    		   if (!friends_xmppjid.equals("#!NotMatch")) {
	    			  
	    			   vectorContactData.get(i).setXmppjid(friends_xmppjid);
	    			   tmp.add(new ContactData(vectorContactData.get(i).getName(), vectorContactData.get(i).getPhoneNumber(), vectorContactData.get(i).getXmppjid()));
	    		   }
	    		  // else 
	    			   //vectorContactData.remove(i);
	    		   publishProgress(i);
	    	   }
	    	   vectorContactData.clear();
	    	   vectorContactData = tmp;
	    	   //Log.v("friend_request", "done!");
	    	   
	    	   if (vectorContactData != null)
	    			for (int i = 0; i < vectorContactData.size() ; i++ )    			
	    				if (vectorContactData.get(i).getXmppjid() != null)
	    					if (app.getServerManager().getConnectedServer() != null) {
	    						if (!vectorContactData.get(i).getXmppjid().equals(
	    								app.getServerManager().getConnectedServer().getUser() + "@" + app.getServerManager().getConnectedServer().getDomain())) {
	    							app.getServerManager().getConnectedServer().addNewBuddyToContact(
	    									vectorContactData.get(i).getXmppjid(), 
	    									vectorContactData.get(i).getName());
	    							
	    						}
	    						//Log.v("Added:"+vectorContactData.get(i).getXmppjid() , vectorContactData.get(i).getName());
	    					}
	    	   
	       }
	        	 
	       if (httpcommands[0].containsKey("delete_request")) {
	        		 
	       }
				
	        // }
	         return output;
	         
	         
	    }	
		
	
		
		
	
    }
}
