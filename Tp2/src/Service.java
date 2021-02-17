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

public class Service {


	private ConnectionFactory connectionFactory;//Connection
	private Destination destinationRES;//QueueRES
	private Destination destinationREQ;//QueueREQ
	private Connection connection;
	private MessageConsumer consREQ;//Consumer des requetes
	private MessageProducer prodRES;//Producer des response
	private Session session;
	private TextMessage mess;

	public static void main(String[] args) {
		try {

			Service service = new Service();
			service.connect();
			service.GetAndReverse();
			service.connection.close();

		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	private void GetAndReverse() throws Exception {
		// Creer une connexion au syst�me de messagerie
		// Et affiche les messages au fur et � mesure de leur arriv�e dans la queue
		while (true) {
			Message m = consREQ.receive(1);
			if (m instanceof TextMessage) {
				System.out.println("Message Received from client");
				System.out.println(((TextMessage) m).getText() + " est le message a reverse");
				this.mess = session.createTextMessage(reverse(((TextMessage) m).getText()));
				prodRES.send(mess);
			}
		}
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

		this.consREQ = session.createConsumer(destinationREQ);
		this.prodRES = session.createProducer(destinationRES);

	}

	public String reverse(String input) {

		if (input == null) {
			return input;
		}

		String output = "";

		for (int i = input.length() - 1; i >= 0; i--) {
			output = output + input.charAt(i);
		}

		return output;
	}

}
