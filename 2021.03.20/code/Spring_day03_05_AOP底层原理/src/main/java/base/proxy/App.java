package base.proxy;

import com.itheima.service.UserService;
import com.itheima.service.impl.UserServiceImpl;

public class App {
    public static void main(String[] args) {
        UserService userService  = new UserServiceImpl();
        UserService userService1 = UserServiceJDKProxy.createUserServiceJDKProxy(userService);
        userService1.save();
    }
}
