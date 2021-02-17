package fr.polytech.serveur;


import java.io.File;
import java.io.IOException;

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
import java.sql.Timestamp;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.file.GenericFileMessage;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.component.jms.JmsMessage;
import org.apache.camel.impl.DefaultCamelContext;

import com.fasterxml.jackson.databind.ObjectMapper;

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
							ObjectMapper mapper = new ObjectMapper();
                            String item = msg.getBody(String.class);
                            try {
                            	System.out.println(item);
                                MyData data = mapper.readValue(item, MyData.class);
                                data.setReception(new Timestamp(System.currentTimeMillis()));
                                data.setPrix(FournisseurService.getPrix(data.getNomfruit())); 
                                Thread.sleep(1000);
                                data.setFournisseur(new Timestamp(System.currentTimeMillis()));
                                data.setCorrID(msg.getHeader("JMSCorrelationID").toString());
                                e.getIn().setBody(mapper.writeValueAsString(data));
                                e.getIn().setHeader("id",data.getId());
                                System.out.println("done");
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
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

