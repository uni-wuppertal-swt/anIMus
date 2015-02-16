package encryption.org.watzlawek;

import java.util.*;
import net.sqlcipher.database.SQLiteException;
import org.watzlawek.*;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class KeySetDB extends SQLiteOpenHelper{


/**
* Name of the database.
*/
private static final String DATABASE_NAME = "ContactDB";
/**
* Version of the database.
*/
private static final int DATABASE_VERSION = 1;
/**
* Name of the table which holds the keysets.
*/
private static final String DB_TABLE_NAME1 = "tblkeyset";
private static final String DB_TABLE_NAME2 = "tbljidset";
/**
* Primary key column of the table "contacts".
*/
private static final String DB_COLUMN_HASH = "hash";
private static final String DB_COLUMN_JIDGROUP = "id";
private static final String DB_COLUMN_COREID = "coreid";
private static final String DB_COLUMN_ALLREADYSEND = "allreadysend";
private static final String DB_COLUMN_KEYDATATYPE = "keydatatype";
private static final String DB_COLUMN_ALGORITHM = "algorithm";
private static final String DB_COLUMN_KEYLENGTH = "keylength";
private static final String DB_COLUMN_SALTEDKEY = "saltedkey";

private static final String DB_COLUMN_ID = "id INT";
private static final String DB_COLUMN_JIDLIST = "jidlist TEXT";


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

private int id_JID;

	
	public KeySetDB(Context context, String jidIdent ) throws EncryptionFaultException {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		
		SQLiteDatabase db = this.getWritableDatabase();

		if (db == null) throw new EncryptionFaultException();
		
		String sqlCommand = 
				"SELECT " + DB_COLUMN_ID + 
				" FROM " + DB_TABLE_NAME2 + 
				" WHERE " + DB_COLUMN_JIDLIST + "=='" + jidIdent + "';";
		
		android.database.Cursor queryCursor = db.rawQuery(sqlCommand, null);
		
		if(queryCursor.moveToFirst()) {
			this.id_JID = Integer.parseInt(queryCursor.getString(0));
			queryCursor.close();
			}
		else {
			queryCursor.close();
			
			this.id_JID = findNextID(db);
		
			sqlCommand = "INSERT INTO " + DB_TABLE_NAME2 + " VALUES (" + this.id_JID + ", '"
					+ jidIdent + "');";

					db.execSQL(sqlCommand);
		

		}	
			
			
			db.close();
	}

	private int findNextID(SQLiteDatabase db){
		int res = 0;
		String sqlCommand = 
				"SELECT " + DB_COLUMN_ID + 
				" FROM " + DB_TABLE_NAME2 + 
				" ORDER BY " + DB_COLUMN_ID + " DESC ;";

		android.database.Cursor queryCursor = db.rawQuery(sqlCommand, null);
		if(queryCursor.moveToFirst())res = Integer.parseInt(queryCursor.getString(0)); 
		queryCursor.close();
		return res + 1;
	}
	
	public int getid(){return id_JID;}
	
	/**
	* Called on creating an object.
	*/
	public void onCreate(SQLiteDatabase db) {
	String createDB1 = "CREATE TABLE " +DB_TABLE_NAME1 +"(" +
			DB_COLUMN_HASH + " CHAR(40)" +
			DB_COLUMN_JIDGROUP + " INT" +
			DB_COLUMN_COREID + " VARCHAR(10)" +
			DB_COLUMN_ALLREADYSEND + " SMALLINT" +
			DB_COLUMN_KEYDATATYPE + " VARCHAR(5)" +
			DB_COLUMN_ALGORITHM + " VARCHAR(10)" +
			DB_COLUMN_KEYLENGTH + " SMALLINT" +
			DB_COLUMN_SALTEDKEY + " BLOB NULL" +
			", PRIMARY KEY (" + DB_COLUMN_HASH + "));";
	db.execSQL(createDB1);
	
	String createDB2 = "CREATE TABLE " +DB_TABLE_NAME2 +"(" +
			DB_COLUMN_ID + " CHAR(40)" +
			DB_COLUMN_JIDLIST + " VARCHAR(10)" +
			", PRIMARY KEY (" + DB_COLUMN_ID + "));";
	db.execSQL(createDB2);

	
	}
	
	/**
	 * -
	 * @param db
	 * @param oldVersion
	 * @param newVersion
	 */
	public void onUpgrade(android.database.sqlite.SQLiteDatabase db, int oldVersion, int newVersion) {
		String command = "DROP TABLE IF EXISTS " + DB_TABLE_NAME1 +";";
		db.execSQL(command);
		command = "DROP TABLE IF EXISTS " + DB_TABLE_NAME2 +";";
		db.execSQL(command);
		onCreate(db);
	}
	
	
	
	
	
}
