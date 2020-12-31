package chapter09;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.JMSSecurityException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.NamingException;

public class SecurityExample {

	/**
	 * @param args
	 * @throws NamingException 
	 * @throws JMSException 
	 */
	public static void main(String[] args) throws NamingException, JMSException {

		
		java.util.Properties p = new java.util.Properties();

		p.put(javax.naming.Context.INITIAL_CONTEXT_FACTORY,"org.jnp.interfaces.NamingContextFactory");

		p.put(javax.naming.Context.URL_PKG_PREFIXES,"org.jboss.naming:org.jnp.interfaces");

		p.put(javax.naming.Context.PROVIDER_URL, "jnp://localhost:1099");

		javax.naming.InitialContext initialContext = new javax.naming.InitialContext(p);

		Queue queue = (Queue)initialContext.lookup("/queue/ECGQueue");
		
		ConnectionFactory cf = (ConnectionFactory) initialContext.lookup("/ConnectionFactory");
		
		try
		{
			cf.createConnection();
		}
		catch (JMSSecurityException e)
		{
			System.out.println("Default user cannot get a connection. Details: " + e.getMessage());
		}

		Connection connection = null;
		try
		{
			connection = cf.createConnection("ecgproducer", "ecgproducer01");
			System.out.println("Successfully created session for user : ecgproducer");
		}
		catch (JMSSecurityException e)
		{
			System.out.println("Default user cannot get a connection. Details: " + e.getMessage());
		}

		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		MessageProducer producer = session.createProducer(queue);
		MessageConsumer consumer = session.createConsumer(queue);

		TextMessage msg = session.createTextMessage("ecg_message");	      
		producer.send(msg);
		System.out.println("message sent on "  + (new java.util.Date()) );

		TextMessage receivedMsg = (TextMessage)consumer.receive(2000);

		Connection producerconnection = cf.createConnection("ecgconsumer", "ecgconsumer01");
		producerconnection.start();

		Session producersession = producerconnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		producer = producersession.createProducer(queue);
		

		System.out.println("received message " + receivedMsg.getText());
		

	}

}
