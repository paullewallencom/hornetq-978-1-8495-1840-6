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

package chapter04;

import java.util.Date;

import javax.jms.JMSException;
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

public class JMSManagementExample {

	/**
	 * @param args
	 * @throws NamingException 
	 * @throws JMSException 
	 */
	public static void main(String[] args) throws NamingException, JMSException {
		
		QueueConnection connection = null;
		InitialContext initialContext = null;
			
		try
		{
			java.util.Properties p = new java.util.Properties();
			
			p.put(javax.naming.Context.INITIAL_CONTEXT_FACTORY,"org.jnp.interfaces.NamingContextFactory");

			p.put(javax.naming.Context.URL_PKG_PREFIXES,"org.jboss.naming:org.jnp.interfaces");
				
			p.put(javax.naming.Context.PROVIDER_URL, "jnp://localhost:1099");

			initialContext = new javax.naming.InitialContext(p);
			
			 
			Queue queue = (Queue)initialContext.lookup("/queue/exampleQueue");
			
			QueueConnectionFactory cf = (QueueConnectionFactory)initialContext.lookup("/ConnectionFactory");
			
			connection = cf.createQueueConnection();
			connection.start();
			
			QueueSession session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
			
			MessageProducer producer = session.createProducer(queue);
			
			TextMessage message = session.createTextMessage("This is a text message");
			 
			for (int i = 0; i<1000; i++) {
				message = session.createTextMessage("This is a text message");		
				producer.send(message);		 
				System.out.println("Sent message: " + message.getText() + " " + new Date());
			}
			
			Queue managementQueue = HornetQJMSClient.createQueue("hornetq.management");
			 
			QueueRequestor requestor = new QueueRequestor(session, managementQueue);
			
			Message m = (Message) session.createMessage();
			
			JMSManagementHelper.putAttribute((javax.jms.Message) m, "jms.queue.exampleQueue", "messageCount");
			
			Message reply = (Message) requestor.request((javax.jms.Message) m);
			
			int messageCount = (Integer)JMSManagementHelper.getResult((javax.jms.Message) reply);
			System.out.println(queue.getQueueName() + " contains " + messageCount + " messages");
			
			System.exit(-1);
			
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
