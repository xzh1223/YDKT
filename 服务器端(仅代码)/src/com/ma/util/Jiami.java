package com.ma.util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * @ClassName: Jiami  
 * @Description: 密码加密  
 * @author MZ  
 * @2017年3月16日下午3:39:15
 */
public class Jiami {
	public static byte[] decryptBASE64(String key) throws Exception {   
        return (new BASE64Decoder()).decodeBuffer(key);   
    }   

    public static String encryptBASE64(byte[] key) throws Exception {   
        return (new BASE64Encoder()).encodeBuffer(key);   
    }  

    public static void main(String[] args) {

        String  str="mazhuang";

        try {
            String  result1= Jiami.encryptBASE64(str.getBytes());
            System.out.println("result1=====加密数据=========="+result1);

            byte  result2[]= Jiami.decryptBASE64(result1);
            String  str2=new String(result2);
            System.out.println("str2========解密数据========"+str2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
