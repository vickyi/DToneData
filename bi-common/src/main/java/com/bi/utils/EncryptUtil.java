package com.bi.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 功能描述
 * 加密常用类
 */
public class EncryptUtil {
	// 密钥是16位长度的byte[]进行Base64转换后得到的字符串
	public static String key = "LmMGStGtOpF4xNyvYt54EQ==";

	/**利用MD5进行加密
	 　　* @param str  待加密的字符串
	　　* @return  加密后的字符串
	　　* @throws NoSuchAlgorithmException  没有这种产生消息摘要的算法
	　　 * @throws UnsupportedEncodingException  
	　　*/
	public static String EncoderByMd5(String str) throws NoSuchAlgorithmException,UnsupportedEncodingException{
	//确定计算方法
	 MessageDigest md5=MessageDigest.getInstance("MD5");
	 BASE64Encoder base64en = new BASE64Encoder();
	//加密后的字符串
	 String newstr=base64en.encode(md5.digest(str.getBytes("utf-8")));
	return newstr;
     }
	
	/**
	 * 这个md5加密与php默认的加密结果都相同.32位小写
	 * @param plain  明文
	 * @return 32位小写密文
	 */
	public static String encryption(String plain) {
		String re_md5 = new String();
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plain.getBytes());
			byte b[] = md.digest();

			int i;

			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}

			re_md5 = buf.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return re_md5;
	}

	
	/**
	 * <li>
	 * 方法名称:encrypt</li> <li>
	 * 加密方法
	 * @param xmlStr
	 *            需要加密的消息字符串
	 * @return 加密后的字符串
	 */
	public static String encrypt(String xmlStr) {
		byte[] encrypt = null;

		try {
			// 取需要加密内容的utf-8编码。
			encrypt = xmlStr.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		// 取MD5Hash码，并组合加密数组
		byte[] md5Hasn = null;
		try {
			md5Hasn = EncryptUtil.MD5Hash(encrypt, 0, encrypt.length);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 组合消息体
		byte[] totalByte = EncryptUtil.addMD5(md5Hasn, encrypt);

		// 取密钥和偏转向量
		byte[] key = new byte[8];
		byte[] iv = new byte[8];
		getKeyIV(EncryptUtil.key, key, iv);
		SecretKeySpec deskey = new SecretKeySpec(key, "DES");
		IvParameterSpec ivParam = new IvParameterSpec(iv);

		// 使用DES算法使用加密消息体
		byte[] temp = null;
		try {
			temp = EncryptUtil.DES_CBC_Encrypt(totalByte, deskey, ivParam);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 使用Base64加密后返回
		return new BASE64Encoder().encode(temp);
	}

	/**
	 * <li>
	 * 方法名称:encrypt</li> <li>
	 * 功能描述:
	 * 
	 * <pre>
	 * 解密方法
	 * </pre>
	 * 
	 * </li>
	 * 
	 * @param xmlStr
	 *            需要解密的消息字符串
	 * @return 解密后的字符串
	 * @throws Exception
	 */
	public static String decrypt(String xmlStr) throws Exception {
		// base64解码
		BASE64Decoder decoder = new BASE64Decoder();
		byte[] encBuf = null;
		try {
			encBuf = decoder.decodeBuffer(xmlStr);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 取密钥和偏转向量
		byte[] key = new byte[8];
		byte[] iv = new byte[8];
		getKeyIV(EncryptUtil.key, key, iv);

		SecretKeySpec deskey = new SecretKeySpec(key, "DES");
		IvParameterSpec ivParam = new IvParameterSpec(iv);

		// 使用DES算法解密
		byte[] temp = null;
		try {
			temp = EncryptUtil.DES_CBC_Decrypt(encBuf, deskey, ivParam);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 进行解密后的md5Hash校验
		byte[] md5Hash = null;
		try {
			md5Hash = EncryptUtil.MD5Hash(temp, 16, temp.length - 16);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 进行解密校检
		for (int i = 0; i < md5Hash.length; i++) {
			if (md5Hash[i] != temp[i]) {
				// System.out.println(md5Hash[i] + "MD5校验错误。" + temp[i]);
				throw new Exception("MD5校验错误。");
			}
		}

		// 返回解密后的数组，其中前16位MD5Hash码要除去。
		return new String(temp, 16, temp.length - 16, "utf-8");
	}

	/**
	 * <li>
	 * 方法名称:TripleDES_CBC_Encrypt</li> <li>
	 * 功能描述:
	 * 
	 * <pre>
	 * 经过封装的三重DES/CBC加密算法，如果包含中文，请注意编码。
	 * </pre>
	 * 
	 * </li>
	 * 
	 * @param sourceBuf
	 *            需要加密内容的字节数组。
	 * @param deskey
	 *            KEY 由24位字节数组通过SecretKeySpec类转换而成。
	 * @param ivParam
	 *            IV偏转向量，由8位字节数组通过IvParameterSpec类转换而成。
	 * @return 加密后的字节数组
	 * @throws Exception
	 */
	public static byte[] TripleDES_CBC_Encrypt(byte[] sourceBuf,
			SecretKeySpec deskey, IvParameterSpec ivParam) throws Exception {
		byte[] cipherByte;
		// 使用DES对称加密算法的CBC模式加密
		Cipher encrypt = Cipher.getInstance("TripleDES/CBC/PKCS5Padding");

		encrypt.init(Cipher.ENCRYPT_MODE, deskey, ivParam);

		cipherByte = encrypt.doFinal(sourceBuf, 0, sourceBuf.length);
		// 返回加密后的字节数组
		return cipherByte;
	}

	/**
	 * <li>
	 * 方法名称:TripleDES_CBC_Decrypt</li> <li>
	 * 功能描述:
	 * 
	 * <pre>
	 * 经过封装的三重DES / CBC解密算法
	 * </pre>
	 * 
	 * </li>
	 * 
	 * @param sourceBuf
	 *            需要解密内容的字节数组
	 * @param deskey
	 *            KEY 由24位字节数组通过SecretKeySpec类转换而成。
	 * @param ivParam
	 *            IV偏转向量，由6位字节数组通过IvParameterSpec类转换而成。
	 * @return 解密后的字节数组
	 * @throws Exception
	 */
	public static byte[] TripleDES_CBC_Decrypt(byte[] sourceBuf,
			SecretKeySpec deskey, IvParameterSpec ivParam) throws Exception {

		byte[] cipherByte;
		// 获得Cipher实例，使用CBC模式。
		Cipher decrypt = Cipher.getInstance("TripleDES/CBC/PKCS5Padding");
		// 初始化加密实例，定义为解密功能，并传入密钥，偏转向量
		decrypt.init(Cipher.DECRYPT_MODE, deskey, ivParam);

		cipherByte = decrypt.doFinal(sourceBuf, 0, sourceBuf.length);
		// 返回解密后的字节数组
		return cipherByte;
	}

	/**
	 * <li>
	 * 方法名称:DES_CBC_Encrypt</li> <li>
	 * 功能描述:
	 * 
	 * <pre>
	 * 经过封装的DES/CBC加密算法，如果包含中文，请注意编码。
	 * </pre>
	 * 
	 * </li>
	 * 
	 * @param sourceBuf
	 *            需要加密内容的字节数组。
	 * @param deskey
	 *            KEY 由8位字节数组通过SecretKeySpec类转换而成。
	 * @param ivParam
	 *            IV偏转向量，由8位字节数组通过IvParameterSpec类转换而成。
	 * @return 加密后的字节数组
	 * @throws Exception
	 */
	public static byte[] DES_CBC_Encrypt(byte[] sourceBuf,
			SecretKeySpec deskey, IvParameterSpec ivParam) throws Exception {
		byte[] cipherByte;
		// 使用DES对称加密算法的CBC模式加密
		Cipher encrypt = Cipher.getInstance("DES/CBC/PKCS5Padding");

		encrypt.init(Cipher.ENCRYPT_MODE, deskey, ivParam);

		cipherByte = encrypt.doFinal(sourceBuf, 0, sourceBuf.length);
		// 返回加密后的字节数组
		return cipherByte;
	}

	/**
	 * <li>
	 * 方法名称:DES_CBC_Decrypt</li> <li>
	 * 功能描述:
	 * 
	 * <pre>
	 * 经过封装的DES/CBC解密算法。
	 * </pre>
	 * 
	 * </li>
	 * 
	 * @param sourceBuf
	 *            需要解密内容的字节数组
	 * @param deskey
	 *            KEY 由8位字节数组通过SecretKeySpec类转换而成。
	 * @param ivParam
	 *            IV偏转向量，由6位字节数组通过IvParameterSpec类转换而成。
	 * @return 解密后的字节数组
	 * @throws Exception
	 */
	public static byte[] DES_CBC_Decrypt(byte[] sourceBuf,
			SecretKeySpec deskey, IvParameterSpec ivParam) throws Exception {

		byte[] cipherByte;
		// 获得Cipher实例，使用CBC模式。
		Cipher decrypt = Cipher.getInstance("DES/CBC/PKCS5Padding");
		// 初始化加密实例，定义为解密功能，并传入密钥，偏转向量
		decrypt.init(Cipher.DECRYPT_MODE, deskey, ivParam);

		cipherByte = decrypt.doFinal(sourceBuf, 0, sourceBuf.length);
		// 返回解密后的字节数组
		return cipherByte;
	}

	/**
	 * <li>
	 * 方法名称:MD5Hash</li> <li>
	 * 功能描述:
	 * 
	 * <pre>
	 * MD5，进行了简单的封装，以适用于加，解密字符串的校验。
	 * </pre>
	 * 
	 * </li>
	 * 
	 * @param buf
	 *            需要MD5加密字节数组。
	 * @param offset
	 *            加密数据起始位置。
	 * @param length
	 *            需要加密的数组长度。
	 * @return
	 * @throws Exception
	 */
	public static byte[] MD5Hash(byte[] buf, int offset, int length)
			throws Exception {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(buf, offset, length);
		return md.digest();
	}

	/**
	 * <li>
	 * 方法名称:byte2hex</li> <li>
	 * 功能描述:
	 * 
	 * <pre>
	 * 字节数组转换为二行制表示
	 * </pre>
	 * 
	 * </li>
	 * 
	 * @param inStr
	 *            需要转换字节数组。
	 * @return 字节数组的二进制表示。
	 */
	public static String byte2hex(byte[] inStr) {
		String stmp;
		StringBuffer out = new StringBuffer(inStr.length * 2);

		for (int n = 0; n < inStr.length; n++) {
			// 字节做"与"运算，去除高位置字节 11111111
			stmp = Integer.toHexString(inStr[n] & 0xFF);
			if (stmp.length() == 1) {
				// 如果是0至F的单位字符串，则添加0
				out.append("0" + stmp);
			} else {
				out.append(stmp);
			}
		}
		return out.toString();
	}

	/**
	 * <li>
	 * 方法名称:addMD5</li> <li>
	 * 功能描述:
	 * 
	 * <pre>
	 * MD校验码 组合方法，前16位放MD5Hash码。 把MD5验证码byte[]，加密内容byte[]组合的方法。
	 * </pre>
	 * 
	 * </li>
	 * 
	 * @param md5Byte
	 *            加密内容的MD5Hash字节数组。
	 * @param bodyByte
	 *            加密内容字节数组
	 * @return 组合后的字节数组，比加密内容长16个字节。
	 */
	public static byte[] addMD5(byte[] md5Byte, byte[] bodyByte) {
		int length = bodyByte.length + md5Byte.length;
		byte[] resutlByte = new byte[length];

		// 前16位放MD5Hash码
		for (int i = 0; i < length; i++) {
			if (i < md5Byte.length) {
				resutlByte[i] = md5Byte[i];
			} else {
				resutlByte[i] = bodyByte[i - md5Byte.length];
			}
		}

		return resutlByte;
	}

	/**
	 * <li>
	 * 方法名称:getKeyIV</li> <li>
	 * 功能描述:
	 * 
	 * <pre>
	 * 
	 * </pre>
	 * </li>
	 * 
	 * @param encryptKey
	 * @param key
	 * @param iv
	 */
	public static void getKeyIV(String encryptKey, byte[] key, byte[] iv) {
		// 密钥Base64解密
		BASE64Decoder decoder = new BASE64Decoder();
		byte[] buf = null;
		try {
			buf = decoder.decodeBuffer(encryptKey);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 前8位为key
		int i;
		for (i = 0; i < key.length; i++) {
			key[i] = buf[i];
		}
		// 后8位为iv向量
		for (i = 0; i < iv.length; i++) {
			iv[i] = buf[i + 8];
		}
	}
	/**
	 * unicode转中文
	 * 将JsonUnicode转为中文
	 * @param theString
	 * @return
	 */
	public static String decodeUnicode(String theString) {      
		   
	    char aChar;      
	   
	     int len = theString.length();      
	   
	    StringBuffer outBuffer = new StringBuffer(len);      
	   
	    for (int x = 0; x < len;) {      
	   
	     aChar = theString.charAt(x++);      
	   
	     if (aChar == '\\') {      
	   
	      aChar = theString.charAt(x++);      
	   
	      if (aChar == 'u') {      
	   
	       // Read the xxxx      
	   
	       int value = 0;      
	   
	       for (int i = 0; i < 4; i++) {      
	   
	        aChar = theString.charAt(x++);      
	   
	        switch (aChar) {      
	   
	        case '0':      
	   
	        case '1':      
	   
	        case '2':      
	   
	        case '3':      
	   
	       case '4':      
	   
	        case '5':      
	   
	         case '6':      
	          case '7':      
	          case '8':      
	          case '9':      
	           value = (value << 4) + aChar - '0';      
	           break;      
	          case 'a':      
	          case 'b':      
	          case 'c':      
	          case 'd':      
	          case 'e':      
	          case 'f':      
	           value = (value << 4) + 10 + aChar - 'a';      
	          break;      
	          case 'A':      
	          case 'B':      
	          case 'C':      
	          case 'D':      
	          case 'E':      
	          case 'F':      
	           value = (value << 4) + 10 + aChar - 'A';      
	           break;      
	          default:      
	           throw new IllegalArgumentException(      
	             "Malformed   \\uxxxx   encoding.");      
	          }      
	   
	        }      
	         outBuffer.append((char) value);      
	        } else {      
	         if (aChar == 't')      
	          aChar = '\t';      
	         else if (aChar == 'r')      
	          aChar = '\r';      
	   
	         else if (aChar == 'n')      
	   
	          aChar = '\n';      
	   
	         else if (aChar == 'f')      
	   
	          aChar = '\f';      
	   
	         outBuffer.append(aChar);      
	   
	        }      
	   
	       } else     
	   
	       outBuffer.append(aChar);      
	   
	      }      
	   
	      return outBuffer.toString();      
	   
	     }     
	
	
	public static void main(String[] args) {
		/*System.out.println(encrypt("123456"));
		String encryptPassword=encrypt("123456");
		try {
			System.out.println("解密后的字符串为:"+decrypt(encryptPassword));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		String str="0123456789";
		try {
			System.out.println(EncoderByMd5(str));
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
