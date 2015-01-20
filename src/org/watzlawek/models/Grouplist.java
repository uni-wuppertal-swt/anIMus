package org.watzlawek.models;

import java.util.Vector;

public class Grouplist {
	
	
	private Vector<Group> mGrouplist;
	private int mLength;
	private static Grouplist instance;
	
	
	private Grouplist() 
	{
		mGrouplist=new Vector<Group>();
		mLength=-1;
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
		mGrouplist.add(group);
	}
	
	public int getLength()
	{
		return mLength;
	}
	
	public void deleteGRoup(int id){
		mGrouplist.remove(id);
	}
	
	public Group getGroup(int id)
	{
		if ((id<=mLength) && (id>=0))
		  return mGrouplist.elementAt(id);
		return null;
	}
	
}
