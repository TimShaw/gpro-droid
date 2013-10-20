package lib.ui.veiwpager;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.lib.R;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

public class MyViewPager2 extends Activity implements OnClickListener{

	LayoutInflater 		inflater;
	View			 	first;
	View 				second;
	TextView			textView1;  //导航栏第一个标题 
	TextView			textView2;  //导航栏第二个标题 
	ViewPager 			viewPager;  
	ImageView 			image;      //移动的那调横线，其实是一个imageviwe
	int 				moveX;    //导航下面横线偏移宽度
	int 				width;    //导航下面比较粗的线的宽度
	int 				index;    //当前第一个view
	List<View> 			viewList=new ArrayList<View>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_pager2);
		init();
	}


	public void init(){
		viewPager	=(ViewPager) findViewById(R.id.viewpager);
		inflater	=this.getLayoutInflater().from(this);
		first		=inflater.inflate(R.layout.view_pager_1, null);
		second		=inflater.inflate(R.layout.view_pager_2,null);
		textView1	=(TextView) findViewById(R.id.text1);
		textView2	=(TextView) findViewById(R.id.text2);
		image		=(ImageView) findViewById(R.id.iamge);

		textView1	.setOnClickListener(this);
		textView2	.setOnClickListener(this);
		viewList	.add(first);  
		viewList	.add(second);
		viewPager	.setAdapter(new MyPagerAdapter(viewList));
		viewPager	.setOnPageChangeListener(new MyPageListener());
		
		DisplayMetrics dm 	= new DisplayMetrics();  //获取手机分辨率
		getWindowManager()	.getDefaultDisplay().getMetrics(dm);
		int screenW 		= dm.widthPixels;// 获取分辨率宽度

		width	= BitmapFactory.decodeResource(getResources(), R.drawable.indicator).getWidth();// 获取图片宽度
		// 计算偏移量，也就是那条粗的下划线距离屏幕坐标的距离，如果有三个view则screeW/3，以此类推
		moveX 	= (screenW / 2 - width) / 2;	
		Matrix matrix = new Matrix();
		matrix	.postTranslate(moveX, 0);
		image	.setImageMatrix(matrix);	// 设置动画初始位置
	}


	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.text1:
			viewPager.setCurrentItem(0);  //点击first的时候设置第一个view显示
			break;
		case R.id.text2:
			viewPager.setCurrentItem(1);  //点击second的时候设置第一个view显示
			break;
		}
	}


	class MyPageListener implements  OnPageChangeListener{

		@Override
		public void onPageScrollStateChanged(int arg0) {}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {}

		@Override
		public void onPageSelected(int arg0) {
			int x=moveX*2+width;   //从第一个到第二个view，粗的下划线的偏移量
			/**
			 * TranslateAnimation(float fromXDelta, float toXDelta, float fromYDelta, float toYDelta)
			 * 　     float fromXDelta:这个参数表示动画开始的点离当前View X坐标上的差值；
				float toXDelta, 这个参数表示动画结束的点离当前View X坐标上的差值；
				float fromYDelta, 这个参数表示动画开始的点离当前View Y坐标上的差值；
				float toYDelta)这个参数表示动画开始的点离当前View Y坐标上的差值；
			 */
			Log.v("index的值为:", index+"");
			Log.v("arg0的值为:", arg0+"");
			Animation animation=
					new TranslateAnimation(x*index, x*arg0, 0, 0);
			index=arg0;
			
			animation	.setFillAfter(true);   //设置动画停止在结束位置
			animation	.setDuration(300);     //设置动画时间
			image		.startAnimation(animation);  //启动动画
		}
	}
	class MyPagerAdapter extends PagerAdapter {
		List<View> viewList;
		public MyPagerAdapter(List<View> viewList){
			this.viewList=viewList;
		}
		@Override
		public int getCount() {
			return viewList.size();
		}

		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(viewList.get(position));
		}
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(viewList.get(position));
			return viewList.get(position);   //返回当前要显示的view
		}
		
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0==arg1;  //不这样写会显示不了 view
		}

	}
}
