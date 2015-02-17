package org.watzlawek.views.activities;

import java.util.ArrayList;

import org.watzlawek.R;
import org.watzlawek.models.Group;
import org.watzlawek.models.User;
import org.watzlawek.views.adapters.GroupAdministrationAdapter;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

/**
 * Ermöglicht es neue Gruppen zu erstellen und bestehende Gruppen zu bearbeiten.
 * 
 * @author Karsten Klaus
 * @author Safran Quader
 * 
 * @version 2015-02-17
 */ 

public class GroupAdministrationActivity extends ListActivity {
	
	public final static int MODE_CREATE = 0;
	public final static int MODE_EDIT = 1;
	public final static String GROUP_ID = "ID";
	public final static String MODE = "mode";
	
	private ArrayList<User> mUserList;
	private GroupAdministrationAdapter mAdapter; 	
	private int mModus;
	private int mGroupID;
	
	
	/**
	 * Startet die Activity.
	 */
	@Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        initGroupAdministrationActivity();

        getContact();
        
        mAdapter = new GroupAdministrationAdapter(this, mUserList);
        setListAdapter(mAdapter);
	}
	
	
	/**
	 * Initialisiert die Activity, indem die Memberattribute und der Modus ermittelt werden.
	 */
	private void initGroupAdministrationActivity(){
		Intent intent = getIntent();
		mModus = intent.getIntExtra(GroupAdministrationActivity.MODE, 0);
		mGroupID = intent.getIntExtra(GroupAdministrationActivity.GROUP_ID, -1);
		
		switch(mModus){
        	case MODE_CREATE:
        		setContentView(R.layout.activity_groupadministration);
        		break;
        	case MODE_EDIT:
        		setContentView(R.layout.activity_groupadministration_edit);
        		EditText et = (EditText) findViewById(R.id.groupadministration_et_title); 
        		
        		et.setText(Group.getList().get(mGroupID).getTitle());
        		
        		break;
        }
	}

	/**
	 * Ermittelt die Kontakte des aktuellen Anwenders.
	 */	
	private void getContact() {
		//TODO Hier Schnittstelle zu Kontaktverwaltung implementieren! noch �brig: aktuelle serverid ermitteln
		
		if(mUserList == null){
		User user;
		mUserList = new ArrayList<User>();
		
		user = new User("svenja.clemens@dev.animus-im.org", "Svenja Clemens");
		mUserList.add(user);
		
		user = new User("frederick.bettray@dev.animus-im.org", "Frederick Bettray");
		mUserList.add(user);
		
		user = new User("safran.quader@dev.animus-im.org", "Safran Quader");
		mUserList.add(user);
		
		user = new User("victurus@dev.animus-im.org", "Karsten Klaus");
		mUserList.add(user);
		
		user = new User("cschluet@dev.animus-im.org", "Cristoph Schlüter");
		mUserList.add(user);
		
		user = new User("stefan.wegehoff@dev.animus-im.org", "Stefan Wegehoff");
		mUserList.add(user);
		} else{
			mUserList = Group.getList().get(mGroupID).getMember();
		}
		
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
	
	/**
	 * OnClick auf den Button R.id.groupadministration_bt_add.
	 * @param v Button R.id:groupadministration_bt_add
	 */
	public void safeButtonOnClick(View v) {
		switch(v.getId()){
			case R.id.groupadministration_bt_add:
				View root = v.getRootView();
				EditText tv = (EditText) root.findViewById(R.id.txtName);
				String groupTitle = tv.getText().toString();
				Group group = new Group(groupTitle, mAdapter.getUserList());
				finish();
		}
	}
	
	
	/**
	 * OnClick auf den Button R.id.groupadministration_bt_delete.
	 * @param v Button R.id.groupadministration_bt_delete
	 */
	public void deleteButtonOnClick(View v){
		switch(v.getId()){
			case R.id.groupadministration_bt_delete:
				Group.getList().remove(mGroupID);
				Intent intent = new Intent(this, GroupManagementActivity.class);
				startActivity(intent);
		}
	}
	
	
	/**
	 * OnClick auf den Button R.id.groupadministration_bt_change.
	 * @param v Button R.id.groupadministration_bt_change
	 */
	public void changeButtonOnClick(View v){
		switch(v.getId()){
		case R.id.groupadministration_bt_change:
			String title = ((EditText) findViewById(R.id.groupadministration_et_title)).getText().toString();
			Group.getList().get(mGroupID).setTitle(title);
			//TODO Gruppenmitglieder aktuallisieren
			finish();
		}
	}
	
}
