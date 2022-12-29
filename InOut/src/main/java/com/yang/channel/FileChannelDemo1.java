package com.yang.channel;

import java.io.*;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;

/**
 * @author zhangyang03
 * @Description 读取通道数据写入 buffer
 * @create 2022-10-05 10:55
 */
public class FileChannelDemo1 {
    private static final String CLASS_PATH_NAME = new File("InOut").getAbsolutePath();
    private static final String FILE_PATH_NAME = CLASS_PATH_NAME + File.separator + "file";
    private static final String IN_PATH_NAME = FILE_PATH_NAME + File.separator + "abc.txt";

    public static void main(String[] args) throws IOException {
        // 新建 文件通道
        FileInputStream fileInputStream = new FileInputStream(IN_PATH_NAME);
        FileChannel channel = fileInputStream.getChannel();
        // 分配 buffer
        ByteBuffer buffer = ByteBuffer.allocate(8);

        // 读取通道的数据，写入 buffer 中
        while (channel.read(buffer) > 0) {
            System.out.println("start read channel data ");
            buffer.flip();
            while (buffer.hasRemaining()) {
                // get() 方法 只会读取当前 position 的数据，之后position 自动加1
                byte b = buffer.get();
                System.out.println("data: " + (char) b);
            }
            buffer.clear();
        }

        fileInputStream.close();
        System.out.println("over !!");
    }
}
