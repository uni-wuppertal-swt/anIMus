package org.watzlawek.models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.java.otr4j.OtrEngineImpl;
import net.java.otr4j.OtrPolicy;
import net.java.otr4j.OtrPolicyImpl;

import org.jivesoftware.smackx.muc.MultiUserChat;
import org.watzlawek.Encryption;
import org.watzlawek.IMChatMessageListener;
import org.watzlawek.IMServer;
import org.watzlawek.IMServer.Status;
import org.watzlawek.MessageLog;

import android.content.Context;
import android.util.Log;

public class Group {
	
	private MultiUserChat mChat;
	private Context mContext;
	//private Encryption mEncryption;
	private IMChatMessageListener mMessageListener;
//	private Grouplist mContacts; 
	private MessageLog mMessageLog;
	private String mName;
	private IMServer mServer;
	private int mServerId;
	private Status mServerStatus;
	private boolean mUnreadMessage;
	private Context context;
	protected IMServer.Status status;
	
	
/*	public Group(Context in_context, IMServer.Status in_status, int in_serverid, IMServer in_server) {
		context = in_context;
	//	encryption = new Encryption(context);
		mServerId = in_serverid;
		mMessageLog = new MessageLog();
		mServer = in_server;
		status = in_status;
	}   */
	
	public Group()
	{
		mMessageLog = new MessageLog();
	}
	
	public void setName(String newname)
	{
		mName=newname;
	}
	
	public String getName()
	{
		return mName;
	}
	
	public String currentTimeStamp() {
		//Date d = new Date();	
		//CharSequence timestamp  = DateFormat.format("kk:mm:ss", d.getTime());
		
		//anIMus 2.0 BA-Thesis New TimeStamp Format
		SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String timestamp = s.format(new Date());
		Log.v("format",timestamp);
		return timestamp.toString();		
	}
	
//	public storeHistory(){
//		
//	}
	
	public void send(String message){
	
	}
	
	public void setMessageListener(IMChatMessageListener input) {
//		unreadMessages = false;
		mMessageListener = input;
	}
	
	
	public void flushMessages() {
		this.mMessageLog.clearMessageRAM();	
	}
	
	public String EscapeHTMLFromMsg(String input) {		
		String res = input;		
		res = res.replaceAll("<", "&lt;");
		res = res.replaceAll(">", "&gt;");				
		return res;	
	}

	public String MessageFormat(String username, String message, String color, int leftside) {
		/*String output = "<font color='"+ color + "'>" + "(" + currentTimeStamp() + ") "
				+ "<b>" + username + "</b>"+ ": " + "</font><font color='black'>" + MakeLinksClickAble(EscapeHTMLFromMsg(message)) + "</font>" + "<br>";
		Log.v("MessageLog", output);
		return output;*/
		String tmp_msg ="<font color='black'>" + MakeLinksClickAble(EscapeHTMLFromMsg(message)) + "</font> <br>";
		String tmp_stamp = "<font color='"+ color + "'>" + "(" + currentTimeStamp() + ") </font>";
		String tmp_usr = "<font color='"+ color + "'>" + "<b>" + username + "</b>"+ ": </font>";
				
		this.mMessageLog.addMessage(tmp_msg, tmp_stamp, tmp_usr, leftside);
		return "";
	}
	
	private String MakeLinksClickAble(String input) {
		String res = input;
		String regex = "\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";	       
	    
		//String matches a hyperlink format
		if (match_expression(input,regex)) {
			res = "<a href ='" + res + "'>"+ res+ "</a>";
			return res;
		}				
			
		return res;
	}
	
	private static boolean match_expression(String input, String pattern) {
        try {
        	Pattern p = Pattern.compile(pattern);
            Matcher matcher = p.matcher(input);
            return matcher.matches();
        } 
        catch (RuntimeException e) {
        	return false;
        } 
	}
	
	public void setContactsInGroup(Vector<String> jids){

	}
}
