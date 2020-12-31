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

package chapter03;

import java.util.HashMap;
import java.util.Map;

import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.api.core.client.ClientSessionFactory;
import org.hornetq.api.core.client.HornetQClient;
import org.hornetq.api.core.client.ServerLocator;
import org.hornetq.core.remoting.impl.netty.NettyConnectorFactory;

public class MyCoreClientFactory {

	private static ClientSessionFactory factory = null; 
	static HashMap map = null;
	private static TransportConfiguration configuration;
	private static ServerLocator locator;

	public static ClientSessionFactory getConnectionFactory() throws Exception{
		if (factory == null){
			configuration = new TransportConfiguration(NettyConnectorFactory.class.getName(), map);
			locator = HornetQClient.createServerLocatorWithoutHA(configuration);
			factory =  locator.createSessionFactory();
		}
		return factory;		
	}

	public static Map<String, Object> createSettings(String host, int port) {
		
		if (map == null){
			
			map = new HashMap();
			map.put("host", host);
			map.put("port", port);
			
		}
		return map;
	}
	
	public static void close(){
		if (factory != null){
			factory.close();
			factory = null;			
		} 		
	}
}
