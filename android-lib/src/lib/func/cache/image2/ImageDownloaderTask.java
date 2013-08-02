package lib.func.cache.image2;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Comparator;

import lib.Tool;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

public class ImageDownloaderTask extends AsyncTask<String, Void, Bitmap>
{

    private static String                       TAG                           = ImageDownloaderTask.class.getSimpleName();
    private static final int                    IO_BUFFER_SIZE                = 4 * 1024;
    private static final int                    MB                            = 1024 * 1024;
    private static final int                    CACHE_SIZE                    = 1024 * 1024;
    private static final int                    mTimeDiff                     = 5 * 24 * 60 * 60 * 1000;
    private static final int                    FREE_SD_SPACE_NEEDED_TO_CACHE = 30;
    private final String                        fileCachePath;
    private String                              url;
    private final WeakReference<AsyncListImageActivity> activityReference;

    public ImageDownloaderTask(AsyncListImageActivity activity)
    {
        activityReference = new WeakReference<AsyncListImageActivity>(activity);
        fileCachePath    = Tool.getDataDir(activity)+"/files/caches";
    }

    @Override
    protected Bitmap doInBackground(String... params)
    {
        url = params[0];
        String filename = convertUrlToFileName(url);
        
        /* 先从 文件系统中 加载图片 */
        File file = new File(fileCachePath + "/" + filename);
        if (file.exists())
        {
            //removeExpiredCache(fileCachePath, filename);
            updateFileTime(fileCachePath, filename);
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            if (bitmap != null) 
                return bitmap;
        }

        /* 若本地文件中不存在，则从网络中加载  */
        final DefaultHttpClient client = new DefaultHttpClient();

        final HttpGet getRequest = new HttpGet(url);
        try
        {
            HttpResponse response = client.execute(getRequest);
            final int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK)
            {
                Log.w(TAG, "从" + url + "中下载图片时出错!,错误码:" + statusCode);
                return null;
            }
            final HttpEntity entity = response.getEntity();
            if (entity != null)
            {
                InputStream inputStream = null;
                OutputStream outputStream = null;
                try
                {
                    inputStream = entity.getContent();
                    final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
                    outputStream = new BufferedOutputStream(dataStream, IO_BUFFER_SIZE);
                    copy(inputStream, outputStream);
                    outputStream.flush();
                    final byte[] data = dataStream.toByteArray();
                    final Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

                    saveBmpToSd(bitmap, url);

                    return bitmap;
                } finally
                {
                    closeStream(inputStream);
                    closeStream(outputStream);
                    entity.consumeContent();
                }
            }
        } catch (IOException e)
        {
            getRequest.abort();
            Log.w(TAG, "I/O error while retrieving bitmap from " + url, e);
        } catch (IllegalStateException e)
        {
            getRequest.abort();
            Log.w(TAG, "Incorrect URL:" + url);
        } catch (Exception e)
        {
            getRequest.abort();
            Log.w(TAG, "Error while retrieving bitmap from " + url, e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap result)
    {
        super.onPostExecute(result);
        AsyncListImageActivity act = activityReference.get();
        if (act != null && result != null)
        {
            act.onComplete(url, result);
        }
    }

    /**
     * Copy the content of the input stream into the output stream, using a temporary byte array buffer whose size is
     * defined by {@link #IO_BUFFER_SIZE}.
     * 
     * @param in The input stream to copy from.
     * @param out The output stream to copy to.
     * @throws java.io.IOException If any error occurs during the copy.
     */
    public static void copy(InputStream in, OutputStream out) throws IOException
    {
        byte[] b = new byte[IO_BUFFER_SIZE];
        int read;
        while ((read = in.read(b)) != -1)
        {
            out.write(b, 0, read);
        }
    }

    /**
     * Closes the specified stream.
     * 
     * @param stream The stream to close.
     */
    public static void closeStream(Closeable stream)
    {
        if (stream != null)
        {
            try
            {
                stream.close();
            } catch (IOException e)
            {
                android.util.Log.e(TAG, "Could not close stream", e);
            }
        }
    }

    private void saveBmpToSd(Bitmap bm, String url)
    {
        if (bm == null)
        {
            Log.w(TAG, " trying to savenull bitmap");
            return;
        }
        // 判断sdcard上的空间
        if (FREE_SD_SPACE_NEEDED_TO_CACHE > freeSpaceOnSd())
        {
            Log.w(TAG, "Low free space onsd, do not cache");
            removeCache(fileCachePath);
            return;
        }
        String filename = convertUrlToFileName(url);
        String dir = this.fileCachePath;
        File file = new File(dir + "/" + filename);
        try
        {
            file.createNewFile();
            OutputStream outStream = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            outStream.flush();
            outStream.close();
            Log.i(TAG, "Image saved tosd");
        } catch (FileNotFoundException e)
        {
            Log.w(TAG, "FileNotFoundException");
        } catch (IOException e)
        {
            Log.w(TAG, "IOException");
        }
    }

    private String convertUrlToFileName(String url)
    {
        int lastIndex = url.lastIndexOf('/');
        return url.substring(lastIndex + 1);
    }

    
    /**
     * 计算sdcard上的剩余空间
     * 
     * @return
     */
    private int freeSpaceOnSd()
    {
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        double sdFreeMB = ((double) stat.getAvailableBlocks() * (double) stat.getBlockSize()) / MB;
        return (int) sdFreeMB;
    }

    /**
     * 修改文件的最后修改时间
     * 
     * @param dir
     * @param fileName
     */
    private void updateFileTime(String dir, String fileName)
    {
        File file = new File(dir, fileName);
        long newModifiedTime = System.currentTimeMillis();
        file.setLastModified(newModifiedTime);
    }

    /**
     * 计算存储目录下的文件大小， 当文件总大小大于规定的CACHE_SIZE或者sdcard剩余空间小于FREE_SD_SPACE_NEEDED_TO_CACHE的规定 那么删除40%最近没有被使用的文件
     * 
     * @param dirPath
     * @param filename
     */
    private void removeCache(String dirPath)
    {
        File dir = new File(dirPath);
        File[] files = dir.listFiles();
        if (files == null)
        {
            return;
        }
        int dirSize = 0;
        for (int i = 0; i < files.length; i++)
        {
            File file = files[i];
            Log.i(TAG, "removeCache...."+file.getName());
            if (file.getName().contains(fileCachePath))
            {
                dirSize += file.length();
            }
        }
        if (dirSize > CACHE_SIZE * MB || FREE_SD_SPACE_NEEDED_TO_CACHE > freeSpaceOnSd())
        {
            int removeFactor = (int) ((0.4 * files.length) + 1);

            Arrays.sort(files, new FileLastModifSort());

            Log.i(TAG, "Clear some expiredcache files ");

            for (int i = 0; i < removeFactor; i++)
            {
                File file = files[i];
                if (file.getName().contains(fileCachePath))
                {
                    file.delete();
                }

            }

        }

    }

    /**
     * TODO 根据文件的最后修改时间进行排序 *
     */
    class FileLastModifSort implements Comparator<File>
    {

        public int compare(File arg0, File arg1)
        {
            if (arg0.lastModified() > arg1.lastModified())
            {
                return 1;
            } else if (arg0.lastModified() == arg1.lastModified())
            {
                return 0;
            } else
            {
                return -1;
            }
        }
    }

    /**
     * 删除过期文件
     * 
     * @param dirPath
     * @param filename
     */
    private void removeExpiredCache(String dirPath, String filename)
    {

        File file = new File(dirPath, filename);

        if (System.currentTimeMillis() - file.lastModified() > mTimeDiff)
        {

            Log.i(TAG, "Clear some expiredcache files ");

            file.delete();

        }

    }
}
