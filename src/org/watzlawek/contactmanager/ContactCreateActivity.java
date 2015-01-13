package org.watzlawek.contactmanager;

import org.watzlawek.R;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ContactCreateActivity extends Activity {
	
	private Button btCancelCreate;
	private Button btSaveCreate;
	
	private EditText etPhoneCreate;
	private EditText etNameCreate;
	private EditText etJIDCreate;
	
	private TextView tvPhoneCreate;
	private TextView tvJIDCreate;
	private TextView tvNameCreate;
	private TextView tvNoteCreate;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contactcreate);
        
        btCancelCreate = (Button) findViewById(R.id.btCancelCreate);
        btSaveCreate = (Button) findViewById(R.id.btSaveCreate);
        
        etPhoneCreate = (EditText) findViewById(R.id.etPhoneCreate);
        etNameCreate = (EditText) findViewById(R.id.etNameCreate);
        etJIDCreate = (EditText) findViewById(R.id.etJIDCreate);  
        
        tvPhoneCreate = (TextView) findViewById(R.id.tvPhoneCreate);
        tvJIDCreate = (TextView) findViewById(R.id.tvJIDCreate);
        tvNameCreate = (TextView) findViewById(R.id.tvNameCreate);
        tvNoteCreate = (TextView) findViewById(R.id.tvNoteCreate);
        
	}
}
