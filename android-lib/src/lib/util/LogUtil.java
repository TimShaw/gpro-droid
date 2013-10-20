package lib.util;

import lib.LibConstants;
import android.util.Log;

public class LogUtil {

	private static final boolean DEBUG = true;
	
	public static void v(Object object, String text) {
		if (DEBUG) {
			if (object != null) {
				Log.v(LibConstants.LOG_PREFIX+object.getClass().getSimpleName(), text);
			}else {
				Log.v("", text);
			}
		}
	}
	public static void v(String str,String text){
		Log.v(LibConstants.LOG_PREFIX+str, text);
	}
	public static void d(Object object, String text) {
		if (DEBUG) {
			if (object != null) {
				Log.d(LibConstants.LOG_PREFIX+object.getClass().getSimpleName(), text);
			}else {
				Log.d("", text);
			}
		}
	}
	public static void d(String str,String text){
		Log.d(LibConstants.LOG_PREFIX+str, text);
	}
	public static void i(Object object, String text) {
		if (DEBUG) {
			if (object != null) {
				Log.i(LibConstants.LOG_PREFIX+object.getClass().getSimpleName(), text);
			}else {
				Log.i("", text);
			}
		}
	}
	public static void i(String str,String text){
		Log.i(LibConstants.LOG_PREFIX+str, text);
	}
	public static void w(Object object, String text) {
		if (DEBUG) {
			if (object != null) {
				Log.w(LibConstants.LOG_PREFIX+object.getClass().getSimpleName(), text);
			}else {
				Log.w("", text);
			}
		}
	}
	public static void w(String str,String text){
		Log.w(LibConstants.LOG_PREFIX+str, text);
	}
	public static void e(Object object, String text) {
		if (DEBUG) {
			if (object != null) {
				Log.e(LibConstants.LOG_PREFIX+object.getClass().getSimpleName(), text);
			}else {
				Log.e("", text);
			}
		}
	}
	public static void e(String str,String text){
		Log.e(LibConstants.LOG_PREFIX+str, text);
	}
}
