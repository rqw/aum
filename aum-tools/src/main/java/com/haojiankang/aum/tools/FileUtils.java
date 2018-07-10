package com.haojiankang.aum.tools;


import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
    private static final int DEFAULT_BUFFER_SIZE = 1024 * 1024 * 10;// 10M

    public static void writeFile(String content, File targetFile) throws IOException {
        if (!targetFile.getParentFile().exists()) {
            targetFile.getParentFile().mkdirs();
        }
        try (
                FileOutputStream fo = new FileOutputStream(targetFile);
                OutputStreamWriter wo = new OutputStreamWriter(fo, "UTF-8");
                BufferedWriter out = new BufferedWriter(wo);
        ) {
            out.write(content);
        }

    }

    public static void writeFile(byte[] bs, File targetFile) throws IOException {
        if (!targetFile.getParentFile().exists()) {
            targetFile.getParentFile().mkdirs();
        }
        try (FileOutputStream out = new FileOutputStream(targetFile);) {
            out.write(bs);
        }
    }

    public static void writeFile(byte[] bs, File targetFile, String fileName) throws IOException {
        writeFile(bs, new File(targetFile, fileName));
    }

    public static File getBasePath() {
        return new File("");
    }

    public static String readFileToString(File file, String encoding) throws IOException {
        if (file == null)
            return null;
        StringBuilder txt = new StringBuilder();
        try (InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);// 考虑到编码格式
             BufferedReader buffer = new BufferedReader(read);) {
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
        List<String> list = new ArrayList<>();
        try (InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);// 考虑到编码格式
             BufferedReader buffer = new BufferedReader(read);) {
            String lineTxt = null;
            while ((lineTxt = buffer.readLine()) != null) {
                list.add(lineTxt);
            }
        }
        return list;
    }

    public static String md5(File file) throws IOException, NoSuchAlgorithmException {
        if (file.exists() == false)
            return null;
        try (FileInputStream fis = new FileInputStream(file);) {
            return Strings.md5(fis);
        }
    }

    /**
     * 拷贝文件
     *
     * @param src  原文件
     * @param dest 目的文件
     * @return 是否成功
     */
    public static void copyFile(File src, File dest) throws IOException {
        if (null == src && !dest.getParentFile().exists() && !dest.getParentFile().mkdirs())
            return;
        if (src.isDirectory()) {
            File[] files = src.listFiles();
            for (File sf : files) {
                copyFile(sf, new File(dest, sf.getName()));
            }
        } else {
            if (dest.exists())
                delFile(dest);
            try (FileInputStream ins = new FileInputStream(src);
                 FileOutputStream out = new FileOutputStream(dest);) {
                byte[] b = new byte[1024];
                int n = 0;
                while ((n = ins.read(b)) != -1) {
                    out.write(b, 0, n);
                }
            }
        }
    }

    public static boolean delFile(File file) {
        if (!file.exists())
            return false;
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                delFile(f);
            }
        }
        return file.delete();
    }

}
