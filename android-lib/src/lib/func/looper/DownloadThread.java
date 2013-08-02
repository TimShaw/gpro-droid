package lib.func.looper;

import java.util.Random;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class DownloadThread extends Thread
{

    // 去的当前class的名称
    private static final String    TAG = DownloadThread.class.getSimpleName();

    // 使用handler发送消息
    private Handler                mHandler;

    // 当前下载任务总数
    private int                    totalQueued;
    // 已经完成任务数
    private int                    totalCompleted;

    // 完成任务时的回调
    private DownloadThreadListener mDownloadThreadListener;
    
    private Context ctx;

    // 构造函数，创建DownloadThread时需要设置回调
    public DownloadThread(Context ctx,DownloadThreadListener listener)
    {
        this.mDownloadThreadListener = listener;
        this.ctx = ctx;
    }

    @Override
    public void run()
    {
        // TODO Auto-generated method stub

        try
        {
            // 在当前线程中调用Looper的prepare()方法
            // 必须要显示调用的Looper的prepare()方法
            Looper.prepare();

            Log.i(TAG, "DownloadThread entering the loop");

            // 初始化Handler
            // handler会自动绑定到当前线程的Looper
            // 不需要显示地设置handler的Looper
            mHandler = new Handler(){

                @Override
                public void handleMessage(Message msg)
                {
                    Toast.makeText(ctx, String.valueOf(msg.what), Toast.LENGTH_LONG).show();
                }
                
            };

            // 调用Looper的loop()方法之后，一个消息循环就建立
            // 只有遇到错误或者调用了quit()方法之后才会退出
            Looper.loop();

            Log.i(TAG, "DownloadThread exiting gracefully");

        } catch (Throwable t)
        {
            // TODO: handle exception

            Log.e(TAG, "DownloadThread halted due to an error", t);
        }

    }

    // 停止消息循环
    // 这个方法可以在任何线程中调用
    public synchronized void requestStop()
    {

        // 使用handler的post方法执行Runnable
        // 在Runnable中执行Looper的 quit()，之后DownloadThread的loop退出
        // 很显然，这个Runnable是插入在任务队列末尾的
        // 也就是说只有它在之前任务执行完毕之后，才会退出loop
        mHandler.post(new Runnable()
        {

            @Override
            public void run()
            {
                // TODO Auto-generated method stub

                // 由于使用handler发送消息到DownloadThread线程，
                // 能够确定这肯定是在DownloadThread执行，
                // 所以可以直接使用myLooper() 获取当前线程的Loop
                Looper.myLooper().quit();

                Log.i(TAG, "DownloadThread loop quitting by request");

            }
        });
    }

    public synchronized void enqueueDownload(final Download task)
    {
        mHandler.sendEmptyMessage(new Random().nextInt(20000));
        mHandler.post(new Runnable()
        {

            @Override
            public void run()
            {
                // TODO Auto-generated method stub

                try
                {
                    task.run();
                } finally
                {

                    // 注册下载任务
                    // synchronized (DownloadThread.this) {
                    totalCompleted++;
                    // }
                    // 通知listener
                    signalUpdate();
                }
            }
        });
        totalQueued++;
        // 增加任务之后通知listener
        signalUpdate();
    }

    public synchronized int getTotalQueued()
    {
        // 队列中任务数
        return totalQueued;
    }

    public synchronized int getTotalCompleted()
    {
        // 任务总数
        return totalCompleted;
    }

    // 注意这个方法只能在DownloadThread中调用
    // 所以如果执行与UI相关的代码需要通过Handler的post方法
    // 否则的话，会有异常抛出。
    // 使用方法如：enqueueDownload
    private void signalUpdate()
    {
        if (mDownloadThreadListener != null)
        {
            mDownloadThreadListener.handleDownloadThreadUpdate();
        }
    }
}
