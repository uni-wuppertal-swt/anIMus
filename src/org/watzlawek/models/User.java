package org.watzlawek.models;

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
