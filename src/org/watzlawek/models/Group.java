package org.watzlawek.models;

import java.util.List;
import java.util.Vector;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.watzlawek.IMApp;
import org.watzlawek.XMPPServer;

import android.content.Context;

public class Group {
	
	private String mGroupName; 
	
	private MultiUserChat mMultiUserChat;
	private XMPPServer mServer;
	
	public Group(){
		mMultiUserChat = null;
		mServer = null;
	}

	public Group(Context context){
		mMultiUserChat = null;
		mServer = (XMPPServer) ((IMApp) context).getServerManager().getConnectedServer();
	}
	
	public void createRoom(String roomname, String nickname) throws XMPPException{
        String chatroom = roomname+"@conference." + mServer.getDomain();
		mMultiUserChat = new MultiUserChat(mServer.getConnection(), chatroom);
		
		mMultiUserChat.create(roomname);
		mMultiUserChat.sendConfigurationForm(new Form(Form.TYPE_SUBMIT));
		joinRoom(nickname);
	}
	
	public void joinRoom(String nickname) throws XMPPException{
		mMultiUserChat.join(nickname);
		
		mMultiUserChat.addMessageListener(
				new PacketListener() {
			
					public void processPacket(Packet packet) {
						Message message = (Message) packet;
						receiveMessage();
					}
				}
		);
	}
	
	public void sendMessage(String message) throws XMPPException{
		mMultiUserChat.sendMessage(message);
	}
	
	public void receiveMessage(){
		
	}
	
	public String getGroupName(){
		return null;
	}
	
	public void setName(String groupName){
		mGroupName = groupName;
	}
	
	public void setContactsInGroup(Vector<String> contacts){
		
	}
	
	public void setMember(List<String> member){
		
	}
}
