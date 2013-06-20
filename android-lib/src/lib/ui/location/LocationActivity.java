package lib.ui.location;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;


/**
 * @ClassName:  LocationActivity.java
 * @Description: 
 * @Author JinChao
 * @Date 2013-5-27 下午3:17:42
 * @Copyright: 版权由 HundSun 拥有
 */
public class LocationActivity extends Activity
{
    private String TAG = LocationActivity.class.getSimpleName();
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        LocationManager locManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);  
        LocationListener locListener = new LocationListener() {  
            @Override  
            public void onStatusChanged(String provider, int status,  
                    Bundle extras) {  
                Log.i(TAG, "onStatusChanged... provider: "+provider+", status:"+status);
            }  
            @Override  
            public void onProviderEnabled(String provider) {  
                Log.i(TAG, "onProviderEnabled... provider: "+provider);
            }  
            @Override  
            public void onProviderDisabled(String provider) {  
                Log.i(TAG, "onProviderDisabled... provider: "+provider);
            }  
            @Override  
            public void onLocationChanged(Location location) {  
                Log.i(TAG, "onLocationChanged... latitude: "+location.getLatitude()+", Longitude"+location.getLongitude());
            }  
        };  
        locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locListener);  

    }
}
