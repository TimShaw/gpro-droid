package lib.func.wps;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.HashSet;
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
public class WifiPositionDemo extends Activity
{

    private WifiManager  wifiManager;
    private LinearLayout container;
    private final String TAG = WifiPositionDemo.class.getSimpleName();

    
    private Map<String,ScanResult> apMap = new HashMap<String,ScanResult>();
    
    private Set<String> apSet = new HashSet<String>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.wifi_position);

        container = (LinearLayout) findViewById(R.id.container);

        MyBroadcastReceiver receiver = new MyBroadcastReceiver();
        this.registerReceiver(receiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        this.registerReceiver(receiver, new IntentFilter(WifiManager.RSSI_CHANGED_ACTION));
        wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled())
        {
            wifiManager.setWifiEnabled(true);
        }
        
        new Thread(new Runnable()
        {

            @Override
            public void run()
            {
                while (true)
                {
                    wifiManager.startScan();
                    
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

        private SimpleDateFormat sdf = new SimpleDateFormat("MM/dd HH:mm:ss");

        @Override
        public void onReceive(Context context, Intent intent)
        {
            if (intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION))
            {
                List<ScanResult> results = wifiManager.getScanResults();
                
                for(String ssid:apSet){
                   apMap.put(ssid, null);
                }
                
                
                if (results != null)
                {
                    for (ScanResult result : results)
                    {
                        // if(result.SSID.contains("hundsun")){
                        /*if(level!=null){
                           if(level.intValue() == result.level){
                               
                           }
                        }*/
                        apMap.put(result.SSID, result);
                        apSet.add(result.SSID);
                        /*int signalLevel = WifiManager.calculateSignalLevel(result.level, 20);
                        TextView tv = new TextView(context);
                        String str = "ssid: " + result.SSID + ",bssid:" + result.BSSID + ",level:" + result.level
                                     + ",frequency:" + result.frequency + ",signalLevel:" + signalLevel;
                        tv.setText(str);
                        Log.i(TAG, str);*/
                        //container.addView(tv, 0);
                        // }
                    }
                    
                    
                    /*TextView tv = new TextView(context);
                    tv.setText(sdf.format(new Date(System.currentTimeMillis())) + "------------");
                    container.addView(tv, 0);*/
                }
                
                container.removeAllViews();
                Set<Entry<String, ScanResult>> es = apMap.entrySet();
                Iterator<Entry<String, ScanResult>> it = es.iterator();
                while(it.hasNext()){
                    Entry<String, ScanResult> entry = it.next();
                    String ssid = entry.getKey();
                    ScanResult result = entry.getValue();
                    int level = 0;
                    if(result!=null){
                        level = result.level;
                    }
                    String str="ssid: "+ssid+"  level:"+level;
                    TextView tv = new TextView(context);
                    tv.setText(str);
                    container.addView(tv,0);
                }
            }
        }

    }

}
