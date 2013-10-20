package lib.util;

public class ByteUtil {
	public static int signedByteToInt(byte b){
		return (int) b & 0xFF;  
	}
}
