package org.watzlawek.views.activities;

import java.util.ArrayList;

import org.watzlawek.R;
import org.watzlawek.models.Group;
import org.watzlawek.models.User;
import org.watzlawek.views.adapters.GroupAdministrationAdapter;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

/**
 * GUI der Gruppenadministration.
 * 
 * @author Karsten Klaus
 * @author Safran Quader
 * 
 * @version 2015-02-02
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

	//TODO Hier Schnittstelle zu Kontaktverwaltung implementieren!
	private void getContact() {
		User user;
		user = new User("safran.quader@dev.animus-im.org", "Safran Quader");
		
		mUserList = new ArrayList<User>();
		mUserList.add(user);
  	}
	
	//TODO Hier beginnt der Spaß
	public void safeButtonOnClick(View v) {
		Log.d("TAG", "click!");
		
		User user = new User("aa", "aa");
		ArrayList<User> userList = new ArrayList<User>();
		userList.add(user);
		
		Group group = new Group("Testgruppe", userList);
		Group.getList().add(group);
		
		finish();
	}
	
}
