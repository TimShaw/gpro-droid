package lib.func.wifi_link;

import java.util.List;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiConfiguration.KeyMgmt;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;

public class WifiAdmin {
    // 定义WifiManager对象
    private WifiManager mWifiManager;
    // 定义WifiInfo对象
    private WifiInfo mWifiInfo;
    // 扫描出的网络连接列表
    private List<ScanResult> mWifiList;
    // 网络连接列表
    private List<WifiConfiguration> mWifiConfiguration;
    // 定义一个WifiLock
    WifiLock mWifiLock;
      
    public enum WIFICIPHER{
        NOPASS, WEP, WPA
    }
  
    // 构造器
    public WifiAdmin(Context context) {
        // 取得WifiManager对象
        mWifiManager = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        // 取得WifiInfo对象
        mWifiInfo = mWifiManager.getConnectionInfo();
    }
  
    // 打开WIFI
    public void openWifi() {
        if (!mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(true);
        }
    }
  
    // 关闭WIFI
    public void closeWifi() {
        if (mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(false);
        }
    }
  
    // 检查当前WIFI状态
    public int checkState() {
        return mWifiManager.getWifiState();
    }
  
    // 锁定WifiLock
    public void acquireWifiLock() {
        mWifiLock.acquire();
    }
  
    // 解锁WifiLock
    public void releaseWifiLock() {
        // 判断时候锁定
        if (mWifiLock.isHeld()) {
            mWifiLock.acquire();
        }
    }
  
    // 创建一个WifiLock
    public void creatWifiLock() {
        mWifiLock = mWifiManager.createWifiLock("Test");
    }
  
    // 得到配置好的网络
    public List<WifiConfiguration> getConfiguration() {
        return mWifiConfiguration;
    }
  
    // 指定配置好的网络进行连接
    public void connectConfiguration(int index) {
        // 索引大于配置好的网络索引返回
        if (index > mWifiConfiguration.size()) {
            return;
        }
        // 连接配置好的指定ID的网络
        mWifiManager.enableNetwork(mWifiConfiguration.get(index).networkId,
                true);
    }
  
    public void startScan() {
        mWifiManager.startScan();
        // 得到扫描结果
        mWifiList = mWifiManager.getScanResults();
        // 得到配置好的网络连接
        mWifiConfiguration = mWifiManager.getConfiguredNetworks();
    }
  
    // 得到网络列表
    public List<ScanResult> getWifiList() {   	
        return mWifiList;
    }
  
    public String[] getssidlist(){
    	startScan();
    	String[] s = new String[mWifiList.size()];
    	System.out.println(mWifiList.toString());
    	for(int i=0;i<mWifiList.size();i++)
    	{
    		s[i] = mWifiList.get(i).SSID;
    	}
    	return s;
    }
    // 查看扫描结果
    public StringBuilder lookUpScan() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < mWifiList.size(); i++) {
            stringBuilder
                    .append("Index_" + new Integer(i + 1).toString() + ":");
            // 将ScanResult信息转换成一个字符串包
            // 其中把包括：BSSID、SSID、capabilities、frequency、level
            stringBuilder.append((mWifiList.get(i)).toString());
            stringBuilder.append("/n");
        }
        return stringBuilder;
    }
  
    // 得到MAC地址
    public String getMacAddress() {
        return mWifiInfo.getMacAddress();
    }
  
    // 得到接入点的BSSID
    public String getBSSID() {
        return mWifiInfo.getBSSID();
    }
    public String getSSID(){
    	return mWifiInfo.getSSID();
    }
    // 得到IP地址
    public int getIPAddress() {
        return mWifiInfo.getIpAddress();
    }
  
    // 得到连接的ID
    public int getNetworkId() {
        return mWifiInfo.getNetworkId();
    }
  
    // 得到WifiInfo的所有信息包
    public WifiInfo getWifiInfo() {
        return mWifiInfo;
    }
  
    // 添加一个网络并连接
    public boolean addNetwork(WifiConfiguration wcg) {
    	System.out.println("______addNetwork_______");
        int wcgID = mWifiManager.addNetwork(wcg);
        disconnectWifi(wcgID);
        return mWifiManager.enableNetwork(wcgID, true);
    }
  
    // 断开指定ID的网络
    public void disconnectWifi(int netId) {
        mWifiManager.disableNetwork(netId);
        mWifiManager.disconnect();
    }
  
    // 然后是一个实际应用方法，只验证过没有密码的情况：
  
    public WifiConfiguration CreateWifiInfo(String SSID, String Password,
            int i) {
    	System.out.println("___________CreateWifiInfo_____________");
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = "\"" + SSID + "\"";
  
        WifiConfiguration tempConfig = this.IsExsits(SSID);
        if (tempConfig != null) {
            mWifiManager.removeNetwork(tempConfig.networkId);
        }
  
        if (i==1)// WIFICIPHER_NOPASS
        {
//            config.wepKeys[0] = "";
//            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
//            config.wepTxKeyIndex = 0;
       	
        	config.allowedKeyManagement.set(KeyMgmt.NONE);
        	config.status=WifiConfiguration.Status.ENABLED;
        	int netId=mWifiManager.addNetwork(config);
        }
        if (i==2) // WIFICIPHER_WEP
        {
            config.hiddenSSID = false;
              config.wepKeys[0] = "\"" + Password + "\"";
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
              
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.NONE);
            config.wepTxKeyIndex = 0;
            config.status = WifiConfiguration.Status.ENABLED;
        }
        if (i==3 ) // WIFICIPHER_WPA
        {
            config.preSharedKey = "\"" + Password + "\"";
            config.hiddenSSID = false;
            config.allowedAuthAlgorithms
                    .set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers
                    .set(WifiConfiguration.PairwiseCipher.TKIP);
            // config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedPairwiseCiphers
                    .set(WifiConfiguration.PairwiseCipher.CCMP);
            config.status = WifiConfiguration.Status.ENABLED;
        }
        return config;
    }
  
    private WifiConfiguration IsExsits(String SSID) {
        List<WifiConfiguration> existingConfigs = mWifiManager
                .getConfiguredNetworks();
        for (WifiConfiguration existingConfig : existingConfigs) {
            if (existingConfig.SSID.equals("\"" + SSID + "\"")) {
                return existingConfig;
            }
        }
        return null;
    }
    public int getcurrentsecrit(String ssid){
    	startScan();
    	for(int i = 0 ;i<mWifiList.size();i++)
    	{
    		System.out.println(mWifiList.get(i).SSID+mWifiList.get(i).capabilities);
    		if(mWifiList.get(i).SSID.equalsIgnoreCase(ssid))
    		{
    			if(mWifiList.get(i).capabilities.indexOf("WEP")>-1)
    			{
    				return 2;
    			}
    			else if(mWifiList.get(i).capabilities.indexOf("WPA")>-1)
    			{
    				return 3;
    			}
    			else 
    			{
    				return 1;
    			}
    		}    	
    	}
		return 0;
    }
}