package org.watzlawek;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class GroupAdministrationContactAdapter extends BaseAdapter{
	private final Context context;
	private final String[] user = new String[]{"Benutzer 3, benutzer 4"};
	
	public GroupAdministrationContactAdapter(Context context){
		this.context = context;
	}
	
	public int getCount() {
		// TODO Auto-generated method stub
		return user.length;
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	     View rowView = inflater.inflate(R.layout.item_groupadministrationcontact, parent,false);

	     TextView textView = (TextView) rowView.findViewById(R.id.GroupadministrationContaktName);
	     textView.setText("test");
	     
		return rowView;
	}

}
