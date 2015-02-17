package encryption.org.watzlawek;

import java.util.*;
import net.sqlcipher.database.SQLiteException;
import org.watzlawek.*;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class KeySetDB extends SQLiteOpenHelper{


/**
* Name of the database.
*/
private static final String DATABASE_NAME = "EncryptionDB";
/**
* Version of the database.
*/
private static final int DATABASE_VERSION = 1;
/**
* Name of the table which holds the keysets.
*/
private static final String DB_TABLE_NAME1 = "tblkeyset";
private static final String DB_TABLE_NAME2 = "tbljidset";

private static final String DB_COLUMN_HASH = "hash";
private static final String DB_COLUMN_JIDGROUP = "id";
private static final String DB_COLUMN_COREID = "coreid";
private static final String DB_COLUMN_ALLREADYSEND = "allreadysend";
private static final String DB_COLUMN_KEYDATATYPE = "keydatatype";
private static final String DB_COLUMN_ALGORITHM = "algorithm";
private static final String DB_COLUMN_KEYLENGTH = "keylength";
private static final String DB_COLUMN_SALTEDKEY = "saltedkey";

private static final String DB_COLUMN_ID = "_id";
private static final String DB_COLUMN_JIDLIST = "jidlist";


private int id_JID;

	
	public KeySetDB(Context context, String jidIdent ) throws EncryptionFaultException, SQLiteException {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		Toast.makeText(context.getApplicationContext(), jidIdent , Toast.LENGTH_LONG).show();
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
		if(queryCursor.moveToFirst())res = queryCursor.getInt(0); 
		queryCursor.close();
		return res + 1;
	}
	
	public int getid(){return id_JID;}
	
	/**
	* Called on creating an object.
	*/
	public void onCreate(SQLiteDatabase db) {
		
		String createDB1 = "CREATE TABLE " +DB_TABLE_NAME1 +"(" +
			DB_COLUMN_HASH + " TEXT," +
			DB_COLUMN_JIDGROUP + " INT," +
			DB_COLUMN_COREID + " VARCHAR(10)," +
			DB_COLUMN_ALLREADYSEND + " SMALLINT," +
			DB_COLUMN_KEYDATATYPE + " VARCHAR(5)," +
			DB_COLUMN_ALGORITHM + " VARCHAR(10)," +
			DB_COLUMN_KEYLENGTH + " SMALLINT," +
			DB_COLUMN_SALTEDKEY + " TEXT NULL," +
			", PRIMARY KEY (" + DB_COLUMN_HASH + "));";
	db.execSQL(createDB1);

	String createDB2 = "CREATE TABLE " +DB_TABLE_NAME2 +"(" +
			DB_COLUMN_ID + " VARCHAR(40)," +
			DB_COLUMN_JIDLIST + " VARCHAR(160)" +
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
		String command;
		command = "DROP TABLE IF EXISTS " + DB_TABLE_NAME1 +";";
		db.execSQL(command);
		command = "DROP TABLE IF EXISTS " + DB_TABLE_NAME2 +";";
		db.execSQL(command);
		onCreate(db);
	}
	
	
	public Vector<SaltedAndPepperedKey> requestKeys( String coreid, int many ){
		Vector<SaltedAndPepperedKey> res = new Vector<SaltedAndPepperedKey>();
		
		byte[] key = new byte[22];
		
		
		
		for(int i = 0; i < key.length;i++)
		{
			key[i] = 0x34;
		}		
		
		
		for(int j = 0; j < many;j++)
		{
			res.add(new SaltedAndPepperedKey(key, 20, "AES", "RAW", "TextSecureCore"));
		}
		
		
		return res;
	}
	
	void setKey(Vector<SaltedAndPepperedKey> keys){
		
	}
	
	SaltedAndPepperedKey getKey(String core){
		byte[] key = new byte[22];
		
		
		
		for(int i = 0; i < key.length;i++)
		{
			key[i] = 0x34;
		}
		
		return new SaltedAndPepperedKey(key, 20, "AES", "RAW", core);
		
	}
	
	
	SaltedAndPepperedKey getKey(byte[] hash){
		
		byte[] key = new byte[22];
		
		
		
		for(int i = 0; i < key.length;i++)
		{
			key[i] = 0x34;
		}
		
		return new SaltedAndPepperedKey(key, 20, "AES", "RAW", "TextSecureCore");
	}
	
	public int getManyOfKeys(String core){
		int res = -1;
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "SELECT count(*) AS total FROM " + DB_TABLE_NAME1 + 
				" WHERE " + DB_COLUMN_COREID + "=" + core + ";";
		
		android.database.Cursor queryCursor = db.rawQuery(sql, null);
		if(queryCursor.moveToFirst())res = queryCursor.getInt(0); 
		queryCursor.close();
		db.close();
		return res;
	}
	
}
