package mast.avalons;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;

public class TempProvider extends ContentProvider {
	
    public static final String DB_CONTACTS = "temp.db";
    
    public static final Uri CONTENT_URI = Uri.parse(
			"content://mast.avalons.tempprovider/temp");
    public static final int URI_CODE = 1;
    public static final int URI_CODE_ID = 2;

    private static final UriMatcher mUriMatcher;
    private static HashMap<String, String> mContactMap;
    
    private SQLiteDatabase db;

    static {
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mUriMatcher.addURI("mast.avalons.tempprovider", 
                TempDbHelper.TABLE_NAME, URI_CODE);
        mUriMatcher.addURI("mast.avalons.tempprovider", 
                TempDbHelper.TABLE_NAME + "/#", URI_CODE_ID);
        mContactMap = new HashMap<String, String>();
        mContactMap.put(TempDbHelper._ID, TempDbHelper._ID);
        mContactMap.put(TempDbHelper.OBJECT, TempDbHelper.OBJECT);
        mContactMap.put(TempDbHelper.MATERIAL, TempDbHelper.MATERIAL); 
        mContactMap.put(TempDbHelper.VOLUME, TempDbHelper.VOLUME);
        mContactMap.put(TempDbHelper.DATE, TempDbHelper.DATE);
        mContactMap.put(TempDbHelper.COMMENT, TempDbHelper.COMMENT);
        mContactMap.put(TempDbHelper.FLAG, TempDbHelper.FLAG);
        mContactMap.put(TempDbHelper.FACTVOLUME, TempDbHelper.FACTVOLUME);
        mContactMap.put(TempDbHelper.FLAG_FACT, TempDbHelper.FLAG_FACT);
    }

    public String getDbName() {
        return(DB_CONTACTS);
    }
    
    @Override
    public boolean onCreate() {

        db = (new TempDbHelper(getContext())).getWritableDatabase();
        return (db == null) ? false : true;
    }
   
    @Override
    public Cursor query(Uri url, String[] projection, 
            String selection, String[] selectionArgs, String sort) {
      
        String orderBy;       
        if (TextUtils.isEmpty(sort)) {
            orderBy = TempDbHelper.OBJECT;
        } 
        else {
            orderBy = sort;
        }

        Cursor c = db.query(TempDbHelper.TABLE_NAME, projection, selection, selectionArgs, 
                null, null, orderBy);
        c.setNotificationUri(getContext().getContentResolver(), url);
        return c;
    }

    @Override
    public Uri insert(Uri url, ContentValues inValues) {

        ContentValues values = new ContentValues(inValues);

        long rowId = db.insert(TempDbHelper.TABLE_NAME, TempDbHelper.OBJECT, values);
        if (rowId > 0) {
            Uri uri = ContentUris.withAppendedId(CONTENT_URI, rowId);
            getContext().getContentResolver().notifyChange(uri, null);
            return uri;
        }
        else {
        	throw new SQLException("Failed to insert row into " + url);
        }
    }
    

    @Override
    public int delete(Uri url, String where, String[] whereArgs) {
        int retVal = db.delete(TempDbHelper.TABLE_NAME, where, whereArgs);

        getContext().getContentResolver().notifyChange(url, null);
        return retVal;
    }
    

    @Override
    public int update(Uri url, ContentValues values, 
            String where, String[] whereArgs) {
        int retVal = db.update(TempDbHelper.TABLE_NAME, values, where, whereArgs);
    
        getContext().getContentResolver().notifyChange(url, null);
        return retVal;
    }

    @Override
    public String getType(Uri uri) {       
        return null;
    }  
}
