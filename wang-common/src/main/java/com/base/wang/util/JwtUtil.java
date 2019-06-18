package com.base.wang.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.util.UUID;

/**
 * JSON Web Token (JWT)是一种基于 token 的认证方案
 */
public class JwtUtil {
    public static String secretKey="ZC+BrFRngnZ8MBDJ9yjOpmlC1wEh+SebqbNTZ5IRUUOifGk" +
            "Ii81zas/+i1lFPPtjqVG/38UYnRiQZ9fFfcAsEw==";

    public static String generateSecretKey(){
        return Base64.encodeBase64String(UUID.randomUUID().toString().getBytes());
    }

    /**
     * 生成令牌
     */
    public static String generateToken(String subject,Date date){
        return JwtUtil.generateToken(subject,date,secretKey);
    }
    public static String generateToken(String subject,Date date,String secretKey){
        Key key=decodeKey(secretKey);
        String token=Jwts.builder().setExpiration(date).setSubject(subject).signWith(SignatureAlgorithm.HS256, key).compact();
        String base64Token=Base64.encodeBase64String(token.getBytes());
        base64Token=base64Token.replace('=','*');
        base64Token=base64Token.replace("\n","");
        base64Token=base64Token.replace("\r","");
        return base64Token;
    }
    private static Key decodeKey(String secretKey){
        Key key=null;
        try {
            byte[] keyBytes= Base64.decodeBase64(secretKey);
            key=new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName());
        }catch (Exception e){

        }
        return  key;
    }

    /**
     * 解析令牌
     */
    public static String verify(String token)throws Exception{
        return verify(token,secretKey);
    }
    public static String verify(String token,String secretKey)throws Exception{
        token=token.replace('*','=');
        Key key=decodeKey(secretKey);
        String decodeToken=new String(Base64.decodeBase64(token));
        return Jwts.parser().setSigningKey(key).parseClaimsJws(decodeToken).getBody().getSubject();
    }

    public static void main(String[] args) throws Exception {
        String subject="100200123456";
        Date date=new Date();
//        date= DateUtils.addMonths(date,10);
        date= DateUtils.addMinutes(date,5);
        String token= JwtUtil.generateToken(subject,date);
        System.out.println("--->>>>token:"+token);
        System.out.println("---->>>>>解析令牌后的数据："+ JwtUtil.verify(token,secretKey));
        String tokenOld="ZXlKaGJHY2lPaUpJVXpJMU5pSjkuZXlKbGVIQWlPakUxTmpBMU9EUXpORFlzSW5OMVlpSTZJakV3TURJd01ERXlNelExTmlKOS5ndFBPRnI0Rm92OHVRTXhBVW1uSXQ4aDhianBuYkRRYTFlVFkzUlprMzdn";

        System.out.println(JwtUtil.verify(tokenOld,secretKey));
    }
}
