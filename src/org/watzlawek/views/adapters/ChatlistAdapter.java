package org.watzlawek.views.adapters;

import org.watzlawek.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Adapter chatlist 
 * 
 * @author Safran Quader
 * 
 * @version 2015-01-12
 */
public class ChatlistAdapter extends BaseAdapter{

private final Context context;
	
	private final String[] groups = new String[]{"gruppe 5", "blubb und so"};
	
	public ChatlistAdapter(Context context){
		this.context = context;
	}
	
	public int getCount() {
		return groups.length;
	}

	public Object getItem(int position) {
		return groups[position];
	}

	public long getItemId(int position) {
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		//groups setzen
	    View rowView = inflater.inflate(R.layout.item_chatlist, parent,false);

	    TextView textView = (TextView) rowView.findViewById(R.id.chatlist_groupname);
	    textView.setText(groups[position]);
	     
		return rowView;
	}

}
