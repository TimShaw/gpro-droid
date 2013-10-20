package lib.util;

import java.io.UnsupportedEncodingException;

/**
 * 16进制值与String/Byte之间的转换
 * @author JerryLi
 * @email lijian@dzs.mobi
 * @data 2011-10-16
 * */
public class HexUtil
{
	/** 
	 * 字符串转换成十六进制字符串
	 * @param String str 待转换的ASCII字符串
	 * @return String 每个Byte之间空格分隔，如: [61 6C 6B]
	 */  
    public static String str2HexStr(String str)
    {  

        char[] chars = "0123456789ABCDEF".toCharArray();  
        StringBuilder sb = new StringBuilder("");
        byte[] bs = str.getBytes();  
        int bit;  
        
        for (int i = 0; i < bs.length; i++)
        {  
            bit = (bs[i] & 0x0f0) >> 4;  
            sb.append(chars[bit]);  
            bit = bs[i] & 0x0f;  
            sb.append(chars[bit]);
            sb.append(' ');
        }  
        return sb.toString().trim();  
    }
    
    /** 
     * 十六进制转换字符串
	 * @param String str Byte字符串(Byte之间无分隔符 如:[616C6B])
	 * @return String 对应的字符串
     */  
    public static String hexStr2Str(String hexStr)
    {  
        String str = "0123456789ABCDEF";  
        char[] hexs = hexStr.toCharArray();  
        byte[] bytes = new byte[hexStr.length() / 2];  
        int n;  

        for (int i = 0; i < bytes.length; i++)
        {  
            n = str.indexOf(hexs[2 * i]) * 16;  
            n += str.indexOf(hexs[2 * i + 1]);  
            bytes[i] = (byte) (n & 0xff);  
        }  
        return new String(bytes);  
    }
    
    /**
     * bytes转换成十六进制字符串
     * @param byte[] b byte数组
     * @return String 每个Byte值之间空格分隔
     */
    public static String byte2HexStr(byte[] b)
    {
        String stmp="";
        StringBuilder sb = new StringBuilder("");
        for (int n=0;n<b.length;n++)
        {
            stmp = Integer.toHexString(b[n] & 0xFF);
            sb.append((stmp.length()==1)? "0"+stmp : stmp);
            sb.append(" ");
        }
        return sb.toString().toUpperCase().trim();
    }
    
    /**
     * bytes字符串转换为Byte值
     * @param String src Byte字符串，每个Byte之间没有分隔符
     * @return byte[]
     */
    public static byte[] hexStr2Bytes(String src)
    {
        int m=0,n=0;
        int l=src.length()/2;
        System.out.println(l);
        byte[] ret = new byte[l];
        for (int i = 0; i < l; i++)
        {
            m=i*2+1;
            n=m+1;
            ret[i] = Byte.decode("0x" + src.substring(i*2, m) + src.substring(m,n));
        }
        return ret;
    }

    /**
     * String的字符串转换成unicode的String
     * @param String strText 全角字符串
     * @return String 每个unicode之间无分隔符
     * @throws Exception
     */
    public static String strToUnicode(String strText)
    	throws Exception
    {
        char c;
        StringBuilder str = new StringBuilder();
        int intAsc;
        String strHex;
        for (int i = 0; i < strText.length(); i++)
        {
            c = strText.charAt(i);
            intAsc = (int) c;
            strHex = Integer.toHexString(intAsc);
            if (intAsc > 128)
            	str.append("\\u" + strHex);
            else // 低位在前面补00
            	str.append("\\u00" + strHex);
        }
        return str.toString();
    }
    
    /**
     * unicode的String转换成String的字符串
     * @param String hex 16进制值字符串 （一个unicode为2byte）
     * @return String 全角字符串
     */
    public static String unicodeToString(String hex)
    {
        int t = hex.length() / 6;
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < t; i++)
        {
            String s = hex.substring(i * 6, (i + 1) * 6);
            // 高位需要补上00再转
            String s1 = s.substring(2, 4) + "00";
            // 低位直接转
            String s2 = s.substring(4);
            // 将16进制的string转为int
            int n = Integer.valueOf(s1, 16) + Integer.valueOf(s2, 16);
            // 将int转换为字符
            char[] chars = Character.toChars(n);
            str.append(new String(chars));
        }
        return str.toString();
    }
    
    
    private static final int BASELENGTH = 255;
    private static final int LOOKUPLENGTH = 16;
    private static byte[] hexNumberTable = new byte['ÿ'];
    private static byte[] lookUpHexAlphabet = new byte[16];

    static boolean isHex(byte octect)
    {
      return hexNumberTable[octect] != -1;
    }

    public static String bytesToString(byte[] binaryData)
    {
      if (binaryData == null)
        return null;
      return new String(encode(binaryData));
    }

     

    public static byte[] stringToBytes(String hexEncoded)
    {
      return decode(hexEncoded.getBytes());
    }

    public static byte[] encode(byte[] binaryData)
    {
      if (binaryData == null)
        return null;
      int lengthData = binaryData.length;
      int lengthEncode = lengthData * 2;
      byte[] encodedData = new byte[lengthEncode];
      for (int i = 0; i < lengthData; ++i) {
        encodedData[(i * 2)] = lookUpHexAlphabet[(binaryData[i] >> 4 & 0xF)];
        encodedData[(i * 2 + 1)] = lookUpHexAlphabet[(binaryData[i] & 0xF)];
      }
      return encodedData;
    }

    public static byte[] decode(byte[] binaryData) {
      if (binaryData == null)
        return null;
      int lengthData = binaryData.length;
      if (lengthData % 2 != 0) {
        return null;
      }
      int lengthDecode = lengthData / 2;
      byte[] decodedData = new byte[lengthDecode];
      for (int i = 0; i < lengthDecode; ++i) {
        if ((!isHex(binaryData[(i * 2)])) || (!isHex(binaryData[(i * 2 + 1)]))) {
          return null;
        }
        decodedData[i] = (byte)(hexNumberTable[binaryData[(i * 2)]] << 4 | hexNumberTable[binaryData[(i * 2 + 1)]]);
      }
      return decodedData;
    }

    public static String decode(String binaryData)
    {
      if (binaryData == null) {
        return null;
      }
      byte[] decoded = null;
      try {
        decoded = decode(binaryData.getBytes("utf-8"));
      }
      catch (UnsupportedEncodingException e) {
      }
      return (decoded == null) ? null : new String(decoded);
    }

    public static String encode(String binaryData)
    {
      if (binaryData == null) {
        return null;
      }
      byte[] encoded = null;
      try {
        encoded = encode(binaryData.getBytes("utf-8"));
      } catch (UnsupportedEncodingException e) {
      }
      return (encoded == null) ? null : new String(encoded);
    }

    static
    {
      for (int i = 0; i < 255; ++i) {
        hexNumberTable[i] = -1;
      }
      for (int i = 57; i >= 48; --i) {
        hexNumberTable[i] = (byte)(i - 48);
      }
      for (int i = 70; i >= 65; --i) {
        hexNumberTable[i] = (byte)(i - 65 + 10);
      }
      for (int i = 102; i >= 97; --i) {
        hexNumberTable[i] = (byte)(i - 97 + 10);
      }

      for (int i = 0; i < 10; ++i)
        lookUpHexAlphabet[i] = (byte)(48 + i);
      for (int i = 10; i <= 15; ++i)
        lookUpHexAlphabet[i] = (byte)(65 + i - 10);
    }
    
    
    
    public static void printByteArray(byte[] bytes) {

		System.out.println("\n-----------------");
		for (int i = 0; i < bytes.length; i++) {
			System.out.print("  0x" + Integer.toHexString(bytes[i] & 0xff));
		}
		System.out.println("\n-----------------");
	}
    
    public static int sumHexStr(String hex,int da){
    	int sum=0;
    	String[] datas = hex.split(" ");
		for(int i=0;i<datas.length;i++){
			int d = Integer.parseInt(datas[i], 16);
			sum+=d;
		}
		int i = sum+da;
		String str = Integer.toHexString(i);
		System.out.println(str);
		return i;
    }
    public static int sumByteArray(byte[] bs){
    	int sum=0;
    	for(int i=0;i<bs.length;i++){
    		int _b = ByteUtil.signedByteToInt(bs[i]);
    		sum +=_b;
    	}
    	return sum;
    }
    public static int sumHexStrWithNoSpace(String hex){
    	int sum =0;
    	for(int i=0;i<hex.length();i++){
    		char c = hex.charAt(i);
    		i++;
    		String b = String.valueOf(c)+String.valueOf(hex.charAt(i));
    		sum += Integer.valueOf(b, 16);
    	}
    	return sum;
    } 
}