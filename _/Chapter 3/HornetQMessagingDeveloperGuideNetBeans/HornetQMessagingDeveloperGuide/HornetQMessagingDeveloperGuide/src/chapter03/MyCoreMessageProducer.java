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

public class MyCoreMessageProducer {

	private org.hornetq.api.core.client.ClientProducer producer;
	private String queuename;
	private org.hornetq.api.core.client.ClientMessage message;

	public void send(String _message) throws HornetQException, Exception
	{
		message = MyCoreSession.getSession().createMessage(false);
		message.putStringProperty("ecg", _message);
		producer.send(message);
		System.out.println("message sent successfully");
		
	}
	public void setQueuename(String queuename) throws HornetQException, Exception {
		// TODO Auto-generated method stub
		if (producer == null){
			producer = MyCoreSession.getSession().createProducer(queuename);			
		}		
		
	}

	
}
