package lib.func.db;

import lib.LibConstants;
import lib.Tool;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class AppDbHelper extends SQLiteOpenHelper
{
    private String TAG = AppDbHelper.class.getSimpleName();
    private static final String DB_NAME = LibConstants.DB_NAME;  
    private static final int DB_VERSION = LibConstants.DB_VERSION; 
    
    
    private Context context  = null;
    
    public AppDbHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }
    
    public AppDbHelper(Context context, String name, CursorFactory factory, int version)
    {
        super(context, name, factory, version);
        this.context = context;
    }
    
    
    @Override
    public void onCreate(SQLiteDatabase db)
    {
    	createDB(db);
        
        //db.close();
    }
    
    private void createDB(SQLiteDatabase db){
    	db.execSQL("DROP TABLE IF EXISTS app_setting");
        
        Log.i(TAG,"db create .....");
        
        
        /*通过数据库升级机制设置是否为第一次启动*/
        Tool.setFirstRun();
    }
     
    private void test(SQLiteDatabase db){
    	
         ContentValues cv = new ContentValues();
         cv.clear();
          
         
    }
    
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        Log.i(TAG,"db upgrade .....");
        //this.backUp();
        createDB(db);

    }
 
    public boolean backUp(){
        boolean flag =true;
        //
        Log.i(TAG,"back up db ....");
        return flag;
        
    }
}