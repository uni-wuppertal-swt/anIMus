package org.watzlawek.contactmanager;


import java.util.*;

import net.sqlcipher.database.SQLiteException;

import org.watzlawek.*;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ContactDatabaseHandler extends SQLiteOpenHelper{

	
	/**
	 * Name of the database.
	 */
	private static final String DATABASE_NAME = "ContactDB";
	
	/**
	 * Version of the database.
	 */
	private static final int DATABASE_VERSION = 1;
	
	/**
	 * Name of the table which holds the contacts.
	 */
	private static final String DB_TABLE_NAME = "contacts";
	
	
	private static final String DB_COLUMN_ID = "_id";
	/**
	 * Primary key column of the table "contacts".
	 */
	private static final String DB_COLUMN_JID = "jid";
	
	/**
	 * Column of the table "contacts".
	 * It includes the usernames.
	 */
	private static final String DB_COLUMN_USERNAME = "username";
	
	/**
	 * Column of the table "contacts".
	 * It includes the notes.
	 */
	private static final String DB_COLUMN_NOTE = "note";
	
	/**
	 * Column of the table "contacts".
	 * It includes the serverID.
	 */
	private static final String DB_COLUMN_SERVERID = "serverid";
	
	/**
	 * Column of the table "contacts".
	 * Stores, if the user is visible in the contactlist.
	 */
	private static final String DB_COLUMN_VISIBLE = "visible";
	

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

	public ContactDatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public void onCreate(SQLiteDatabase db) {
		String createDB = "CREATE TABLE " +DB_TABLE_NAME +"(" +
				DB_COLUMN_JID +" VARCHAR(30),"+
				DB_COLUMN_USERNAME +" VARCHAR(30)," +
				DB_COLUMN_NOTE +" VARCHAR(140),"+
				DB_COLUMN_SERVERID + " INTEGER,"+
				DB_COLUMN_VISIBLE +" INTEGER, PRIMARY KEY(" + 
				DB_COLUMN_JID + ", " + DB_COLUMN_SERVERID + "));";
		db.execSQL(createDB);
	}

	/**
	 * 
	 * @param db
	 * @param oldVersion
	 * @param newVersion
	 */
	public void onUpgrade(android.database.sqlite.SQLiteDatabase db, int oldVersion, int newVersion) {
		String command = "DROP TABLE IF EXISTS " + DATABASE_NAME +";";
		db.execSQL(command);
		onCreate(db);
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
		Collections.sort(neededContacts, new Comparator<ContactDatabaseHandler.Contact>() {
			public int compare(ContactDatabaseHandler.Contact c1, ContactDatabaseHandler.Contact c2) {
				return c1.jid.compareTo(c2.jid);
			}
		});
		
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
					updateContact(sc.jid, sc.username, sc.note, sc.serverID);
					scIndex++;
					ncIndex++;
				}
			}
			
			// A contact in the DB is no more on the server, delete it from the DB.
			else {
				deleteContact(nc.jid, nc.serverID);
				ncIndex++;
			}
		}
		
		// If the ncIndex has not reached the end of the vector, complete it (case 1)
		while (ncIndex < neededContacts.size()) {
			deleteContact(neededContacts.elementAt(ncIndex).jid, neededContacts.elementAt(ncIndex).serverID);
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

	
	private void deleteContact(String delJID, int delServerID) {
		try {
			SQLiteDatabase db = getWritableDatabase();		

			if (db != null) {
				String sqlCommand = "DELETE FROM " + DB_TABLE_NAME + " WHERE " 
						+ DB_COLUMN_JID + " = '" + delJID + "' AND " 
						+ DB_COLUMN_SERVERID + " = '" + delServerID + "';";			
				db.execSQL(sqlCommand);
				db.close();
			}
		}
		catch (SQLiteException e) {}
	}
	
	
	private Vector<Contact> getDBContacts() {
		Vector<Contact> contactList = new Vector<Contact>();
		SQLiteDatabase db = null;
		String sqlCommand = "SELECT * FROM " + DB_TABLE_NAME +";";
		try {
			db = getReadableDatabase();
			
			if (db != null) {
				android.database.Cursor queryCursor = db.rawQuery(sqlCommand, null);
			
				if(queryCursor.moveToFirst()) {
					do {
						Contact currentContact;
						currentContact = new Contact(queryCursor.getString(0), queryCursor.getString(1), 
								queryCursor.getString(2), Integer.parseInt(queryCursor.getString(3)), 
								(Integer.parseInt(queryCursor.getString(4)) == 1 ? true : false));

						contactList.add(currentContact);
					} 	while(queryCursor.moveToNext());
				}
				queryCursor.close();
				db.close();
			}
			
		}
		catch (SQLiteException e){
			
		}
		
		
		return contactList;
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
		
		return jidUserV;
	}
	
	
	private boolean getState(String inJID, int inServerID) {
		Boolean b = false;
		try {
			SQLiteDatabase db = getReadableDatabase();
			if (db != null) {
				String sqlCommand = "SELECT " + DB_COLUMN_VISIBLE 
						+ " FROM " + DB_TABLE_NAME 
						+ " WHERE " + DB_COLUMN_JID + " = '" + inJID 
						+ "' AND " + DB_COLUMN_SERVERID + " = " + inServerID + ";";			
				
				
				android.database.Cursor queryCursor = db.rawQuery(sqlCommand, null);
				
				if(queryCursor.moveToFirst()) {
						b = (Integer.parseInt(queryCursor.getString(0)) == 1 ? true : false);
				}
				queryCursor.close();
				db.close();
			}
		} 
		catch(SQLiteException e) {	
			
		}
		return b;
	}
	
	
	public Vector<IMChat> getVisibleContacts(Vector<IMChat> contacts, int serverID) {
		Vector<IMChat> vContacts = new Vector<IMChat>();
		
		for (IMChat imc : contacts) {
			if (getState(((XMPPChat)imc).get_jid(), imc.get_serverId())){
				vContacts.add(imc);
			}
		}
		
		return vContacts;
	}

	/**
	 * 
	 *  This method inserts a given contact into the DB if it doesn't already exist.
	 * @param inJID
	 * @param inUsername
	 * @param inNote
	 * @param inSID
	 * @param inVisible
	 */
	public void insertContact(String inJID, String inUsername, String inNote, 
			int inSID, boolean inVisible) {
		
		try {
			SQLiteDatabase db = getWritableDatabase();
			if (db != null) {// TO DO; SQL Syntax Fehler Fixen
				int visible = (inVisible ? 1 : 0);
				String sqlCommand = "INSERT INTO " + DB_TABLE_NAME + " VALUES ('" + inJID +  "', '"
						+inUsername + "', '"+ inNote + "', " + inSID + ", " + visible + ");";
                Log.v("Ausgabe-SQL-Insert", sqlCommand);
                db.execSQL(sqlCommand);
				
				/*
				// Create a new map of values, where column names are the keys
				ContentValues values = new ContentValues();
				values.put(ContactDatabaseHandler.DB_COLUMN_JID, inJID);
				values.put(ContactDatabaseHandler.DB_COLUMN_USERNAME, inUsername);
				values.put(ContactDatabaseHandler.DB_COLUMN_NOTE , inNote);
				values.put(ContactDatabaseHandler.DB_COLUMN_SERVERID , inSID);
				values.put(ContactDatabaseHandler.DB_COLUMN_VISIBLE , inVisible);
				

				// Insert the new row, returning the primary key value of the new row
				long newRowId;
				newRowId = db.insert(
						ContactDatabaseHandler.DB_TABLE_NAME,
						null,
				         values);
				*/
				
				db.close();
			}
		} 
		catch(SQLiteException e) {	
			
		}
	}

	
	/*private Vector<String> readJIDs(int serverID) {
		Vector<String> jidVector = new Vector<String>();
		// "Select jid from contacts where serverid = " +in_SID
		Vector<Contact> dbContacts = getDBContacts();
		
		for (Contact dbc : dbContacts) {
			if (dbc.serverID == serverID) 
				jidVector.add(dbc.jid);
		}
		
		return jidVector;
	}*/
	
	

	
	/**
	 * 
	 * @param inJID
	 * @param inUsername
	 * @param inNote
	 * @param inServerID
	 * @param inVisible
	 */
	private void updateContact(String inJID, String inUsername, String inNote, 
			int inServerID) {
		
		try {
			SQLiteDatabase db = getWritableDatabase();		

			if (db != null) {
				String sqlCommand = "UPDATE " + DB_TABLE_NAME + " SET " + DB_COLUMN_USERNAME + " = '" 
						+ inUsername + "', " + DB_COLUMN_NOTE + " = '" + inNote + "' WHERE " 
						+ DB_COLUMN_JID + " = '" + inJID	+ "' AND " 
						+ DB_COLUMN_SERVERID + " = " + inServerID + ";";			
				db.execSQL(sqlCommand);
				db.close();
			}
		}
		catch (SQLiteException e) {}
		
	}
	
	
	/**
	 * 
	 * @param inJID
	 * @param inUsername
	 * @param inNote
	 * @param inServerID
	 * @param inVisible
	 */
	public void updateContact(String inJID, String inUsername, String inNote, 
			int inServerID, boolean inVisible) {
		
		try {
			SQLiteDatabase db = getWritableDatabase();		

			if (db != null) {
				int visible = (inVisible ? 1 : 0);
				String sqlCommand = "UPDATE " + DB_TABLE_NAME + " SET " + DB_COLUMN_USERNAME + " = '" 
						+ inUsername + "', " + DB_COLUMN_NOTE + " = '" + inNote + "', " + DB_COLUMN_VISIBLE
						+ " = " + visible + " WHERE " + DB_COLUMN_JID + " = '" + inJID
						+ "' AND " + DB_COLUMN_SERVERID + " = " + inServerID + ";";			
				db.execSQL(sqlCommand);
				db.close();
			}
		}
		catch (SQLiteException e) {}
		
	}

}
