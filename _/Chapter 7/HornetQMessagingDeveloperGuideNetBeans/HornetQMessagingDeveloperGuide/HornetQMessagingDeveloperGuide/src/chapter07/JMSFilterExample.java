/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter07;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.*;
import javax.naming.NamingException;

/**
 *
 * @author piero.giacomelli
 */
public class JMSFilterExample {
    private static volatile boolean result = true;    
    
    public static void main(String[] argcv) throws NamingException, JMSException, InterruptedException {
        
	javax.naming.Context ic = null;
	javax.jms.ConnectionFactory cf = null;
	javax.jms.Connection connection =  null;
	javax.jms.Queue queue = null;
	javax.jms.Session session = null;
        String destinationName = "queue/ECGQueue";
        String ECG_TEXT = "1;31/01/2012 15:45:01.100;1021;1022;1023";
        
        java.util.Properties p = new java.util.Properties();		

        p.put(javax.naming.Context.INITIAL_CONTEXT_FACTORY,"org.jnp.interfaces.NamingContextFactory");
        p.put(javax.naming.Context.URL_PKG_PREFIXES,"org.jboss.naming:org.jnp.interfaces");			
        p.put(javax.naming.Context.PROVIDER_URL, "jnp://localhost:1099");

        ic = new javax.naming.InitialContext(p);
        
        cf = (javax.jms.ConnectionFactory)ic.lookup("/ConnectionFactory");
        queue = (javax.jms.Queue)ic.lookup(destinationName);
        connection = cf.createConnection();
        session = connection.createSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE);
        connection.start();				
        
        
        MessageProducer producer = session.createProducer(queue);
                 
        MessageConsumer consumer = session.createConsumer(queue);
        consumer.setMessageListener(new FilterMessageListener());
        
        TextMessage ecgMessage = session.createTextMessage("ECG_TEXT");
        ecgMessage.setStringProperty("isECGMeasure", "true");
        
        producer.send(ecgMessage);
        
        TextMessage deviceMessage = session.createTextMessage("DEVICE01;low battery level;");
        //deviceMessage.setStringProperty("isECGMeasure", "false");
        
        producer.send(deviceMessage);
        
        Thread.sleep(5000);
        
        if (ic != null)
        {
           ic.close();
        }
        if (connection != null)
        {
            connection.close();
        }

        
        
    }

    private static class FilterMessageListener implements MessageListener {

        public FilterMessageListener() {
        }

        @Override
        public void onMessage(Message msg) {
            TextMessage message = (TextMessage)msg;
            try {
                if (message.getStringProperty("isECGMeasure") == "true") {
                    System.out.println(message.getText() + " with property isECGMeasure " + message.getStringProperty("isECGMeasure"));                    
                    result = true;
                } else {
                    result = false;
                }
                
            } catch (JMSException ex) {
                ex.printStackTrace();
            }
        }
    }
}
