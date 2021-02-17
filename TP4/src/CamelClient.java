import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.component.jms.JmsConsumer;
import org.apache.camel.component.jms.JmsEndpoint;
import org.apache.camel.impl.DefaultCamelContext;

public class CamelClient  {
	
	private ConnectionFactory connectionFactory;// Connection
	private ProducerTemplate pt;
	public static void main(String[] args) {
		try {
			CamelClient  camclient= new CamelClient();	
			Context jndiContext = new InitialContext();
			camclient.connectionFactory = (ConnectionFactory) jndiContext.lookup("connectionFactory");

			CamelContext context = new DefaultCamelContext();
			context.addComponent("jms-test",
					JmsComponent.jmsComponentAutoAcknowledge(camclient.connectionFactory));
			JmsEndpoint responseEndPoint = (JmsEndpoint)
					context.getEndpoint("jms-test:foo.response");
			JmsConsumer consumer = responseEndPoint.createConsumer(new Processor() {
				public void process(Exchange e) throws Exception {
					System.out.println("Réponse reçue : " + e.getIn().getBody());
					}});
			camclient.pt = context.createProducerTemplate();
			context.start();
			consumer.start();
			camclient.sendProduct();
			Thread.sleep(50000000);
			consumer.stop();
			context.stop();


		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	private void sendProduct() throws Exception {
		Scanner in = new Scanner(System.in);
		while (true) {
			Thread.sleep(50);
			System.out.println("Taper le nom de fruit à envoyer (QUIT pour arreter)");
			String s = in.nextLine();
			if (s.equals("QUIT")) {
				break;
			} else {
				Map<String, Object> headers = new HashMap<>();
				String corrID= UUID.randomUUID().toString();
				headers.put("JMSCorrelationID", corrID);
				this.pt.sendBodyAndHeader("jms-test:foo.request", s, "JMSCorrelationID", corrID);
				System.out.println("JMS requete envoyé et CorrID :" + corrID);  
			}
		}
		in.close();
	}	
	
	}
	
