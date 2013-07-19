package lib.ui.tab;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;


/**
 * @ClassName:  ContentActivity0.java
 * @Description: 
 * @Author JinChao
 * @Date 2013-7-18 下午4:33:14
 * @Copyright: 版权由 HundSun 拥有
 */
public class ContentActivity4 extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        TextView tv = new TextView(this);
        tv.setText("ContentActivity4");
        setContentView(tv);
        
    }
    
}
