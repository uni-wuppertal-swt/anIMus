package org.watzlawek.views.adapters;

import java.util.ArrayList;

import org.watzlawek.R;
import org.watzlawek.models.Group;
import org.watzlawek.views.activities.GroupChatActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class GroupManagerAdapter extends BaseAdapter{

	private Context mContext;
	private LayoutInflater mInflater;
	private ArrayList<Group> mItemList;
	
	public GroupManagerAdapter(Context context, ArrayList<Group> itemList){
		setContext(context);
		setItemList(itemList);
		
		mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public int getCount() {
		return getItemList().size();
	}

	public Object getItem(int position) {
		return getItemList().get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		final int pos = position;
		
		if(convertView==null){
			convertView = mInflater.inflate(R.layout.item_chatmanagement, parent, false);
			
			viewHolder = new ViewHolder();
			viewHolder.tvItemName = (TextView) convertView.findViewById(R.id.chatmanager_tv_itemname);
			
			convertView.setTag(viewHolder);
		} else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		viewHolder.tvItemName.setText(mItemList.get(position).getTitle());
		
		convertView.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				itemClicked(mContext, pos);
			}
		});
		
		return convertView;
	}
	
	public void itemClicked(Context context, int position){
		Intent intent = new Intent(context, GroupChatActivity.class);
		intent.putExtra("id", position);
		
		context.startActivity(intent);
	}
	
	public Context getContext() {
		return mContext;
	}

	public void setContext(Context context) {
		mContext = context;
	}
	
	public ArrayList<Group> getItemList(){
		return mItemList;
	}
	
	public void setItemList(ArrayList<Group> itemList){
		mItemList = itemList;
	}
	
	public class ViewHolder{
		TextView tvItemName;
	}

}
