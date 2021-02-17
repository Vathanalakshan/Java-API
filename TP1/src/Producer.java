import java.util.Scanner;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;

public class Producer {

	private ConnectionFactory connectionFactory;
	private Destination destination;
	private MessageProducer prod;
	private TextMessage mess;

	public static void main(String[] args) {
		try {
			Producer producer = new Producer();
			producer.connect();
			producer.sendMessages();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	private void sendMessages() throws Exception {
		// Créer une connexion au système de messagerie
		// Emet des messages au fur et à mesure que l’utilisateur les saisit
		Connection connection = connectionFactory.createConnection();
		connection.start();
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

		Scanner in = new Scanner(System.in);
		while (true) {
			System.out.println("Taper le message à envoyer (QUIT pour arreter)");
			String s = in.nextLine();
			if (s.equals("QUIT")) {
				break;
			} else {
				this.prod = session.createProducer(destination);
				this.mess = session.createTextMessage(s);
				prod.send(mess);
			}
		}
		in.close();
		connection.close();
	}

	private void connect() throws Exception {
		// Initialise les attributs connectionFactory et destination.
		Context jndiContext = new InitialContext();
		this.connectionFactory = (ConnectionFactory) jndiContext.lookup("connectionFactory");
		this.destination = (Destination) jndiContext.lookup("MyQueue");
	}

}

/*
TP1.
1.Les Messages ne sont plus enqueued
2.Une systeme de acquittement permettant au messages de rester dans le queue meme s'ils sont pas 
*/