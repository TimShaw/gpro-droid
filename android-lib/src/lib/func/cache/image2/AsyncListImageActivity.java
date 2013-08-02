package lib.func.cache.image2;

import java.io.File;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.ConcurrentHashMap;

import lib.Tool;
import lib.ui.R;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;

@SuppressWarnings("serial")
public class AsyncListImageActivity extends Activity implements RemoteImageCallback
{

    private ListView                                                      list;
    private static final String                                           TAG                 = AsyncListImageActivity.class.getSimpleName();
    private static final int                                              HARD_CACHE_CAPACITY = 10;

    private final HashMap<String, Bitmap>                                 mHardBitmapCache    = new LinkedHashMap<String, Bitmap>(
                                                                                                                                  HARD_CACHE_CAPACITY / 2,
                                                                                                                                  0.75f,
                                                                                                                                  true)
                                                                                              {

                                                                                                  @Override
                                                                                                  protected boolean removeEldestEntry(LinkedHashMap.Entry<String, Bitmap> eldest)
                                                                                                  {
                                                                                                      if (size() > HARD_CACHE_CAPACITY)
                                                                                                      {
                                                                                                          // 当map的size大于10时，把最近不常用的key放到mSoftBitmapCache中，从而保证mHardBitmapCache的效率
                                                                                                          mSoftBitmapCache.put(eldest.getKey(),
                                                                                                                               new SoftReference<Bitmap>(
                                                                                                                                                         eldest.getValue()));
                                                                                                          return true;
                                                                                                      } else return false;
                                                                                                  }
                                                                                              };

    /**
     * 当mHardBitmapCache的key大于10的时候，会根据LRU算法把最近没有被使用的key放入到这个缓存中。 Bitmap使用了SoftReference，当内存空间不足时，此cache中的bitmap会被垃圾回收掉
     */
    private final static ConcurrentHashMap<String, SoftReference<Bitmap>> mSoftBitmapCache    = new ConcurrentHashMap<String, SoftReference<Bitmap>>(
                                                                                                                                                     HARD_CACHE_CAPACITY / 2);

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cache_images_layout);
        list = (ListView) findViewById(R.id.list);

        initCacheDir();

        MyListAdapter adapter = new MyListAdapter();
        list.setAdapter(adapter);
    }

    private void initCacheDir()
    {
        //String cacheDir = "/data/data/com.devdiv.android.asynimagelist/files/caches";
        String cacheDir = Tool.getDataDir(this)+"/files/caches";
        Log.i(TAG, "cacheDir : "+cacheDir);
        File f = new File(cacheDir);
        if (!f.exists())
        {
            f.mkdirs();
        }
    }

    private class MyListAdapter extends BaseAdapter
    {

        private String[] urls = new String[]
                              {
                                  "http://d.hiphotos.baidu.com/album/w%3D230/sign=3f22ed427acb0a4685228c3a5b62f63e/96dda144ad3459828166ea2a0df431adcaef849d.jpg",
                                  "http://e.hiphotos.baidu.com/album/w%3D230/sign=b530b09b622762d0803ea3bc90ed0849/359b033b5bb5c9ea09120c5bd439b6003bf3b3a0.jpg",
                                  "http://b.hiphotos.baidu.com/album/w%3D230/sign=c5721455a2cc7cd9fa2d33da09002104/0823dd54564e9258b8d489569d82d158cdbf4ea1.jpg",
                                  "http://d.hiphotos.baidu.com/album/w%3D230/sign=99d3e3bbc995d143da76e32043f18296/8601a18b87d6277f455b3d8029381f30e924fc39.jpg",
                                  "http://e.hiphotos.baidu.com/album/w%3D230/sign=b530b09b622762d0803ea3bc90ed0849/359b033b5bb5c9ea09120c5bd439b6003bf3b3a0.jpg",
                                  "http://e.hiphotos.baidu.com/album/w%3D230/sign=959f30ddb219ebc4c078719ab227cf79/0bd162d9f2d3572cfd7193648b13632763d0c3f3.jpg",
                                  "http://g.hiphotos.baidu.com/album/w%3D230/sign=4fd27018f703738dde4a0b21831ab073/908fa0ec08fa513dd04463663c6d55fbb3fbd998.jpg",
                                  "http://c.hiphotos.baidu.com/album/w%3D230/sign=a34c4615b219ebc4c078719ab227cf79/0bd162d9f2d3572ccba2e5ac8b13632762d0c37a.jpg",
                                  "http://c.hiphotos.baidu.com/album/w%3D230/sign=174a6b5280cb39dbc1c06055e01709a7/8b82b9014a90f603b90393ff3812b31bb151edd3.jpg",
                                  "http://d.hiphotos.baidu.com/album/w%3D230/sign=43fa42af08f79052ef1f403d3cf2d738/9213b07eca8065386f8995af96dda144ad34822e.jpg",
                                  "http://h.hiphotos.baidu.com/album/w%3D230/sign=ceda378bd043ad4ba62e41c3b2035a89/a08b87d6277f9e2f10e1d7261e30e924b999f3cc.jpg",
                                  "http://b.hiphotos.baidu.com/album/w%3D230/sign=6a808d75fd039245a1b5e60cb795a4a8/024f78f0f736afc3d07f44ddb219ebc4b7451212.jpg",
                                  "http://g.hiphotos.baidu.com/album/w%3D230/sign=4f957018f703738dde4a0b21831ab073/908fa0ec08fa513dd00363663c6d55fbb3fbd9d9.jpg",
                                  "http://h.hiphotos.baidu.com/album/w%3D230/sign=e6b9ad82314e251fe2f7e3fb9787c9c2/aa18972bd40735fa414041019f510fb30e2408d1.jpg",
                                  "http://c.hiphotos.baidu.com/album/w%3D230/sign=d9e65e6b37d12f2ece05a9637fc3d5ff/6d81800a19d8bc3e133f3976838ba61ea8d34552.jpg",
                                  "http://a.hiphotos.baidu.com/album/w%3D230/sign=6a34d3d8342ac65c67056170cbf3b21d/e4dde71190ef76c6f75cf4939c16fdfaae5167ff.jpg",
                                  "http://f.hiphotos.baidu.com/album/w%3D230/sign=7a4dc3087e3e6709be0042fc0bc69fb8/9f510fb30f2442a704f8c28ad043ad4bd113022e.jpg",
                                  "http://h.hiphotos.baidu.com/album/w%3D230/sign=28b2c59eb58f8c54e3d3c22c0a282dee/c8177f3e6709c93df72c0d879e3df8dcd00054e7.jpg",
                                  "http://f.hiphotos.baidu.com/album/w%3D230/sign=7a4dc3087e3e6709be0042fc0bc69fb8/9f510fb30f2442a704f8c28ad043ad4bd113022e.jpg",
                                  "http://g.hiphotos.baidu.com/album/w%3D230/sign=a1056c16c83d70cf4cfaad0ec8dcd1ba/0e2442a7d933c8950f78a654d01373f08202009e.jpg",
                                  "http://b.hiphotos.baidu.com/album/w%3D230/sign=6d2d485ed31b0ef46ce89f5dedc551a1/eac4b74543a9822606132b578b82b9014b90ebf4.jpg",
                                  "http://g.hiphotos.baidu.com/album/w%3D230/sign=12abb1d4a5c27d1ea5263cc72bd4adaf/0b55b319ebc4b745853db466cefc1e178a821510.jpg",
                                  "http://b.hiphotos.baidu.com/album/w%3D230/sign=5512da47cdbf6c81f7372beb8c3fb1d7/7c1ed21b0ef41bd564fed6db50da81cb39db3d6b.jpg",
                                  "http://b.hiphotos.baidu.com/album/w%3D230/sign=ae29b20dc9fcc3ceb4c0ce30a245d6b7/4afbfbedab64034f91c9fc29aec379310a551d9c.jpg",
                                  "http://d.hiphotos.baidu.com/album/w%3D230/sign=ae3cdddb50da81cb4ee684ce6266d0a4/1f178a82b9014a90f5517392a8773912b31beeb5.jpg",
                                  "http://a.hiphotos.baidu.com/album/w%3D230/sign=0359fd3b6a600c33f079d9cb2a4c5134/0df431adcbef7609230ca7b02fdda3cc7cd99e9d.jpg",
                                  "http://e.hiphotos.baidu.com/album/w%3D230/sign=a0d78b1518d8bc3ec60801c9b28ba6c8/1ad5ad6eddc451da4d9d32c4b7fd5266d01632b1.jpg",
                                  "http://d.hiphotos.baidu.com/album/w%3D230/sign=66ce852ae4dde711e7d244f597eecef4/a71ea8d3fd1f413424a63051241f95cad1c85e0f.jpg",
                                  "http://b.hiphotos.baidu.com/album/w%3D230/sign=872d6d46d009b3deebbfe36bfcbf6cd3/d788d43f8794a4c2ac7d2d050ff41bd5ad6e3946.jpg",
                                  "http://a.hiphotos.baidu.com/album/w%3D230/sign=d39532f414ce36d3a20484330af33a24/37d12f2eb9389b50c7e22e7c8435e5dde7116efb.jpg",
                                  "http://b.hiphotos.baidu.com/album/w%3D230/sign=583f62e554fbb2fb342b5f117f4b2043/e850352ac65c10385292a47ab3119313b17e899d.jpg",
                                  "http://a.hiphotos.baidu.com/album/w%3D230/sign=323eca97bf096b63811959533c328733/5882b2b7d0a20cf409d5f36777094b36adaf99c4.jpg",
                                  "http://c.hiphotos.baidu.com/album/w%3D230/sign=a001a0b1a50f4bfb8cd09957334f788f/1e30e924b899a9016e0b94181c950a7b0208f5e6.jpg",
                                  "http://d.hiphotos.baidu.com/album/w%3D230/sign=8577003adbb44aed594eb9e7831d876a/42166d224f4a20a4454c3fb791529822720ed06b.jpg",
                                  "http://c.hiphotos.baidu.com/album/w%3D230/sign=68700dc7e850352ab161220b6342fb1a/8435e5dde71190ef244590afcf1b9d16fdfa6007.jpg",
                                  "http://a.hiphotos.baidu.com/album/w%3D230/sign=9aa1f9874afbfbeddc59317c48f1f78e/e824b899a9014c08ae08401d0b7b02087bf4f453.jpg",
                                  "http://b.hiphotos.baidu.com/album/w%3D230/sign=e0264d176f061d957d46303b4bf40a5d/6a600c338744ebf8536b5010d8f9d72a6059a782.jpg",
                                  "http://f.hiphotos.baidu.com/album/w%3D230/sign=d4aeb386a8014c08193b2fa63a7a025b/6a63f6246b600c33bacee0e71b4c510fd9f9a16d.jpg",
                                  "http://e.hiphotos.baidu.com/album/w%3D230/sign=0009e9d0cdbf6c81f7372beb8c3fb1d7/7c1ed21b0ef41bd531e5e54c50da81cb38db3dd7.jpg",
                                  "http://h.hiphotos.baidu.com/album/w%3D230/sign=8c46474ff91986184147e8877aed2e69/3c6d55fbb2fb4316527fb85421a4462309f7d3b8.jpg",
                                  "http://e.hiphotos.baidu.com/album/w%3D230/sign=b90cc643113853438ccf8022a313b01f/91ef76c6a7efce1b1130fde5ae51f3deb48f6581.jpg",
                                  "http://f.hiphotos.baidu.com/album/w%3D230/sign=6e6d2958f603918fd7d13ac9613c264b/5ab5c9ea15ce36d3d873fd883bf33a87e950b116.jpg",
                                      "http://www.icosky.com/icon/thumbnails/System/Sleek%20XP%20Basic/Add%20Icon.jpg",
                                      "http://www.icosky.com/icon/thumbnails/System/Sleek%20XP%20Basic/Adobe%20Illustator%20Icon.jpg",
                                      "http://www.icosky.com/icon/thumbnails/System/Sleek%20XP%20Basic/Attach%20Icon.jpg",
                                      "http://www.icosky.com/icon/thumbnails/System/Sleek%20XP%20Basic/Applications%20Cascade%20Icon.jpg",
                                      "http://www.icosky.com/icon/thumbnails/System/Sleek%20XP%20Basic/Administrator%20Icon.jpg",
                                      "http://www.icosky.com/icon/thumbnails/System/Sleek%20XP%20Basic/Clients%20Icon.jpg",
                                      "http://www.icosky.com/icon/thumbnails/System/Sleek%20XP%20Basic/Coinstack%20Icon.jpg",
                                      "http://www.icosky.com/icon/thumbnails/System/Sleek%20XP%20Basic/Download%20Icon.jpg",
                                      "http://www.icosky.com/icon/thumbnails/System/Sleek%20XP%20Basic/Help%20Icon.jpg",
                                      "http://www.icosky.com/icon/thumbnails/System/Sleek%20XP%20Basic/Home%20Icon.jpg",
                                      "http://www.icosky.com/icon/thumbnails/System/Sleek%20XP%20Basic/Pen%20Icon.jpg",
                                      "http://www.icosky.com/icon/thumbnails/System/Sleek%20XP%20Basic/Statistics%20Icon.jpg" };

        @Override
        public int getCount()
        {
            return urls.length;
        }

        @Override
        public String getItem(int position)
        {
            return urls[position];
        }

        @Override
        public long getItemId(int position)
        {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            if (convertView == null)
            {
                convertView = new ImageView(AsyncListImageActivity.this);
            }
            ImageView iv = (ImageView) convertView;
            iv.setLayoutParams(new ListView.LayoutParams(80,80));
            iv.setScaleType(ScaleType.FIT_START);
            Bitmap bitmap = getBitmapFromCache(getItem(position));
            if (bitmap == null)
            {
                iv.setImageResource(R.drawable.usa);
                iv.setTag(getItem(position));
                new ImageDownloaderTask(AsyncListImageActivity.this).execute(new String[]
                { getItem(position) });
            } else
            {
                iv.setImageBitmap(bitmap);
            }
            iv = null;
            return convertView;
        }

    }

    /**
     * 从缓存中获取图片
     */
    private Bitmap getBitmapFromCache(String url)
    {
        // 先从mHardBitmapCache缓存中获取
        synchronized (mHardBitmapCache)
        {
            final Bitmap bitmap = mHardBitmapCache.get(url);
            if (bitmap != null)
            {
                // 如果找到的话，把元素移到linkedhashmap的最前面，从而保证在LRU算法中是最后被删除
                mHardBitmapCache.remove(url);
                Log.d(TAG, "move bitmap to the head of linkedhashmap:" + url);
                mHardBitmapCache.put(url, bitmap);
                return bitmap;
            }
        }
        // 如果mHardBitmapCache中找不到，到mSoftBitmapCache中找
        SoftReference<Bitmap> bitmapReference = mSoftBitmapCache.get(url);
        if (bitmapReference != null)
        {
            final Bitmap bitmap = bitmapReference.get();
            if (bitmap != null)
            {
                Log.d(TAG, "get bitmap from mSoftBitmapCache with key:" + url);
                return bitmap;
            } else
            {
                mSoftBitmapCache.remove(url);
                Log.d(TAG, "remove bitmap with key:" + url);
            }
        }
        return null;
    }

    @Override
    public void onComplete(String url, Bitmap bitmap)
    {
        Log.d(TAG, "onComplete after got bitmap from remote with key:" + url);
        ImageView iv = (ImageView) list.findViewWithTag(url);
        if (iv != null)
        {
            iv.setImageBitmap(bitmap);
            mHardBitmapCache.put(url, bitmap);
        }
    }

}
