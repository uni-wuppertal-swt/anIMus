package org.watzlawek.views.adapters;

import java.util.List;

import org.watzlawek.R;
import org.watzlawek.models.Group;
import org.watzlawek.models.Groupmember;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
	private final List<Groupmember> mMember;
	private Group mGroup;
	
	public GroupadminContactAdapter(Activity context, List<Groupmember> member){
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
		final ViewHolder holder;
		
		if (convertView == null){
			view = mInflater.inflate(R.layout.item_groupadmincontact, parent, false);
			
			holder = new ViewHolder();
			holder.textView = (TextView) view.findViewById(R.id.groupadmin_contactname);
			holder.checkbox = (CheckBox) view.findViewById(R.id.groupadmin_checkbox);
			holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					Groupmember g = (Groupmember) holder.checkbox.getTag();
					g.setSelected(buttonView.isChecked());
				}
			});
			
			view.setTag(holder);
			holder.checkbox.setTag(mMember.get(position));
		} else{
			view = convertView;
			//holder = (ViewHolder) view.getTag();
			((ViewHolder) view.getTag()).checkbox.setTag(mMember.get(position));
		}
		
		ViewHolder viewHolder = (ViewHolder) view.getTag();
		viewHolder.textView.setText(mMember.get(position).getNick());
		viewHolder.checkbox.setChecked(mMember.get(position).isSelected());
		
		
		//String member = (String) mMember.get(position).getNick();
		//holder.textView.setText(member);
		
		return view;
	}
	
	public void safeSettings(Activity context){
		mGroup = new Group(context, "abc");
		Log.d("", mGroup.getGroupName());
	}
	
	private class ViewHolder{
		public TextView textView;
		public CheckBox checkbox;
	}
	
}
