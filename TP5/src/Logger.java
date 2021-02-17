import org.apache.camel.Exchange;
import org.apache.camel.component.jms.JmsMessage;

public class Logger {

	public  void log(Exchange e) {
		JmsMessage msg = (JmsMessage) e.getIn();
        String item = msg.getBody(String.class);
        System.out.println(item);
	}
}
