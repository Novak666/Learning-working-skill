import com.itheima.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class UserApp {
    public static void main(String[] args) {

//        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
//        UserService userService1 = (UserService) ctx.getBean("userService3");
//        UserService userService2 = (UserService) ctx.getBean("userService3");
//        UserService userService3 = (UserService) ctx.getBean("userService3");
//        System.out.println(userService1);
//        System.out.println(userService2);
//        System.out.println(userService3);
//        System.out.println(userService1 == userService2);

//        userService.save();



//        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
//        UserService userService1 = (UserService) ctx.getBean("userService3");
//        UserService userService2 = (UserService) ctx.getBean("userService3");
//        UserService userService3 = (UserService) ctx.getBean("userService3");
//
//        ctx.close();



        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        UserService userService1 = (UserService) ctx.getBean("userService5");
        userService1.save();
    }
}
