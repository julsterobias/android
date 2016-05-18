/*
Author: Juls Terobias
Version: 1.0
Platform: java
Location: UAE
*/
package com.terobias.gaidenreader;

import java.util.ArrayList;
import java.util.List;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class DatabaseHandlerClass extends SQLiteOpenHelper {
	
	public static final String DB_NAME = "gaiden_DB";
	public static final int DB_VERSION = 9;

	
	public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS" +
			" bookmark_berde (id INTEGER PRIMARY KEY AUTOINCREMENT," +
			" chapter INTEGER," +
			" code varchar(255))";
	public static final String DELETE_TABLE = "DROP TABLE IF EXISTS bookmark_berde";
	
	public static final String CREATE_TABLE_ACTIVITY = "CREATE TABLE IF NOT EXISTS" +
			" allactivity (id INTEGER PRIMARY KEY AUTOINCREMENT," +
			" chapter INTEGER," +
			" code varchar(255))";
	public static final String DELETE_TABLE_ACTIVITY = "DROP TABLE IF EXISTS allactivity";
	
	public static final String DELETE_TABLE_SETTINGS = "DROP TABLE IF EXISTS settings";	
	public static final String DELETE_TABLE_BOOKMARKS = "DROP TABLE IF EXISTS bookmarks";	
	
	
	
	private Context context;
	private SQLiteDatabase db;

	
	public int mid = 0;
	
	
	public DatabaseHandlerClass(Context context){
		super(context, DB_NAME, null, DB_VERSION);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		
		try{
			db.execSQL(CREATE_TABLE);
			db.execSQL(CREATE_TABLE_ACTIVITY);
			
		}catch(SQLException e){
			//do nothing
			Toast.makeText(context, "Error on creating tables", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		 try{

			db.execSQL(DELETE_TABLE);
			db.execSQL(CREATE_TABLE);
			db.execSQL(DELETE_TABLE_ACTIVITY);
			db.execSQL(CREATE_TABLE_ACTIVITY);
			db.execSQL(DELETE_TABLE_SETTINGS);
			db.execSQL(DELETE_TABLE_BOOKMARKS);
		 }catch(SQLException e){
			 //Do nothing
			 //Toast.makeText(context, "Error on upgrading tables", Toast.LENGTH_LONG).show();
		 }

	}
	
	public void dbClose(){
		db.close();
	}
	
	public long DoAddData(String TABLE_NAME, String toadd[][]){
		db = this.getWritableDatabase();
		long lastid = 0;
		try{
			ContentValues valadd = new ContentValues();
			for(int cnt = 0; cnt < toadd.length; cnt++ ){
				for(int ccnt = 1; ccnt < toadd[cnt].length; ccnt++){
						valadd.put(toadd[cnt][0].toString(), toadd[cnt][ccnt].toString());
				}
			}
			lastid = db.insert(TABLE_NAME, null, valadd);
			db.close();
			
		}catch(SQLException e){
			//do noting
			//Toast.makeText(context, "Error adding data on table "+TABLE_NAME, Toast.LENGTH_LONG).show();
		}
		this.dbClose();
		return lastid;
	}
	
	//Do Update data
	public long DoUpdateData(String TABLE_NAME, String tblfields[][], String Cond){
		db = this.getWritableDatabase();
		ContentValues valedit = new ContentValues();
		long res = 0;
		try{
			for(int cnt = 0; cnt < tblfields.length; cnt++ ){
				for(int ccnt = 1; ccnt < tblfields[cnt].length; ccnt++){
					valedit.put(tblfields[cnt][0].toString(), tblfields[cnt][ccnt].toString());
				}
			}
			res = db.update(TABLE_NAME, valedit, Cond, null);
			db.close();
		}catch(Exception e){
			//do nothing
			//Toast.makeText(context, "Error on update table "+TABLE_NAME, Toast.LENGTH_LONG).show();
		}
		this.dbClose();
		return res;
	}
	
	//Do delete data from database
	public int DoDeleteData(String TABLE_NAME, String params){
		db = this.getWritableDatabase();
		int check = db.delete(TABLE_NAME, params, null);
		this.dbClose();
		return check;
	}
	
	//Get RowData
	public List<String> GetRowData(String TABLE_NAME, String condi){
		List<String> recordholder = new ArrayList<String>();
		db = this.getWritableDatabase();
		String RowQuery = "SELECT * FROM "+TABLE_NAME+" "+condi;
		Cursor rowcursor = db.rawQuery(RowQuery, null);		
		int getcolcnt = rowcursor.getColumnCount();
		if(rowcursor.moveToFirst()){
			do{
				for(int cc = 0; cc < getcolcnt ; cc++){
					recordholder.add(rowcursor.getString(cc));
				}		
			}while(rowcursor.moveToNext());
		}else{
			//do nothing
		}
		this.dbClose();
		return recordholder;
		
	}
	
	public List<List<String>> GetAllData(String TABLE_NAME, String condi){
		List<List<String>> datalist = new ArrayList<List<String>>();
		db = this.getWritableDatabase();
		String query = "SELECT * FROM "+TABLE_NAME+" "+condi;
		Cursor cursor = db.rawQuery(query, null);
		int colcnt = cursor.getColumnCount();
		if(cursor.moveToFirst()){
			do{
				ArrayList<String> innerarr = new ArrayList<String>();
				for(int cnt=0; cnt<colcnt; cnt++){
					innerarr.add(cursor.getString(cnt));
				}
				datalist.add(innerarr);
			}while(cursor.moveToNext());
		}
		this.dbClose();
		return datalist;
	}
	
	public int CountData(String table, String condition){
		int cnt = 0;
		String query = "SELECT COUNT(*) as cnt FROM "+table+" "+condition;
		db = this.getWritableDatabase();
		Cursor thecnt = db.rawQuery(query,null);
		if(thecnt.moveToFirst()){
			cnt = Integer.parseInt(thecnt.getString(0));
		}
		this.dbClose();
		return cnt;
	}
	
	
}
