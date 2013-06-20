package lib.ui.menu;

import lib.ui.R;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TabHost;

public class MyMenu extends PopupWindow {
    private TabHost tabHost;    //标签页窗口
    private LayoutInflater inflater;     //用于加载tabhost
    private View layout;
    private Context context;

    //构造函数
    public MyMenu(Context context, int width, int height) {
        super(context);
        this.context = context;
        inflater = LayoutInflater.from(this.context);

        //创建标签页
        initTab();

        //设置默认选项
        setWidth(width);          //宽
        setHeight(height);        //高
        setContentView(tabHost);  //把标签页设置到PopupWindow上
    }

    //实例化标签页
    private void initTab(){
        layout =  inflater.inflate(R.layout.menu,null);
        tabHost = (TabHost)layout.findViewById(android.R.id.tabhost);       //获取tabhost
        tabHost.setBackgroundColor(Color.argb(60,144,144,150));              //设置背景色
        tabHost.setup();           //使用findViewById()加载tabhost时在调用addTab前必须调用
        /**
         * addTab()添加标签页
         *      tabHost.newTabSpec("Fitst")  创建一个tab
         *      setIndicator("A") 设置指针
         *      setContent(R.id.tab1)设置内容
         */
        tabHost.addTab(tabHost.newTabSpec("Fitst").setIndicator("A").setContent(R.id.tab1));
        tabHost.addTab(tabHost.newTabSpec("SECOND").setIndicator("B").setContent(R.id.tab2));
        tabHost.addTab(tabHost.newTabSpec("THIRD").setIndicator("C").setContent(R.id.tab3));
        tabHost.setCurrentTab(1);                                            //设置默认选种标签
    }

    //获取选项卡中的组件
    public  View getOption(int id){
        return layout.findViewById(id);
    }
}
