package org.watzlawek;

import android.content.ContentValues;
import android.content.Context;
//import android.database.Cursor;                  Native Android Cursor
//import android.database.sqlite.SQLiteDatabase;   Native Android SQLiteDatabase
//import android.database.sqlite.SQLiteOpenHelper; Native Android SQLiteOpenHelper

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteException;
import net.sqlcipher.database.SQLiteOpenHelper;

import android.provider.Settings.Secure;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Hashtable;
import java.util.Vector;

/**
 * This class handles all SQLite database operations on the server database.
 * It is used for adding, updating, reading or removing servers from database.
 * 
 * @author Klaus-Peter Watzlawek anIMus 1/2.0
 * @author Moritz Lipfert only anIMus 1.0
 * 
 * @version 2013-09-10
 * 
 * @see http://www.vogella.com/articles/AndroidSQLite/article.html The tutorial this class' layout is based on.
 */
public class ServerDatabaseHandler extends SQLiteOpenHelper {
	/**
	 * Current version of the database layout.
	 * An incrementation will cause a recreation of an existing database and loss of it's content.
	 */
	private static final int SERVER_DB_VERSION = 11;
	
	/**
	 * Name of the database.
	 */
	private static final String SERVER_DB_NAME = "serverList";
	
	/**
	 * Name of the table which holds the servers.
	 */
	private static final String SERVER_DB_TABLE = "server";
	
	/**
	 * Primary key column of the table "server".
	 * It holds an auto incremented and unique integer value.
	 */
	private static final String SERVER_DB_TABLE_PKEY = "server_id"; // column 0
	
	/**
	 * Column of the table "server".
	 * It defines the the server type, e.g. xmpp.
	 */
	private static final String SERVER_DB_TABLE_TYPE = "type"; // column 1
	
	/**
	 * Column of the table "server".
	 * It defines a server's domain.
	 */
	private static final String SERVER_DB_TABLE_DOMAIN = "domain"; // column 2
	
	/**
	 * Column of the table "server".
	 * It defines a server's port.
	 */
	private static final String SERVER_DB_TABLE_PORT = "port"; // column 3
	
	/**
	 * Column of the table "server".
	 * It defines a server's username for login.
	 */
	private static final String SERVER_DB_TABLE_USER = "user"; // column 4
	
	/**
	 * Column of the table "server".
	 * It defines a server's password for login.
	 */
	private static final String SERVER_DB_TABLE_PW = "password"; // column 5
	
	/**
	 * Column of the table "server".
	 * It defines if encryption will be used for connecting or not.
	 */
	private static final String SERVER_DB_TABLE_ENC = "encryption"; // column 6
	
	/**
	 * Column of the table "server".
	 * It takes a Base64 encoded OTR private key.
	 * It is optional to use.
	 */
	private static final String SERVER_DB_TABLE_OTRPRIV = "otrprivate"; // column 7
	
	/**
	 * Column of the table "server".
	 * It takes a Base64 encoded OTR public key.
	 * It is optional to use.
	 */
	private static final String SERVER_DB_TABLE_OTRPUB = "otrpub"; // column 8
	
	/**
	 * The name of the table which stores the histories.
	 */
	private static final String HISTORY_DB_TABLE = "history";
	
	/**
	 * Column of the table "history".
	 * It takes the ID of a contact for whom a history is stored
	 */
	private static final String HISTORY_DB_TABLE_PKEY = "historyID";
	
	/**
	 * Column of the table "history".
	 * It takes the userID of a contact for whom a history is stored.
	 */
	private static final String HISTORY_DB_TABLE_USERID = "userID";	
	
	/**
	 *  Column of the table "history".
	 *  It takes the own userID of an account.
	 */ 
	private static final String HISTORY_DB_TABLE_OWNUSERID = "ownuserID";	
	
	/**
	 * Column of the table "history".
	 * It takes the username of a contact.
	 */	
	private static final String HISTORY_DB_TABLE_USERNAME = "username";
	
	/**
	 * Column of the table "history".
	 * It takes the message history of a contact.
	 */
	private static final String HISTORY_DB_TABLE_MESSAGES = "messages";
	
	/**
	 * Column of the table "history".
	 * It takes the timestamp  of a contact.
	 */
	private static final String HISTORY_DB_TABLE_TIMESTAMP = "timestamp";

	/** DEPRECATED SINCE 2.0
	 * Name of the table which maps a contact's ID to a server ID.
	 */
	private static final String HISTORY_SERVER_MAPPING_DB_TABLE = "historyServerMapping";
	
	/**
	 * Column of the table "historyServerMapping".
	 * It takes the server ID of a server which is mapped to a user ID.
	 */
	private static final String HISTORY_SERVER_MAPPING_DB_TABLE_SERVERID = "server_id";
	
	/**
	 * Column of the table "historyServerMapping".
	 * It takes the user ID of a contact which is mapped to a server ID.
	 */
	private static final String HISTORY_SERVER_MAPPING_DB_TABLE_USERID = "userId";
	
	
	/**
	 * Name of the table which maps a country code id to calling number.   
	 */
	private static final String COUNTRY_CODES_DB_TABLE = "countryCodes";
	
	/**
	 * Colum of the table "countryCodes".
	 * It takes the country ID, like DE for Deutschland
	 */
	private static final String COUNTRY_CODES_DB_TABLE_COUNTRYID = "countryID";
	
	/**
	 * Column of table "countryCodes".
	 * It takes the country calling number which maps to the country ID.
	 */
	private static final String COUNTRY_CODES_DB_TABLE_COUNTRYCALL = "countryCall";
	
	/**
	 * Name of the table which saves all tokens for contact index system.   
	 */
	private static final String TOKENSYSTEM_DB_TABLE = "tokensystem";
	
	/**
	 * Column of table "tokensystem".
	 * It takes the primary key which maps to the xmpp jid of a server entry. 
	 */
	private static final String TOKENSYSTEM_DB_TABLE_XMPPJID = "xmppjid_id";
	
	/**
	 * Column of table "tokensystem".
	 * It takes the xmpp jid of a anIMa service. 
	 */
	private static final String TOKENSYSTEM_DB_TABLE_SERVICEACOUNT = "xmppjidserivce";
	
	/**
	 * Column of table "tokensystem".
	 * It takes the own identifier of a anIMa service. 
	 */
	private static final String TOKENSYSTEM_DB_TABLE_OWNIDENTIFIER = "ownidentifier";
	
	/**
	 * Column of table "tokensystem".
	 * It takes the authtoken of a anIMa account. 
	 */
	private static final String TOKENSYSTEM_DB_TABLE_AUTHTOKEN = "authtoken";
	
	/**
	 * Column of table "tokensystem".
	 * It takes the password of a anIMa account. 
	 */
	private static final String TOKENSYSTEM_DB_TABLE_PASSWORD = "password";
	
	/**
	 *  Column of table "tokensystem".
	 *  It takes the service address of an anIMa Core Server.
	 */
	private static final String TOKENSYSTEM_DB_TABLE_SERVICEADDRESS = "serviceaddress";
	
	/**
	 * The application's context object.
	 */
	private Context context;
	
	/**
	 * The password to use for AES database encryption.
	 */
	private String password;
	
	
	/**
	 * Standard Constructor which sets the context variable and initializes the SQLite database with the values specified by the contants.
	 * SQLCipher AddOn, Password is empty for native plain database support.
	 * @param context An Activity's context.
	 */
	public ServerDatabaseHandler(Context context) {		
		super(context, SERVER_DB_NAME, null, SERVER_DB_VERSION);
		this.context = context;		
		this.password = "";	
	}
	
	/**
	 * anIMus 2.0 BA-Thesis AddOn
	 * Constructor which sets the context variable and initializes the SQLite database with the values specified by the contants.
	 * SQLCipher AddOn, Password is Android Id if in_password parameter is empty. 
	 * If password is empty and database file is plain, data base encryption is not active and runs like native android sqlite.
	 * @param context An Activity's context.
	 * @param in_password a individual user password to decrypt database.
	 */
	public ServerDatabaseHandler(Context context, String in_password) {		
		super(context, SERVER_DB_NAME, null, SERVER_DB_VERSION);
		this.context = context;			
		this.password = in_password;		
	}	
	
	/**
	 * Setter for Password.
	 * @param in_password
	 */
	public void setPassword(String in_password) {
		this.password = in_password;
	}
	
	/**
	 * Changes password for an existing database.
	 * @param oldpws the old password.
	 * @param newpws the new password.
	 */
	public void changeDBPassword(String oldpws, String newpws) {
		try {
			SQLiteDatabase db = getWritableDatabase(oldpws);
			if (db != null) {
				String sqlCommand = "PRAGMA rekey = '" + newpws + "'";			
				db.execSQL(sqlCommand);
				this.password = newpws;
				Log.v("changeDBPassword", "TRUE");
			}
		} 
		catch(SQLiteException e) {	
			Log.v("changeDBPassword", "FALSE");
		}
		
	}
	
	/**
	 * Get the working directory of the app.
	 * @param context The application context.
	 * @return directory as string.
	 * @throws Exception
	 */
	public static String getAppDirectory(Context context) throws Exception {
	    return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).applicationInfo.dataDir;
	}
	
	/**
	 * Check if given internal database is encrypted or not.
	 * @return boolean type
	 * - true if database file is plain readable and not encrypted.
	 * - false if database file is encrypted.
	 * @throws Exception
	 */
	public boolean isDatabasePlain() throws Exception {				
		String path_to_database  = getAppDirectory(context) + "//" + "databases" + "//" + SERVER_DB_NAME;
		//hex representation of pattern "SQLite format 3" stored in 
		//the beginning of every correct sqlite3 file.
		int hexfileheader[] = new int[] { 0x53, 0x51, 0x4c, 0x69, 0x74, 0x65, 0x20, 0x66, 
				0x6F, 0x72, 0x6D, 0x61, 0x74, 0x20, 0x33, 0x00 };		
		FileInputStream checkfile = new FileInputStream(path_to_database);
		try {			
			for(int i = 0; i < hexfileheader.length; i++) 
				if(hexfileheader[i] != checkfile.read()) 
					return false;					
		} 
		catch (IOException e) {
			Log.v("IOException", e.getMessage());
		} 
		finally {
			checkfile.close();
		}		
		return true;		
	}
	
	/**
	 * Converts a plain database to SQLCipher secured database with password.
	 * @param in_password
	 */
	public void ConvertPlain2EncryptedDB(String in_password) {
		Log.v("ConvertPlain2EncryptedDB", "RUNS");		
		IMApp app = (IMApp)context.getApplicationContext();	
		app.ReloadPrefManager();
		try { // Code is broken, not working
			/*if (isDatabasePlain()) { 
				//this.password = "";	
				Log.v("isDatabasePlain", "YES");
				
				if (app.getAppPrefHandler().getDBCryptState().equals("_internal")) {
					Log.v("Convert to encrypted", "YES");
				//Create new temp database for encryption
				String path_to_database_crypt  = getAppDirectory(context) + "//" + "databases" + "//" + "dbencrypted";
				
				File path_of_plaindb = context.getDatabasePath(SERVER_DB_NAME);
				File path_of_encrpyted_db = new File(path_to_database_crypt);				
				
				SQLiteDatabase db = getWritableDatabase("");					
				
				String sqlcommand = "ATTACH DATABASE '" + path_of_encrpyted_db.getAbsolutePath() +  "' AS encrypted KEY '" + in_password + "';";
				db.rawQuery(sqlcommand, null);				
				Log.v("sqlcommand", sqlcommand);
				db.rawQuery("SELECT sqlcipher_export('encrypted');", null);
				db.rawQuery("DETACH DATABASE encrypted;", null);
				
				SQLiteDatabase encrypted_db = SQLiteDatabase.openOrCreateDatabase(path_of_encrpyted_db, in_password, null );	
				
				db.close();
				
				
				//Remove plain database
				if (path_of_plaindb.exists())
					path_of_plaindb.delete();
				
				//Transfer Data from source to destination
				FileChannel src = new FileInputStream(path_of_encrpyted_db.getAbsoluteFile()).getChannel();            
				FileChannel dst = new FileOutputStream(path_of_plaindb.getAbsoluteFile()).getChannel();
				dst.transferFrom(src, 0, src.size());
				src.close();
				dst.close();
				
				Log.v("name of file", path_of_plaindb.getName());		
				encrypted_db.close();
				path_of_encrpyted_db.delete();				
				
				
				}
			} else {
				Log.v("isDatabasePlain", "NO");
				this.password = in_password;	
			}*/
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();			
		}	
		app.ReloadPrefManager();	
		app.ReloadServerManager();
	}
	
	/**
	 * Checks if a given password is valid.
	 * @param in_password The password to get access to database.
	 * @return 
	 * - true if password was correct
	 * - false otherwise
	 */
	public boolean isPasswordVaild(String in_password) {
		try {
			
			String sqlCommand = "SELECT count(*) FROM sqlite_master;";
			String output = "";
			SQLiteDatabase db = null;
			db = getReadableDatabase(in_password);
			if (db != null) {
				Cursor queryCursor = db.rawQuery(sqlCommand, null);
				Log.v("size of table column", queryCursor.getColumnCount()+"");
				if (queryCursor.getColumnCount() > 0)			

				if(queryCursor.moveToFirst()) {
					do {
					output = queryCursor.getString(0);
					}
					while(queryCursor.moveToNext());
					db.close(); 
				}
				queryCursor.close();				
				return 0 == Integer.parseInt(output);
			}
			else Log.v("DB", "NULL");
		} catch(SQLiteException e) {}
		
		return false;
	}
	
	/**
	 * This method deletes all contact's histories.
	 */
	public void clearHistory() {
		SQLiteDatabase db = getWritableDatabase(password);
		db.delete(HISTORY_DB_TABLE, null, null);
		db.delete(HISTORY_SERVER_MAPPING_DB_TABLE, null, null);
		db.execSQL("VACUUM;");
		db.close();
	}
	
	/**
	 * Clears History for a pair of userID and ownuserID. 
	 * Only history who belongs to ownuserID is clear in this case.
	 * @param userID
	 * @param ownuserID
	 */
	public void clearHistoryByUserID(String userID, String ownuserID) {
		SQLiteDatabase db = getWritableDatabase(password);
		db.delete(HISTORY_DB_TABLE, "userID = '"+ userID +"' AND ownuserID = '"+ ownuserID +"'" , null);		
		db.execSQL("VACUUM;");
		db.close();
	}
	
	/**
	 * On creation of this class the database and the table will be created if they are non existent.
	 * 
	 * @param input_db The SQLite database on which the creation command will be executed.
	 */
	@Override
	public void onCreate(SQLiteDatabase input_db) {
		String sqlCommand = "CREATE TABLE " + SERVER_DB_TABLE + "("
							+ SERVER_DB_TABLE_PKEY + " INTEGER PRIMARY KEY AUTOINCREMENT, "
							+ SERVER_DB_TABLE_TYPE + " VARCHAR(10) NOT NULL, "
							+ SERVER_DB_TABLE_DOMAIN + " VARCHAR(255) NOT NULL, "
							+ SERVER_DB_TABLE_PORT + " UNSIGNED SMALLINT NOT NULL, "
							+ SERVER_DB_TABLE_USER + " VARCHAR(255) NOT NULL, "
							+ SERVER_DB_TABLE_PW + " VARCHAR(255) NOT NULL, "
							+ SERVER_DB_TABLE_ENC + " UNSIGNED INTEGER NOT NULL, "
							+ SERVER_DB_TABLE_OTRPRIV + " VARCHAR(255), "
							+ SERVER_DB_TABLE_OTRPUB + " VARCHAR(255) " + ");";

		input_db.execSQL(sqlCommand);
		
		//Added for MessageItem anIMus 2.0 BA-Thesis
		sqlCommand = "CREATE TABLE " + HISTORY_DB_TABLE + "("
					 + HISTORY_DB_TABLE_PKEY + " INTEGER PRIMARY KEY AUTOINCREMENT, "
					 + HISTORY_DB_TABLE_USERID + " VARCHAR(255) NOT NULL, "
					 + HISTORY_DB_TABLE_USERNAME + " VARCHAR(255) NOT NULL, "
					 + HISTORY_DB_TABLE_TIMESTAMP + " VARCHAR(255) NOT NULL, "					
					 + HISTORY_DB_TABLE_MESSAGES + " VARCHAR(500) NOT NULL, "
					 + HISTORY_DB_TABLE_OWNUSERID + " VARCHAR(500) NOT NULL" + ");";
		input_db.execSQL(sqlCommand);
		
		//DEPRECATED since 2.0
		sqlCommand = "CREATE TABLE " + HISTORY_SERVER_MAPPING_DB_TABLE + "("
					 + HISTORY_SERVER_MAPPING_DB_TABLE_SERVERID + " INTEGER NOT NULL, "
					 + HISTORY_SERVER_MAPPING_DB_TABLE_USERID + " VARCHAR(20) NOT NULL );";
		input_db.execSQL(sqlCommand);
		
		//Country Calling Codes Mappings
		sqlCommand = "CREATE TABLE " + COUNTRY_CODES_DB_TABLE + "("
					 + COUNTRY_CODES_DB_TABLE_COUNTRYID + " VARCHAR(2) NOT NULL PRIMARY KEY, "
					 + COUNTRY_CODES_DB_TABLE_COUNTRYCALL + " VARCHAR(10) NOT NULL " + " );";
		input_db.execSQL(sqlCommand);
		
		//Insert countryID and countryCall
		
		String arr[] = {"FR:33", "DE:49", "IT:39", "AT:43", "GB:44", "US:1"};
		
		input_db.beginTransaction();			
		for (int i = 0; i < arr.length; i++) {			
			String parts[] = arr[i].split(":");			
			sqlCommand = "INSERT INTO " + COUNTRY_CODES_DB_TABLE + " VALUES(" + "'" + parts[0] + "','" +  parts[1] + "');" ;			
			input_db.execSQL(sqlCommand);
		}		
		
		//Insert anIMa Auth System
		sqlCommand = "CREATE TABLE " + TOKENSYSTEM_DB_TABLE  + "(" 
					 + TOKENSYSTEM_DB_TABLE_XMPPJID + " VARCHAR(255), "
					 + TOKENSYSTEM_DB_TABLE_SERVICEACOUNT + " VARCHAR(255) NOT NULL, "
					 + TOKENSYSTEM_DB_TABLE_OWNIDENTIFIER +  " VARCHAR(255) NOT NULL, "
					 + TOKENSYSTEM_DB_TABLE_AUTHTOKEN + " VARCHAR(255) NOT NULL, "
					 + TOKENSYSTEM_DB_TABLE_PASSWORD + " VARCHAR(255) NOT NULL, "
					 + TOKENSYSTEM_DB_TABLE_SERVICEADDRESS + " VARCHAR(255) NOT NULL, " +
					 "PRIMARY KEY ( " + TOKENSYSTEM_DB_TABLE_XMPPJID + ", " + TOKENSYSTEM_DB_TABLE_OWNIDENTIFIER + " ) );";			
		
		input_db.execSQL(sqlCommand);
		input_db.setTransactionSuccessful();
		input_db.endTransaction();
		
	}
	
	/**
	 * Saves all data from successfully anIMa Account Registration Process.
	 * @param in_xmppjid The xmppjid.
	 * @param in_identifier The hashed SHA256 idenfifier.
	 * @param in_startpassword The start password.
	 * @param in_serviceaddress The anIMa Service Address.
	 */

	public void insertTokenSystemAccount(String in_xmppjid, String in_identifier,  String in_startpassword, String in_serviceaddress) {
		SQLiteDatabase db = getWritableDatabase(password);		
		ContentValues tokenValues = new ContentValues();
		
		tokenValues.put(TOKENSYSTEM_DB_TABLE_XMPPJID, in_xmppjid);
		tokenValues.put(TOKENSYSTEM_DB_TABLE_OWNIDENTIFIER, in_identifier);
		tokenValues.put(TOKENSYSTEM_DB_TABLE_PASSWORD, in_startpassword);
		tokenValues.put(TOKENSYSTEM_DB_TABLE_SERVICEADDRESS, in_serviceaddress);
		
		tokenValues.put(TOKENSYSTEM_DB_TABLE_SERVICEACOUNT, "");
		tokenValues.put(TOKENSYSTEM_DB_TABLE_AUTHTOKEN, "");
		
		
		
		
		db.insert(TOKENSYSTEM_DB_TABLE, null, tokenValues);
		db.close();
	}
	/**
	 * Updates Token Data, if account was enabled by anIMa Server.
	 * @param in_xmppjid The xmppjid.
	 * @param in_identifier The hashed SHA256 idenfifier.
	 * @param in_xmppservice The service account for account validation.
	 * @param in_authtoken The authtoken.
	 * @param in_newpassword The new generated password after account validation.
	 * @param in_serviceaddress The anIMa Service Address.
	*/
	
	public void updateTokenSystemAccount(String in_xmppjid, String in_identifier,  String in_xmppservice, String in_authtoken, String in_newpassword, String in_serviceaddress) {
		
		Hashtable<String, String> ht_temp = getTokenSystemAccount(in_xmppjid, in_identifier);		
		
		
		ContentValues tokenValues = new ContentValues();
		if (!in_xmppservice.equals(""))
			tokenValues.put(TOKENSYSTEM_DB_TABLE_SERVICEACOUNT, in_xmppservice);
		
		if (!in_authtoken.equals(""))
			tokenValues.put(TOKENSYSTEM_DB_TABLE_AUTHTOKEN, in_authtoken);
		
		if (!in_newpassword.equals(""))
			tokenValues.put(TOKENSYSTEM_DB_TABLE_PASSWORD, in_newpassword);
		
		if (!in_serviceaddress.equals("")) {
			//Have to make a reset of token data if the anIMa Service Address changed now.		
			if (ht_temp.size() > 0) {
				if (!in_serviceaddress.equals(ht_temp.get(TOKENSYSTEM_DB_TABLE_SERVICEADDRESS).toString())) {
					tokenValues.put(TOKENSYSTEM_DB_TABLE_SERVICEACOUNT, "");
					tokenValues.put(TOKENSYSTEM_DB_TABLE_AUTHTOKEN, "");
					tokenValues.put(TOKENSYSTEM_DB_TABLE_SERVICEADDRESS, in_serviceaddress);
					tokenValues.put(TOKENSYSTEM_DB_TABLE_PASSWORD, AutoDiscover.randomPassword(32));
				}
			} 
			else {
				this.insertTokenSystemAccount(in_xmppjid, in_identifier, AutoDiscover.randomPassword(32), in_serviceaddress);
			}
		}
		if (tokenValues.size() > 0) {
			SQLiteDatabase db = getWritableDatabase(password);
			db.update(TOKENSYSTEM_DB_TABLE, tokenValues, TOKENSYSTEM_DB_TABLE_XMPPJID + "=?" + " AND " + TOKENSYSTEM_DB_TABLE_OWNIDENTIFIER + "=?", new String[] {in_xmppjid,in_identifier});
			Log.v("updateTokenSystemAccount", "OK");
			db.close();
		}	
		
		
	}	
	
	public Hashtable<String, String> getTokenSystemAccount (String in_xmppjid, String in_identifier) {
		Hashtable<String, String> ht = new Hashtable<String, String>();
		SQLiteDatabase db = null;
		
		
		String sqlCommand = "SELECT * FROM " + 
				TOKENSYSTEM_DB_TABLE + 
				" WHERE " + TOKENSYSTEM_DB_TABLE_XMPPJID + " = " + "'"+ in_xmppjid + "'" + " " +
				"AND " + TOKENSYSTEM_DB_TABLE_OWNIDENTIFIER + " = " +  "'" + in_identifier + "'";	
		try {
			db = getReadableDatabase(password);
			
			if (db != null) {
				Cursor queryCursor = db.rawQuery(sqlCommand, null);
				
				if(queryCursor.moveToFirst()) {
					do {	
							
						for (int i = 0; i < queryCursor.getColumnCount(); i++) {
							ht.put(queryCursor.getColumnName(i).toString(), queryCursor.getString(i).toString());	
							//Log.v(queryCursor.getColumnName(i).toString(), queryCursor.getString(i).toString());
						}
					} 	while(queryCursor.moveToNext());
				} else{
					Log.v("EmptyTokenAccount", "YES");
				}
				queryCursor.close();
			}
			
			db.close();
		}		
		catch(SQLiteException e) {
				Log.v("SQLiteException_getTokenSystemAccount", e.getMessage());
		}
		//db.close();
		return ht;
	}
	
	/**
	 * Upgrades the database to a higher version. The old table with all its data will be deleted and then recreated without the old data content.
	 * 
	 * @param input_db The SQLite database on which the commands will be executed.
	 * @param input_currentVersion The current version of the database.
	 * @param input_latestVersion The Version the database will be upgraded to.
	 */
	@Override
	public void onUpgrade(SQLiteDatabase input_db, int input_currentVersion, int input_latestVersion) {
		String sqlCommand = "DROP TABLE IF EXISTS " + SERVER_DB_TABLE;
		input_db.execSQL(sqlCommand);
		
		sqlCommand = "DROP TABLE IF EXISTS " + HISTORY_DB_TABLE;
		input_db.execSQL(sqlCommand);
		
		sqlCommand = "DROP TABLE IF EXISTS " + HISTORY_SERVER_MAPPING_DB_TABLE;
		input_db.execSQL(sqlCommand);
		
		sqlCommand = "DROP TABLE IF EXISTS " + COUNTRY_CODES_DB_TABLE;
		input_db.execSQL(sqlCommand);
		
		sqlCommand = "DROP TABLE IF EXISTS " + TOKENSYSTEM_DB_TABLE;
		input_db.execSQL(sqlCommand);
				
		onCreate(input_db);
	}
	
	/**
	 * Method to insert a new server to the database.
	 * 
	 * @param input_type The server type. Currently only xmpp is supported.
	 * @param input_domain The domain of the server.
	 * @param input_port The port of the server.
	 * @param input_user The username for login.
	 * @param input_pw The password of the user.
	 * @param input_enc If the server supports encryption this value has to be true.
	 */
	public void insertServer(String input_type, String input_domain, int input_port, String input_user, String input_pw, boolean input_enc) {
		Encryption enc = new Encryption(context);
		SQLiteDatabase db = getWritableDatabase(password);
		
		ContentValues serverValues = new ContentValues();
		serverValues.put(SERVER_DB_TABLE_TYPE, input_type);
		serverValues.put(SERVER_DB_TABLE_DOMAIN, input_domain);
		serverValues.put(SERVER_DB_TABLE_PORT, input_port);
		serverValues.put(SERVER_DB_TABLE_USER, input_user);
		serverValues.put(SERVER_DB_TABLE_PW, enc.encrypt(input_pw));
		serverValues.put(SERVER_DB_TABLE_ENC, input_enc);
		serverValues.put(SERVER_DB_TABLE_OTRPRIV, "");
		serverValues.put(SERVER_DB_TABLE_OTRPUB, "");
		
		db.insert(SERVER_DB_TABLE, null, serverValues);
		db.close();
	}
	
	/**
	 * This method removes a server from database.
	 * 
	 * @param input_Id The ID of the server which is to be deleted.
	 */
	public void deleteServer(String input_Id) {
		SQLiteDatabase db = getWritableDatabase(password);
		String[] where_param = new String[] { input_Id };
		db.delete(SERVER_DB_TABLE, SERVER_DB_TABLE_PKEY + "=?", where_param);
		
		Cursor cursor = db.query(HISTORY_SERVER_MAPPING_DB_TABLE, new String[] {HISTORY_SERVER_MAPPING_DB_TABLE_USERID}, "server_id = ?", new String[] {input_Id}, null, null, null);
		if(cursor.moveToFirst()) {
			do {
				db.delete(HISTORY_DB_TABLE, "userId = ?", new String[] {cursor.getString(0)});
			}
			while(cursor.moveToNext());
		}
		cursor.close();
		db.delete(HISTORY_SERVER_MAPPING_DB_TABLE, "server_id = ?", new String[] {input_Id});
		db.execSQL("VACUUM;");
		
		db.close();
	}
	
	/**
	 * Returns the message history of a contact.
	 * 
	 * @param input_UserId The ID of the contact for whom the history will be returned, e.g. the Jabber ID.
	 * @param input_ownID  The ID of the own jabber id.	 * 
	 * @param count the max message count of history that should be get from database. 
	 * @return The message history as MessageLog Object.
	 */
	public MessageLog getHistory(String input_UserId, String input_ownID, int count) {
		MessageLog messagelog = new MessageLog();
		
		String sqlCommand = "SELECT * FROM " + HISTORY_DB_TABLE 
				+ " WHERE userID = " + "'"+input_UserId + "'" + " AND ownuserID = " 
				+  "'" + input_ownID + "'" + " ORDER BY " + HISTORY_DB_TABLE_PKEY + " DESC LIMIT " + count;
		SQLiteDatabase db = getReadableDatabase(password);
		Cursor queryCursor = db.rawQuery(sqlCommand, null);
	
		if(queryCursor.moveToFirst()) {
			do {
				int setLeft = 0;
				if (queryCursor.getString(2).contains(context.getText(R.string.IMChatMe).toString()))
					setLeft = 1;
				else 
					if (queryCursor.getString(2).contains(context.getText(R.string.app_name).toString()))
						setLeft = 2;
				messagelog.addMessage(
						
						messagelog.MakeChatLogOld(queryCursor.getString(4), "#8f8f8f"), 
						messagelog.MakeChatLogOld(queryCursor.getString(3), "#8f8f8f"), 
						messagelog.MakeChatLogOld(queryCursor.getString(2), "#8f8f8f"),
						setLeft
					);				
			} while(queryCursor.moveToNext());
		}
		queryCursor.close();
		//Log.v("LOAD", messagelog.getVectorMessage().size() + "");
		db.close();		
		messagelog.reverseMessageItems();
		return messagelog;
	}
	
	/**
	 * This method inserts a new message history for a user to the database. If a history for a contact is already in the database it will be updated.
	 * @param input_userId The contact's ID, e.g. the Jabber ID.
	 * @param input_messageitem  The message item who holds all data.
	 * @param input_ownID Your Own user ID.
	 */	
	public void storeHistory(String input_userId, String input_ownID, MessageItem input_messageitem) {			
		SQLiteDatabase db = getWritableDatabase(password);		
		ContentValues historyValues = new ContentValues();		
		MessageItem item = input_messageitem;
		
		historyValues.put(HISTORY_DB_TABLE_USERID, input_userId);
		historyValues.put(HISTORY_DB_TABLE_MESSAGES, item.getMessage());
		historyValues.put(HISTORY_DB_TABLE_TIMESTAMP, item.getTimestamp());
		historyValues.put(HISTORY_DB_TABLE_USERNAME, item.getUsername());	
		historyValues.put(HISTORY_DB_TABLE_OWNUSERID, input_ownID);		
		
		db.insert(HISTORY_DB_TABLE, null, historyValues);
		db.close();	
		
	}
	
	/**
	 * Updates the OTR keypair for an existent server entry in the database.
	 * 
	 * @param input_id Server ID of the server which gets a new keypair.
	 * @param input_priv Base64 encoded OTR private key which gets added.
	 * @param input_pub Base64 encoded OTR public key which gets added.
	 */
	public void updateKeypair(String input_id, String input_priv, String input_pub) {
		SQLiteDatabase db = getWritableDatabase(password);
		
		ContentValues serverValues = new ContentValues();
		serverValues.put(SERVER_DB_TABLE_OTRPRIV, input_priv);
		serverValues.put(SERVER_DB_TABLE_OTRPUB, input_pub);
		
		db.update(SERVER_DB_TABLE, serverValues, SERVER_DB_TABLE_PKEY + "=?", new String[] {input_id});
		db.close();
	}
	
	/**
	 * Method for updating the record of an existent server.
	 * 
	 * @param input_id Server's primary key for identification.
	 * @param input_domain The domain to connect to.
	 * @param input_port New Server port.
	 * @param input_user Username for login.
	 * @param input_pw User's password for login.
	 * @param input_enc If set to true TLS / SSL will be used for connecting.
	 */
	public void updateServer(String input_id, String input_domain, int input_port, String input_user, String input_pw, boolean input_enc) {
		Encryption enc = new Encryption(context);
		SQLiteDatabase db = getWritableDatabase(password);
		
		ContentValues serverValues = new ContentValues();
		serverValues.put(SERVER_DB_TABLE_DOMAIN, input_domain);
		serverValues.put(SERVER_DB_TABLE_PORT, input_port);
		serverValues.put(SERVER_DB_TABLE_USER, input_user);
		serverValues.put(SERVER_DB_TABLE_ENC, input_enc);
		if(!input_pw.equals("*****")) serverValues.put(SERVER_DB_TABLE_PW, enc.encrypt(input_pw));
		
		db.update(SERVER_DB_TABLE, serverValues, SERVER_DB_TABLE_PKEY + "=?", new String[] {input_id});
		db.close();
	}
	
	/**
	 * Method for reading all servers from the database.
	 * 
	 * @return Vector of IMServer objects.
	 */
	public Vector<IMServer> getServers() {
		Vector<IMServer> serverList = new Vector<IMServer>();
		SQLiteDatabase db = null;
		String sqlCommand = "SELECT * FROM " + SERVER_DB_TABLE;		
		try {
		db = getReadableDatabase(password);
		
		if (db != null) {
		
			Cursor queryCursor = db.rawQuery(sqlCommand, null);
		
			Encryption encryption = new Encryption(context);
		
			if(queryCursor.moveToFirst()) {
				do {
					IMServer currentServer;
					boolean enc = false;
					if(queryCursor.getString(6).equals("1")) enc = true;
					if(queryCursor.getString(1).equals("XMPP")) {
						currentServer = new XMPPServer(context, enc, Integer.parseInt(queryCursor.getString(3)), Integer.parseInt(queryCursor.getString(0)), queryCursor.getString(2), encryption.decrypt(queryCursor.getString(5)), queryCursor.getString(1), queryCursor.getString(4), queryCursor.getString(7), queryCursor.getString(8));
					}
					else {
						currentServer = null;
					}
					serverList.add(currentServer);
				} 	while(queryCursor.moveToNext());
			}
			queryCursor.close();
			db.close();
		} 
		} catch(SQLiteException e) {
			Log.v("Password falsch", "!2");			
		}
		return serverList;
	}
	
	/**
	 * Gets the call number for an country ID. For DE the result is 49
	 * @param in_countryID
	 * @return call number which belongs to county id.
	 */
	public String getCallNumberByCountryID(String in_countryID) {
		String res = "";
		
		SQLiteDatabase db = getWritableDatabase(password);
		Cursor cursor = db.query(COUNTRY_CODES_DB_TABLE, new String[] {COUNTRY_CODES_DB_TABLE_COUNTRYCALL}, "countryID = ?", new String[] {in_countryID}, null, null, null);
		if(cursor.moveToFirst()) {
			res = cursor.getString(0);
		}
		cursor.close();
		db.close();
		return res;
	}
	/**
	 * Returns the name of database.
	 * @return database name as string.
	 */
	public String getServerDatabaseName() {
		return SERVER_DB_NAME;
	}
	
	/**
	 * Closes the database if it is opened.
	 */
	public void closeDB() {		
		SQLiteDatabase db = getWritableDatabase(password);
		if (db != null) {
			if (db.isOpen())
				db.close();
		}
		else  {
			Log.v("DB", "encrypted!");
		}
	 
	}
	
}
