# ClassLoader详解

参考：

https://blog.csdn.net/briblue/article/details/54973413

https://zhuanlan.zhihu.com/p/375932112



ClassLoaderd的作用是将class文件加载到JVM虚拟机中，让程序正确运行，但是jvm启动时，并不会一次性加载所有的class文件，而是根据需要动态加载。

## Class文件认识

对于一个基础的java程序 HelloWorld.java

```java
public class HelloWorld{

	public static void main(String[] args){
		System.out.println("Hello world!");
	}
}

```

经过编译命令javac后会生成.class文件

再通过运行命令行java执行 java HelloWorld，java程序才能正常运行

## JAVA类加载流程

Java 中有三个自带的类加载器：

1. **引导类加载器(Bootstrap类加载器)**：由本地代码(c/c++)实现，加载(%JAVA_HOME%\jre\lib),如rt.jar(runtime)、i18n.jar等，这些是Java的核心类。 
2. **扩展类加载器(Extension类加载器)**主要加载扩展目录（ %JAVA_HOME%\lib\ext）下的jar包
3. **系统类加载器(System类加载器)**，也称**Appclass Loader**，主要加载我们应用程序中classpath的类

**java系统按照由上到下依次加载**

### 类加载路径

在Launcher类中（jre\lib\rt.jar!\sun\misc\Launcher.class）

```java
public class Launcher {
    private static URLStreamHandlerFactory factory = new Launcher.Factory();
    private static Launcher launcher = new Launcher();
    private static String bootClassPath = System.getProperty("sun.boot.class.path");
    private ClassLoader loader;
    private static URLStreamHandler fileHandler;

    public static Launcher getLauncher() {
        return launcher;
    }

    public Launcher() {
        Launcher.ExtClassLoader var1;
        try {
            var1 = Launcher.ExtClassLoader.getExtClassLoader();
        } catch (IOException var10) {
            throw new InternalError("Could not create extension class loader", var10);
        }

        try {
            this.loader = Launcher.AppClassLoader.getAppClassLoader(var1);
        } catch (IOException var9) {
            throw new InternalError("Could not create application class loader", var9);
        }

        Thread.currentThread().setContextClassLoader(this.loader);
        String var2 = System.getProperty("java.security.manager");
        if (var2 != null) {
            SecurityManager var3 = null;
            if (!"".equals(var2) && !"default".equals(var2)) {
                try {
                    var3 = (SecurityManager)this.loader.loadClass(var2).newInstance();
                } catch (IllegalAccessException var5) {
                } catch (InstantiationException var6) {
                } catch (ClassNotFoundException var7) {
                } catch (ClassCastException var8) {
                }
            } else {
                var3 = new SecurityManager();
            }

            if (var3 == null) {
                throw new InternalError("Could not create SecurityManager: " + var2);
            }

            System.setSecurityManager(var3);
        }

    }
```

Launcher初始化 ExtClassLoader 和 AppClassLoader，并将 **loader指定成AppClassLoader**（下面会用）

bootClassPath 记录BootstrapClassLoader加载jar包的路径，而java -xbootClassPath 也可以追加Bootstrap类加载器的加载路径

在ExtClassLoader的源码中

```java
 static class ExtClassLoader extends URLClassLoader {
        private static volatile Launcher.ExtClassLoader instance;

        public static Launcher.ExtClassLoader getExtClassLoader() throws IOException {
            if (instance == null) {
                Class var0 = Launcher.ExtClassLoader.class;
                synchronized(Launcher.ExtClassLoader.class) {
                    if (instance == null) {
                        instance = createExtClassLoader();
                    }
                }
            }

            return instance;
        }

        private static Launcher.ExtClassLoader createExtClassLoader() throws IOException {
            try {
                return (Launcher.ExtClassLoader)AccessController.doPrivileged(new PrivilegedExceptionAction<Launcher.ExtClassLoader>() {
                    public Launcher.ExtClassLoader run() throws IOException {
                        File[] var1 = Launcher.ExtClassLoader.getExtDirs();
                        int var2 = var1.length;

                        for(int var3 = 0; var3 < var2; ++var3) {
                            MetaIndex.registerDirectory(var1[var3]);
                        }

                        return new Launcher.ExtClassLoader(var1);
                    }
                });
            } catch (PrivilegedActionException var1) {
                throw (IOException)var1.getException();
            }
        }

        void addExtURL(URL var1) {
            super.addURL(var1);
        }

        public ExtClassLoader(File[] var1) throws IOException {
            super(getExtURLs(var1), (ClassLoader)null, Launcher.factory);
            SharedSecrets.getJavaNetAccess().getURLClassPath(this).initLookupCache(this);
        }

        private static File[] getExtDirs() {
            String var0 = System.getProperty("java.ext.dirs");
            File[] var1;
            if (var0 != null) {
                StringTokenizer var2 = new StringTokenizer(var0, File.pathSeparator);
                int var3 = var2.countTokens();
                var1 = new File[var3];

                for(int var4 = 0; var4 < var3; ++var4) {
                    var1[var4] = new File(var2.nextToken());
                }
            } else {
                var1 = new File[0];
            }

            return var1;
        }

        private static URL[] getExtURLs(File[] var0) throws IOException {
            Vector var1 = new Vector();

            for(int var2 = 0; var2 < var0.length; ++var2) {
                String[] var3 = var0[var2].list();
                if (var3 != null) {
                    for(int var4 = 0; var4 < var3.length; ++var4) {
                        if (!var3[var4].equals("meta-index")) {
                            File var5 = new File(var0[var2], var3[var4]);
                            var1.add(Launcher.getFileURL(var5));
                        }
                    }
                }
            }

            URL[] var6 = new URL[var1.size()];
            var1.copyInto(var6);
            return var6;
        }

```

java.ext.dirs 指定ExtClassLoader的加载路径，可以通过 -D java.ext.dirs  来添加或改变ExtClassLoader的加载路径

在AppClassLoader源码中

```java
static class AppClassLoader extends URLClassLoader {
        final URLClassPath ucp = SharedSecrets.getJavaNetAccess().getURLClassPath(this);

        public static ClassLoader getAppClassLoader(final ClassLoader var0) throws IOException {
            final String var1 = System.getProperty("java.class.path");
            final File[] var2 = var1 == null ? new File[0] : Launcher.getClassPath(var1);
            return (ClassLoader)AccessController.doPrivileged(new PrivilegedAction<Launcher.AppClassLoader>() {
                public Launcher.AppClassLoader run() {
                    URL[] var1x = var1 == null ? new URL[0] : Launcher.pathToURLs(var2);
                    return new Launcher.AppClassLoader(var1x, var0);
                }
            });
        }

        AppClassLoader(URL[] var1, ClassLoader var2) {
            super(var1, var2, Launcher.factory);
            this.ucp.initLookupCache(this);
        }

     
```

java.class.path指定AppClassLoader 的加载路径，该路径会出现工程路径

`E:\project_java\JavaBase\ClassLoaderTest\target\classes`



可以上面参数验证类加载路径

```java
System.out.println("bootClassPath: " + System.getProperty("sun.boot.class.path"));
System.out.println("ExtClassLoader Path: " + System.getProperty("java.ext.dirs"));
System.out.println("AppClassLoader Path: " + System.getProperty("java.class.path"));
```

类加载顺序

### 默认类加载器

```java
public class DefaultClassLoaderTest {

    public static void main(String[] args) {
        ClassLoader cl = DefaultClassLoaderTest.class.getClassLoader();
        System.out.println("ClassLoader is:" + cl.toString());
        System.out.println("ClassLoader\'s parent is:" + cl.getParent().toString());

        cl = int.class.getClassLoader();
        System.out.println("ClassLoader is:" + cl.toString());

    }
}
```

上述代码执行结果

```shell
ClassLoader is:sun.misc.Launcher$AppClassLoader@b4aac2
ClassLoader's parent is:sun.misc.Launcher$ExtClassLoader@16d3586
Exception in thread "main" java.lang.NullPointerException
	at com.DefaultClassLoaderTest.main(DefaultClassLoaderTest.java:14)
```

可知，默认类加载器是AppClassLoader，即DefaultClassLoaderTest.class由AppClassLoader加载，而int.class提示报错，是因为int.class由BootstrapLoader加载

同时，通过getParent()方法，可知AppClassLoader的父加载器是ExtClassLoader，但是ExtClassLoader的父加载器报错提示空指针

通过以下代码可验证

```java
System.out.println("ClassLoader\'s grand father is:"+cl.getParent().getParent().toString());
```

显然这与每个加载器都有父加载器矛盾？？

### 父加载器不是父类

在之前ExtClassLoader 和 AppClassLoader 的源码中，

```java
static class ExtClassLoader extends URLClassLoader {}
static class AppClassLoader extends URLClassLoader {}
class URLClassLoader extends SecureClassLoader
public class SecureClassLoader extends ClassLoader
public abstract class ClassLoader
```

ExtClassLoader 和AppClassLoader 都是继承URLClassLoader，而URLClassLoader 继承SecureClassLoader，SecureClassLoader继承抽象类ClassLoader，之间的类图如下

![](https://imgconvert.csdnimg.cn/aHR0cDovL2ltZy5ibG9nLmNzZG4ubmV0LzIwMTcwMjExMTEyNzU0MTk3?x-oss-process=image/format,png)

在ClassLoader 的源码中

```java
public abstract class ClassLoader {

    private static native void registerNatives();
    static {
        registerNatives();
    }

    // The parent class loader for delegation
    // Note: VM hardcoded the offset of this field, thus all new fields
    // must be added *after* it.
    private final ClassLoader parent;    

	@CallerSensitive
    public final ClassLoader getParent() {
        if (parent == null)
            return null;
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            // Check access to the parent class loader
            // If the caller's class loader is same as this class loader,
            // permission check is performed.
            checkClassLoaderPermission(parent, Reflection.getCallerClass());
        }
        return parent;
    }
}
```

getParent()方法直接返回的是parent，parent赋值来自两方面

1. 外部类创建ClassLoader是指定一个ClassLoader
2. 默认由getSystemClassLoader()构成

```java
	protected ClassLoader() {
        this(checkCreateClassLoader(), getSystemClassLoader());
    }

	private ClassLoader(Void unused, ClassLoader parent) {
        this.parent = parent;
        if (ParallelLoaders.isRegistered(this.getClass())) {
            parallelLockMap = new ConcurrentHashMap<>();
            package2certs = new ConcurrentHashMap<>();
            domains =
                Collections.synchronizedSet(new HashSet<ProtectionDomain>());
            assertionLock = new Object();
        } else {
            // no finer-grained lock; lock on the classloader instance
            parallelLockMap = null;
            package2certs = new Hashtable<>();
            domains = new HashSet<>();
            assertionLock = this;
        }
    }
```

而默认getSystemClassLoader()方法中，会有initSystemClassLoader()初始化，在 initSystemClassLoader中sun.misc.Launcher.getLauncher()生成sun.misc.Launcher类，会通过getClassLoader()返回ClassLoader, 而此时的ClassLoader就是AppClassLoader，AppClassLoader的父类是ExtClassLoader

这里也可以看出：**如果ClassLoader类不指定parent，则parent默认是AppClassLoader**

```java
    private static synchronized void initSystemClassLoader() {
        if (!sclSet) {
            if (scl != null)
                throw new IllegalStateException("recursive invocation");
            sun.misc.Launcher l = sun.misc.Launcher.getLauncher();
            if (l != null) {
                Throwable oops = null;
                scl = l.getClassLoader();
                try {
                    scl = AccessController.doPrivileged(
                        new SystemClassLoaderAction(scl));
                } catch (PrivilegedActionException pae) {
                    oops = pae.getCause();
                    if (oops instanceof InvocationTargetException) {
                        oops = oops.getCause();
                    }
                }
                if (oops != null) {
                    if (oops instanceof Error) {
                        throw (Error) oops;
                    } else {
                        // wrap the exception
                        throw new Error(oops);
                    }
                }
            }
            sclSet = true;
        }
    }
```

而ExtClassLoader初始化时

```java
private static Launcher.ExtClassLoader createExtClassLoader() throws IOException {
    try {
        return (Launcher.ExtClassLoader)AccessController.doPrivileged(new PrivilegedExceptionAction<Launcher.ExtClassLoader>() {
            public Launcher.ExtClassLoader run() throws IOException {
                File[] var1 = Launcher.ExtClassLoader.getExtDirs();
                int var2 = var1.length;

                for(int var3 = 0; var3 < var2; ++var3) {
                    MetaIndex.registerDirectory(var1[var3]);
                }

                return new Launcher.ExtClassLoader(var1);
            }
        });
    } catch (PrivilegedActionException var1) {
        throw (IOException)var1.getException();
    }
}

public ExtClassLoader(File[] var1) throws IOException {
    super(getExtURLs(var1), (ClassLoader)null, Launcher.factory);
    SharedSecrets.getJavaNetAccess().getURLClassPath(this).initLookupCache(this);
}

```

调用`super(getExtURLs(var1), (ClassLoader)null, Launcher.factory);`,parent 为null，也就是

ExtClassLoader的parent为null

Bootstrap ClassLoader由C/C++编写，可以作用任意ClassLoader的附加载器，比如ExtClassLoader

### 双亲委派

一个类加载器查找class时，是通过"委托模式"进行的，首先判断class是不是已经加载过，如果没有，先通过父加载器查找，递归下去，直到Bootstrap ClassLoader，如果Bootstrap ClassLoader找到了直接返回，如果没有找到，则一级一级返回

### 重要方法

#### loadClass()

loadClass()方法来实现双亲委派的原理，方法接收两种方式

一种是loadClass(String name) ，resolve默认是false

```java
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        return loadClass(name, false);
    }
```

另一种 Class<?> loadClass(String name, boolean resolve)

```java
protected Class<?> loadClass(String name, boolean resolve)
        throws ClassNotFoundException
    {
        synchronized (getClassLoadingLock(name)) {
            // First, check if the class has already been loaded
            Class<?> c = findLoadedClass(name);
            if (c == null) {
                long t0 = System.nanoTime();
                try {
                    if (parent != null) {
                        c = parent.loadClass(name, false);
                    } else {
                        c = findBootstrapClassOrNull(name);
                    }
                } catch (ClassNotFoundException e) {
                    // ClassNotFoundException thrown if class not found
                    // from the non-null parent class loader
                }

                if (c == null) {
                    // If still not found, then invoke findClass in order
                    // to find the class.
                    long t1 = System.nanoTime();
                    c = findClass(name);

                    // this is the defining class loader; record the stats
                    sun.misc.PerfCounter.getParentDelegationTime().addTime(t1 - t0);
                    sun.misc.PerfCounter.getFindClassTime().addElapsedTimeFrom(t1);
                    sun.misc.PerfCounter.getFindClasses().increment();
                }
            }
            if (resolve) {
                resolveClass(c);
            }
            return c;
        }
    }
```

在loadClass()方法中主要步骤是

1. 执行 findLoadedClass(name) 去检查class是不是已经加载过了
2. 执行父加载器的loadClass()，如果父加载器为null，调用findBootstrapClassOrNull(name)，用内置的Bootstrap ClassLoader去加载
3. 如果父加载器没有加载成功，则通过findClass(name)去加载

### 自定义 ClassLoader

不管是Bootstrap ClassLoader 还是ExtClassLoader，AppClassLoader ,这些类都是在指定路径下加载class，如果在某些情况下，需要从其他路径动态（比如D盘的某个文件，或者网络中下载）加载，这样就需要自定义classLoader

自定义步骤：

1. 编写一个继承ClassLoader的类
2. 复写findClass()方法
3. 在findClass()中调用defineClass()

defineClass()方法能将class二进制内容转换成Class对象，如果不符合要求就会抛异常

**注意**

一个ClassLoader类创建的时候，如果没有指定parent，那么parent默认是AppClassLoader，这点可以通过抽象类ClassLoade的源码看出（详见父加载器不是父类），但是一个普通的java类A，A 的类加载器可以通过A.class.getClassLoader()查出也是AppClassLoader，这两者之间有什么关联？？

#### DiskClassLoader

自定义DiskClassLoader类，让DiskClassLoader类从磁盘路径`D:\lib`中加载class

编写测试类文件

```java
package com;

public class Test {
    public void say() {

        System.out.println("Say Hello");
    }

}

```

将编译后的class文件，放到路径`D:\lib`中，

**注意**

如果是用idea编译的，要把类文件的class文件删除掉，否则，不会从自定义的DiskClassLoader类加载类文件，而是从AppClassLoader中加载

编写DiskClassLoader代码，重写findClass方法

```java
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

```

测试

```java
package com;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class DiskClassLoaderTest {

    public static void main(String[] args) throws ClassNotFoundException,
            IllegalAccessException, InstantiationException,
            NoSuchMethodException, InvocationTargetException {
        DiskClassLoader diskClassLoader = new DiskClassLoader("D:\\lib");
        Class<?> aClass = diskClassLoader.loadClass("com.Test");
        if (aClass != null) {
            // 通过反射调用 Test类的say 方法
            Object obj = aClass.newInstance();
            Method say = aClass.getDeclaredMethod("say",  null);
            say.invoke(obj, null);
        }
    }
}

```

### Context ClassLoader线程上下文类加载器

每个线程Thread 都有一个相关联的ClassLoader，默认是AppClassLoader，并且子线程默认使用父线程的ClassLoader，可以通过 Thread.currentThread().setContextClassLoader() 方法来修改子线程使用的ClassLoader

测试代码

```java
package com;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ContextClassLoaderTest {
    public static void main(String[] args) throws ClassNotFoundException,
            IllegalAccessException, InstantiationException,
            NoSuchMethodException, InvocationTargetException {
//        DiskClassLoader diskLoader1 = new DiskClassLoader("D:\\lib\\test");
        DiskClassLoader diskLoader1 = new DiskClassLoader("D:\\lib");
        Class aClass = diskLoader1.loadClass("com.Test");
        System.out.println(aClass.getClassLoader().toString());
        if (aClass != null) {
            // 通过反射调用 Test类的say 方法
            Object obj = aClass.newInstance();
            Method say = aClass.getDeclaredMethod("say",  null);
            say.invoke(obj, null);
        }

        DiskClassLoader diskLoader = new DiskClassLoader("D:\\lib");
        System.out.println("Thread "+Thread.currentThread().getName()+" classloader: "
                +Thread.currentThread().getContextClassLoader().toString());
        new Thread(new Runnable() {

            @Override
            public void run() {

                System.out.println("Thread "+Thread.currentThread().getName()+" classloader: "+
                        Thread.currentThread().getContextClassLoader().toString());
                ClassLoader cl = Thread.currentThread().getContextClassLoader();
                try {
                    Class<?> c = cl.loadClass("com.Test");
                    System.out.println(c.getClassLoader().toString());
                    if (c != null) {
                        // 通过反射调用 Test类的say 方法
                        Object obj = c.newInstance();
                        Method say = c.getDeclaredMethod("say",  null);
                        say.invoke(obj, null);
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}

```

运行结果

```shell
com.DiskClassLoader@154617c
Say Hello
Thread main classloader: sun.misc.Launcher$AppClassLoader@b4aac2
Thread Thread-0 classloader: sun.misc.Launcher$AppClassLoader@b4aac2
java.lang.ClassNotFoundException: com.Test
	at java.net.URLClassLoader.findClass(URLClassLoader.java:382)
	at java.lang.ClassLoader.loadClass(ClassLoader.java:424)
	at sun.misc.Launcher$AppClassLoader.loadClass(Launcher.java:349)
	at java.lang.ClassLoader.loadClass(ClassLoader.java:357)
	at com.ContextClassLoaderTest$1.run(ContextClassLoaderTest.java:34)
	at java.lang.Thread.run(Thread.java:748)
```

表明：

1. 子线程的ContextClassLoader是AppClassLoader
2. AppClassLoader 加载不了父线程中已经加载的class

设置子线程的ContextClassLoader

```java
 Thread.currentThread().setContextClassLoader(diskLoader1);
```

可以看到

```shell
com.DiskClassLoader@154617c
Say Hello
Thread main classloader: sun.misc.Launcher$AppClassLoader@b4aac2
Thread Thread-0 classloader: com.DiskClassLoader@154617c
com.DiskClassLoader@154617c
Say Hello
```

可以看到子线程的ContextClassLoader变成DiskClassLoader，类能加载成功

## 总结

1. ClassLoader用来加载class文件
2. 系统内置的ClassLoader通过双亲委派的方式来加载执行路径下的class 和资源
3. 可以自定义ClassLoader，一般覆盖findClass()方法，如果重写loadClass()方法，就可以打破双亲委派的方式
4. ContextClassLoader与线程相关，可以设置ContextClassLoader，也可以打破双亲委派的方式



