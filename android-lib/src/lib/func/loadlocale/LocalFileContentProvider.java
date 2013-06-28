package lib.func.loadlocale;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;

public class LocalFileContentProvider extends ContentProvider {
	private static final String URI_PREFIX = "content://lib.func.loadlocale";
	private final String TAG = LocalFileContentProvider.class.getSimpleName();

	//private static final String URI_PREFIX = "content://com.andych008.demo.webview";  
    
    @Override  
    public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {  
          
        Log.i(TAG,"openFile path:" + uri.getPath()+" , mode: "+mode);  
        File file = new File(uri.getPath());  
        ParcelFileDescriptor parcel = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);  
        return parcel;  
          
    }  
      
    @Override  
    public AssetFileDescriptor openAssetFile (Uri uri, String mode) throws FileNotFoundException{  
        AssetManager am = getContext().getAssets();    
        String path = uri.getPath().substring(1);    
        Log.i(TAG," path: "+path+" , mode: "+mode);  
          
        //sdcard里有没有  
        String tpath = "/sdcard/"+path;  
        File file = new File(tpath);  
        if (file.exists()) {  
            Log.i(TAG," path2: " + tpath);  
            Uri turi = Uri.parse(URI_PREFIX+tpath);  
            return super.openAssetFile(turi, mode);  
        }  
          
        //C盘有没有  
        /*tpath = "/data/data/com.andych008.demo.webview/andych008/"+path;  
        file = new File(tpath);  
        if (file.exists()) {  
            Log.e("path2:", tpath);  
            Uri turi = Uri.parse(URI_PREFIX+tpath);  
            return super.openAssetFile(turi, mode);  
        }  */
          
        try {  
            AssetFileDescriptor afd = am.openFd(path);  
            return afd;  
        } catch (IOException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
        return super.openAssetFile(uri, mode);  
    }  
  
    @Override  
    public boolean onCreate() {  
        return true;  
    }  
  
    @Override  
    public int delete(Uri uri, String s, String[] as) {  
        throw new UnsupportedOperationException("Not supported by this provider");  
    }  
  
    @Override  
    public String getType(Uri uri) {  
        throw new UnsupportedOperationException("Not supported by this provider");  
    }  
  
    @Override  
    public Uri insert(Uri uri, ContentValues contentvalues) {  
        throw new UnsupportedOperationException("Not supported by this provider");  
    }  
  
    @Override  
    public Cursor query(Uri uri, String[] as, String s, String[] as1, String s1) {  
        throw new UnsupportedOperationException("Not supported by this provider");  
    }  
  
    @Override  
    public int update(Uri uri, ContentValues contentvalues, String s, String[] as) {  
        throw new UnsupportedOperationException("Not supported by this provider");  
    } 
}
