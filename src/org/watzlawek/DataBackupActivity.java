package org.watzlawek;

import android.app.ListActivity;
import android.content.Context;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.AdapterContextMenuInfo;

/**
 * Class for the activity for displaying data backup files. * 
 * @author Klaus-Peter Watzlawek * 
 * @version 2013-09-10
 */
public class DataBackupActivity extends ListActivity {
	
	/**
	 * Costume DataBackupAdapter List Adapter: for timestamp of backup
	 */
	protected DataBackupAdapter listadapter;
	
	/**
	 * Saves all DataBackupItems as array for internal use.
	 */
	protected DataBackupItem[] mDataBackupItemArray;
	
	/**
	 * Holds an identifier for the user defined theme.
	 * 
	 * @see AppPrefHandler#getTheme() Possible theme identifiers.
	 */
	private String theme;
	
	public class DataBackupAdapter extends ArrayAdapter<DataBackupItem> {

		/**
		 * Constructor to create DataBackupAdapter.
		 * @param context
		 * @param i
		 * @param dataBackupItemArray
		 */
		public DataBackupAdapter(Context context, int i, DataBackupItem[] dataBackupItemArray) {
			super(context, i, dataBackupItemArray );   
			mDataBackupItemArray = dataBackupItemArray;			
		}
		
		/** 
         * Returns the ID of a data backup item in the data backup list.
         * 
         * @param position Position of an item in the data backup list. 
         * 
         * @return Item iD of an item in the data backup list. 
         */
        @Override
    	public long getItemId(int position) {
    		return position;
    	}
        
        /**
         * Displays content from mDataBackupItemArray.
         */
        @Override
        public View getView(int position, View v, ViewGroup parent) {
            if (v == null) {
                LayoutInflater vi = (LayoutInflater)getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE); 
                v = vi.inflate(getDataBackupListItemLayout(), null);       
            }
            
            String backup_locale = getText(R.string.databackuplistBackups).toString();
            String path_locale = getText(R.string.databackuplistPath).toString();
            
            TextView TextViewDataBackupName = (TextView)v.findViewById(R.id.DataBackupListImportItemTextViewName);
            TextViewDataBackupName.setText(backup_locale +": " + mDataBackupItemArray[position].getBackupname());
            
            TextView TextViewDataBackupPath = (TextView)v.findViewById(R.id.DataBackupListImportItemTextViewPath);
            TextViewDataBackupPath.setText(path_locale + ": " + mDataBackupItemArray[position].getFilepath());
            return v;            
        }		
	}
	
	 	  
	/**
	 * Refreshs the data backup item list and displays backups.
	 */
	private void refreshList() {  
		IMApp app = (IMApp)getApplicationContext();
		DataBackupItem[] items = app.getDataBackupHandler().getDataBackupItemArrayReverse();
	    if(app.getDataBackupHandler().getDatabackupsize() > 0) {	    	
	    	listadapter = new DataBackupAdapter(this, getDataBackupListItemLayout(), items );
	    	setListAdapter(listadapter);
	    }
	    else setListAdapter(null);  
	}
	    
	  /**
	   * This method is called on resume of this activity.
	   * It sets the theme the context menu on the list view and refreshs the data backup item list.
	   */
	  @Override
	  protected void onResume() {
		  super.onResume();
		  AppPrefHandler appprefhandler = new AppPrefHandler(this);
		  theme = appprefhandler.getTheme();
		  refreshList();
		  setContentView(getDataBackuplistLayout());			
		  ListView listview = getListView();
		  listview.setOnCreateContextMenuListener(this);   	 	    	
	  }

	  /**
	   * Getter method for the data backup list element layout depending on the theme.
	   * 
	   * @return Layout identifier:
	   * - R.layout.databackuplistimportitemlight when the light theme is chosen.
	   * - R.layout.databackuplistimportitemdark when the dark theme is chosen.
	   */
	  private int getDataBackupListItemLayout() {
		  if(theme.equals("_light")) return R.layout.databackuplistimportitemlight;
		  else return R.layout.databackuplistimportitemdark;
	  }
	  
	  /**
	   * Getter for the databackup list layout depending on the theme.
	   * 
	   * @return Layout identifier:
	   * - R.layout.databackuplistimportlight when the light theme is chosen.
	   * - R.layout.databackuplistimportdark when the dark theme is chosen.
	   */
	  private int getDataBackuplistLayout() {
		  if(theme.equals("_light")) return R.layout.databackuplistimportlight;
		  else return R.layout.databackuplistimportdark;
	  }	
	  
	  /**
	  * Creates the context menu of the server list.
	  */
	  public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		  menu.setHeaderTitle(R.string.databackuplistContextmenuHeader);
		  menu.add(ContextMenu.NONE, 1, ContextMenu.NONE, R.string.databackuplistContextmenuImport);
		  menu.add(ContextMenu.NONE, 2, ContextMenu.NONE, R.string.databackuplistContextmenuDelete);		  
	  }
	  
	  /**
	   * Executes commands for Contexmenu: 
	   */
	  @Override
	  public boolean onContextItemSelected(MenuItem item) {
		  AdapterContextMenuInfo contextMenuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		  IMApp app =(IMApp)getApplicationContext();
		  int selectedbackup = (int)contextMenuInfo.id;				
		  switch (item.getItemId()) {
		  //Import backup
		  case 1:
			  try {
				  app.getServerManager().disconnect();				  
				  app.getDataBackupHandler().ReadBackupFilesFromDrive();	
				  app.getDataBackupHandler().ResetInternalData();
				  app.getDataBackupHandler().import_sharefprefs(this.mDataBackupItemArray[selectedbackup].getId());
				  app.getDataBackupHandler().import_db(this.mDataBackupItemArray[selectedbackup].getId());		
				  app.ReloadPrefManager();
				  app.ReloadServerManager();
			  } catch (Exception e) {					
				  e.printStackTrace();
			  }
			  return true;		
		  //Delete backup
		  case 2:
			  this.mDataBackupItemArray[selectedbackup].removefile();					
			  this.refreshList();
			  return true;
		  default: {
			  return true;
		  	}			
		  }
	    }
	  }
