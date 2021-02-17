import java.util.Scanner;
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

	private ConnectionFactory connectionFactory;// Connection
	private Destination destinationRES;// QueueRES
	private Destination destinationREQ;// QueueREQ
	private Connection connection;
	private MessageProducer prodREQ;// Producer des requetes
	private MessageConsumer consRES;// Consumer des responses
	private Session session;
	private TextMessage mess;

	public static void main(String[] args) {
		try {
			Client client = new Client();
			client.connect();
			client.consRES.setMessageListener(client);
			client.sendMessages();
			client.connection.close();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	private void sendMessages() throws Exception {

		Scanner in = new Scanner(System.in);
		while (true) {
			System.out.println("Taper le message à envoyer (QUIT pour arreter)");
			String s = in.nextLine();
			if (s.equals("QUIT")) {
				break;
			} else {
				this.mess = this.session.createTextMessage(s);
				prodREQ.send(mess);
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

		this.prodREQ = session.createProducer(destinationREQ);
		this.consRES = session.createConsumer(destinationRES);

		// Créer une connexion au système de messagerie
		// Emet des messages au fur et à mesure que l’utilisateur les saisit
		Connection connection = connectionFactory.createConnection();
		connection.start();
		this.session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

	}

	@Override
	public void onMessage(Message message) {
		// TODO Auto-generated method stub
		try {
			if (message instanceof TextMessage) {
				System.out.println("Message Received From Service");
				System.out.println(((TextMessage) message).getText());
			}
		} catch (JMSException jmse) {
			jmse.printStackTrace();
		}

	}

}
