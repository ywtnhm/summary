import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by Administrator on 2016/10/27.
 */
public class Provider {
    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationProvider.xml");
        context.start();
        System.in.read(); // 按任意键退出
    }
}
