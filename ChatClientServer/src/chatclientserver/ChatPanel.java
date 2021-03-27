/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatclientserver;

import SendFile.FileChooser;
import SendFile.StorageFile;
import emoji.emojiChoosen;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import static java.lang.Thread.sleep;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import sun.security.util.Length;

/**
 *
 * @author TRONG DAT
 */
public class ChatPanel extends JPanel{
    //JScrollPane chatTextScrollPane;
    JTabbedPane tabbedPane;
    //JTextPane chatTextPane;
    JPanel chatterPanel;
    JToolBar toolBar;
    JButton fileBtn;
    JButton emojiBtn;
    JButton closeTabChatBtn;
    JButton sendBtn;
    JTextArea chatterTextArea;
    emojiChoosen emojiLayerPane;
    JPanel btnPanel;
    SendFile.FileChooser filechooser;
    JScrollPane fileScrollPane;
    JPanel fileReceivePanel;
    JPanel filePanel;
    JButton showFileChooserBtn;
    
    Socket socket;  
    ObjectOutputStream oos=null;
    OutputThread t=null;
    String sender;
    String receiver;
    public ChatPanel(Socket socket, String sender,ObjectOutputStream oos,ObjectInputStream ois) {
        initComponent();
        this.socket = socket;
        this.sender = sender;
        //this.receiver = receiver;
        try {            
            
            this.oos=oos;            
            t=new OutputThread(socket,tabbedPane,sender,emojiLayerPane,fileReceivePanel,ois);
            //t.start();            
        } catch (Exception e) {
        }
        chatterTextArea.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void keyPressed(KeyEvent e) {
                //textAreaKeyPress();
            }

            @Override
            public void keyReleased(KeyEvent e) {
               //textAreaKeyPress();
            }
        });
        sendBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                 sendBtnActionPerformed(oos);
            }
        });
        
        emojiBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                emojiBtnActionPerformed();
            }
        });
        chatterTextArea.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                chatterTextAreaMouseClick();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseExited(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
        showFileChooserBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showFileChooserBtnActionPerformed();
            }
        });
        fileBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fileBtnActionPerformed();
            }
        });
        closeTabChatBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                closeTabChatBtnActionPerformed();
            }
        });
    }
    void initComponent(){
        this.setLayout(new BorderLayout(10, 10));
//        chatTextPane=new JTextPane();
//        chatTextPane.setEditable(false);
 //       chatTextScrollPane=new JScrollPane();
        tabbedPane=new JTabbedPane();
        chatterPanel=new JPanel(new BorderLayout(5, 5));
        fileBtn=new JButton("FILE");
        emojiBtn=new JButton("EMOJI");
        closeTabChatBtn=new JButton("CLOSE TAB");
//        closeTabChatBtn.setVisible(false);
        sendBtn=new JButton("SEND");
        toolBar=new JToolBar();
        toolBar.add(fileBtn);
        toolBar.add(emojiBtn);
        toolBar.add(closeTabChatBtn);
        chatterTextArea=new JTextArea();
        chatterTextArea.setLineWrap(true);
        emojiLayerPane=new emojiChoosen(chatterTextArea);
        btnPanel=new JPanel(new GridLayout(1, 2,20,20));
        btnPanel.add(toolBar);
        btnPanel.add(sendBtn);
        chatterPanel.add(btnPanel,BorderLayout.NORTH);
        chatterPanel.add(chatterTextArea,BorderLayout.CENTER);
        fileReceivePanel=new JPanel(new FlowLayout(1, 10, 10));
        fileReceivePanel.setPreferredSize(new Dimension(new Dimension(150, 200)));
        fileScrollPane=new JScrollPane(fileReceivePanel);
        fileScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        showFileChooserBtn=new JButton("Open");
        filePanel=new JPanel(new BorderLayout(10, 10));
        filePanel.add(fileScrollPane,BorderLayout.CENTER);
        filePanel.add(showFileChooserBtn,BorderLayout.SOUTH);
        filePanel.setVisible(false);
        this.add(tabbedPane,BorderLayout.CENTER);
        this.add(chatterPanel,BorderLayout.SOUTH);
        this.add(emojiLayerPane,BorderLayout.EAST);
        this.add(filePanel,BorderLayout.WEST);
    }
    void sendBtnActionPerformed(ObjectOutputStream oos){
        if(!this.chatterTextArea.getText().trim().equals("")){
            try {
                  
                DataSend newdata=new DataSend(chatterTextArea.getText().trim(), null,tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()),sender);
                excuteChatPane(newdata);  
                oos.writeObject(newdata);                  
                  oos.flush();
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "You must connect to a friend to chat");
            }
        }
        this.chatterTextArea.setText("");
    }
    void fileBtnActionPerformed(){
        filePanel.setVisible(!filePanel.isVisible());       
    }
    void showFileChooserBtnActionPerformed(){
        try{
        filechooser=new SendFile.FileChooser(sender,tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()), oos);
        }catch(Exception e){
            
        }
    }
    void emojiBtnActionPerformed(){
        if(this.emojiLayerPane.isVisible()) this.emojiLayerPane.setVisible(false);
        else this.emojiLayerPane.setVisible(true);
    }
    void closeTabChatBtnActionPerformed(){
        int tabSelect= this.tabbedPane.getSelectedIndex();
        if(tabSelect!=-1) this.tabbedPane.remove(tabSelect);
    }
    void chatterTextAreaMouseClick(){
        this.emojiLayerPane.setVisible(false);
    }
     void appendTextPaneString(String str,JTextPane chatTextPane) throws BadLocationException
    {
     StyledDocument document = (StyledDocument) chatTextPane.getDocument();
     document.insertString(document.getLength(), str, null);
     }
     void excuteChatPane(DataSend data){
           String receiver=data.receiver.trim();
           String sender=data.sender.trim();
           int receiverTab=tabbedPane.indexOfTab(receiver);
          // System.out.println(receiverTab);
//           if(receiverTab==-1) {
//               JTextPane newTextPane=new JTextPane();
//               newTextPane.setEditable(false);
//               JScrollPane newScroll=new JScrollPane(newTextPane);
//               tabbedPane.add(receiver, newScroll);
//               receiverTab=tabbedPane.getTabCount()-1;
//           }
           JTextPane textPane=(JTextPane)tabbedPane.getComponentAt(receiverTab);
           try {
           this.appendTextPaneString("\n"+sender+":\n",textPane);  
           String[] line=data.text.split("\n");
           for (String string : line){
                 int index=0; 
                 while((index=string.indexOf("@",index))!=-1 && string.length()-index>=4){
                     try {
                         String str;
                         if((str=string.substring(index, index+4)).matches("[@][0-9][0-9][@]")){ 
                             Icon icon=new ImageIcon(((ImageIcon)emojiLayerPane.getIcon(str)).getImage());
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