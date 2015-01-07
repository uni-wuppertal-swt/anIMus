package org.watzlawek;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.text.Html;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;


/**
 * Class to create costume Chat Bubble Adapter. anIMus 2.0 BA-Thesis.
 * It is used to create a list of chat bubbles for a chat.
 * @author Klaus-Peter Watzlawek
 * @version 2013-09-10
 * Idea of 9patches images and some sample code taken from http://warting.se/2012/06/04/chat-bubbles-in-android/.
 */
public class BubbleChatListAdapter extends ArrayAdapter<MessageItem> implements OnCreateContextMenuListener{
	
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
	
	/**
	 * Addes messages to the bubblelist list 
	 * and to the own array adapter.
	 */
	@Override
	public void add(MessageItem in_messageitem) {		
		this.bubblesList.add(in_messageitem);
		super.add(in_messageitem);
	}

	/**
	 * Standard Constructor for this Class.
	 * @param in_context
	 * @param textViewResourceId
	 */
	public BubbleChatListAdapter(Context in_context, int textViewResourceId) {
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
	
	/**
	 * Give the MessageItem a position i back.
	 * @param in_index
	 * @return MessageItem a position i.
	 */
	public MessageItem getMessageItem(int in_index) {
		return this.bubblesList.get(in_index);
	}
	
	/**
	 * Matches a input string with a regular expression pattern.
	 * 
	 * @param input A String to match with the pattern.
	 * 
	 * @param pattern A regular expression pattern.
	 * 
	 * @return true if pattern matches with string.
	 */
	private boolean match_expression(String input, String pattern) {
        try {
        	Pattern p = Pattern.compile(pattern);
            Matcher matcher = p.matcher(input);
            Log.v("maybebefore", "RuntimeException");
            return matcher.matches();
        } 
        catch (RuntimeException e) {
        	
        	return false;
        } 
	}
	
	/**
	 * Checks if a given string matches a hyperlink
	 * @param input string
	 * @return output string matched to uri format
	 */
	public String checkforValidUri(String input) { 
		String res = input;
		String regex = "\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";	       
	    
		//String matches a hyperlink format
		if (match_expression(input,regex)) {
			
			return res;
		}				
			
		return "";
	}
	
	/**
	 * Gets view to show an bubble with all contents. 
	 * Registers Action listener for context menu options.
	 */
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
		//anIMus 2.0 BA-Thesis Addon
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
		// anIMus 2.0 BA-Thesis Filter Date form new TimeStamp Format 
		//--> show only Clock Time
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
	
	/**
	 * Used fo 9patch images.
	 * @param decodedByte
	 * @return bitmap image.
	 */
	public Bitmap decodeToBitmap(byte[] decodedByte) {
		return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
	}
	
	/**
	 * Sets Header for Chatlog context menu.
	 */
	public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {
		menu.setHeaderTitle(R.string.chatContextMenuChatLog);       	
	}		  
}
