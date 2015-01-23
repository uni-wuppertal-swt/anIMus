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
	private TextView tvNoteUpdate;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contactupdate);
        
        btCancelUpdate = (Button) findViewById(R.id.btCancelUpdate);
        btSaveUpdate = (Button) findViewById(R.id.btSaveUpdate);
        
        etNameUpdate = (EditText) findViewById(R.id.etNameUpdate); 
        etNoteUpdate = (EditText) findViewById(R.id.etNoteUpdate); 
        
        tvNameUpdate = (TextView) findViewById(R.id.tvNameCreate);
        tvJIDUpdate = (TextView) findViewById(R.id.tvJIDCreate);
        tvNoteUpdate = (TextView) findViewById(R.id.tvNoteCreate);
        
        
        btCancelUpdate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	finish();
            }
        });
        
        
        btSaveUpdate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               	XMPPServer conServer = ((XMPPServer)((IMApp)getApplicationContext()).getServerManager().getConnectedServer());
               	ContactDatabaseHandler cdbh = new ContactDatabaseHandler((IMApp)getApplicationContext());
               	cdbh.insertContact(
               			"dummy",
               			//tvJIDUpdate.getText().toString().substring(5), 
               			etNameUpdate.getText().toString(), 
               			etNoteUpdate.getText().toString(), conServer.getServerId(),	true);
               	conServer.addNewBuddyToContact(
               			//tvJIDUpdate.getText().toString().substring(5), 
               			"Dummy",
               			etNameUpdate.getText().toString());
                	
            }
        });
	}
}
