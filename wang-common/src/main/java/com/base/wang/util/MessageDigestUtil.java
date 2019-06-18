package com.base.wang.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MessageDigestUtil {
    
    public static char[] hexCharUpperCase = { '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
    
    public static char[] hexChar = { '0', '1', '2', '3', '4', '5', '6', '7',
        '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };   //default
    
    public static String DEFAULT_MESSAGE_DIGEST_VALUE = "MD5";

    /**
     * 取Hash值
     * @param fileName
     * @param hashType  hash类型: MD5,SHA...(默认使用MD5)
     * @return
     * @throws Exception
     */
    public static String getHash(String fileName, String hashType)throws Exception {
        InputStream is = new FileInputStream(fileName);
        return getHash(is, hashType);
    }
    
    /**
     * 取文件hash值
     * @param file
     * @param hashType  hash类型: MD5,SHA...(默认使用MD5)
     * @return
     * @throws Exception
     */
    public static String getHash(File file, String hashType)throws Exception {
        InputStream is = new FileInputStream(file);
        return getHash(is, hashType);
    }

    /**
     * 取流的hash
     * @param is
     * @param hashType  hash类型: MD5,SHA...(默认使用MD5)
     * @return
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    public static String getHash(InputStream is, String hashType)
            throws NoSuchAlgorithmException, IOException {
        if (null == is) {
            throw new RuntimeException("InputStream is empty!");
        }
        MessageDigest md5 = StringUtil.isEmpty(hashType) ? MessageDigest
                .getInstance(DEFAULT_MESSAGE_DIGEST_VALUE) : MessageDigest
                .getInstance(hashType);
        int numRead = 0;
        byte[] buffer = new byte[1024];
        try {
            while ((numRead = is.read(buffer)) > 0) {
                md5.update(buffer, 0, numRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Read inputStream content failure!", e);
        } finally {
            is.close();
        }
        return toHexString(md5.digest());
    }
    
    /**
     * 取byte数组hash
     * @param b
     * @param hashType  hash类型: MD5,SHA...(默认使用MD5)
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static String getHash(byte[] b,String hashType) throws NoSuchAlgorithmException{
        MessageDigest md5 = StringUtil.isEmpty(hashType) ?
                MessageDigest.getInstance(DEFAULT_MESSAGE_DIGEST_VALUE) : MessageDigest.getInstance(hashType);
        return toHexString(md5.digest(b));
    }

    /**
     * byte数组转String
     * @param b
     * @return
     */
    public static String toHexString(byte[] b) {
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            sb.append(hexChar[(b[i] & 0xf0) >>> 4]);
            sb.append(hexChar[b[i] & 0x0f]);
        }
        return sb.toString();
    }
    /**
     * 将一个16进制的String转为十进制byte数组
     */
    public static byte[] parseHexString(String str){
        if(str==null) return null;
        byte[] bytes = new byte[str.length()/2];
        char[] chars=str.toCharArray();

        for(int i=0; i<bytes.length; i++){
            char ch1 = chars[i*2];
            char ch2 = chars[i*2+1];
            bytes[i]=(byte)Integer.parseInt(""+ch1+ch2,16);
        }
        return bytes;
    }
    public static void main(String[] args)throws Exception{
        String str="000中文000abcdABCD";
        String str2= MessageDigestUtil.toHexString(str.getBytes("utf-8"));
        System.out.println(str2);
        byte[] bytes= parseHexString(str2);
        for(int i=0;i<bytes.length;i++){
            System.out.print(bytes[i]);
        }
        System.out.println();
        System.out.println(new String( parseHexString(str2), "utf-8") );
    }

}
