/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatclientserver;

import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * @author TRONG DAT
 */
//class to manage clientSocket when connection is set up
public class clientSocket extends Socket{
      String name;
     // ObjectInputStream ois;
      ObjectOutputStream oos;
      public clientSocket(String name, ObjectOutputStream oos){
          super();
          this.name=name;
         this.oos=oos;
      }
          
      public boolean sendData(DataSend data){
          try {
              oos.writeObject(data);
              oos.flush();
              return true;
          } catch (Exception e) {
              e.printStackTrace();
              return false;
          }
      }
}
