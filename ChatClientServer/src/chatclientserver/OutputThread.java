/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatclientserver;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import javafx.stage.FileChooser;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

/**
 *
 * @author TRONG DAT
 */
public class OutputThread extends Thread{
    Socket socket;
    JTabbedPane tabbedPane;
   // JTextPane textPane;
    ObjectInputStream ois=null;
    String sender;
    //String receiver;
    emoji.emojiChoosen emojis;
    JPanel filepanel; 

//    public OutputThread(Socket socket, JTextPane textPane, String sender, String receiver, emoji.emojiChoosen emojis, JPanel filepanel,ObjectInputStream ois) {
//        super();
//        this.socket = socket;
//        this.textPane = textPane;
//        this.sender = sender;
//        this.receiver = receiver;
//        this.emojis=emojis;
//        this.filepanel=filepanel;
//        this.ois=ois;
//    }
    public OutputThread(Socket socket, JTabbedPane tabbedPane, String sender, emoji.emojiChoosen emojis, JPanel filepanel,ObjectInputStream ois){
        super();
        this.socket = socket;
        this.tabbedPane = tabbedPane;
        this.sender = sender;
        this.emojis=emojis;
        this.filepanel=filepanel;
        this.ois=ois;
    }
    @Override
    public void run(){
        while (true) {             
            try {
                if(socket!=null){
                   // if(ois==null) ois=new ObjectInputStream(socket.getInputStream());   
                    DataSend data = null;
                    try {
                        data=(DataSend)ois.readObject();
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Fail to connect to server");
                        e.printStackTrace();
                        System.exit(0);
                    }                    
                    if(data!=null){
                    excuteChatPane(data);
                    if(data.data!=null) filepanel.add(new SendFile.StorageFileButton(data.data));}
//                    }else JOptionPane.showMessageDialog(null, "Your name has existed. Please try with another name");
                }
                sleep(1000);
            } catch (Exception e) {
                //e.printStackTrace();
            }
        }
    }
    void excuteChatPane(DataSend data){
       // try{
           if(data.text!=null){
           String sender=data.sender.trim();
           int receiverTab=tabbedPane.indexOfTab(sender);
           if(receiverTab==-1) {
               JTextPane newTextPane=new JTextPane();
               newTextPane.setEditable(false);
               newTextPane.add(new JScrollPane());
               tabbedPane.add(sender, newTextPane);
               receiverTab=tabbedPane.indexOfTab(sender);
           }
           JTextPane textPane=(JTextPane)tabbedPane.getComponentAt(receiverTab);
//        }catch(Exception e){
//            System.out.println("fail");
//        }
           try {
           this.appendTextPaneString("\n"+sender+":\n",textPane);  
           String[] line=data.text.split("\n");
           for (String string : line){
                 int index=0; 
                 while((index=string.indexOf("@",index))!=-1 && string.length()-index>=4){
                     try {
                         String str;
                         if((str=string.substring(index, index+4)).matches("[@][0-9][0-9][@]")){ 
                             Icon icon=new ImageIcon(((ImageIcon)emojis.getIcon(str)).getImage());
                             this.appendTextPaneString(string.substring(0, index),textPane);
                             textPane.setCaretPosition(textPane.getDocument().getLength());
                             textPane.insertIcon(icon);
                             string=string.substring(index+4, string.length());
                             index=0;
                         }else index++;
                     } catch (Exception e) {
                         index++;
                     }
                 
                 }
                 this.appendTextPaneString(string+"\n",textPane);
             }
             }catch (Exception e) {
            }
           }
    }
         
    void appendTextPaneString(String str, JTextPane textPane) throws BadLocationException
    {
     StyledDocument document = (StyledDocument) textPane.getDocument();
     document.insertString(document.getLength(), str, null);
     }
}
