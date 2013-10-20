package lib.ui.splash;

import java.util.ArrayList;
import java.util.List;

import android.lib.R;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class AnimationSplashActivity extends Activity {
    
    private Animation mAnimation;
    private int marginTop;
    private List<ImageView> mImageViews;
    private LinearLayout mLinearLayout;
    private int[] imageId = {R.drawable.l, R.drawable.a, R.drawable.o, R.drawable.d, 
                             R.drawable.i, R.drawable.n, R.drawable.g};
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //设置没有标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        //获取屏幕的尺寸信息
        DisplayMetrics dm = this.getResources().getDisplayMetrics();
        marginTop = dm.heightPixels - 100;
        
        //载入动画，根据myanim.xml文件的初始化动画
        mAnimation = AnimationUtils.loadAnimation(this, R.anim.splash_letter_anim);
        
        mLinearLayout = new LinearLayout(this);
        mLinearLayout.setBackgroundResource(R.drawable.background);
        
        mImageViews = new ArrayList<ImageView>();
        
        //初始化布局（初始化图片）
        imageInit(mLinearLayout);
        
        //设置布局文件
        setContentView(mLinearLayout);
        
        //开始执行动画
        startAnimation();
            
    }
    
    
    private void imageInit(LinearLayout layout) {
        
        //设置LinearLayout中的空间的排列方式，非常重要
        layout.setGravity(Gravity.CENTER_HORIZONTAL);
        
        //定义两个布局的参数，之后应用到LinearLayout里面的控件上
        LinearLayout.LayoutParams mParams1 = new LinearLayout.LayoutParams(40, 40);
        mParams1.setMargins(30, marginTop, 0, 0);
        
        LinearLayout.LayoutParams mParams2 = new LinearLayout.LayoutParams(40, 40);
        mParams2.setMargins(0, marginTop, 0, 0);

        //将代表“Loading”的ImageView添加到Linearlayout中，但是没有初始化
        ImageView l = new ImageView(this);
        //应用LayoutParam
        l.setLayoutParams(mParams1);
        layout.addView(l);
        mImageViews.add(l);
        
        ImageView o = new ImageView(this);
        o.setLayoutParams(mParams2);
        layout.addView(o);
        mImageViews.add(o);
        
        ImageView a = new ImageView(this);
        a.setLayoutParams(mParams2);
        layout.addView(a);
        mImageViews.add(a);
        
        ImageView d = new ImageView(this);
        d.setLayoutParams(mParams2);
        layout.addView(d);
        mImageViews.add(d);
        
        ImageView i = new ImageView(this);
        i.setLayoutParams(mParams2);
        layout.addView(i);
        mImageViews.add(i);
        
        ImageView n = new ImageView(this);
        n.setLayoutParams(mParams2);
        layout.addView(n);
        mImageViews.add(n);
        
        ImageView g = new ImageView(this);
        g.setLayoutParams(mParams2);
        layout.addView(g);
        mImageViews.add(g);
    
    }
    
    
    private void imageClear() {
        for(ImageView image:mImageViews) {
            //将ImageView置为空
            image.setImageDrawable(null);
            //清除缓存
            image.destroyDrawingCache();        
        }
    }
    
    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            //super.handleMessage(msg);
            
            switch (msg.what) {
            case 0:
                //注意ImageView初始化的方法
                //注意获取Drawable对象的方法
                mImageViews.get(0).setImageDrawable(AnimationSplashActivity.this.getResources().getDrawable(R.drawable.l));
                mImageViews.get(0).setAnimation(mAnimation);
                //mImageViews.get(0).startAnimation(mAnimation);
                break;
            case 1:
                mImageViews.get(1).setImageDrawable(AnimationSplashActivity.this.getResources().getDrawable(R.drawable.o));
                mImageViews.get(1).setAnimation(mAnimation);
                break;
            case 2:
                mImageViews.get(2).setImageDrawable(AnimationSplashActivity.this.getResources().getDrawable(R.drawable.a));
                mImageViews.get(2).setAnimation(mAnimation);
                break;
            case 3:
                mImageViews.get(3).setImageDrawable(AnimationSplashActivity.this.getResources().getDrawable(R.drawable.d));
                mImageViews.get(3).setAnimation(mAnimation);
                break;
            case 4:
                mImageViews.get(4).setImageDrawable(AnimationSplashActivity.this.getResources().getDrawable(R.drawable.i));
                mImageViews.get(4).setAnimation(mAnimation);
                break;
            case 5:
                mImageViews.get(5).setImageDrawable(AnimationSplashActivity.this.getResources().getDrawable(R.drawable.n));
                mImageViews.get(5).setAnimation(mAnimation);
                break;
            case 6:
                mImageViews.get(6).setImageDrawable(AnimationSplashActivity.this.getResources().getDrawable(R.drawable.g));
                mImageViews.get(6).setAnimation(mAnimation);
                break;
                
            case 100:
                imageClear();
            default:
                break;
            }
        }
        
        
    };
    
    public void startAnimation() {
        new Thread() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                //super.run();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                
                int runCount = 0;
                
                while(true) {
                    if(runCount < 2) {
                        for(int i = 0; i < 7; i++) {
                            mHandler.sendEmptyMessage(i);
                            
                            try {
                                Thread.sleep(400);
                            } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            
                        }
                        runCount++;
                    } else {
                        mHandler.sendEmptyMessage(100);
                        runCount = 0;
                    }
                }
            }
            
        }.start();
    }
    
    
}