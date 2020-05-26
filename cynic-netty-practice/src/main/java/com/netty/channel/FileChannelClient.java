package com.netty.channel;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author cynic
 * @version 1.0
 * @describe todo
 * @createTime 2020/5/21 13:44
 */
public class FileChannelClient {
    public static void main(String[] args) throws IOException {
//        File file = new File("E:\\test\\filesNames.txt");
//        FileInputStream fileInputStream = new FileInputStream(file);
//        FileChannel fileInputChannel = fileInputStream.getChannel();
//        File fileDes = new File("E:\\test\\0521.txt");
//        FileOutputStream fileOutputStream  = new FileOutputStream(fileDes);
//        FileChannel fileChannelOut = fileOutputStream.getChannel();
////        RandomAccessFile randomAccessFile = new RandomAccessFile("E:\\test\\0519.txt","rw");
////        FileChannel fc = randomAccessFile.getChannel();
//        ByteBuffer byteBuffer = ByteBuffer.allocate(2000);
//        int length = -1;
//        while (length == fileInputChannel.read(byteBuffer)){
//            fileChannelOut.write(byteBuffer);
//            byteBuffer.clear();
//        }
        Path path = Paths.get("E:\\test\\filesNames.txt");
        byte[] data = Files.readAllBytes(path);
        String result = new String(data, "utf-8");
        System.out.println(result);
    }
}
