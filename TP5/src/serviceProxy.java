import org.apache.camel.Exchange;
import org.apache.camel.component.jms.JmsMessage;

public class serviceProxy {

	public void call(Exchange e) {
		JmsMessage msg = (JmsMessage) e.getIn();
        String item = msg.getBody(String.class);
        String prixMsg = " Corr ID " +  msg.getHeader("JMSCorrelationID")+ " "+ item + " : "+ getPrix(item);
        e.getIn().setBody(prixMsg);
	}
	
	public  static final float getPrix(String idProduit) {
		if (idProduit.equals("Pomme"))
			return 1.0f;
		else if (idProduit.equals("Orange"))
			return 0.9f;
		else if (idProduit.equals("Abricot"))
			return 0.9f;
		else if (idProduit.equals("Fraise"))
			return 2.0f;
		else
			return 999999.9f;
	}
}
