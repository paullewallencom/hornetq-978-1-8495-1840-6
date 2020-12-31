import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class HornetQMessageTester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
        ApplicationContext context = new ClassPathXmlApplicationContext("Bean.xml");
        HornetQInterfaceImpl test = (HornetQInterfaceImpl) context.getBean("HornetQMessage");
        test.ProduceConsumeMessage();
	}

}
