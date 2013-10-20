package lib.ui.drawer;

import android.lib.R;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.SlidingDrawer;
import android.widget.TextView;

public class SlidingDrawerDemo extends Activity
{

    private SlidingDrawer mDrawer;
    private ImageButton   imbg;
    private Boolean       flag = false;
    private TextView      tv;
    private String TAG = SlidingDrawerDemo.class.getSimpleName();

    /*
     * (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slidingdrawer);

        imbg = (ImageButton) findViewById(R.id.handle);
        mDrawer = (SlidingDrawer) findViewById(R.id.slidingdrawer);
        tv = (TextView) findViewById(R.id.tv);

        mDrawer.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener()
        {

            @Override
            public void onDrawerOpened()
            {
                flag = true;
                imbg.setImageResource(R.drawable.down_arrow);
            }

        });

        mDrawer.setOnDrawerCloseListener(new SlidingDrawer.OnDrawerCloseListener()
        {

            @Override
            public void onDrawerClosed()
            {
                flag = false;
                imbg.setImageResource(R.drawable.up_arrow);
            }

        });

        mDrawer.setOnDrawerScrollListener(new SlidingDrawer.OnDrawerScrollListener()
        {

            @Override
            public void onScrollEnded()
            {
                tv.setText("结束拖动");
                Log.i(TAG, "onScrollEnded");
            }

            @Override
            public void onScrollStarted()
            {
                tv.setText("开始拖动");
                tv.invalidate();
                Log.i(TAG, "onScrollStarted");
            }

        });

    }

}
