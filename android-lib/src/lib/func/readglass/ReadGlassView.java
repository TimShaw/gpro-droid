package lib.func.readglass;

import lib.ui.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 放大镜实现方式1
 *
 */
public class ReadGlassView extends View{
	private Bitmap bitmap;
	private ShapeDrawable drawable;
	//放大镜的半径
	private static final int RADIUS = 80;
	//放大倍数
	private static final int FACTOR = 3;
	private Matrix matrix = new Matrix();

	public ReadGlassView(Context context,AttributeSet attr){
	    super(context, attr);
	    Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.guide1);
        bitmap = bmp;       
        BitmapShader shader = new BitmapShader(
                Bitmap.createScaledBitmap(bmp, bmp.getWidth()*FACTOR,
                        bmp.getHeight()*FACTOR, true), TileMode.CLAMP, TileMode.CLAMP);
        //圆形的drawable
        drawable = new ShapeDrawable(new OvalShape());
        drawable.getPaint().setShader(shader);
        drawable.setBounds(0, 0, RADIUS*2, RADIUS*2);
	}
	
	public ReadGlassView(Context context) {
		super(context);
		
	}	
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		final int x = (int) event.getX();
		final int y = (int) event.getY();
		//这个位置表示的是，画shader的起始位置
		matrix.setTranslate(RADIUS-x*FACTOR, RADIUS-y*FACTOR);
		drawable.getPaint().getShader().setLocalMatrix(matrix);
		//bounds，就是那个圆的外切矩形
		//drawable.setBounds(x-RADIUS, y-RADIUS, x+RADIUS, y+RADIUS);
		drawable.setBounds(x-2*RADIUS, y-2*RADIUS, x, y);
		invalidate();
		return true;
	}
	
	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawBitmap(bitmap, 0, 0, null);
		drawable.draw(canvas);
	}
}
