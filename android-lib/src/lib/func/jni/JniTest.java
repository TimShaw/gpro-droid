package lib.func.jni;

import android.util.Log;

/**
 * @ClassName:  JniTest.java
 * @Description: 
 * @Author JinChao
 * @Date 2013-7-11 下午4:39:12
 * @Copyright: 版权由 HundSun 拥有
 */
public class JniTest
{
    private static String TAG = JniTest.class.getSimpleName();
    private native int fnwindll(); 
    public native String getLine(String line);
    static{
          System.loadLibrary("JniTest");       
    }
    
    public static String staticString(String value){
        Log.i(TAG," callstatic  from native callback .... value: "+value);
        return value;
    }
    public static int staticInt(){
        return 1000;
    }
    
    
    public static native float testFloat(float value);
    
    public static native double testDouble(double value);
    
    public static native int testInt(int value);
    
    public static native char testChar(char value);
    
    public static native short testShort(short value);
    
    public static native boolean testBoolean(boolean value);
    
    public static native long testLong(long value);
    
    public static native String testString(String value);
    
    public static native String[] testStringArray(String[] value);
    
    public static native int[] testIntArray(int[] value);
    
    public static native Person testObject(Person value);
    
    public void callback(int notify_id,Person person){
        Log.i(TAG,"JNI callback  from native   ..... id :"+notify_id+", person name: "+person.getName());
    }
    
    public static native  void testException() throws InstantiationException;
    
    public void testObjectFromNative(Person person) throws Exception{
        Log.i(TAG," person name : "+person.getName()); 
    }
}
