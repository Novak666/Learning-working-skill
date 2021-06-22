package base.proxy;

import com.itheima.service.UserService;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class UserServiceJDKProxy {

    public static UserService createUserServiceJDKProxy(final UserService userService){
        //获取被代理对象的类加载器
        ClassLoader cl = userService.getClass().getClassLoader();
        //获取被代理对象实现的接口
        Class[] classes = userService.getClass().getInterfaces();
        //对原始方法执行进行拦截并增强
        InvocationHandler ih = new InvocationHandler() {
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                //原始调用
                Object ret = method.invoke(userService, args);
                //后置增强内容
                System.out.println("刮大白2");
                System.out.println("贴壁纸2");
                return ret;
            }
        };
        //使用原始被代理对象创建新的代理对象
        UserService service = (UserService )Proxy.newProxyInstance(cl,classes,ih);
        return service;
    }

}