package lib.ui;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

/**
 * @ClassName: AnotherAcitivty.java
 * @Description:
 * @Author JinChao
 * @Date 2013-2-26 下午2:15:32
 * @Copyright: 版权由 HundSun 拥有
 */
public class QQShareActivity extends Activity
{

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_qq);
        WebView wv = (WebView)findViewById(R.id.webView);
        //wv.loadUrl("file:///android_asset/www/share_qq.html");
        
        wv.loadUrl("http://111.1.45.158/txlmain-manage/share_qq.html");
    }

}
