package com.hust.keyRD.commons.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

public class AESUtil
{
    // 加密
    public static String Encrypt(String sSrc, String sKey)
            throws Exception
    {
        if (sKey == null)
        {
            System.out.print("Key为空null");
            return null;
        }
        // 判断Key是否为16位
        if (sKey.length() != 16)
        {
            System.out.print("Key长度不是16位");
            return null;
        }
        byte[] raw = sKey.getBytes("utf-8");
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");// "算法/模式/补码方式"
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));

        return new Base64().encodeToString(encrypted);// 此处使用BASE64做转码功能，同时能起到2次加密的作用。
    }

    // 解密
    public static String Decrypt(String sSrc, String sKey)
            throws Exception
    {
        try
        {
            // 判断Key是否正确
            if (sKey == null)
            {
                System.out.print("Key为空null");
                return null;
            }
            // 判断Key是否为16位
            if (sKey.length() != 16)
            {
                System.out.print("Key长度不是16位");
                return null;
            }
            byte[] raw = sKey.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] encrypted1 = new Base64().decode(sSrc);// 先用base64解密
            try
            {
                byte[] original = cipher.doFinal(encrypted1);
                String originalString = new String(original, "utf-8");
                return originalString;
            }
            catch (Exception e)
            {
                System.out.println(e.toString());
                return null;
            }
        }
        catch (Exception ex)
        {
            System.out.println(ex.toString());
            return null;
        }
    }

    public static String transTo16(String str){
        if(str.length()<=16){
            StringBuilder stringBuilder = new StringBuilder(str);
            while (stringBuilder.length()<16){
                stringBuilder.append("#");
            }
            return stringBuilder.toString();
        }else {
            return str.substring(0,16);
        }
    }
    public static void main(String[] args)
            throws Exception
    {
        /*
         * 此处使用AES-128-ECB加密模式，key需要为16位。
         */
        String cKey = "6ay4dlwd4enjbf90";
        // 需要加密的字串
        String cSrc = "<mediator><courtCode>1306</courtCode><telephone>15068866668</telephone></mediator>";
        System.out.println(cSrc);
        // 加密
        String enString = AESUtil.Encrypt(cSrc, cKey);
        System.out.println("加密后的字串是：" + enString);

        // 解密
        String DeString = AESUtil.Decrypt(enString, cKey);
        System.out.println("解密后的字串是：" + DeString);
    }

}