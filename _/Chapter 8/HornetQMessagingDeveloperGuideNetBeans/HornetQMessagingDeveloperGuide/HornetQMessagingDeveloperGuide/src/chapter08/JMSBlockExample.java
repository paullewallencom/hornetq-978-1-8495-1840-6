package chapter08;

import java.util.Date;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueRequestor;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.hornetq.api.core.Message;
import org.hornetq.api.jms.HornetQJMSClient;
import org.hornetq.api.jms.management.JMSManagementHelper;

public class JMSBlockExample {

	/**
	 * @param args
	 * @throws NamingException 
	 * @throws JMSException 
	 */
	public static void main(String[] args) throws NamingException, JMSException {
		// TODO Auto-generated method stub
		QueueConnection connection = null;
		InitialContext initialContext = null;
			
		try
		{
			java.util.Properties p = new java.util.Properties();
			
			p.put(javax.naming.Context.INITIAL_CONTEXT_FACTORY,
			"org.jnp.interfaces.NamingContextFactory");

			p.put(javax.naming.Context.URL_PKG_PREFIXES,
			"org.jboss.naming:org.jnp.interfaces");
				
			p.put(javax.naming.Context.PROVIDER_URL, "jnp://localhost:1099");

			initialContext = new javax.naming.InitialContext(p);
			 
			Queue queue = (Queue)initialContext.lookup("/queue/ECGQueue");
			
			QueueConnectionFactory cf = (QueueConnectionFactory)initialContext.lookup("/ConnectionFactory");
			
			connection = cf.createQueueConnection();
			
			QueueSession session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
						
			MessageProducer producer = session.createProducer(queue);
			MessageConsumer messageConsumer = session.createConsumer(queue);
			connection.start();
			BytesMessage message = session.createBytesMessage();
			
			
			int tot = 0;
			int cons = 0;
			for (int i = 0; i<1000; i++) {
				try  {
					
			        //message.writeBytes(new byte[1024]);
			        tot +=  1024;
					//producer.send(message);		 
					System.out.println("Sent message: " + i + " " + new Date());
					System.out.println("Total bytes sent " + tot);
					System.out.println("consuming message " + i);
					
				}catch(Exception e){
					//System.out.println("treadshot reached try to consume messages");
					
					//message = (BytesMessage)messageConsumer.receive(3000);
					//cons++;
					//System.out.println("consuming " + cons + " messages ");
					
					
				}
				
				
			}
			
			
			
		}catch(Exception ex){
			ex.printStackTrace();
		}finally {
			
			if (initialContext != null)
			{
				initialContext.close();
			}
			if (connection != null)
			{
				connection.close();
			}
			
		}

	}

}
