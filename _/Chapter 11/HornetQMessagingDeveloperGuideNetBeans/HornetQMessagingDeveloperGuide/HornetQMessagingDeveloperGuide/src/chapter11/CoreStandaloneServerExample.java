package chapter11;

import org.hornetq.core.server.embedded.EmbeddedHornetQ;

public class CoreStandaloneServerExample {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		EmbeddedHornetQ embedded = new EmbeddedHornetQ();
		embedded.start();	
		Thread.sleep(10000);
		embedded.stop();

	}

}
