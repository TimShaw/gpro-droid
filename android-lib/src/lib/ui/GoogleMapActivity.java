package lib.ui;

import android.lib.R;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * @ClassName:  GoogleMapActivity.java
 * @Description: 
 * @Author JinChao
 * @Date 2013-5-9 下午4:48:05
 */
public class GoogleMapActivity extends FragmentActivity  {
    private GoogleMap mMap;
    
    
    static final LatLng MELBOURNE = new LatLng(28.367443083801006, 121.39126539230346);
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.googlemap);
        
        mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
       /* double x = 121.39126539230346;
        double y = 28.367443083801006;
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(x, y))
                .title("Hello world"));*/
        
        Marker melbourne = mMap.addMarker(new MarkerOptions()
        .position(MELBOURNE));
        
        
    }
}
