package org.watzlawek;

import org.watzlawek.R;

import android.app.ListActivity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Class for the activity for setting the own connection status.
 * 
 * @author Klaus-Peter Watzlawek
 * @author Moritz Lipfert
 * 
 * @version 2012-11-14
 */
public class StatusActivity extends ListActivity {
	
	/**
	 * Array of possible status identifiers.
	 */
	private String[] status;
	
	/**
	 * This method will get called on creation of the activity.
	 * It initializes the list view which shows the possible status values.
	 * 
	 * @param savedInstanceState A saved instance of this activity. This parameter is used internally by the application.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Resources res = getResources();
		status = new String[] { res.getString(R.string.statusAvailable), res.getString(R.string.statusAway), res.getString(R.string.statusExtendedAway), res.getString(R.string.statusFreeToChat), res.getString(R.string.statusDnd) };
		setListAdapter(new ArrayAdapter<String>(this, R.layout.statuslistelement, status));
		
		final IMApp app = (IMApp)getApplicationContext();
		ListView listView = getListView();
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {				
				if (app.getServerManager().getConnectedServer() != null) {				
				switch(position) {
					case 0: {
						app.getServerManager().setStatus(IMServer.Status.available);
						finish();
						break;
					}
					case 1: {
						app.getServerManager().setStatus(IMServer.Status.away);
						finish();
						break;
					}
					case 2: {
						app.getServerManager().setStatus(IMServer.Status.extendedAway);
						finish();
						break;
					}
					case 3: {
						app.getServerManager().setStatus(IMServer.Status.freeToChat);
						finish();
						break;
					}
					case 4: {
						app.getServerManager().setStatus(IMServer.Status.dnd);
						finish();
						break;
					}
					default: {
						finish();
						break;
					}					
				}
			}}
		});		
	}
}
