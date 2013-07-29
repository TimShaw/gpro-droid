package lib.ui.view;

import lib.ui.R;
import android.app.Activity;
import android.os.Bundle;


/**
 * @ClassName:  MyPhoneCardActivity.java
 * @Description: 
 * @Author JinChao
 * @Date 2013-7-25 下午4:41:57
 * @Copyright: 版权由 HundSun 拥有
 */
public class MyPhoneCardActivity extends Activity
{
    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.phone_layout);
    }
}
