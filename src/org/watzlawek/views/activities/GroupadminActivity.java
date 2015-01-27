package org.watzlawek.views.activities;

import java.util.Vector;

import org.watzlawek.R;
import org.watzlawek.models.Group;
import org.watzlawek.models.Grouplist;
import org.watzlawek.views.adapters.GroupadminContactAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Activity to display the groupadministration.
 * 
 * @author Safran Quader
 * 
 * @version 2015-01-09
 */ 

                   // zu tun: häckchen setzen, speichern, abbrechen
public class GroupadminActivity extends Activity {
	
	private Vector<Group> lokallist=new Vector<Group>();
	private String name="blubb";
	private int groupnumber;
	@Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupadmin);
        Bundle extras = getIntent().getExtras();
        groupnumber = extras.getInt("groupnr");
        name=Grouplist.getInstance().getGroup(groupnumber).getName();
        TextView groupnametextView = (TextView) findViewById(R.id.groupadmin_groupname_textview);
 	     groupnametextView.setText("neuer Name der Gruppe: "+name);
        GroupadminContactAdapter gAdapter = new GroupadminContactAdapter(getBaseContext());
        gAdapter.setname(name);
        gAdapter.setnr(groupnumber);
        
        
        		
        ListView listView = (ListView) findViewById(R.id.groupadmin_contactlist);
        listView.setAdapter(gAdapter);
        // häckchen setzen   wird togglebutton gedrückt?
        listView.setOnItemClickListener(new OnItemClickListener() 
        {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				//angeklickte nummer: arg2
				
			//	angeklickten kontakt in lokale liste übernehmen
				// oder aus lokaler liste herausnehmen
				//togglebutton geklickt?
			}
		});
        
        //button "speichern"
        Button safebutton = (Button) findViewById(R.id.groupadmin_safe_button);
        safebutton.setOnClickListener(new OnClickListener()                                
        {
			public void onClick(View v) {
				EditText edit= (EditText) findViewById(R.id.groupadmin_groupname_edittext);
				name=edit.getText().toString();
				// gruppeneinstellungen übernehmen				   
				Grouplist.getInstance().getGroup(groupnumber).setName(name);
				    //kontaktliste der gruppen mit lokaler liste überschreiben
				
				finish();
			}
		});
        
 
        Button cancelbutton = (Button) findViewById(R.id.groupadmin_cancel_button);
        cancelbutton.setOnClickListener(new OnClickListener()                                
        {
			public void onClick(View v) {
				finish();
				
			}
		});
	} 
	
}
