package chapter06;

import java.util.Date;
import java.util.Properties;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class ClusteredQueueExample {

	/**
	 * @param args
	 * @throws JMSException 
	 * @throws NamingException 
	 */
	public static void main(String[] args) throws JMSException, NamingException {
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

		Session sessiona = connectiona.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Session sessionb = connectionb.createSession(false, Session.AUTO_ACKNOWLEDGE);

		Queue queue = (Queue)initialContexta .lookup("/queue/ECGQueue");

		MessageConsumer consumera = sessiona.createConsumer(queue);
		MessageConsumer consumerb = sessionb.createConsumer(queue);

		MessageProducer producer = sessiona.createProducer(queue);
		
		final int numMessages = 10;

		for (int i = 0; i < numMessages; i++)
		{
			TextMessage message = sessiona.createTextMessage(ECG_TEXT);

			producer.send(message);

			System.out.println("Sent message: " + message.getText() + new Date());
		}

		for (int i = 0; i < numMessages; i++)
		{
			TextMessage messagea = (TextMessage)consumera.receive(5000);

			System.out.println("Got message: " + messagea.getText() + " from node A");

			TextMessage messageb = (TextMessage)consumerb.receive(5000);

			System.out.println("Got message: " + messageb.getText() + " from node B");
		}

		if (connectiona != null)
		{
			connectiona.close();
		}

		if (connectionb != null)
		{
			connectionb.close();
		}

		if (initialContexta  != null)
		{
			initialContexta.close();
		}

		if (initialContextb != null)
		{
			initialContextb.close();
		}

		
		
	}

}
