package org.watzlawek.contactmanager;

import org.watzlawek.XMPPChat;
import org.watzlawek.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ContactShowActivity extends Activity {

	
	private Button btShowContact;
	
	private TextView tvNameShow;
	private TextView tvJIDShow;
	private TextView tvNoteShow;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contactshow);
        
        btShowContact = (Button) findViewById(R.id.btShowContact);

        tvNameShow = (TextView) findViewById(R.id.tvNameShow);
        tvJIDShow = (TextView) findViewById(R.id.tvJIDShow);
        tvNoteShow = (TextView) findViewById(R.id.tvNoteShow);
       
        // tvNameShow.setText("@string/tvNameShow" +);
        // tvJIDShow.setText("@string/tvJIDShow" +get_jid());
        // tvNoteShow.setText("@string/tvNoteShow" +);
        
        
        btShowContact.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	finish();
            }
        });
	}
	
}
