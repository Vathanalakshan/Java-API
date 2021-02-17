import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;

public class Consumer {

	private ConnectionFactory connectionFactory;
	private Destination destination;
	private MessageConsumer cons;
	private Connection connection;

	public static void main(String[] args) {
		try {
			Consumer consumer = new Consumer();
			consumer.connect();
			consumer.waitForMessage();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	private void waitForMessage() throws Exception {
		// Creer une connexion au syst�me de messagerie
		// Et affiche les messages au fur et � mesure de leur arriv�e dans la queue
		while (true) {
			Message m = cons.receive(1);
			if (m instanceof TextMessage) {
				System.out.println("Message Received");
				System.out.println(((TextMessage) m).getText());
			} else {
				break;
			}
		}
		this.connection.close();
	}

	private void connect() throws Exception {
		// Initialise les attributs connectionFactory et destination.
		Context jndiContext = new InitialContext();
		this.connectionFactory = (ConnectionFactory) jndiContext.lookup("connectionFactory");
		this.destination = (Destination) jndiContext.lookup("MyQueue");

		this.connection = connectionFactory.createConnection();
		connection.start();
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

		this.cons = session.createConsumer(destination);
	}
}
