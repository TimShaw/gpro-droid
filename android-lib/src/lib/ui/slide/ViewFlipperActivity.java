package lib.ui.slide;

import lib.ui.R;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.widget.ViewFlipper;

/**
 * @ClassName: ViewFlipperActivity.java
 * @Description:
 * @Author JinChao
 * @Date 2013-7-23 上午11:03:30
 * @Copyright: 版权由 HundSun 拥有
 */
public class ViewFlipperActivity extends Activity
{

    ViewFlipper     myViewFlipper;
    GestureDetector myGestureDetector;
    private String TAG = ViewFlipperActivity.class.getSimpleName();

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_filpper_container);

        myViewFlipper = (ViewFlipper) findViewById(R.id.myviewflipper);

        myViewFlipper.addView(getLayoutView(R.layout.view_filpper_1));
        myViewFlipper.addView(getLayoutView(R.layout.view_filpper_2));
        myViewFlipper.addView(getLayoutView(R.layout.view_filpper_3));

        myGestureDetector = new GestureDetector(this, new GestureListener());

    }
    
    
    

    // 必须要重写这个方法！！
    // 否则无法捕获GestureDetector的onTouchEvent事件
    // 详细用法参照GestureDetector和GestureDetector.OnGestureListener的使用文档
    // 以及我的对文档的两张截图
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        // return super.onTouchEvent(event);
        return myGestureDetector.onTouchEvent(event);
    }

    public View getLayoutView(int layoutId)
    {
        LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mView = mInflater.inflate(layoutId, null);
        return mView;
    }
    
    
    
    class GestureListener implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener{

        /**
         * 用户（轻触触摸屏后）松开，由一个1个MotionEvent ACTION_UP触发  
         */
        @Override
        public boolean onSingleTapUp(MotionEvent e)
        {
            Log.d(TAG, "...onSingleTapUp...");
            return false;
        }

        /**
         * 用户轻触触摸屏，尚未松开或拖动，由一个1个MotionEvent ACTION_DOWN触发  
                                     注意和onDown()的区别，强调的是没有松开或者拖动的状态  
         */
        @Override
        public void onShowPress(MotionEvent e)
        {
            Log.d(TAG, "...onShowPress...");
        }

        /**
         * 用户按下触摸屏，并拖动，由1个MotionEvent ACTION_DOWN, 多个ACTION_MOVE触发  
         */
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)
        {
            Log.d(TAG, "...onScroll...");
            /*if((e1.getX() - e2.getX()) > 0)
            {
                myViewFlipper.scrollBy(10, 0);  
                return true;
            }
            else if ((e1.getX() - e2.getX()) < 0) {
                myViewFlipper.scrollBy(-10, 0);           
                return true;
            }
            else            
            {

            }*/
            return false;
        }

        /**
         * 用户长按触摸屏，由多个MotionEvent ACTION_DOWN触发
         */
        @Override
        public void onLongPress(MotionEvent e)
        {
            Log.d(TAG, "...onLongPress...");
        }

        // 注意每一个参数的含义，参考文档
        // Returns：true if the event is consumed, else false
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
        {
            // myViewFlipper.showNext();
            // Toast.makeText(UI_Test_ViewFlipperActivity.this, "onFling test", Toast.LENGTH_SHORT).show();

            Log.d(TAG, e1.getX() + "," + e2.getX()+", velocityX:"+velocityX+", velocityY:"+velocityY);
            if (e1.getX() - e2.getX() > 120)
            {
                myViewFlipper.setInAnimation(AnimationUtils.loadAnimation(ViewFlipperActivity.this,
                                                                          R.anim.push_left_in));
                myViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(ViewFlipperActivity.this,
                                                                           R.anim.push_left_out));
                myViewFlipper.showNext();
                return true;
            } else if (e1.getX() - e2.getX() < -120)
            {
                myViewFlipper.setInAnimation(AnimationUtils.loadAnimation(ViewFlipperActivity.this,
                                                                          R.anim.push_right_in));
                myViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(ViewFlipperActivity.this,
                                                                           R.anim.push_right_out));
                myViewFlipper.showPrevious();
                return true;
            }
            return false;
        }

        /**
         * 用户轻触触摸屏，由1个MotionEvent ACTION_DOWN触发
         */
        @Override
        public boolean onDown(MotionEvent e)
        {
            Log.d(TAG, "...onDown...");
            return false;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e)
        {
            if(myViewFlipper.isFlipping()){ //单击结束自动切换  
                myViewFlipper.stopFlipping();  
            }  
            return false;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e)
        {
            myViewFlipper.startFlipping(); //双击自动切换界面  
            return true;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e)
        {
            // TODO Auto-generated method stub
            return false;
        }
        
    }
}
