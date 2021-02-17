# Lambda表达式

**匿名内部类**

本质：是一个继承了该类或者实现了该接口的子类匿名对象

**Lambda表达式的使用前提【理解】**

- 使用Lambda必须要有接口
- <font color = 'red'>并且要求接口中有且仅有一个抽象方法</font>

**Lambda表达式和匿名内部类的区别【理解】**

- 所需类型不同
  - 匿名内部类：可以是接口，也可以是抽象类，还可以是具体类
  - Lambda表达式：只能是接口
- 使用限制不同
  - 如果接口中有且仅有一个抽象方法，可以使用Lambda表达式，也可以使用匿名内部类
  - 如果接口中多于一个抽象方法，只能使用匿名内部类，而不能使用Lambda表达式
- 实现原理不同
  - 匿名内部类：编译之后，产生一个单独的.class字节码文件
  - Lambda表达式：编译之后，没有一个单独的.class字节码文件。对应的字节码会在运行的时候动态生成

# Stream流

## Stream流的三类方法

- 获取Stream流
  - 创建一条流水线,并把数据放到流水线上准备进行操作
- 中间方法
  - 流水线上的操作
  - 一次操作完毕之后,还可以继续进行其他操作
- 终结方法
  - 一个Stream流只能有一个终结方法
  - 是流水线上的最后一个操作

## 生成Stream流的方式

- Collection体系集合（单列）

  使用默认方法stream()生成流， default Stream<E> stream()

- Map体系集合（双列）

  把Map转成Set集合，间接的生成流

- 数组

  通过Arrays中的静态方法stream生成流

- 同种数据类型的多个数据

  通过Stream接口的静态方法of(T... values)生成流

## 中间方法

中间操作的意思是,执行完此方法之后,Stream流依然可以继续执行其他操作

- 常见方法

  | 方法名                                          | 说明                                                         |
  | ----------------------------------------------- | ------------------------------------------------------------ |
  | Stream<T> filter(Predicate predicate)           | 用于对流中的数据进行过滤                                     |
  | Stream<T> limit(long maxSize)                   | 返回此流中的元素组成的流，截取前指定参数个数的数据           |
  | Stream<T> skip(long n)                          | 跳过指定参数个数的数据，返回由该流的剩余元素组成的流         |
  | static <T> Stream<T> concat(Stream a, Stream b) | 合并a和b两个流为一个流                                       |
  | Stream<T> distinct()                            | 返回由该流的不同元素（根据Object.equals(Object) ）组成的流hashCode和equals |

## 终结方法

常见方法

| 方法名                        | 说明                     |
| ----------------------------- | ------------------------ |
| void forEach(Consumer action) | 对此流的每个元素执行操作 |
| long count()                  | 返回此流中的元素数       |

## 收集操作

<font color = 'red'>**在Stream流中无法直接修改集合等数据源中的数据**</font>

- 常用方法

  | 方法名                         | 说明               |
  | ------------------------------ | ------------------ |
  | R collect(Collector collector) | 把结果收集到集合中 |

- 工具类Collectors提供了具体的收集方式

  | 方法名                                                       | 说明                       |
  | ------------------------------------------------------------ | -------------------------- |
  | public static <T> Collector toList()                         | 把流中元素收集到List集合中 |
  | public static <T> Collector toSet()                          | 把流中元素收集到Set集合中  |
  | public static  Collector toMap(Function keyMapper,Function valueMapper) | 把流中元素收集到Map集合中  |

代码

```java
单列
List<Integer> list = list1.stream().filter(number -> number % 2 == 0)
        .collect(Collectors.toList());
Set<Integer> set = list1.stream().filter(number -> number % 2 == 0)
                .collect(Collectors.toSet());
双列
Map<String, Integer> map = list.stream().filter(
                s -> {
                    String[] split = s.split(",");
                    int age = Integer.parseInt(split[1]);
                    return age >= 24;
                }
         //   collect方法只能获取到流中剩余的每一个数据.
         //在底层不能创建容器,也不能把数据添加到容器当中
         //Collectors.toMap 创建一个map集合并将数据添加到集合当中
          // s 依次表示流中的每一个数据
          //第一个lambda表达式就是如何获取到Map中的键
          //第二个lambda表达式就是如何获取Map中的值
        ).collect(Collectors.toMap(
                s -> s.split(",")[0],
                s -> Integer.parseInt(s.split(",")[1]) ));
```



# 练习

```java
/*
 * 现在有两个ArrayList集合，分别存储6名男演员名称和6名女演员名称，要求完成如下的操作
 * 1.男演员只要名字为3个字的前两人
 * 2.女演员只要姓杨的，并且不要第一个
 * 3.把过滤后的男演员姓名和女演员姓名合并到一起
 * 4.把上一步操作后的元素作为构造方法的参数创建演员对象,遍历数据
 * 演员类Actor，里面有一个成员变量，一个带参构造方法，以及成员变量对应的get/set方法
 */
public class MyStream9 {
    public static void main(String[] args) {
        ArrayList<String>  manList = new ArrayList<>();
        manList.add("张国立");
        manList.add("张晋");
        manList.add("刘烨");
        manList.add("郑伊健");
        manList.add("徐峥");
        manList.add("王宝强");

        ArrayList<String>  womanList = new ArrayList<>();
        womanList.add("郑爽");
        womanList.add("杨紫");
        womanList.add("关晓彤");
        womanList.add("张天爱");
        womanList.add("杨幂");
        womanList.add("赵丽颖");

        //男演员只要名字为3个字的前两人
        Stream<String> stream1 = manList.stream().filter(name -> name.length() == 3).limit(2);

        //女演员只要姓杨的，并且不要第一个
        Stream<String> stream2 = womanList.stream().filter(name -> name.startsWith("杨")).skip(1);


        Stream.concat(stream1,stream2).forEach(name -> {
            Actor actor = new Actor(name);
            System.out.println(actor);
        });
    }
}
```

