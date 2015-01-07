package org.watzlawek;


import java.util.Collections;
import java.util.Stack;
import java.util.Vector;

/**
 * This class holds the complete message log as message items.
 * It was introduced for bubble chat system of anIMus 2.0 BA-Thesis
 * @author Klaus-Peter Watzlawek
 * 
 * @version 2013-09-10
 */
public class MessageLog {
	private Vector<MessageItem> vectorMessage;
	
	/**
	 * Give us a reference to our Vector of MessageItems.
	 * @return vectorMessage. A vector of MessageItem.
	 */
	public Vector<MessageItem> getVectorMessage() {
		return vectorMessage;
	}
	
	public MessageLog() {
		vectorMessage = new Vector<MessageItem>();
	}
	
	/** 
	 * Adds a new message to the log.
	 * @param in_message
	 * @param in_timestamp
	 * @param in_username
	 * @param in_leftside
	 */
	public void addMessage(String in_message, String in_timestamp, String in_username, int in_leftside) {
		MessageItem item  = new MessageItem(in_message, in_timestamp, in_username);	
		item.setLeftside(in_leftside);
		this.vectorMessage.add(item);
		
	}
	
	/**
	 * returns the whole list of messages stored in the vector.
	 * @return all messages merged to a long string.
	 */
	public String getMessagesAsString() {
		String tmp = "";
		for (int i = 0; i < this.vectorMessage.size()-1; i++) {
			MessageItem item = vectorMessage.get(i);
			tmp += item.ShowOnlyClockTime() + item.getUsername() + item.getMessage();
		}
			
		return tmp;	
	}
	
	/**
	 * Gets the last Message und makes it to one single string by merging all items of MessageItem.
	 * It is used for Stock Chat.
	 * @return the last message as string. 
	 */
	public String getLastMessage() {
		String tmp = "";
		if (this.vectorMessage.size() > 0) {
			MessageItem item = vectorMessage.get(this.vectorMessage.size()-1);
			tmp += item.ShowOnlyClockTime() + item.getUsername() + item.getMessage();
		}
		return tmp;
	}
	/**
	 * Gets the last MessageItem.
	 * It is used for Bubble Chat.
	 * @return the last MessageItem.
	 */
	public MessageItem getLastMessageItem() {
		MessageItem item = null;
		if (this.vectorMessage.size() > 0) {
			item = vectorMessage.get(this.vectorMessage.size()-1);			
		}		
		return item; 
	}
	
	/**
	 * Puts the font color of the chat Log to gray. It helps to identify the history in the chat log.	
	 * @param input A string there all words like red, blue and black will be replaced with the new color name.
	 * @param color The new color string as replacement.
	 * @return new messages log with global new color setting.
	 */
	public String MakeChatLogOld(String input, String color) {
		String res = input;	
		res = res.replaceAll("red", color);
		res = res.replaceAll("blue", color);
		res = res.replaceAll("black", color);
		return res;	
	}
	
	/**
	 * Clears all messages from a current chat instance. History saved in database is not affected.
	 */
	public void clearMessageRAM() {
		this.vectorMessage.clear();
	}
	
	/**
	 * Reverses the vector of MessageItems
	 */
	public void reverseMessageItems() {
		Collections.reverse(this.vectorMessage);
	}
}


