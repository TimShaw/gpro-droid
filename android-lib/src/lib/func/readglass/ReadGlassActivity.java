package lib.func.readglass;

import android.lib.R;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;


/**
 * @ClassName:  ReadGlassActivity.java
 * @Description: 
 * @Author JinChao
 * @Date 2013-7-24 上午9:32:51
 * @Copyright: 版权由 HundSun 拥有
 */
public class ReadGlassActivity extends Activity
{

    private String TAG = ReadGlassActivity.class.getSimpleName();
    
    private WindowManager mWindowManager = null;
    private WindowManager.LayoutParams mLayoutParams;
    private Button button = null;
    private ReadGlassActivity me = this;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.read_glass_layout);
        button = new Button(this);
        button.setOnTouchListener(new View.OnTouchListener()
        {
            
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                me.onTouchEvent(event);
                return false;
            }
        });
        
        
        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        mLayoutParams = new WindowManager.LayoutParams();
        mLayoutParams.format = 1;
        //mLayoutParams.format = PixelFormat.RGBA_8888;
        
        //mLayoutParams.type = 2002;
        //mLayoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        
        mLayoutParams.type =2003;
        //mLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        
        mLayoutParams.flags =40;
        //mLayoutParams.flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        
        //以屏幕的左上角为初始值，设置x y的初值
        //也就是设置LayoutParams的基准！可以改变x y的数值测试效果
        //x:X position for this window.摘自官方文档
        //When using LEFT or START or RIGHT or END it provides an offset from the given edge.
        mLayoutParams.x = 0;
        mLayoutParams.y = 0;
        mLayoutParams.width = 150;
        mLayoutParams.height = 150;
        //mLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        //mLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        
        //mLayoutParams.gravity = Gravity.CENTER;
        //mLayoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        
        mLayoutParams.alpha = 0.7f;
        mLayoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        mWindowManager.addView(button, mLayoutParams);
        
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        //float x = event.getX();
        //float y = event.getY();
        float x = event.getRawX();
        float y = event.getRawY();
        Log.d(TAG, "x:"+x+",y:"+y);
        
        x = x-75;
        y = y-75;
        mLayoutParams.x = (int)x;
        mLayoutParams.y = (int)y;
        
        mWindowManager.updateViewLayout(button, mLayoutParams);
        return super.onTouchEvent(event);
    }
    
    
    
    
}
