package org.watzlawek.views.activities;

import java.util.ArrayList;
import java.util.List;

import org.watzlawek.R;
import org.watzlawek.models.Groupmember;
import org.watzlawek.views.adapters.GroupadminContactAdapter;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;

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
	private List<Groupmember> getContact() {
		Groupmember g;
		
		List<Groupmember> list = new ArrayList<Groupmember>();
		
		g = new Groupmember("svenja.clemens@dev.animus-im.org", "Svenja Clemens");
		list.add(g);
		
		g = new Groupmember("frederick.bettray@dev.animus-im.org", "Frederick Bettray");
		list.add(g);

		g = new Groupmember("victurus@dev.animus-im.org", "Karsten Klaus");
		list.add(g);
		
		g = new Groupmember("cschluet@dev.animus-im.org", "Christoph Schlüter");
		list.add(g);
		
		g = new Groupmember("stefan.wegehoff@dev.animus-im.org", "Stefan Wegehoff");
		list.add(g);
 
    	return list;
  	}
	
	//TODO Hier beginnt der Spaß
	public void safeButtonOnClick(View v) {
		//String groupName = ((EditText)((Activity)v.getContext()).findViewById(R.id.txtName)).getText().toString();
		//Activity context = (Activity) v.getContext().getApplicationContext();

		//Lokal speichern
		//adapter.saveSettings(context);
		
		//MUC erstellen
		//adapter.createMultiUserChat();
		
		//Aktuelle Aktivität schließen
		//finish();
	}
	
}
