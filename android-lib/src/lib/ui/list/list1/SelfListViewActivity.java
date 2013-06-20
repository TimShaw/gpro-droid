package lib.ui.list.list1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lib.ui.R;
import android.app.Activity;
import android.os.Bundle;

public class SelfListViewActivity extends Activity { 
    
    @Override 
    public void onCreate(Bundle savedInstanceState) { 
        super.onCreate(savedInstanceState); 
         
        /** 
         * 设置主布局。 
         */ 
        setContentView(R.layout.list_self_listview_main); 
         
        /** 
         * ListView数据集。 
         */ 
        List<Map<String , Object>> mSelfData = new ArrayList<Map<String,Object>>(); 
         
        /** 
         * 获取ListView组件。 
         */ 
        SelfListView mSelfListView = (SelfListView) findViewById(R.id.ListView); 
         
        /** 
         * 生成数据。 
         */ 
        for (int i = 0; i < 1000; i++) { 
            HashMap<String, Object> mMap = new HashMap<String, Object>(); 
            mMap.put("key_name", "name" + i); 
            mMap.put("value_name", "value" + i); 
            mSelfData.add(mMap); 
        }  
  
        /**  
         * 自定义Adapter。  
         */  
        final SelfAdapter mSelfAdapter = new SelfAdapter(this, mSelfData,   
                R.layout.list_self_listview_item, new String[] { "key_name", "value_name" }, new int[] { R.id.TextViewOne, R.id.TextViewTwo });  
          
        /**  
         * 向ListView设置Adapter。  
         */  
        mSelfListView.setSelfAdapter(mSelfAdapter);  
    }  
      
}  