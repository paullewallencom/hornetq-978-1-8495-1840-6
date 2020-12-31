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

package chapter06;

import java.util.Properties;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.hornetq.api.jms.HornetQJMSClient;

public class TwoNodesExample {

	/**
	 * @param args
	 * @throws NamingException 
	 * @throws JMSException 
	 */
	public static void main(String[] args) throws NamingException, JMSException {
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

		MessageConsumer messageConsumera = sessiona.createConsumer(topic);

		MessageProducer producer = sessiona.createProducer(topic);

		TextMessage message = sessiona.createTextMessage(ECG_TEXT);

		producer.send(message);

		
		MessageConsumer messageConsumerb = sessionb.createConsumer(topic);
		

		
	}

}
