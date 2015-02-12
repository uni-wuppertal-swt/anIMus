package org.watzlawek.views.activities;

import java.util.ArrayList;
import java.util.Vector;

import org.watzlawek.R;
import org.watzlawek.models.Group;
import org.watzlawek.models.User;
import org.watzlawek.views.adapters.GroupAdministrationAdapter;

import android.app.ListActivity;
import android.os.Bundle;
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
	
	@Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupadministration);
        
        getContact();
        
        mAdapter = new GroupAdministrationAdapter(this, mUserList);
        setListAdapter(mAdapter);
	}	

	//TODO Hier Schnittstelle zu Kontaktverwaltung implementieren! noch übrig: aktuelle serverid ermitteln
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
