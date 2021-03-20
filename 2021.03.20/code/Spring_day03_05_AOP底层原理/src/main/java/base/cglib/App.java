package base.cglib;

import com.itheima.service.UserService;
import com.itheima.service.impl.UserServiceImpl;

public class App {

    public static void main(String[] args) {
        UserService userService = UserServiceCglibProxy.createUserServiceCglibProxy(UserServiceImpl.class);
        userService.save();
    }

}
