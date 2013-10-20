package lib.ui.location;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import android.lib.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * @ClassName: LocationActivity.java
 * @Description:
 * @Author JinChao
 * @Date 2013-5-27 下午3:17:42
 * @Copyright: 版权由 HundSun 拥有
 */
public class CellActivity extends Activity
{

    private String TAG = CellActivity.class.getSimpleName();

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cell);

        /** 为按钮绑定事件 */
        Button btnGetLocation = (Button) findViewById(R.id.button1);
        btnGetLocation.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View arg0)
            {
                onBtnClick();
            }
        });

    }

    /** 基站信息结构体 */
    public class SCell
    {

        public int MCC;
        public int MNC;
        public int LAC;
        public int CID;
    }

    /** 经纬度信息结构体 */
    public class SItude
    {

        public String latitude;
        public String longitude;
    }

    /** 按钮点击回调函数 */
    private void onBtnClick()
    {
        /** 弹出一个等待状态的框 */
        ProgressDialog mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("正在获取中...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.show();

        try
        {
            /** 获取基站数据 */
            SCell cell = getCellInfo();

            /** 根据基站数据获取经纬度 */
            SItude itude = getItude(cell);

            /** 获取地理位置 */
            String location = getLocation(itude);

            /** 显示结果 */
            showResult(cell, location);

            /** 关闭对话框 */
            mProgressDialog.dismiss();
        } catch (Exception e)
        {
            /** 关闭对话框 */
            mProgressDialog.dismiss();
            /** 显示错误 */
            TextView cellText = (TextView) findViewById(R.id.cellText);
            cellText.setText(e.getMessage());
        }
    }

    /** 获取基站信息 
     * @throws Exception */
    private SCell getCellInfo() throws Exception{
        SCell cell = new SCell();
        /** 调用API获取基站信息 */
        TelephonyManager mTelNet = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        GsmCellLocation location = (GsmCellLocation) mTelNet.getCellLocation();
        if (location == null)
            throw new Exception("获取基站信息失败");
        String operator = mTelNet.getNetworkOperator();
        Log.i(TAG, "operator:"+operator);
        int mcc = Integer.parseInt(operator.substring(0, 3));
        int mnc = Integer.parseInt(operator.substring(3));
        int cid = location.getCid();
        int lac = location.getLac();
        /** 将获得的数据放到结构体中 */
        cell.MCC = mcc;
        cell.MNC = mnc;
        cell.LAC = lac;
        cell.CID = cid;
        Log.i(TAG,"mcc:"+mcc+",mnc:"+mnc+",lac:"+lac+",cid:"+cid);
        return cell;
    }

    /** 获取经纬度 */
    private SItude getItude(SCell cell) throws Exception
    {
        SItude itude = new SItude();
        
        /** 采用Android默认的HttpClient */
        HttpClient client = new DefaultHttpClient();
        /** 采用POST方法 */
        HttpPost post = new HttpPost("http://www.google.com/loc/json");
        try {
            /** 构造POST的JSON数据 */
            JSONObject holder = new JSONObject();
            holder.put("version", "1.1.0");
            holder.put("host", "maps.google.com");
            holder.put("address_language", "zh_CN");
            holder.put("request_address", true);
            holder.put("radio_type", "gsm");
            holder.put("carrier", "HTC");
     
            JSONObject tower = new JSONObject();
            tower.put("mobile_country_code", cell.MCC);
            tower.put("mobile_network_code", cell.MNC);
            tower.put("cell_id", cell.CID);
            tower.put("location_area_code", cell.LAC);
     
            JSONArray towerarray = new JSONArray();
            towerarray.put(tower);
            holder.put("cell_towers", towerarray);
     
            StringEntity query = new StringEntity(holder.toString());
            post.setEntity(query);
     
            /** 发出POST数据并获取返回数据 */
            HttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            BufferedReader buffReader = new BufferedReader(new InputStreamReader(entity.getContent()));
            StringBuffer strBuff = new StringBuffer();
            String result = null;
            while ((result = buffReader.readLine()) != null) {
                strBuff.append(result);
            }
     
            /** 解析返回的JSON数据获得经纬度 */
            JSONObject json = new JSONObject(strBuff.toString());
            JSONObject subjosn = new JSONObject(json.getString("location"));
     
            itude.latitude = subjosn.getString("latitude");
            itude.longitude = subjosn.getString("longitude");
             
            Log.i("Itude", itude.latitude + itude.longitude);
            Log.i(TAG, holder.toString()); 
        } catch (Exception e) {
            Log.e(e.getMessage(), e.toString());
            throw new Exception("获取经纬度出现错误:"+e.getMessage());
        } finally{
            post.abort();
            client = null;
        }
         
        return itude;
    }

    private SItude getItudeByMiniGps(SCell cell) throws Exception{
        SItude itude = new SItude();
        
        /** 采用Android默认的HttpClient */
        HttpClient client = new DefaultHttpClient();
        /** 采用POST方法 */
        HttpPost post = new HttpPost("http://www.minigps.net/l.do");
        //c=460&n=0&a=22548&e=18419
        /*返回：0|OK|baidu|30.192006|120.172151|浙江省杭州市滨江区园支二路;恒生电子股份有限公司、金轩烟酒、卓尔造型[附近] */
        try {
            /** 构造POST的JSON数据 */
            JSONObject tower = new JSONObject();
            tower.put("c", cell.MCC);
            tower.put("n", cell.MNC);
            tower.put("e", cell.CID);
            tower.put("a", cell.LAC);
     
            StringEntity query = new StringEntity(tower.toString());
            post.setEntity(query);
     
            /** 发出POST数据并获取返回数据 */
            HttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            BufferedReader buffReader = new BufferedReader(new InputStreamReader(entity.getContent()));
            StringBuffer strBuff = new StringBuffer();
            String result = null;
            while ((result = buffReader.readLine()) != null) {
                strBuff.append(result);
            }
            String[] parts = strBuff.toString().split("|");
            for(String part:parts){
                //part
            }
      
            Log.i("Itude", itude.latitude + itude.longitude);
        } catch (Exception e) {
            Log.e(e.getMessage(), e.toString());
            throw new Exception("获取经纬度出现错误:"+e.getMessage());
        } finally{
            post.abort();
            client = null;
        }
         
        return itude;
    }
    /** 获取地理位置 */
    private String getLocation(SItude itude) throws Exception
    {
        String resultString = "";
        
        /** 这里采用get方法，直接将参数加到URL上 */
        String urlString = String.format("http://maps.google.cn/maps/geo?key=abcdefg&q=%s,%s", itude.latitude, itude.longitude);
        Log.i("URL", urlString);
     
        /** 新建HttpClient */
        HttpClient client = new DefaultHttpClient();
        /** 采用GET方法 */
        HttpGet get = new HttpGet(urlString);
        try {
            /** 发起GET请求并获得返回数据 */
            HttpResponse response = client.execute(get);
            HttpEntity entity = response.getEntity();
            BufferedReader buffReader = new BufferedReader(new InputStreamReader(entity.getContent()));
            StringBuffer strBuff = new StringBuffer();
            String result = null;
            while ((result = buffReader.readLine()) != null) {
                strBuff.append(result);
            }
            resultString = strBuff.toString();
     
            /** 解析JSON数据，获得物理地址 */
            if (resultString != null && resultString.length() > 0) {
                JSONObject jsonobject = new JSONObject(resultString);
                JSONArray jsonArray = new JSONArray(jsonobject.get("Placemark").toString());
                resultString = "";
                for (int i = 0; i < jsonArray.length(); i++) {
                    resultString = jsonArray.getJSONObject(i).getString("address");
                }
            }
        } catch (Exception e) {
            throw new Exception("获取物理位置出现错误:" + e.getMessage());
        } finally {
            get.abort();
            client = null;
        }
     
        return resultString;
    }

    /** 显示结果 */
    private void showResult(SCell cell, String location)
    {
        TextView cellText = (TextView) findViewById(R.id.cellText);
        cellText.setText(String.format("基站信息：mcc:%d, mnc:%d, lac:%d, cid:%d",
                cell.MCC, cell.MNC, cell.LAC, cell.CID));
     
        TextView locationText = (TextView) findViewById(R.id.lacationText);
        locationText.setText("物理位置：" + location);
    }
}
