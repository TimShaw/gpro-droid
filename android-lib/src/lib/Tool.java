package lib;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Vibrator;
import android.util.Log;

/**
 * @ClassName: Tool.java
 * @Description:
 * @Author JinChao
 * @Date 2013-7-1 下午2:23:40
 * @Copyright: 版权由 HundSun 拥有
 */
public class Tool
{

    private final static String TAG = Tool.class.getSimpleName();

    private static String       _dataDir;
    
    private static boolean isFirstRun;
    
    public static boolean isFirstRun(){
    	return isFirstRun;
    }
    public static void setFirstRun(){
    	isFirstRun = true;
    }

    public static String getDataDir(Context ctx)
    {
        if (_dataDir == null)
        {
            _getDataDir(ctx);
        }
        return _dataDir;
    }

    private static void _getDataDir(Context ctx)
    {
        PackageManager m = ctx.getPackageManager();
        String dataDir = ctx.getPackageName();
        try
        {
            PackageInfo p = m.getPackageInfo(dataDir, 0);
            _dataDir = p.applicationInfo.dataDir;
        } catch (NameNotFoundException e)
        {
            Log.w("yourtag", "Error Package name not found ", e);
        }
        Log.i(TAG, "dataDir: " + _dataDir);
    }

    public static void copyAssets(Context ctx)
    {
        AssetManager assetManager = ctx.getAssets();
        String[] files = null;
        try
        {
            files = assetManager.list("");
        } catch (IOException e)
        {
            Log.e("tag", "Failed to get asset file list.", e);
        }
        for (String filename : files)
        {
            Log.i(TAG, "filename: " + filename);
            InputStream in = null;
            OutputStream out = null;
            try
            {
                in = assetManager.open(filename);
                File outFile = new File(Tool.getDataDir(ctx), filename);
                out = new FileOutputStream(outFile);
                copyFile(in, out);
                in.close();
                in = null;
                out.flush();
                out.close();
                out = null;
            } catch (IOException e)
            {
                Log.e("tag", "Failed to copy asset file: " + filename, e);
            }
        }
    }

    private static void copyFile(InputStream in, OutputStream out) throws IOException
    {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1)
        {
            out.write(buffer, 0, read);
        }
    }

    public static List<String> listFile(Context ctx, String assetDir)
    {
        String[] files;
        List<String> listFiles = new ArrayList<String>();
        try
        {
            files = ctx.getAssets().list(assetDir);
            for (int j = 0, _len = files.length; j < _len; j++)
            {
                File file = new File(files[j]);
                listFile(file, ctx, assetDir, listFiles);
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return listFiles;
    }

    private static void listFile(File file, Context context, String relativePath, List<String> listFiles)
    {
        String filepath = relativePath + file.getAbsolutePath();
        String[] files;
        try
        {
            files = context.getResources().getAssets().list(filepath);

            if (files != null && files.length > 0)
            {
                if (files != null)
                {
                    for (int i = 0, len = files.length; i < len; i++)
                    {
                        File _file = new File(files[i]);
                        listFile(_file, context, relativePath + File.separator + file.getName(), listFiles);
                    }
                }
                // File[] files = file.listFiles();

            } else
            {
                InputStream fis = null;
                try
                {
                    /*
                     * fis = context.getResources().getAssets().open(filepath); byte[] b = new byte[256]; int fileSize =
                     * 0; int count=0; while((count=fis.read(b))!=-1){ fileSize +=count; }
                     */
                    listFiles.add(filepath);
                    Log.i(TAG, " filepath: " + filepath);
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

    public static synchronized void getMemoryInfo(Context context)
    {
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();

        // 通过getMemoryInfo（）方法，获取内存信息
        mActivityManager.getMemoryInfo(memoryInfo);
        Log.i(TAG, " memoryInfo.availMem " + memoryInfo.availMem + "\n");
        Log.i(TAG, " memoryInfo.lowMemory " + memoryInfo.lowMemory + "\n");
        Log.i(TAG, " memoryInfo.threshold " + memoryInfo.threshold + "\n");

        // 通过getRunningAppProcesses（）方法，获取正在运行中的进程的信息
        List<RunningAppProcessInfo> runningAppProcesses = mActivityManager.getRunningAppProcesses();
        for (RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses)
        {
            int pids[] = new int[1];
            pids[0] = runningAppProcessInfo.pid;

            android.os.Debug.MemoryInfo[] memoryInfos = mActivityManager.getProcessMemoryInfo(pids);
            for (android.os.Debug.MemoryInfo pidMemoryInfo : memoryInfos)
            {
                Log.i(TAG, String.format("** MEMINFO in pid %d [%s] **\n", runningAppProcessInfo.pid,
                                         runningAppProcessInfo.processName));
                Log.i(TAG, " pidMemoryInfo.getTotalPrivateDirty(): " + pidMemoryInfo.getTotalPrivateDirty() + "\n");
                Log.i(TAG, " pidMemoryInfo.getTotalPss(): " + pidMemoryInfo.getTotalPss() + "\n");
                Log.i(TAG, " pidMemoryInfo.getTotalSharedDirty(): " + pidMemoryInfo.getTotalSharedDirty() + "\n");
            }
        }
    }

    /**
     *  判断service是否运行
     * @param serviceName
     * @param context
     * @return
     */
    
    public static Boolean isServiceRunning(String serviceName, Context context)
    {

        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        List<RunningServiceInfo> runningServiceInfos = mActivityManager.getRunningServices(Integer.MAX_VALUE);
        for (RunningServiceInfo runningServiceInfo : runningServiceInfos)
        {
            Log.i(TAG, "runningServiceInfo.service.getClassName():" + runningServiceInfo.service.getClassName() + "\n");
            if (serviceName.equals(runningServiceInfo.service.getClassName()))
            {
                return true;
            }
        }
        return false;
    }
    
    public static void vibrate(Context ctx, int t){    
        Vibrator v = (Vibrator) ctx.getSystemService(Context.VIBRATOR_SERVICE);    
        v.vibrate(t);  
    }
    
    
    /**
	 * Check whether the specific string is ip address
	 * @param str
	 * @return
	 */
	public synchronized static boolean isIP(String str) {
		Pattern pattern = Pattern.compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])" +
				"\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\." +
				"((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\." +
				"((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");
		return pattern.matcher(str).matches();
	}

	public synchronized static boolean isMAC(String str) {

		str = str.trim();
		if (str.length() != 12) {
			return false;
		}

		char[] chars = new char[12];
		str.getChars(0, 12, chars, 0);
		for (int i = 0; i < chars.length; i++) {
			if (!((chars[i]>='0' && chars[i]<='9') || (chars[i]>='A' && chars[i]<='F') || (chars[i]>='a' && chars[i]<='f'))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * For custom purposes. Not used by ColorPickerPreferrence
	 * @param color
	 * @author Unknown
	 */
    public static String convertToARGB(int color) {
        String alpha = Integer.toHexString(Color.alpha(color));
        String red = Integer.toHexString(Color.red(color));
        String green = Integer.toHexString(Color.green(color));
        String blue = Integer.toHexString(Color.blue(color));

        if (alpha.length() == 1) {
            alpha = "0" + alpha;
        }

        if (red.length() == 1) {
            red = "0" + red;
        }

        if (green.length() == 1) {
            green = "0" + green;
        }

        if (blue.length() == 1) {
            blue = "0" + blue;
        }

        return "#" + alpha + red + green + blue;
    }

    /**
	 * For custom purposes. Not used by ColorPickerPreference
	 * @param color
	 * @author Charles Rosaaen
	 * @return A string representing the hex value of color,
	 * without the alpha value
	 */
    public static String convertToRGB(int color) {
        String red = Integer.toHexString(Color.red(color));
        String green = Integer.toHexString(Color.green(color));
        String blue = Integer.toHexString(Color.blue(color));

        if (red.length() == 1) {
            red = "0" + red;
        }

        if (green.length() == 1) {
            green = "0" + green;
        }

        if (blue.length() == 1) {
            blue = "0" + blue;
        }

        return "#" + red + green + blue;
    }

    /**
     * For custom purposes. Not used by ColorPickerPreferrence
     * @param argb
     * @throws NumberFormatException
     * @author Unknown
     */
    public static int convertToColorInt(String argb) throws NumberFormatException {

    	if (argb.startsWith("#")) {
    		argb = argb.replace("#", "");
    	}

        int alpha = -1, red = -1, green = -1, blue = -1;

        if (argb.length() == 8) {
            alpha = Integer.parseInt(argb.substring(0, 2), 16);
            red = Integer.parseInt(argb.substring(2, 4), 16);
            green = Integer.parseInt(argb.substring(4, 6), 16);
            blue = Integer.parseInt(argb.substring(6, 8), 16);
        }
        else if (argb.length() == 6) {
            alpha = 255;
            red = Integer.parseInt(argb.substring(0, 2), 16);
            green = Integer.parseInt(argb.substring(2, 4), 16);
            blue = Integer.parseInt(argb.substring(4, 6), 16);
        }
        else
        	throw new NumberFormatException("string " + argb + "did not meet length requirements");

        return Color.argb(alpha, red, green, blue);
    }
}
