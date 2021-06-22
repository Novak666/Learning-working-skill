import com.itheima.config.SpringConfig;
import com.itheima.domain.Account;
import com.itheima.service.AccountService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
    public static void main(String[] args) {
//        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
//        AccountService accountService = (AccountService) ctx.getBean("accountService");
//        Account ac = accountService.findById(2);
//        System.out.println(ac);

        ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
        AccountService accountService = (AccountService) ctx.getBean("accountService");
        Account ac = accountService.findById(2);
        System.out.println(ac);
    }
}
