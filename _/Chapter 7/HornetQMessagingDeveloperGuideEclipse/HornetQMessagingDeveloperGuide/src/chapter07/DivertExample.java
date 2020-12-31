/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter07;

import javax.jms.*;
import javax.naming.NamingException;

/**
 *
 * @author piero.giacomelli
 */
public class DivertExample {
    
    public static void main(String[] argcv) throws NamingException, JMSException, InterruptedException {

        
	javax.naming.Context initialContextA = null;
        javax.naming.Context initialContextB = null;
	
	javax.jms.Connection connectionA =  null;
        javax.jms.Connection connectionB =  null;
        
	javax.jms.Session sessionA = null;
        javax.jms.Session sessionB = null;
        
        java.util.Properties p = new java.util.Properties();		

        p.put(javax.naming.Context.INITIAL_CONTEXT_FACTORY,"org.jnp.interfaces.NamingContextFactory");
        p.put(javax.naming.Context.URL_PKG_PREFIXES,"org.jboss.naming:org.jnp.interfaces");			
        p.put(javax.naming.Context.PROVIDER_URL, "jnp://localhost:1099");
        
        
        Queue ECGQueue = (Queue)initialContextA.lookup("/queue/ECQQueue");
        Queue AlertQueue = (Queue)initialContextB.lookup("/queue/AlertQueue");

        ConnectionFactory cfA = (ConnectionFactory)initialContextA.lookup("/ConnectionFactory");
        ConnectionFactory cfB = (ConnectionFactory)initialContextB.lookup("/ConnectionFactory");

        connectionA = cfA.createConnection();
        connectionB = cfB.createConnection();
        
        
        MessageProducer ECGProducer = sessionA.createProducer(ECGQueue);
        MessageConsumer ECGConsumer = sessionB.createConsumer(ECGQueue);
        MessageConsumer AlertConsumer = sessionB.createConsumer(AlertQueue);

        connectionA.start();
        connectionB.start();
        
        
        String ECG_TEXT = "1;31/01/2012 15:45:01.100;1021;1022;1023";
        TextMessage ECGMessage = sessionA.createTextMessage(ECG_TEXT);

        ECGProducer.send(ECGMessage);

        System.out.println("Message " + ECGMessage.getText() + " send to server A" );

        TextMessage receivedMessageA = (TextMessage)ECGConsumer.receive(5000);

        System.out.println("Receiving message " + receivedMessageA.getText() + " from server A" );
        
        String ALERT_TEXT = "this is an alert";
        ECGMessage = sessionA.createTextMessage(ALERT_TEXT);
        ECGProducer.send(ECGMessage);

        System.out.println("Message " + ECGMessage.getText() + " send to server A" );
        
        if (receivedMessageA == null)
        {
            System.out.println("no message to be received from server A" );
        }
        
        TextMessage receivedMessageB = (TextMessage)ECGConsumer.receive(5000);
        
        System.out.println("Receiving message " + receivedMessageB.getText() + " from server B" );

        System.out.println("message received with priority " + receivedMessageB.getJMSPriority() + " from server B" );
        


        
    }
}
