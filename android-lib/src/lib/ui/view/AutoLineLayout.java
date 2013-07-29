package lib.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;


/**
 * @ClassName:  AutoLineLayout.java
 * @Description: 
 * @Author JinChao
 * @Date 2013-7-29 上午10:50:28
 * @Copyright: 版权由 HundSun 拥有
 */
public class AutoLineLayout extends ViewGroup
{
    private final static String TAG = AutoLineLayout.class.getSimpleName();
    
    private final static int VIEW_MARGIN=2;
    
    public AutoLineLayout(Context context)
    {
        super(context);
    }
    
    public AutoLineLayout(Context context, AttributeSet attrs){
        super(context,attrs);
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int childCount = getChildCount();
        for (int index = 0; index < childCount ; index++) {
            final View child = getChildAt(index);
            // measure
            child.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        }
        
    }
    
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {
        Log.i(TAG," changed : "+changed+" , left: "+l+", top:"+t+" , right: "+r+" , bottom: "+b);
        final int count = getChildCount();
        /*
         * 
        int row=0;// which row lay you view relative to parent
        int lengthX=l;    // right position of child relative to parent
        int lengthY=t;    // bottom position of child relative to parent
        for(int i=0;i<count;i++){
            
            final View child = this.getChildAt(i);
            int width = child.getMeasuredWidth();
            int height = child.getMeasuredHeight();
            lengthX+=width+VIEW_MARGIN;
            lengthY=row*(height+VIEW_MARGIN)+VIEW_MARGIN+height+t;
            //if it can't drawing on a same line , skip to next line
            if(lengthX>r){
                lengthX=width+VIEW_MARGIN+l;
                row++;
                lengthY=row*(height+VIEW_MARGIN)+VIEW_MARGIN+height+t;
                
            }
            
            child.layout(lengthX-width, lengthY-height, lengthX, lengthY);
        }*/
        int xLength = 0;
        int yLength = 0;
        
        int left = 0;
        int top = 0;
        int right = 0 ;
        int bottom = 0;
        
        int maxHeight = 0;
        for(int i=0;i<count;i++){
            View child = this.getChildAt(i);
            int width = child.getMeasuredWidth();
            int height = child.getMeasuredHeight();
            Log.i(TAG," width: "+width+",height:"+height);
            int _xLen = xLength+width;
            
            //换行
            if(_xLen > r){
               yLength += maxHeight;
               left = l;
               top = yLength;
               right = width;
               bottom = top + height;
               child.layout(left,top,right,bottom);
               xLength = width;
               maxHeight = height;
            }
            //同行
            else{
                if(maxHeight ==0 ){
                    maxHeight = height;
                }else{
                    maxHeight = maxHeight < height ? height: maxHeight;
                }
                /* 计算 空白间距  */
                int spaceHeight = maxHeight - height;
                spaceHeight = spaceHeight>0 ? spaceHeight/2 : 0;
                Log.i(TAG, "onLayout spaceHeight:"+spaceHeight);
                left = xLength;
                top = yLength + spaceHeight;
                right = xLength + width;
                bottom = top + height;
                child.layout(left,top,right,bottom);
                xLength += width;
                 
                
            }
            
            
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas)
    {
        // TODO Auto-generated method stub
        super.dispatchDraw(canvas);
    }

    
    
    

}
