/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatclientserver;

import emoji.emojiChoosen;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;

/**
 *
 * @author TRONG DAT
 */
public class ChatClient extends JFrame{
    Socket socket;
    final int port=6000;
    String clientName;
//    boolean createChatPanel=false;
    ObjectOutputStream oos;
    ObjectInputStream ois;
    threadCheckConnect check;
    emoji.emojiChoosen emojis;
            
    JPanel panel;
    JLabel clientLabel;
    JLabel receiverLabel;
    JTextField clientTextField;
    JTextField receiverTextField;
    ChatPanel chatpanel;
    JButton connectBtn;
    JButton connectToChatBtn;
    public ChatClient() throws IOException{
        super();
        initComponent();
        connectBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connectBtnActionPerformed();
            }
        });
        connectToChatBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connectToChatBtnActionPerformed();
            }
        });
    }
    void initComponent(){
        panel=new JPanel(new GridLayout(1, 3, 5, 5));
        clientLabel=new JLabel("NAME");
        clientTextField=new JTextField("");
        connectBtn=new JButton("LOGIN");
        receiverLabel=new JLabel("FRIEND");
        receiverTextField=new JTextField("");
        connectToChatBtn=new JButton("CONNECT");
        panel.add(clientLabel);
        panel.add(clientTextField);
//        panel.add(receiverLabel);
//        panel.add(receiverTextField);
        panel.add(connectBtn);
       
        this.setLayout(new BorderLayout(5, 5));
        this.add(panel,BorderLayout.NORTH);
        
    }
    void connectBtnActionPerformed(){
        clientName=clientTextField.getText(); 
        if(!clientName.trim().equals("")){
        try {
            socket=new Socket("localhost",port);            
            if(socket!=null){
                oos=new ObjectOutputStream(socket.getOutputStream());
                 chatpanel=new ChatPanel(socket, clientName, oos, ois);
//                ChatPanel p=new ChatPanel(socket, clientName, "Server",oos,null);
//                outputTabbedPanel.add(p,"Server");
//                p.appendTextPaneString("Server is running");
//                p.updateUI();
                oos.writeObject(new DataSend(null, null,"Server",clientName));
                oos.flush();
                clientTextField.setText("");
                check=new threadCheckConnect(socket, "Your name is existed. Try with another name",chatpanel.t) {
                    @Override
                    void editComponent() {
                        panel.removeAll();
                        panel.setLayout(new GridLayout(1, 4, 5, 5));
                        clientLabel.setText(clientName);
                        panel.add(clientLabel);
                        panel.add(receiverLabel);
                        panel.add(receiverTextField);
                        panel.add(connectToChatBtn);
                        panel.updateUI();
                    }
                };                
                check.start();
              //  chatpanel.t.ois=check.ois;
                this.add(chatpanel,BorderLayout.CENTER);
            }
        } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Server is not running");
                System.exit(0);
        }
        }else JOptionPane.showMessageDialog(this, "Input your name");
    }
    abstract class threadCheckConnect extends Thread{
        ObjectInputStream ois=null;
        Socket socket;
        String outputResult;
        //boolean check=false;
        String outputConnected="Connect successfully";
        OutputThread t;
        public threadCheckConnect(Socket socket,String outputResult, OutputThread t){
            super();
            this.socket=socket;
            this.outputResult=outputResult;
            this.t=t;
        }

        @Override
        public void run() {
            try {
                ois=new ObjectInputStream(socket.getInputStream());
                DataSend data=(DataSend)ois.readObject();
                if(data==null) JOptionPane.showMessageDialog(null, outputResult);
                else {
                    editComponent();
                    JOptionPane.showMessageDialog(null,outputConnected);
                    t.ois=this.ois;
                    t.start();
                }
            } catch (Exception e) {
            }
            
        }
        abstract void editComponent();
    }
    void connectToChatBtnActionPerformed(){
        try {
            
            String receiver=receiverTextField.getText().trim();
            if(!receiver.equals("") && !receiver.equals(clientName)){
                int receiverTab=chatpanel.tabbedPane.indexOfTab(receiver);
                if(receiverTab==-1){
                JTextPane newTab=new JTextPane();
                newTab.add(new JScrollPane());
                newTab.setEditable(false);
                chatpanel.tabbedPane.add(receiver,newTab);
                }
                
                oos.writeObject(new DataSend(null, null, receiver,clientName));
                oos.flush();
                //ois=check.ois;
//                if(!this.createChatPanel) {
//                p=new ChatPanel(socket, clientName, oos, check.ois);
//                this.add(p,BorderLayout.CENTER);
//                JTextPane newTab=new JTextPane();
//                newTab.setEditable(false);
//                p.tabbedPane.add(receiver,new JScrollPane(newTab));
//                p.updateUI();
//                p.t.start();
//                this.createChatPanel=false;
//                }else{

//                chatpanel.updateUI();  
                 receiverTextField.setText("");
            }else JOptionPane.showMessageDialog(this,"Invalid input");
        } catch (Exception e) {
            //System.out.println("eqweqwe");
            e.printStackTrace();
            
        }
    }
    public static void main(String[] args) {
        try {
            ChatClient client=new ChatClient();
            client.setDefaultCloseOperation(EXIT_ON_CLOSE);
            client.setSize(new Dimension(800, 600));
            client.setResizable(false);
            client.setVisible(true);
        } catch (Exception e) {
            System.exit(0);
        }
        
        
    }
}
