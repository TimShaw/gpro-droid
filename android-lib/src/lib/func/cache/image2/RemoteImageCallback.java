package lib.func.cache.image2;

import android.graphics.Bitmap;

/**
 * @ClassName:  RemoteImageCallback.java
 * @Description: 
 * @Author JinChao
 * @Date 2013-8-1 上午11:01:48
 * @Copyright: 版权由 HundSun 拥有
 */
public interface RemoteImageCallback
{
    public void onComplete(String url, Bitmap bitmap) ;
}
