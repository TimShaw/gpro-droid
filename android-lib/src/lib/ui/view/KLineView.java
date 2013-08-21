package lib.ui.view;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Bitmap.Config;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * K线图
 * 
 * @author jinchao
 * 
 */
public class KLineView extends View {
	private String TAG = KLineView.class.getSimpleName();
	private Canvas canvas;
	private int width;
	private int height;
	private Paint paint;
	
	private float x;
	
	private Bitmap cachebBitmap; 
	
	private MoveListener moveListener;
	
	private int xCounts = 10;  //初始化为10
	
	private int average;
	
	private boolean hasDrawed;
	
	private Path path;  
	
	private List<KLineItemData> mDataList ;

	public KLineView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setStrokeWidth(2);
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(Color.BLACK);
		
		path = new Path(); 
		
	}
	 
	
	public void setDataList(List<KLineItemData> dataList){
		this.mDataList = dataList;
		this.xCounts = this.mDataList.size();
	}
	public void setMoveListener(MoveListener listener){
		this.moveListener = listener;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		
		int measuredWidth = this.getMeasuredWidth();
		width = this.getWidth();
		int measuredHeight = this.getMeasuredHeight();
		height = this.getHeight();
		
		average = this.width/this.xCounts;
		Log.i(TAG," average : "+average);
		
		Log.i(TAG, " measuredWidth:" + measuredWidth + " , measuredHeight:"
				+ measuredHeight + " ,width: " + width + ",height:" + height);
		
		/*cachebBitmap = Bitmap.createBitmap(width, (int)(height*0.8), Config.ARGB_8888); 
		this.canvas = new Canvas(cachebBitmap);
		this.canvas.drawColor(Color.WHITE);*/
		
		
		//this.canvas.setBitmap(cachebBitmap);
		canvas.drawColor(Color.WHITE);
		canvas.drawLine(x, 0, x, height, paint);
		
		
		if(!hasDrawed){
			float lastX = 0;
			float lastY = 0;
			for(int i=0;i<xCounts;i++){
				float x = i*average+average/2;
				/*因为canvas绘图坐标 和 呈现的图形坐标在 y轴上相反*/
				float y = height-this.mDataList.get(i).lastPrice;
				if(i>0){
					path.quadTo(lastX, lastY, x, y);
				}
				
				lastX = x;
				lastY = y;
				if(i==0){
					path.moveTo(lastX, lastY);  
				}
				Log.i(TAG," draw ploy line : x:"+x+",y:"+y);
			}
			canvas.drawPath(path, paint);
			hasDrawed = true;
		}
		
		super.onDraw(canvas);

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		x = event.getRawX();
		float y = event.getRawY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			break;

		case MotionEvent.ACTION_MOVE:
			Log.i(TAG, " x : " + x);
			if(moveListener!=null){
				int index = (int)x/average;
				int mod = (int)x%average;
				Log.i(TAG, "x:"+x+", index :"+index+",mod:"+mod);
			
				/*防止边界溢出
				if(index < xCounts){
					this.moveListener.onMove(index);
				}*/
				int mid = index*average + average/2;
				if((int)x == mid){
					this.moveListener.onMove(index);
				}
				
			}
			this.invalidate();
			break;

		case MotionEvent.ACTION_UP:

			break;
		}

		return true;
	}
	
	
	public interface MoveListener{
		public void onMove(int index);
	}

	public static class KLineItemData{
		public float maxPrvice;
		public float minPrvice;
		public float lastPrice;
	}
}
