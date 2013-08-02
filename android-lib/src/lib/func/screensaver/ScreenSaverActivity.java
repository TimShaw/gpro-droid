package lib.func.screensaver;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;


/**
 * @ClassName:  ScreenSaverActivity.java
 * @Description: 
 * @Author JinChao
 * @Date 2013-7-31 下午2:12:55
 * @Copyright: 版权由 HundSun 拥有
 */
public class ScreenSaverActivity extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        /**
         * 屏幕保持高亮显示
         */
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  
                             WindowManager.LayoutParams.FLAG_FULLSCREEN);  
        
        TextView tv = new TextView(this);
        tv.setText("屏保文字。。。");
        
        setContentView(tv);
        
        
    }
    
}
