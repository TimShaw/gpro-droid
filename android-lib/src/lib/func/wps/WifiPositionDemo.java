package lib.func.wps;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.HashSet;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
public class WifiPositionDemo extends Activity
{

    private WifiManager  wifiManager;
    private LinearLayout container;
    private final String TAG = WifiPositionDemo.class.getSimpleName();
    
    private Map<String,ScanResult> apMap = new HashMap<String,ScanResult>();
    
    private Set<String> apSet = new HashSet<String>();
    
    private MyBroadcastReceiver receiver;
    
    private Button collectBtn;
    private Button collectStopBtn;
    private Integer count;
    private Integer posId=1;
    
    private Integer doCount=0;
    
    private boolean registed = false;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.wifi_position);
        
        collectBtn = (Button)findViewById(R.id.collectBtn);
        collectStopBtn = (Button)findViewById(R.id.collectStopBtn);
        
        //final EditText countET = (EditText)findViewById(R.id.count);
        final EditText posIdET = (EditText)findViewById(R.id.posId);
        
        
        collectBtn.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                /*String countStr = countET.getText().toString();
                count = countStr.length()>0?Integer.parseInt(countStr):10;*/
                String posIdStr = posIdET.getText().toString();
                posId = posIdStr.length()>0?Integer.parseInt(posIdStr):1;
                collectBtn.setEnabled(false);
                registReceiver();
                registed = true;
                /*if(count>0){
                    
                }*/
                new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        /*for(int i=1;i<count;i++)
                        {
                            
                        }*/
                        while(true){
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
        });
        collectStopBtn.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(registed){
                    collectBtn.setEnabled(true); 
                    unRegistReceiver();
                    registed = false;
                }
                
            }
        });
        
        container = (LinearLayout) findViewById(R.id.container);

        receiver = new MyBroadcastReceiver();
        
        wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled())
        {
            wifiManager.setWifiEnabled(true);
        }
        
    }
    
    private void registReceiver(){
        this.registerReceiver(receiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        this.registerReceiver(receiver, new IntentFilter(WifiManager.RSSI_CHANGED_ACTION));
    }
    
    private void unRegistReceiver(){
        this.unregisterReceiver(receiver);
    }

    public class MyBroadcastReceiver extends BroadcastReceiver
    {

        private SimpleDateFormat sdf = new SimpleDateFormat("MM/dd HH:mm:ss");

        @Override
        public void onReceive(Context context, Intent intent)
        {
            if (intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION))
            {
                //doCount++;
                final List<ScanResult> results = wifiManager.getScanResults();
                
                for(String ssid:apSet){
                   apMap.put(ssid, null);
                }
                
                
                if (results != null)
                {
                    new Thread(){
                        public void run(){
                            URL url;
                            try
                            {
                                url = new URL("http://192.168.92.61/collect.php");
                                url.getFile();
                                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                conn.setDoInput(true);
                                conn.setDoOutput(true);
                                conn.setUseCaches(false);
                                conn.setRequestMethod("POST");
                                conn.connect();
                                DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                                
                                int i=0;
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
                                    
                                    String value = posId+"|"+result.BSSID+"|"+result.level;
                                    Log.i(TAG, "pos"+i+":"+value);
                                    
                                    
                                    String postContent = URLEncoder.encode("pos"+i, "UTF-8") + "=" + URLEncoder.encode(value, "UTF-8");
                                    dos.write(postContent.getBytes());
                                    dos.flush();
                                    
                                    InputStream is = conn.getInputStream();
                                    int read = -1;
                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                    while ((read = is.read()) != -1) {
                                            baos.write(read);
                                    }
                                    byte[] data = baos.toByteArray();
                                    baos.close();

                                    String content = null;
                                    // 开始GET数据
                                    String encoding = conn.getContentEncoding();
                                    if (encoding != null) {
                                            content = new String(data, encoding);
                                    } else {
                                            content = new String(data);
                                    }
                                    Log.i(TAG, " response content: "+content);
                                    
                                    
                                    i++;
                                }
                                dos.close();
                            }catch (MalformedURLException e)
                            {
                                e.printStackTrace();
                            } catch (IOException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                    
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
                
                /*
                if(doCount >= count){
                   unRegistReceiver(); 
                }*/
            }
        }

    }

}
