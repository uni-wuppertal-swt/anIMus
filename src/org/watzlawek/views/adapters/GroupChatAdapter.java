package org.watzlawek.views.adapters;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.watzlawek.MessageItem;
import org.watzlawek.R;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

//public class GroupChatAdapter extends BaseAdapter{
public class GroupChatAdapter extends ArrayAdapter<MessageItem> implements OnCreateContextMenuListener{
	/**
	 * Holds the reference to the message of the bubble.
	 */
	protected TextView messageTextView;	
	
	/**
	 * Holds the reference to the timestamp of the bubble.
	 */
	protected TextView timestampTextView;
	
	/**
	 * Internal list to save our MessageItems.
	 */
	protected List<MessageItem> bubblesList = new ArrayList<MessageItem>();
	/**
	 * Access to the wrapper layout.
	 */
	protected LinearLayout wrapperLinearLayout;
	
	/**
	 * Acces to one bubble element.
	 */	
	protected LinearLayout bubblecompleteLinearLayout;;
	
	/**
	 * The applications context.
	 */
	protected Context context;
	
	
	public GroupChatAdapter(Context in_context, int textViewResourceId) {
		super(in_context, textViewResourceId);
		context = in_context;
		
	}
	/**
	 * Size of the bubble list.
	 * @return Size of bubble list.
	 */
	public int getSize() {
		return this.bubblesList.size();
	}
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * Give the MessageItem at position i back.
	 * @param in_index
	 * @return MessageItem at position i.
	 */
	public MessageItem getMessageItem(int in_index) {
		return this.bubblesList.get(in_index);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	// einzele items noch anzupassen
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		if (row == null) {
			LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.chatlistitemdark, parent, false);
		}
		
		this.wrapperLinearLayout = (LinearLayout) row.findViewById(R.id.ChatListItemDarkWrapperLinearLayout);
		this.messageTextView = (TextView) row.findViewById(R.id.ChatListItemDarkMessage);
		this.timestampTextView = (TextView) row.findViewById(R.id.ChatListItemDarkTimestamp);		
		this.bubblecompleteLinearLayout = (LinearLayout) row.findViewById(R.id.ChatListItemDarkBubbleLinearLayout);		
		
		final MessageItem messageitem = this.getItem(position);
	
		//Open hyperlinks in messages bubbles
		row.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {				
						
				TextView candidateTextView = (TextView) v.findViewById(R.id.ChatListItemDarkMessage);	
				String urlmaybe = Html.fromHtml(candidateTextView.getText().toString()).toString();				
				
				try {
					URL url = new URL(urlmaybe);
					if (!urlmaybe.equals("")) {
						Uri uri = Uri.parse(url.toString());
						v.getContext().startActivity( new Intent( Intent.ACTION_VIEW, uri ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) );
					}					
				} catch (MalformedURLException e) {				
					e.printStackTrace();
					
				}			
			}
		});		
		
		row.setOnCreateContextMenuListener(this);			
		
		this.messageTextView.setText(Html.fromHtml(messageitem.getMessage()));

		this.timestampTextView.setText(Html.fromHtml(messageitem.ShowOnlyClockTime()));
		
		int gravity_ressource = Gravity.LEFT;
		int pic_ressource  = R.drawable.bubble_yellow;
		
		if (messageitem.getLeftside() == 1) {
			pic_ressource =  R.drawable.bubble_blue;
			gravity_ressource = Gravity.RIGHT;
		}
		else 
			if (messageitem.getLeftside() == 2) {
				pic_ressource =  R.drawable.bubble_red_system;
				gravity_ressource = Gravity.CENTER;
		}
		
		this.bubblecompleteLinearLayout.setBackgroundResource(pic_ressource);		
		this.wrapperLinearLayout.setGravity(gravity_ressource);
		
		return row;		
		
	}
	
	public void add(MessageItem in_messageitem) {		
		this.bubblesList.add(in_messageitem);
		super.add(in_messageitem);
	}
	/**
	 * Sets Header for Chatlog context menu.
	 */
	public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {
		menu.setHeaderTitle(R.string.chatContextMenuChatLog);       	
	}	

}
