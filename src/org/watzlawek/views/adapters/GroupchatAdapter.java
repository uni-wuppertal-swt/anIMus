package org.watzlawek.views.adapters;

/**
 * Adapter groupchat
 * 
 * @author Safran Quader
 * 
 * @version 2015-01-12
 */
import org.watzlawek.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class GroupchatAdapter extends BaseAdapter{
	
	private final Context context;
	
	String[] messages = new String[] {"Nachricht 1", "Nachricht 2"};
	
	public GroupchatAdapter(Context context){
		this.context = context;
	}
	
	public int getCount() {
		return messages.length;
	}

	public Object getItem(int position) {
		return messages[position];
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    
		View rowView = inflater.inflate(R.layout.item_groupchat_self, parent,false);

	    TextView textView = (TextView) rowView.findViewById(R.id.item_groupchat_self_message);
	    textView.setText(messages[position]);
	     
	    return rowView;
	}

}
