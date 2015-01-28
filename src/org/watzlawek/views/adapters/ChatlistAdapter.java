package org.watzlawek.views.adapters;

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
private int anz=1;
    
	private String[] groups = new String[]{"hier muss was stehen? wtf"};

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
		
		anz=Grouplist.getInstance().getLength()+1;
		for(int j=0;j<anz;j++)
		{
			
		}
			
		//Toast.makeText(context.getApplicationContext(), String.valueOf(anz), Toast.LENGTH_SHORT).show();
		if (anz!=0) 
		{
			
			groups = new String[anz];  
			for(int i=0;i<anz;i++)  
			{
				//Toast.makeText(context.getApplicationContext(), Grouplist.getInstance().getGroup(i).getName(), Toast.LENGTH_SHORT).show();
				groups[i]=Grouplist.getInstance().getGroup(i).getName();
				
			}
			//Toast.makeText(context.getApplicationContext(), groups[0], Toast.LENGTH_SHORT).show();
		} 
		
		
	    View rowView = inflater.inflate(R.layout.item_chatlist, parent,false);

	    TextView textView = (TextView) rowView.findViewById(R.id.chatlist_groupname);
	    textView.setText(groups[position]); //das erste textview wird nicht gesetzt?
	     
		return rowView;
	}

}
