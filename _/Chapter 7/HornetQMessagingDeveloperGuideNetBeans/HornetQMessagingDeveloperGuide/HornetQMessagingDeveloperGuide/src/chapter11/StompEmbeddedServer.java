package chapter11;

import javax.security.auth.login.Configuration;

import org.hornetq.core.config.impl.ConfigurationImpl;

public class StompEmbeddedServer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Configuration configuration = new ConfigurationImpl();

		configuration.setSecurityEnabled(false);
		configuration.getAcceptorConfigurations().add(new TransportConfiguration(InVMAcceptorFactory.class.getName()));
		configuration.getQueueConfigurations().add(new QueueConfiguration("jms.queue.ECGQueue", "jms.queue.ECGQueue",null, true));


		HornetQServer hornetqServer = HornetQServers.newHornetQServer(configuration);
		JMSServerManager jmsServer = new JMSServerManagerImpl(hornetqServer);
		jmsServer.start();

		ConnectionFactory connectionFactory = HornetQJMSClient.createConnectionFactory(
		new TransportConfiguration(InVMConnectorFactory.class.getName()));

		StompConnect stompConnect = new StompConnect(connectionFactory);
		stompConnect.start();

	}

}
