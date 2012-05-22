package mast.avalons;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class ContactDbHelper extends SQLiteOpenHelper 
        implements BaseColumns {
    
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
    public static final String FLAG_ACT = "flag_act";
    public ContactDbHelper(Context context) {
        super(context, ContactProvider.DB_CONTACTS, null, 1);
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME 
                + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " 
                + OBJECT + " TEXT, " +MATERIAL+ " TEXT, "
                + VOLUME + " TEXT, "+DATE + " TEXT, "+COMMENT 
                + " TEXT, "+FLAG+" TEXT, "+FACTVOLUME+" TEXT, "
                +FLAG_FACT+" TEXT, "+ACT+" TEXT, "+WORK+" TEXT, "+FLAG_ACT+" TEXT);");
        
        ContentValues values = new ContentValues();
        
        values.put(OBJECT, "Банк");
        values.put(MATERIAL, "Пенопласт, штук");
        values.put(VOLUME, "9");
        values.put(DATE, "12-10-2011");
        values.put(COMMENT, "-----");
        values.put(FLAG, "+");
        values.put(FACTVOLUME, "9");
        values.put(FLAG_FACT, "+");
        values.put(ACT, "000001");
        values.put(WORK, "Обивка стен");
        values.put(FLAG_ACT, "+");
        db.insert(TABLE_NAME, OBJECT, values);
        
        values.put(OBJECT, "Банк");
        values.put(MATERIAL, "Краска, литров");
        values.put(VOLUME, "9");
        values.put(DATE, "31-11-2011");
        values.put(COMMENT, "-----");
        values.put(FLAG, "+");
        values.put(FACTVOLUME, "9");
        values.put(FLAG_FACT, "+");
        values.put(ACT, "000002");
        values.put(WORK, "Обивка стен");
        values.put(FLAG_ACT, "+");
        db.insert(TABLE_NAME, OBJECT, values);
       
        values.put(OBJECT, "Кухня");
        values.put(MATERIAL, "Трубы, штук");
        values.put(VOLUME, "-8");
        values.put(DATE, "24-10-2012");
        values.put(COMMENT, "-----");
        values.put(FLAG, "-");
        values.put(FACTVOLUME, "-8");
        values.put(FLAG_FACT, "+");
        values.put(ACT, "000003");
        values.put(WORK, "Установка канализации");
        values.put(FLAG_ACT, "+");
        db.insert(TABLE_NAME, OBJECT, values);

    }

    
    @Override
    public void onUpgrade(
            SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }   
}