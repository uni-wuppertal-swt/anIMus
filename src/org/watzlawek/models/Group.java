package org.watzlawek.models;

import java.util.ArrayList;

import org.watzlawek.IMChatMessageListener;
import org.watzlawek.MessageLog;

public class Group {
	private static ArrayList<Group> sList;
	/**
	 * Holds the complete messages.
	 */
	private MessageLog messagelog;
	/**
	 * Flag for determination that there are unread messages pending.
	 */
	private boolean unreadMessages;
	/**
	 * IMChatMessageListener object which is called when new messages arrive.
	 */
	private IMChatMessageListener messageListener;
	private String mTitle;
	private ArrayList<User> mMember;
	
	public Group(String title, ArrayList<User> member){
		mTitle = title;
		mMember = member;
		messagelog = new MessageLog();
		
		if(sList == null){
			sList = new ArrayList<Group>();
		}
		
		sList.add(this);
	}
	
	public String getTitle() {
		return mTitle;
	}
	
	public void setTitle(String mTitle) {
		this.mTitle = mTitle;
	}

	public ArrayList<User> getMember() {
		return mMember;
	}

	public void setMember(ArrayList<User> mMember) {
		this.mMember = mMember;
	}
	
	public static ArrayList<Group> getList(){
		if(sList == null){
			sList = new ArrayList<Group>();
		}
		
		return sList;
	}

	public MessageLog getMessagelog() {
		return messagelog;
	}

	public void setMessageListener(IMChatMessageListener input) {
	//	unreadMessages = false;
		messageListener = input;
	}



}
