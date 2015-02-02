package org.watzlawek.views.adapters;

import java.util.List;

import org.jivesoftware.smack.XMPPException;
import org.watzlawek.R;
import org.watzlawek.models.Group;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Adapter für die Gruppenadministration.
 * 
 * @author Safran Quader
 * 
 * @version 2015-02-02
 */
public class GroupadminContactAdapter extends BaseAdapter{

	private LayoutInflater mInflater;
	private final List<String> mMember;
	private Group group;
	
	public GroupadminContactAdapter(Activity context, List<String> member){
		mInflater = LayoutInflater.from(context);
		mMember = member;
	}
	
	public int getCount() {
		return mMember.size();
	}

	public Object getItem(int position) {
		return mMember.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		ViewHolder holder;
		
		if (convertView == null){
			view = mInflater.inflate(R.layout.item_groupadmincontact, parent, false);
			
			holder = new ViewHolder();
			holder.textView = (TextView) view.findViewById(R.id.groupadmin_contactname);
			
			view.setTag(holder);
		} else{
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}
		
		String member = (String) mMember.get(position);
		holder.textView.setText(member);
		
		return view;
	}
	
	public void saveSettings(String groupName){
		group.setName(groupName);
		Log.d("GroupadminAdapter", "Speichere: " + groupName);
		group.setMember(null);//TODO
	}
	
	public void createMultiUserChat(){
		try {
			group.createRoom(group.getGroupName(), ""); //TODO Nickname angeben
		} catch (XMPPException e) {
			e.printStackTrace();
		}
	}
	
	//TODO
	public Group getGroup(){
		group = new Group();
		group.setName("wow");
		return group;
	}
	
	private class ViewHolder{
		public TextView textView;
	}
	
}
