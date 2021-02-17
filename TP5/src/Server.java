import org.apache.camel.CamelContext;
import org.apache.xbean.spring.context.ClassPathXmlApplicationContext;
import org.springframework.context.ApplicationContext;

public class Server {

	
	public static void main(String[] args) {
	ApplicationContext appContext = new ClassPathXmlApplicationContext("camel-server.xml");
	CamelContext context = (CamelContext) appContext.getBean("camel");
	try {
		context.start();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
}
