package mast.avalons;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class WandMDbHelper extends SQLiteOpenHelper 
        implements BaseColumns {
    
    public static final String TABLE_NAME = "tables";
    public static final String WORK = "work";
    public static final String MATERIAL = "material";
    public static final String VOLUME = "volume";
    public WandMDbHelper(Context context) {
        super(context, WandMProvider.DB_CONTACTS, null, 1);
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME 
                + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " 
                +WORK+" TEXT, "+MATERIAL+" TEXT, "+VOLUME
                +" TEXT);");
        ContentValues values = new ContentValues();
        values.put(WORK, "WORKTEST");
        values.put(MATERIAL, "Шпаклевка, кг");
        values.put(VOLUME, "4");
        db.insert(TABLE_NAME, WORK, values);  
   }  
    @Override
    public void onUpgrade(
            SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }   
}