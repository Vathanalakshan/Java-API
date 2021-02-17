package fr.polytech.serveur;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageConsumer;
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

import com.fasterxml.jackson.databind.ObjectMapper;

@Path("/prod")
public class ProducerWS1 {
	private ConnectionFactory connectionFactory;
	private Destination destinationREQ;
	private Connection connection;
	private MessageProducer prodREQ;
	private Session session;
	private TextMessage mess;
	@POST
	@Path("/fruit")
	@Consumes("application/json")
	@Produces("text/plain")
	public String temp(MyData req) {
		String s= " ";
		try {
			connect();
			ObjectMapper mapper = new ObjectMapper();
			this.mess = this.session.createTextMessage(mapper.writeValueAsString(req));
			mess.setJMSCorrelationID(UUID.randomUUID().toString());
			prodREQ.send(mess);
			System.out.println("JMS requete envoyé + corrid: " + mess.getJMSCorrelationID());
			s= mess.getJMSCorrelationID();
			connection.close();
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return "Requete envoyé avc " + s;
	}
	
	private void connect() throws Exception {
		// Initialise les attributs connectionFactory et destination.
		Context jndiContext = new InitialContext();
		this.connectionFactory = (ConnectionFactory) jndiContext.lookup("connectionFactory");
		this.destinationREQ = (Destination) jndiContext.lookup("req");// QUEUE DES REQUETES

		this.connection = connectionFactory.createConnection();
		connection.start();
		this.session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

		this.prodREQ = session.createProducer(destinationREQ);

	}
		
	@GET
	@Produces("text/plain")
	@Path("/hello")
	public String methode2() {
		return "Hello Prod";
	}
}
