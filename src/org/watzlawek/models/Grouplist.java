package org.watzlawek.models;

import java.util.Vector;

public class Grouplist {
	
	
	private Vector<Group> mGrouplist;
	private int mLength;
	private static Grouplist instance;
	
	
	private Grouplist() 
	{
		mGrouplist=new Vector<Group>();
		mLength=0;
	}
	
	public static Grouplist getInstance()
	{
		if (Grouplist.instance == null) {
			Grouplist.instance = new Grouplist();
		    }
		    return Grouplist.instance;
	};
	
	public void addGroup(Group group)
	{
		mLength=mLength+1;
		
	}
	
	public int getLength()
	{
		return mLength;
	}
	
	public void deleteGRoup(int id){
		
	}
	
	public Group getGroup(int id){
		return null;
	}
	
}
