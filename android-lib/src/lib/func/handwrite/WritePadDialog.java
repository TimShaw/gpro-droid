package lib.func.handwrite;

import lib.ui.R;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class WritePadDialog extends Dialog {  
    
    Context context;  
    LayoutParams p ;  
    DialogListener dialogListener;  
    
  
    public WritePadDialog(Context context,DialogListener dialogListener) {  
        super(context,R.style.Dialog_Fullscreen);
        this.context = context;  
        this.dialogListener = dialogListener;  
    }  
  
    static final int BACKGROUND_COLOR = Color.WHITE;  
  
    static final int BRUSH_COLOR = Color.BLACK;  
  
    PaintView mView;  
  
    /** The index of the current color to use. */  
    int mColorIndex;  
  
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        requestWindowFeature(Window.FEATURE_NO_TITLE);  
        requestWindowFeature(Window.FEATURE_PROGRESS);  
        setContentView(R.layout.hand_write_pad);  
          
        /*p = getWindow().getAttributes();  //获取对话框当前的参数值     
        p.height = 320;//(int) (d.getHeight() * 0.4);   //高度设置为屏幕的0.4   
        p.width = 480;//(int) (d.getWidth() * 0.6);    //宽度设置为屏幕的0.6             
        getWindow().setAttributes(p);     //设置生效  
*/          
          
        mView = new PaintView(context);  
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.tablet_view);  
        frameLayout.addView(mView);  
        mView.requestFocus();  
        
        RadioGroup colorGroup = (RadioGroup)findViewById(R.id.colorGroup);
        colorGroup.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                switch(checkedId){
                    case R.id.red : {
                        mView.getPaint().setColor(context.getResources().getColor(R.color.red));
                        break;
                    }
                    case R.id.blue : {
                        mView.getPaint().setColor(context.getResources().getColor(R.color.blue));
                        break;
                    }
                    case R.id.yellow : {
                        mView.getPaint().setColor(context.getResources().getColor(R.color.yellow));
                        break;
                    }
                }
            }
        });
        
        RadioGroup thicknessGroup = (RadioGroup)findViewById(R.id.thicknessGroup);
        thicknessGroup.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                switch(checkedId){
                    case R.id.thickness_0 : {
                        mView.getPaint().setStrokeWidth(0);
                        break;
                    }
                    case R.id.thickness_1 : {
                        mView.getPaint().setStrokeWidth(1);
                        break;
                    }
                    case R.id.thickness_2 : {
                        mView.getPaint().setStrokeWidth(2);
                        break;
                    }
                    case R.id.thickness_3 : {
                        mView.getPaint().setStrokeWidth(3);
                        break;
                    }
                    case R.id.thickness_4 : {
                        mView.getPaint().setStrokeWidth(5);
                        break;
                    }
                }
            }
        });
        
        Button btnClear = (Button) findViewById(R.id.tablet_clear);  
        btnClear.setOnClickListener(new View.OnClickListener() {  
  
            @Override  
            public void onClick(View v) {  
                 mView.clear();  
            }  
        });  
  
        Button btnOk = (Button) findViewById(R.id.tablet_ok);  
        btnOk.setOnClickListener(new View.OnClickListener() {  
  
            @Override  
            public void onClick(View v) {  
                try {  
                    dialogListener.refreshActivity(mView.getCachebBitmap());  
                    WritePadDialog.this.dismiss();  
                } catch (Exception e) {  
                    e.printStackTrace();  
                }  
            }  
        });  
          
        Button btnCancel = (Button)findViewById(R.id.tablet_cancel);  
        btnCancel.setOnClickListener(new View.OnClickListener() {  
              
            @Override  
            public void onClick(View v) {  
                cancel();  
            }  
        });  
    }  
      
  
    /** 
     * This view implements the drawing canvas. 
     *  
     * It handles all of the input events and drawing functions. 
     */  
    class PaintView extends View {  
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
  
        private void init(){  
            paint = new Paint();  
            paint.setAntiAlias(true);  
            paint.setStrokeWidth(4);  
            paint.setStyle(Paint.Style.STROKE);  
            paint.setColor(Color.BLACK);                      
            path = new Path();  
            WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
            int height = wm.getDefaultDisplay().getHeight();  
            int width = wm.getDefaultDisplay().getWidth();
            
            cachebBitmap = Bitmap.createBitmap(width, (int)(height*0.8), Config.ARGB_8888);           
            cacheCanvas = new Canvas(cachebBitmap);  
            cacheCanvas.drawColor(Color.WHITE);  
        }  
        public void clear() {  
            if (cacheCanvas != null) {  
                //cacheCanvas.drawPaint(paint);  
                //paint.setColor(Color.BLACK);  
                cacheCanvas.drawColor(Color.WHITE);  
                invalidate();             
            }  
        }  
        public Paint getPaint(){
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
  
            Bitmap newBitmap = Bitmap.createBitmap(curW, curH, Bitmap.Config.ARGB_8888);  
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
    }  
  
}  