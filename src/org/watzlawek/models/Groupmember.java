package org.watzlawek.models;

public class Groupmember {
	
	private String mJid;
	private String mNick;
	private boolean mSelected;
	
	public Groupmember(String jid, String nick){
		setJid(jid);
		setNick(nick);
		setSelected(false);
	}
	
	public Groupmember(String jid, String nick, boolean selected){
		setJid(jid);
		setNick(nick);
		setSelected(selected);
	}

	public String getJid() {
		return mJid;
	}

	public void setJid(String mJid) {
		this.mJid = mJid;
	}

	public String getNick() {
		return mNick;
	}

	public void setNick(String mNick) {
		this.mNick = mNick;
	}

	public boolean isSelected() {
		return mSelected;
	}

	public void setSelected(boolean mSelected) {
		this.mSelected = mSelected;
	}
}
