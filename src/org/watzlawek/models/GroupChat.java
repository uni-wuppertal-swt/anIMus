package org.watzlawek.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

/**
 * A GroupChat is a conversation between several participants in a virtual room.
 * @author Safran Quader
 * @version 2015-01-13
 */
public class GroupChat {
	
		private XMPPConnection mConnection;
		private String mRoom;
		private String mUsername;
		private List mParticipants = new ArrayList();
		private boolean mJoined;
		
		//TODO Add message storage and handling
		/**
		 * Creates a new group chat.
		 * @param connection the XMPP connection.
		 * @param room the name of the room following the schema roomname@hostname.
		 */
		public GroupChat(XMPPConnection connection, String room){
			mConnection = connection;
			mRoom = room;
		}
		
		/**
		 * Returns the name of the room.
		 * @return the group chat room name.
		 */
		public String getRoom(){
			return mRoom;
		}
		
		//TODO
		/**
		 * Joins the chatroom using the specified username.
		 * @param username the username to use
		 * @throws XMPPException 409 error can occur if username already in use 
		 */
		public void join(String username) throws XMPPException{
		
		}
		
		/**
		 * Checks if we already joined the group chat. 
		 * @return true if currently in the group chat or false if not.
		 */
		public boolean isJoined(){
			return mJoined;
		}
		
		//TODO
		/**
		 * Leaves the group chat.
		 */
		public void leave(){
			
		}
		
		/**
		 * Gets the username used for joining the group chat. 
		 * @return the username currently used or null.
		 */
		public String getUsername(){
			return mUsername;
		}
		
		//TODO
		public int getParticipantCount() {
			return 0;
		}
		
		//TODO
		public Iterator getParticipants() {
			return null;
		}
		
		//TODO
		 public void addParticipantListener(PacketListener listener) {
			 
		 }
		 
		 //TODO
		 public void sendMessage(String text){
			 
		 }
		 
		 //TODO
		 public Message createMessage(){
			return null; 
		 }

		 //TODO
		 public void sendMessage(Message message){
			 
		 }
		 
		 //TODO
		 public Message pollMessage(){
			 return null;
		 }
		 
		 //TODO
		 public Message nextMessage(){
			 return null;
		 }
		 
		 //TODO
		 public void addMessageListener(){
			 
		 }
		 
		 //TODO finish
		 public void finalize(){
			 
		 }
}
