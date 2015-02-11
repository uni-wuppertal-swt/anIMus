package org.watzlawek.views.adapters;

import java.util.ArrayList;

import org.watzlawek.R;
import org.watzlawek.models.User;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

/**
 * Adapter für die Gruppenadministration.
 * 
 * @author Safran Quader
 * 
 * @version 2015-02-02
 */
public class GroupAdministrationAdapter extends BaseAdapter{

	private Context mContext;
	private LayoutInflater mInflater;
	private ArrayList<User> mUserList;
	private ArrayList<User> mSelectionList;
	
	public GroupAdministrationAdapter(Context context, ArrayList<User> userList){
		mContext = context;
		mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mUserList = userList;
		mSelectionList = new ArrayList<User>();
	}
	
	public int getCount() {
		return mUserList.size();
	}

	public Object getItem(int position) {
		return mUserList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		final int pos = position;
		
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.item_groupadministration_selection, parent, false);
			
			viewHolder = new ViewHolder();
			viewHolder.tvUserName = (TextView) convertView.findViewById(R.id.groupadministration_username);
			viewHolder.cbUserSelection = (CheckBox) convertView.findViewById(R.id.groupadministration_userselection);
			
			convertView.setTag(viewHolder);
		} else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		viewHolder.tvUserName.setText(mUserList.get(position).getNickname());
		viewHolder.cbUserSelection.setChecked(false);
		
		viewHolder.cbUserSelection.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				if( ((CheckBox) v).isChecked()){
					Log.d("", "checked");
					mSelectionList.add(mUserList.get(pos));
				}			
			}
		});;
		
		
		return convertView;
	}
	
//	public String getTitle(){
//		return null;
//	}
//	
	public ArrayList<User> getUserSelection(){
		return mSelectionList;
	}
	
	private class ViewHolder{
		TextView tvUserName;
		CheckBox cbUserSelection;
	}
	
}
