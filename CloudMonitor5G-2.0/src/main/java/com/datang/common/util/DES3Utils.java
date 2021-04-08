package com.datang.common.util;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * DES3加解密
 * 
 * @author yinzhipeng
 * 
 */
public class DES3Utils {
	private static Logger logger = LoggerFactory.getLogger(DES3Utils.class);
	public final static String USERNAME_KEY = "ZHONGHUARENMINGGONGHEGUOWANSUI";
	public final static String PASSWORD_KEY = "ZHONGGUOYIDONGTONGXINYANJUYUAN";

	public static void main(String[] args) throws Exception {
		byte[] key = new BASE64Decoder()
				.decodeBuffer("ZHONGHUARENMINGGONGHEGUOWANSUI");
		byte[] keyiv = { 1, 2, 3, 4, 5, 6, 7, 8 };
		byte[] data = "admin".getBytes("UTF-8");

		System.out.println("ECB加密解密");
		byte[] str3 = des3EncodeECB(key, data);// 加密
		byte[] str4 = des3DecodeECB(key, str3);// 解密
		System.out.println(new BASE64Encoder().encode(str3));
		String mm = new String(str4);
		System.out.println(mm);

		byte[] str8 = des3DecodeECB(key,
				new BASE64Decoder().decodeBuffer(new BASE64Encoder()
						.encode(str3)));
		System.out.println(new String(str8));
		System.out.println();

		System.out.println("CBC加密解密");
		byte[] str5 = des3EncodeCBC(key, keyiv, data);
		byte[] str6 = des3DecodeCBC(key, keyiv, str5);
		System.out.println(new BASE64Encoder().encode(str5));
		System.out.println(new String(str6));
	}

	/**
	 * * ECB加密,不要IV *
	 * 
	 * @param key
	 *            密钥 *
	 * @param data
	 *            明文 *
	 * @return Base64编码的密文
	 * @throws Exception
	 */
	public static byte[] des3EncodeECB(byte[] key, byte[] data)
			throws Exception {
		Key deskey = null;
		DESedeKeySpec spec = new DESedeKeySpec(key);
		SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
		deskey = keyfactory.generateSecret(spec);
		Cipher cipher = Cipher.getInstance("desede" + "/ECB/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, deskey);
		byte[] bOut = cipher.doFinal(data);
		return bOut;
	}

	/**
	 * * ECB解密,不要IV *
	 * 
	 * @param key
	 *            密钥 *
	 * @param data
	 *            Base64编码的密文
	 * @return 明文 *
	 * @throws Exception
	 */
	public static byte[] des3DecodeECB(byte[] key, byte[] data)
			throws Exception {
		Key deskey = null;
		DESedeKeySpec spec = new DESedeKeySpec(key);
		SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
		deskey = keyfactory.generateSecret(spec);
		Cipher cipher = Cipher.getInstance("desede" + "/ECB/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, deskey);
		byte[] bOut = cipher.doFinal(data);
		return bOut;
	}

	/**
	 * * CBC加密 *
	 * 
	 * @param key
	 *            密钥 *
	 * @param keyiv
	 *            IV *
	 * @param data
	 *            明文 *
	 * @return Base64编码的密文
	 * @throws Exception
	 */
	public static byte[] des3EncodeCBC(byte[] key, byte[] keyiv, byte[] data)
			throws Exception {
		Key deskey = null;
		DESedeKeySpec spec = new DESedeKeySpec(key);
		SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
		deskey = keyfactory.generateSecret(spec);
		Cipher cipher = Cipher.getInstance("desede" + "/CBC/PKCS5Padding");
		IvParameterSpec ips = new IvParameterSpec(keyiv);
		cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
		byte[] bOut = cipher.doFinal(data);
		return bOut;
	}

	/**
	 * * CBC解密 *
	 * 
	 * @param key
	 *            密钥 *
	 * @param keyiv
	 *            IV *
	 * @param data
	 *            Base64编码的密文
	 * @return 明文 *
	 * @throws Exception
	 */
	public static byte[] des3DecodeCBC(byte[] key, byte[] keyiv, byte[] data)
			throws Exception {
		Key deskey = null;
		DESedeKeySpec spec = new DESedeKeySpec(key);
		SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
		deskey = keyfactory.generateSecret(spec);
		Cipher cipher = Cipher.getInstance("desede" + "/CBC/PKCS5Padding");
		IvParameterSpec ips = new IvParameterSpec(keyiv);
		cipher.init(Cipher.DECRYPT_MODE, deskey, ips);
		byte[] bOut = cipher.doFinal(data);
		return bOut;
	}
}