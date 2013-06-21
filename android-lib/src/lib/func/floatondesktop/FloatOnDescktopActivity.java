package lib.func.floatondesktop;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;


/**
 * @ClassName:  FloatOnDescktopActivity.java
 * @Description: 
 * @Author JinChao
 * @Date 2013-6-21 上午11:19:42
 * @Copyright: 版权由 HundSun 拥有
 */
public class FloatOnDescktopActivity extends Activity
{
    
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        final TextView tv = new TextView(getApplicationContext());
        tv.setText("word");
        
        FloatingFunc.show(this, this.getWindow(), tv);
        this.getWindow().getDecorView().setOnTouchListener(new View.OnTouchListener() {
            
            @Override
            public boolean onTouch(View arg0, MotionEvent event) {
                FloatingFunc.onTouchEvent(event, tv);
                return true;
            }
        });
        
        /*view.setOnTouchListener(new View.OnTouchListener() {
            
            @Override
            public boolean onTouch(View arg0, MotionEvent event) {
                FloatingFunc.onTouchEvent(event, tv);
                return true;
            }
        });
    */
    
    }
}
