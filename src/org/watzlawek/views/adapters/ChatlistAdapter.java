package org.watzlawek.views.adapters;

import java.util.Vector;

import org.watzlawek.R;
import org.watzlawek.models.Grouplist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Dieser Adapter stellt die Daten zur Anzeige in der ChatlistActivity bereit.
 * 
 * @author Karsten Klaus
 * @author Safran Quader
 * 
 * @version 2015-01-30
 */
public class ChatlistAdapter extends BaseAdapter{
    
	private Context mContext;
	private Grouplist mGrouplist = Grouplist.getInstance();

	public ChatlistAdapter(Context context){
		mContext = context;
		//TODO populate mGrouplist
	}
	
	public int getCount() {
		return mGrouplist.size();
	}

	public Object getItem(int position) {
		return mGrouplist.getGroup(position);
	}

	public long getItemId(int position) {
		return position; 
	}

	public View getView(int position, View convertView, ViewGroup parent) { 
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
	    View rowView = inflater.inflate(R.layout.item_chatlist, parent,false);

	    TextView textView = (TextView) rowView.findViewById(R.id.chatlist_groupname);
	    //textView.setText(groups[position]); //das erste textview wird nicht gesetzt?
	     
		return rowView;
	}

}
