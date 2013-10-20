package lib.func.wps;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import android.lib.R;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
public class CopyOfWifiPositionDemo extends Activity
{

    private WifiManager  wfm;
    private LinearLayout c;
    private final String TAG = CopyOfWifiPositionDemo.class.getSimpleName();
    private String posId;
    
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
                    URL url;
                    try
                    {
                        url = new URL("http://127.0.0.1/collect.php");
                        url.getFile();
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setDoInput(false);
                        conn.setDoOutput(true);
                        conn.setUseCaches(false);
                        conn.setRequestMethod("POST");
                        conn.connect();
                        DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                         
                        int i=0;
                        for (ScanResult result : results)
                        {
                            am.put(result.SSID, result.level);
                            String value = posId+"|"+result.BSSID+"|"+result.level;
                            Log.i(TAG, "pos"+i+":"+value);
                            conn.addRequestProperty("pos"+i, value);
                            conn.connect();
                            
                            String postContent = URLEncoder.encode("pos"+i, "UTF-8") + "=" + URLEncoder.encode(value, "UTF-8");
                            dos.write(postContent.getBytes());
                            dos.flush();
                            i++;
                        }
                        dos.close();
                        
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
                        
                        
                        
                    } catch (MalformedURLException e)
                    {
                        e.printStackTrace();
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                    

                    
                }
            }
        }

    }

}
