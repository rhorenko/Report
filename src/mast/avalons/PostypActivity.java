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

public class PostypActivity extends Activity implements AdapterView.OnItemSelectedListener{
	
	private Button mDateDisplay;
    private Button addObjectButton;
    private Button addButton;
    private Button addMaterialButton;
    private Button saveButton;
    private Button editButton;  
    private Button delButton; 
    private Button openButton;
    private Button escButton;
    private TextView actNumber;
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

    static final int DATE_DIALOG_ID = 0;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.postyp);
        mCursor = managedQuery(
                ContactProvider.CONTENT_URI, mContent, "flag='+'", null,null);//ORDER BY _ID ASC
        mTempCursor = managedQuery(
                TempProvider.CONTENT_URI, mTempContent, "flag='+'", null,"_ID ASC");//ORDER BY _ID ASC
        mObjectCursor = managedQuery(
                ObjectProvider.CONTENT_URI, mContentObject, null, null,null);//ORDER BY _ID ASC
        for (int i = 0; i < mTempCursor.getCount(); i++) {
        	mTempCursor.moveToPosition(i);
        	getContentResolver().delete( TempProvider.CONTENT_URI, null, null);
        }; 
        mMaterialCursor = managedQuery(
                MaterialProvider.CONTENT_URI, mContentMaterial, null, null,null);//ORDER BY _ID ASC
        mTempAdapter = new SimpleCursorAdapter(this, 
                R.layout.temppost, mTempCursor, 
                new String[] {TempDbHelper.MATERIAL,
                TempDbHelper.VOLUME,TempDbHelper.DATE,TempDbHelper.COMMENT}, 
                new int[] {R.id.tempmaterial, R.id.tempvolume,R.id.tempdate, R.id.tempcomment});
        mObjectAdapter = new SimpleCursorAdapter(this, 
        		R.layout.item, mObjectCursor, 
                new String[] {ObjectDbHelper.OBJECT}, 
                new int[] {R.id.item});
        mMaterialAdapter = new SimpleCursorAdapter(this, 
        		R.layout.item, mMaterialCursor, 
                new String[] {MaterialDbHelper.MATERIAL}, 
                new int[] {R.id.item});        
        ((ListView)findViewById(R.id.listViewTemp)).setAdapter(mTempAdapter);
                
        spinObject = (Spinner)findViewById(R.id.SpinnerObject);
        spinMaterial = (Spinner)findViewById(R.id.SpinnerMaterial);
        mDateDisplay = (Button) findViewById(R.id.date);
        spinObject.setAdapter(mObjectAdapter);   
        spinMaterial.setAdapter(mMaterialAdapter);
        spinObject.setOnItemSelectedListener(this);
        spinMaterial.setOnItemSelectedListener(this);
        actNumber = (TextView)findViewById(R.id.act_number);
        addObjectButton = (Button) findViewById(R.id.addObject);
        addMaterialButton = (Button) findViewById(R.id.addMaterial);
        saveButton = (Button) findViewById(R.id.save);
        addButton = (Button) findViewById(R.id.button_add);
        escButton = (Button) findViewById(R.id.button_esc);
        String actnumer = Integer.toString(managedQuery(
                ContactProvider.CONTENT_URI, mContent, null, null,null).getCount()+1);
        int num=6-actnumer.length();
        for (int k = 0; k < num; k++) {
        	actnumer="0"+actnumer;
    	}
        actNumber.setText(getResources().getString(R.string.act_number)+"   "+actnumer);
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
            	AddToTemp(spinObject,spinMaterial);
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
            	Context context = v.getContext();
            	for (int i = 0; i < mTempCursor.getCount(); i++) {
                	mTempCursor.moveToPosition(i);
                	getContentResolver().delete( TempProvider.CONTENT_URI, null, null);
                	}; 
                mTempCursor.requery();
                Intent intent = new Intent();
                intent.setClass((Activity)context, ReportActivity.class);
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
                    .append(mDay).append("-")
                    .append(mMonth + 1).append("-")
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
void AddToTemp(Spinner spinObject,Spinner spinMaterial) {
    EditText volume= (EditText)findViewById(R.id.volume_postyp);
    EditText comment= (EditText)findViewById(R.id.comment_postyp); 
    Button date=(Button)findViewById(R.id.date); 
    ContentValues values = new ContentValues(2);
    mObjectCursor.moveToPosition(spinObject.getSelectedItemPosition());
    mMaterialCursor.moveToPosition(spinMaterial.getSelectedItemPosition());
    values.put(TempDbHelper.OBJECT, mObjectCursor.getString(1));
    values.put(TempDbHelper.MATERIAL, mMaterialCursor.getString(1));
    values.put(TempDbHelper.VOLUME, volume.getText().toString());
    values.put(TempDbHelper.DATE,date.getText().toString() );
    values.put(TempDbHelper.COMMENT, comment.getText().toString());
    values.put(TempDbHelper.FLAG, "+");
    values.put(TempDbHelper.FACTVOLUME, volume.getText().toString());
    values.put(TempDbHelper.FLAG_FACT, "+");//?
    getContentResolver().insert(TempProvider.CONTENT_URI, values);
};  
void SaveTempToBase() {
	String actnum = Integer.toString(managedQuery(
            ContactProvider.CONTENT_URI, mContent, null, null,null).getCount()+1);
	int num=6-actnum.length();
    for (int j = 0; j < num; j++) {
    	actnum="0"+actnum;
	}
    String positionEnd="";
	for (int i = 0; i < mTempCursor.getCount(); i++) {
    	mTempCursor.moveToPosition(i);
    	ContentValues values = new ContentValues(2);
    	values.put(ContactDbHelper.OBJECT,    mTempCursor.getString(1));
        values.put(ContactDbHelper.MATERIAL,  mTempCursor.getString(2) );
        values.put(ContactDbHelper.VOLUME,    mTempCursor.getString(3) ); 
        values.put(ContactDbHelper.DATE,      mTempCursor.getString(4) ); 
        values.put(ContactDbHelper.COMMENT,   mTempCursor.getString(5) );
        values.put(ContactDbHelper.FLAG, "+");
        values.put(ContactDbHelper.FACTVOLUME,mTempCursor.getString(7) );
        values.put(ContactDbHelper.FLAG_FACT, mTempCursor.getString(8) );
        values.put(ContactDbHelper.ACT,       actnum);
        getContentResolver().insert(ContactProvider.CONTENT_URI, values).getPath();
        updateFactVolume(mTempCursor.getString(1),mTempCursor.getString(2));
        };
        mCursor.moveToLast();
        positionEnd = mCursor.getString(0);
        Log.e("posEndLast=",positionEnd);
        ContentValues values10 = new ContentValues(2);
    	values10.put(ContactDbHelper.FLAG_ACT,  "+"  );
    	getContentResolver().update(
        		ContactProvider.CONTENT_URI, values10, "_ID="+positionEnd, null);
    for (int i = 0; i < mTempCursor.getCount(); i++) {
    	mTempCursor.moveToPosition(i);
    	getContentResolver().delete( TempProvider.CONTENT_URI, null, null);
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
