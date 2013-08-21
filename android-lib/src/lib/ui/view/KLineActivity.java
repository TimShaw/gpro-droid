package lib.ui.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import lib.ui.R;
import lib.ui.view.KLineView.KLineItemData;
import lib.ui.view.KLineView.MoveListener;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class KLineActivity extends Activity {
	
	private String TAG = KLineActivity.class.getSimpleName();
	List<KLineItemData> mList ;
	private TextView mLastPriceView;
	@Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.kline_activity);
        
        mList = new ArrayList<KLineItemData>();
        for(int i=0;i<10;i++){
        	int p = new Random().nextInt(150);
        	KLineItemData data = new KLineItemData();
        	data.lastPrice = p;
        	mList.add(data);
        	Log.i(TAG,"lastPrice:"+p);
        }
        
        mLastPriceView = (TextView)findViewById(R.id.lastPrice);
        KLineView kLineView = (KLineView)findViewById(R.id.kline);
        kLineView.setDataList(mList);
        kLineView.setMoveListener(new MoveListener() {
			@Override
			public void onMove(int index) {
				Log.i(TAG, "index: "+index+" , lastPrive:"+mList.get(index).lastPrice);
				mLastPriceView.setText(String.valueOf(mList.get(index).lastPrice));
			}
		});
        
        
    }
	
	
	
}
