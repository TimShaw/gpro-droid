package lib.func.looper;

import java.util.Random;

import android.lib.R;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.MessageQueue.IdleHandler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MessageQueue_TestActivity extends Activity
{

    private DownloadThread            mDownloadThread;
    private Handler                   mHandler;

    // 进度条
    private ProgressBar               progressBar;
    // 任务状态提示信息
    private TextView                  statusText;
    // 添加任务按钮
    private Button                    scheduleButton;

    private MessageQueue_TestActivity me = this;
    
    Handler handler;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.looper_layout);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        statusText = (TextView) findViewById(R.id.status_text);
        scheduleButton = (Button) findViewById(R.id.schedule_button);

        // 初始化DownloadThread进程
        mDownloadThread = new DownloadThread(this, new DownloadThreadListener()
        {

            @Override
            public void handleDownloadThreadUpdate()
            {
                // TODO Auto-generated method stub

                // 由于需要修改进度条来展示下载进度
                // 所以使用handler把执行UI的操作发送大UI线程中执行
                mHandler.post(new Runnable()
                {

                    @Override
                    public void run()
                    {
                        // TODO Auto-generated method stub
                        int total = mDownloadThread.getTotalQueued();
                        int completed = mDownloadThread.getTotalCompleted();

                        // 设置最好的任务数
                        progressBar.setMax(total);

                        progressBar.setProgress(0);
                        // 设置已完成任务数
                        progressBar.setProgress(completed);

                        // 设置下载状态提示信息
                        statusText.setText(String.format("Downloaded %d/%d", completed, total));

                        // // 下载完毕，使用震动提示
                        // if (completed == total) {
                        // ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(100);
                        // }

                    }
                });

            }
        });
        mDownloadThread.start();

        // 创建mHandler，不需要指定looper
        // mHandler会自动绑定到UI Thread的Looper
        mHandler = new Handler();

        scheduleButton.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                // 随机生成任务数据
                int totalTasks = new Random().nextInt(3) + 1;
                for (int i = 0; i < totalTasks; ++i)
                {
                    // 添加任务
                    mDownloadThread.enqueueDownload(new Download());
                }

            }
        });

        /** 添加IdleHandler **/
        Looper.myQueue().addIdleHandler(new IdleHandler()
        {

            @Override
            public boolean queueIdle()
            {
                Toast.makeText(me, String.format("queueIdle %d ", new Random().nextInt(20000)), Toast.LENGTH_LONG).show();
                return true;
            }
        });
        
        
        /** HandlerThread 例子  **/
        Button handlerThreadButton = (Button)findViewById(R.id.handlerThread);
        handlerThreadButton.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                /* 使用 HandlerThread 消息循环线程 处理任务*/
                final Runnable runnable = new ProcessRunnable();
                
                HandlerThread handlerThread = new HandlerThread("ProcessBar Thread...");
                handlerThread.start();
                
                handler = new Handler(handlerThread.getLooper()){

                    @Override
                    public void handleMessage(Message msg)
                    {
                        Integer process = (Integer)msg.what;
                        progressBar.setProgress(process);
                        handler.post(runnable);
                        if(process>=90){
                            handler.removeCallbacks(runnable);
                        }
                    }
                };
                
                handler.post(runnable);
            }
        });
        
        

    }
    
    class ProcessRunnable implements Runnable
    {
        int i =0 ;
        @Override
        public void run()
        {
            Message message = Message.obtain();
            message.what = i;
            i+=5;
            handler.sendMessage(message);
            try
            {
                Thread.sleep(1000);
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy()
    {
        // TODO Auto-generated method stub
        super.onDestroy();
        // 停止任务线程
        mDownloadThread.requestStop();
    }

}
