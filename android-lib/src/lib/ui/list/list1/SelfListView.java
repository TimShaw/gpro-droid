package lib.ui.list.list1;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

public class SelfListView extends LinearLayout{  
    
    private BaseAdapter mSelfAdapter;  
    private String TAG = SelfListView.class.getSimpleName();
      
    public SelfListView(Context context) {  
        super(context);  
    }  
  
    public SelfListView(Context context, AttributeSet attributeSet) {  
        super(context, attributeSet);  
    }  
  
    /** 
     * 删除ListView中上一次渲染的View，并添加新View。 
     */  
    private void buildList() {  
        if (mSelfAdapter == null) {  
              
        }  
          
        if (getChildCount() > 0) {  
            removeAllViews();  
        }  
          
        int count = mSelfAdapter.getCount();  
          
        for(int i = 0 ; i < count ; i++) {  
            View view = mSelfAdapter.getView(i, null, null);  
            if (view != null) {  
                addView(view, i);  
            }  
        }  
    }  
      
    public BaseAdapter getSelfAdapter() {  
        return mSelfAdapter;  
    }  
  
    /** 
     * 设置Adapter。 
     *  
     * @param selfAdapter 
     */  
    public void setSelfAdapter(BaseAdapter selfAdapter) {  
        this.mSelfAdapter = selfAdapter;  
        buildList();  
    }

    /*@Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
    {
        Log.i(TAG, "onScroll....firstVisibleItem:"+firstVisibleItem+",visibleItemCount:"+visibleItemCount+",totalItemCount:"+totalItemCount);
        
        
        
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState)
    {
        Log.i(TAG, "onScrollStateChanged.... scrollState:"+scrollState);
        
        
        
    }  */
      
}  