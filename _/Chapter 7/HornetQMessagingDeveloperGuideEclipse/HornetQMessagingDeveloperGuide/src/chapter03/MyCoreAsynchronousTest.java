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

public class MyCoreAsynchronousTest {

	/**
	 * @param args
	 */
    public static void main(String[] args) {
            // TODO Auto-generated method stub
            try {

                MyCoreClientFactory.createSettings("localhost", 5445);

                MyCoreMessageProducer m = new MyCoreMessageProducer();			


                MyCoreMessageProducer c = new MyCoreMessageProducer();
                MyCoreMessageConsumer.getAsynchronousMessages("jms.queue.ECGQueue");    
                //MyCoreMessageConsumer.getMessages("jms.queue.ECGQueue");
                
                m.setQueuename("jms.queue.ECGQueue");
                m.send("1;02/20/2012 14:01:59.010;1020,1021,1022");
                


            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                        MyCoreSession.close();
                } catch (HornetQException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }
                MyCoreClientFactory.close();			
            }

            System.exit(0);

    }

}
