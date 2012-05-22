package mast.avalons;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class ObjectDbHelper extends SQLiteOpenHelper 
        implements BaseColumns {
    
    public static final String TABLE_NAME = "object";
    public static final String OBJECT = "object";
    
    
    
    public ObjectDbHelper(Context context) {
        super(context, ObjectProvider.DB_CONTACTS, null, 1);
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME 
                + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " 
                + OBJECT + " TEXT);");
        
        ContentValues values = new ContentValues();
                      
        values.put(OBJECT, "Кухня");
        db.insert(TABLE_NAME, OBJECT, values);  
        
        values.put(OBJECT, "Банк");
        db.insert(TABLE_NAME, OBJECT, values);
        
        values.put(OBJECT, "Лоджия");
        db.insert(TABLE_NAME, OBJECT, values);
        
        values.put(OBJECT, "Мастерская");
        db.insert(TABLE_NAME, OBJECT, values);
       
    }

    
    @Override
    public void onUpgrade(
            SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }   
}