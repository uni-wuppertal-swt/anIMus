package org.watzlawek.contactmanager;

import org.watzlawek.R;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
        
        btCancelCreate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	finish();
            }
        });
        
        btSaveCreate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	//ContactDatabaseHandler cdbh = new ContactDatabaseHandler();
            }
        });
        
	}
}
