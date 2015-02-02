package org.watzlawek.views.activities;

import java.util.ArrayList;
import java.util.List;

import org.watzlawek.R;
import org.watzlawek.views.adapters.GroupadminContactAdapter;

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

public class GroupadminActivity extends ListActivity {
	GroupadminContactAdapter adapter; 
	
	@Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupadmin);
        
        adapter = new GroupadminContactAdapter(this, getContact());
        setListAdapter(adapter);  
	}	

	//TODO Hier Schnittstelle zu Kontaktverwaltung implementieren!
	private List<String> getContact() {
		List<String> list = new ArrayList<String>();
		list.add("svenja.clemens@dev.animus-im.org");
		list.add("frederick.bettray@dev.animus-im.org");
		list.add("safran.quader@dev.animus-im.org");
		list.add("victurus@dev.animus-im.org");
		list.add("cschluet@dev.animus-im.org");
		list.add("stefan.wegehoff@dev.animus-im.org");
 
    	return list;
  	}
	
	//TODO Hier beginnt der Spaß
	public void safeButtonOnClick(View v) {
		String groupName = ((EditText) v.findViewById(R.id.txtName)).getText().toString();

		//Lokal speichern
		adapter.saveSettings(groupName);
		
		//MUC erstellen
		//adapter.createMultiUserChat();
		
		//Aktuelle Aktivität schließen
		finish();
	}
	
}
