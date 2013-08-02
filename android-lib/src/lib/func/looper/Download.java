package lib.func.looper;

import java.util.Random;

import android.util.Log;

public class Download  {
        
        //Returns the simple name of the class represented by this Class as defined in the source code
        private static final String TAG = Download.class.getSimpleName();
        
        //定义Random，用于产生随机数
        private static final Random RANDOM = new Random();
        
        //定义休眠的时间，秒数
        private int lengthSec;
        
        //在构造函数中（任务创建时），初始化休眠时间
        public Download() {
                lengthSec = RANDOM.nextInt(3) + 1;
        }

        public void run() {
                // TODO Auto-generated method stub
                try {
                        //Causes the thread which sent this message to sleep 
                        //for the given interval of time (given in milliseconds). 
                        Thread.sleep(lengthSec * 1000);
                } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        Log.e(TAG, "Error in DownloadTask");
                }

        }

}