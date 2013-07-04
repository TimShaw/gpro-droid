package lib.func.loadlocale;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.HttpAuthHandler;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;


/**
 * @ClassName:  LibWebViewClient.java
 * @Description: 
 * @Author JinChao
 * @Date 2013-6-30 下午7:58:09
 * @Copyright: 版权由 HundSun 拥有
 */
public class LibWebViewClient extends WebViewClient
{
    private String TAG = LibWebViewClient.class.getSimpleName();
    
    
    private Map<String,String> urlCache  = new HashMap<String,String>();
    
    
    public LibWebViewClient()
    {
        urlCache.put("http://192.168.92.61:8080/www/js/test.js", "www/js/test.js");
    }
    
    @Override
    public void onLoadResource(WebView view, String url) {
        super.onLoadResource(view, url);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url)
    {
        // TODO Auto-generated method stub
        Log.i(TAG, "...shouldOverrideUrlLoading ....url: "+url);
        return super.shouldOverrideUrlLoading(view, url);
    }
    
    

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon)
    {
        // TODO Auto-generated method stub
        super.onPageStarted(view, url, favicon);
    }

    @Override
    public void onPageFinished(WebView view, String url)
    {
        // TODO Auto-generated method stub
        super.onPageFinished(view, url);
    }
    private WebResourceResponse getUtf8EncodedJsWebResourceResponse(InputStream data) {
        return new WebResourceResponse("text/javascript", "UTF-8", data);
    }
    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, String url)
    {
        // TODO Auto-generated method stub
        Log.i(TAG, "...shouldInterceptRequest ....url: "+url);
        
        try
        {
            String _url = urlCache.get(url);
            if(_url!=null){
                Log.i(TAG, "...getAssets open ....url: "+_url);
                return getUtf8EncodedJsWebResourceResponse(view.getContext().getAssets().open(_url));
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return super.shouldInterceptRequest(view, url);
    }

    @Override
    public void onTooManyRedirects(WebView view, Message cancelMsg, Message continueMsg)
    {
        // TODO Auto-generated method stub
        super.onTooManyRedirects(view, cancelMsg, continueMsg);
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl)
    {
        // TODO Auto-generated method stub
        super.onReceivedError(view, errorCode, description, failingUrl);
    }

    @Override
    public void onFormResubmission(WebView view, Message dontResend, Message resend)
    {
        // TODO Auto-generated method stub
        super.onFormResubmission(view, dontResend, resend);
    }

    @Override
    public void doUpdateVisitedHistory(WebView view, String url, boolean isReload)
    {
        // TODO Auto-generated method stub
        super.doUpdateVisitedHistory(view, url, isReload);
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error)
    {
        // TODO Auto-generated method stub
        super.onReceivedSslError(view, handler, error);
    }

    @Override
    public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm)
    {
        // TODO Auto-generated method stub
        super.onReceivedHttpAuthRequest(view, handler, host, realm);
    }

    @Override
    public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event)
    {
        // TODO Auto-generated method stub
        return super.shouldOverrideKeyEvent(view, event);
    }

    @Override
    public void onUnhandledKeyEvent(WebView view, KeyEvent event)
    {
        // TODO Auto-generated method stub
        super.onUnhandledKeyEvent(view, event);
    }

    @Override
    public void onScaleChanged(WebView view, float oldScale, float newScale)
    {
        // TODO Auto-generated method stub
        super.onScaleChanged(view, oldScale, newScale);
    }

}
