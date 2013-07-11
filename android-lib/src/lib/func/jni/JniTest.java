package lib.func.jni;

/**
 * @ClassName:  JniTest.java
 * @Description: 
 * @Author JinChao
 * @Date 2013-7-11 下午4:39:12
 * @Copyright: 版权由 HundSun 拥有
 */
public class JniTest
{
    private native int fnwindll(); 
    private native String getLine(String line);
    static{
          System.loadLibrary("JniTest");       
    }
    
    public static String staticString(String value){
        System.out.println(" callstatic  from native callback .... value: "+value);
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
    
    public void callback(int notify_id,int param){
        System.out.println("JNI callback  from native callback ..... id :"+notify_id+", param: "+param);
    }
    
    public static native  void testException() throws InstantiationException;
    
    public void testObjectFromNative(Person person) throws Exception{
       System.out.println(" person name : "+person.getName()); 
    }
}
