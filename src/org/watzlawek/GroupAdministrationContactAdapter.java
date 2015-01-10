package org.watzlawek;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class GroupAdministrationContactAdapter extends BaseAdapter{
	private final Context context;
	private final String[] user = new String[]{"Benutzer 3", "Benutzer 4"};
	
	public GroupAdministrationContactAdapter(Context context){
		this.context = context;
	}
	
	public int getCount() {
		return user.length;
	}

	public Object getItem(int position) {
		return user[position];
	}

	public long getItemId(int position) {
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	     View rowView = inflater.inflate(R.layout.item_groupadministrationcontact, parent,false);

	     TextView textView = (TextView) rowView.findViewById(R.id.GroupadministrationContaktName);
	     textView.setText(user[position]);
	     
		return rowView;
	}

}
