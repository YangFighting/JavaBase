import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author zhangyang
 * @date 2021/9/26 11:41
 */
public class InOut {
    private static final String CLASS_PATH_NAME = new File("InOut").getAbsolutePath();
    private static final String FILE_PATH_NAME = CLASS_PATH_NAME + File.separator + "file";
    private static final String IN_PATH_NAME = FILE_PATH_NAME + File.separator + "abc.txt";
    private static final String OUT_BIO_PATH_NAME = FILE_PATH_NAME + File.separator + "abc_bio_copy.txt";
    private static final String OUT_NIO_PATH_NAME = FILE_PATH_NAME + File.separator + "abc_nio_copy.txt";
    private static final String OUT_NIO2_PATH_NAME = FILE_PATH_NAME + File.separator + "abc_nio2_copy.txt";
    private static final int SIZE = 1024;

    /**
     * 使用 bio 复制文件，使用Buffered，加快复制速度
     * @throws IOException IO 异常
     */
    public static void bioInOut() throws IOException {

        try (FileInputStream fileInputStream = new FileInputStream(IN_PATH_NAME);
             BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
             FileOutputStream fileOutputStream = new FileOutputStream(OUT_BIO_PATH_NAME);
             BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream)
        ) {

            int off = 0;
            byte[] b = new byte[SIZE];

            while (bufferedInputStream.read(b, off, SIZE) != -1) {
                bufferedOutputStream.write(b, 0, SIZE);
            }
        }
    }

    /**
     * 使用 nio 的方式复制文件，使用 transferTo API
     * @throws IOException IO 异常
     */
    public static void nioInOut() throws IOException {
        try(FileChannel fileInputchannel = new FileInputStream(IN_PATH_NAME).getChannel();
            FileChannel fileOutputChannel = new FileOutputStream(OUT_NIO_PATH_NAME).getChannel()){
            fileInputchannel.transferTo(0, fileInputchannel.size(), fileOutputChannel);

        }
    }

    /**
     * 使用 nio 的方式复制文件
     * @throws IOException  IO 异常
     */
    public static void nioInOut2() throws IOException{
        try(FileChannel fileInputchannel = new FileInputStream(IN_PATH_NAME).getChannel();
            FileChannel fileOutputChannel = new FileOutputStream(OUT_NIO2_PATH_NAME).getChannel()){
            // 分配 1024 个字节大小的缓存区
            ByteBuffer dsts = ByteBuffer.allocate(SIZE);
            while (fileInputchannel.read(dsts) != -1){
                // 切换缓存区的读写模式
                dsts.flip();
                fileOutputChannel.write(dsts);
                dsts.clear();
            }
        }
    }
    public static void main(String[] args) throws IOException {
        nioInOut2();
    }

}
