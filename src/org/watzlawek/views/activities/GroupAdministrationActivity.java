package org.watzlawek.views.activities;

import java.util.ArrayList;

import org.watzlawek.R;
import org.watzlawek.models.Group;
import org.watzlawek.models.User;
import org.watzlawek.views.adapters.GroupAdministrationAdapter;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

/**
 * GUI der Gruppenadministration.
 * 
 * @author Karsten Klaus
 * @author Safran Quader
 * 
 * @version 2015-02-11
 */ 

public class GroupAdministrationActivity extends ListActivity {
	GroupAdministrationAdapter mAdapter; 	
	ArrayList<User> mUserList;
	
	public final static String MODE = "mode";
	public final static String GROUP_ID = "ID";
	
	public final static int MODE_CREATE = 0;
	public final static int MODE_EDIT = 1;
	
	private int modus;
	private int groupID;
	
	@Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        initGroupAdministrationActivity();

        getContact();
        
        mAdapter = new GroupAdministrationAdapter(this, mUserList);
        setListAdapter(mAdapter);
        
        Log.d("", "Modus: " + modus);
        Log.d("", "ID: " + groupID);
	}	
	
	private void initGroupAdministrationActivity(){
		Intent intent = getIntent();
		modus = intent.getIntExtra(GroupAdministrationActivity.MODE, 0);
		groupID = intent.getIntExtra(GroupAdministrationActivity.GROUP_ID, -1);
		
		switch(modus){
        	case MODE_CREATE:
        		setContentView(R.layout.activity_groupadministration);
        		break;
        	case MODE_EDIT:
        		setContentView(R.layout.activity_groupadministration_edit);
        		break;
        }
	}

	//TODO Hier Schnittstelle zu Kontaktverwaltung implementieren! noch �brig: aktuelle serverid ermitteln
	private void getContact() {
		User user;
		user = new User("safran.quader@dev.animus-im.org", "Safran Quader");
		
		mUserList = new ArrayList<User>();
		mUserList.add(user);
		
//		ContactDatabaseHandler cdbh = new ContactDatabaseHandler(context);
//		Vector<String> jidsUsernames = cdbh.getJIDsUsernames(serverID);
		// ACHTUNG: Der Vector beinhaltet immer abwechselnd JID und Username.
//		cdbh.close();
		
//		for (int i=0;i<jidsUsernames.size()/2;i++)
//		{
//			user = new User(jidsUsernames.elementAt(2*i),jidsUsernames.elementAt(2*i+1));
//			mUserList.add(user);
//		}
		
  	}
	
	public void safeButtonOnClick(View v) {
		switch(v.getId()){
		case R.id.groupadministration_bt_add:
			View root = v.getRootView();
			EditText tv = (EditText) root.findViewById(R.id.txtName);
			String groupTitle = tv.getText().toString();
		
			Group group = new Group(groupTitle, mAdapter.getUserSelection());
			
			finish();
		}
	}
	
}
