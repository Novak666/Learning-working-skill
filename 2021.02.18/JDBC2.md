# 1. 数据库连接池

## 1.1 简介

- 数据库连接背景
  - 数据库连接是一种关键的、有限的、昂贵的资源，这一点在多用户的网页应用程序中体现得尤为突出。对数据库连接的管理能显著影响到整个应用程序的伸缩性和健壮性，影响到程序的性能指标。
- 数据库连接池
  - 数据库连接池负责分配、管理和释放数据库连接，它允许应用程序重复使用一个现有的数据库连接，而不是再重新建立一个。这项技术能明显提高对数据库操作的性能。

## 1.2 自定义连接池

java.sql.DataSource接口：数据源(数据库连接池)。java官方提供的数据库连接池规范(接口)

- 获取数据库连接对象：Connection getConnection();

步骤：

1. 定义一个类，实现DataSource接口
2. 定义一个容器，用于保存多个Connection连接对象
3. 定义静态代码块，通过JDBC工具类获取10个连接保存到容器中
4. 重写getConnection方法，从容器中获取一个连接并返回
5. 定义getSize方法，用于获取容器的大小并返回

- 自定义连接池

```java
/*
	自定义连接池类
*/
public class MyDataSource implements DataSource{
    //定义集合容器，用于保存多个数据库连接对象
    private static List<Connection> pool = Collections.synchronizedList(new ArrayList<Connection>());

    //静态代码块，生成10个数据库连接保存到集合中
    static {
        for (int i = 0; i < 10; i++) {
            Connection con = JDBCUtils.getConnection();
            pool.add(con);
        }
    }

    //返回连接池的大小
    public int getSize() {
        return pool.size();
    }

    //从池中返回一个数据库连接
    @Override
    public Connection getConnection() {
        if(pool.size() > 0) {
            //从池中获取数据库连接
            return pool.remove(0);
        }else {
            throw new RuntimeException("连接数量已用尽");
        }
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return null;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {

    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {

    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }
}
```

## 1.3 测试自定义连接池

```java
public class MyDataSourceTest {
    public static void main(String[] args) throws Exception{
        //创建数据库连接池对象
        MyDataSource dataSource = new MyDataSource();

        System.out.println("使用之前连接池数量：" + dataSource.getSize());
        
        //获取数据库连接对象
        Connection con = dataSource.getConnection();
        System.out.println(con.getClass());// JDBC4Connection

        //查询学生表全部信息
        String sql = "SELECT * FROM student";
        PreparedStatement pst = con.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();

        while(rs.next()) {
            System.out.println(rs.getInt("sid") + "\t" + rs.getString("name") + "\t" + rs.getInt("age") + "\t" + rs.getDate("birthday"));
        }
        
        //释放资源
        rs.close();
        pst.close();
		//目前的连接对象close方法，是直接关闭连接，而不是将连接归还池中
        con.close();

        System.out.println("使用之后连接池数量：" + dataSource.getSize());
    }
}
```

## 1.4 归还连接

### 1.4.1 继承（无法解决）

通过打印连接对象，发现DriverManager获取的连接实现类是JDBC4Connection。

自定义一个类，继承JDBC4Connection这个类，重写close()方法。

1. 定义一个类，继承JDBC4Connection
2. 定义Connection连接对象和连接池容器对象的成员变量
3. 通过有参构造方法完成对成员变量的赋值
4. 重写close方法，将连接对象添加到池中

```java
public class MyConnection1 extends JDBC4Connection{//1.定义一个类，继承JDBC4Connection
    //2.定义Connection连接对象和容器对象的成员变量
    private Connection con;
    private List<Connection> pool;

    //3.通过有参构造方法为成员变量赋值
    public MyConnection1(String hostToConnectTo, int portToConnectTo, Properties info, String databaseToConnectTo, String url,Connection con,List<Connection> pool) throws SQLException {
        super(hostToConnectTo, portToConnectTo, info, databaseToConnectTo, url);
        this.con = con;
        this.pool = pool;
    }

    //4.重写close方法，完成归还连接
    @Override
    public void close() throws SQLException {
        pool.add(con);
    }
}
```

但是这种方式行不通，通过查看JDBC工具类获取连接的方法我们发现：我们虽然自定义了一个子类，完成了归还连接的操作。但是DriverManager获取的还是JDBC4Connection这个对象，并不是我们的子类对象，而我们又不能整体去修改驱动包中类的功能。

```java
//将之前的连接对象换成自定义的子类对象
private static MyConnection1 con;

//4.获取数据库连接的方法
public static Connection getConnection() {
    try {
        //等效于：MyConnection1 con = new JDBC4Connection();  语法错误！
        con = DriverManager.getConnection(url,username,password);
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return con;
}
```

### 1.4.2 装饰者设计模式（有待改进）

自定义一个类，实现Connection接口，具备和JDBC4Connection相同的行为

重写close()方法，完成连接的归还，其余的功能还调用mysql驱动包实现类原有的方法即可

1. 定义一个类，实现Connection接口
2. 定义Connection连接对象和连接池容器对象的成员变量
3. 通过有参构造方法完成对成员变量的赋值
4. 重写close方法，将连接对象添加到池中
5. 剩余方法，只需要调用mysql驱动包的连接对象完成即可
6. 在自定义的连接池中，将获取的连接对象通过自定义连接对象进行包装

```java
//1.定义一个类，实现Connection接口
public class MyConnection2 implements Connection{

    //2.定义连接对象和连接池容器对象的成员变量
    private Connection con;
    private List<Connection> pool;

    //3.通过有参构造方法为成员变量赋值
    public MyConnection2(Connection con,List<Connection> pool) {
        this.con = con;
        this.pool = pool;
    }

    //4.重写close方法，完成归还连接
    @Override
    public void close() throws SQLException {
        pool.add(con);
    }

    //5.剩余方法，还是调用原有的连接对象中的功能即可
    @Override
    public Statement createStatement() throws SQLException {
        return con.createStatement();
    }

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return con.prepareStatement(sql);
    }

    @Override
    public CallableStatement prepareCall(String sql) throws SQLException {
        return con.prepareCall(sql);
    }

    @Override
    public String nativeSQL(String sql) throws SQLException {
        return con.nativeSQL(sql);
    }

    @Override
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        con.setAutoCommit(autoCommit);
    }

    @Override
    public boolean getAutoCommit() throws SQLException {
        return con.getAutoCommit();
    }

    @Override
    public void commit() throws SQLException {
        con.commit();
    }

    @Override
    public void rollback() throws SQLException {
        con.rollback();
    }

    @Override
    public boolean isClosed() throws SQLException {
        return con.isClosed();
    }

    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        return con.getMetaData();
    }

    @Override
    public void setReadOnly(boolean readOnly) throws SQLException {
        con.setReadOnly(readOnly);
    }

    @Override
    public boolean isReadOnly() throws SQLException {
        return con.isReadOnly();
    }

    @Override
    public void setCatalog(String catalog) throws SQLException {
        con.setCatalog(catalog);
    }

    @Override
    public String getCatalog() throws SQLException {
        return con.getCatalog();
    }

    @Override
    public void setTransactionIsolation(int level) throws SQLException {
        con.setTransactionIsolation(level);
    }

    @Override
    public int getTransactionIsolation() throws SQLException {
        return con.getTransactionIsolation();
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return con.getWarnings();
    }

    @Override
    public void clearWarnings() throws SQLException {
        con.clearWarnings();
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        return con.createStatement(resultSetType,resultSetConcurrency);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return con.prepareStatement(sql,resultSetType,resultSetConcurrency);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return con.prepareCall(sql,resultSetType,resultSetConcurrency);
    }

    @Override
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        return con.getTypeMap();
    }

    @Override
    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        con.setTypeMap(map);
    }

    @Override
    public void setHoldability(int holdability) throws SQLException {
        con.setHoldability(holdability);
    }

    @Override
    public int getHoldability() throws SQLException {
        return con.getHoldability();
    }

    @Override
    public Savepoint setSavepoint() throws SQLException {
        return con.setSavepoint();
    }

    @Override
    public Savepoint setSavepoint(String name) throws SQLException {
        return con.setSavepoint(name);
    }

    @Override
    public void rollback(Savepoint savepoint) throws SQLException {
        con.rollback(savepoint);
    }

    @Override
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        con.releaseSavepoint(savepoint);
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return con.createStatement(resultSetType,resultSetConcurrency,resultSetHoldability);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return con.prepareStatement(sql,resultSetType,resultSetConcurrency,resultSetHoldability);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return con.prepareCall(sql,resultSetType,resultSetConcurrency,resultSetHoldability);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        return con.prepareStatement(sql,autoGeneratedKeys);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        return con.prepareStatement(sql,columnIndexes);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        return con.prepareStatement(sql,columnNames);
    }

    @Override
    public Clob createClob() throws SQLException {
        return con.createClob();
    }

    @Override
    public Blob createBlob() throws SQLException {
        return con.createBlob();
    }

    @Override
    public NClob createNClob() throws SQLException {
        return con.createNClob();
    }

    @Override
    public SQLXML createSQLXML() throws SQLException {
        return con.createSQLXML();
    }

    @Override
    public boolean isValid(int timeout) throws SQLException {
        return con.isValid(timeout);
    }

    @Override
    public void setClientInfo(String name, String value) throws SQLClientInfoException {
        con.setClientInfo(name,value);
    }

    @Override
    public void setClientInfo(Properties properties) throws SQLClientInfoException {
        con.setClientInfo(properties);
    }

    @Override
    public String getClientInfo(String name) throws SQLException {
        return con.getClientInfo(name);
    }

    @Override
    public Properties getClientInfo() throws SQLException {
        return con.getClientInfo();
    }

    @Override
    public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        return con.createArrayOf(typeName,elements);
    }

    @Override
    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        return con.createStruct(typeName,attributes);
    }

    @Override
    public void setSchema(String schema) throws SQLException {
        con.setSchema(schema);
    }

    @Override
    public String getSchema() throws SQLException {
        return con.getSchema();
    }

    @Override
    public void abort(Executor executor) throws SQLException {
        con.abort(executor);
    }

    @Override
    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
        con.setNetworkTimeout(executor,milliseconds);
    }

    @Override
    public int getNetworkTimeout() throws SQLException {
        return con.getNetworkTimeout();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return con.unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return con.isWrapperFor(iface);
    }
}
```

自定义连接池类

```java
public class MyDataSource implements DataSource{
    //定义集合容器，用于保存多个数据库连接对象
    private static List<Connection> pool = Collections.synchronizedList(new ArrayList<Connection>());

    //静态代码块，生成10个数据库连接保存到集合中
    static {
        for (int i = 0; i < 10; i++) {
            Connection con = JDBCUtils.getConnection();
            pool.add(con);
        }
    }

    //返回连接池的大小
    public int getSize() {
        return pool.size();
    }

    //从池中返回一个数据库连接
    @Override
    public Connection getConnection() {
        if(pool.size() > 0) {
            //从池中获取数据库连接
            Connection con = pool.remove(0);
            //通过自定义连接对象进行包装
            MyConnection2 mycon = new MyConnection2(con,pool);
            //返回包装后的连接对象
            return mycon;
        }else {
            throw new RuntimeException("连接数量已用尽");
        }
    }
}
```

存在的问题：实现Connection接口后，有大量的方法需要在自定义类中进行重写

### 1.4.3 适配器设计模式（仍然有待改进）

可以提供一个适配器，实现Connection接口，将所有的方法进行实现，除了close方法

自定义连接类只需要继承这个适配器类，重写需要改进的close方法即可

1. 定义一个适配器类，实现Connection接口
2. 定义Connection连接对象的成员变量
3. 通过有参构造方法完成对成员变量的赋值
4. 重写所有除了close的方法，调用mysql驱动包的连接对象完成即可
5. 定义一个连接类，继承适配器类
6. 定义Connection连接对象和连接池容器对象的成员变量，并通过有参构造进行赋值
7. 重写close方法，完成归还连接
8. 在自定义的连接池中，将获取的连接对象通过自定义连接对象进行包装

适配器类

```java
/*
    1.定义一个适配器类。实现Connection接口
    2.定义连接对象的成员变量
    3.通过有参构造为变量赋值
    4.重写所有的抽象方法(除了close)
 */
public abstract class MyAdapter implements Connection {

    //2.定义连接对象的成员变量
    private Connection con;

    //3.通过有参构造为变量赋值
    public MyAdapter(Connection con) {
        this.con = con;
    }


    //4.重写所有的抽象方法(除了close)
    @Override
    public Statement createStatement() throws SQLException {
        return con.createStatement();
    }

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return con.prepareStatement(sql);
    }

    @Override
    public CallableStatement prepareCall(String sql) throws SQLException {
        return con.prepareCall(sql);
    }

    @Override
    public String nativeSQL(String sql) throws SQLException {
        return con.nativeSQL(sql);
    }

    @Override
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        con.setAutoCommit(autoCommit);
    }

    @Override
    public boolean getAutoCommit() throws SQLException {
        return con.getAutoCommit();
    }

    @Override
    public void commit() throws SQLException {
        con.commit();
    }

    @Override
    public void rollback() throws SQLException {
        con.rollback();
    }

    @Override
    public boolean isClosed() throws SQLException {
        return con.isClosed();
    }

    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        return con.getMetaData();
    }

    @Override
    public void setReadOnly(boolean readOnly) throws SQLException {
        con.setReadOnly(readOnly);
    }

    @Override
    public boolean isReadOnly() throws SQLException {
        return con.isReadOnly();
    }

    @Override
    public void setCatalog(String catalog) throws SQLException {
        con.setCatalog(catalog);
    }

    @Override
    public String getCatalog() throws SQLException {
        return con.getCatalog();
    }

    @Override
    public void setTransactionIsolation(int level) throws SQLException {
        con.setTransactionIsolation(level);
    }

    @Override
    public int getTransactionIsolation() throws SQLException {
        return con.getTransactionIsolation();
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return con.getWarnings();
    }

    @Override
    public void clearWarnings() throws SQLException {
        con.clearWarnings();
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        return con.createStatement(resultSetType,resultSetConcurrency);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return con.prepareStatement(sql,resultSetType,resultSetConcurrency);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return con.prepareCall(sql,resultSetType,resultSetConcurrency);
    }

    @Override
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        return con.getTypeMap();
    }

    @Override
    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        con.setTypeMap(map);
    }

    @Override
    public void setHoldability(int holdability) throws SQLException {
        con.setHoldability(holdability);
    }

    @Override
    public int getHoldability() throws SQLException {
        return con.getHoldability();
    }

    @Override
    public Savepoint setSavepoint() throws SQLException {
        return con.setSavepoint();
    }

    @Override
    public Savepoint setSavepoint(String name) throws SQLException {
        return con.setSavepoint(name);
    }

    @Override
    public void rollback(Savepoint savepoint) throws SQLException {
        con.rollback(savepoint);
    }

    @Override
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        con.releaseSavepoint(savepoint);
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return con.createStatement(resultSetType,resultSetConcurrency,resultSetHoldability);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return con.prepareStatement(sql,resultSetType,resultSetConcurrency,resultSetHoldability);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return con.prepareCall(sql,resultSetType,resultSetConcurrency,resultSetHoldability);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        return con.prepareStatement(sql,autoGeneratedKeys);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        return con.prepareStatement(sql,columnIndexes);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        return con.prepareStatement(sql,columnNames);
    }

    @Override
    public Clob createClob() throws SQLException {
        return con.createClob();
    }

    @Override
    public Blob createBlob() throws SQLException {
        return con.createBlob();
    }

    @Override
    public NClob createNClob() throws SQLException {
        return con.createNClob();
    }

    @Override
    public SQLXML createSQLXML() throws SQLException {
        return con.createSQLXML();
    }

    @Override
    public boolean isValid(int timeout) throws SQLException {
        return con.isValid(timeout);
    }

    @Override
    public void setClientInfo(String name, String value) throws SQLClientInfoException {
        con.setClientInfo(name,value);
    }

    @Override
    public void setClientInfo(Properties properties) throws SQLClientInfoException {
        con.setClientInfo(properties);
    }

    @Override
    public String getClientInfo(String name) throws SQLException {
        return con.getClientInfo(name);
    }

    @Override
    public Properties getClientInfo() throws SQLException {
        return con.getClientInfo();
    }

    @Override
    public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        return con.createArrayOf(typeName,elements);
    }

    @Override
    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        return con.createStruct(typeName,attributes);
    }

    @Override
    public void setSchema(String schema) throws SQLException {
        con.setSchema(schema);
    }

    @Override
    public String getSchema() throws SQLException {
        return con.getSchema();
    }

    @Override
    public void abort(Executor executor) throws SQLException {
        con.abort(executor);
    }

    @Override
    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
        con.setNetworkTimeout(executor,milliseconds);
    }

    @Override
    public int getNetworkTimeout() throws SQLException {
        return con.getNetworkTimeout();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return con.unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return con.isWrapperFor(iface);
    }
}
```

自定义连接类

```java
/*
    1.定义一个类，继承适配器类
    2.定义连接对象和连接池容器对象的成员变量
    3.通过有参构造为变量赋值
    4.重写close方法，完成归还连接
 */
//1.定义一个类，继承适配器类
public class MyConnection3 extends MyAdapter {

    //2.定义连接对象和连接池容器对象的成员变量
    private Connection con;
    private List<Connection> pool;

    //3.通过有参构造为变量赋值
    public MyConnection3(Connection con,List<Connection> pool) {
        super(con);
        this.con = con;
        this.pool = pool;
    }

    //4.重写close方法，完成归还连接
    @Override
    public void close() {
        pool.add(con);
    }
}
```

自定义连接池类

```java
public class MyDataSource implements DataSource{
    //定义集合容器，用于保存多个数据库连接对象
    private static List<Connection> pool = Collections.synchronizedList(new ArrayList<Connection>());

    //静态代码块，生成10个数据库连接保存到集合中
    static {
        for (int i = 0; i < 10; i++) {
            Connection con = JDBCUtils.getConnection();
            pool.add(con);
        }
    }

    //返回连接池的大小
    public int getSize() {
        return pool.size();
    }

    //从池中返回一个数据库连接
    @Override
    public Connection getConnection() {
        if(pool.size() > 0) {
            //从池中获取数据库连接
            Connection con = pool.remove(0);

            //通过自定义连接对象进行包装
            //MyConnection2 mycon = new MyConnection2(con,pool);
            MyConnection3 mycon = new MyConnection3(con,pool);

            //返回包装后的连接对象
            return mycon;
        }else {
            throw new RuntimeException("连接数量已用尽");
        }
    }
}
```

存在的问题：适配器类仍是自己编写的

### 1.4.4 动态代理

作用：在不改变目标对象方法的情况下对方法进行增强

组成：

​	被代理的对象：真实的对象

​	代理对象：内存中的一个对象

要求：代理对象必须和被代理对象实现相同的接口

实现：Proxy.newProxyInstance()

```java
public class Student implements StudentInterface{
    public void eat(String name) {
        System.out.println("学生吃" + name);
    }

    public void study() {
        System.out.println("在家自学");
    }
}
```

```java
public interface StudentInterface {
    void eat(String name);
    void study();
}
```

```java
public class Test {
    public static void main(String[] args) {
        Student stu = new Student();
        /*stu.eat("米饭");
        stu.study();*/

        /*
            要求：在不改动Student类中任何的代码的前提下，通过study方法输出一句话：来黑马学习
            类加载器：和被代理对象使用相同的类加载器
            接口类型Class数组：和被代理对象使用相同接口
            代理规则：完成代理增强的功能
         */
        StudentInterface proxyStu = (StudentInterface) Proxy.newProxyInstance(stu.getClass().getClassLoader(), new Class[]{StudentInterface.class}, new InvocationHandler() {
            /*
                执行Student类中所有的方法都会经过invoke方法
                对method方法进行判断
                    如果是study，则对其增强
                    如果不是，还调用学生对象原有的功能即可
             */
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if(method.getName().equals("study")) {
                    System.out.println("来黑马学习");
                    return null;
                }else {
                    return method.invoke(stu,args);
                }
            }
        });

        proxyStu.eat("米饭");
        proxyStu.study();

    }
}
```

### 1.4.5 动态代理方式

可以通过Proxy来完成对Connection实现类对象的代理

如果执行的是close方法，就将连接归还到连接池中。如果是其他方法则调用原来的功能即可

```java
public class MyDataSource implements DataSource{
    //定义集合容器，用于保存多个数据库连接对象
    private static List<Connection> pool = Collections.synchronizedList(new ArrayList<Connection>());

    //静态代码块，生成10个数据库连接保存到集合中
    static {
        for (int i = 0; i < 10; i++) {
            Connection con = JDBCUtils.getConnection();
            pool.add(con);
        }
    }

    //返回连接池的大小
    public int getSize() {
        return pool.size();
    }

    //动态代理方式
    @Override
    public Connection getConnection() {
        if(pool.size() > 0) {
            //从池中获取数据库连接
            Connection con = pool.remove(0);

            Connection proxyCon = (Connection)Proxy.newProxyInstance(con.getClass().getClassLoader(), new Class[]{Connection.class}, new InvocationHandler() {
                /*
                    执行Connection实现类所有方法都会经过invoke
                    如果是close方法，则将连接还回池中
                    如果不是，直接执行实现类的原有方法
                 */
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    if(method.getName().equals("close")) {
                        pool.add(con);
                        return null;
                    }else {
                        return method.invoke(con,args);
                    }
                }
            });

            return proxyCon;
        }else {
            throw new RuntimeException("连接数量已用尽");
        }
    }


    //从池中返回一个数据库连接
    /*@Override
    public Connection getConnection() {
        if(pool.size() > 0) {
            //从池中获取数据库连接
            Connection con = pool.remove(0);

            //通过自定义连接对象进行包装
            //MyConnection2 mycon = new MyConnection2(con,pool);
            MyConnection3 mycon = new MyConnection3(con,pool);

            //返回包装后的连接对象
            return mycon;
        }else {
            throw new RuntimeException("连接数量已用尽");
        }
    }*/
}
```

```java
public class MyDataSourceTest {
    public static void main(String[] args) throws Exception{
        //1.创建连接池对象
        MyDataSource dataSource = new MyDataSource();

        System.out.println("使用之前的数量：" + dataSource.getSize());

        //2.通过连接池对象获取连接对象
        Connection con = dataSource.getConnection();
        System.out.println(con.getClass());

        //3.查询学生表的全部信息
        String sql = "SELECT * FROM student";
        PreparedStatement pst = con.prepareStatement(sql);

        //4.执行sql语句，接收结果集
        ResultSet rs = pst.executeQuery();

        //5.处理结果集
        while(rs.next()) {
            System.out.println(rs.getInt("sid") + "\t" + rs.getString("name") + "\t" + rs.getInt("age") + "\t" + rs.getDate("birthday"));
        }

        //6.释放资源
        rs.close();
        pst.close();
        con.close();    // 用完以后，关闭连接

        System.out.println("使用之后的数量：" + dataSource.getSize());
    }
}
```

存在的问题：自己写的连接池功能不够完善

### 1.4.6 开源连接池C3P0

1. 导入jar包
2. 导入配置文件到src目录下
3. 创建C3P0连接池对象
4. 获取数据库连接进行使用

注意：C3P0的配置文件会自动加载，但是必须叫c3p0-config.xml或c3p0-config.properties

配置文件

```xml
<c3p0-config>
  <!-- 使用默认的配置读取连接池对象 -->
  <default-config>
   <!--  连接参数 -->
    <property name="driverClass">com.mysql.jdbc.Driver</property>
    <property name="jdbcUrl">jdbc:mysql://192.168.59.129:3306/db14</property>
    <property name="user">root</property>
    <property name="password">itheima</property>
    
    <!-- 连接池参数 -->
    <!--初始化的连接数量-->
    <property name="initialPoolSize">5</property>
    <!--最大连接数量-->
    <property name="maxPoolSize">10</property>
    <!--超时时间-->
    <property name="checkoutTimeout">3000</property>
  </default-config>

  <named-config name="otherc3p0"> 
    <!--  连接参数 -->
    <property name="driverClass">com.mysql.jdbc.Driver</property>
    <property name="jdbcUrl">jdbc:mysql://localhost:3306/db15</property>
    <property name="user">root</property>
    <property name="password">itheima</property>
    
    <!-- 连接池参数 -->
    <property name="initialPoolSize">5</property>
    <property name="maxPoolSize">8</property>
    <property name="checkoutTimeout">1000</property>
  </named-config>
</c3p0-config>
```

使用案例1

```java
public class C3P0Test1 {
    public static void main(String[] args) throws Exception{
        //1.创建c3p0的数据库连接池对象
        DataSource dataSource = new ComboPooledDataSource();

        //2.通过连接池对象获取数据库连接
        Connection con = dataSource.getConnection();

        //3.执行操作
        String sql = "SELECT * FROM student";
        PreparedStatement pst = con.prepareStatement(sql);

        //4.执行sql语句，接收结果集
        ResultSet rs = pst.executeQuery();

        //5.处理结果集
        while(rs.next()) {
            System.out.println(rs.getInt("sid") + "\t" + rs.getString("name") + "\t" + rs.getInt("age") + "\t" + rs.getDate("birthday"));
        }

        //6.释放资源
        rs.close();
        pst.close();
        con.close();
    }
}
```

使用案例2

```java
public class C3P0Test2 {
    public static void main(String[] args) throws Exception{
        //1.创建c3p0的数据库连接池对象
        DataSource dataSource = new ComboPooledDataSource();

        //2.测试
        for(int i = 1; i <= 11; i++) {
            Connection con = dataSource.getConnection();
            System.out.println(i + ":" + con);
        //归还连接
            if(i == 5) {
                con.close();
            }
        }
    }
}
```

### 1.4.7 开源连接池Druid

1. 导入jar包
2. 编写配置文件，放在src目录下
3. 通过Properties集合加载配置文件
4. 通过Druid连接池工厂类获取数据库连接池对象
5. 获取数据库连接，进行使用

注意：Druid不会自动加载配置文件，需要我们手动加载，但是文件的名称可以自定义

druid.properties

```properties
driverClassName=com.mysql.jdbc.Driver
url=jdbc:mysql://192.168.59.129:3306/db14
username=root
password=itheima
# 初始化连接数量
initialSize=5
# 最大连接数量
maxActive=10
# 超时时间
maxWait=3000
```

使用案例

```java
/*
    1.通过Properties集合，加载配置文件
    2.通过Druid连接池工厂类获取数据库连接池对象
    3.通过连接池对象获取数据库连接进行使用
 */
public class DruidTest1 {
    public static void main(String[] args) throws Exception{
        //获取配置文件的流对象
        InputStream is = DruidTest1.class.getClassLoader().getResourceAsStream("druid.properties");

        //1.通过Properties集合，加载配置文件
        Properties prop = new Properties();
        prop.load(is);

        //2.通过Druid连接池工厂类获取数据库连接池对象
        DataSource dataSource = DruidDataSourceFactory.createDataSource(prop);

        //3.通过连接池对象获取数据库连接进行使用
        Connection con = dataSource.getConnection();


        String sql = "SELECT * FROM student";
        PreparedStatement pst = con.prepareStatement(sql);

        //4.执行sql语句，接收结果集
        ResultSet rs = pst.executeQuery();

        //5.处理结果集
        while(rs.next()) {
            System.out.println(rs.getInt("sid") + "\t" + rs.getString("name") + "\t" + rs.getInt("age") + "\t" + rs.getDate("birthday"));
        }

        //6.释放资源
        rs.close();
        pst.close();
        con.close();
    }
}
```

## 1.5 抽取工具类

```java
/*
    数据库连接池的工具类
 */
public class DataSourceUtils {
    //1.私有构造方法
    private DataSourceUtils(){}

    //2.声明数据源变量
    private static DataSource dataSource;

    //3.提供静态代码块，完成配置文件的加载和获取数据库连接池对象
    static{
        try{
            //完成配置文件的加载
            InputStream is = DataSourceUtils.class.getClassLoader().getResourceAsStream("druid.properties");

            Properties prop = new Properties();
            prop.load(is);

            //获取数据库连接池对象
            dataSource = DruidDataSourceFactory.createDataSource(prop);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //4.提供一个获取数据库连接的方法
    public static Connection getConnection() {
        Connection con = null;
        try {
            con = dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return con;
    }

    //5.提供一个获取数据库连接池对象的方法
    public static DataSource getDataSource() {
        return dataSource;
    }

    //6.释放资源
    public static void close(Connection con, Statement stat, ResultSet rs) {
        if(con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if(stat != null) {
            try {
                stat.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if(rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void close(Connection con, Statement stat) {
        if(con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if(stat != null) {
            try {
                stat.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
```

```java
public class DruidTest2 {
    public static void main(String[] args) throws Exception{
        //1.通过连接池工具类获取一个数据库连接
        Connection con = DataSourceUtils.getConnection();

        String sql = "SELECT * FROM student";
        PreparedStatement pst = con.prepareStatement(sql);

        //2.执行sql语句，接收结果集
        ResultSet rs = pst.executeQuery();

        //3.处理结果集
        while(rs.next()) {
            System.out.println(rs.getInt("sid") + "\t" + rs.getString("name") + "\t" + rs.getInt("age") + "\t" + rs.getDate("birthday"));
        }

        //4.释放资源
        DataSourceUtils.close(con,pst,rs);
    }
}
```

# 2. JDBC框架（JDBCTemplate）

## 2.1 存在的问题

之前的案例中dao层存在重复代码

定义必要的信息、获取数据库的连接、释放资源都是重复的代码，而我们最终的核心功能仅仅只是执行一条sql语句而已。

所以我们可以抽取出一个JDBC模板类，来封装一些方法(update、query)，专门帮我们执行增删改查的sql语句，将之前那些重复的操作，都抽取到模板类中的方法里，就能大大简化我们的使用步骤。

## 2.2 源信息

- DataBaseMetaData(了解)：数据库的源信息
  - java.sql.DataBaseMetaData：封装了整个数据库的综合信息
  - 例如：
    - String getDatabaseProductName()：获取数据库产品的名称
    - int getDatabaseProductVersion()：获取数据库产品的版本号
- ParameterMetaData：参数的源信息
  - java.sql.ParameterMetaData：封装的是预编译执行者对象中每个参数的类型和属性
  - 这个对象可以通过预编译执行者对象中的getParameterMetaData()方法来获取
  - 核心功能：
    - int getParameterCount()：获取sql语句中参数的个数
- ResultSetMetaData：结果集的源信息
  - java.sql.ResultSetMetaData：封装的是结果集对象中列的类型和属性
  - 这个对象可以通过结果集对象中的getMetaData()方法来获取
  - 核心功能：
    - int getColumnCount()：获取列的总数
    - String getColumnName(int i)：获取列名

## 2.3 update方法

示例代码

```java
/*
    JDBC框架类
 */
public class JDBCTemplate {
    //1.定义参数变量(数据源、连接对象、执行者对象、结果集对象)
    private DataSource dataSource;
    private Connection con;
    private PreparedStatement pst;
    private ResultSet rs;

    //2.通过有参构造为数据源赋值
    public JDBCTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /*
        查询方法：用于将聚合函数的查询结果进行返回
     */
    public Long queryForScalar(String sql, ResultSetHandler<Long> rsh, Object...objs){
        Long value = null;

        try{
            //通过数据源获取一个数据库连接
            con = dataSource.getConnection();

            //通过数据库连接对象获取执行者对象，并对sql语句进行预编译
            pst = con.prepareStatement(sql);

            //通过执行者对象获取参数的源信息对象
            ParameterMetaData parameterMetaData = pst.getParameterMetaData();
            //通过参数源信息对象获取参数的个数
            int count = parameterMetaData.getParameterCount();

            //判断参数数量是否一致
            if(count != objs.length) {
                throw new RuntimeException("参数个数不匹配");
            }

            //为sql语句占位符赋值
            for(int i = 0; i < objs.length; i++) {
                pst.setObject(i+1,objs[i]);
            }

            //执行sql语句并接收结果
            rs = pst.executeQuery();

            //通过ScalarHandler方式对结果进行处理
            value = rsh.handler(rs);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //释放资源
            DataSourceUtils.close(con,pst,rs);
        }

        //返回结果
        return value;
    }

    /*
        用于执行增删改功能的方法
     */
    //3.定义update方法。参数：sql语句、sql语句中的参数
    public int update(String sql,Object...objs) {
        //4.定义int类型变量，用于接收增删改后影响的行数
        int result = 0;

        try{
            //5.通过数据源获取一个数据库连接
            con = dataSource.getConnection();

            //6.通过数据库连接对象获取执行者对象，并对sql语句进行预编译
            pst = con.prepareStatement(sql);

            //7.通过执行者对象获取参数的源信息对象
            ParameterMetaData parameterMetaData = pst.getParameterMetaData();
            //8.通过参数源信息对象获取参数的个数
            int count = parameterMetaData.getParameterCount();

            //9.判断参数数量是否一致
            if(count != objs.length) {
                throw new RuntimeException("参数个数不匹配");
            }

            //10.为sql语句占位符赋值
            for(int i = 0; i < objs.length; i++) {
                pst.setObject(i+1,objs[i]);
            }

            //11.执行sql语句并接收结果
            result = pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //12.释放资源
            DataSourceUtils.close(con,pst);
        }

        //13.返回结果
        return result;
    }
}
```

测试

定义测试类，模拟dao层，测试insert、update、delete语句。

```java
/*
    模拟dao层
 */
public class JDBCTemplateTest {

    private JDBCTemplate template = new JDBCTemplate(DataSourceUtils.getDataSource());

    @Test
    public void delete() {
        //删除数据的测试
        String sql = "DELETE FROM student WHERE name=?";
        int result = template.update(sql, "周七");
        System.out.println(result);
    }

    @Test
    public void update() {
        //修改数据的测试
        String sql = "UPDATE student SET age=? WHERE name=?";
        Object[] params = {37,"周七"};
        int result = template.update(sql, params);
        System.out.println(result);
    }

    @Test
    public void insert() {
        //新增数据的测试
        String sql = "INSERT INTO student VALUES (?,?,?,?)";
        Object[] params = {5,"周七",27,"1997-07-07"};
        int result = template.update(sql, params);
        if(result != 0) {
            System.out.println("添加成功");
        }else {
            System.out.println("添加失败");
        }
    }
}
```

## 2.4 查询数据准备

查询方法

1. 查询一条记录并封装对象的方法：queryForObject()
2. 查询多条记录并封装集合的方法：queryForList()
3. 查询聚合函数并返回单条数据的方法：queryForScalar()

实体类

<font color='red'>注意：成员变量的名称和数据类型要和表中的列保持一致</font>

```java
/*
    学生的实体类
 */
public class Student {
    private Integer sid;
    private String name;
    private Integer age;
    private Date birthday;

    public Student() {
    }

    public Student(Integer sid, String name, Integer age, Date birthday) {
        this.sid = sid;
        this.name = name;
        this.age = age;
        this.birthday = birthday;
    }

    public Integer getSid() {
        return sid;
    }

    public void setSid(Integer sid) {
        this.sid = sid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    @Override
    public String toString() {
        return "Student{" +
                "sid=" + sid +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", birthday=" + birthday +
                '}';
    }
}
```

处理结果集的接口

1. 定义泛型接口ResultSetHandler<T>
2. 定义用于处理结果集的泛型方法<T> T handler(ResultSet rs)

<font color='red'>注意：此接口仅用于为不同处理结果集的方式提供规范，具体的实现类还需要自行编写</font>

```java
/*
    用于处理结果集方式的接口
 */
public interface ResultSetHandler<T> {
    <T> T handler(ResultSet rs);
}
```

## 2.5 BeanHandler()

```java
/*
    实现类1：用于将查询到的一条记录，封装为Student对象并返回
 */
//1.定义一个类，实现ResultSetHandler接口
public class BeanHandler<T> implements ResultSetHandler<T>{
    //2.定义Class对象类型变量
    private Class<T> beanClass;

    //3.通过有参构造为变量赋值
    public BeanHandler(Class<T> beanClass) {
        this.beanClass = beanClass;
    }

    //4.重写handler方法。用于将一条记录封装到自定义对象中
    @Override
    public T handler(ResultSet rs) {
        //5.声明自定义对象类型
        T bean = null;
        try {
            //6.创建传递参数的对象，为自定义对象赋值
            bean = beanClass.newInstance();

            //7.判断结果集中是否有数据
            if(rs.next()) {
                //8.通过结果集对象获取结果集源信息的对象
                ResultSetMetaData metaData = rs.getMetaData();
                //9.通过结果集源信息对象获取列数
                int count = metaData.getColumnCount();

                //10.通过循环遍历列数
                for(int i = 1; i <= count; i++) {
                    //11.通过结果集源信息对象获取列名
                    String columnName = metaData.getColumnName(i);

                    //12.通过列名获取该列的数据
                    Object value = rs.getObject(columnName);

                    //13.创建属性描述器对象，将获取到的值通过该对象的set方法进行赋值
                    PropertyDescriptor pd = new PropertyDescriptor(columnName.toLowerCase(),beanClass);
                    //获取set方法
                    Method writeMethod = pd.getWriteMethod();
                    //执行set方法，给成员变量赋值
                    writeMethod.invoke(bean,value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //14.返回封装好的对象
        return bean;
    }
}
```

## 2.6 queryForObject()

示例代码

```java
/*
    JDBC框架类
 */
public class JDBCTemplate {
    //1.定义参数变量(数据源、连接对象、执行者对象、结果集对象)
    private DataSource dataSource;
    private Connection con;
    private PreparedStatement pst;
    private ResultSet rs;

    //2.通过有参构造为数据源赋值
    public JDBCTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /*
        查询方法：用于将一条记录封装成自定义对象并返回
     */
    public <T> T queryForObject(String sql, ResultSetHandler<T> rsh,Object...objs){
        T obj = null;

        try{
            //通过数据源获取一个数据库连接
            con = dataSource.getConnection();

            //通过数据库连接对象获取执行者对象，并对sql语句进行预编译
            pst = con.prepareStatement(sql);

            //通过执行者对象获取参数的源信息对象
            ParameterMetaData parameterMetaData = pst.getParameterMetaData();
            //通过参数源信息对象获取参数的个数
            int count = parameterMetaData.getParameterCount();

            //判断参数数量是否一致
            if(count != objs.length) {
                throw new RuntimeException("参数个数不匹配");
            }

            //为sql语句占位符赋值
            for(int i = 0; i < objs.length; i++) {
                pst.setObject(i+1,objs[i]);
            }

            //执行sql语句并接收结果
            rs = pst.executeQuery();

            //通过BeanHandler方式对结果进行处理
            obj = rsh.handler(rs);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //释放资源
            DataSourceUtils.close(con,pst,rs);
        }

        //返回结果
        return obj;
    }
}
```

测试

```java
/*
    模拟dao层
 */
public class JDBCTemplateTest {

    private JDBCTemplate template = new JDBCTemplate(DataSourceUtils.getDataSource());

    @Test
    public void queryForObject() {
        //查询一条记录并封装自定义对象的测试
        String sql = "SELECT * FROM student WHERE sid=?";
        Student stu = template.queryForObject(sql,new BeanHandler<>(Student.class),1);
        System.out.println(stu);
    }
}
```

## 2.7 BeanListHandler()

```java
/*
    实现类2：用于将查询到的多条记录，封装为Student对象并添加到集合返回
 */
//1.定义一个类，实现ResultSetHandler接口
public class BeanListHandler<T> implements ResultSetHandler<T>{
    //2.定义Class对象类型变量
    private Class<T> beanClass;

    //3.通过有参构造为变量赋值
    public BeanListHandler(Class<T> beanClass) {
        this.beanClass = beanClass;
    }

    //4.重写handler方法。用于将多条记录封装到自定义对象中并添加到集合返回
    @Override
    public List<T> handler(ResultSet rs) {
        //5.声明集合对象类型
        List<T> list = new ArrayList<>();
        try {
            //6.判断结果集中是否有数据
            while(rs.next()) {
                //7.创建传递参数的对象，为自定义对象赋值
                T bean = beanClass.newInstance();

                //8.通过结果集对象获取结果集源信息的对象
                ResultSetMetaData metaData = rs.getMetaData();
                //9.通过结果集源信息对象获取列数
                int count = metaData.getColumnCount();

                //10.通过循环遍历列数
                for(int i = 1; i <= count; i++) {
                    //11.通过结果集源信息对象获取列名
                    String columnName = metaData.getColumnName(i);

                    //12.通过列名获取该列的数据
                    Object value = rs.getObject(columnName);

                    //13.创建属性描述器对象，将获取到的值通过该对象的set方法进行赋值
                    PropertyDescriptor pd = new PropertyDescriptor(columnName.toLowerCase(),beanClass);
                    //获取set方法
                    Method writeMethod = pd.getWriteMethod();
                    //执行set方法，给成员变量赋值
                    writeMethod.invoke(bean,value);
                }
                //将对象保存到集合中
                list.add(bean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //14.返回封装好的对象
        return list;
    }
}
```

## 2.8 queryForList()

示例代码

```java
/*
    JDBC框架类
 */
public class JDBCTemplate {
    //1.定义参数变量(数据源、连接对象、执行者对象、结果集对象)
    private DataSource dataSource;
    private Connection con;
    private PreparedStatement pst;
    private ResultSet rs;

    //2.通过有参构造为数据源赋值
    public JDBCTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /*
        查询方法：用于将多条记录封装成自定义对象并添加到集合返回
     */
    public <T> List<T> queryForList(String sql, ResultSetHandler<T> rsh, Object...objs){
        List<T> list = new ArrayList<>();

        try{
            //通过数据源获取一个数据库连接
            con = dataSource.getConnection();

            //通过数据库连接对象获取执行者对象，并对sql语句进行预编译
            pst = con.prepareStatement(sql);

            //通过执行者对象获取参数的源信息对象
            ParameterMetaData parameterMetaData = pst.getParameterMetaData();
            //通过参数源信息对象获取参数的个数
            int count = parameterMetaData.getParameterCount();

            //判断参数数量是否一致
            if(count != objs.length) {
                throw new RuntimeException("参数个数不匹配");
            }

            //为sql语句占位符赋值
            for(int i = 0; i < objs.length; i++) {
                pst.setObject(i+1,objs[i]);
            }

            //执行sql语句并接收结果
            rs = pst.executeQuery();

            //通过BeanListHandler方式对结果进行处理
            list = rsh.handler(rs);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //释放资源
            DataSourceUtils.close(con,pst,rs);
        }

        //返回结果
        return list;
    }
}
```

测试

```java
/*
    模拟dao层
 */
public class JDBCTemplateTest {

    private JDBCTemplate template = new JDBCTemplate(DataSourceUtils.getDataSource());

    @Test
    public void queryForList() {
        //查询所有学生信息的测试
        String sql = "SELECT * FROM student";
        List<Student> list = template.queryForList(sql, new BeanListHandler<>(Student.class));
        for(Student stu : list) {
            System.out.println(stu);
        }
    }
}
```

## 2.9 ScalarHandler()

```java
//1.定义一个类，实现ResultSetHandler接口
public class ScalarHandler<T> implements ResultSetHandler<T> {

    //2.重写handler方法
    @Override
    public Long handler(ResultSet rs) {
        //3.定义一个Long类型变量
        Long value = null;
        try{
            //4.判断结果集对象中是否还有数据
            if(rs.next()) {
                //5.获取结果集源信息的对象
                ResultSetMetaData metaData = rs.getMetaData();
                //6.获取第一列的列名
                String columnName = metaData.getColumnName(1);
                //7.根据列名获取该列的值
               value = rs.getLong(columnName);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        //8.返回结果
        return value;
    }
}
```

## 2.10 queryForScalar()

示例代码

```java
/*
    JDBC框架类
 */
public class JDBCTemplate {
    //1.定义参数变量(数据源、连接对象、执行者对象、结果集对象)
    private DataSource dataSource;
    private Connection con;
    private PreparedStatement pst;
    private ResultSet rs;

    //2.通过有参构造为数据源赋值
    public JDBCTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /*
        查询方法：用于将聚合函数的查询结果进行返回
     */
    public Long queryForScalar(String sql, ResultSetHandler<Long> rsh, Object...objs){
        Long value = null;

        try{
            //通过数据源获取一个数据库连接
            con = dataSource.getConnection();

            //通过数据库连接对象获取执行者对象，并对sql语句进行预编译
            pst = con.prepareStatement(sql);

            //通过执行者对象获取参数的源信息对象
            ParameterMetaData parameterMetaData = pst.getParameterMetaData();
            //通过参数源信息对象获取参数的个数
            int count = parameterMetaData.getParameterCount();

            //判断参数数量是否一致
            if(count != objs.length) {
                throw new RuntimeException("参数个数不匹配");
            }

            //为sql语句占位符赋值
            for(int i = 0; i < objs.length; i++) {
                pst.setObject(i+1,objs[i]);
            }

            //执行sql语句并接收结果
            rs = pst.executeQuery();

            //通过ScalarHandler方式对结果进行处理
            value = rsh.handler(rs);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //释放资源
            DataSourceUtils.close(con,pst,rs);
        }

        //返回结果
        return value;
    }
}
```

测试

```java
/*
    模拟dao层
 */
public class JDBCTemplateTest {

    private JDBCTemplate template = new JDBCTemplate(DataSourceUtils.getDataSource());

    @Test
    public void queryForScalar() {
        //查询聚合函数的测试
        String sql = "SELECT COUNT(*) FROM student";
        Long value = template.queryForScalar(sql,new ScalarHandler<Long>());
        System.out.println(value);
    }
}
```