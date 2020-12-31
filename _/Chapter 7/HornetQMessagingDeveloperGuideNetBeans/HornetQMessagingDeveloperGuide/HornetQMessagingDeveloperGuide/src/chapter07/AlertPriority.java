/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter07;

import org.hornetq.core.server.ServerMessage;

/**
 *
 * @author piero.giacomelli
 */
public class AlertPriority implements org.hornetq.core.server.cluster.Transformer  {
    
 public ServerMessage transform(ServerMessage message)
   {
      //message.setPriority(true);
      return message;
   }
    
}
