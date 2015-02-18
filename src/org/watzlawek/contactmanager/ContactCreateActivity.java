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

/**
 * Class of the activity which lets the user create a new contact by typing in the username,
 * the jid and the note (optional).
 * 
 * @author Christoph Schlueter 
 * @author Svenja Clemens
 *
 *@version 2015-24-01 
 */

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
         
        etNoteCreate.addTextChangedListener(teWatcher);
        
        btCancelCreate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	finish();
            } 
        });
        
        btSaveCreate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	XMPPServer conServer = ((XMPPServer)((IMApp)getApplicationContext()).getServerManager().getConnectedServer());
            	ContactDatabaseHandler cdbh = ((ContactDatabaseHandler)((IMApp)getApplicationContext()).getContactDatabasehandler());
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
           tvZaehlen.setText(String.valueOf(s.length()) +"/140");
        }

        public void afterTextChanged(Editable s) {
        }
        
};

}
