/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatclientserver;

import java.awt.Container;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

/**
 *
 * @author TRONG DAT
 */
public class Server extends JFrame implements Runnable{
    ServerSocket svrSocket;
    ObjectInputStream ois;
    ObjectOutputStream oos;
    Thread t;
    final int port=6000;
    ArrayList<clientSocket> clientSocket;
    
    JLabel label;
    public Server(){
        super();
        initComponent();
        try {
            svrSocket=new ServerSocket(port);
            label.setText("Server is running...");
        } catch (Exception e) { 
            JOptionPane.showMessageDialog(this, "Error");
        }
        clientSocket=new ArrayList();
        t=new Thread(this);
        t.start();
    }
    void initComponent(){
        label=new JLabel();
        this.add(label);
    }
    clientSocket checkClientSocket(String name){
        for (clientSocket clientsocket : clientSocket) {
            if(clientsocket.name.equals(name.trim())) return clientsocket; 
        }
        return null;
    }
    boolean checkName(String name){
        for (clientSocket clientsocket : clientSocket) {
            if(clientsocket.name.equals(name.trim())) return false;
        }
        return true;
    }
//    @Override
//    public void run() {
//        while(true){
//            try {
//                Socket client=svrSocket.accept();
//                if(client!=null){
//                   // clientSocket.add(client);
//                    ois=new ObjectInputStream(client.getInputStream());
//                    oos=new ObjectOutputStream(client.getOutputStream());
//                    String S = null;
//                    try {
//                        S=((DataSend)ois.readObject()).text;
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    int pos=S.indexOf(":");
//                    String clientName=S.substring(pos+1);
//                    ChatPanel p=new ChatPanel(client, "Server", clientName,oos,ois);
//                    clientTabPane.add(clientName,p);
//                    p.updateUI();
//                }
//                Thread.sleep(1000);
//            } catch (Exception e) {
//                JOptionPane.showMessageDialog(this, "Error");
//            }
//        }
//    }
     @Override
    public void run() {
        while(true){
            try {
                Socket client=svrSocket.accept();
                if(client!=null){                    
                    ObjectInputStream clientOis=new ObjectInputStream(client.getInputStream());
                    ObjectOutputStream clientOos=new ObjectOutputStream(client.getOutputStream());
                    String name=((DataSend)clientOis.readObject()).sender;
                    clientSocket newSocket=null;
                    if(checkName(name)) {
                        DataSend newData=new DataSend("", null, name, "Server");
                        clientOos.writeObject(newData);
                        newSocket=new clientSocket(name, clientOos);
                        clientSocket.add(newSocket);
//                        this.add(new JLabel(name));

                        handlingData newThread=new handlingData(clientSocket,name, clientOis,clientOos);
                        newThread.start();
                    }
                    else {
                        DataSend newData=null;
                        clientOos.writeObject(newData);
                        clientOos.flush();
                        clientOis.close();
                        clientOos.close();
                    }
                }
                Thread.sleep(1000);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error");
            }
        }
    }
    class handlingData extends Thread{
        ObjectInputStream ois=null;
        ObjectOutputStream oos=null;
//        clientSocket socket=null;
        ArrayList<clientSocket> clients;
        String clientName;
        public handlingData(ArrayList<clientSocket> clients,String clientName ,ObjectInputStream ois,ObjectOutputStream oos){
            this.clientName=clientName;
            this.ois=ois;
            this.clients=clients;
            this.oos=oos;
        }

        @Override
        public void run() {
            
            try {
                while(true){
                DataSend data=(DataSend)ois.readObject();
                boolean check=false;
                for (clientSocket client : clients) {
                    if(data.receiver.trim().equals(client.name.trim())) {
                        check=true;
                        client.sendData(data);
                    }                         
                }
                if(!check){
                        data.sender=data.receiver;
                        data.text="[SERVER]Not online";
                        oos.writeObject(data);
                        oos.flush();
                }
                }
            } catch (Exception e) {
                for (clientSocket client : clients) {
                    if(client.name.equals(clientName)) {
                        clients.remove(client);
                        return;
                    }
                }
            }
        }
        
    }
    public static void main(String[] args) {
        Server svr=new Server();
        svr.setSize(new Dimension(300, 50));
        svr.setVisible(true);
        svr.setResizable(false);
        svr.setDefaultCloseOperation(EXIT_ON_CLOSE);
        svr.run();
    }
}
//create class extends Socket class 
//Set name to send dataSend with name 
//find socket with name in dataSend & get dataSend by this socket 