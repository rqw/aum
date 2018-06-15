package com.haojiankang.aum.daemon.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.system.ApplicationHome;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

@Slf4j
public class FileUtils {
    private static final int DEFAULT_BUFFER_SIZE = 1024 * 1024 * 10;// 10M
    public static void saveFile(byte[] file, File targetFile, String fileName)throws Exception {
            if(!targetFile.exists()){
                targetFile.mkdirs();
            }
            FileOutputStream out = new FileOutputStream(new File(targetFile,fileName));
            out.write(file);
            out.flush();
            out.close();
    }
    public static File getBasePath() {
        ApplicationHome home = new ApplicationHome(FileUtils.class);
        return home.getDir();
    }
    public static String fileToString(File file, String encoding) throws IOException {
        if (file == null)
            return null;
        StringBuilder txt = new StringBuilder();
        InputStreamReader read = null;
        BufferedReader buffer = null;
        try {
            read = new InputStreamReader(new FileInputStream(file), encoding);// 考虑到编码格式
            buffer = new BufferedReader(read);
            String lineTxt = null;
            while ((lineTxt = buffer.readLine()) != null) {
                txt.append(lineTxt);
                txt.append("\r\n");
            }
            read.close();
        } finally {
            if (read != null)
                read.close();
            if (buffer != null)
                buffer.close();

        }
        return txt.toString();
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
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }

}
