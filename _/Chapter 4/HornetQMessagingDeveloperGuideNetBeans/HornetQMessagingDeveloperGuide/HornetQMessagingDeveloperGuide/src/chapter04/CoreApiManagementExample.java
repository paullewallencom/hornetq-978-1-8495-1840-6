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
import java.util.HashMap;

import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.api.core.client.ClientMessage;
import org.hornetq.api.core.client.ClientProducer;
import org.hornetq.api.core.client.ClientRequestor;
import org.hornetq.api.core.client.ClientSession;
import org.hornetq.api.core.client.ClientSessionFactory;
import org.hornetq.api.core.client.HornetQClient;
import org.hornetq.api.core.client.ServerLocator;
import org.hornetq.api.core.management.ManagementHelper;
import org.hornetq.core.remoting.impl.netty.NettyConnectorFactory;

public class CoreApiManagementExample {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
		ServerLocator serverLocator = null;
		ClientSessionFactory factory = null;
		ClientSession session = null;
		
		HashMap map = new HashMap();
		map.put("host", "localhost");
		map.put("port", 5445);
		
		try{
		
		TransportConfiguration configuration = new TransportConfiguration(NettyConnectorFactory.class.getName(), map);
		
		serverLocator = HornetQClient.createServerLocatorWithoutHA(configuration);
		factory = serverLocator.createSessionFactory();
		        
		session = factory.createSession(false, false, false);
		session.start();
		
		ClientMessage message = session.createMessage(false);
        
        final String queueName = "queue.exampleQueue";
        ClientProducer producer = session.createProducer(queueName);
        	
        for (int i = 0; i < 10;i++){
            message.getBodyBuffer().clear();
            message.getBodyBuffer().writeString("new message sent at " + new Date());
            System.out.println("Sending the message.");
            producer.send(message);            
        }
        
        session.close();
        
        ClientSession managedsession = factory.createSession();
        ClientRequestor requestor = new ClientRequestor(managedsession, "hornetq.management");
        ClientMessage messagemanaged = managedsession.createMessage(false);
        ManagementHelper.putAttribute(messagemanaged, queueName, "messageCount");
        ClientMessage reply = requestor.request(messagemanaged);
        int count = (Integer) ManagementHelper.getResult(reply);
        System.out.println("There are " + count + " messages in exampleQueue");
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		
		System.exit(-1);

	}

}
