package com.base.wang.util;

import java.util.Random;

public class RandomUtil {

    static char[] codeSequence = {  'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
                                    'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
                                    'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
                                    'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
                                    'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

    static char[] numberSequence = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

    static char[] codeAllSequence = {   'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
                                        'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
                                        'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
                                        'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
                                        'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                                         '_'};

    private static int LENGTH = 6;

    private static final char[] LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private static final char[] LOWER_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toLowerCase().toCharArray();
    private static final char[] NUMBERS = "0123456789".toCharArray();
    private static Random r = new Random();

    /***
     * 获得指定长度的随机大写字母
     */
    public static String upperString(int length) {
        StringBuilder sb = new StringBuilder(length);
        while(length-- > 0) {
            sb.append(randomChar(LETTERS));
        }
        return sb.toString();
    }
    /***
     * 获得指定长度的随机小写字母
     */
    public static String lowerString(int length) {
        StringBuilder sb = new StringBuilder(length);
        while(length-- > 0) {
            sb.append(randomChar(LOWER_LETTERS));
        }
        return sb.toString();
    }

    /***
     * 获得指定长度的随机数字
     */
    public static String getNumber(int length) {
        StringBuilder sb = new StringBuilder(length);
        while(length-- > 0) {
            sb.append(randomChar(NUMBERS));
        }
        return sb.toString();
    }

    private static char randomChar(char[] letters) {
        int index = r.nextInt(letters.length);
        return letters[index];
    }

    public static String getSameString(int length) {
        char c = randomChar(LETTERS);
        StringBuilder sb = new StringBuilder(length);

        while(length-- > 0) {
            sb.append(c);
        }

        return sb.toString();
    }

    /**
     * 默认产生6位 数字随机密码
     */
    public static String generateRandomNum(int... length) {
        Random random = new Random();
        // 取随机产生的认证码(6位数字)
        String sRand = "";
        int len = LENGTH;
        if(length.length > 0){
            len = length[0];
        }
        for (int i = 0; i < len; i++) {
            String rand = String.valueOf(numberSequence[random.nextInt(10)]);
            sRand += rand;
        }
        return sRand;
    }

    /**
     * 默认产生6位 随机密码
     */
    public static String generateRandomChar(int... length) {
        Random random = new Random();
        String sRand = "";
        int len = LENGTH;
        if(length.length > 0){
            len = length[0];
        }
        for (int i = 0; i < len; i++) {
            String rand = String.valueOf(codeSequence[random.nextInt(62)]);
            sRand += rand;
        }
        return sRand;
    }
    /**
     * 默认产生6位 随机密码
     */
    public static String generateRandomStr(int... length) {
        Random random = new Random();
        String sRand = "";
        int len = LENGTH;
        if(length.length > 0){
            len = length[0];
        }
        for (int i = 0; i < len; i++) {
            String rand = String.valueOf(codeAllSequence[random.nextInt(63)]);
            sRand += rand;
        }
        return sRand;
    }




    public static String generateRandomUuid() {
        Random random = new Random();
        // 取随机产生的认证码(6位数字)
        String sRand = "";
        for (int i = 0; i < LENGTH; i++) {
            String rand = String.valueOf(codeSequence[random.nextInt(62)]);
            sRand += rand;
        }
        return sRand;
    }

    public static void main(String[] args) {
//        System.out.println(RandomUtil.lowerString(6));
        System.out.println(RandomUtil.generateRandomStr(64));
//        for(int i=0;i<100;i++){
//            System.out.print(RandomUtil.generateRandomNum(4)+" ");
//            System.out.print(RandomUtil.generateRandomChar(4)+" ");
//            System.out.print(RandomUtil.generateRandomStr(4)+" ");
//            System.out.println();
//        }

    }

}
