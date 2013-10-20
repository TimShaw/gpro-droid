package lib.ui.veiwpager;

import java.util.ArrayList;

import android.app.Activity;
import android.lib.R;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MyViewPager extends Activity {
	private ViewPager mViewPager;
	private PagerTitleStrip mTitleStrip;
	private PagerTabStrip mPagerTabStrip;
	
	private MyViewPager me = this;
	private ArrayList<View> mViews;
	private ArrayList<String> mTitles;
	 
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_pager);
        
        mViewPager = (ViewPager)findViewById(R.id.viewPager);
        mTitleStrip = (PagerTitleStrip)findViewById(R.id.pagerTitle);
        mPagerTabStrip = (PagerTabStrip)findViewById(R.id.pagerTab);
        
        mViews = new ArrayList<View>();
        View view1 = LayoutInflater.from(me).inflate(R.layout.view_pager_1, null);
        View view2 = LayoutInflater.from(me).inflate(R.layout.view_pager_2, null);
        mViews.add(view1);
        mViews.add(view2);
        mTitles = new ArrayList<String>();
        mTitles.add("页面1");
        mTitles.add("页面2");
        
        PagerAdapter mPagerAdapter = new PagerAdapter() {

			@Override
			public int getCount() {
				return me.mViews.size();
			}

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}

			@Override
			public void destroyItem(ViewGroup container, int position,
					Object object) {
				container.removeView(mViews.get(position));
			}

			@Override
			public int getItemPosition(Object object) {
				return super.getItemPosition(object);
			}

			@Override
			public CharSequence getPageTitle(int position) {
				return mTitles.get(position);
			}

			@Override
			public Object instantiateItem(ViewGroup container, int position) {
				  container.addView(mViews.get(position));
				  return mViews.get(position);
			}
        	
        };
        mViewPager.setAdapter(mPagerAdapter);
	}
}
