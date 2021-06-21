import com.alibaba.druid.pool.DruidDataSource;
import com.itheima.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class UserApp {
    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
//        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext-user.xml","applicationContext-book.xml");
        UserService userService = (UserService) ctx.getBean("userService");
        userService.save();

        DruidDataSource dataSource = (DruidDataSource) ctx.getBean("dataSource");
        System.out.println(dataSource);
    }
}
