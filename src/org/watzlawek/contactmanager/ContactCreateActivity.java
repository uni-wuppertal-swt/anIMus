package org.watzlawek.contactmanager;

import org.watzlawek.R;
import org.watzlawek.XMPPServer;
import org.watzlawek.IMApp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

public class ContactCreateActivity extends Activity {
	
	private Button btCancelCreate;
	private Button btSaveCreate;
	
	private EditText etNameCreate;
	private EditText etJIDCreate;
	private EditText etNoteCreate;
	
	private TextView tvNameCreate;
	private TextView tvJIDCreate;
	private TextView tvNoteCreate;
	
    private TextView tvZaehlen;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contactcreate);

        
        btCancelCreate = (Button) findViewById(R.id.btCancelCreate);
        btSaveCreate = (Button) findViewById(R.id.btSaveCreate);
        
        etNameCreate = (EditText) findViewById(R.id.etNameCreate);
        etJIDCreate = (EditText) findViewById(R.id.etJIDCreate); 
        etNoteCreate = (EditText) findViewById(R.id.etNoteCreate); 
        
        tvNameCreate = (TextView) findViewById(R.id.tvNameCreate);
        tvJIDCreate = (TextView) findViewById(R.id.tvJIDCreate);
        tvNoteCreate = (TextView) findViewById(R.id.tvNoteCreate);
        tvZaehlen = (TextView)findViewById(R.id.tvZaehlen);
         
        tvNoteCreate.addTextChangedListener(teWatcher);
        
        btCancelCreate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	finish();
            } 
        });
        
        btSaveCreate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	XMPPServer conServer = ((XMPPServer)((IMApp)getApplicationContext()).getServerManager().getConnectedServer());
            	ContactDatabaseHandler cdbh = new ContactDatabaseHandler((IMApp)getApplicationContext());
            	if (!cdbh.exists(etJIDCreate.getText().toString(), conServer.getServerId())){
            		cdbh.insertContact(etJIDCreate.getText().toString(), etNameCreate.getText().toString(), 
            				etNoteCreate.getText().toString(), conServer.getServerId(),	true);
            	}
            	else {
            		cdbh.updateContact(etJIDCreate.getText().toString(), etNameCreate.getText().toString(), 
            				etNoteCreate.getText().toString(), conServer.getServerId(),	true);
            	}
            	
            	conServer.addNewBuddyToContact(etJIDCreate.getText().toString(), etNameCreate.getText().toString());
            	
            	conServer.pullContacts();
            	
            	finish();
            }
        });
        
	}
	
    private final TextWatcher teWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
           //This sets a textview to the current length
           tvZaehlen.setText(String.valueOf(150 - s.length()));
        }

        public void afterTextChanged(Editable s) {
        }
};
}
