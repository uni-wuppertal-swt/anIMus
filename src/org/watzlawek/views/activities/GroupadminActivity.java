package org.watzlawek.views.activities;

import java.util.Vector;

import org.watzlawek.R;
import org.watzlawek.XMPPServer;
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

                   // zu tun: häckchen setzen, bestehende kontakte anzeigen?
public class GroupadminActivity extends Activity {
	
	private Vector<String> lokallist=new Vector<String>();
	private Vector<String> contacts=new Vector<String>();
	private int lllenght=0;
	private Boolean[] lltocontactsyesno=new Boolean[1];
	private String name="blubb";
	private int groupnumber;
	private int i;
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
        //lokallist setzen:
      //  XMPPServer sv = (XMPPServer) app.getServerManager().getConnectedServer();
     //   ContactDatabaseHandler cdbh = new ContactDatabaseHandler(context);
   //     lokallist = cdbh.getJIDsUsernames(serverID);
        lokallist.add("jo");
        lokallist.add("test");
        lokallist.add("blubb");
        lokallist.add("irgendwas");
        lokallist.add("nochmehr");
        lokallist.add("auch etwas");
        // ACHTUNG: Der Vector beinhaltet immer abwechselnd JID und Username.
  //      cdbh.close();
        lllenght=lokallist.size();
        lltocontactsyesno=new Boolean[lllenght];
    //    Toast.makeText(getApplicationContext(), String.valueOf(lllenght), Toast.LENGTH_SHORT).show();
        String[] blubb = new String[lllenght];
        for(i=0; i<lllenght; i++)
        {
        	lltocontactsyesno[i]=false;     // problem: man muss jeden einzeln nochmal auswählen
        	blubb[i]=lokallist.elementAt(i);
        }		
        gAdapter.setuser(blubb);
        
        ListView listView = (ListView) findViewById(R.id.groupadmin_contactlist);
        listView.setAdapter(gAdapter);
        // häckchen setzen   wird togglebutton gedrückt?
        listView.setOnItemClickListener(new OnItemClickListener() 
        {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				//angeklickte nummer: arg2
			//	Toast.makeText(getApplicationContext(), String.valueOf(arg2), Toast.LENGTH_SHORT).show();
				lltocontactsyesno[arg2]=!lltocontactsyesno[arg2];
				
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
				if (!edit.getText().toString().equals("") )
				name=edit.getText().toString();
				// gruppeneinstellungen übernehmen				   
				Grouplist.getInstance().getGroup(groupnumber).setName(name);
				    //kontaktliste der gruppen mit lokaler liste überschreiben
				for(int i=0;i<lllenght;i++)
				{
					if (lltocontactsyesno[i])
					{
						contacts.add(lokallist.elementAt(i));
					//	Toast.makeText(getApplicationContext(), lokallist.elementAt(i), Toast.LENGTH_SHORT).show();
					}
				}
				Grouplist.getInstance().getGroup(groupnumber).setContactsInGroup(contacts);
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
