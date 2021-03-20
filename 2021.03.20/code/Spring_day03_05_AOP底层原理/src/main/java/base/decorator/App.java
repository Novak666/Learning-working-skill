package base.decorator;

import com.itheima.service.UserService;
import com.itheima.service.impl.UserServiceImpl;

public class App {
    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();
        UserService userService1 = new UserServiceImplDecorator(userService);
        UserService userService2 = new UserServiceImplDecorator2(userService1);
        userService2.save();
    }
}
