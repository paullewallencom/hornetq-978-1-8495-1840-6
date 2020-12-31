/*
*   Copyright 2012 Piero Giacomelli
*
*   Licensed under the Apache License, Version 2.0 (the "License");
*   you may not use this file except in compliance with the License.
*   You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
*   Unless required by applicable law or agreed to in writing, software
*   distributed under the License is distributed on an "AS IS" BASIS,
*   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*   See the License for the specific language governing permissions and
*   limitations under the License.
*   
*   author: Piero Giacomelli
*   email:	pgiacome@gmail.com
*/

package chapter08;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class ConsumerMaxRateExample {

	/**
	 * @param args
	 * @throws JMSException 
	 * @throws NamingException 
	 */
	public static void main(String[] args) throws JMSException, NamingException {
		// TODO Auto-generated method stub
		
	      Connection connection = null;
	      InitialContext initialContext = null;
	      try
	      {
	    	  
	    	  String ECG_TEXT = "1;02/20/2012 14:01:59.010;1020,1021,1022";	    	  
	    	  
	  		java.util.Properties p = new java.util.Properties();
			
			p.put(javax.naming.Context.INITIAL_CONTEXT_FACTORY,
			"org.jnp.interfaces.NamingContextFactory");

			p.put(javax.naming.Context.URL_PKG_PREFIXES,
			"org.jboss.naming:org.jnp.interfaces");
				
			p.put(javax.naming.Context.PROVIDER_URL, "jnp://localhost:1099");
				 
			
	         initialContext = new javax.naming.InitialContext(p);

	         // Step 2. Perfom a lookup on the queue
	         Queue queue = (Queue)initialContext.lookup("/queue/ECGQueue");

	         // Step 3. Perform a lookup on the Connection Factory
	         ConnectionFactory cf = (ConnectionFactory)initialContext.lookup("/ConnectionFactory");

	         // Step 4. Create a JMS Connection
	         connection = cf.createConnection();

	         // Step 5. Create a JMS Session
	         Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

	         // Step 6. Create a JMS Message Producer
	         MessageProducer producer = session.createProducer(queue);

	         // Step 7. Create a JMS Message Consumer

	         MessageConsumer consumer = session.createConsumer(queue);

	         // Step 8. Start the connection

	         connection.start();

	         // Step 9. Send a bunch of messages

	         final int numMessages = 150;

	         for (int i = 0; i < numMessages; i++)
	         {
	            TextMessage message = session.createTextMessage(ECG_TEXT);

	            producer.send(message);
	         }

	         System.out.println("Sent messages");

	         System.out.println("Will now try and consume as many as we can in 10 seconds ...");

	         // Step 10. Consume as many messages as we can in 10 seconds

	         final long duration = 10000;

	         int i = 0;

	         long start = System.currentTimeMillis();

	         while (System.currentTimeMillis() - start <= duration)
	         {
	            TextMessage message = (TextMessage)consumer.receive(2000);

	            i++;
	         }

	         long end = System.currentTimeMillis();

	         double rate = 1000 * (double)i / (end - start);

	         System.out.println("We consumed " + i + " messages in " + (end - start) + " milliseconds");

	         System.out.println("Actual consume rate was " + rate + " messages per second");

	         
	      }
	      finally
	      {
	         // Step 9. Be sure to close our resources!
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
