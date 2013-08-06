package lib.app.music_player;

import android.app.Application;

public class MusicPlayerApp extends Application
{
	private MusicInfoController	mMusicInfoController	= null;

	public void onCreate()
	{
		super.onCreate();

		mMusicInfoController = MusicInfoController.getInstance(this);
	}

	public MusicInfoController getMusicInfoController()
	{
		return mMusicInfoController;
	}

}
