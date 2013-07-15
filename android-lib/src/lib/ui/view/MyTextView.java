package lib.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

public class MyTextView extends TextView { 
   
    public MyTextView(Context context, AttributeSet attrs) { 
        super(context, attrs); 
    } 
   
    @Override 
    protected void onDraw(Canvas canvas) { 
    	canvas.rotate(-90); 
    	canvas.translate(-getHeight(), 0); 
        super.onDraw(canvas); 
        
    } 
}