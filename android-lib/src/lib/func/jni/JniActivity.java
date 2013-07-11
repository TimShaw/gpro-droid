package lib.func.jni;

import lib.ui.R;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;


/**
 * @ClassName:  JniActivity.java
 * @Description: 
 * @Author JinChao
 * @Date 2013-7-11 下午4:35:21
 * @Copyright: 版权由 HundSun 拥有
 */
public class JniActivity extends Activity
{
    private String TAG = JniActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jni);
        Log.i(TAG, "");
        JniTest jniTest = new JniTest();
        String line = jniTest.getLine(" allan kin 你");
        Log.i(TAG, line);
        
    }
    
}
