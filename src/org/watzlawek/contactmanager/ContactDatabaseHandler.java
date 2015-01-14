package org.watzlawek.contactmanager;


import java.util.*;
import org.watzlawek.*;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class ContactDatabaseHandler extends SQLiteOpenHelper{

	private static final String DATABASE_NAME = "ContactDB";
	private static final int DATABASE_VERSION = 1;
	private static final String DB_TABLE_NAME = "contacts";
	private static final String DB_COLUMN_JID = "jid";
	private static final String DB_COLUMN_INVISIBLE = "invisible";
	private static final String DB_COLUMN_NOTE = "note";
	private static final String DB_COLUMN_USERNAME = "username";


	public ContactDatabaseHandler(android.content.Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public void onCreate(android.database.sqlite.SQLiteDatabase db) {
		String createDB = "CREATE TABLE " +DB_TABLE_NAME +"(" +
				DB_COLUMN_JID +" VARCHAR(30) PRIMARY KEY,"+
				DB_COLUMN_INVISIBLE +" INTEGER,"+
				DB_COLUMN_NOTE +" VARCHAR(140),"+
				DB_COLUMN_USERNAME +" VARCHAR(30))";
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


	
	private void open_db() { // oeffnet oder erstellt die Datenbank
	//	SQLiteDatabase db = SQLiteDatabase.openDatabase(DATABASE_NAME,
		//		null, SQLiteDatabase.CREATE_IF_NECESSARY, null);
	}

	
	
	private void close_db() { 
		// TODO - implement ContactDatabaseHandler.close_db
		throw new UnsupportedOperationException();
	}

	
	
	private Vector<String> read_jids() {
		// TODO - implement ContactDatabaseHandler.read_jids
		throw new UnsupportedOperationException();
	}

	
	
	/**
	 * 
	 * @param contacts
	 */
	public void compareContacts(Vector<IMChat> contacts) {
		// is changed in a moment...
	}

	
	
	/**
	 * 
	 * @param in_jid
	 * @param in_note
	 * @param in_invisible
	 */
	private void insertContact(String in_jid, String in_note, int in_invisible) {
		// TODO - implement ContactDatabaseHandler.insertContact
		throw new UnsupportedOperationException();
	}

	
	
	/**
	 * 
	 * @param in_jid
	 * @param in_username
	 * @param in_note
	 * @param in_invisible
	 */
	private void updateContact(String in_jid, String in_username, String in_note, int in_invisible) {
		// TODO - implement ContactDatabaseHandler.updateContact
		throw new UnsupportedOperationException();
	}

	
	
	public HashMap<String, String> getJIDsUsernames() {
		// TODO - implement ContactDatabaseHandler.getJIDsUsernames
		throw new UnsupportedOperationException();
	}

}
