package com.yang.channel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

/**
 * @author zhangyang03
 * @Description
 * @create 2022-10-05 11:44
 */
public class FileChannelDemo2 {
    private static final String CLASS_PATH_NAME = new File("InOut").getAbsolutePath();
    private static final String FILE_PATH_NAME = CLASS_PATH_NAME + File.separator + "file";
    private static final String OUT_PATH_NAME = FILE_PATH_NAME + File.separator + "Demo2.txt";

    public static void main(String[] args) throws IOException {
        // 新建 文件通道
        FileOutputStream fileOutputStream = new FileOutputStream(OUT_PATH_NAME);
        FileChannel channel = fileOutputStream.getChannel();

        // 分配 buffer
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        String dataStr = "hello world";

        // 数据写入 buffer 中
        buffer.clear();
        buffer.put(dataStr.getBytes(StandardCharsets.UTF_8));
        // 转换读写模式
        buffer.flip();

        while (buffer.hasRemaining()) {
            System.out.println("start write channel");
            // 读取buffer数据，写入 channel 中
            channel.write(buffer);
        }
        channel.close();
    }

}
