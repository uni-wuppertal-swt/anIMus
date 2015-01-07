package org.watzlawek;

import android.app.Activity;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

/**
 * Activity for displaying the about screen with information about the developers and the licenses of the app itself and the used 3rd party libraries.
 *
 * @author Klaus-Peter Watzlawek
 * @author Moritz Lipfert
 * 
 * @version 2012-11-14
 */
public class AboutActivity extends Activity {
	
	/**
	 * This method is called on creation of the activity.
	 * It sets the TextView for the app version to the current software version.
	 * TextViews which describe the 3rd party libraries become linked to the websites which display their licenses.
	 *
	 * @param savedInstanceState Represents a saved instance of the activity. This parameter is used internally by the app.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		
		TextView textViewVersion = (TextView)findViewById(R.id.textViewVersion);
		String version = "0";
		try {
			version = getPackageManager().getPackageInfo(getPackageName(), 0 ).versionName;
		} catch (NameNotFoundException e) {}
		textViewVersion.setText(version);
		
		TextView textViewLicenseJabberIcon = (TextView)findViewById(R.id.textViewJabberIcon);
		textViewLicenseJabberIcon.setMovementMethod(LinkMovementMethod.getInstance());
		textViewLicenseJabberIcon.setLinkTextColor(Color.LTGRAY);
		
		TextView textViewLicenseSmack = (TextView)findViewById(R.id.textViewSmack);
		textViewLicenseSmack.setMovementMethod(LinkMovementMethod.getInstance());
		textViewLicenseSmack.setLinkTextColor(Color.LTGRAY);
		
		TextView textViewLicenseOTR = (TextView)findViewById(R.id.textViewOTR);
		textViewLicenseOTR.setMovementMethod(LinkMovementMethod.getInstance());
		textViewLicenseOTR.setLinkTextColor(Color.LTGRAY);
		
		TextView textViewLicenseBouncyCastle = (TextView)findViewById(R.id.textViewBouncyCastle);
		textViewLicenseBouncyCastle.setMovementMethod(LinkMovementMethod.getInstance());
		textViewLicenseBouncyCastle.setLinkTextColor(Color.LTGRAY);
		
		TextView textViewLicenseJzlib = (TextView)findViewById(R.id.textViewJzlib);
		textViewLicenseJzlib.setMovementMethod(LinkMovementMethod.getInstance());
		textViewLicenseJzlib.setLinkTextColor(Color.LTGRAY);
		
		TextView textViewLicenseSQLCipger = (TextView)findViewById(R.id.textViewSQLCipher);
		textViewLicenseSQLCipger.setMovementMethod(LinkMovementMethod.getInstance());
		textViewLicenseSQLCipger.setLinkTextColor(Color.LTGRAY);
	}
}
