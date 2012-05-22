package mast.avalons;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class WorkDbHelper extends SQLiteOpenHelper 
        implements BaseColumns {
    
    public static final String TABLE_NAME = "work";
    public static final String WORK = "work";
    
    
    
    public WorkDbHelper(Context context) {
        super(context, WorkProvider.DB_CONTACTS, null, 1);
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME 
                + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " 
                + WORK + " TEXT);");
        
        ContentValues values = new ContentValues();
                      
        values.put(WORK, "Установка канализации");
        db.insert(TABLE_NAME, WORK, values);  
        
        values.put(WORK, "Обивка стен");
        db.insert(TABLE_NAME, WORK, values);
        
        values.put(WORK, "Покраска стен");
        db.insert(TABLE_NAME, WORK, values);
    }

    
    @Override
    public void onUpgrade(
            SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }   
}