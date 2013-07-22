package lib.func.handwrite;

import java.io.ByteArrayOutputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.samsung.chord.ChordManager;
import com.samsung.chord.IChordChannel;

public class PaintView extends View {

	public ChordManager chordManager = null;
	public IChordChannel jointedChannel = null;

	private Paint paint;
	private Canvas cacheCanvas;
	private Bitmap cachebBitmap;
	private Path path;
	private Context ctx;

	public Bitmap getCachebBitmap() {
		return cachebBitmap;
	}

	public PaintView(Context context) {
		super(context);
		this.ctx = context;
		init();
	}
	
	public void setContent(byte[] bytes){
		Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		//cacheCanvas = new Canvas(cachebBitmap);
		//cacheCanvas.setBitmap(bmp);
		//cacheCanvas.drawColor(Color.WHITE);
		if(bmp!=null){
			cacheCanvas.drawBitmap(bmp, 0, 0, null);
			invalidate();
			bmp.recycle();
		}
	}

	private void init() {
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setStrokeWidth(4);
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(Color.BLACK);
		path = new Path();
		WindowManager wm = (WindowManager) ctx
				.getSystemService(Context.WINDOW_SERVICE);
		int height = wm.getDefaultDisplay().getHeight();
		int width = wm.getDefaultDisplay().getWidth();

		cachebBitmap = Bitmap.createBitmap(width, (int) (height * 0.8),
				Config.ARGB_8888);
		cacheCanvas = new Canvas(cachebBitmap);
		cacheCanvas.drawColor(Color.WHITE);
	}

	public void clear() {
		if (cacheCanvas != null) {
			// cacheCanvas.drawPaint(paint);
			// paint.setColor(Color.BLACK);
			cacheCanvas.drawColor(Color.WHITE);
			invalidate();
		}
	}

	public Paint getPaint() {
		return this.paint;
	}

	
	@Override
	protected void onDraw(Canvas canvas) {
		// canvas.drawColor(BRUSH_COLOR);
		canvas.drawBitmap(cachebBitmap, 0, 0, null);
		canvas.drawPath(path, paint);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {

		int curW = cachebBitmap != null ? cachebBitmap.getWidth() : 0;
		int curH = cachebBitmap != null ? cachebBitmap.getHeight() : 0;
		if (curW >= w && curH >= h) {
			return;
		}

		if (curW < w)
			curW = w;
		if (curH < h)
			curH = h;

		Bitmap newBitmap = Bitmap.createBitmap(curW, curH,
				Bitmap.Config.ARGB_8888);
		Canvas newCanvas = new Canvas();
		newCanvas.setBitmap(newBitmap);
		if (cachebBitmap != null) {
			newCanvas.drawBitmap(cachebBitmap, 0, 0, null);
		}
		cachebBitmap = newBitmap;
		cacheCanvas = newCanvas;
	}

	private float cur_x, cur_y;

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		float x = event.getX();
		float y = event.getY();

		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN: {
				cur_x = x;
				cur_y = y;
				path.moveTo(cur_x, cur_y);
				break;
			}
	
			case MotionEvent.ACTION_MOVE: {
				path.quadTo(cur_x, cur_y, x, y);
				cur_x = x;
				cur_y = y;
				break;
			}
	
			case MotionEvent.ACTION_UP: {
				cacheCanvas.drawPath(path, paint);
				path.reset();
				break;
			}
		}
		
		invalidate();

		return true;
	}
	
	public void send(){
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		cachebBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
		byte[] byteArray = stream.toByteArray();

		byte[][] payload = new byte[2][];
		if(byteArray!=null && byteArray.length>0){
			payload[0] = byteArray;
			payload[1] = "".getBytes();
			if(jointedChannel != null)
			{
				jointedChannel.sendDataToAll("samsungdemo", payload);
			}
			byteArray = null;
		}
	}
}