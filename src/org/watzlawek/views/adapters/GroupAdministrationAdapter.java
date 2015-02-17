package org.watzlawek.views.adapters;

import java.util.ArrayList;

import org.watzlawek.R;
import org.watzlawek.models.User;

import android.content.Context;
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
	
	public GroupAdministrationAdapter(Context context, ArrayList<User> userList){
		mContext = context;
		mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mUserList = userList;
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
		
		if(mUserList.get(position).isSelected()){
			viewHolder.cbUserSelection.setChecked(true);	
		} else{
			viewHolder.cbUserSelection.setChecked(false);
		}
		
		viewHolder.cbUserSelection.setTag(position);

		viewHolder.cbUserSelection.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				int position = (Integer) v.getTag();
				
				if( ((CheckBox) v).isChecked()){
					mUserList.get(position).setSelected(true);
				} else{
					mUserList.get(position).setSelected(false);
				}			
			}
		});
		
		return convertView;
	}

	public ArrayList<User> getUserList(){
		return mUserList;
	}
	
	private class ViewHolder{
		TextView tvUserName;
		CheckBox cbUserSelection;
	}
	
}
