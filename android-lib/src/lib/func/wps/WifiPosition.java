package lib.func.wps;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import lib.ui.R;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * @ClassName:  WifiPosition.java
 * @Description: 
 * @Author JinChao
 * @Date 2013-7-9 下午3:09:39
 * @Copyright: 版权由 HundSun 拥有
 */
public class WifiPosition extends Activity
{
    
    private WifiManager wifiManager;
    private LinearLayout container;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.wifi_position);
        
        container = (LinearLayout) findViewById(R.id.container);
        
        MyBroadcastReceiver receiver = new MyBroadcastReceiver();
        this.registerReceiver(receiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        this.registerReceiver(receiver, new IntentFilter(WifiManager.RSSI_CHANGED_ACTION));
        wifiManager = (WifiManager)this.getSystemService(Context.WIFI_SERVICE);
        wifiManager.startScan();
        
        
        
    }
    
    public class MyBroadcastReceiver extends BroadcastReceiver{
        private SimpleDateFormat sdf = new SimpleDateFormat("MM/dd HH:mm:ss");
        @Override
        public void onReceive(Context context, Intent intent)
        {
            if(intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)){
                List<ScanResult> results =  wifiManager.getScanResults();
                if(results!=null){
                    for(ScanResult result: results){
                        int signalLevel = WifiManager.calculateSignalLevel(result.level, 20);
                        TextView tv = new TextView(context);
                        tv.setText("ssid: "+result.SSID+",bssid:"+result.BSSID+",level:"+result.level+",frequency:"+result.frequency+",signalLevel:"+signalLevel);
                        container.addView(tv, 0);
                    }
                    
                    TextView tv = new TextView(context);
                    tv.setText(sdf.format(new Date(System.currentTimeMillis()))+"------------");
                    container.addView(tv, 0);
                }
            }
        }
        
    }
    
}
