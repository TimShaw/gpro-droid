package lib.func.wps;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

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
public class CopyOfWifiPositionDemo extends Activity
{

    private WifiManager  wfm;
    private LinearLayout c;
    private final String TAG = CopyOfWifiPositionDemo.class.getSimpleName();

    
    private Map<String,Integer> am = new HashMap<String,Integer>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.wifi_position);

        c = (LinearLayout) findViewById(R.id.container);

        MyBroadcastReceiver r = new MyBroadcastReceiver();
        this.registerReceiver(r, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        this.registerReceiver(r, new IntentFilter(WifiManager.RSSI_CHANGED_ACTION));
        wfm = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        if (!wfm.isWifiEnabled())
        {
            wfm.setWifiEnabled(true);
        }
        
        new Thread(new Runnable()
        {

            @Override
            public void run()
            {
                while (true)
                {
                    wfm.startScan();
                    
                    try
                    {
                        Thread.sleep(100);
                    } catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }).start(); 
        
    }

    public class MyBroadcastReceiver extends BroadcastReceiver
    {


        @Override
        public void onReceive(Context context, Intent intent)
        {
            if (intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION))
            {
                List<ScanResult> results = wfm.getScanResults();
                if (results != null)
                {
                    for (ScanResult result : results)
                    {
                        Integer level = am.get(result.SSID);
                        am.put(result.SSID, result.level);
                    }
                    c.removeAllViews();
                    Set<Entry<String, Integer>> es = am.entrySet();
                    Iterator<Entry<String, Integer>> it = es.iterator();
                    while(it.hasNext()){
                        Entry<String, Integer> entry = it.next();
                        String ssid = entry.getKey();
                        Integer level = entry.getValue();
                        String str="ssid: "+ssid+"  level:"+level;
                        TextView tv = new TextView(context);
                        tv.setText(str);
                        c.addView(tv,0);
                    }
                }
            }
        }

    }

}
