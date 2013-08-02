package lib.func.system_service;

import lib.Tool;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;


/**
 * @Description: 
 * @Author JinChao
 * @Date 2013-7-31 上午9:24:41
 * @Copyright: 版权由 HundSun 拥有
 */
public class SystemServiceActivity extends Activity
{
    private SystemServiceActivity me = this;
    private OnetimeAlarmReceiver alarmReceiver ;
    
    private String ALARM_ACTION = "alarm.action";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        LinearLayout linear = new LinearLayout(me);
        linear.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
        linear.setOrientation(LinearLayout.VERTICAL);
        setContentView(linear);
        
        
        Button button = new Button(this);
        button.setText("添加闹钟");
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        //button.setLayoutParams(params);
        
        
        button.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(me, OnetimeAlarmReceiver.class);
                //intent.setAction(ALARM_ACTION);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(me, 0, intent, 0);
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                //alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (5 * 1000), pendingIntent);
                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (5 * 1000), 5*1000, pendingIntent);
                Toast.makeText( me, "Alarm set", Toast.LENGTH_LONG).show();
            }
        });
        
        linear.addView(button, params);
        
        Button vibrateButton = new Button(this);
        vibrateButton.setText("震动");
        vibrateButton.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                Tool.vibrate(me, 2000);
            }
        });
        linear.addView(vibrateButton, params);
        
        
        
        
        
        
        
        
        /*
                    闹钟服务 receiver只能用xml方式注册
        alarmReceiver = new OnetimeAlarmReceiver();
        IntentFilter filter = new IntentFilter(ALARM_ACTION);
        this.registerReceiver(alarmReceiver,filter);*/
    }
    
    

    @Override
    protected void onStop()
    {
        super.onStop();
        //me.unregisterReceiver(alarmReceiver);
    }
    
    
    
    
}
