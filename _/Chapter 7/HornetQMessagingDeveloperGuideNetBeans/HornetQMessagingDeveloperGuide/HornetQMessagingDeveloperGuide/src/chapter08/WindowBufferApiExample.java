package chapter08;

import java.util.HashMap;

import org.hornetq.api.core.HornetQException;
import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.api.core.client.ClientConsumer;
import org.hornetq.api.core.client.ClientMessage;
import org.hornetq.api.core.client.ClientProducer;
import org.hornetq.api.core.client.ClientSession;
import org.hornetq.api.core.client.ClientSessionFactory;
import org.hornetq.api.core.client.HornetQClient;
import org.hornetq.api.core.client.ServerLocator;
import org.hornetq.core.remoting.impl.netty.NettyConnectorFactory;

public class WindowBufferApiExample {

	/**
	 * @param args
	 * @throws HornetQException 
	 */
	public static void main(String[] args) throws HornetQException {
		// TODO Auto-generated method stub
		
		HashMap map = new HashMap();
		map.put("host", "localhost");
		map.put("port", 5445);
		 
	    TransportConfiguration configuration = new TransportConfiguration(NettyConnectorFactory.class.getName(), map);
	    ServerLocator serverLocator = null;
	    ClientSessionFactory factory = null;
	    ClientSession session = null;		
	    
	    try {
	    	
	    	  	serverLocator = HornetQClient.createServerLocatorWithoutHA(configuration);
	    	  
	            factory = serverLocator.createSessionFactory();
	            ((ServerLocator) factory).setConsumerWindowSize(0);
	            session = factory.createSession(false, false, false);
	            
	            session.createQueue("ECGQueue", "ECGQueue", true);
	            ClientProducer producer = session.createProducer("ECGQueue");
	            System.out.println(producer.isClosed());
	            ClientMessage message = session.createMessage(true);
	            message.getBodyBuffer().writeString("Hello");
	            message.setExpiration(60000);
	            System.out.println("message = " + message.getBodyBuffer().readString());
	            producer.send(message);
	           
	            session.start();
	            ClientConsumer consumer = session.createConsumer("ECGQueue");
	           
	           
	 
	            ClientMessage msgReceived = consumer.receive(100);
	            System.out.println("message received "+ msgReceived.getBodyBuffer().readString());
	            session.close();    
	            
	    }catch(Exception ex){
	    	
	    	ex.printStackTrace();
	    	
	    } finally {
	    	if (session != null)
	    		session.close();
	    	if (factory != null)
	    		factory.close();

	    }
		

	}

}
