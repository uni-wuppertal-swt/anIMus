package org.watzlawek.contactmanager;


import java.util.*;

import net.sqlcipher.database.SQLiteException;

import org.watzlawek.*;

import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;  
import android.util.Log;


public class ContactDatabaseHandler extends SQLiteOpenHelper{

	// private static String DB_PATH = "/data/data/org.watzlawek.contactmanager/databases/";
	// private SQLiteDatabase ContactDB;
	
	private static final String DATABASE_NAME = "ContactDB";
	private static final int DATABASE_VERSION = 1;
	private static final String DB_TABLE_NAME = "contacts";
	private static final String DB_COLUMN_JID = "jid";
	private static final String DB_COLUMN_USERNAME = "username";
	private static final String DB_COLUMN_NOTE = "note";
	private static final String DB_COLUMN_SERVERID = "serverid";
	private static final String DB_COLUMN_INVISIBLE = "invisible";
	

	private class Contact{
		public String jid;
		public String username;
		public String note;
		public int serverID;
		public boolean visible;
		
		public Contact(String inJID, String inUsername, String inNote, int inSID, boolean inVisible) {
			jid = inJID;
			username = inUsername;
			note = inNote;
			serverID = inSID;
			visible = inVisible;
		}
		
		@Override
		public boolean equals(Object o) {
			if (!((o.getClass()).getName()).equals("Contact")) { return false; }
			Contact c = (Contact)o;
			if (!jid.equals(c.jid)) { return false; }
			if (!username.equals(c.username)) { return false; }
			if (!note.equals(c.note)) { return false; }
			if (serverID != c.serverID) { return false; }
			if (visible != c.visible) { return false; }
			return true;
		}
	}

	public ContactDatabaseHandler(android.content.Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public void onCreate(android.database.sqlite.SQLiteDatabase db) {
		String createDB = "CREATE TABLE " +DB_TABLE_NAME +"(" +
				DB_COLUMN_JID +" VARCHAR(30) PRIMARY KEY,"+
				DB_COLUMN_USERNAME +" VARCHAR(30)" +
				DB_COLUMN_NOTE +" VARCHAR(140),"+
				DB_COLUMN_SERVERID + " INTEGER"+
				DB_COLUMN_INVISIBLE +" INTEGER)";
		db.execSQL(createDB);
	}

	/**
	 * 
	 * @param db
	 * @param oldVersion
	 * @param newVersion
	 */
	public void onUpgrade(android.database.sqlite.SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}


	
	private void open_db() throws SQLException{ // oeffnet oder erstellt die Datenbank
	//	SQLiteDatabase db = SQLiteDatabase.openDatabase(DATABASE_NAME,
		//		null, SQLiteDatabase.CREATE_IF_NECESSARY, null);
		
		
		/**
		 *  String contactPath = DB_PATH + DB_NAME;
			contactDataBase = SQLiteDatabase.openDatabase(contactPath, null, SQLiteDatabase.OPEN_READONLY);
			READONLY ist doof, das müssen wir noch ändern
		 */
		//oder 
		/** private MySQLiteHelper dbHelper;
		 *  private SQLiteDatabase database;
		 * database = dbHelper.getWritableDatabase();
		 */
		
		
	}

	
	
	private void close_db() { 
		// TODO - implement ContactDatabaseHandler.close_db
		throw new UnsupportedOperationException();
		
		//Class: "SQLiteClosable" 	An object created from a SQLiteDatabase that can be closed.
		// http://developer.android.com/reference/android/database/sqlite/SQLiteDatabase.html
		// oder
		/**
		 * private MySQLiteHelper dbHelper;
		 *  dbHelper.close();
		 */
	}

	
	
	/**
	 * This method compares the contacts on the server(serverID) with the 
	 * corresponding contacts in the DB. It pulls the contacts of the DB
	 * and compares then.
	 * @param contacts
	 */
	public void compareContacts(Vector<IMChat> contacts, int serverID) {
		
		Vector<Contact> dbContacts = getDBContacts();
		
		/*
		 * Create a Vector<Contact> where only contacts are saved,
		 * which are available on the currently connected server
		 * Visibility is ignored because invisible contacts should
		 * not be inserted in the DB again. 
		 */
		Vector<Contact> neededContacts = new Vector<Contact>();
		for (Contact c : dbContacts){
			if (c.serverID == serverID) 
				neededContacts.add(c);
		}
		
		// Create a new Vector<Contact>, which extracts all important 
		// information of IMChat and stores it. 
		Vector<Contact> serverContacts = new Vector<Contact>();
		for (IMChat c : contacts) {
			serverContacts.add(new Contact(
					((XMPPChat)c).get_jid(), c.get_username(), c.get_note(), serverID, c.isVisible()));
		}
		
		// Now the comparison can start:
		int ncIndex = 0;
		int scIndex = 0;
		Contact nc;
		Contact sc;
		
		while (ncIndex < neededContacts.size() && scIndex < serverContacts.size()){
			nc = neededContacts.elementAt(ncIndex);
			sc = serverContacts.elementAt(scIndex);
			
			// A contact on the server is not available in the DB - insert!
			if (nc.jid.compareTo(sc.jid) > 0) {
				insertContact(sc.jid, sc.username, sc.note, sc.serverID, sc.visible);
				scIndex++;
			}
			
			// Both jids are equal, compare other entries, if they have to be updated 
			//in the DB
			else if (nc.jid.compareTo(sc.jid) == 0) {
				if (!nc.equals(sc)) {
					updateContact(sc.jid, sc.username, sc.note, sc.serverID, sc.visible);
					scIndex++;
					ncIndex++;
				}
			}
			
			// A contact in the DB is no more on the server, delete it from the DB.
			else {
				deleteContact(nc.jid);
				ncIndex++;
			}
		}
		
		// If the ncIndex has not reached the end of the vector, complete it (case 1)
		while (ncIndex < neededContacts.size()) {
			deleteContact(neededContacts.elementAt(ncIndex).jid);
			ncIndex++;
		}
		
		// If the scIndex has not reached the end of the vector, complete it (case 2,
		// is only possible, if case 1 did not occur).
		while (scIndex < serverContacts.size()) {
			sc = serverContacts.elementAt(scIndex);
			insertContact(sc.jid, sc.username, sc.note, sc.serverID, sc.visible);
			scIndex++;
		}
	}

	
	private void deleteContact(String delJID) {
		
	}
	
	
	private Vector<Contact> getDBContacts() {
		// This is the real reading-method with DB access.
		// ...
		return new Vector<Contact>(); // Dummy
	}
	
	
	public Vector<String> getJIDsUsernames(int serverID) {
		Vector<Contact> dbContacts = getDBContacts();
		/* 
		 * Here must be filtered: Is the contact in the ServerID, which is 
		 * requesting this List and is the contact visible?
		 */
		Vector<String> jidUserV = new Vector<String>();
		
		for (Contact dbc : dbContacts) {
			if (dbc.serverID == serverID && dbc.visible == true) {
				jidUserV.add(dbc.jid);
				jidUserV.add(dbc.username);
			}
		}
		
		return jidUserV; // Dummy
	}
	
	
	
	/**
	 * This method inserts a given contact into the DB if it doesn't already exist.
	 * 
	 * @param in_jid
	 * @param in_username
	 * @param in_note
	 * @param in_SID
	 * @param in_invisible
	 * 
	 */
	private void insertContact(String in_jid, String in_username, String in_note, 
			int in_SID, boolean in_invisible) {
		//private SQLiteStatement  statement = null;       // SQL Anweisung
	    //private ...  result   = null;       		// SQL Ergebnis
		
		//throw new UnsupportedOperationException();
		//https://www.youtube.com/watch?v=dOA8RkTr5AI
		
		/**
		 * try {
			SQLiteDatabase db = getWritableDatabase("");
			if (db != null) {
				String sqlCommand = "Select ..." +SID;			
				db.execSQL(sqlCommand);
			}
		} 
		catch(SQLiteException e) {	
			
		}
		
		 */
		
		
	}

	
	private Vector<String> readJIDs(int serverID) {
		Vector<String> jidVector = new Vector<String>();
		// "Select jid from contacts where serverid = " +in_SID
		Vector<Contact> dbContacts = getDBContacts();
		
		for (Contact dbc : dbContacts) {
			if (dbc.serverID == serverID) 
				jidVector.add(dbc.jid);
		}
		
		return jidVector;
	}
	
	
	/**
	 * 
	 * @param in_jid
	 * @param in_username
	 * @param in_note
	 * @param in_invisible
	 */
	private void updateContact(String in_jid, String in_username, String in_note, 
			int serverID, boolean in_invisible) {
		// TODO - implement ContactDatabaseHandler.updateContact
		throw new UnsupportedOperationException();
		
		/**
		 * SQLiteDatabase db = getWritableDatabase(password);		
		ContentValues tokenValues = new ContentValues();
		
		tokenValues.put(TOKENSYSTEM_DB_TABLE_XMPPJID, in_xmppjid);
		tokenValues.put(TOKENSYSTEM_DB_TABLE_OWNIDENTIFIER, in_identifier);
		tokenValues.put(TOKENSYSTEM_DB_TABLE_PASSWORD, in_startpassword);
		tokenValues.put(TOKENSYSTEM_DB_TABLE_SERVICEADDRESS, in_serviceaddress);
		
		tokenValues.put(TOKENSYSTEM_DB_TABLE_SERVICEACOUNT, "");
		tokenValues.put(TOKENSYSTEM_DB_TABLE_AUTHTOKEN, "");
		
		
		
		
		db.insert(TOKENSYSTEM_DB_TABLE, null, tokenValues);
		db.close();
		 */
	}

}
