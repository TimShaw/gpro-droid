package lib.func.net;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Proxy;
import android.os.Bundle;
import android.util.Log;

/**
 * @ClassName: ProxyConnectActivity.java
 * @Description:
 * @Author JinChao
 * @Date 2013-7-30 下午4:58:58
 * @Copyright: 版权由 HundSun 拥有
 */
public class ProxyConnectActivity extends Activity
{

    private String TAG = ProxyConnectActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        DefaultHttpClient httpClient = new DefaultHttpClient();
        // 网络检测
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null)
        {
            NetworkInfo ni = cm.getActiveNetworkInfo();
            if (ni != null)
            {
                if (!ni.getTypeName().equals("WIFI"))
                {
                    // 设置代理
                    String host = Proxy.getDefaultHost();
                    int port = Proxy.getPort(this);
                    Log.i(TAG, "host: "+host+" port: "+port);
                    /* 若为wap，需要添加代理。 net则不需要。 */
                    if(host!=null){
                        HttpHost httpHost = new HttpHost(host, port);
                        httpClient.getParams().setParameter(ConnRouteParams.DEFAULT_PROXY, httpHost);
                        HttpPost post = new HttpPost("http://www.baidu.com/");
                        try
                        {
                            HttpResponse response = httpClient.execute(post);
                            if(response.getStatusLine().getStatusCode()==200){
                                HttpEntity entity = response.getEntity();
                                String body = EntityUtils.toString(entity, "UTF-8");  
                                Log.i(TAG,body);
                            }
                        } catch (ClientProtocolException e)
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

}
