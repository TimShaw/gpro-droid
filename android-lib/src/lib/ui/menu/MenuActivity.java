package lib.ui.menu;

import android.lib.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MenuActivity extends Activity
{
    private MyMenu menu;
    private LinearLayout linear;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        init();
    }
    //组件初始化
    private void init(){
        linear = (LinearLayout) findViewById(R.id.popMenu);
        int width = getWindowManager().getDefaultDisplay().getWidth()-15;      //菜单的宽度
        int height = getWindowManager().getDefaultDisplay().getHeight()/3;     //菜单的高度
        menu = new MyMenu(this, width,height);
        Button button= (Button) menu.getOption(R.id.first_button); //获取菜单第一个标签页中的按钮
        //添加点击事件
        button.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v) {
                Toast.makeText(MenuActivity.this,"tab one",Toast.LENGTH_SHORT).show();
            }
        });
    }

    //显示菜单
    private void show(){
        menu.showAtLocation(linear, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL,0,0);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //按以下菜单show按键展示菜单 按两下隐藏菜单
        if(!menu.isShowing()&&keyCode == KeyEvent.KEYCODE_MENU){
            show();
        }
        else{
            menu.dismiss();
        }
        return true;
    }
}
