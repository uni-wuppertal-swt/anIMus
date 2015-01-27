package org.watzlawek.contactmanager;

import org.watzlawek.IMApp;
import org.watzlawek.R;
import org.watzlawek.XMPPServer;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ContactEditActivity extends Activity {
	
	private Button btCancelUpdate;
	private Button btSaveUpdate;
	
	private EditText etNameUpdate;
	private EditText etNoteUpdate;
	
	private TextView tvNameUpdate;
	private TextView tvJIDUpdate;
	private TextView tvJIDUpdate2;
	private TextView tvNoteUpdate;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contactupdate);
        
        Bundle intentPar = getIntent().getExtras();
        
        btCancelUpdate = (Button) findViewById(R.id.btCancelUpdate);
        btSaveUpdate = (Button) findViewById(R.id.btSaveUpdate);
        
        etNameUpdate = (EditText) findViewById(R.id.etNameUpdate); 
        etNoteUpdate = (EditText) findViewById(R.id.etNoteUpdate); 
        
        tvNameUpdate = (TextView) findViewById(R.id.tvNameUpdate);
        tvJIDUpdate = (TextView) findViewById(R.id.tvJIDUpdate);
        tvJIDUpdate2 = (TextView) findViewById(R.id.tvJIDUpdate2);
        tvNoteUpdate = (TextView) findViewById(R.id.tvNoteUpdate);
             
        //if (intentPar != null)
        tvJIDUpdate2.setText(" " + intentPar.getString("jid"));
        //else
        //	tvJIDUpdate2.setText("Bullshit");
        
        etNameUpdate.setText(intentPar.getString("name"));
        etNoteUpdate.setText(intentPar.getString("note"));
        
        btCancelUpdate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	finish();
            }
        });
        
        
        btSaveUpdate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               	XMPPServer conServer = ((XMPPServer)((IMApp)getApplicationContext()).getServerManager().getConnectedServer());
               	ContactDatabaseHandler cdbh = new ContactDatabaseHandler((IMApp)getApplicationContext());
               	cdbh.updateContact(
               			tvJIDUpdate2.getText().toString().substring(1), 
               			etNameUpdate.getText().toString(), 
               			etNoteUpdate.getText().toString(), conServer.getServerId(),	true);
               	conServer.addNewBuddyToContact(
               			tvJIDUpdate2.getText().toString().substring(1), 
               			etNameUpdate.getText().toString());
               	
               	IMApp app =(IMApp)getApplicationContext();
                XMPPServer sv = (XMPPServer) app.getServerManager().getConnectedServer();
				sv.clearRoster();
				sv.pullRoster();
				sv.pullContacts();
                finish();	
            }
        });
	}
}
