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

import org.hornetq.api.core.HornetQException;
import org.hornetq.api.core.client.ClientSession;

public class MyCoreSession {

	static ClientSession session = null;
	
	

	public static ClientSession getSession() throws HornetQException, Exception{

		if (session == null) {
			System.out.println("creating session at " + new java.util.Date());			
			session = MyCoreClientFactory.getConnectionFactory().createSession(false, true, true);;			
				
		}
		
		return session;
	}
	
	
	public static void start() throws HornetQException{
		if (session != null)
			session.start();
	}
	
	public static void close() throws HornetQException{
		if (session != null)
			session.close();
	}
	
	
}
