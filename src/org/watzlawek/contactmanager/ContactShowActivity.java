package org.watzlawek.contactmanager;

import org.watzlawek.IMApp;
import org.watzlawek.XMPPChat;
import org.watzlawek.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Class of the activity displaying the selected contact and its content.
 * 
 * @author Christoph Schlueter 
 * @author Svenja Clemens
 *
 *@version 2015-24-01
 */


public class ContactShowActivity extends Activity {

	
	private Button btShowContact;
	
	private TextView tvNameShow;
	private TextView tvNameShow2;
	private TextView tvJIDShow;
	private TextView tvJIDShow2;
	private TextView tvNoteShow;
	private TextView tvNoteShow2;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //IMApp app =(IMApp)getApplicationContext();
        	 	
        setContentView(R.layout.contactshow);
        Bundle intentParameter = getIntent().getExtras();
        
        
        btShowContact = (Button) findViewById(R.id.btShowContact);

        tvNameShow = (TextView) findViewById(R.id.tvNameShow);
        tvNameShow2 = (TextView) findViewById(R.id.tvNameShow2);
        tvJIDShow = (TextView) findViewById(R.id.tvJIDShow);
        tvJIDShow2 = (TextView) findViewById(R.id.tvJIDShow2);
        tvNoteShow = (TextView) findViewById(R.id.tvNoteShow);
        tvNoteShow2 = (TextView) findViewById(R.id.tvNoteShow2);
       
        tvNameShow2.setText(" " + intentParameter.getString("name"));
        tvJIDShow2.setText(" " + intentParameter.getString("jid"));
        tvNoteShow2.setText(" " + intentParameter.getString("note"));
        
        
        btShowContact.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	finish();
            }
        });
	}
	
}
