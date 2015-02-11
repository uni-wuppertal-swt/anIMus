package org.watzlawek.models;

import java.util.ArrayList;

public class Group {
	private static ArrayList<Group> sList;
	
	private String mTitle;
	private ArrayList<User> mMember;
	
	public Group(String title, ArrayList<User> member){
		mTitle = title;
		mMember = member;
		
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

}
