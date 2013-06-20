package lib.ui.layout;

import lib.ui.R;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;


/**
 * @ClassName:  TableLayout.java
 * @Description: 
 * @Author JinChao
 * @Date 2012-7-12 上午11:54:36
 * @Copyright: 版权由 HundSun 拥有
 */
public class TableLayout extends Activity
{
    private final String TAG = TableLayout.class.getSimpleName();
    public void onCreate(Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.table_layout);
        
       /* WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        Log.d(TAG, " dpi x: "+metrics.xdpi +" dpi y: "+metrics.ydpi);
        Log.d(TAG, " pixel x: "+metrics.widthPixels +" pixel y: "+metrics.heightPixels);
        int width  = getWindow().getWindowManager().getDefaultDisplay().getWidth();
        int height  = getWindow().getWindowManager().getDefaultDisplay().getHeight();
        
        Log.d(TAG, " width: "+width+"height: "+height);
        Log.d(TAG, " Device : "+ android.os.Build.DEVICE);*/
         
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        int width = this.getWindowManager().getDefaultDisplay().getWidth();//得到系统显示属性后得到屏幕宽度  
        int height = this.getWindowManager().getDefaultDisplay().getHeight();//得到屏幕高度  
        if(width > height){
            Log.d(TAG, "横屏显示...."); 
            android.widget.TableLayout showControlTable = (android.widget.TableLayout)findViewById(R.id.showControlTable);
            showControlTable.setPadding(200, 0, 200, 0); 
        }else{
            Log.d(TAG, "竖屏显示...."); 
        }
        
        
        
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        
        int width = this.getWindowManager().getDefaultDisplay().getWidth();//得到系统显示属性后得到屏幕宽度  
        int height = this.getWindowManager().getDefaultDisplay().getHeight();//得到屏幕高度  
        if(width > height){
            Log.d(TAG, "横屏显示...."); 
            android.widget.TableLayout showControlTable = (android.widget.TableLayout)findViewById(R.id.showControlTable);
            showControlTable.setPadding(200, 0, 200, 0); 
        }else{
            Log.d(TAG, "竖屏显示...."); 
        }
    }
}
