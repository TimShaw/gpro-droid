package lib.func.looper;

import lib.ui.R;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;


/**
 * @ClassName:  LooperActivity.java
 * @Description: 
 * @Author JinChao
 * @Date 2013-7-30 下午1:49:47
 * @Copyright: 版权由 HundSun 拥有
 */
public class LooperActivity extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.looper_layout);
        
    }
    
    
    class DownloadRunnable implements Runnable{
        
        @Override
        public void run()
        {
            
        }
        
    }
    
    
    class LooperRunnable implements Runnable{

        @Override
        public void run()
        {
            Looper.prepare();
            Handler handler = new Handler();
            
            Looper.loop();
        }
        
    }
}
