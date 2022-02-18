package com;

import java.io.*;

public class DiskClassLoader extends ClassLoader {

    private final String mLibPath;

    public DiskClassLoader(String mLibPath) {
        this.mLibPath = mLibPath;
    }

    /**
     * 根据 class名称 拼接加载的 class文件名
     *
     * @param name class名称
     * @return 要加载的 class文件名
     */
    private String getFileName(String name) {
        int index = name.lastIndexOf('.');
        if (-1 == index) {
            return name + ".class";
        } else {
            return name.substring(index + 1) + ".class";
        }
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        String fileName = getFileName(name);
        File file = new File(mLibPath, fileName);
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            int len = 0;
            while ((len = fileInputStream.read()) != -1) {
                byteArrayOutputStream.write(len);
            }
            byte[] bytes = byteArrayOutputStream.toByteArray();
            fileInputStream.close();
            byteArrayOutputStream.close();
            return defineClass(name, bytes, 0, bytes.length);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return super.findClass(name);
    }
}
