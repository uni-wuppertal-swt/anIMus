package org.watzlawek;

import java.util.EventListener;

/**
 * Interface which is needed to pass incoming messages to ChatActivity.
 * 
 * @author Klaus-Peter Watzlawek
 * @author Moritz Lipfert
 * 
 * @version 2012-11-14
 */
public interface IMChatMessageListener extends EventListener {
	
	/**
	 * Method that is called when a new message arrives.
	 * It has to be implemented by the class which implements this interface.
	 * 
	 * @param message Message event object of the message which arrived.
	 */
	public void newMessage(IMChatMessageEvent message);
}
