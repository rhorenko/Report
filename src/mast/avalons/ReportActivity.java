package mast.avalons;

import java.io.OutputStreamWriter;

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
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class ReportActivity extends Activity implements AdapterView.OnItemSelectedListener,
						OnClickListener,CompoundButton.OnCheckedChangeListener {
	public static final String TAG="ReportActivity";
	private Button addButton;
    private Button addButton1;  
    private Button createWork1;
    private Button masterSpus1;
    private Button exportButton;  
    private Button exportButton1;  
    private Button exportButton3;  
    private Button delButton; 
    private Button delButton1; 
    private Button editButton; 
    private Button editButton1; 
    private Button openButton;
    private Button startDate;
    private Button endDate;
    private RadioButton postypRadioButton2;
    private RadioButton spisanRadioButton2;
    private CheckBox objectCheckBox2;
    private CheckBox materialCheckBox2;
    private CheckBox workCheckBox2;
    private Spinner spinObject2;
    private Spinner spinMaterial2;
    private Spinner spinWork2;
    private RadioButton postypRadioButton;
    private RadioButton spisanRadioButton;
    private CheckBox objectCheckBox;
    private CheckBox materialCheckBox;
    private CheckBox workCheckBox;
    private CheckBox dateCheckBox;
    private Spinner spinObject3;
    private Spinner spinMaterial3;
    private Spinner spinWork3;
    View root;
    public static final int IDM_OPEN = 101;
    public static final int IDM_SAVE = 102;
    public static final int IDM_EXIT = 103;
    private final static String FILENAME =  "exportpostyp.txt";  
    private final static String FILENAME1 = "exportspisanie.txt";  
    private final static String FILENAME3 = "exportvuborka.txt";  
    private String parameter;
    private EditText mEdit;
    EditText id_number;
    private Cursor mCursor; 
    private Cursor mCursor1;
    private Cursor mCursor2;
    private Cursor mCursor3;
    private Cursor mCursorExport;
    private Cursor mObjectCursor;
    private Cursor mMaterialCursor;
    private Cursor mWorkCursor;
    private Cursor mWandMCursor;
    private ListAdapter mAdapter;
    private ListAdapter mAdapter1;
    private ListAdapter mAdapter2;
    private ListAdapter mAdapter3;
    private SimpleCursorAdapter mObjectAdapter;
    private SimpleCursorAdapter mMaterialAdapter;
    private SimpleCursorAdapter mWorkAdapter;
    private static final String[] mContent = new String[] {
            ContactDbHelper._ID, ContactDbHelper.OBJECT, ContactDbHelper.MATERIAL,
            ContactDbHelper.VOLUME,ContactDbHelper.DATE,ContactDbHelper.COMMENT,
            ContactDbHelper.FLAG,ContactDbHelper.FACTVOLUME,ContactDbHelper.ACT,ContactDbHelper.FLAG_ACT};
    private static final String[] mContent3 = new String[] {
        	ContactDbHelper._ID, ContactDbHelper.OBJECT, ContactDbHelper.MATERIAL,
        	ContactDbHelper.VOLUME,ContactDbHelper.DATE,ContactDbHelper.COMMENT,
        	ContactDbHelper.FLAG_FACT,ContactDbHelper.FACTVOLUME,ContactDbHelper.FLAG_FACT,
            ContactDbHelper.ACT,ContactDbHelper.WORK};
    private static final String[] mContentObject = new String[] {
        	ObjectDbHelper._ID, ObjectDbHelper.OBJECT};
    private static final String[] mContentMaterial = new String[] {
    	MaterialDbHelper._ID, MaterialDbHelper.MATERIAL};
    private static final String[] mContentWork = new String[] {
    	WorkDbHelper._ID, WorkDbHelper.WORK};
    
    Context mContext;
    TextView mLabel;
    String[] mObjects = {};
    private int mYear;
    private int mMonth;
    private int mDay;
    Context context;
    static final int DATE_DIALOG_ID = 0;
    static final int DATE_DIALOG_ID_START = 1;
    static final int DATE_DIALOG_ID_END = 2;
    @Override
    public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.main);
       context = getApplicationContext();
       mContext=getApplicationContext();
       mCursor = managedQuery(
                ContactProvider.CONTENT_URI, mContent, "flag='+' and flag_act='+'", null,"_ID ASC");//ORDER BY _ID ASC
       mCursor1 = managedQuery(
                ContactProvider.CONTENT_URI, mContent, "flag='-' and flag_act='+'", null,"object ASC" );//ORDER BY _ID ASC/"_ID DESC"-for reverse
       mCursor2 = managedQuery(
               ContactProvider.CONTENT_URI, mContent3, "flag_fact='+'", null,"object ASC");//ORDER BY _ID ASC   
       mCursor3 = managedQuery(
                ContactProvider.CONTENT_URI, mContent3, null, null,"object ASC");//ORDER BY _ID ASC
       mObjectCursor = managedQuery(
                ObjectProvider.CONTENT_URI, mContentObject, null, null,null);//ORDER BY _ID ASC
       mWorkCursor = managedQuery(
               WorkProvider.CONTENT_URI, mContentWork, null, null,null);//ORDER BY _ID ASC
       mAdapter = new SimpleCursorAdapter(this, 
                R.layout.act, mCursor, 
                new String[] {ContactDbHelper._ID,ContactDbHelper.OBJECT, ContactDbHelper.ACT,
                ContactDbHelper.DATE}, 
                new int[] {R.id.number,R.id.object,R.id.act, R.id.date});
       mAdapter1 = new SimpleCursorAdapter(this, 
               R.layout.act, mCursor1, 
               new String[] {ContactDbHelper._ID,ContactDbHelper.OBJECT, ContactDbHelper.ACT,
               ContactDbHelper.DATE}, 
               new int[] {R.id.number,R.id.object,R.id.act, R.id.date});
       mAdapter2 = new SimpleCursorAdapter(this, 
                R.layout.post, mCursor2, 
                new String[] {ContactDbHelper._ID, ContactDbHelper.OBJECT, ContactDbHelper.MATERIAL,
                ContactDbHelper.FACTVOLUME,ContactDbHelper.DATE,ContactDbHelper.COMMENT}, 
                new int[] {R.id.number,R.id.object,R.id.material, R.id.volume,R.id.date, R.id.comment});
       mAdapter3 = new SimpleCursorAdapter(this, 
                R.layout.post, mCursor3, 
                new String[] {ContactDbHelper._ID,ContactDbHelper.OBJECT, ContactDbHelper.MATERIAL,
                ContactDbHelper.VOLUME,ContactDbHelper.DATE,ContactDbHelper.COMMENT}, 
                new int[] {R.id.number,R.id.object,R.id.material, R.id.volume,R.id.date, R.id.comment});
       mMaterialCursor = managedQuery(
                MaterialProvider.CONTENT_URI, mContentMaterial, null, null,null);//ORDER BY _ID ASC
        
       mObjectAdapter = new SimpleCursorAdapter(this, 
        		R.layout.item, mObjectCursor, 
                new String[] {ObjectDbHelper.OBJECT}, 
                new int[] {R.id.item});
       mMaterialAdapter = new SimpleCursorAdapter(this, 
        		R.layout.item, mMaterialCursor, 
                new String[] {MaterialDbHelper.MATERIAL}, 
                new int[] {R.id.item});    
       mWorkAdapter = new SimpleCursorAdapter(this, 
       		R.layout.item, mWorkCursor, 
               new String[] {WorkDbHelper.WORK}, 
               new int[] {R.id.item});   
        ((ListView)findViewById(R.id.listView0)).setAdapter(mAdapter);
        ((ListView)findViewById(R.id.listView1)).setAdapter(mAdapter1);
        ((ListView)findViewById(R.id.listView2)).setAdapter(mAdapter2);
        ((ListView)findViewById(R.id.listView3)).setAdapter(mAdapter3);
        TabHost tabs=(TabHost)findViewById(R.id.tabhost);       
        tabs.setup();
        TabHost.TabSpec spec=tabs.newTabSpec("tag1");
        spec.setContent(R.id.tabPage0);
        spec.setIndicator("                         "+getResources().getString(R.string.title_tab0)+"             ");
        tabs.addTab(spec);
        spec=tabs.newTabSpec("tag2");
        spec.setContent(R.id.tabPage1);
        spec.setIndicator("                   "+getResources().getString(R.string.title_tab1)+"                 ");
        tabs.addTab(spec);
        spec=tabs.newTabSpec("tag3");
        spec.setContent(R.id.tabPage2);  
        spec.setIndicator("                   "+getResources().getString(R.string.title_tab2)+"                 ");
        tabs.addTab(spec); 
        spec=tabs.newTabSpec("tag4");
        spec.setContent(R.id.tabPage3);
        spec.setIndicator("               "+getResources().getString(R.string.title_tab3)+"                     ");
        tabs.addTab(spec);
        tabs.setCurrentTab(1);
        addButton = (Button) findViewById(R.id.button_add);
        addButton1 = (Button) findViewById(R.id.button_add1);
        exportButton = (Button) findViewById(R.id.button_export3);
        createWork1 = (Button) findViewById(R.id.button_work1);
        masterSpus1 = (Button) findViewById(R.id.button_master1);
        editButton = (Button) findViewById(R.id.button_edit);
        editButton1 = (Button) findViewById(R.id.button_edit1);
        delButton = (Button) findViewById(R.id.button_del);
        delButton1 = (Button) findViewById(R.id.button_del1);
        postypRadioButton = (RadioButton) findViewById(R.id.radio0);
        spisanRadioButton= (RadioButton) findViewById(R.id.radio1);
        objectCheckBox = (CheckBox) findViewById(R.id.objectCheckBox3);
        materialCheckBox = (CheckBox) findViewById(R.id.materialCheckBox3);
        workCheckBox = (CheckBox) findViewById(R.id.workCheckBox3);
        spinObject3 = (Spinner) findViewById(R.id.objectSpinner3);
        spinMaterial3 = (Spinner) findViewById(R.id.materialSpinner3);
        spinWork3 = (Spinner) findViewById(R.id.workSpinner3);
        addButton.setOnClickListener(this);
        addButton1.setOnClickListener(this);
        createWork1.setOnClickListener(this);
        delButton1.setOnClickListener(this);
        postypRadioButton.setOnClickListener(this);
        spisanRadioButton.setOnClickListener(this);
        spinObject3.setAdapter(mObjectAdapter);   
        spinMaterial3.setAdapter(mMaterialAdapter);
        spinWork3.setAdapter(mWorkAdapter);
        objectCheckBox2 = (CheckBox) findViewById(R.id.objectCheckBox2);
        materialCheckBox2 = (CheckBox) findViewById(R.id.materialCheckBox2);
        workCheckBox2 = (CheckBox) findViewById(R.id.workCheckBox2);
        spinObject2 = (Spinner) findViewById(R.id.objectSpinner2);
        spinMaterial2 = (Spinner) findViewById(R.id.materialSpinner2);
        spinWork2 = (Spinner) findViewById(R.id.workSpinner2);
        addButton.setOnClickListener(this);
        addButton1.setOnClickListener(this);
        createWork1.setOnClickListener(this);
        delButton1.setOnClickListener(this);
        postypRadioButton.setOnClickListener(this);
        spisanRadioButton.setOnClickListener(this);
        spinObject2.setAdapter(mObjectAdapter);   
        spinMaterial2.setAdapter(mMaterialAdapter);
        spinWork2.setAdapter(mWorkAdapter);
        spinObject3.setAdapter(mObjectAdapter);   
        spinMaterial3.setAdapter(mMaterialAdapter);
        spinWork3.setAdapter(mWorkAdapter);
        objectCheckBox2.setOnCheckedChangeListener(this);
        materialCheckBox2.setOnCheckedChangeListener(this);
        workCheckBox2.setOnCheckedChangeListener(this);
        spinObject2.setOnItemSelectedListener(this);
        spinMaterial2.setOnItemSelectedListener(this);
        spinWork2.setOnItemSelectedListener(this);
        objectCheckBox.setOnCheckedChangeListener(this);
        materialCheckBox.setOnCheckedChangeListener(this);
        workCheckBox.setOnCheckedChangeListener(this);
        spinObject3.setOnItemSelectedListener(this);
        spinMaterial3.setOnItemSelectedListener(this);
        spinWork3.setOnItemSelectedListener(this);
        masterSpus1.setOnClickListener(this);
        exportButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	parameter=sortBy(postypRadioButton,spisanRadioButton,objectCheckBox,
    					materialCheckBox,workCheckBox,spinObject3,spinMaterial3,spinWork3);
    			mCursorExport= getContentResolver().query(
    	                ContactProvider.CONTENT_URI, mContent3, parameter, null,"_ID ASC");//ORDER BY _ID ASC
    			mCursorExport.moveToFirst();
    			String FileName = "";
            	FileName=FileName+"Q";
            	FileName=FileName+mCursorExport.getString(9)+"_"+mCursorExport.getString(4);
            	saveFile(FileName,mCursorExport); 
            	Context context = getApplicationContext();
                Toast toast = Toast.makeText(context, 
                		getResources().getString(R.string.toast_exported), Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }}
        );
        delButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	CallDeleteDialog();
            }}
        );
        editButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	CallEditDialog();
            }}
        );
        editButton1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	CallEditDialog1();
            }}
        );
        ListView list0 = (ListView)findViewById(R.id.listView0);
        list0.setOnItemClickListener(new OnItemClickListener() {
           
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				arg0.getChildCount();
				TextView act = (TextView)arg1.findViewById(R.id.act);
				Log.d("----", "text="+act.getText().toString());
				Intent intent1 = new Intent();
				
		        intent1.setClass(mContext, ActActivity.class);
		        intent1.putExtra(ActActivity.EXT_FLAG, "+");
		        intent1.putExtra(ActActivity.EXT_ACT, act.getText().toString());
		        startActivity(intent1);
		        finish();
    			
			}}
        );
        		ListView list1 = (ListView)findViewById(R.id.listView1);
        		list1.setOnItemClickListener(new OnItemClickListener() {
           
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				TextView act = (TextView)arg1.findViewById(R.id.act);
				Log.d("----", "text="+act.getText().toString());
				Intent intent1 = new Intent();
				
		        intent1.setClass(mContext, ActActivity.class);
		        intent1.putExtra(ActActivity.EXT_FLAG, "-");
		        intent1.putExtra(ActActivity.EXT_ACT, act.getText().toString());
		        startActivity(intent1);
		        finish();
    			
			}}
        ); 		
       mLabel = (TextView)findViewById(R.id.mText);
        
  }
public void onClick(View v) {
    	    	
    	switch (v.getId()) {
		case R.id.button_add:
			Intent intent = new Intent();
	        intent.setClass(this, PostypActivity.class);
	        startActivity(intent);
	        finish();
			break;
		case R.id.button_add1:
			Intent intent1 = new Intent();
	        intent1.setClass(this, SpisActivity.class);
	        startActivity(intent1);
	        finish();
			break;
		case R.id.button_work1:
			Intent intent2= new Intent();
	        intent2.setClass(this, WorksActivity.class);
	        startActivity(intent2);
	        finish();
			break;
		case R.id.radio0:
			parameter=sortBy(postypRadioButton,spisanRadioButton,objectCheckBox,
					materialCheckBox,workCheckBox,spinObject3,spinMaterial3,spinWork3);
			mCursor3= getContentResolver().query(
	                ContactProvider.CONTENT_URI, mContent3, parameter, null,"_ID ASC");//ORDER BY _ID ASC
			mAdapter3 = new SimpleCursorAdapter(this, 
	                R.layout.post, mCursor3, 
	                new String[] {ContactDbHelper._ID,ContactDbHelper.OBJECT, ContactDbHelper.MATERIAL,
	                ContactDbHelper.FACTVOLUME,ContactDbHelper.DATE,ContactDbHelper.COMMENT}, 
	                new int[] {R.id.number,R.id.object,R.id.material, R.id.volume,R.id.date, R.id.comment});
			((ListView)findViewById(R.id.listView3)).setAdapter(mAdapter3);
			mCursor3.requery();
			break;
		case R.id.radio1:
			parameter=sortBy(postypRadioButton,spisanRadioButton,objectCheckBox,
					materialCheckBox,workCheckBox,spinObject3,spinMaterial3,spinWork3);
			Log.e("onClick", "parameter="+parameter);
			mCursor3= getContentResolver().query(
	                ContactProvider.CONTENT_URI, mContent3, parameter, null,"_ID ASC");//ORDER BY _ID ASC
			mAdapter3 = new SimpleCursorAdapter(this, 
	                R.layout.post, mCursor3, 
	                new String[] {ContactDbHelper._ID,ContactDbHelper.OBJECT, ContactDbHelper.MATERIAL,
	                ContactDbHelper.VOLUME,ContactDbHelper.DATE,ContactDbHelper.COMMENT}, 
	                new int[] {R.id.number,R.id.object,R.id.material, R.id.volume,R.id.date, R.id.comment});
			((ListView)findViewById(R.id.listView3)).setAdapter(mAdapter3);
			mCursor3.requery();
			break;
		case R.id.button_del1:
			CallDeleteDialog();
			break;
		case R.id.button_master1:
			CallSpusAvtomDialog1();
			break;
    	};
     }
    @Override
protected Dialog onCreateDialog(int id) {
        switch (id) {
        case DATE_DIALOG_ID_START:
            return new DatePickerDialog(this,
                        mStartDateSetListener,
                        mYear, mMonth, mDay);
            
        case DATE_DIALOG_ID_END:
            return new DatePickerDialog(this,
                        mEndDateSetListener,
                        mYear, mMonth, mDay);
        }
        return null;
    }
    // the callback received when the user "sets" the date in the dialog
private DatePickerDialog.OnDateSetListener mStartDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, 
                                      int monthOfYear, int dayOfMonth) {
                	 mYear = year;
                     mMonth = monthOfYear;
                     mDay = dayOfMonth;
                     startDate.setText(
                                 new StringBuilder()
                                         // Month is 0 based so add 1
                                         .append(mMonth + 1).append("-")
                                         .append(mDay).append("-")
                                         .append(mYear).append(" "));
                   }
   };
private DatePickerDialog.OnDateSetListener mEndDateSetListener =
           new DatePickerDialog.OnDateSetListener() {
               public void onDateSet(DatePicker view, int year, 
                                     int monthOfYear, int dayOfMonth) {
               	 	mYear = year;
                    mMonth = monthOfYear;
                    mDay = dayOfMonth;
                    endDate.setText(
                                new StringBuilder()
                                        // Month is 0 based so add 1
                                        .append(mMonth + 1).append("-")
                                        .append(mDay).append("-")
                                        .append(mYear).append(" "));
                  }
  };   
public void onItemSelected(AdapterView<?> parent, View v, int position, long id) { }
public void onNothingSelected(AdapterView<?> parent) {     }
public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {  
	
	mCursor3.requery();
		parameter=sortBy(postypRadioButton,spisanRadioButton,objectCheckBox,
				materialCheckBox,workCheckBox,spinObject3,spinMaterial3,spinWork3);
		Log.d("onClick", "parameter="+parameter)  ;
		mCursor3= getContentResolver().query(
                ContactProvider.CONTENT_URI, mContent3, parameter, null,null);//ORDER BY _ID ASC
		Log.d("numberforselect", "n="+mCursor3.getCount()+"parameter="+parameter)  ;
		mAdapter3 = new SimpleCursorAdapter(this, 
                R.layout.post, mCursor3, 
                new String[] {ContactDbHelper._ID,ContactDbHelper.OBJECT, ContactDbHelper.MATERIAL,
                ContactDbHelper.FACTVOLUME,ContactDbHelper.DATE,ContactDbHelper.COMMENT}, 
                new int[] {R.id.number,R.id.object,R.id.material, R.id.volume,R.id.date, R.id.comment});
		((ListView)findViewById(R.id.listView3)).setAdapter(mAdapter3);	
		mCursor3.requery();
		
		mCursor2.requery();
		parameter=sortBy(objectCheckBox2,
				materialCheckBox2,workCheckBox2,spinObject2,spinMaterial2,spinWork2);
		Log.d("onClick", "parameter="+parameter)  ;
		mCursor2= getContentResolver().query(
                ContactProvider.CONTENT_URI, mContent3, checkParam(parameter), null,null);//ORDER BY _ID ASC
		Log.d("numberforselect", "n="+mCursor3.getCount()+"parameter="+parameter)  ;
		mAdapter2 = new SimpleCursorAdapter(this, 
                R.layout.post, mCursor2, 
                new String[] {ContactDbHelper._ID,ContactDbHelper.OBJECT, ContactDbHelper.MATERIAL,
                ContactDbHelper.FACTVOLUME,ContactDbHelper.DATE,ContactDbHelper.COMMENT}, 
                new int[] {R.id.number,R.id.object,R.id.material, R.id.volume,R.id.date, R.id.comment});
		((ListView)findViewById(R.id.listView2)).setAdapter(mAdapter2);	
		mCursor2.requery();
	
}
                        
private void CallAddContactDialog() {
                LayoutInflater inflater = LayoutInflater.from(this);
                View root = inflater.inflate(R.layout.dialog_postyp, null);
                final EditText textObject = (EditText)root.findViewById(R.id.object);                 
                final EditText textMaterial = (EditText)root.findViewById(R.id.material);
                final EditText textVolume = (EditText)root.findViewById(R.id.volume);
                final EditText textComment = (EditText)root.findViewById(R.id.comment);
                final Button mDateDisplay = (Button) root.findViewById(R.id.date);
                
                   Log.e("ReportActivity", "CallAddContact"+mMonth+mDay+mYear)  ; 
                   // display the current date
                   
                   
                AlertDialog.Builder b = new AlertDialog.Builder(this);
                b.setView(root);
               
                b.setTitle(R.string.title_add);
                b.setPositiveButton(
                        R.string.btn_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        ContentValues values = new ContentValues(2);
                        
                       
                        values.put(ContactDbHelper.OBJECT, textObject.getText().toString());
                        values.put(ContactDbHelper.MATERIAL, textMaterial.getText().toString());
                        values.put(ContactDbHelper.VOLUME, textVolume.getText().toString()); 
                        values.put(ContactDbHelper.DATE,mDateDisplay.getText().toString() ); 
                        values.put(ContactDbHelper.COMMENT, textComment.getText().toString());
                        values.put(ContactDbHelper.FLAG, "+");
                        values.put(ContactDbHelper.FACTVOLUME, textVolume.getText().toString());
                        
                        getContentResolver().insert(ContactProvider.CONTENT_URI, values);
                        mCursor.requery();
                    };
                    
                });
                b.setNegativeButton(
                        R.string.btn_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {}
                });
                b.show(); 
                mDateDisplay.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        showDialog(DATE_DIALOG_ID);
                        mDateDisplay.setText(
                                new StringBuilder()
                                        // Month is 0 based so add 1
                                        .append(mMonth + 1).append("-")
                                        .append(mDay).append("-")
                                        .append(mYear).append(" "));
                    }
                });
                
            }
private void CallAddDialog1() {
      LayoutInflater inflater = LayoutInflater.from(this);
      View root = inflater.inflate(R.layout.dialog_postyp, null);
      final EditText textObject = (EditText)root.findViewById(R.id.object);                 
      final EditText textMaterial = (EditText)root.findViewById(R.id.material);
      final EditText textVolume = (EditText)root.findViewById(R.id.volume);
      final EditText textComment = (EditText)root.findViewById(R.id.comment);
      final Button mDateDisplay = (Button) root.findViewById(R.id.date);
      // add a click listener to the button
         
         
        
         Log.e("ReportActivity", "CallAddContact"+mMonth+mDay+mYear)  ; 
         // display the current date
         
         
      AlertDialog.Builder b = new AlertDialog.Builder(this);
      b.setView(root);
     
      b.setTitle(R.string.title_add);
      b.setPositiveButton(
              R.string.btn_ok, new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int whichButton) {
              ContentValues values = new ContentValues(2);
              
             
              values.put(ContactDbHelper.OBJECT, textObject.getText().toString());
              values.put(ContactDbHelper.MATERIAL, textMaterial.getText().toString());
              values.put(ContactDbHelper.VOLUME, textVolume.getText().toString()); 
              values.put(ContactDbHelper.DATE,mDateDisplay.getText().toString() ); 
              values.put(ContactDbHelper.COMMENT, textComment.getText().toString());
              values.put(ContactDbHelper.FLAG, "-");
              int minus=Integer.parseInt(textVolume.getText().toString());
              getContentResolver().insert(ContactProvider.CONTENT_URI, values);
              mCursor.requery(); 
              
              //getContentResolver().update(
            		 // ContactProvider.CONTENT_URI, values, "OBJECT="+textObject.getText().toString()
            		//  +" AND MATERIAL="+textMaterial.getText().toString()+" AND FLAG='+'" , null);
              mCursor=getContentResolver().query(ContactProvider.CONTENT_URI, mContent, "object='"+textObject.getText().toString()
              		  +"' and material='"+textMaterial.getText().toString()+"' and flag='+'", null, "_ID DESC");
              mCursor.moveToPosition(0);
              Log.e("mCursor.getString(7)=", ""+mCursor.getString(7));
              int factvolume=Integer.parseInt(mCursor.getString(7))-minus;
              values.put(ContactDbHelper.FACTVOLUME, Integer.toString(factvolume));
              values.put(ContactDbHelper.FLAG, "+");
             int position = mCursor.getPosition();
             int colCount = mCursor.getColumnCount();
             int count = mCursor.getCount();
             String column = mCursor.getColumnName(6);
             Log.e("DATABASE position=", ""+position);
             Log.e("DATABASE colCount=", ""+colCount);
             Log.e("DATABASE count=", ""+count);
             Log.e("DATABASE column=", ""+column);
             //Log.e("DATABASE buffer=", ""+mCursor.getString(2)+""+mCursor.getString(3));
             getContentResolver().update(
            	ContactProvider.CONTENT_URI, values, "object='"+textObject.getText().toString()
        		  +"' and material='"+textMaterial.getText().toString()+"' and flag='+'", null);
             mCursor.requery();
              //values.put(ContactDbHelper.OBJECT, textObject.getText().toString());
              //values.put(ContactDbHelper.MATERIAL, textMaterial.getText().toString());
              //values.put(ContactDbHelper.COMMENT, textComment.getText().toString());
              //values.put(ContactDbHelper.FLAG, "+");
              //values.put(ContactDbHelper.FACTVOLUME, "");
              //getContentResolver().update(
            		//  ContactProvider.CONTENT_URI, values, null, null);
              //mCursorReplace.requery();
              //Cursor mCursorReplace = query(ContactDbHelper.TABLE_NAME, , , , null, null, orderBy);
              
          };
          
      });
      b.setNegativeButton(
              R.string.btn_cancel, new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int whichButton) {}
      });
      b.show(); 
      mDateDisplay.setOnClickListener(new View.OnClickListener() {
          public void onClick(View v) {
              showDialog(DATE_DIALOG_ID);
              mDateDisplay.setText(
                      new StringBuilder()
                              // Month is 0 based so add 1
                              .append(mMonth + 1).append("-")
                              .append(mDay).append("-")
                              .append(mYear).append(" "));
          }
      });
      
      
  }
            

private void CallEditDialog() {
	LayoutInflater inflater = LayoutInflater.from(this);
	root = inflater.inflate(R.layout.dialog, null);
	AlertDialog.Builder b = new AlertDialog.Builder(this);
	b.setView(root);

	b.setTitle(getResources().getString(R.string.title_add_editing));
	b.setPositiveButton(
			R.string.btn_ok, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					final EditText id_number = (EditText)root.findViewById(R.id.id_number0);
					String id = id_number.getText().toString();
					Log.d("Edited ID=", ""+id);
					Intent intent = new Intent();
			        intent.setClass(getApplicationContext(), EditActivity.class);
			        intent.putExtra(EditActivity.EXT_ID, id);
			        intent.putExtra(EditActivity.EXT_FLAG, "+");
			        startActivity(intent);
			        finish();
				}
			});
	b.setNegativeButton(
			R.string.btn_cancel, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {}
			});
	b.show();
}
private void CallEditDialog1() {
	LayoutInflater inflater = LayoutInflater.from(this);
	root = inflater.inflate(R.layout.dialog, null);
	AlertDialog.Builder b = new AlertDialog.Builder(this);
	b.setView(root);

	b.setTitle(getResources().getString(R.string.title_delete_editing));
	b.setPositiveButton(
			R.string.btn_ok, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					final EditText id_number = (EditText)root.findViewById(R.id.id_number0);
					String id = id_number.getText().toString();
					Log.d("Edited ID=", ""+id);
					Intent intent = new Intent();
			        intent.setClass(getApplicationContext(), EditActivity.class);
			        intent.putExtra(EditActivity.EXT_ID, id);
			        intent.putExtra(EditActivity.EXT_FLAG, "-");
			        startActivity(intent);
			        finish();
				}
			});
	b.setNegativeButton(
			R.string.btn_cancel, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {}
			});
	b.show();
}
private void CallSpusAvtomDialog1() {
	LayoutInflater inflater = LayoutInflater.from(this);
	root = inflater.inflate(R.layout.dialog_spus_avtom, null);
	AlertDialog.Builder b = new AlertDialog.Builder(this);
	Spinner spinnerWorkAvtom = (Spinner) root.findViewById(R.id.SpinnerWorkAvtom);
	Spinner spinnerObjectAvtom = (Spinner) root.findViewById(R.id.SpinnerObjectAvtom);
	spinnerWorkAvtom.setAdapter(mWorkAdapter);
	spinnerObjectAvtom.setAdapter(mObjectAdapter);
	
	b.setView(root);
	b.setTitle(getResources().getString(R.string.title_spis_avtomatom));
	b.setPositiveButton(
			R.string.btn_ok, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					final EditText count_work = (EditText)root.findViewById(R.id.count_work);
					String work=mWorkCursor.getString(1);	
					String object=mObjectCursor.getString(1);
					String count=count_work.getText().toString();
					Intent intent1 = new Intent();
			        intent1.setClass(context, SpisMasterActivity.class);
			        intent1.putExtra(SpisMasterActivity.EXT_WORK, work);
			        intent1.putExtra(SpisMasterActivity.EXT_OBJECT, object);
			        intent1.putExtra(SpisMasterActivity.EXT_COUNT, count);
			        startActivity(intent1);
			        finish();
				}
			});
	b.setNegativeButton(
			R.string.btn_cancel, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {}
			});
	b.show();
}
            
private void CallDeleteDialog() {
	LayoutInflater inflater = LayoutInflater.from(this);
    root = inflater.inflate(R.layout.dialog, null);
    AlertDialog.Builder b = new AlertDialog.Builder(this);
    b.setView(root);
    
    b.setTitle(getResources().getString(R.string.title_delete));
    b.setPositiveButton(
            R.string.btn_ok, new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int whichButton) {
        	final EditText id_number = (EditText)root.findViewById(R.id.id_number0);
        	String act = id_number.getText().toString();
        	String id;
        	String obj;
        	String mater;
        	Cursor mTempCursor = managedQuery(
                    ContactProvider.CONTENT_URI, mContent, "act='"+act+"'", null,"_ID ASC");
        	for (int i = 0; i < mTempCursor.getCount(); i++) {
        		mTempCursor.moveToPosition(i);
        		id=mTempCursor.getString(0);
        		obj=mTempCursor.getString(1);
            	mater = mTempCursor.getString(2);
            	Log.d("Deleted act=", ""+act+"obj="+obj+"mater="+mater+"_ID="+id);
                getContentResolver().delete(ContactProvider.CONTENT_URI, "_ID='"+id+"'", null);
                updateFactVolume(obj,mater);
			}
        	
        	 
        }
    });
    b.setNegativeButton(
            R.string.btn_cancel, new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int whichButton) {}
    });
b.show();
}

private void saveFile(String FileName, Cursor mCursorExport) {
                try {
                    OutputStreamWriter outStream = 
                       new OutputStreamWriter(openFileOutput(FileName, 0));
                    
                    
                   // textName.setText(mCursor.getString(1));
                   // textVOLUME.setText(mCursor.getString(2));
                    for (int i = 0; i < mCursorExport.getCount(); i++) {
                    mCursorExport.moveToPosition(i);
                    outStream.write(""+Integer.toString(i+1)+"  "+mCursorExport.getString(9)+"    "
                    +mCursorExport.getString(4)+"    "+mCursorExport.getString(1)+"   "+mCursorExport.getString(3)+"   "+mCursorExport.getString(2)+"\n");
            		}
                    
                    outStream.close();      
                }
                catch (Throwable t) {
                    Toast.makeText(getApplicationContext(), 
                          "Exception: " + t.toString(), Toast.LENGTH_LONG)
                        .show();
                }
            }
private String  sortBy(RadioButton postypRadioButton,RadioButton spisanRadioButton,CheckBox objectCheckBox,
		CheckBox materialCheckBox,CheckBox workCheckBox,Spinner spinObject3,Spinner spinMaterial3,Spinner spinWork3)   {
	String parameter="";
	if (objectCheckBox.isChecked()){
    	mObjectCursor.moveToPosition(spinObject3.getSelectedItemPosition());
    	parameter=parameter+"object='"+mObjectCursor.getString(1)+"' and ";
		//materialCheckBox.Log.d("----", ""+parameter);
	};
	if (materialCheckBox.isChecked()){
		parameter=parameter+"material='"+mMaterialCursor.getString(1)+"' and ";
		Log.d("----", ""+parameter);
	};
	if (workCheckBox.isChecked()){
		parameter=parameter+"work='"+mWorkCursor.getString(1)+"' and ";
		Log.d("----", ""+parameter);
	};
	if (spisanRadioButton.isChecked()){
		parameter=parameter+"flag='-'";
		Log.d("----", ""+parameter);
	};
	if (postypRadioButton.isChecked()){
		parameter=parameter+"flag='+'";
		Log.d("----", ""+parameter);
	};
	Log.d("----", ""+parameter);
	if((!spisanRadioButton.isChecked())&(!postypRadioButton.isChecked()))
	{
		if(!(parameter==""))parameter=parameter.substring(0,parameter.length()-5);
		
	}
	Log.d("----", ""+parameter);
	
	return parameter;
}   
private String  sortBy(CheckBox objectCheckBox,
		CheckBox materialCheckBox,CheckBox workCheckBox,Spinner spinObject3,Spinner spinMaterial3,Spinner spinWork3)   {
	String parameter="";
	if (objectCheckBox.isChecked()){
    	mObjectCursor.moveToPosition(spinObject3.getSelectedItemPosition());
    	parameter=parameter+"object='"+mObjectCursor.getString(1)+"' and ";
		//materialCheckBox.Log.d("----", ""+parameter);
	};
	if (materialCheckBox.isChecked()){
		parameter=parameter+"material='"+mMaterialCursor.getString(1)+"' and ";
		//Log.d("----", ""+parameter);
	};
	if (workCheckBox.isChecked()){
		parameter=parameter+"work='"+mWorkCursor.getString(1)+"' and ";
		//Log.d("----", ""+parameter);
	};
	
		if(!(parameter==""))parameter=parameter.substring(0,parameter.length()-5);
		
	
	Log.d("----", ""+parameter);
	
	return parameter;
} 
String checkParam(String parameter){
	if (!(parameter=="")){parameter=parameter+" and flag_fact='+'";
	}else{parameter="flag_fact='+'";};
	return parameter;
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