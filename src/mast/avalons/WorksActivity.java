package mast.avalons;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class WorksActivity extends Activity implements AdapterView.OnItemSelectedListener{
	private Button addButton;
    private Button addMaterialButton;
    private Button saveButton;
    private Button escButton;
    View root;
    public static final int IDM_OPEN = 101;
    public static final int IDM_SAVE = 102;
    public static final int IDM_EXIT = 103;
    private Spinner spinMaterial;
    private Cursor mCursor; 
    private Cursor mTempCursor;
    private Cursor mWandMCursor;
    private Cursor mMaterialCursor;
    private Cursor mWorkCursor;
    private ListAdapter mTempAdapter;
    private SimpleCursorAdapter mWandMAdapter;
    private SimpleCursorAdapter mMaterialAdapter;
    private SimpleCursorAdapter mWorkAdapter;
    private static final String[] mTempContent = new String[] {
        TempDbHelper._ID, TempDbHelper.MATERIAL,
        TempDbHelper.VOLUME, TempDbHelper.WORK};
    private static final String[] mContent = new String[] {
        WandMDbHelper._ID, WandMDbHelper.WORK, 
        WandMDbHelper.MATERIAL, WandMDbHelper.VOLUME};
    private static final String[] mContentMaterial = new String[] {
    	MaterialDbHelper._ID, MaterialDbHelper.MATERIAL};
    private static final String[] mContentWork = new String[] {
    	WorkDbHelper._ID, WorkDbHelper.WORK};
    TextView mLabelObj;
    TextView mLabelMat;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.works);
        mCursor = managedQuery(
                WandMProvider.CONTENT_URI, mContent, null, null,null);//ORDER BY _ID ASC
        mTempCursor = managedQuery(
                TempProvider.CONTENT_URI, mTempContent, null, null,"_ID ASC");//ORDER BY _ID ASC
        for (int i = 0; i < mTempCursor.getCount(); i++) {
        	mTempCursor.moveToPosition(i);
        	getContentResolver().delete( TempProvider.CONTENT_URI, null, null);
        }; 
        mWandMCursor = managedQuery(
                WandMProvider.CONTENT_URI, mContentMaterial, null, null,null);//ORDER BY _ID ASC
        mMaterialCursor = managedQuery(
                MaterialProvider.CONTENT_URI, mContentMaterial, null, null,null);//ORDER BY _ID ASC
        mWorkCursor = managedQuery(
                WorkProvider.CONTENT_URI, mContentWork, null, null,null);//ORDER BY _ID ASC
        mTempAdapter = new SimpleCursorAdapter(this, 
                R.layout.temppost, mTempCursor, 
                new String[] {TempDbHelper.MATERIAL,
                TempDbHelper.VOLUME}, 
                new int[] {R.id.tempmaterial, R.id.tempvolume});
        mWandMAdapter = new SimpleCursorAdapter(this, 
        		R.layout.item, mWandMCursor, 
                new String[] {MaterialDbHelper.MATERIAL}, 
                new int[] {R.id.item});  
        mMaterialAdapter = new SimpleCursorAdapter(this, 
        		R.layout.item, mMaterialCursor, 
                new String[] {MaterialDbHelper.MATERIAL}, 
                new int[] {R.id.item});  
        mWorkAdapter = new SimpleCursorAdapter(this, 
        		R.layout.item, mWorkCursor, 
                new String[] {WorkDbHelper.WORK}, 
                new int[] {R.id.item}); 
        ((ListView)findViewById(R.id.listViewTemp)).setAdapter(mTempAdapter);
        spinMaterial = (Spinner)findViewById(R.id.SpinnerMaterial);
        spinMaterial.setAdapter(mMaterialAdapter);
        spinMaterial.setOnItemSelectedListener(this);
        addMaterialButton = (Button) findViewById(R.id.addMaterial);
        saveButton = (Button) findViewById(R.id.btn_save);
        addButton = (Button) findViewById(R.id.button_add);
        escButton = (Button) findViewById(R.id.button_esc);
        addMaterialButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	AddMaterialDialog();
            }}
        );
        
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	AddToTemp(spinMaterial);
            }}
        );
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
    }
public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
	}     
public void onNothingSelected(AdapterView<?> parent) {
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
void AddToTemp(Spinner spinMaterial) {
    EditText volume= (EditText)findViewById(R.id.volume);
    ContentValues values = new ContentValues(2);
    mMaterialCursor.moveToPosition(spinMaterial.getSelectedItemPosition());
    values.put(TempDbHelper.MATERIAL, mMaterialCursor.getString(1).toString());
    values.put(TempDbHelper.VOLUME, volume.getText().toString());
    getContentResolver().insert(TempProvider.CONTENT_URI, values);
};  
void SaveTempToBase() {
	for (int i = 0; i < mTempCursor.getCount(); i++) {
    	mTempCursor.moveToPosition(i);
    	ContentValues values = new ContentValues(2);
    	values.put(WandMDbHelper.WORK,    ((EditText)findViewById(R.id.newWorkName)).getText().toString() );
    	values.put(WandMDbHelper.MATERIAL,  mTempCursor.getString(1) );
        values.put(WandMDbHelper.VOLUME,    mTempCursor.getString(2) );
        getContentResolver().insert(WandMProvider.CONTENT_URI, values).getPath();
        };
        ContentValues values1 = new ContentValues(2);
        values1.put(WorkDbHelper.WORK,    ((EditText)findViewById(R.id.newWorkName)).getText().toString() );
    	getContentResolver().insert(WorkProvider.CONTENT_URI, values1);
    for (int i = 0; i < mTempCursor.getCount(); i++) {
    	mTempCursor.moveToPosition(i);
    	getContentResolver().delete(TempProvider.CONTENT_URI, null, null);
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
}
