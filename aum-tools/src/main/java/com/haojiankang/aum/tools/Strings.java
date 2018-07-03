package com.haojiankang.aum.tools;


import org.xidea.lite.LiteCompiler;
import org.xidea.lite.LiteTemplate;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Strings {
    public static String md5(String str)throws  IOException,NoSuchAlgorithmException {
        ByteArrayInputStream bis = new ByteArrayInputStream(str.getBytes());
        return md5(bis);
    }
    public static String md5(InputStream input)throws  IOException,NoSuchAlgorithmException {
        // 实际返回的为MD5值
        MessageDigest messageDigest = null;
            StringBuilder md5StrBuff = new StringBuilder();
            byte[] byteArray = digest(input, "md5");
            for (int i = 0; i < byteArray.length; i++) {
                if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
                    md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
                else
                    md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
            }
            return md5StrBuff.toString();

    }
    /**
     * 对输入流进行散列运算, 支持md5与sha1算法.
     *
     * @param input     待运算输入流
     * @param algorithm MD2/MD5/SHA-1/SHA-256/SHA-384/SHA-512
     * @return 散列运算得到的字节数组
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    public static byte[] digest(InputStream input, String algorithm) throws IOException, NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
        int read = -1;
        int bufferLength = 1024 * 1024 * 10;
        byte[] buffer = new byte[bufferLength];
        do {
            read = input.read(buffer, 0, bufferLength);
            if (read > -1)
                messageDigest.update(buffer, 0, read);
        } while (read > -1);
        return messageDigest.digest();
    }
    public static String compile(String target,Map<String,String> context){
        for(Map.Entry<String,String> item:context.entrySet()){
            String k="{"+item.getKey()+"}";
            if(target.contains(k)){
                target=target.replace(k,item.getValue());
            }
        }
        return target;
    }

}
