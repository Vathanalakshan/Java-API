package fr.polytech.serveur;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.jms.Message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Path("/cons")
public class ConsumerWS2  {
	private ConnectionFactory connectionFactory;
	private Destination destinationRES;
	private Connection connection;
	private MessageConsumer consRES;
	private Session session;

	
	@POST
	@Path("/fruit")
	@Produces("application/json")
	@Consumes("application/json")
	public String temp(MyData res) {
		try {
			connect(String.valueOf(res.getId()));
			System.out.println("connection reussi");
	        TextMessage textMsg = (TextMessage) consRES.receive(1);
	        if (textMsg != null) { 
	        	System.out.println(textMsg);
	        	System.out.println("Received: " + textMsg.getText());
	        	return textMsg.getText();
	        }
	        else
	        {
	    		return "error";
	        }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Marche po");
		return "error";
		}
	
	
	private void connect(String s) throws Exception {
		// Initialise les attributs connectionFactory et destination.
		System.out.println("debut de connection");
		Context jndiContext = new InitialContext();
		this.connectionFactory = (ConnectionFactory) jndiContext.lookup("connectionFactory");
		this.destinationRES = (Destination) jndiContext.lookup("res");
		this.connection = connectionFactory.createConnection();
		connection.start();
		this.session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		String k="id = "+s;
		System.out.println(k);
		this.consRES = session.createConsumer(destinationRES,k );
	}
	
	@GET
	@Produces("text/plain")
	@Path("/hello")
	public String methode2() {
		return "Hello Cons";
	}
	
		
}
