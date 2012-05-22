package mast.avalons;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class EditActivity extends Activity implements AdapterView.OnItemSelectedListener{
	public static final String EXT_ID = "_ID";
	public static final String EXT_FLAG = "flag";
	private Button mDateDisplay;
    private Button addObjectButton;
    private Button addButton;
    private Button addMaterialButton;
    private Button saveButton;
    private Button editButton;  
    private Button delButton; 
    private Button openButton;
    private Button escButton;  
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
        TempDbHelper.FLAG,TempDbHelper.FACTVOLUME,ContactDbHelper.FLAG_FACT,
        ContactDbHelper.ACT,ContactDbHelper.WORK};
    private static final String[] mContent = new String[] {
        ContactDbHelper._ID, ContactDbHelper.OBJECT, ContactDbHelper.MATERIAL,
        ContactDbHelper.VOLUME,ContactDbHelper.DATE,ContactDbHelper.COMMENT,
        ContactDbHelper.FLAG,ContactDbHelper.FACTVOLUME,ContactDbHelper.FLAG_FACT,
        ContactDbHelper.ACT,ContactDbHelper.WORK,ContactDbHelper.FLAG_ACT};
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
    String actnum;
    static final int DATE_DIALOG_ID = 0;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit);
        
        Bundle extras = getIntent().getExtras();
        String id = extras.getString(EXT_ID);
        actnum = id;
        int num=6-actnum.length();
        //Log.e("num=", actnum);
        for (int j = 0; j < num; j++) {
        	actnum="0"+actnum;
    		 };
    	((TextView) findViewById(R.id.act_number)).setText("   Àêò ¹ "+actnum);  		 
        String flag = extras.getString(EXT_FLAG);
        Log.d("EditActivity","id="+id+"flag="+flag+"actnum="+actnum );
        mCursor = managedQuery(
                ContactProvider.CONTENT_URI, mContent, "flag='"+flag+"' and act='"+actnum+"'", null,null);//ORDER BY _ID ASC
        mTempCursor = managedQuery(
                ContactProvider.CONTENT_URI, mContent, "flag='"+flag+"' and act='"+actnum+"'", null,null);//ORDER BY _ID ASC
        mObjectCursor = managedQuery(
                ObjectProvider.CONTENT_URI, mContentObject, null, null,null);//ORDER BY _ID ASC
        mMaterialCursor = managedQuery(
                MaterialProvider.CONTENT_URI, mContentMaterial, null, null,null);//ORDER BY _ID ASC
        mTempAdapter = new SimpleCursorAdapter(this, 
                R.layout.editpost, mTempCursor, 
                new String[] {ContactDbHelper.OBJECT,ContactDbHelper.MATERIAL,ContactDbHelper.VOLUME,ContactDbHelper.DATE,ContactDbHelper.COMMENT}, 
                new int[] {R.id.editobject,R.id.editmaterial,R.id.editvolume,R.id.editdate, R.id.editcomment});
        mObjectAdapter = new SimpleCursorAdapter(this, 
        		R.layout.item, mObjectCursor, 
                new String[] {ObjectDbHelper.OBJECT}, 
                new int[] {R.id.item});
        mMaterialAdapter = new SimpleCursorAdapter(this, 
        		R.layout.item, mMaterialCursor, 
                new String[] {MaterialDbHelper.MATERIAL}, 
                new int[] {R.id.item});        
        ((ListView)findViewById(R.id.listViewEdit)).setAdapter(mTempAdapter);
        Log.d("EditActivity","count(mTempAdapter)="+mTempAdapter.getCount() );
        spinObject = (Spinner)findViewById(R.id.SpinnerObject);
        spinMaterial = (Spinner)findViewById(R.id.SpinnerMaterial);
        mDateDisplay = (Button) findViewById(R.id.date);
        spinObject.setAdapter(mObjectAdapter);   
        spinMaterial.setAdapter(mMaterialAdapter);
        spinObject.setOnItemSelectedListener(this);
        spinMaterial.setOnItemSelectedListener(this);
        addObjectButton = (Button) findViewById(R.id.addObject);
        addMaterialButton = (Button) findViewById(R.id.addMaterial);
        saveButton = (Button) findViewById(R.id.save);
        addButton = (Button) findViewById(R.id.button_add);
        escButton = (Button) findViewById(R.id.button_esc);
        
        addObjectButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	AddObjectDialog();
            }}
        );
        addMaterialButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	AddMaterialDialog();
            }}
        );
        
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	//AddToTemp(spinObject,spinMaterial);
            }}
        );
        mDateDisplay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	SaveTempToBase();
            }}
        );
        escButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent intent = new Intent();
                intent.setClass(getApplicationContext(), ReportActivity.class);
                startActivity(intent);
                finish();
                
            }}
        );
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);     
        updateDisplay();
    }
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
        case DATE_DIALOG_ID:
            return new DatePickerDialog(this,
                        mDateSetListener,
                        mYear, mMonth, mDay);
        }
        return null;
    }
    // updates the date we display in the TextView
    private void updateDisplay() {
    	Button mDateDisplay = (Button) findViewById(R.id.date);
        mDateDisplay.setText(
            new StringBuilder()
                    // Month is 0 based so add 1
                    .append(mMonth + 1).append("-")
                    .append(mDay).append("-")
                    .append(mYear));
    }   
    // the callback received when the user "sets" the date in the dialog
    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {

                public void onDateSet(DatePicker view, int year, 
                                      int monthOfYear, int dayOfMonth) {
                    mYear = year;
                    mMonth = monthOfYear;
                    mDay = dayOfMonth;
                    updateDisplay();
                }
            };
public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {		
			}           
public void onNothingSelected(AdapterView<?> parent) {	
            }                       
private void AddObjectDialog() {
                LayoutInflater inflater = LayoutInflater.from(this);
                View root = inflater.inflate(R.layout.adddialog, null);
                final EditText text = (EditText)root.findViewById(R.id.addtext);                 
                 
                AlertDialog.Builder b = new AlertDialog.Builder(this);
                b.setView(root);
               
                b.setTitle(getResources().getString(R.string.title_adding_object));
                b.setPositiveButton(
                        R.string.btn_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        ContentValues values = new ContentValues(2);
                        values.put(ObjectDbHelper.OBJECT, text.getText().toString());
                        getContentResolver().insert(ObjectProvider.CONTENT_URI, values);
                        mObjectCursor.requery();
                    };
                    
                });
                b.setNegativeButton(
                        R.string.btn_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {}
                });
                b.show(); 
                
                
            }
private void AddMaterialDialog() {
    LayoutInflater inflater = LayoutInflater.from(this);
    View root = inflater.inflate(R.layout.adddialog, null);
    final EditText text = (EditText)root.findViewById(R.id.addtext);                 
     
    AlertDialog.Builder b = new AlertDialog.Builder(this);
    b.setView(root);
   
    b.setTitle(getResources().getString(R.string.title_adding_material));
    b.setPositiveButton(
            R.string.btn_ok, new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int whichButton) {
            ContentValues values = new ContentValues(2);
            values.put(MaterialDbHelper.MATERIAL, text.getText().toString());
            getContentResolver().insert(MaterialProvider.CONTENT_URI, values);
            mMaterialCursor.requery();
        };
        
    });
    b.setNegativeButton(
            R.string.btn_cancel, new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int whichButton) {}
    });
    b.show(); 
    
}

void SaveTempToBase() {
	 ListView listEdit = (ListView)findViewById(R.id.listViewEdit);
	 int count = listEdit.getAdapter().getCount();
	 AdapterView view = (AdapterView)listEdit;
	 String positionEnd="";
	 for (int i = 0; i < count; i++) {
		 mTempCursor.moveToPosition(i);
		 String id=mTempCursor.getString(0);
		 TextView object = (TextView)view.getChildAt(i).findViewById(R.id.editobject);
		 EditText editMaterial  = (EditText)view.getChildAt(i).findViewById(R.id.editmaterial);
		 EditText editVolume  = (EditText)view.getChildAt(i).findViewById(R.id.editvolume);
		 EditText editDate  = (EditText)view.getChildAt(i).findViewById(R.id.editdate);
		 EditText editComment  = (EditText)view.getChildAt(i).findViewById(R.id.editcomment);
		 ContentValues values = new ContentValues(2);
		 values.put(ContactDbHelper.MATERIAL, editMaterial.getText().toString());
		 values.put(ContactDbHelper.VOLUME, editVolume.getText().toString());
         values.put(ContactDbHelper.DATE, editDate.getText().toString());
         values.put(ContactDbHelper.COMMENT, editComment.getText().toString());      
         getContentResolver().update(
       		  ContactProvider.CONTENT_URI, values, "_ID=" + id, null); 
         updateFactVolume(object.getText().toString(),editMaterial.getText().toString());
		 Log.d("EditActivity", "editVolume="+editVolume.getText().toString());		 
		};
    Context context = getApplicationContext();
    Toast toast = Toast.makeText(context, 
    		getResources().getString(R.string.toast_saved), Toast.LENGTH_LONG);
    toast.setGravity(Gravity.CENTER, 0, 0);
    toast.show();
    Intent intent = new Intent();
    intent.setClass(this, ReportActivity.class);
    startActivity(intent);
    finish();
}
private void updateFactVolume(String obj,String mater){
	String parameter ="object='"+obj+"' and material='"+mater+"'";
	mCursor = managedQuery(
            ContactProvider.CONTENT_URI, mContent, parameter, null,"_ID ASC");//ORDER BY _ID ASC
	
	
    ContentValues values3 = new ContentValues(2);
	values3.put(ContactDbHelper.FLAG_FACT, "-");
	getContentResolver().update(
    		ContactProvider.CONTENT_URI, values3, parameter, null);
	if(!(mCursor.getCount()==0)){
		int factvolume=0;
		for (int i = 0; i < mCursor.getCount(); i++) {
	    	mCursor.moveToPosition(i);
	    	factvolume=factvolume+Integer.parseInt(mCursor.getString(3));
		}	
		ContentValues values4 = new ContentValues(2);
		values4.put(ContactDbHelper.FACTVOLUME, ""+factvolume);
		getContentResolver().update(
	    		ContactProvider.CONTENT_URI, values4,parameter,null);
		
    	Log.d("factvolume=", factvolume+"mCursor.getString(0)=");
    	Log.d("mCursor=", mCursor.getString(0)+mCursor.getString(1)+
    			 mCursor.getString(2)+mCursor.getString(3)+mCursor.getString(4)+
    			 mCursor.getString(5)+mCursor.getString(6)+mCursor.getString(7)+
    			 mCursor.getString(8));
    	mCursor.moveToLast();
    	ContentValues values5 = new ContentValues(2);
		values5.put(ContactDbHelper.FLAG_FACT, "+");
		getContentResolver().update(
	    		ContactProvider.CONTENT_URI, values5,"_ID="+mCursor.getString(0),null);
		}
}
}
