package org.watzlawek.views.activities;

import org.jivesoftware.smack.XMPPException;
import org.watzlawek.R;
import org.watzlawek.models.Grouplist;
import org.watzlawek.views.adapters.GroupchatAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Activity to display the groupchat
 * 
 * @author Karsten Klaus
 * @author Safran Quader
 * 
 * @version 2015-01-30
 */

                           // zu tun: sendbutton
public class GroupchatActivity extends Activity {
	
	private int groupnumber=5;
	private String message;

	/**
	 * Initilize the activity at first start or resume
	 */
	@Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupchat); 
        
        GroupchatAdapter groupchatAdapter = new GroupchatAdapter(getBaseContext());
        ListView listView = (ListView) findViewById(R.id.groupchat_listview);
        listView.setAdapter(groupchatAdapter); 
        Bundle extras = getIntent().getExtras();
        groupnumber = extras.getInt("groupnr");
        TextView groupnametextView = (TextView) findViewById(R.id.Groupchatnametextview);
	    groupnametextView.setText(Grouplist.getInstance().getGroup(groupnumber).getGroupName());
        
        
        Button sendbutton = (Button) findViewById(R.id.groupchat_send_button);
        sendbutton.setOnClickListener(new OnClickListener() {                              // sendbutton
			public void onClick(View v) 
			{
				EditText mesedit= (EditText) findViewById(R.id.groupchat_message_edittext);
				if (!mesedit.getText().toString().equals("") )
				message=mesedit.getText().toString();
				try {
					Grouplist.getInstance().getGroup(groupnumber).sendMessage(message);
				} catch (XMPPException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
        Button editbutton = (Button) findViewById(R.id.groupchat_edit_button);
        editbutton.setOnClickListener(new OnClickListener()                                // edit button schickt auf groupeditactivity
        {
			public void onClick(View v) { 
				Intent intent = new Intent(GroupchatActivity.this, GroupadminActivity.class);
				intent.putExtra("groupnr", groupnumber);
	        	startActivity(intent);
			}
		});
        
	}

}
