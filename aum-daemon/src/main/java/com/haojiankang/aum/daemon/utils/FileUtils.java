package com.haojiankang.aum.daemon.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.system.ApplicationHome;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class FileUtils {
    private static final int DEFAULT_BUFFER_SIZE = 1024 * 1024 * 10;// 10M
    public static boolean writeFile(String content, File targetFile) {
       return writeFile(content.getBytes(),targetFile);
    }
    public static boolean writeFile(byte[] bs, File targetFile) {
        if(!targetFile.getParentFile().exists()){
            targetFile.getParentFile().mkdirs();
        }
        try(FileOutputStream out = new FileOutputStream(targetFile);){
            out.write(bs);
            out.flush();
        }catch(Exception e){
            log.error(e.getMessage(),e);
            return false;
        }
        return true;
    }
    public static boolean  writeFile(byte[] bs, File targetFile, String fileName)throws Exception {
       return  writeFile(bs,new File(targetFile,fileName));
    }
    public static File getBasePath() {
        ApplicationHome home = new ApplicationHome(FileUtils.class);
        return home.getDir();
    }
    public static String readFileToString(File file, String encoding) throws IOException {
        if (file == null)
            return null;
        StringBuilder txt = new StringBuilder();
        try (InputStreamReader read= new InputStreamReader(new FileInputStream(file), encoding);// 考虑到编码格式
             BufferedReader buffer = new BufferedReader(read); ){
            String lineTxt = null;
            while ((lineTxt = buffer.readLine()) != null) {
                txt.append(lineTxt);
                txt.append("\r\n");
            }
        }
        return txt.toString();
    }
    public static List<String> readFileToList(File file, String encoding) throws IOException {
        if (file == null)
            return null;
        List<String> list=new ArrayList<>();
        try (InputStreamReader read= new InputStreamReader(new FileInputStream(file), encoding);// 考虑到编码格式
             BufferedReader buffer = new BufferedReader(read); ){
            String lineTxt = null;
            while ((lineTxt = buffer.readLine()) != null) {
                list.add(lineTxt);
            }
        }
        return list;
    }
    public static String md5(File file) {
        if (file.exists() == false)
            return null;
        try(FileInputStream fis = new FileInputStream(file);) {
            return Strings.md5(fis);
        } catch (IOException  e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }
    /**
     * 拷贝文件
     *
     * @param src
     *            原文件
     * @param dest
     *            目的文件
     * @return 是否成功
     */
    public static boolean copyFile(File src, File dest) {
        if (null == src || !src.isFile())
            return false;
        if (!dest.getParentFile().exists() && !dest.getParentFile().mkdirs())
            return false;
        if (dest.isFile() && dest.exists())
            dest.delete();
        try(
        FileInputStream ins = new FileInputStream(src);
        FileOutputStream out = new FileOutputStream(dest);
        ){
            byte[] b = new byte[1024];
            int n=0;
            while((n=ins.read(b))!=-1){
                out.write(b, 0, n);
            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
            return false;
        }
        return true;
    }

}
