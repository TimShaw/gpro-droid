package lib.ui.slide;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.lib.R;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * 见 txl-android   GuideActivity
 * @author jinchao
 *
 */
public class GuideActivity extends Activity {
	private ViewPager mViewPager;
	private List<View> views =new ArrayList<View>();
	
	private GuideActivity me = this;
	 private static final int[] guides = { R.drawable.guide1,
         R.drawable.guide2, R.drawable.guide3};
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide);
        mViewPager = (ViewPager)findViewById(R.id.viewPager);
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT);
        for(int i=0; i<guides.length; i++) {
            ImageView iv = new ImageView(this);
            iv.setLayoutParams(mParams);
            iv.setImageResource(guides[i]);
            views.add(iv);
        }
        PagerAdapter pagerAdapter = new PagerAdapter(){
        	
			@Override
			public int getCount() {
				return views.size();
			}

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0==arg1;
			}
			
			@Override
			public Object instantiateItem(ViewGroup container, int position) {
				container.addView(views.get(position));
				return views.get(position);   //返回当前要显示的view
			}
			@Override
			public void destroyItem(ViewGroup container, int position, Object object) {
				container.removeView(views.get(position));
			}
			
			
        };
        
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setOnPageChangeListener(new MyPageListener());
    }
    
    class MyPageListener implements  OnPageChangeListener{

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int arg0) {
		}
    	
    }
}
