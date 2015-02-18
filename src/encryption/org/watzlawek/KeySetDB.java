package encryption.org.watzlawek;

/**
* This database classe stores JID Groups and keys for several Secure_Cores
* It only interacts with Encryption and Header.  
*
* @author Frederick Bettray
* @author Stefan Wegerhoff
*
*@version 2015-02-18
*/

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
/**
 * Name of the table for jid groups
 */
private static final String DB_TABLE_NAME2 = "tbljidset";
/**
 * every encrypted message has a 40 signs long Hexcount at the top
 * it specificates on key and its content in this table 
 */
private static final String DB_COLUMN_HASH = "hash";
/**
 * id to jid groups
 */
private static final String DB_COLUMN_JIDGROUP = "id";
/**
 * specificate the algorithm to encrypt
 */
private static final String DB_COLUMN_COREID = "coreid";
/**
 * the db stores a list of several keys in db and send it on every outgoing message
 * this bool stores if it is shared.
 */
private static final String DB_COLUMN_ALLREADYSEND = "allreadysend";
/**
 * information of class Key
 */
private static final String DB_COLUMN_KEYDATATYPE = "keydatatype";
/**
 * information of class Key
 */
private static final String DB_COLUMN_ALGORITHM = "algorithm";
/**
 * holds the real key length
 */
private static final String DB_COLUMN_KEYLENGTH = "keylength";
/**
 * stores complete key with salt
 */
private static final String DB_COLUMN_SALTEDKEY = "saltedkey";

/**
 * id and jidlist for JID table
 */
private static final String DB_COLUMN_ID = "_id";
private static final String DB_COLUMN_JIDLIST = "jidlist";

/**
 * holds current id in JID list
 */
private int id_JID;

/**
 * creates an entry for every jid group
 * 
 * @param context
 * @param jidIdent
 * @throws EncryptionFaultException
 * @throws SQLiteException
 */
	
	public KeySetDB(Context context, String jidIdent) throws EncryptionFaultException, SQLiteException {
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

	/**
	 * look for higgest id and return the next higger integer 
	 * @param db
	 * @return
	 */
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
	
	/**
	 * current JID group
	 * @return
	 */
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
			DB_COLUMN_SALTEDKEY + " TEXT NULL" +
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
	
	
	/**
	 * returns non sended keys and mark it in db
	 * @param coreid
	 * @param many
	 * @return
	 */
	public Vector<SaltedAndPepperedKey> requestKeys( String coreid, int many ){

		Vector<SaltedAndPepperedKey> res = new Vector<SaltedAndPepperedKey>();
		SQLiteDatabase db = this.getWritableDatabase();			
				
		String selecttbl = "SELECT " + 	  
		DB_COLUMN_COREID + "," +
		DB_COLUMN_KEYDATATYPE + "," +
		DB_COLUMN_ALGORITHM + "," +
		DB_COLUMN_KEYLENGTH + "," +
		DB_COLUMN_SALTEDKEY + 
		" FROM " + DB_TABLE_NAME1 + 
		" WHERE " + DB_COLUMN_COREID + "='" + coreid + 
		"' AND " + DB_COLUMN_JIDGROUP + "=" + id_JID + 
		" AND " + DB_COLUMN_ALLREADYSEND + "=0" +
		" LIMIT " + many + ";";
		
		android.database.Cursor queryCursor = db.rawQuery(selecttbl, null);
	
		if(queryCursor.moveToFirst()) {
			

			do {
				
				
//	    		Log.v("Encryption", queryCursor.getString(4));
				

				res.add(new SaltedAndPepperedKey(
						Encryption.hexStringToByteArray(queryCursor.getString(4))
						, queryCursor.getInt(3)
						, queryCursor.getString(2)
						, queryCursor.getString(1)
						, queryCursor.getString(0)));
				
				} while(queryCursor.moveToNext());
			

			queryCursor.close();
			}
		else {
			queryCursor.close();
			db.close();
			return res;
		}
		
	      Iterator<SaltedAndPepperedKey> iter = res.iterator();
	      String update;

	      
	        while (iter.hasNext()) {

	        	
	        	update = "UPDATE " + DB_TABLE_NAME1 +
	        			" SET " + DB_COLUMN_ALLREADYSEND + "=1" +
	        			" WHERE " + DB_COLUMN_SALTEDKEY + "='" + iter.next().getSaltedEncoded() + "';";
	        	db.execSQL(update);
	        }
		
		
		
		return res;
	}
	
	
	/**
	 * gets a couple of keys, to store it with state shared or not shared
	 * @param keys
	 * @param received
	 */
	void setKey(Vector<SaltedAndPepperedKey> keys, boolean received){
		SQLiteDatabase db = this.getWritableDatabase();		
		String createDB = "";
		int allreadySend = (received ? 1 : 0);
	      Iterator<SaltedAndPepperedKey> iter = keys.iterator();

	      SaltedAndPepperedKey key = null;
	      
	        while (iter.hasNext()) {
	        	key=iter.next();
	        	
	    		createDB = "INSERT INTO " +DB_TABLE_NAME1 +"(" +
	    				DB_COLUMN_HASH + "," +
	    				DB_COLUMN_JIDGROUP + "," +
	    				DB_COLUMN_COREID + "," +
	    				DB_COLUMN_ALLREADYSEND + "," +
	    				DB_COLUMN_KEYDATATYPE + "," +
	    				DB_COLUMN_ALGORITHM + "," +
	    				DB_COLUMN_KEYLENGTH + "," +
	    				DB_COLUMN_SALTEDKEY + 
	    				") VALUES ('" + Encryption.byteToHex( Encryption.hashByte(key.getSaltedEncoded())) +
	    				"', " + id_JID + 
	    				",'" + key.getPepper() + 
	    				"'," + allreadySend + 
	    				", '" + key.getFormat() + 
	    				"', '" + key.getAlgorithm() + 
	    				"', " + key.getKeyLength() + 
	    				", '" + Encryption.byteToHex(key.getSaltedEncoded()) +
	    				"');";
	    		Log.v("Encryption", createDB);

	    		db.execSQL(createDB);	        	
	        	iter.remove();
	            
	        }
		

	}
	
	/**
	 * delete all keys of this JID group
	 */
	void truncateKeySet(){
		SQLiteDatabase db = this.getWritableDatabase();	

		String DeleteTBL = "DELETE FROM " +DB_TABLE_NAME1 +" WHERE " + DB_COLUMN_JIDGROUP + "=" + id_JID + ";";

		Log.v("Encryption", DeleteTBL);

		db.execSQL(DeleteTBL);	        	

	}
	
	/**
	 * request a key to encrypt a message
	 * Not finished yet
	 * @param core
	 * @return
	 */
	SaltedAndPepperedKey getKey(String core){
		byte[] key = new byte[22];
		
		
		
		for(int i = 0; i < key.length;i++)
		{
			key[i] = 0x34;
		}
		
		return new SaltedAndPepperedKey(key, 20, "AES", "RAW", core);
		
	}
	
	/**
	 * request a specific key to decrypt a message
	 * @param hash
	 * @return
	 */
	SaltedAndPepperedKey getKey(byte[] hash){
		
		byte[] key = new byte[22];
		
		
		
		for(int i = 0; i < key.length;i++)
		{
			key[i] = 0x34;
		}
		
		return new SaltedAndPepperedKey(key, 20, "AES", "RAW", "TextSecureCore");
	}
	
	
	/**
	 * return many of available keys
	 * @param core
	 * @return
	 */
	public int getManyOfKeys(String core){
		int res = -1;
		SQLiteDatabase db = this.getWritableDatabase();
		
		String sql = "SELECT count(*) AS total FROM " + DB_TABLE_NAME1 + 
				" WHERE " + DB_COLUMN_COREID + "='" + core + "' AND " + DB_COLUMN_JIDGROUP + "=" + id_JID + ";";
		
		android.database.Cursor queryCursor = db.rawQuery(sql, null);
		if(queryCursor.moveToFirst())res = queryCursor.getInt(0); 
		queryCursor.close();
		db.close();
		return res;
	}
	
}
