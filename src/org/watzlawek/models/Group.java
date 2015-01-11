package org.watzlawek.models;

import java.util.Vector;

import org.jivesoftware.smackx.muc.MultiUserChat;
import org.watzlawek.IMChatMessageListener;
import org.watzlawek.IMServer;
import org.watzlawek.IMServer.Status;
import org.watzlawek.MessageLog;

import android.content.Context;

public class Group {
	
	private MultiUserChat mChat;
	private Context mContext;
	//private Encryption mEncryption;
	private IMChatMessageListener mMessageListener;
	private MessageLog mMessageLog;
	private String mName;
	private IMServer mServer;
	private int mServerId;
	private Status mServerStatus;
	private boolean mUnreadMessage;
	
//	public Group(){
//		
//	}
	
//	public currentTimeStamp(){
//		
//	}
	
//	public storeHistory(){
//		
//	}
	
	public void send(String message){
	
	}
	
//	public setMessageListener(IMChatMessageListener listener){
//		
//	}
	
	
//	public flushMessages(){
//		
//	}

//	public MessageFormat(String usernmae, String message, String color, int leftside){
//		
//	} 
	
//	public makeLinksClickable(String input){
//		
//	}
	
//	public match_expression(String input, String pattern){
//	
//	}
	
	public void setContactsInGroup(Vector<String> jids){

	}
}
