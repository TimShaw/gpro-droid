package lib.ui.view;

import lib.ui.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ViewActivity extends Activity {
	
	private ViewActivity me = this;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view);

        Button gameView = (Button)findViewById(R.id.gameView);
        gameView.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(me,GameViewActivity.class);
                startActivity(intent);
            }
        });

        Button textView = (Button)findViewById(R.id.textView);
        textView.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(me,TextViewActivity.class);
                startActivity(intent);
            }
        });
		
	}

}
