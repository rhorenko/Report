package mast.avalons;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class TempDbHelper extends SQLiteOpenHelper implements BaseColumns {
    public static final String TABLE_NAME = "contact";
    public static final String OBJECT = "object";
    public static final String MATERIAL = "material";
    public static final String VOLUME = "volume";
    public static final String DATE = "date";
    public static final String COMMENT = "comment";
    public static final String FLAG = "flag";
    public static final String FACTVOLUME = "factvolume";
    public static final String FLAG_FACT = "flag_fact";
    public static final String ACT = "act";
    public static final String WORK = "work";
    
    public TempDbHelper(Context context) {
        super(context, TempProvider.DB_CONTACTS, null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME 
                + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " 
                + OBJECT + " TEXT, " +MATERIAL+ " TEXT, "
                + VOLUME + " TEXT, "+DATE + " TEXT, "+COMMENT 
                + " TEXT, "+"FLAG" + " TEXT, "+FACTVOLUME+" TEXT, "+FLAG_FACT+" TEXT, "
                +ACT+" TEXT, "+WORK+" TEXT);");
   }
    @Override
    public void onUpgrade(
            SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }   
}