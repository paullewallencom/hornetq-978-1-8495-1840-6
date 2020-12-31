package chapter11;

import org.hornetq.jms.server.embedded.EmbeddedJMS;

public class JMSServerExample {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		EmbeddedJMS jmsServer = new EmbeddedJMS();
        jmsServer.start();

	}

}
