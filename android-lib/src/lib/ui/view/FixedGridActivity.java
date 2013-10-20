package lib.ui.view;

import android.lib.R;
import android.app.Activity;
import android.os.Bundle;


/**
 * @ClassName:  FixedGridActivity.java
 * @Description: 
 * @Author JinChao
 * @Date 2013-7-25 上午10:02:00
 * @Copyright: 版权由 HundSun 拥有
 */
public class FixedGridActivity extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.fixed_grid_layout);
        
    }
    
}
