package com.guoyin.util;

import android.os.Handler;
import android.os.Message;

public abstract class Repeater extends Handler {

	private long delay;
	private boolean pause;

	public Repeater(long delay) {
		super();
		this.delay = delay;
	}

	@Override
	public void handleMessage(Message msg) {
		// TODO Auto-generated method stub
		super.handleMessage(msg);

		if (! pause) {
			repeateAction();
			sendEmptyMessageDelayed(0, delay);
		}
	}

	public void resumeWithDelay() {
		pause = false;
		if (!hasMessages(0)) {
			sendEmptyMessageDelayed(0, delay);
		}
	}

	public void resume() {
		pause = false;
		if (!hasMessages(0)) {
			sendEmptyMessage(0);
		}
	}

	public void pause() {
		pause = true;
		removeMessages(0);
	}

	public abstract void repeateAction();
}
