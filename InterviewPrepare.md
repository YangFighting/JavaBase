# Interview Prepare

## 基础运算

```java
  public static void main(String[] args) {
        int i = 1;
        i = i++;
        int j = i++;
        int k = i + ++i * i++;
        System.out.println("i=" + i);
        System.out.println("j=" + j);
        System.out.println("k=" + k);
    }

```

运行第4行后 `i = 2, j= 1`

第5行等价于`int k = i++ +i * i++`， 等价于`2 + 3*3` 

运行结果：

```shell
i=4
j=1
k=11
```

总结

- 赋值=，最后计算
- =右边的从左到右加载值依次压入操作数栈
- 实际先算哪个，看运算符优先级
- 自增、自减操作都是直接修改变量的值，不经过操作数栈
- 最后的赋值之前，临时结果也是存储在操作数栈中



## 单例模式示例

单例模式(Singleton Pattern)：确保某一个类只有一个实例，而且自行实例化并向整个系统提供这个实例，这个类称为单例类，它提供全局访问的方法。单例模式是一种对象创建型模式。

单例模式实现过程

1. 将该类的构造函数私有化（目的是禁止其他程序创建该类的对象）
2. 在本类中自定义一个对象（既然禁止其他程序创建该类的对象，就要自己创建一个供程序使用，否则类就没法用，更不是单例）
3. 提供一个可访问类自定义对象的类成员方法（对外提供该对象的访问方式）

而程序调用类中方法只有两种方式：1. 创建类的一个对象，用该对象去调用类中方法；2. 使用类名直接调用类中方法，格式“类名.方法名()”；

上面说了，构造函数私有化后第一种情况就不能用，只能使用第二种方法。而使用类名直接调用类中方法，类中方法必须是静态的，而静态方法不能访问非静态成员变量，因此类自定义的实例变量也必须是静态的

### 饿汉式单例类

```java
public class EagerSingletonClass {

    String name;
    private static final EagerSingletonClass EAGER_SINGLETON_CLASS_INSTANCE = new EagerSingletonClass();

    public static EagerSingletonClass getInstance() {
        return EAGER_SINGLETON_CLASS_INSTANCE;
    }

    private EagerSingletonClass() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
```

测试

```java
EagerSingletonClass eagerSingletonClass1 = EagerSingletonClass.getInstance();
EagerSingletonClass eagerSingletonClass2 = EagerSingletonClass.getInstance();
System.out.println(eagerSingletonClass1);
System.out.println(eagerSingletonClass2);
```

结果

```shell
com.yang.singleton.pojo.EagerSingletonClass@49e4cb85
com.yang.singleton.pojo.EagerSingletonClass@49e4cb85
```



#### 饿汉式单例类总结

优点：无须考虑多线程访问问题，可以确保实例的唯一性；从调用速度和反应时间角度来讲，由于单例对象一开始就得以创建，因此要优于懒汉式单例。

缺点：由于在类加载时该对象就需要创建，因此从资源利用效率角度来讲，饿汉式单例不及懒汉式单例，而且在系统加载时由于需要创建饿汉式单例对象，加载时间可能会比较长

### 懒汉式单例类

```java
public class LazySingletonClass {
    String name;
    private static LazySingletonClass instance = null;

    private LazySingletonClass() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static LazySingletonClass getInstance() {
        if (instance == null) {
            instance = new LazySingletonClass();
        }
        return LazySingletonClass.instance;
    }
}

```

测试

```java
public class LazySingletonClassTest implements Runnable  {

    @Override
    public void run() {
        LazySingletonClass lazySingletonClass = LazySingletonClass.getInstance();
        System.out.println(lazySingletonClass);
    }


    public static void main(String[] args) {
        LazySingletonClassTest lazySingletonClassTest = new LazySingletonClassTest();

        new Thread(lazySingletonClassTest).start();
        new Thread(lazySingletonClassTest).start();
        new Thread(lazySingletonClassTest).start();
        new Thread(lazySingletonClassTest).start();
        new Thread(lazySingletonClassTest).start();
        new Thread(lazySingletonClassTest).start();
        new Thread(lazySingletonClassTest).start();
    }
}
```

结果

```shell
com.yang.singleton.pojo.LazySingletonClass@5d9bb358
com.yang.singleton.pojo.LazySingletonClass@5d9bb358
com.yang.singleton.pojo.LazySingletonClass@65ede110
com.yang.singleton.pojo.LazySingletonClass@5d9bb358
com.yang.singleton.pojo.LazySingletonClass@5d9bb358
com.yang.singleton.pojo.LazySingletonClass@5d9bb358
com.yang.singleton.pojo.LazySingletonClass@5d9bb358
```

#### 懒汉式单例总结

优点：延迟加载(Lazy Load)技术，在第一次调用getInstance()方法时实例化，在类加载时并不自行实例化

缺点：多个线程同时调用getInstance()方法，存在线程不安全的问题

#### 带双重检查锁的懒汉式单例

在懒汉式单例类在**getInstance()**方法前面增加了关键字**synchronized**进行线程锁，这种方式虽然解决了线程安全问题，但是每次调用getInstance()时都需要进行线程锁定判断，在多线程高并发访问环境中，将会导致系统性能大大降低。

懒汉式单例再次进行改进，只对其中的代码“instance = new LazySingleton();”进行锁定

```java

public static LazySingleton getInstance() { 
    if (instance == null) {
        synchronized (LazySingleton.class) {
            instance = new LazySingleton(); 
        }
    }
    return instance; 
}
```

这种方式还是会存在单例对象不唯一，原因是instance == null 不能保证线程安全

再次改进，使用双重检查锁定实现的懒汉式单例

```
public class LazySingletonWithDoubleCheck {
    private static LazySingletonWithDoubleCheck instance = null;

    private String name;

    private LazySingletonWithDoubleCheck() {

    }

    public static LazySingletonWithDoubleCheck getInstance() {
        if (instance == null) {
            synchronized (LazySingletonWithDoubleCheck.class) {
                if (instance == null) {
                    instance = new LazySingletonWithDoubleCheck();
                }
            }
        }
        return instance;
    }
}

```

sonar 报错 This method may contain an instance of double-checked locking（DCL）

这是由于指令重排序引起的。指令重排序是为了优化指令，提高程序运行效率例如 instance = new [Singleton](https://so.csdn.net/so/search?q=Singleton&spm=1001.2101.3001.7020)() 可分解为如下伪代码

```java
memory = allocate();   //1：分配对象的内存空间
ctorInstance(memory);  //2：初始化对象
instance = memory;     //3：设置instance指向刚分配的内存地址
```

经过重排序后如下：

```java
memory = allocate();   //1：分配对象的内存空间
instance = memory;     //3：设置instance指向刚分配的内存地址
                       //注意，此时对象还没有被初始化！
ctorInstance(memory);  //2：初始化对象
```

将第2步和第3步调换顺序，在单线程情况下不会影响程序执行的结果，但是在多线程情况下就不一样了。线程A执行了instance = memory（这对另一个线程B来说是可见的），此时线程B执行外层 if (instance == null)，发现instance不为空，随即返回，但是 得到的却是未被完全初始化的实例，在使用的时候必定会有风险，这正是双重检查锁定的问题所在！

在JDK1.5之后，可以使用**volatile变量**禁止指令重排序，让DCL生效

```java
public class LazySingletonWithDoubleCheck {
    private volatile static LazySingletonWithDoubleCheck instance = null;
    private String name;

    private LazySingletonWithDoubleCheck() {

    }

    public static LazySingletonWithDoubleCheck getInstance() {
        if (instance == null) {
            synchronized (LazySingletonWithDoubleCheck.class) {
                if (instance == null) {
                    instance = new LazySingletonWithDoubleCheck();
                }
            }
        }
        return instance;
    }
}

```

### 静态内部类的单例

```java
public class InnerSingletonClass {
    private String name;

    private InnerSingletonClass() {
    }

    private static class Inner {
        private static final InnerSingletonClass instance = new InnerSingletonClass();
    }


    public static InnerSingletonClass getInstance() {
        return Inner.instance;
    }
}
```

#### 静态内部类单例总结

在内部类被加载和初始化时，才创建实例对象，静态内部类不会自动随着外部类的加载和初始化而初始化，它是要单独去加载和初始化的。
因为是在内部类加载和初始化时创建的，因此是线程安全的

### 枚举实现

**克隆、反射和反序列化对单例模式的破坏**

除了直接通过new和使用工厂来创建对象以外，还可以通过克隆、反射和反序列化等方式来创建对象

- 为了防止客户端使用克隆方法来创建对象，单例类不能实现Cloneable接口，即不能支持clone()方法。

-  由于反射可以获取到类的构造函数，包括私有构造函数，因此反射可以生成新的对象。

  采用一些传统的实现方法都不能避免客户端通过反射来创建新对象，此时，我们可以通过枚举单例对象的方式来解决该问题

- 在原型模式中，我们可以通过反序列化实现深克隆，反序列化也会生成新的对象。具体来说就是每调用一次readObject()方法，都将会返回一个新建的实例对象，这个新建的实例对象不同于类在初始化时创建的实例对象。   

  那么，如何防止反序列化创建对象呢？解决方法一是类不能实现Serializable接口，即不允许该类支持序列化，这将导致类的应用受限制（有时候我们还是需要对一个对象进行持久化处理）；解决方法二就是本文将要详细介绍的枚举实现。

#### 简单实现

```java
public enum SingletonEnum {
    INSTANCE;

    public static SingletonEnum getInstance() {
        return INSTANCE;
    }

    public void businessMethod() {
        // 处理业务方法
        System.out.println("我是一个单例！");
    }
}

```

测试

```java
SingletonEnum singletonClass3 = SingletonEnum.INSTANCE;
SingletonEnum singletonClass4 = SingletonEnum.INSTANCE;
System.out.println(singletonClass3.getClass());
System.out.println(singletonClass4.getClass());
```

#### 改造单例类

```java
package com.yang.singleton.pojo;

public class EnumSingletonClass {
    String name;

    private EnumSingletonClass() {
    }

    enum SingletonClassEnum {
        INSTANCE;

        private EnumSingletonClass enumSingletonClass = null;

        //私有化枚举的构造函数
        SingletonClassEnum() {
            enumSingletonClass = new EnumSingletonClass();
        }

        public EnumSingletonClass getInstnce() {
            return enumSingletonClass;
        }
    }

    public static EnumSingletonClass getInstnce() {
        return SingletonClassEnum.INSTANCE.getInstnce();
    }

}

```

在代码中，将EnumSingletonClass类的构造函数设置为private私有的，然后在EnumSingletonClass类中定义一个静态的枚举类型SingletonClassEnum默认就是静态的。

在SingletonClassEnum中定义了枚举类型的实例对象INSTANCE，再按照单例模式的要求在其中定义一个EnumSingletonClass类型的对象instance，其初始值为null；我们需要将SingletonClassEnum的构造函数改为私有的（默认就是私有的），在私有构造函数中创建一个EnumSingletonClass的实例对象；最后在getInstance()方法中返回该对象。**Java虚拟机会保证枚举类型不能被反射并且构造函数只被执行一次**。

只需要在其中增加一个内部枚举类型来存储和创建它的唯一实例即可，这和前面的静态内部类的实现有点相似，但是枚举实现可以很好地解决反射和反序列化会破坏单例模式的问题，提供了一种更加安全和可靠的单例模式实现机制。

## 类的创建和初始化

来源： https://juejin.cn/post/7025806136322293773

这里类的创建主要指用构造方法的方法，即使用关键字new



### 加载

加载 是 “类加载”（Class Loading）过程的一个阶段，在加载阶段，虚拟机需要完成以下 3 件事情：

1. 通过一个类的全限定名来获取定义此类的二进制字节流。
2. 将这个字节流所代表的静态存储结构转换为方法区的运行时数据结构。
3. 在内存中生成一个代表这个类的 java.lang.Class 对象，作为方法区这个类的各种数据的访问入口。

**加载阶段完成**后，虚拟机外部的二进制字节流就按照虚拟机所需的格式存储在方法区之中，加载阶段与连接阶段的部分内容（如一部分字节码文件格式验证动作）是交叉进行的，加载阶段尚未完成，连接阶段可能已经开始

### 验证

验证是连接阶段的第一步，这一阶段的目的是为了确保 Class 文件的字节流中包含的信息符合当前虚拟机的要求，并且不会危害虚拟机自身的安全，验证阶段大致上会完成下面 4 个阶段的检验动作：文件格式验证、元数据验证、字节码验证、符号引用验证。

### 准备

准备阶段是正式为类变量分配内存并设置类变量初始值的阶段，

1. 这时候进行内存分配的仅包括类变量（被 static 修饰的变量），而不包括实例变量
2. 这里所说的初始值 “通常情况” 下是数据类型的零值

在 “通常情况” 下初始值是零值，那相对的会有一些 “特殊情况”：如果类字段属性表中存在 ConstantValue 属性，那在准备阶段变量 value 就会被初始化为 ConstantValue 属性所指定的值，假设上面类变量 value 的定义变为：

```java
public static final int value = 123;
```

编译时 javac 将会为 value 生成 ConstantValue 属性，在准备阶段虚拟机就会根据 ConstantValue 的设置将 value 复制为 123。

#### 分配内存（todo 这里涉及到jvm，需要扩展）

在类加载检查通过后，接下来虚拟机将为新生对象分配内存。对象所需内存的大小在类加载完成后便可完全确定，为对象分配空间的任务等同于把一块确定大小的内存从Java堆中划分出来。

##### 内存分配方式

##### 内存分配时的安全问题

##### 空间初始化

内存分配完成后,虚拟机需要**将分配到的内存空间都初始化为零值(不包括对象头)**， 如果使用TLAB方式分配内存 ，这一工作过程也可以提前至TLAB分配时进行。

这一步操作保证了对象的实例字段在Java代码中可以不赋初始值就直接使用,程序能访问到这些字段的数据类型所对应的零值。

### 解析

解析阶段是虚拟机将常量池内的符号引用替换为直接引用的过程

### 初始化

类初始化阶段是类加载过程的最后一步，初始化阶段是执行类构造器 \<clinit>() 方法的过程，执行所有类变量的赋值动作和静态语句块（static{} 块）中的语句

### 执行< init >方法（new关键字专属）

从虚拟机的视角来看,一个新的对象已经产生了，但从Java程序的视角来看，对象创建才刚刚开始——`< init >`方法（实例初始化方法，类初始化方法是`< clinit >` ）还没有执行，所有的字段都还为零值。 所以，一般来说(**由字节码中是否跟随invokespecial指令所决定，不走构造器的初始化方式没有这条指令**)，执行new指令之后会接着 执行< init >方法（**子类的< init >方法中会首先对父类< init >方法的调用**），把对象按照程序员的意愿进行初始化。然后将内存地址赋给栈内存中的变量，这样一个真正可用的对象才算完全产生出来

**特别注意，对象初始化和指向栈空间内存这两步，先后发生顺序是随机的。这对单例模式中的双重检查锁具有重要影响！可能直接导致单例模式的失效！（使用JDK1.5之后volatile关键字可以避免，但是之前的当本则无法保证！）**

## < init >和< clinit >

`< init >`这个方法名称是由编译器命名的， 因为它并非一个合法的Java方法名字， 不可能通过程序编码的方式实现。实例初始化方法只能在实例的初始化期间， 通过Java虚拟机的`invokespecial`指令来调用， 而且只能在尚未初始化的实例上调用该指令。构造器的访问权限，也会约束由该构造器所衍生出来的实例初始化方法。

`< clinit >` 这个名字也是由编译器命名的， 因为它并非一个合法的Java方法名字， 不可能通过Java程序编码的方式直接实现。类或接口的初始化方法由Java虚拟机自身隐式调用， 没有任何虚拟机字节码指令可以调用这个方法， 它只会在类的初始化阶段中由虚拟机自身调用。

`< clinit>`方法对于类或接口来说并不是必须的，如果**一个类/接口中没有静态语句块，也没有对类变量的赋值操作，或者该类声明了类变量，但没有明确使用类变量初始化语句或静态初始化语句初始化或者该类仅包含static final 变量的类变量初始化语句，并且类变量初始化语句是编译时常量表达式**，那么编译器可以不为这个类生成< clinit>()方法。

### 区别

`< init >`是对象构造器方法，也就是说在程序执行 **new 一个对象调用该对象类的构造方法时才会执行< init >方法**，而`< clinit >`是类构造器方法，也就是在jvm进行类加载—验证—解析—初始化中的**初始化阶段jvm会调用< clinit >方法**。

### 加载顺序

**< init >将语句块、变量初始化、调用父类的构造器等操作放到该方法中，顺序为：**

1. 父类变量初始化块/父类非静态代码块（按代码编写顺序）。
2. 父类构造函数。
3. 子类变量初始化块/子类非静态代码块（按代码编写顺序）。
4. 子类构造函数。

**< clinit >将静态语句块、静态变量初始化等操作放到该方法中，顺序为：**

1. 父类静态变量初始化/父类静态语句块（按代码编写顺序）。
2. 子类静态变量初始化/子类静态语句块（按代码编写顺序）。

### 代码说明

```java

public class Father{
	private int i = test();
	private static int j = method();
	
	static{
		System.out.print("(1)");
	}
	Father(){
		System.out.print("(2)");
	}
	{
		System.out.print("(3)");
	}
	
	
	public int test(){
		System.out.print("(4)");
		return 1;
	}
	public static int method(){
		System.out.print("(5)");
		return 1;
	}
}
```

父类的初始化\<clinit>：

1. j = method();
2. 父类的静态代码块

父类的实例化方法：

1. super()（最前）
2. i = test();
3. 父类的非静态代码块
4. 父类的无参构造（最后）

非静态方法前面其实有一个默认的对象this
this在构造器（或\<init>）它表示的是正在创建的对象，因为这里是在创建Son对象，所以**test()执行的是子类重写的代码**（面向对象多态）

这里i=test()执行的是子类重写的test()方法

```java

public class Son extends Father{
	private int i = test();
	private static int j = method();
	static{
		System.out.print("(6)");
	}
	Son(){
//		super();//写或不写都在，在子类构造器中一定会调用父类的构造器
		System.out.print("(7)");
	}
	{
		System.out.print("(8)");
	}
	public int test(){
		System.out.print("(9)");
		return 1;
	}
	public static int method(){
		System.out.print("(10)");
		return 1;
	}
	public static void main(String[] args) {
		Son s1 = new Son();
		System.out.println();
		Son s2 = new Son();
	}
}
```

子类的初始化\<clinit>：

1. j = method();
2. 子类的静态代码块

先初始化父类：(5) (1)
初始化子类：（10）(6)

子类的实例化方法\<init>：

1. super()（最前）      （9）（3）（2）
2. i = test();    （9）
3. 子类的非静态代码块    （8）
4. 子类的无参构造（最后） （7）

因为创建了两个Son对象，因此实例化方法\<init>执行两次

（9）（3）（2）（9）（8）（7）



## String、包装类等对象的不可变性

### Java的数据类型分为两大类

- 基本数据类型：包括整数 、浮点数 、字符 、布尔
- 引用数据类型：包括 类、数组、接口

#### 八大基本类型

- byte，占一个字节

- short，占两个字节

- char，占两个字节

- int，占4个字节

- long，占8个字节

- float，占4个字节

- double，占8个字节
- boolean，占1位

boolean，占1位，但计算机的最小处理单位是字节，《Java虚拟机规范》一书中的描述：“虽然定义了boolean这种数据类型，但是只对它提供了非常有限的支持。在Java虚拟机中没有任何供boolean值专用的字节码指令，Java语言表达式所操作的boolean值，在编译之后都使用Java虚拟机中的int数据类型来代替，而boolean数组将会被编码成Java虚拟机的byte数组，每个元素boolean元素占8位”。所以java中boolean的大小取决于虚拟机实现是否按照规范来，所以1个字节、4个字节都是有可能的

#### 包装类

包装类（Byte，Short，Integer，Long，Double，Float，Charater，Boolean），从jdk 1.5开始，Java提供了自动装箱和自动拆箱的功能。自动装箱就是可以把一个基本类型变量赋给对应的包装类变量。自动拆箱与之相反。

包装类提供了**基本类型变量**和**字符串**之间的转换的方法。有两种方式把字符串类型值转换成基本类型的值

- 利用包装类提供的parseXxx(String s)静态方法（除了Character之外的所有包装类都提供了该方法。）
- 利用包装类提供的Xxx(String s)构造器，或者使用Xxx.valueOf(String s)

**String类**提供了多个重载valueOf()方法，用于将基本类型变量转换成字符串。

### 基本类型变量和引用类型变量

#### 基本类型变量：

- 在内存种存储的是一个基本类型值。
- 可以在栈中直接分配内存

#### 引用类型变量：

- 对应内存所存储的值是一个引用，是对象的存储地址。
- 对象的引用在栈中，对象实际存放在堆中。

#### 赋值问题

##### **基本类型**

将一个对对象的实际值赋给另一个变量

```java
i=1; 

j=2;

j = i; 
```

将基本类型变量 i 的内容复制给基本类型变量 j。

##### 引用类型

将一个变量的引用赋给另一个变量

```java
c1= new Circle(9); 

c2= new Circle(6); 

c2=c1; 
```

将c2的引用赋给c1,赋值之后，变量c2和c1指向同一个对象。

### String类、包装类的不可变性

不可变类的意思是创建该类的实例后，该实例的实例变量是不可改变的，Java提供的8个包装类和String类都是不可变类

String类存储值的底层实际上是一个私有final类型的字符数组，因此在JVM运行的时候是把"hello"当成常量存储在运行时常量池内部。只有一个"hello"常量，而变量s1和s2都指向同一个"hello"常量

![](https://img-blog.csdnimg.cn/2020050618063070.png)



**包装类Integer、Long等有相同的不可变性**

### ==和equals方法

== 用来判断基本数据类型时，当且仅当变量的数据类型和变量的值都一致时才返回true。== 用来判断引用类型变量时，只有当它们指向同一个对象时才返回true

equals方法是Object类提供的一个实例方法，因此所有引用变量都可调用该方法来判断是否等于引用变量，但使用这个方法与==运算符没有区别，同样要求两个引用变量指向同一个对象才返回true。如果希望采用自定义的相等标准，则可采用重写equals方法实现

### 代码测试

```java
public class Exam4 {
    public static void main(String[] args) {
        int i = 1;
        String str = "hello";
        Integer num = 200;
        int[] arr = {1, 2, 3, 4, 5};
        MyData my = new MyData();


        change(i, str, num, arr, my);

        System.out.println("i = " + i);
        System.out.println("str = " + str);
        System.out.println("num = " + num);
        System.out.println("arr = " + Arrays.toString(arr));
        System.out.println("my.a = " + my.a);
    }

    public static void change(int j, String s, Integer n, int[] a, MyData m) {
        j += 1;
        s += "world";
        n += 1;
        a[0] += 1;
        m.a += 1;
    }
}

class MyData {

    public int a = 10;

}
```

运行结果

```shell
i = 1
str = hello
num = 200
arr = [2, 2, 3, 4, 5]
my.a = 11
```

涉及到方法的参数传递机制

- 形参是基本数据类型时，传递数据值
- 行参是引用数据类型时，传递地址值（数组），特殊的类型（String、包装类Integer等对象）具有不可变性



## n步台阶

编程题：有n步台阶，一次只能上1步或2步，共有多少种走法？

### 递归

```java
public class TestStepRecursion {
    public long f(int n) {
        if (n < 1) {
            return 0;
        }
        if (n == 1 || n == 2) {
            return n;
        }
        return f(n - 2) + f(n - 1);
    }

    @Test
    public void test() {
        long start = System.currentTimeMillis();
        System.out.println(f(50));//20365011074
        long end = System.currentTimeMillis();
        System.out.println(end - start);//49590 ms
    }
}
```

### 动态规划

```java
public class TestStepDynamic {
    // 用数据记录计算过的值
    public static long[] A = new long[1000];

    public static long f(int n) {
        if (n <= 0) {
            return 0;
        }
        if (n == 1 || n == 2) {
            A[n] = n;
            return n;
        }
        if (A[n] > 0) {
            // 数组没有初始化
            return A[n];
        } else {
            A[n] = f(n - 1) + f(n - 2);
            return A[n];
        }
    }

    @Test
    public void test() {
        long start = System.currentTimeMillis();
        System.out.println(f(50)); // 20365011074
        long end = System.currentTimeMillis();
        System.out.println(end - start);//<1ms
    }

}
```

### 循环调用

```java
public class TestStepLoop {

    public long loop(int n) {
        if (n < 1) {
            return 0;
        }
        if (n == 1 || n == 2) {
            return n;
        }

        long one = 2;//初始化为走到第二级台阶的走法
        long two = 1;//初始化为走到第一级台阶的走法
        long sum = 0;

        for (int i = 3; i <= n; i++) {
            //最后跨2步 + 最后跨1步的走法
            sum = two + one;
            two = one;
            one = sum;
        }
        return sum;
    }

    @Test
    public void test() {
        long start = System.currentTimeMillis();
        System.out.println(loop(50));//20365011074
        long end = System.currentTimeMillis();
        System.out.println(end - start);//<1ms
    }

}

```

