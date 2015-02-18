package org.watzlawek.models;

/**
 * Ermöglicht beim Speichern die einfach Zuordnung von JID und Nickname. 
 * 
 * @author Karsten Klaus
 * @author Safran Quader
 * @version 2015-02-05
 */

public class User {
	private String mJid;
	private String mNickname;
	private boolean mSelected;
	
	public User(String jid, String nickname){
		setJid(jid);
		setNickname(nickname);
		setSelected(false);
	}

	public String getJid() {
		return mJid;
	}

	public void setJid(String jid) {
		mJid = jid;
	}

	public String getNickname() {
		return mNickname;
	}

	public void setNickname(String nickname) {
		mNickname = nickname;
	}

	public boolean isSelected() {
		return mSelected;
	}

	public void setSelected(boolean selected) {
		mSelected = selected;
	}
}
