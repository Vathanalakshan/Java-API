import java.util.Scanner;
import java.util.UUID;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;

public class Client implements MessageListener {

	private ConnectionFactory connectionFactory;
	private Destination destinationRES;
	private Destination destinationREQ;
	private Connection connection;
	private MessageConsumer consRES;
	private MessageProducer prodREQ;
	private Session session;
	private TextMessage mess;

	public static void main(String[] args) {
		try {

			Client client = new Client();
			client.connect();
			client.consRES.setMessageListener(client);
			client.sendProduct();
			client.connection.close();

		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	private void sendProduct() throws Exception {
		// Creer une connexion au syst�me de messagerie
		// Et affiche les messages au fur et � mesure de leur arriv�e dans la queue
		Scanner in = new Scanner(System.in);
		while (true) {
			Thread.sleep(50);
			System.out.println("Taper le nom de fruit à envoyer (QUIT pour arreter)");
			String s = in.nextLine();
			if (s.equals("QUIT")) {
				break;
			} else {
				this.mess = this.session.createTextMessage(s);
				mess.setJMSCorrelationID(UUID.randomUUID().toString());
				prodREQ.send(mess);
				System.out.println("JMS requete envoyé:" + mess.getJMSCorrelationID());  
			}
		}
		in.close();
	}

	private void connect() throws Exception {
		// Initialise les attributs connectionFactory et destination.
		Context jndiContext = new InitialContext();
		this.connectionFactory = (ConnectionFactory) jndiContext.lookup("connectionFactory");
		this.destinationREQ = (Destination) jndiContext.lookup("req");// QUEUE DES REQUETES
		this.destinationRES = (Destination) jndiContext.lookup("res");// QUEUE DES RESPONSE

		this.connection = connectionFactory.createConnection();
		connection.start();
		this.session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

		this.consRES = session.createConsumer(destinationRES);
		this.prodREQ = session.createProducer(destinationREQ);

	}

	@Override
	public void onMessage(Message message) {
		// TODO Auto-generated method stub
		try {
			if (message instanceof TextMessage) {
				System.out.println("Message Received From Fournisseur service " + ((TextMessage) message).getText());
			}
		} catch (JMSException jmse) {
			jmse.printStackTrace();
		}

	}

}
