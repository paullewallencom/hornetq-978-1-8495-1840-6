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

import java.util.Date;
import org.hornetq.api.core.client.ClientMessage;
import org.hornetq.api.core.client.MessageHandler;

/**
 *
 * @author piero.giacomelli
 */
public class myMessageHandler implements MessageHandler  {

    @Override
    public void onMessage(ClientMessage message) {
        System.out.println("received message " + message.getMessageID() + " at  " + new Date(message.getTimestamp()));
    }
    
}
