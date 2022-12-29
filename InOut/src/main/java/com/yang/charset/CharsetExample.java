package com.yang.charset;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author zhangyang03
 * @Description
 * @create 2022-10-04 22:10
 */
public class CharsetExample {
    public static void main(String[] args) {
        Charset cs = StandardCharsets.UTF_8;
        System.out.println(cs.displayName());
        System.out.println(cs.canEncode());
        String st = "hello world";
        ByteBuffer bytebuffer = ByteBuffer.wrap(st.getBytes());

        CharBuffer charbuffer = cs.decode(bytebuffer);
        ByteBuffer newByteBuffer = cs.encode(charbuffer);
        while (newByteBuffer.hasRemaining()) {
            char c = (char) newByteBuffer.get();
            System.out.println(c);
        }
        newByteBuffer.clear();


    }
}
