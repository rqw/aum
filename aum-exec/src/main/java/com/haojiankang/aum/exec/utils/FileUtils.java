package com.haojiankang.aum.exec.utils;

import lombok.extern.slf4j.Slf4j;

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
        if(!targetFile.exists()){
            targetFile.mkdirs();
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
        return new File("");
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
        byte[] buff = new byte[DEFAULT_BUFFER_SIZE];
        try ( RandomAccessFile rafIn = new RandomAccessFile(src, "r");
              RandomAccessFile rafOut = new RandomAccessFile(dest, "rw");
              FileChannel fcIn = rafIn.getChannel();
              FileChannel fcOut = rafOut.getChannel();
              ){
            long fileSize = fcIn.size();
            MappedByteBuffer mbbIn = fcIn.map(FileChannel.MapMode.READ_ONLY, 0, fileSize);
            MappedByteBuffer mbbOut = fcOut.map(FileChannel.MapMode.READ_WRITE, 0, fileSize);
            if (fileSize <= DEFAULT_BUFFER_SIZE) {// 如果文件不大,可以选择一次性读取到内存
                mbbIn.get(buff, 0, (int) fileSize);
                mbbOut.put(buff, 0, (int) fileSize);
            } else {// 如果文件内容很大,可以循环读取,计算应该读取多少次
                long cycle = fileSize / DEFAULT_BUFFER_SIZE;
                int mode = (int) (fileSize % DEFAULT_BUFFER_SIZE);
                for (int i = 0; i < cycle; i++) {// 每次读取DEFAULT_BUFFER_SIZE个字节
                    mbbIn.get(buff, 0, DEFAULT_BUFFER_SIZE);
                    mbbOut.put(buff, 0, DEFAULT_BUFFER_SIZE);
                }
                if (mode > 0) {
                    buff = new byte[mode];
                    mbbOut.get(buff, 0, mode);
                    mbbOut.put(buff, 0, mode);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
        return true;
    }

}
