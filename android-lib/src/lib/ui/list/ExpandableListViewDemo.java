package lib.ui.list;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lib.ui.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

public class ExpandableListViewDemo extends Activity {
    /** Called when the activity is first created. */
	
	//定义两个List用来控制Group和Child中的String;
	
	private  List<String>  groupArray;//组列表
	private  List<List<String>> childArray;//子列表
	private  ExpandableListView  expandableListView;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);  //设置为无标题  
        setContentView(R.layout.expandable_listview);
        expandableListView =(ExpandableListView)findViewById(R.id.expandableListView);
        groupArray = new ArrayList<String>();
        childArray = new ArrayList<List<String>>();
       
        
        /*initdata();
        expandableListView.setAdapter(new ExpandableListViewAdapter(ExpandableListViewDemo.this));*/
        
        
       //创建二个一级条目标题    
        Map<String, String> title_1 = new HashMap<String, String>();   
        Map<String, String> title_2 = new HashMap<String, String>();   
           
        title_1.put("group", "移动开发");   
        title_2.put("group", "男人的需求"); 
        
        //创建一级条目容器    
        List<Map<String, String>> groups = new ArrayList<Map<String,String>>();   
           
        groups.add(title_1);   
        groups.add(title_2);   
           
        //创建二级条目内容    
           
        //内容一    
        Map<String, String> content_1 = new HashMap<String, String>();   
        Map<String, String> content_2 = new HashMap<String, String>();   
           
        content_1.put("child", "ANDROID");   
        content_2.put("child", "IOS");   
           
        List<Map<String, String>> childs_1 = new ArrayList<Map<String,String>>();   
        childs_1.add(content_1);   
        childs_1.add(content_2);   
           
        //内容二    
        Map<String, String> content_3 = new HashMap<String, String>();   
        Map<String, String> content_4 = new HashMap<String, String>();  
        Map<String, String> content_5 = new HashMap<String, String>(); 
           
        content_3.put("child", "金钱");   
        content_4.put("child", "权力");   
        content_5.put("child", "女人"); 
        List<Map<String, String>> childs_2 = new ArrayList<Map<String,String>>();   
        childs_2.add(content_3);   
        childs_2.add(content_4);  
        childs_2.add(content_5); 
           
        //存放两个内容, 以便显示在列表中    
        List<List<Map<String, String>>> childs = new ArrayList<List<Map<String,String>>>();   
        childs.add(childs_1);   
        childs.add(childs_2);   
        
        SimpleExpandableListAdapter adapter = new SimpleExpandableListAdapter(   
                                                                              this, groups, R.layout.expandable_listview_group, new String[]{"group"}, new int[]{R.id.textGroup},    
                                                                              childs, R.layout.expandable_listview_child, new String[]{"child"}, new int[]{R.id.textChild}   
                                                                              );   
        expandableListView.setAdapter(adapter); 
 
        
    }
    class ExpandableListViewAdapter extends BaseExpandableListAdapter {
    	Activity activity;
    	 public  ExpandableListViewAdapter(Activity a)  
    	    {  
    	        activity = a;  
    	    }  
       /*-----------------Child */
    	@Override
    	public Object getChild(int groupPosition, int childPosition) {
    		// TODO Auto-generated method stub
    		return childArray.get(groupPosition).get(childPosition);
    	}

    	@Override
    	public long getChildId(int groupPosition, int childPosition) {
    		// TODO Auto-generated method stub
    		return childPosition;
    	}

    	@Override
    	public View getChildView(int groupPosition, int childPosition,
    			boolean isLastChild, View convertView, ViewGroup parent) {
    		
    		String string =childArray.get(groupPosition).get(childPosition);
    		
    		return getGenericView(string);
    	}

    	@Override
    	public int getChildrenCount(int groupPosition) {
    		// TODO Auto-generated method stub
    		return childArray.get(groupPosition).size();
    	}
       /* ----------------------------Group */
    	@Override
    	public Object getGroup(int groupPosition) {
    		// TODO Auto-generated method stub
    		return getGroup(groupPosition);
    	}

    	@Override
    	public int getGroupCount() {
    		// TODO Auto-generated method stub
    		return groupArray.size();
    	}

    	@Override
    	public long getGroupId(int groupPosition) {
    		// TODO Auto-generated method stub
    		return groupPosition;
    	}

    	@Override
    	public View getGroupView(int groupPosition, boolean isExpanded,
    			View convertView, ViewGroup parent) {
    		
           String   string=groupArray.get(groupPosition);
           return getGenericView(string);
    	}

    	@Override
    	public boolean hasStableIds() {
    		// TODO Auto-generated method stub
    		return false;
    	}

    	@Override
    	public boolean isChildSelectable(int groupPosition, int childPosition) 
    	{
    		// TODO Auto-generated method stub
    		return true;
    	}
    	
    	private TextView  getGenericView(String string ) 
    	{
              AbsListView.LayoutParams  layoutParams =new AbsListView.LayoutParams(
    				ViewGroup.LayoutParams.MATCH_PARENT,
    				ViewGroup.LayoutParams.WRAP_CONTENT);
              
              TextView  textView =new TextView(activity);
              textView.setLayoutParams(layoutParams);
              
              textView.setGravity(Gravity.CENTER_VERTICAL |Gravity.LEFT);
              
              textView.setPadding(40, 0, 0, 0);
              textView.setText(string);
              return textView;
         }
    }
    
    private void initdata() 
    {
    	addInfo("语言", new String[]{"Oracle","Java","Linux","Jquery"});
    	addInfo("男人的需求", new String[]{"金钱","事业","权力","女人","房子","车","球"});
    }
    private void addInfo(String group,String []child) {
		
    	groupArray.add(group);
    	
    	List<String>  childItem =new ArrayList<String>();
    	
    	for(int index=0;index<child.length;index++)
    	{
    		childItem.add(child[index]);
    	}
         childArray.add(childItem);
	}
}