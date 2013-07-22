package lib.func.handwrite;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.samsung.R;
import com.samsung.chord.ChordManager;
import com.samsung.chord.IChordChannel;
import com.samsung.chord.IChordChannelListener;
import com.samsung.chord.IChordManagerListener;

public class HandwritingActivity extends Activity implements IChordChannelListener, IChordManagerListener{  
    /** Called when the activity is first created. */  
      
    private Bitmap mSignBitmap;  
    private String signPath;  
    private ImageView ivSign;  
    private TextView tvSign;  
    PaintView mView;  
    Context context;  
    
    private String TAG = HandwritingActivity.class.getSimpleName();
    private ChordManager chordManager;
	private IChordChannel jointedChannel;
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        this.context = this;
        setContentView(R.layout.hand_write_pad);  
        setChordEnvironment();
        mView = new PaintView(context);  
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.tablet_view);  
        frameLayout.addView(mView);  
        mView.requestFocus();  
        mView.chordManager = chordManager;
        
        
        
        
        RadioGroup colorGroup = (RadioGroup)findViewById(R.id.colorGroup);
        colorGroup.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                switch(checkedId){
                    case R.id.red : {
                        mView.getPaint().setColor(context.getResources().getColor(R.color.red));
                        break;
                    }
                    case R.id.blue : {
                        mView.getPaint().setColor(context.getResources().getColor(R.color.blue));
                        break;
                    }
                    case R.id.yellow : {
                        mView.getPaint().setColor(context.getResources().getColor(R.color.yellow));
                        break;
                    }
                    case R.id.black: {
                    	 mView.getPaint().setColor(context.getResources().getColor(R.color.black));
                         break;
                    }
                }
            }
        });
        
        RadioGroup thicknessGroup = (RadioGroup)findViewById(R.id.thicknessGroup);
        thicknessGroup.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                switch(checkedId){
                    case R.id.thickness_0 : {
                        mView.getPaint().setStrokeWidth(0);
                        break;
                    }
                    case R.id.thickness_1 : {
                        mView.getPaint().setStrokeWidth(1);
                        break;
                    }
                    case R.id.thickness_2 : {
                        mView.getPaint().setStrokeWidth(2);
                        break;
                    }
                    case R.id.thickness_3 : {
                        mView.getPaint().setStrokeWidth(3);
                        break;
                    }
                    case R.id.thickness_4 : {
                        mView.getPaint().setStrokeWidth(5);
                        break;
                    }
                }
            }
        });
        
        
        Button btnClear = (Button) findViewById(R.id.tablet_clear);  
        btnClear.setOnClickListener(new View.OnClickListener() {  
  
            @Override  
            public void onClick(View v) {  
                 mView.clear();  
                 mView.send();
            }  
        });  
  
        Button btnOk = (Button) findViewById(R.id.tablet_ok);  
        btnOk.setOnClickListener(new View.OnClickListener() {  
  
            @Override  
            public void onClick(View v) {  
                try {  
                	mView.send();
                } catch (Exception e) {  
                    e.printStackTrace();  
                }  
            }  
        });  
          
         
    }  
    
  
    private void setChordEnvironment() {
		// TODO Auto-generated method stub
        Log.v(TAG, "setChordEnvironment Started");

		
		chordManager = ChordManager.getInstance(this);
		chordManager.setHandleEventLooper(getMainLooper());
		chordManager.start(ChordManager.INTERFACE_TYPE_WIFI,  this);


		Log.v(TAG, "setChordEnvironment End");
		
	}
     
    /** 
     * 创建手写签名文件 
     *  
     * @return 
     */  
    private String createFile() {  
        ByteArrayOutputStream baos = null;  
        String _path = null;  
        try {  
            String sign_dir = Environment.getExternalStorageDirectory() + File.separator;             
            _path = sign_dir + System.currentTimeMillis() + ".jpg";  
            baos = new ByteArrayOutputStream();  
            mSignBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);  
            byte[] photoBytes = baos.toByteArray();  
            if (photoBytes != null) {  
                new FileOutputStream(new File(_path)).write(photoBytes);  
            }  
  
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            try {  
                if (baos != null)  
                    baos.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
        return _path;  
    }

	@Override
	public void onError(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNetworkDisconnected() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStarted(String arg0, int arg1) {
		// TODO Auto-generated method stub
		Log.v(TAG, "onStarted");
		
		jointedChannel = chordManager.joinChannel(ChordManager.PUBLIC_CHANNEL,  this);
		mView.jointedChannel = this.jointedChannel;
	}

	@Override
	public void onDataReceived(String arg0, String arg1, String arg2,
			byte[][] arg3) {
		byte[] bytes = arg3[0];
		mView.setContent(bytes);
	}

	@Override
	public void onFileChunkReceived(String arg0, String arg1, String arg2,
			String arg3, String arg4, String arg5, long arg6, long arg7) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFileChunkSent(String arg0, String arg1, String arg2,
			String arg3, String arg4, String arg5, long arg6, long arg7,
			long arg8) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFileFailed(String arg0, String arg1, String arg2,
			String arg3, String arg4, int arg5) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFileReceived(String arg0, String arg1, String arg2,
			String arg3, String arg4, String arg5, long arg6, String arg7) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFileSent(String arg0, String arg1, String arg2, String arg3,
			String arg4, String arg5) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFileWillReceive(String arg0, String arg1, String arg2,
			String arg3, String arg4, String arg5, long arg6) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNodeJoined(String arg0, String arg1) {
		// TODO Auto-generated method stub
		Log.v(TAG, "onNodeJoined "+arg0+","+arg1);
		
	}

	@Override
	public void onNodeLeft(String arg0, String arg1) {
		// TODO Auto-generated method stub
		Log.v(TAG, "onNodeLeft  "+arg0+","+arg1);
		
	}  
} 