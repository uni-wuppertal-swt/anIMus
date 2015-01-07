package org.watzlawek;

import java.util.EventObject;

/**
 * Message event which is thrown on arrival of incoming messages.
 * 
 * @author Klaus-Peter Watzlawek
 * @author Moritz Lipfert
 * 
 * @version 2012-11-14
 */
public class IMChatMessageEvent extends EventObject {
	/**
	 * UID for this event. This constant is used internally by the application.
	 */
	private static final long serialVersionUID = 8837854321672603972L;
	
	/**
	 * Default constructor which takes the object that throws this event.
	 * 
	 * @param source Source object which throws the message.
	 */
	public IMChatMessageEvent(Object source) {
		super(source);
	}
}
