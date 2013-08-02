package lib.func.screensaver;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;


/**
 * @ClassName:  ScreenSaverService.java
 * @Description: 
 * @Author JinChao
 * @Date 2013-7-31 下午2:48:39
 * @Copyright: 版权由 HundSun 拥有
 */
public class ScreenSaverService extends Service
{
    private String TAG = ScreenSaverService.class.getSimpleName();
    private ScreenSaverBroadcast ssb;
    @Override
    public IBinder onBind(Intent intent)
    {
        Log.i(TAG,"onBind....");
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.i(TAG,"onStartCommand....");
        /**
         * 由于 android.intent.action.SCREEN_OFF是系统级广播，
         * 因此需要在代码中注册才可以捕获由android系统发出android.intent.action.SCREEN_OFF广播。
         */
        ssb = new ScreenSaverBroadcast();
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        this.registerReceiver(ssb, filter);
        
        return START_NOT_STICKY;
    }
    @Override
    public void onCreate()
    {
        Log.i(TAG,"onCreate....");
        super.onCreate();
    }
    @Override
    public void onStart(Intent intent, int startId)
    {
        Log.i(TAG,"onStart....");
        super.onStart(intent, startId);
    }
    @Override
    public void onDestroy()
    {
        Log.i(TAG,"onDestroy....");
        super.onDestroy();
        this.unregisterReceiver(ssb);
    }
    
    
}
