package com.base.wang.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.SecureRandom;

/**
 * 加密解密算法工具类
 */
public class DESUtils {
    private static final String	ENCODING= "UTF-8";

    /**
     * 通过key对str进行加密
     */
    public static String encrypt(String str, String key) throws Exception {
        if (str == null) return null;
        try {
            SecureRandom sr = new SecureRandom();
            byte rawKeyData[] = key.getBytes(ENCODING);
            DESKeySpec dks = new DESKeySpec(rawKeyData);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, sr);
            byte encryptedData[] = cipher.doFinal(str.getBytes(ENCODING));
            return toHexString(encryptedData);
        }
        catch (Exception e) {
            throw new Exception("加密失败", e);
        }
    }

    /**
     * 通过key对str进行解密
     */
    public static String decrypt(String str, String key) throws Exception {
        if (str == null) return null;

        try {
            byte[] result = convertHexString(str);
            SecureRandom sr = new SecureRandom();
            byte rawKeyData[] = key.getBytes(ENCODING);
            DESKeySpec dks = new DESKeySpec(rawKeyData);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, sr);
            byte encryptedData[] = result;
            byte decryptedData[] = cipher.doFinal(encryptedData);
            return new String(decryptedData, ENCODING);
        }
        catch (Exception e) {
            throw new Exception("解密失败", e);
        }
    }
	private static String toHexString(byte b[]) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            String plainText = Integer.toHexString(0xff & b[i]);
            if (plainText.length() < 2)
                plainText = "0" + plainText;
            hexString.append(plainText);
        }

        return hexString.toString();
    }

	private static byte[] convertHexString(String str) {
        byte digest[] = new byte[str.length() / 2];
        for (int i = 0; i < digest.length; i++) {
            String byteString = str.substring(2 * i, 2 * i + 2);
            int byteValue = Integer.parseInt(byteString, 16);
            digest[i] = (byte) byteValue;
        }

        return digest;
    }





	public static void main(String[] args) throws Exception {
		String str = "20170902161753";
		String key="anYi22U9";
		String encryptStr = DESUtils.encrypt(str,key);
		String decryptStr = DESUtils.decrypt(encryptStr, key);
		
		System.out.println("明文：\t\t" + str);
		System.out.println("加密后的密文：\t" + encryptStr);
		System.out.println("解密后的结果：\t" + decryptStr);
	}


}