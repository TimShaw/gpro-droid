package lib.ui.tab;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;


/**
 * @ClassName:  ContentActivity0.java
 * @Description: 
 * @Author JinChao
 * @Date 2013-7-18 下午4:33:14
 * @Copyright: 版权由 HundSun 拥有
 */
public class ContentActivity0 extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        LinearLayout ll = new LinearLayout(this);
        LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT);
        ll.setLayoutParams(params);
        ll.setOrientation(LinearLayout.VERTICAL);
        TextView tv = new TextView(this);
        tv.setText("ContentActivity0");
        ll.addView(tv);
        Button button = new Button(this);
        button.setText("ContentActivity4");
        button.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(ContentActivity0.this,ContentActivity4.class);
                startActivity(intent);
            }
        });
        ll.addView(button);
        setContentView(ll);
        
        
        
         
    }
    
}
