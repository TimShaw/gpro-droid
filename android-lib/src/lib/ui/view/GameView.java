package lib.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;

public class GameView extends View {
	private Paint paint = null;
	private String TAG = GameView.class.getSimpleName();
	
	public GameView(Context context) {
		super(context);
		
		paint = new Paint();
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true){
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					GameView.this.postInvalidate();
				}
			}
		}).start();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Log.i(TAG, "..onDraw..");
		canvas.drawColor(Color.WHITE);
		canvas.clipRect(10,10,280,460);
		canvas.save();
		
		paint.setColor(Color.RED);
		paint.setStrokeWidth(3);
		canvas.rotate(45);
		//canvas.translate(-170, 0);
		canvas.drawRect(new Rect(200,10,250,210), paint);
		canvas.restore();
		paint.setColor(Color.GREEN);
		paint.setStyle(Paint.Style.STROKE);
		canvas.drawRect(150,30,230,80, paint);
		
	}
	
	

}
