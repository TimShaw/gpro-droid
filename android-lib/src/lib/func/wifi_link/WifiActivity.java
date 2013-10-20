package lib.func.wifi_link;

import java.text.SimpleDateFormat;
import java.util.List;

import lib.LibConstants;
import lib.util.LogUtil;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.lib.R;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiConfiguration.AuthAlgorithm;
import android.net.wifi.WifiConfiguration.KeyMgmt;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class WifiActivity extends Activity {
	private final String AP_SSID = "HF-LPB";
	private final String DOUBLE_QUOTE_AP_SSID = "\""+AP_SSID+"\"";

	private WifiActivity me = this;
	private String TAG = WifiActivity.class.getSimpleName();
	private ListView mSsidListView = null;
	private WifiManager mWifiManager;
	private TextView mWifiStatus;
	private MyBroadcastReceiver mReceiver ;
	private String mApIp = "10.10.100.254";
	private UdpUnicast mConfigUdp;
	private Button mSelectRouter;
	
	private WifiAdmin mWifiAdmin;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.wifi_link_main);
		
		mWifiAdmin = new WifiAdmin(me);

		mSsidListView = (ListView) findViewById(R.id.ssidListView);
		mWifiManager = (WifiManager) this
				.getSystemService(Context.WIFI_SERVICE);
		mWifiStatus = (TextView) findViewById(R.id.wifiStatus);
		mReceiver = new MyBroadcastReceiver();
		this.registerReceiver(mReceiver, new IntentFilter(
				WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
		this.registerReceiver(mReceiver, new IntentFilter(
				WifiManager.RSSI_CHANGED_ACTION));

		mWifiAdmin.openWifi();
		String ssid = mWifiAdmin.getSSID();
		
		EditText ssidEt = (EditText)findViewById(R.id.ssid);
		ssidEt.setText(ssid);
		String ssidPwd = AppSharePref.getString(LibConstants.SSID_PREFIX+ssid, "");
		EditText passwordEt = (EditText)findViewById(R.id.password);
		passwordEt.setText(ssidPwd);
		
		
		Button connectBtn = (Button)findViewById(R.id.connect);
		connectBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(ssidEt.getText().toString().length()==0){
				}
			}
		});
		//scanWifi();
		
		
		mConfigUdp = new UdpUnicast();
		//unicast.setIp("10.10.100.254");
		mConfigUdp.setIp("10.10.100.254");
		mConfigUdp.setPort(48899);
		mConfigUdp.setListener(new UdpUnicastListener() {
			@Override
			public void onReceived(byte[] data, int length) {
				String str = new String(data);
				if(str.split(",").length==3){
					mConfigUdp.send("+ok");
				}
			}
		});
		mConfigUdp.open();
		
		mSelectRouter = (Button)findViewById(R.id.selectRouter);
		mSelectRouter.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mConfigUdp.send("AT+WSCAN\r");
			}
		});
		
		Button closeWifi = (Button)findViewById(R.id.closeWifi);
		
		Button openWifi = (Button)findViewById(R.id.openWifi);
		
		closeWifi.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mWifiAdmin.closeWifi();
			}
		});
		
		openWifi.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mWifiAdmin.openWifi();
			}
		});
	}
	
 

	
	@Override
	protected void onStop() {
		super.onStop();
		unregisterReceiver(mReceiver);
		Log.i(TAG, "...onStop...");
	}


	public class MyBroadcastReceiver extends BroadcastReceiver {

		private SimpleDateFormat sdf = new SimpleDateFormat("MM/dd HH:mm:ss");

		@Override
        public void onReceive(Context context, Intent intent)
        {
            if (intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION))
            {
                List<ScanResult> results = mWifiManager.getScanResults();
                if (results != null)
                {
                    for (ScanResult result : results)
                    {
                        int signalLevel = WifiManager.calculateSignalLevel(result.level, 20);
                        TextView tv = new TextView(context);
                        String str = "ssid: " + result.SSID + ",bssid:" + result.BSSID + ",level:" + result.level
                                     + ",frequency:" + result.frequency + ",signalLevel:" + signalLevel;
                        tv.setText(str);
                        Log.i(TAG, str);
                        
                        if(result.SSID.equals(AP_SSID)){
                        	mWifiStatus.setText("找到"+AP_SSID);
                        	AccessPoint accessPointWithResult = new AccessPoint(me, result);
                        	WifiConfiguration conf = generateWifiConfiguration(accessPointWithResult,null);
                        	mWifiManager.addNetwork(conf);
                        	
                        	 List<WifiConfiguration> configs = mWifiManager.getConfiguredNetworks();
                        	 if(configs!=null){
                        		 for (WifiConfiguration config : configs) {
                        			 LogUtil.i(me, "networkId:"+config.networkId+", ssid:"+config.SSID);
                        			 if(DOUBLE_QUOTE_AP_SSID.equals(config.SSID)){
                        				// stopScanWifi();
                        				 AccessPoint accessPoint = new AccessPoint(me, config);
                        				 WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
                        				 if(!DOUBLE_QUOTE_AP_SSID.equals(wifiInfo.getSSID())){
                        					 mWifiManager.disconnect();
                        				 }
                        				 boolean enableStauts = mWifiManager.enableNetwork(accessPoint.getNetworkId(), true);
                                    	 Log.i(TAG, "enableStauts:"+enableStauts);
                        				 boolean connectStatus = mWifiManager.reconnect();
                        				 Log.i(TAG, "connectStatus:"+connectStatus);
                        				 if(connectStatus){
                        					 mConfigUdp.send("AT+Q\r");
                        					 mConfigUdp.send("HF-A11ASSISTHREAD");
                        				 }
                        				 
                                    	 break;
                        			 }
                        		 }
                        	 }
                        	 
                        	 break;
                        }
                    }

                }
            }
        }
	}

	public WifiConfiguration generateWifiConfiguration(AccessPoint accessPoint,
			String password) {
		WifiConfiguration config = new WifiConfiguration();
		config.SSID = "\"" + accessPoint.getSsid() + "\"";
		switch (accessPoint.getSecurity()) {
			case AccessPoint.SECURITY_NONE:
				config.allowedKeyManagement.set(KeyMgmt.NONE);
				return config;
	
			case AccessPoint.SECURITY_WEP:
				config.allowedKeyManagement.set(KeyMgmt.NONE);
				config.allowedAuthAlgorithms.set(AuthAlgorithm.OPEN);
				config.allowedAuthAlgorithms.set(AuthAlgorithm.SHARED);
				if (password.length() != 0) {
					int length = password.length();
					String _password = password;// .getText().toString();
					// WEP-40, WEP-104, and 256-bit WEP (WEP-232?)
					if ((length == 10 || length == 26 || length == 58)
							&& _password.matches("[0-9A-Fa-f]*")) {
						config.wepKeys[0] = _password;
					} else {
						config.wepKeys[0] = '"' + _password + '"';
					}
				}
				return config;
	
			case AccessPoint.SECURITY_PSK:
				config.allowedKeyManagement.set(KeyMgmt.WPA_PSK);
				if (password.length() != 0) {
					if (password.matches("[0-9A-Fa-f]{64}")) {
						config.preSharedKey = password;
					} else {
						config.preSharedKey = '"' + password + '"';
					}
				}
				return config;
	
			case AccessPoint.SECURITY_EAP:
	
				return config;
		}
		return null;
	}
}
