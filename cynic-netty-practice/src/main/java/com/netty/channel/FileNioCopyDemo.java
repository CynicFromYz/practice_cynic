package com.netty.channel;

import org.apache.tomcat.util.http.fileupload.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author cynic
 * @version 1.0
 * @describe todo
 * @createTime 2020/5/21 15:34
 */
public class FileNioCopyDemo {
    public static void main(String[] args) throws IOException {
        nioCopyResourceFile();
    }

    public static void nioCopyResourceFile() throws IOException {
        String srcPath = "E:\\test\\filesNames.txt";
        String destPath = "E:\\test\\05-21.txt";
        nioCopyFile(srcPath, destPath);
    }

    /**
     * nio方式复制文件
     *
     * @param srcPath
     * @param destPath
     */
    public static void nioCopyFile(String srcPath, String destPath) throws IOException {
        File srcFile = new File(srcPath);
        File destFile = new File(destPath);
        if (!destFile.exists()) {
            destFile.createNewFile();
        }
        long startTime = System.currentTimeMillis();
        FileInputStream fis = new FileInputStream(srcFile);
        FileOutputStream fos = new FileOutputStream(destFile);
        FileChannel inChannel = fis.getChannel();
        FileChannel outChannel = fos.getChannel();
        int length = -1;
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        while ((length = inChannel.read(byteBuffer)) != -1) {
            byteBuffer.flip();
            int outLength = 0;
            //将byteBuffer写到输出的通道
            while ((outLength = outChannel.write(byteBuffer)) != 0) {
                System.out.println("写入的字节数:" + outLength);
            }
            //第二次切换 清除byteBuffer，变成写入模式
            byteBuffer.clear();
        }
        outChannel.force(true);
        IOUtils.closeQuietly(outChannel);
        IOUtils.closeQuietly(fos);
        IOUtils.closeQuietly(inChannel);
        IOUtils.closeQuietly(fis);
        long endTime = System.currentTimeMillis();
        System.out.println("文件复制时间:" + (endTime - startTime));
    }
}
