package org.watzlawek.views.adapters;

import java.util.Vector;

import org.watzlawek.R;
import org.watzlawek.models.Group;
import org.watzlawek.models.Grouplist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Adapter chatlist 
 * 
 * @author Safran Quader
 * 
 * @version 2015-01-12
 */
public class ChatlistAdapter extends BaseAdapter{

private final Context context;
private int anz=0;
    
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

	public View getView(int position, View convertView, ViewGroup parent) {  // wird nicht beim zurückgehen aufgerufen?
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		//groups setzen
		anz=Grouplist.getInstance().getLength();
		Toast.makeText(context.getApplicationContext(), String.valueOf(anz), Toast.LENGTH_SHORT).show();
		for(int i=0;i<=anz;i++)  // stringarray länge setzen?
		{
			groups[i]="blubb";
		}
	    View rowView = inflater.inflate(R.layout.item_chatlist, parent,false);

	    TextView textView = (TextView) rowView.findViewById(R.id.chatlist_groupname);
	    textView.setText(groups[position]); // wird nicht nochmals ausgeführt?
	     
		return rowView;
	}

}
