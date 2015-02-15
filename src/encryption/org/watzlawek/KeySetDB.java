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
private static final String DB_TABLE_NAME = "tblkeyset";
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

	
	public KeySetDB(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		
	
	}

	/**
	* Called on creating an object.
	*/
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
	 * -
	 * @param db
	 * @param oldVersion
	 * @param newVersion
	 */
	public void onUpgrade(android.database.sqlite.SQLiteDatabase db, int oldVersion, int newVersion) {
		//String command = "DROP TABLE IF EXISTS " + DATABASE_NAME +";";
		//db.execSQL(command);
		//onCreate(db);
	}
	
}
