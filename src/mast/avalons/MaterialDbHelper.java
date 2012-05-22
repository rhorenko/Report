package mast.avalons;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class MaterialDbHelper extends SQLiteOpenHelper 
        implements BaseColumns {
    
    public static final String TABLE_NAME = "material";
    public static final String MATERIAL = "material";
    
    
    
    public MaterialDbHelper(Context context) {
        super(context, MaterialProvider.DB_CONTACTS, null, 1);
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME 
                + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " 
                +MATERIAL+" TEXT);");
        
        ContentValues values = new ContentValues();
                            
        values.put(MATERIAL, "Песок, кг");
        db.insert(TABLE_NAME, MATERIAL, values);  
       
        values.put(MATERIAL, "Краска, литр");
        db.insert(TABLE_NAME, MATERIAL, values);   
        
        values.put(MATERIAL, "Пенопласт, штук");
        db.insert(TABLE_NAME, MATERIAL, values);      

    }

    
    @Override
    public void onUpgrade(
            SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }   
}