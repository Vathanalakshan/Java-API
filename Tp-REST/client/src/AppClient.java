import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.UUID;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;

public class AppClient {
	private static int id = (int)(Math.random() * 50 + 1) ;

	public static int createID() {
		return id++;
	}

	public static void main(String[] args) {

		System.setProperty(ClientBuilder.JAXRS_DEFAULT_CLIENT_BUILDER_PROPERTY,
				"org.apache.cxf.jaxrs.client.spec.ClientBuilderImpl");

		String URL = "http://localhost:9000";

		Client client = ClientBuilder.newClient();

		client.register(JacksonJaxbJsonProvider.class);
		{
			WebTarget r = client.target(URL + "/prod/hello");
			String s = r.request(MediaType.TEXT_PLAIN).get(String.class);
			System.out.println(s);
		}
		{
			WebTarget r = client.target(URL + "/cons/hello");
			String s = r.request(MediaType.TEXT_PLAIN).get(String.class);
			System.out.println(s);
		}

		while (true) {
			System.out.println("1(Demande) ou 2(Reponse)");
			Scanner in = new Scanner(System.in);
			String input = in.nextLine();
			if (input.equals("1")) {
				System.out.println("Taper le nom de fruit à envoyer (QUIT pour arreter)");
				String fr = in.nextLine();
				if (fr.equals("QUIT")) {
					break;
				} else {
					WebTarget r = client.target(URL + "/prod/fruit");
					HashMap<String, Object> map = new HashMap<String, Object>();

					int id = createID();
					map.put("id", id);
					map.put("nomfruit", fr);
					String s = r.request(MediaType.TEXT_PLAIN).post(Entity.json(map), String.class);
					System.out.println("L'id de la requete est " + id);
					System.out.println(s);
				}
			} else if (input.equals("2")) {
				System.out.println("Taper le id de response (QUIT pour arreter)");
				String resid = in.nextLine();
				if (resid.equals("QUIT")) {
					break;
				} else {
					WebTarget r = client.target(URL + "/cons/fruit");
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("id", resid);
					Response s = r.request().post(Entity.json(map));
					System.out.println(s.readEntity(String.class));
				}
			}
		}
	}
}
