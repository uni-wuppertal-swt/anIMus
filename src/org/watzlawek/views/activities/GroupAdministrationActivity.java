package org.watzlawek.views.activities;

import java.util.ArrayList;

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
		View root = v.getRootView();
		EditText tv = (EditText) root.findViewById(R.id.txtName);
		String groupTitle = tv.getText().toString();
		
		Group group = new Group(groupTitle, mAdapter.getUserSelection());
		Group.getList().add(group);
	
		finish();
	}
	
}
