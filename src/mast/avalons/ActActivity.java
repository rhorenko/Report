package mast.avalons;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ActActivity extends Activity implements AdapterView.OnItemSelectedListener{
	public static final String EXT_ACT = "ACT";
	public static final String EXT_FLAG = "FLAG";
	private String flag;
	private Button escButton;  
    private Button exportButton;
    private TextView actText; 
    private TextView mLabelTyp; 
    View root;
    public static final int IDM_OPEN = 101;
    public static final int IDM_SAVE = 102;
    public static final int IDM_EXIT = 103;
    private Spinner spinObject;
    private Spinner spinMaterial;
   
    private Cursor mCursor; 
    private Cursor mTempCursor; 
    private Cursor mObjectCursor;
    private Cursor mMaterialCursor;
    private ListAdapter mTempAdapter;
    private SimpleCursorAdapter mMaterialAdapter;
    private SimpleCursorAdapter mObjectAdapter;
    private static final String[] mTempContent = new String[] {
        TempDbHelper._ID, TempDbHelper.OBJECT, TempDbHelper.MATERIAL,
        TempDbHelper.VOLUME,TempDbHelper.DATE,TempDbHelper.COMMENT,
        TempDbHelper.FLAG,TempDbHelper.FACTVOLUME,TempDbHelper.FLAG_FACT,
        TempDbHelper.ACT,TempDbHelper.WORK};
    private static final String[] mContent = new String[] {
        ContactDbHelper._ID, ContactDbHelper.OBJECT, ContactDbHelper.MATERIAL,
        ContactDbHelper.VOLUME,ContactDbHelper.DATE,ContactDbHelper.COMMENT,
        ContactDbHelper.FLAG,ContactDbHelper.FACTVOLUME,ContactDbHelper.FLAG_FACT,
        ContactDbHelper.ACT,ContactDbHelper.WORK};
    private static final String[] mContentObject = new String[] {
        	ObjectDbHelper._ID, ObjectDbHelper.OBJECT};
    private static final String[] mContentMaterial = new String[] {
    	MaterialDbHelper._ID, MaterialDbHelper.MATERIAL};
    TextView mLabelObj;
    TextView mLabelMat;
    TextView act;  
    private int mYear;
    private int mMonth;
    private int mDay;

    static final int DATE_DIALOG_ID = 0;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actviewer);
        Bundle extras = getIntent().getExtras();
        String act = extras.getString(EXT_ACT);
        flag = extras.getString(EXT_FLAG);
         mTempCursor = managedQuery(
                ContactProvider.CONTENT_URI, mContent, "act='"+act+"' and flag='"+flag+"'", null,"_ID ASC");//ORDER BY _ID ASC
        for (int i = 0; i < mTempCursor.getCount(); i++) {
        	mTempCursor.moveToPosition(i);
        	getContentResolver().delete( TempProvider.CONTENT_URI, null, null);
        }; 
       mTempAdapter = new SimpleCursorAdapter(this, 
                R.layout.temppost, mTempCursor, 
                new String[] {ContactDbHelper.MATERIAL,
                ContactDbHelper.VOLUME,ContactDbHelper.DATE,ContactDbHelper.COMMENT}, 
                new int[] {R.id.tempmaterial, R.id.tempvolume,R.id.tempdate, R.id.tempcomment});
        ((ListView)findViewById(R.id.listViewTemp)).setAdapter(mTempAdapter);
        if(mTempCursor.getString(6).equalsIgnoreCase("-")){((TextView)findViewById(R.id.mLabelType)).setText("");}
        exportButton = (Button)findViewById(R.id.button_export_act);
        escButton = (Button) findViewById(R.id.button_esc);        
        spinObject = (Spinner)findViewById(R.id.SpinnerObject);
        escButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent intent = new Intent();
                intent.setClass(getApplicationContext(), ReportActivity.class);
                startActivity(intent);
                finish();
               }}
        );
        exportButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	String FileName = "";
            	if (mTempCursor.getString(6).equalsIgnoreCase("+")){FileName=FileName+"P";}
            	else {FileName=FileName+"S";};
            	FileName=FileName+mTempCursor.getString(9)+"_"+mTempCursor.getString(4);
            	saveFile(FileName,mTempCursor); 
            	Context context = getApplicationContext();
                Toast toast = Toast.makeText(context, 
                    getResources().getString(R.string.toast_exported), Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }}
        );
    }
     
public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
	}
            
public void onNothingSelected(AdapterView<?> parent) {
	}
private void saveFile(String FileName, Cursor mCursor) {
    try {
    	boolean mExternalStorageAvailable = false;
    	boolean mExternalStorageWriteable = false;
    	String state = Environment.getExternalStorageState();

    	if (Environment.MEDIA_MOUNTED.equals(state)) {
    	    // We can read and write the media
    	    mExternalStorageAvailable = mExternalStorageWriteable = true;
    	} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
    	    // We can only read the media
    	    mExternalStorageAvailable = true;
    	    mExternalStorageWriteable = false;
    	} else {
    	    // Something else is wrong. It may be one of many other states, but all we need
    	    //  to know is we can neither read nor write
    	    mExternalStorageAvailable = mExternalStorageWriteable = false;
    	}
    	/**
    	File path = Environment.getExternalStorageDirectory();
        File FilePath = new File(path, FileName);
        File sdDir;
        File fileName = null;
        String sdState = android.os.Environment.getExternalStorageState();
        if (sdState.equals(android.os.Environment.MEDIA_MOUNTED)) {
            sdDir = android.os.Environment.getExternalStorageDirectory();
            fileName = new File(sdDir, "cache/mastavalons.txt");
            } else {
                fileName = getApplicationContext().getCacheDir();
            }
        if (!fileName.exists())
            fileName.mkdirs();
        try {
            FileWriter f = new FileWriter(fileName);
            f.write("hello world");
            f.flush();
            f.close();
        } catch (Exception e) {

        }
        
        
        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
        OutputStream outStream1 = null;
        File file = new File(extStorageDirectory, "er.PNG");
        try {
            outStream1 = new FileOutputStream(file);
            
            outStream1.flush();
            outStream1.close();
           
            Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
           
           } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
           } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
           }*/
        //BufferedWriter outStream = 
         //new BufferedWriter(new OutputStreamWriter(
					//openFileOutput("myfile", MODE_WORLD_WRITEABLE)));
		String extPath = Environment.getExternalStorageDirectory().getAbsolutePath();
		
		String pathPrefix = extPath + "/Android/data/" + getApplication().getPackageName() + "/cache/";
		//File extPathFile = getExternalFilesDir(pathPrefix+FileName);
		Log.d("TAG", "pathPrefix="+pathPrefix);
		Log.d("TAG", "FileName="+FileName);
		new File(pathPrefix).mkdirs();
		File file = new File(pathPrefix, FileName+".txt");
		if(file.exists()){
		
		Log.d("TAG", "File("+pathPrefix+FileName+")exist");	
		};
       OutputStreamWriter outStream = 
                new OutputStreamWriter(new FileOutputStream(file));
             
        for (int i = 0; i < mCursor.getCount(); i++) {
        mCursor.moveToPosition(i);
        outStream.write(""+Integer.toString(i+1)+"  "+mCursor.getString(9)+"    "+mCursor.getString(4)+"    "+mCursor.getString(1)+"   "+mCursor.getString(3)+"   "+mCursor.getString(2)+"\n");
		}
        
        outStream.close();     
    }
    catch (Throwable t) {
        Toast.makeText(getApplicationContext(), 
              "Exception: " + t.toString(), Toast.LENGTH_LONG)
            .show();
        Log.d("TAG",t.toString());
    }
  }

}
