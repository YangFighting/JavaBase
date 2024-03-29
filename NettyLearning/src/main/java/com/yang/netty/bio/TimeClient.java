package com.yang.netty.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @author zhangyang03
 * @Description 客户端
 * @create 2022-12-29 15:23
 */
public class TimeClient {
    public static void main(String[] args) {
        Socket socket = null;
        BufferedReader in = null;
        PrintWriter out = null;

        try {
            socket = new Socket("127.0.0.1", 8081);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            out.println("QUERY TIME ORDER");
            System.out.println("Send order 2 server succeed.");

            String resp = in.readLine();
            System.out.println("Now is : " + resp);

            Thread.sleep(200);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            if (in != null) {
                try {
                    in.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
            if (out != null) {
                out.close();
                out = null;
            }
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                socket = null;
            }
        }

    }
}
