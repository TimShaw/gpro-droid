package lib.func.animation;

import android.lib.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class AnimationActivity extends Activity
{		
	private AnimactionView	mGameView;
	
	private AnimationActivity me = this;
	private String TAG = AnimationActivity.class.getSimpleName();
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		//mGameView = new GameView(this);
		
		setContentView(R.layout.animation);
		
		final AnimactionView gv = (AnimactionView)findViewById(R.id.animationView);
		
		Button alphaBtn = (Button)findViewById(R.id.AlphaAnimation);
		alphaBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				KeyEvent k = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DPAD_UP);
				gv.dispatchKeyEvent(k);
			}
		});
		
		Button scaleBtn = (Button)findViewById(R.id.ScaleAnimation);
		scaleBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				KeyEvent k = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DPAD_DOWN);
				gv.dispatchKeyEvent(k);
			}
		});
		
		Button translateBtn = (Button)findViewById(R.id.TranslateAnimation);
		translateBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				KeyEvent k = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DPAD_LEFT);
				gv.dispatchKeyEvent(k);
			}
		});
		
		Button rotateBtn = (Button)findViewById(R.id.RotateAnimation);
		rotateBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				KeyEvent k = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DPAD_RIGHT);
				gv.dispatchKeyEvent(k);
			}
		});
	}
	
	public boolean onKeyUp(int keyCode, KeyEvent event)
	{
		if ( mGameView == null )
		{
			return false;
		}
		mGameView.onKeyUp(keyCode,event);
		return true;
	}
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if ( mGameView == null )
		{
			return false;
		}
		if ( keyCode ==  KeyEvent.KEYCODE_BACK)
		{
			this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
