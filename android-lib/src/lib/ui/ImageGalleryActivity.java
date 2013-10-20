package lib.ui;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.lib.R;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.Gallery.LayoutParams;
import android.widget.ImageView;


/**
 * @ClassName:  AsyncLoadImageActivity.java
 * @Description: 
 * @Author JinChao
 * @Date 2013-6-17 上午10:14:24
 * @Copyright: 版权由 HundSun 拥有
 */
public class ImageGalleryActivity extends Activity
{
    private String[] imageURL = new String[]{
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
       "http://f.hiphotos.baidu.com/album/w%3D230/sign=6e6d2958f603918fd7d13ac9613c264b/5ab5c9ea15ce36d3d873fd883bf33a87e950b116.jpg"
    };
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_gallery);
        
        Gallery gallery = (Gallery)findViewById(R.id.gallery1);
        InternetGalleryAdapter galleryAdapter  = new InternetGalleryAdapter(this, imageURL);
        gallery.setAdapter(galleryAdapter);
        galleryAdapter.notifyDataSetChanged();
        
    }

    class InternetGalleryAdapter extends BaseAdapter {  
        private Context context;  
        private String[] imageURL;  
        // private int mGalleryItemBackground;  
        private Bitmap[] Bitmaps;  
      
        public InternetGalleryAdapter(Context c, String[] imageURL) {
            Log.d("lg", "InternetGalleryAdapter");  
      
            this.context = c;  
            this.imageURL = imageURL;  
            Bitmaps = new Bitmap[imageURL.length];  
            Resources res = context.getResources();  
            
            for (int i = 0; i < imageURL.length; i++) {  
               Bitmaps[i] = BitmapFactory.decodeResource(res,R.drawable.ic_launcher);  
            }
      
            // TypedArray a = c.obtainStyledAttributes(R.styleable.Gallery);  
            /* 取得Gallery属性的Index id */  
            // mGalleryItemBackground =  
            // a.getResourceId(R.styleable.Gallery_android_galleryItemBackground,  
            // 0);  
            // 让对象的styleable属性能够反复使用  
            // a.recycle();  
      
            PicLoadTask picLoadTask = new PicLoadTask();  
            picLoadTask.execute();  
      
        }  
      
        @Override  
        public int getCount() {  
            return imageURL.length;  
        }  
      
        @Override  
        public Object getItem(int position) {  
            return Bitmaps[position];  
        }  
      
        @Override  
        public long getItemId(int position) {  
            return position;  
        }  
      
        @Override  
        public View getView(int position, View convertView, ViewGroup parent) {  
            Log.d("lg", "getView");  
            ImageView imageView = new ImageView(context);  
            imageView.setImageBitmap(Bitmaps[position]);  
      
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);  
            imageView.setLayoutParams(new Gallery.LayoutParams(LayoutParams.WRAP_CONTENT,  
                    LayoutParams.WRAP_CONTENT));  
            imageView.setPadding(0, 0, 0, 0);  
            // imageView.setBackgroundResource(mGalleryItemBackground);  
            return imageView;  
        }  
      
        // 很费时的io操作,用异步线程处理  
        class PicLoadTask extends AsyncTask<String, Integer, String> {  
      
            // String... 可变长的输入参数，与AsyncTask.exucute()对应  
            protected String doInBackground(String... params) {  
                // 这里采用一次性全部记载的方法,适合少量图片  
                for (int i = 0; i < imageURL.length; i++) {  
                    try {  
                        // 从网络获取图片  
                        URL aryURI = new URL(imageURL[i]);  
                        URLConnection conn = aryURI.openConnection();  
                        conn.connect();  
                        InputStream is = conn.getInputStream();  
                        Bitmap bm = BitmapFactory.decodeStream(is);  
                        Bitmaps[i] = bm;  
                        cwjHandler.post(mUpdateResults); // 发布消息让主线程接收,实现异步线程和主线程的通信  
                        //notifyDataSetChanged(); //不能直接调用ui操作,这样不是线程安全的  
                        is.close();  
                        Thread.sleep(1000); // 模拟延时  
                    } catch (Exception e) {  
                        // 处理异常,图片加载失败  
                        Log.d("lg", e + "");  
                    }  
                }  
                return null;  
            }  
      
        }  
      
        final Handler cwjHandler = new Handler();  
      
        final Runnable mUpdateResults = new Runnable() {  
            public void run() {  
                notifyDataSetChanged(); // 不能直接在AsyncTask中调用,因为不是线程安全的  
            }  
        };  
    }  
}
