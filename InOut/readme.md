# Java NIO

来源：https://www.yiibai.com/java_nio

https://www.bilibili.com/video/BV1E64y1h7Z4



##  IO与NIO比较

表格列出了Java IO和NIO之间的主要区别：

| IO              | NIO                       |
| --------------- | ------------------------- |
| 基于阻塞I/O操作 | 基于非阻塞I/O操作         |
| 面向流的        | 面向缓存的                |
| 通道不可用      | 通道可用于非阻塞I/O操作   |
| 选择器不可用    | 选择器可用于非阻塞I/O操作 |

### 阻塞与非阻塞I/O

Java IO当线程调用`write()`或`read()`时，线程会被阻塞，直到有一些数据可用于读取或数据被完全写入，

NIO非阻塞模式允许线程请求向通道写入数据，但不等待它被完全写入。允许线程继续进行，并做其他事情。

### 面向流与面向缓冲

#### 面向流

Java IO是面向流的I/O，这意味着需要从流中读取一个或多个字节。它使用流来在数据源/槽和java程序之间传输数据。

![](http://www.yiibai.com/uploads/images/201709/2809/851080928_53643.png)



#### 面向缓冲

将数据读入缓冲器，使用通道进一步处理数据。 在NIO中，使用通道和缓冲区来处理I/O操作。

通道和流之间的主要区别是：

- 流可以用于单向数据传输。
- 通道提供双向数据传输。

通过在java NIO中引入通道，可以执行非阻塞I/O操作。



通道，缓冲区，java程序，数据源和数据接收器之间的相互作用

#####  Q: **会存在两个缓存区吗？？**

A: 两个缓存区是程序自己新建的，如果是直接回传数据，可以用一个缓存区

![](http://www.yiibai.com/uploads/images/201709/2809/885080931_71346.png)





NIO读写是I/O的基本过程，读写操作中使用的核心部件有通道`Channel`，缓冲区`Buffer`和`Selector`

NIO中的所有I/O都是通过一个通道开始的。数据总是从缓冲区写入通道，并从通道读取到缓冲区。

*数据读取*：创建一个缓冲区，然后请求通道读取数据；

![](http://www.yiibai.com/uploads/images/201709/2809/309170906_70762.png)

*数据写入*：创建一个缓冲区，填充数据，并要求通道写入数据。

![](http://www.yiibai.com/uploads/images/201709/2809/826170907_45719.png)

#### **Q: 同步，异步，阻塞，非阻塞的概念？？**

A: 来自https://blog.csdn.net/qq_39192827/article/details/86558668

以**并发思维**来理解

**同步**：当多个任务要发生时，**任务必须逐个地进行**，一个任务的执行会导致整个流程的暂时等待，这些事件不是并发地执行的。

**异步**：当多个任务要发生时，这些**任务可以并发地执行**，一个任务的执行不会导致整个流程的暂时等待。

**阻塞**：当某个任务在执行过程中发出一个请求操作，但是由于该请求操作需要的条件不满足，那么就会一直在那**等待，直至条件满足**；

**非阻塞**：当某个任务在执行过程中发出一个请求操作，如果该请求操作需要的条件不满足，会立即返回一个标志信息**告知条件不满足，不会一直处于等待状态**。

以**I/O模型思维**来理解：

**同步**：API调用返回时调用者就知道操作结果。

**异步**：API调用返回时调用者不知道操作的结果，后面才会**回调通知**结果。

**阻塞**：当无数据可读，或者不能写入所有数据时，**挂起当前线程等待**。

**非阻塞**：读取时，可以读多少数据就读多少然后返回，写入时，可以写入多少数据就写入多少然后返回。

例如，IO操作包括：对硬盘的读写、对socket的读写以及外设的读写。一个**完整的IO读请求操作**包括两个阶段：

1）查看数据是否就绪；

2）进行数据拷贝（内核将数据拷贝到用户线程）。

对于**同步IO**，当用户发出IO请求操作之后，如果数据没有就绪，需要通过用户线程或者内核不断地去轮询数据是否就绪，当数据就绪时，再将数据从内核拷贝到用户线程；

对于**异步IO**，只有IO请求操作的发出是由用户线程来进行的，IO操作的两个阶段都是**由内核自动完成**，然后发送通知告知用户线程IO操作已经完成。也就是说在异步IO中，不会对用户线程产生任何阻塞。

对于**阻塞IO**：如果数据没有就绪，则会一直在那等待，直到数据就绪；

对于**非阻塞IO**：如果数据没有就绪，则会返回一个标志信息告知用户线程当前要读的数据没有就绪。当数据就绪之后，便将数据拷贝到用户线程，这样才完成了一个完整的IO读请求操作



## NIO组件

### 通道列表

主要使用的通道如下：

- *DatagramChannel*（UDP）
- *SocketChannel*（TCP客户端）
- *FileChannel*（文件）
- *ServerSocketChannel*（TCP服务端）

层次关系

![](http://www.yiibai.com/uploads/images/201709/2809/827080947_43814.png)

#### NIO通道基础

通道实现是使用本地代码执行实际工作。通道接口允许我们以便携和受控的方式访问低级I/O服务。

在层次结构的顶部，通道接口如下所示：

```java
package java.nio.channels;  
 public interface Channel{  
    public boolean isclose();  
    public void Open() throws IOException;  
}
```

所有通道只有两个常用操作：

- 检查通道是否关闭(`isclose()`)
- 打开关闭通道(`close()`)

#### 通道实现

主要使用的通道如下：

- ***FileChannel***：文件通道用于从文件读取数据。它只能通过调用`getChannel()`方法来创建对象。不能直接创建`FileChannel`对象。
  下面是一个创建`FileChannel`对象的例子

```java
FileInputStream fis = new FileInputStream("D:\\file-read.txt");
ReadableByteChannel rbc = fis.getChannel();
```

需要通过使用一个 InputStream、OutputStream 或 RandomAccessFile 来获取一个 FileChannel 实例 

- **DatagramChannel**：数据报通道可以通过UDP(用户数据报协议)通过网络读取和写入数据。它使用工厂方法来创建新对象。

打开`DatagramChannel`的语法：

```java
DatagramChannel ch = DatagramChannel.open();
```

 关闭`DatagramChannel`的语法：

```java
DatagramChannel ch = DatagramChannel.close();
```

- **SocketChannel**：数据报通道可以通过TCP(传输控制协议)通过网络读取和写入数据。 它还使用工厂方法来创建新对象。

打开`SocketChannel`的语法：

```java
SocketChannel ch = SocketChannel.open();  
ch.connect(new InetSocketAddress("somehost", someport));
```

 关闭`SocketChannel`的语法：

```java
SocketChannel ch = SocketChannel.close();  
```

- **ServerSocketChannel**：允许用户监听传入的TCP连接，与Web服务器相同。对于每个传入连接，都会为连接创建一个`SocketChannel`。

打开`ServerSocketChannel`的语法：

```java

ServerSocketChannel ch = ServerSocketChannel.open();  
ch.socket().bind (new InetSocketAddress (somelocalport));

```

 下面是关闭`ServerSocketChannel`的语法：

```java
ServerSocketChannel ch = ServerSocketChannel.close();  
```

#### 文件通道

##### 文件通道示例1：读取通道数据写入 buffer

```java
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
System.out.println(" over !!");
```

执行结果

```java
start read channel data 
data: t
data: e
data: s
data: t
data:  
data:  
data: a
data: b
start read channel data 
data: c
over !!
```

如果把`allocate` 的容量调大，则只会读取通道数据一次

**注意：**

Buffer 通常的操作步骤：

- 读取channel数据写入buffer
- 调用 buffer.flip() 反转读写模式
- 从buffer读取数据
- 调用 buffer.clear() 或 buffer.compact() 清除缓冲区内容 

##### 文件通道示例2：读取buffer数据写入 channel 

```java
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

```



##### 文件通道示例3：复制文件

```java
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
```



##### 文件通道示例4：文件通道之间的数据



批量传输文件数据是非常普遍的，因为几个优化方法已经添加到`FileChannel`类中，使其更有效率。（不在使用缓冲区来转换）

通道之间的数据传输在`FileChannel`类中的两种方法是：

- `FileChannel.transferTo()`方法
- `FileChannel.transferFrom()`方法

**FileChannel.transferTo()方法**

`transferTo()`方法用来从`FileChannel`到其他通道的数据传输。

下面来看一下`transferTo()`方法的例子：

```java
public abstract class Channel extends AbstractChannel  
{    
   public abstract long transferTo (long position, long count, WritableByteChannel target);  
}

```

**FileChannel.transferFrom()方法**

`transferFrom()`方法允许从源通道到`FileChannel`的数据传输。

下面来看看`transferFrom()`方法的例子：

```java
public abstract class Channel extends AbstractChannel  
{    
    public abstract long transferFrom (ReadableByteChannel src, long position, long count);  
}
```

###### Q: 文件通道与网络通道之间的传输

A: transferTo 和 transferFrom 是 FileChannel特有的方法，两个channel中必须有一个是FileChannel



###### Q: **transferTo方法（或者transferFrom方法）、使用缓存区、零拷贝之间的关系**

A: 来源：https://www.jianshu.com/p/82d7269521a3

transferTo方法使用零拷贝来传递数据，底层的操作系统使用sendfile方法不用将数据拷贝到用户态的缓存区中，从而减小拷贝次数和上下文切换



#### Socket通道

SocketChannel 就是 NIO 对于非阻塞 socket 操作的支持的组件，其在 socket 上封装了一层，主要是支持了非阻塞的读写。同时改进了传统的单向流 API，Channel同时支持读写。 

socket 通道类主要分为 DatagramChannel、SocketChannel 和 ServerSocketChannel，它们在被实例化时都会创建一个对等 socket 对象。要把一个socket 通道置于非阻塞模式，我们要依靠所有 socket 通道类的公有超级类：SelectableChannel 

设置或重新设置一个通道的阻塞模式是很简单的，只要调用configureBlocking( )方法即可，传递参数值为 true 则设为阻塞模式，参数值为 false 值设为非阻塞模式。可以通过调用 isBlocking( )方法来判断某个 socket 通道当前处于哪种模式。 

##### ServerSocketChannel 

ServerSocketChannel 是一个基于通道的 socket 监听器，因此能够在非阻塞模式下运行，由于ServerSocketChannel 需要返回 ServerSocket类， 并使用 ServerSocket类来绑定到一个端口以开始监听连接。 

> 代码中，使用ServerSocket类去绑定到指定地址，而ServerSocketChannel类也有`bind()`方法，两者达到的效果是一样的，只需要使用其中之一即可。

ServerSocketChannel 的 accept()方法会返回 SocketChannel 类型对象，SocketChannel 可以在非阻塞模式下运行。 

###### ServerSocket通道示例

```java
public static void main(String[] args) throws IOException, InterruptedException {
    int port = 8888;
    ByteBuffer buffer = ByteBuffer.wrap("hello world".getBytes(StandardCharsets.UTF_8));
    // 新建 ServerSocketChannel
    ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

    // 返回 socket 绑定端口
    serverSocketChannel.socket().bind(new InetSocketAddress(port));
    // 设置非阻塞模式
    serverSocketChannel.configureBlocking(false);

    while (true) {
        System.out.println("Waiting for connections");
        SocketChannel accept = serverSocketChannel.accept();

        if (accept == null) {
            System.out.println("null");
            Thread.sleep(2000);
        } else {
            System.out.println("Incoming connection from: " +
                               accept.socket().getRemoteSocketAddress());
            //重复读取 buffer 数据
            buffer.rewind();
            accept.write(buffer);
            accept.close();
        }

    }
}

```

运行后， 使用浏览器或者 telnet命令来连接本地的8888端口，程序返回

```java
Waiting for connections
null
Waiting for connections
Incoming connection from: /127.0.0.1:2996
Waiting for connections
null
```

##### SocketChannel

SocketChannel 是一个连接到 TCP 网络套接字的通道。 SocketChannel 是一种面向流连接
sockets 套接字的可选择通道。

###### SocketChannel示例

```java
public static void main(String[] args) throws IOException {
    SocketChannel socketChannel = SocketChannel.open();
    socketChannel.connect(new InetSocketAddress("127.0.0.1", 8888));
    // tcp客户端一般不设置 非阻塞模式
    //        socketChannel.configureBlocking(false);
    ByteBuffer byteBuffer = ByteBuffer.allocate(16);
    socketChannel.read(byteBuffer);
    byteBuffer.flip();
    while (byteBuffer.hasRemaining()) {
        System.out.println("data: " + (char) byteBuffer.get());
    }
    socketChannel.close();
    System.out.println("read over");
}

```

先运行 ServerSocketChannel 示例，再运行SocketChannel示例，SocketChannel将展示

```java
data: h
data: e
data: l
data: l
data: o
data:  
data: w
data: o
data: r
data: l
data: d
read over
```



### 缓冲列表

NIO中使用的核心缓冲区如下：

- CharBuffer
- DoubleBuffer
- IntBuffer
- LongBuffer
- ByteBuffer
- ShortBuffer
- FloatBuffer

上述缓冲区覆盖了通过I/O发送的基本数据类型：`characters`，`double`，`int`，`long`，`byte`，`short`和`float`。

利用Buffer读写数据，**通常遵循四个步骤**：

1. 把数据写入buffer；
2. 调用flip；
3. 从Buffer中读取数据；
4. 调用buffer.clear()或者buffer.compact()。

当写入数据到buffer中时，buffer会记录已经写入的数据大小。当需要读数据时，通过 **flip()** 方法把buffer从写模式调整为读模式；在读模式下，可以读取所有已经写入的数据。

当读取完数据后，需要清空buffer，以满足后续写入操作。清空buffer有两种方式：调用 **clear()** 或 **compact()** 方法。**clear会清空整个buffer，compact则只清空已读取的数据**，未被读取的数据会被移动到buffer的开始位置，写入位置则近跟着未读数据之后。

#### 核心属性

https://www.cnblogs.com/snailclimb/p/Buffer.html

capacity: 容量，表示缓冲区的容量。声明后不能改变。
limit: 界限，表示缓冲区中可以操作数据的大小。limit以后的数据不能进行读写
position: 位置，表示缓冲区正在操作的位置
mark: 标记，表示记录当前position的位置，可以通过reset()恢复到mark的位置

由此可以得出：mark<=position<=limit<=capacity

![](https://jenkov.com/images/java-nio/buffers-modes.png)

#### Buffer的常见方法

| 方法                             | 介绍                                                         |
| -------------------------------- | ------------------------------------------------------------ |
| int capacity()                   | 返回此缓冲区的容量                                           |
| Buffer clear()                   | **清除此缓存区**。将position = 0;limit = capacity;mark = -1; |
| Buffer flip()                    | 把Buffer**从写模式切换到读模式**。调用flip方法会把position归零，并设置limit为之前的position的值。 也就是说，现在position代表的是读取位置，limit标示的是已写入的数据位置。 |
| abstract boolean hasArray()      | 告诉这个缓冲区是否由可访问的数组支持                         |
| boolean hasRemaining()           | return position < limit，返回**是否还有未读内容**            |
| abstract boolean isDirect()      | 判断个缓冲区是否为 direct                                    |
| abstract boolean isReadOnly()    | 判断告知这个缓冲区是否是只读的                               |
| int limit()                      | 返回此缓冲区的限制                                           |
| Buffer position(int newPosition) | 设置这个缓冲区的位置                                         |
| int remaining()                  | return limit - position; 返回limit和position之间相对位置差   |
| Buffer rewind()                  | 把position设为0，mark设为-1，不改变limit的值, **可以重复读取buffer中的数据** |
| Buffer mark()                    | 将此缓冲区的标记设置在其位置                                 |
| Buffer reset()                   | 将位置position 恢复到标记mark                                |



#### 分配缓冲区

获得缓冲区对象，我们必须首先分配一个缓冲区。在每个`Buffer`类中，`allocate()`方法用于分配缓冲区。

下面来看看`ByteBuffer`分配容量为`2048`字节的例子：

```java
ByteBuffer buf = ByteBuffer.allocate(2048);
```

**注意**

`ByteBuffer `是一个抽象类，allocate方法返回的是`HeapByteBuffer`类

#### 从缓冲区读取数据

从缓冲区读取数据有两种方法：

- 通过使用`get()`方法读取`Buffer`中的数据。
- 将数据从缓冲区读入通道（调用通道的write方法）。

将`Buffer`中的数据读入通道的例子：

```java
int bytesWritten = inChannel.write(buf);
```

**注意**

将缓冲区读取数据的实现是调用 通道的write方法

#### 将数据写入缓冲区

将数据写入缓冲区有两种方法：

- 使用`put()`方法将数据写入缓冲区。
- 将数据从`Channel`写入缓冲区（调用通道的read方法）。



#### 缓存区的操作

##### 缓冲区分片

除了可以分配或者包装一个缓冲区对象外，还可以根据现有的缓冲区对象来创建一个子缓冲区，即在**现有缓冲区**上切出一片来**作为一个新的缓冲区**，但现有的缓冲区与创建的子缓冲区在底层数组层面上是**数据共享**的，也就是说，子缓冲区相当于是现有缓冲区的一个视图窗口。

调用 **slice()方法**可以创建一个子缓冲区。 

##### 只读缓冲区

只读缓冲区非常简单，可以读取它们，但是不能向它们写入数据。可以通过调用缓冲区的 asReadOnlyBuffer()方法，将任何常规缓冲区转 换为只读缓冲区，这个方法返回一个与原缓冲区完全相同的缓冲区，并与原缓冲区共享数据，只不过它是只读的。如果**原缓冲区**的内容发生了**变化**，**只读缓冲区**的内容也**随之发生变化**

##### 直接缓冲区

直接缓冲区是为加快 I/O 速度，使用一种特殊方式为其分配内存的缓冲区，JDK 文档中的描述为：给定一个直接字节缓冲区，Java 虚拟机将尽最大努力直接对它执行本机I/O 操作。也就是说，它会在每一次调用底层操作系统的本机 I/O 操作之前(或之后)，**尝试避免将缓冲区的**内容拷贝到一个中间缓冲区中 或者从一个中间缓冲区中拷贝数据。
要分配直接缓冲区，需要调用 **allocateDirect()方法**，而不是 allocate()方法，使用方式与普通缓冲区并无区别

##### 内存映射文件 I/O

内存映射文件 I/O 是一种读和写文件数据的方法，它可以比常规的基于流或者基于通道的 I/O 快的多。内存映射文件 I/O 是通过使文件中的数据出现为 内存数组的内容来完成的，这其初听起来似乎不过就是将整个文件读到内存中，但是事实上并不是这样。一般来说，只有文件中实际读取或者写入的部分才会映射到内存中 

##### Q: 直接缓冲区、内存映射文件 I/O、零拷贝、DMA拷贝的关系

A：来源：https://cloud.tencent.com/developer/article/1880436

https://zhuanlan.zhihu.com/p/377237946

DMA拷贝：传统拷贝中，从磁盘文件（网卡）复制到内核态的PageCache是由CPU完成，DMA 技术在IO设备上放一块独立的芯片，在进行内存和 I/O 设备的数据传输的时候，不再通过 CPU 来控制数据传输，而直接通过 DMA 控制器，DMA 仅仅能用于设备之间交换数据时进行数据拷贝，但是设备内部的数据拷贝还需要 CPU 进行。

零拷贝：零拷贝是一个思想，是指计算机执行操作时，CPU 不需要先将数据从某处内存复制到另一个特定区域，零拷贝技术的具体实现方式有很多，例如：

- sendfile：一次代替 read/write 系统调用，通过使用 DMA 技术以及传递文件描述符（**transferTo方法**）

![](https://pic4.zhimg.com/80/v2-557b255dbca2fdd3a5a213cbee7df513_1440w.webp)

- mmap（内存映射文件 I/O）：仅代替 read 系统调用，将内核空间地址映射为用户空间地址，write 操作直接作用于内核空间。通过 DMA 技术以及地址映射技术，用户空间与内核空间无须数据拷贝

![](https://pic2.zhimg.com/80/v2-16ff9ac786b16508711083ed44a8ff79_1440w.webp)

- 直接 Direct I/O：读写操作直接在磁盘上进行，不使用 page cache 机制，通常结合用户空间的用户缓存使用。通过 DMA 技术直接与磁盘/网卡进行数据交互。

![](https://ask.qcloudimg.com/http-save/yehe-5805585/eeec542dddf6e931a9f12ecb1edc1f18.jpeg?imageView2/2/w/1620)

**大文件（GB 级别的文件）传输用什么方式实现？**

PageCache 会不起作用（PageCache 的优点：缓存最近被访问的数据和预读功能），浪费 DMA 多做的一次数据拷贝，造成性能的降低，即使使用了 PageCache 的零拷贝也会损失性能，因此大文件的传输，不应该使用 PageCache，因为**可能由于 PageCache 被大文件占据**，而导致「热点」小文件无法利用到 PageCache，这样在高并发的环境下，会带来严重的性能问题。

针对大文件的传输的方式，应该使用「异步 I/O + 直接 I/O」来替代零拷贝技术

- 前半部分，内核向磁盘发起读请求，但是可以**不等待数据就位就可以返回**，于是进程此时可以处理其他任务；
- 后半部分，当内核将磁盘中的数据拷贝到进程缓冲区后，进程将接收到内核的**通知**，再去处理数据；

![](https://pic2.zhimg.com/80/v2-9f29757d2b45de0ada81b8a32b459b31_1440w.webp)



### 选择器

NIO提供了“选择器”的概念。这是一个可以用于监视多个通道的对象，如数据到达，连接打开等。因此，单线程可以监视多个通道中的数据。

![](http://www.yiibai.com/uploads/images/201709/2809/908110905_69661.png)

如果应用程序有多个通道(连接)打开，但每个连接的流量都很低，则可考虑使用它。 例如：在聊天服务器中。

Selector 一般称 为选择器 ，也可以翻译为 多路复用器 。 用于检查一个或多个 NIO Channel（通道）的状态是否处于可读、可写。如此可以实现单线程管理多个 channels,也就是可以管理多个网络链接。 

不是**所有的 Channel** 都可以被 **Selector 复用**的。比方说，FileChannel 就不能被选择器复用。判断一个 Channel 能被 Selector 复用，有一个前提：判断他**是否继承**了一个**抽象类 SelectableChannel**。如果继承了 SelectableChannel，则可以被复用，否则不能。 

**一个通道**可以被**注册到多个选择器**上，但对每个选择器而言只能被注册一次。通道和选择器之间的关系，使用注册的方式完成。 

#### **Channel 注册到 Selector**

使用 `Channel.register（Selector sel，int ops）`方法，将一个通道注册到一个选择器时。

第一个参数，指定通道要注册的选择器。第二个参数指定选择器需要查询的通道操作 

通道操作从类型来分，包括以下四种：

- 可读 : SelectionKey.OP_READ 
- 可写 : SelectionKey.OP_WRITE
- 连接 : SelectionKey.OP_CONNECT
- 接收 : SelectionKey.OP_ACCEPT 

##### **Todo Q: 通道操作与TCP连接状态之间的关系？？ **

#### 查询就绪操作select方法

通过 Selector 的 select（）方法，可以查询出已经就绪的通道操作，这些就绪的状态集合，保存在一个元素是 SelectionKey 对象的 Set 集合中。 

Selector 几个重载的查询 select()方法： 

- select()：阻塞到至少有一个通道在你注册的事件上就绪了。 
- select(long timeout)：和 select()一样，但最长阻塞事件为 timeout 毫秒 。
- selectNow()：非阻塞，只要有通道就绪就立刻返回 

#### 选择键(SelectionKey)

Channel 注册到后，并且一旦Channel处于某种就绪的状态，就可以被选择器查询到。这个工作，使用选择器 Selector 的 select（）方法完成。select 方法的作用，对感兴趣的通道操作进行就绪状态的查询。  

#### NIO 编程步骤 

第一步：创建 Selector 选择器

第二步：创建 ServerSocketChannel 通道，并绑定监听端口

第三步：设置 Channel 通道是非阻塞模式

第四步：把 Channel 注册到 Socketor 选择器上，监听连接事件

第五步：调用 Selector 的 select 方法（循环调用），监测通道的就绪状况

第六步：调用 selectKeys 方法获取就绪 channel 集合

第七步：遍历就绪 channel 集合，判断就绪事件类型，实现具体的业务操作

第八步：根据业务，决定是否需要再次注册监听事件，重复执行第三步操作 

##### Q: nio的select和linux的epoll有什么区别

A: https://www.zhihu.com/question/343373314

nio的select底层用WindowsSelectorImpl来实现，WindowsSelectorImpl调用Linux的poll函数，Linux上本身有select/poll/epoll 是用来实现多路复用的

liunx 的select通过轮询的方式来实现IO多路复用，缺点是限制1024个fd

poll优化fds 的结构不再是bit 数组，没有限制fd个数

epoll不需要轮询，主动唤醒

#### 服务端Selector示例

```java
public class WebServerSelector {
    public static void main(String[] args) throws IOException {
        // 创建 Selector 选择器
        Selector selector = Selector.open();

        // 创建 ServerSocketChannel 通道，并绑定监听端口
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.bind(new InetSocketAddress("localhost", 8090));

        // 设置 Channel 通道是非阻塞模式
        ssc.configureBlocking(false);

        ByteBuffer readBuff = ByteBuffer.allocate(1024);
        ByteBuffer writeBuff = ByteBuffer.allocate(128);

        // 把 Channel 注册到 Socketor 选择器上，监听连接事件
        int validOps = ssc.validOps();
        ssc.register(selector, validOps);

        while (true) {
            // 监测通道的就绪状况
            // 为什么 nReady 始终为 1 , 不应该是 客户端，服务端两个socket吗
            // 因为select查询出已经就绪的通道操作, 服务端监听的通道操作是接收，
            // 第一次进来的时候是服务端的socket，之后把客户端socket通道注册到selector
            // 之后处理的是客户端的socket
            int nReady = selector.select();
            System.out.println("nReady: " + nReady);
            // 获取就绪 channel 集合
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            // 遍历就绪 channel 集合
            while (iterator.hasNext()) {
                SelectionKey nextKey = iterator.next();
                iterator.remove();
                if (nextKey.isAcceptable()) {
                    SocketChannel socketChannelClient = ssc.accept();
                    socketChannelClient.configureBlocking(false);
                    // 将接收的socket注册到selector中，通道操作为 可读
                    socketChannelClient.register(selector, SelectionKey.OP_READ);
                    System.out.println("The new connection is accepted from the client: " + socketChannelClient);

                } else if (nextKey.isReadable()) {
                    SocketChannel socketChannelClient = (SocketChannel) nextKey.channel();
                    // 捕获异常，客户端在断开连接时服务端继续运行
                    try {
                        readBuff.clear();
                        socketChannelClient.read(readBuff);
                        readBuff.flip();
                        System.out.println("Message read from client: " + new String(readBuff.array()));

                        // 将可读socket的通道操作改为 可写
                        nextKey.interestOps(SelectionKey.OP_WRITE);
                    } catch (Exception e) {
                        socketChannelClient.close();
                        System.out.println("Client disconnected");
                    }

                } else if (nextKey.isWritable()) {
                    // 将 接收响应写到 buffer中
                    writeBuff.clear();
                    writeBuff.put(("received " + System.currentTimeMillis()).getBytes());

                    writeBuff.flip();
                    SocketChannel socketChannel = (SocketChannel) nextKey.channel();
                    // 将服务端的接收响应返回给客户端
                    System.out.println("Message write from the server: " + new String(writeBuff.array()));
                    socketChannel.write(writeBuff);

                    //  将可写 socket的通道操作改为可读
                    nextKey.interestOps(SelectionKey.OP_READ);
                }
            }

        }

    }
}
```



#### 客户端Selector示例

```java
public class WebClientSelector {
    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("127.0.0.1", 8090));
        ByteBuffer writeBuffer = ByteBuffer.allocate(32);
        ByteBuffer readBuffer = ByteBuffer.allocate(32);



        while (true) {
            String dataStr = "hello " + System.currentTimeMillis();
            // 客户端写入数据到 buffer
            writeBuffer.clear();
            writeBuffer.put(dataStr.getBytes());
            // 读取 buffer 的数据，写入到 channel
            writeBuffer.flip();
            socketChannel.write(writeBuffer);

            //读取channel返回的数据，写入到 buffer 中
            readBuffer.clear();
            socketChannel.read(readBuffer);
            // 读取 buffer 中返回的数据
            readBuffer.flip();
            System.out.println("server: " + new String(readBuffer.array()));
        }
    }
}
```



