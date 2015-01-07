package org.watzlawek;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.widget.Toast;


/**
 * Class for the activity for displaying global application preferences.
 * 
 * @author Klaus-Peter Watzlawek
 * @author Moritz Lipfert
 * 
 * @version 2013-08-21
 */
public class AppPrefActivity extends PreferenceActivity {
	/**
	 * Holds a reference to the about button.
	 */
	private Preference preferenceAbout;
	
	/**
	 * Holds a reference to the data backup import button.
	 */
	private Preference  preferenceDatabackupImport;
	
	/**
	 * Holds a reference to the data backup export button.
	 */
	private Preference  preferenceDatabackupExport;
	
	/**
	 * Holds a reference to the data backup reset button.
	 */
	private Preference  preferenceDatabackupReset;
	
	/**
	 * Holds a reference to the clear-history button.
	 */
	private Preference preferenceClearHistory;
	
	/**
	 * Holds a reference to the button for generating new OTR keys.
	 */
	private Preference preferenceOTR;
	
	/**
	 * Holds a reference to the button for showing OTR fingerprints.
	 */
	private Preference preferenceFingerprint;
	
	/**
	 * Holds a reference to the encryption class object.
	 */
	private Encryption encryption;
	
	/**
	 * This method is called on creation of the activity.
	 * It sets the click listeners for specific preference buttons of the preferences list.
	 */
	@Override
    public void onCreate(Bundle savedInstanceState) {		
        super.onCreate(savedInstanceState);  
        addPreferencesFromResource(R.xml.preferences);
        final IMApp app = (IMApp)getApplicationContext();	
        
        /* Sets the click listener for the about button. */
        preferenceAbout = (Preference)findPreference("PrefAbout");
        preferenceAbout.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference pref) {
				Intent intent = new Intent().setClass(pref.getContext(), AboutActivity.class);
				startActivity(intent);
				return true;
			}
        });
        
        /* Sets the click listener for the DataBackup import button. */
        preferenceDatabackupImport = (Preference)findPreference("PrefDataBackupImport");
        preferenceDatabackupImport.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference pref) {
				Intent intent = new Intent().setClass(pref.getContext(), DataBackupActivity.class);
				startActivity(intent);
							
				/*try {
					app.getServerManager().disconnect();
					app.getDataBackupHandler().import_db();
					app.getDataBackupHandler().import_sharefprefs();
					app.ReloadServerManager();
					//databackuphandler.import_sharedprefs();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
				return true;
			}
        });
        
        /* Alert Dialog for precaution: Does the user really want to delete all data?*/
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle(this.getText(R.string.PrefDataBackupAlertDialogTitleReset).toString());    			
		alertDialog.setMessage(this.getText(R.string.PrefDataBackupAlertDialogMessageReset).toString());
		alertDialog.setIcon(R.drawable.ic_launcher);
		alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, this.getText(R.string.AlertDialogYes).toString(), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) { 
				app.getServerManager().disconnect();				
				try {
					app.getDataBackupHandler().ResetInternalData();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}					
				app.ReloadServerManager();	
			}
		});
		
		alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, this.getText(R.string.AlertDialogNo).toString(), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {/* nothing to do */}		
		});
		
        /* Sets the click listener for the DataBackup reset button. */
        preferenceDatabackupReset = (Preference)findPreference("PrefDataBackupReset");
        preferenceDatabackupReset.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference pref) {			
	    		alertDialog.show();    			
	    		return true;
			}
        }); 
        
        /*
         * 	try {
						if (app.getDataBackupHandler().isDatabasePlain()) {
							Log.v("DATABASE IS", "PLAIN");
						} else
							Log.v("DATABASE IS", "ENCRYPTED");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
         */
        
        /* Sets the click listener for the DataBackup export button. */
        preferenceDatabackupExport = (Preference)findPreference("PrefDataBackupExport");
        preferenceDatabackupExport.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference pref) {					
				try {
					app.getDataBackupHandler().export_db();
					app.getDataBackupHandler().export_sharedprefs();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return true;
			}
        });
        
        /* Sets the click listener for the OTR key generation button. */
        preferenceOTR = (Preference)findPreference("PrefOTR");
        preferenceOTR.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference pref) {			
				IMApp app = (IMApp)getApplicationContext();
				encryption = new Encryption(app);
				encryption.MakeAKeyPair(true);								
				return true;
			}
        });
       
        /* Sets the click listener for the Fingerprint key generation button. */        
        preferenceFingerprint = (Preference)findPreference("PrefFingerprint");
        preferenceFingerprint.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference pref) {
				IMApp app = (IMApp)getApplicationContext();
				encryption = new Encryption(app);
				IMServer connectedServer = app.getServerManager().getConnectedServer();
				String ownfingerprint = "";
				
				if (connectedServer != null) {
					
					ownfingerprint = encryption.getOwnInternalFingerprint();					
					if (ownfingerprint != "NULL") {
						AlertDialog alertDialog = new AlertDialog.Builder(pref.getContext()).create();
						alertDialog.setTitle(app.getText(R.string.PrefFingerprintAlertTitle).toString());    			
						alertDialog.setMessage(ownfingerprint);
						alertDialog.setButton(-2, "OK", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {    			    
							}
						});
						alertDialog.setIcon(R.drawable.ic_launcher);
						alertDialog.show();	
					}
					else {
						Resources res = app.getResources();
						Toast toast = Toast.makeText(pref.getContext(), res.getText(R.string.chatOtrNoKeyPair), Toast.LENGTH_LONG);
						toast.show();
					}
				}
				else {
					Resources res = app.getResources();
					Toast toast = Toast.makeText(pref.getContext(), res.getText(R.string.serverErrorNotConnectedTo), Toast.LENGTH_LONG);
					toast.show();
				}
				return true;
			}
        });
        
        /* Sets the click listener for deleting message histories. */
        preferenceClearHistory = (Preference)findPreference("historyClearAllNow");
        preferenceClearHistory.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
				IMApp app = (IMApp)getApplicationContext();
				app.getServerManager().disconnect();
				
				ServerDatabaseHandler dbhandler = new ServerDatabaseHandler(preference.getContext());
				dbhandler.clearHistory();				
				
				Resources res = app.getResources();
				Toast toast = Toast.makeText(preference.getContext(), res.getText(R.string.historyCleared), Toast.LENGTH_LONG);
				toast.show();
				
				return true;
			}
		});
        
    }
}
