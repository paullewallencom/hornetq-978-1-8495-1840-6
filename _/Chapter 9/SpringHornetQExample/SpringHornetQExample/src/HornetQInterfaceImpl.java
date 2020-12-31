import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;


public class HornetQInterfaceImpl implements IHornetQInterface {

	@Override
	public void ProduceConsumeMessage()  {
	
	try {	
		Connection connection = null;
		InitialContext initialContext = null;
		try
		{

			java.util.Properties p = new java.util.Properties();
			
			p.put(javax.naming.Context.INITIAL_CONTEXT_FACTORY,"org.jnp.interfaces.NamingContextFactory");

			p.put(javax.naming.Context.URL_PKG_PREFIXES,"org.jboss.naming:org.jnp.interfaces");
				
			p.put(javax.naming.Context.PROVIDER_URL, "jnp://localhost:1099");

			initialContext = new javax.naming.InitialContext(p);

			Queue queue = (Queue)initialContext.lookup("/queue/ECGQueue");

			ConnectionFactory cf = (ConnectionFactory)initialContext.lookup("/ConnectionFactory");

			connection = cf.createConnection();

			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			MessageProducer producer = session.createProducer(queue);

			TextMessage message = session.createTextMessage("This is a text message");

			System.out.println("Sent message: " + message.getText());

			producer.send(message);

			MessageConsumer messageConsumer = session.createConsumer(queue);

			connection.start();

			TextMessage messageReceived = (TextMessage)messageConsumer.receive(5000);

			System.out.println("Received message: " + messageReceived.getText());
		}
		finally
		{
			
			if (initialContext != null)
			{
				initialContext.close();
			}
			if (connection != null)
			{
				connection.close();
			}
		}
	}catch (Exception ex){
		ex.printStackTrace();
		
	}	
		
		
	}

}
