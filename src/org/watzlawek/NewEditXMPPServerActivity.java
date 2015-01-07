package org.watzlawek;

import java.util.Hashtable;

import org.watzlawek.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

/**
 * Class of activity for adding new XMPP-Servers to database or edit existing servers.
 * 
 * @author Klaus-Peter Watzlawek
 * @author Moritz Lipfert
 * 
 * @version 2012-11-14
 */
public class NewEditXMPPServerActivity extends Activity {
	
	/**
	 * Button to cancel adding or editing a server and return to the server list.
	 */
	private Button buttonCancel;
	
	/**
	 * Button to add a server or edit an existing one and return to the server list.
	 */
	private Button buttonSave;
	
	/**
	 * CheckBox which has to be checked when TLS / SSL should be used to connect.
	 */
	private CheckBox checkBoxTLS;
	
	/**
	 * Checkbox which has to be checked if you wanted to save the password (default true). 
	 */
	private CheckBox checkBoxSavePassword;
	
	/**
	 * EditText field for the server's domain.
	 */
	private EditText editTextDomain;
	
	/**
	 * EditText field for the anIMa Core Service address.
	 */
	private EditText editTextAnimaServiceAddress;
	
	/**
	 * EditText field for the login password of the server.
	 */
	private EditText editTextPassword;
	
	/**
	 * EditText field for the server's port.
	 */
	private EditText editTextPort;
	
	/**
	 * EditText field for the login username.
	 */
	private EditText editTextUsername;
	
	/**
	 * Position of the server which should be edited in the server list.
	 */
	private int position;
	
	/**
	 * "new" or "edit" depending on the way the activity is started.
	 * - new: A new server will be added to the database.
	 * - edit: An existent server will be edited.
	 */
	private String mode;
	
	/**
	 * Method which will be started on creation of the related activity.
	 * The GUI elements become created and the OnClickListener functions of the buttons gets set.
	 * If the activity becomes started with "edit" parameter the text fields will be pre-filled with the server's existing settings.
	 *
	 * @param savedInstanceState Represents a saved instance of the activity. This parameter is only used internally by the application.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.neweditserver);
        
        buttonCancel = (Button) findViewById(R.id.NewServerButtonCancel);
        buttonSave = (Button) findViewById(R.id.NewServerButtonSave);
        checkBoxTLS = (CheckBox) findViewById(R.id.NewServerCheckBoxTLS);
        checkBoxSavePassword = (CheckBox) findViewById(R.id.NewServerCheckBoxSavePassword);
        editTextDomain = (EditText) findViewById(R.id.NewServerEditTextDomain);
        editTextPassword = (EditText) findViewById(R.id.NewServerEditTextPassword);
        editTextPort = (EditText) findViewById(R.id.NewServerEditTextPort);
        editTextUsername = (EditText) findViewById(R.id.NewServerEditTextUsername);    
        editTextAnimaServiceAddress = (EditText) findViewById(R.id.NewServerEditTextAnimaServiceAddress);  
        
        Bundle intentParameter = getIntent().getExtras();
        mode = intentParameter.getString("mode");
        
        if(mode.equals("edit")) {
        	position = intentParameter.getInt("position");
        	IMApp app = (IMApp)getApplicationContext();      	       	
        	       	
        	checkBoxTLS.setChecked(app.getServerManager().getVectorServer().elementAt(position).getEncryption());
        	editTextDomain.setText(app.getServerManager().getVectorServer().elementAt(position).getDomain());
        	boolean isPasswordempty = app.getServerManager().getVectorServer().elementAt(position).getPassword().equals(""); 
        	editTextPassword.setText("*****");
        	if (isPasswordempty)       		
        		editTextPassword.setText("");         	
        	editTextPort.setText(Integer.toString(app.getServerManager().getVectorServer().elementAt(position).getPort()));
        	editTextUsername.setText(app.getServerManager().getVectorServer().elementAt(position).getUser()); 
        	//if the password was saved empty before, we know that the user do not want to save his password.
        	checkBoxSavePassword.setChecked(!isPasswordempty);  
        	Hashtable<String, String> ht_temp = app.getServerManager().getServerDatabaseHandler().getTokenSystemAccount(
        			editTextUsername.getText().toString() + "@" + editTextDomain.getText().toString(), AutoDiscover.getSHA256FromOwnPhoneNumber());
        	if (ht_temp!= null)
        		editTextAnimaServiceAddress.setText(ht_temp.get("serviceaddress"));
        }
        
        buttonCancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();				
			}
		});
        
        buttonSave.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				/* If all text fields are filled save data, else show a message. */
				
				if(!editTextDomain.getText().toString().equals("") && !(editTextPassword.getText().toString().equals("") && checkBoxSavePassword.isChecked()  ) && !editTextPort.getText().toString().equals("") && !editTextUsername.getText().toString().equals("")) {
					IMApp app = (IMApp)getApplicationContext();
					
					String tmppassword = editTextPassword.getText().toString();
					
					//Do not save a password, overwrite password field with nothing.
					if (!checkBoxSavePassword.isChecked() ) 
						tmppassword = "";			
					
					if(mode.equals("new")) {
						app.getServerManager().addServer("XMPP", editTextDomain.getText().toString(), Integer.parseInt(editTextPort.getText().toString()), editTextUsername.getText().toString(), tmppassword, editTextAnimaServiceAddress.getText().toString(), checkBoxTLS.isChecked());
						
					}
					else {
						app.getServerManager().commitUpdate(position, editTextDomain.getText().toString(), Integer.parseInt(editTextPort.getText().toString()), editTextUsername.getText().toString(), tmppassword, editTextAnimaServiceAddress.getText().toString(), checkBoxTLS.isChecked());
					}
					finish();
				} 				
				else {
					AlertDialog dialog = new AlertDialog.Builder(v.getContext()).create();
					Resources res = getResources();
					dialog.setTitle(R.string.NewServerDialogTitle);
					dialog.setMessage(res.getString(R.string.NewServerDialogText));
					dialog.setButton(res.getString(R.string.NewServerDialogButton), new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
					dialog.show();
				}			
			}
		});
	}
}
