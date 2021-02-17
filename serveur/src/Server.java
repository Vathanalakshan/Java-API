import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.lifecycle.SingletonResourceProvider;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;

import fr.polytech.serveur.ConsumerWS2;
import fr.polytech.serveur.ProducerWS1;

public class Server {

	public static void main(String[] args) {
		
        JAXRSServerFactoryBean sf = new JAXRSServerFactoryBean();
        sf.setProvider(new JacksonJaxbJsonProvider());
        
        sf.setResourceClasses(ProducerWS1.class, ConsumerWS2.class);
        
        sf.setResourceProvider(
        		ProducerWS1.class, 
        		new SingletonResourceProvider(new ProducerWS1())
        );
        sf.setResourceProvider(
        		ConsumerWS2.class, 
        		new SingletonResourceProvider(new ConsumerWS2())
        );
        
        sf.setAddress("http://localhost:9000");
        sf.create();

	}

}
