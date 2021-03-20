package base.decorator;

import com.itheima.service.UserService;

public class UserServiceImplDecorator2 implements UserService {

    private UserService userService;

    public UserServiceImplDecorator2(UserService userService){
        this.userService  = userService;
    }

    public void save() {
        //原始调用
        userService.save();
        //增强功能（后置）
        System.out.println("贴壁纸");
    }
}
