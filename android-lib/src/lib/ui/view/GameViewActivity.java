package lib.ui.view;

import android.app.Activity;
import android.os.Bundle;

public class GameViewActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		GameView view = new GameView(this);
		setContentView(view);
	}

}
