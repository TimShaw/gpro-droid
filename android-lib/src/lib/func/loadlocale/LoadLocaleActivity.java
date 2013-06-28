package lib.func.loadlocale;

import lib.ui.R;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;


/**
 * @ClassName:  LoadLocaleActivity.java
 * @Description: 
 * @Author JinChao
 * @Date 2013-6-28 上午11:42:03
 * @Copyright: 版权由 HundSun 拥有
 */
public class LoadLocaleActivity extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.load_locale_res);
        
        WebView wv = (WebView)findViewById(R.id.webView);
        this.settingWebView(wv, this);
        wv.setWebChromeClient(new LibWebChromeClient());
        wv.loadUrl("http://192.168.92.61:8080/android-lib/assets/www/load_from_remote.html");
    }
    public void settingWebView(WebView wv,Activity ctx){
        WebSettings settings = wv.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setLayoutAlgorithm(LayoutAlgorithm.NORMAL);
        // Set the nav dump for HTC
        settings.setNavDump(true);
        // Enable database
        settings.setDatabaseEnabled(true);
        String databasePath = ctx.getDir("database", Context.MODE_PRIVATE).getPath();
        settings.setDatabasePath(databasePath);
        // Enable DOM storage
        settings.setDomStorageEnabled(true);
        // Enable built-in geolocation
        settings.setGeolocationEnabled(true);
        settings.setBuiltInZoomControls(true);
    }
}
