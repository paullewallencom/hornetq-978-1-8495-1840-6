
package chapter06;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageConsumer;

public class TwoNodesExample {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Connection connectiona = null;
		Connection connectionb = null;

		InitialContext initialContexta = null;
		InitialContext initialContextb = null;

		String ECG_TEXT = "1;31/01/2012 15:45:01.100;1021;1022;1023";

		Properties p = new Properties();
		p.put(Context.INITIAL_CONTEXT_FACTORY,"org.jnp.interfaces.NamingContextFactory");
		p.put(Context.URL_PKG_PREFIXES,"org.jboss.naming:org.jnp.interfaces");
		p.put(Context.PROVIDER_URL, "jnp://localhost:1099");
		initialContexta  = new javax.naming.InitialContext(p);

		ConnectionFactory cfa = (ConnectionFactory)initialContexta.lookup("/ConnectionFactory");

		p = new Properties();
		p.put(Context.INITIAL_CONTEXT_FACTORY,"org.jnp.interfaces.NamingContextFactory");
		p.put(Context.URL_PKG_PREFIXES,"org.jboss.naming:org.jnp.interfaces");
		p.put(Context.PROVIDER_URL, "jnp://localhost:2099");
		initialContextb  = new javax.naming.InitialContext(p);

		ConnectionFactory cfb = (ConnectionFactory)initialContexta.lookup("/ConnectionFactory");

		
		Topic topic = (Topic)initialContexta.lookup("/topic/ECGTopic");
		connectiona = cfa.createConnection();
		connectionb = cfb.createConnection();

		connectiona.start();
		connectionb.start();

		Session sessiona = connectiona.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Session sessionb = connectionb.createSession(false, Session.AUTO_ACKNOWLEDGE);

		MessageConsumer messageConsumera = sessiona.createConsumer(queue);

		MessageProducer producer = sessiona.createProducer(topic);

		TextMessage message = sessiona.createTextMessage(ECG_TEXT);

		producer.send(message);

		
		MessageConsumer messageConsumerb = sessionb.createConsumer(topic);
		

		final String groupAddress = "231.7.7.7";
		
		final int groupPort = 9876;
		
		ConnectionFactory jmsConnectionFactory = 
		HornetQJMSClient.createConnectionFactory(groupAddress, groupPort);
		
		Connection jmsConnection1 = jmsConnectionFactory.createConnection();
		
		Connection jmsConnection2 = jmsConnectionFactory.createConnection();
		
	}

}
