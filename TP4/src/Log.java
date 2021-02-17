import java.io.File;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.file.GenericFileMessage;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.component.jms.JmsMessage;
import org.apache.camel.impl.DefaultCamelContext;

public class Log {
	
	private ConnectionFactory connectionFactory;// Connection
	private Connection connection;
	public static void main(String[] args) {
		try {
			
			Log logger = new Log();
			logger.connect();
			
			CamelContext context = new DefaultCamelContext();
			context.addComponent("jms-test",
					JmsComponent.jmsComponentAutoAcknowledge(logger.connectionFactory));

			context.addRoutes(new RouteBuilder() {
				public void configure() throws Exception {
					from("jms-test:foo.request").process(new Processor() {
						public void process(Exchange e) throws Exception {
							JmsMessage msg = (JmsMessage) e.getIn();
                            String item = msg.getBody(String.class);
                            System.out.println(item);
                            System.out.println(FournisseurService.getPrix(item));
                            String prixMsg = " Corr ID " +  msg.getHeader("JMSCorrelationID")+ " "+ item + " : "+ FournisseurService.getPrix(item);
                            e.getIn().setBody(prixMsg);
						}
					}).to("jms-test:foo.response");
				}
			});
			
			context.start();
			Thread.sleep(50000000);
			context.stop();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	private void connect() throws Exception {
		Context jndiContext = new InitialContext();
		this.connectionFactory = (ConnectionFactory) jndiContext.lookup("connectionFactory");
		this.connection = connectionFactory.createConnection();
		connection.start();
	}
}

