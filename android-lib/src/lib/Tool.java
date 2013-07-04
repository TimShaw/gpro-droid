package lib;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.util.Log;

/**
 * @ClassName:  Tool.java
 * @Description: 
 * @Author JinChao
 * @Date 2013-7-1 下午2:23:40
 * @Copyright: 版权由 HundSun 拥有
 */
public class Tool
{
    private final static String TAG = Tool.class.getSimpleName();
    
    private static String _dataDir;
    
    
    public static String getDataDir (Context ctx){
        if(_dataDir==null){
            _getDataDir(ctx);
        }
        return _dataDir;
    }
    
    private static void _getDataDir(Context ctx){
        PackageManager m = ctx.getPackageManager();
        String dataDir = ctx.getPackageName();
        try {
            PackageInfo p = m.getPackageInfo(dataDir, 0);
            _dataDir = p.applicationInfo.dataDir;
        } catch (NameNotFoundException e) {
            Log.w("yourtag", "Error Package name not found ", e);
        }
        Log.i(TAG, "dataDir: "+_dataDir);
    }
    
    public static void copyAssets(Context ctx) {
        AssetManager assetManager = ctx.getAssets();
        String[] files = null;
        try {
            files = assetManager.list("");
        } catch (IOException e) {
            Log.e("tag", "Failed to get asset file list.", e);
        }
        for(String filename : files) {
            Log.i(TAG, "filename: "+filename);
            InputStream in = null;
            OutputStream out = null;
            try {
              in = assetManager.open(filename);
              File outFile = new File(Tool.getDataDir(ctx) , filename);
              out = new FileOutputStream(outFile);
              copyFile(in, out);
              in.close();
              in = null;
              out.flush();
              out.close();
              out = null;
            } catch(IOException e) {
                Log.e("tag", "Failed to copy asset file: " + filename, e);
            }       
        }
    }
    
    
    private static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
          out.write(buffer, 0, read);
        }
    }
    public static List<String> listFile(Context ctx,String assetDir){
        String[] files;
        List<String> listFiles = new ArrayList<String>();
        try
        {
            files = ctx.getAssets().list(assetDir);
            for(int j=0,_len=files.length;j<_len;j++){
                File file = new File(files[j]);
                listFile(file,ctx,assetDir,listFiles);
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return listFiles;
    }
    
    private static void listFile(File file,Context context,String relativePath,List<String> listFiles){
        String filepath = relativePath+file.getAbsolutePath();
        String[] files;
        try
        {
            files = context.getResources().getAssets().list(filepath);
            
            if(files!=null && files.length>0){
                if(files!=null){
                    for(int i=0,len=files.length;i<len;i++){
                        File _file = new File(files[i]);
                        listFile(_file,context,relativePath+File.separator+file.getName(),listFiles);
                    }
                }
            //File[] files = file.listFiles();
            
            }else{
                InputStream fis = null;
                try
                {
                    /*fis = context.getResources().getAssets().open(filepath);
                    byte[] b = new byte[256];
                    int fileSize = 0;
                    int count=0;
                    while((count=fis.read(b))!=-1){
                        fileSize +=count;
                    }*/
                    listFiles.add(filepath);
                    Log.i(TAG," filepath: "+filepath);
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            
            
        } catch (IOException e1)
        {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        
    }
}
