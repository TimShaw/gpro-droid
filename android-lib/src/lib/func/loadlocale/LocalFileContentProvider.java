package lib.func.loadlocale;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import lib.Tool;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;

/**
 * 先调用 openAssetFile ，若文件不存在再调用 openFile
 * 
 * 
 * 要使得js文件从assets加载，需要将后缀命名为以下非压缩后缀名：
        static const char* kNoCompressExt[] = {
        ".jpg", ".jpeg", ".png", ".gif",
        ".wav", ".mp2", ".mp3", ".ogg", ".aac",
        ".mpg", ".mpeg", ".mid", ".midi", ".smf", ".jet",
        ".rtttl", ".imy", ".xmf", ".mp4", ".m4a",
        ".m4v", ".3gp", ".3gpp", ".3g2", ".3gpp2",
        ".amr", ".awb", ".wma", ".wmv"
        };
        
   另一种方法：则可以从app data或sdcard中读取     
 * 
 * @author jinchao
 *
 */
public class LocalFileContentProvider extends ContentProvider {
	private static final String URI_PREFIX = "content://lib.func.loadlocale";
	private final String TAG = LocalFileContentProvider.class.getSimpleName();

	//private static final String URI_PREFIX = "content://com.andych008.demo.webview";  
    
    @Override  
    public AssetFileDescriptor openAssetFile (Uri uri, String mode) throws FileNotFoundException{  
        AssetManager am = getContext().getAssets();    
        String path = uri.getPath().substring(1);    
        Log.i(TAG," openAssetFile: "+path+" , mode: "+mode);  
          
        //sdcard里有没有
        /*String sdcardAbsPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/";
        if(path.contains("sdcard")){
            String _path = path.substring(7);
            String filePath = sdcardAbsPath+_path;  
            File file = new File(filePath);  
            Log.i(TAG," filePath: " + filePath);  
            if (file.exists()) {
                Log.i(TAG,"");
                Uri turi = Uri.parse(URI_PREFIX+filePath);  
                return super.openAssetFile(turi, mode);  
            }  
        }*/
        
          
        /* 先检查 app data目录 是否存在*/
        String dataDir = Tool.getDataDir(this.getContext());
        String dataFilePath = dataDir+"/"+path;  
        File dataFile = new File(dataFilePath);  
        if (dataFile.exists()) {  
            Log.i(TAG, "dataFilePath:"+dataFilePath);  
            Uri _uri = Uri.parse(URI_PREFIX+dataFilePath);  
            return super.openAssetFile(_uri, mode);  
        }
        
        if (path.endsWith(".js"))
        {
            String strName = path.substring(0, path.length() - 3);
            path = strName + ".mp3";
        }else if(path.endsWith(".css")){
            String strName = path.substring(0, path.length() - 4);
            path = strName + ".mp3";
        }
        
        /* 接着检查 assets目录 是否存在*/
        try {  
            AssetFileDescriptor afd = am.openFd(path);  
            return afd;  
        } catch (IOException e) { 
            Log.e(TAG, "openFd  IOException...");
            e.printStackTrace();  
        }  
        
        
        /*try
        {
            String[] jsArray = am.list("www/js");
            if(jsArray!=null){
                for(String jsStr :jsArray){
                    Log.i(TAG, "jsStr:"+jsStr);
                    File file = new File("www/js/"+jsStr);
                    Log.i(TAG, "length:"+file.length());
                    if(path.contains(jsStr)){
                        
                    }
                }
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }*/
       /* File file = new File(path);
        Uri _uri = Uri.fromFile(file);*/
        //Log.i(TAG, "file length: "+ file.length()+" _uri:"+_uri.getPath());
        return super.openAssetFile(uri, mode);  
    }
    
    
    @Override  
    public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {  
        String path = uri.getPath().substring(1);  
        Log.i(TAG,"openFile path:" + path+" , mode: "+mode);  
        File file = new File(path);  
        ParcelFileDescriptor parcel = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);  
        return parcel;  
          
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
