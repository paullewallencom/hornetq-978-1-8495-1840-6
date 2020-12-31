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




package chapter01;

import java.net.UnknownHostException;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import javax.naming.NamingException;

import com.mongodb.MongoException;

public class ECGMessageProducerConsumerExample {

	/**
	 * @param args
	 */
	
	javax.naming.Context ic = null;
	javax.jms.ConnectionFactory cf = null;
	javax.jms.Connection connection =  null;
	javax.jms.Queue queue = null;
	javax.jms.Session session = null;
	
	com.mongodb.Mongo m;
	com.mongodb.DB db;
	
	String destinationName = "queue/DLQ";
	
	void connectAndCreateSession() throws NamingException, JMSException 
	{ 
		cf = (javax.jms.ConnectionFactory)ic.lookup("/ConnectionFactory");
		queue = (javax.jms.Queue)ic.lookup(destinationName);
		connection = cf.createConnection();
		session = connection.createSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE);
		connection.start();				
	}
	
	void produceMessage() throws JMSException 
	{
		String theECG = "1;02/20/2012 14:01:59.010;1020,1021,1022";
		javax.jms.MessageProducer publisher = session.createProducer(queue);
		javax.jms.TextMessage message = 
		session.createTextMessage(theECG);
		publisher.send(message);
		System.out.println("Message sent!");
		publisher.close();			
	}
	
	void consumeMessage() throws JMSException {
		javax.jms.MessageConsumer messageConsumer = session.createConsumer(queue);
		javax.jms.TextMessage messageReceived = (javax.jms.TextMessage)messageConsumer.receive(5000);
		insertMongo(messageReceived);
		System.out.println("Received message: " + messageReceived.getText());
		messageConsumer.close();		
	}
	
	private void insertMongo(TextMessage messageReceived) throws JMSException {
		
		try {
			m = new com.mongodb.Mongo();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (MongoException e) {
			e.printStackTrace();
		}
		
		db = m.getDB( "hornetqdb" );
			
		com.mongodb.DBCollection coll = db.getCollection("testCollection");
		com.mongodb.BasicDBObject doc = new com.mongodb.BasicDBObject();
		
		doc.put("name", "MongoDB");
		doc.put("type", "database");
		
		com.mongodb.BasicDBObject info = new com.mongodb.BasicDBObject();
		
		info.put("textmessage", messageReceived.getText()); 
				
	}

	public  void closeConnection()
	{
		if (session != null ) {
			try {
				session.close();
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
				
		if (connection != null) {
			try {
				connection.close();
			} catch (JMSException e) {		
				e.printStackTrace();
			}				
		}
		
	}
	void getInitialContext() throws NamingException
	{
		
		java.util.Properties p = new java.util.Properties();		
		
		p.put(javax.naming.Context.INITIAL_CONTEXT_FACTORY,"org.jnp.interfaces.NamingContextFactory");
		p.put(javax.naming.Context.URL_PKG_PREFIXES,"org.jboss.naming:org.jnp.interfaces");			
		p.put(javax.naming.Context.PROVIDER_URL, "jnp://localhost:1099");
			
		ic = new javax.naming.InitialContext(p);
		
		
	} 
	
	public static void main(String[] args) {
		try {
			ECGMessageProducerConsumerExample eCGMessageProducerConsumerExample = new ECGMessageProducerConsumerExample();
			
			eCGMessageProducerConsumerExample.getInitialContext();
			eCGMessageProducerConsumerExample.connectAndCreateSession();
			eCGMessageProducerConsumerExample.produceMessage();
			eCGMessageProducerConsumerExample.consumeMessage();
			eCGMessageProducerConsumerExample.closeConnection();

			
		}catch(Exception ex){
			ex.printStackTrace();
		}

	}

}
