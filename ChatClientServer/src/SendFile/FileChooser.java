/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SendFile;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author TRONG DAT
 */
public class FileChooser extends JFrame{
    JFileChooser filechooser;
    JTextField pathField;
    JButton sendBtn;
    JButton openBtn;
    JPanel btnPanel;
    JPanel sendPanel;
    StorageFile data=null;
    String receiver;
    String sender;
    ObjectOutputStream oos=null;
    JTabbedPane tabbedPane;
    public FileChooser(String sender,String receiver ,ObjectOutputStream oos){
        super();
        this.sender=sender;
        this.oos=oos;
        this.receiver=receiver;
        initComponent();
        sendBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendBtnActionPerformed();
            }
        });
        openBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filechooserActionPerformed();
            }
        });
    }
    void initComponent(){
        this.setSize(new Dimension(500, 80));
        this.setLayout(new BorderLayout(20, 20));
        this.setVisible(true);
        this.setResizable(false);
        filechooser=new JFileChooser();
        filechooser.setFileHidingEnabled(true);
        filechooser.setDialogType(JFileChooser.OPEN_DIALOG);
        filechooser.setMultiSelectionEnabled(false);
        filechooser.setApproveButtonText("Select");
        filechooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
       
        //sendPanel=new JPanel(new BorderLayout(10, 10));
        pathField=new JTextField("");
        pathField.setEditable(false);
        sendBtn=new JButton("Send to "+receiver);
        openBtn=new JButton("Open");
        btnPanel=new JPanel(new GridLayout(1, 2, 10, 10));
        btnPanel.add(openBtn);
        btnPanel.add(sendBtn);
        
        this.add(btnPanel,BorderLayout.EAST);
        this.add(pathField,BorderLayout.CENTER);
    }
    void sendBtnActionPerformed(){
        try {
            String path=pathField.getText();
        if(path.equals("")){
            JOptionPane.showMessageDialog(this, "Please choose a file less than or equal 1MB");
        }else{
            File f=new File(path);
            if(f.length()>(1024*1024)) JOptionPane.showMessageDialog(this, "Please choose a file less than or equal 1MB");
            else if(!f.canRead()) JOptionPane.showMessageDialog(this, "Can't send this file");
            else{
                FileInputStream fis = null;
                try {
                   fis =new FileInputStream(f);
                    byte[]getData=new byte[fis.available()];
                    fis.read(getData);
                    String name=f.getName();
                    data=new StorageFile(name, getData);
                } catch (Exception e) {
                    e.printStackTrace();
                }finally{
                    try {
                        if(fis!=null) fis.close();
                    } catch (Exception e) {
                    }                 
                }
            }
        }
        if(data!=null){
            oos.writeObject(new chatclientserver.DataSend("[You receive a new file]: "+data.name, data,receiver,sender));
            oos.flush();
            JOptionPane.showMessageDialog(this, "Done");
        }
        } catch (Exception e) {
            e.printStackTrace();
        }
        data=null;
        this.pathField.setText("");
    }
    void filechooserActionPerformed(){
        int choose=filechooser.showSaveDialog(this);
        if(choose==JFileChooser.APPROVE_OPTION){
            this.pathField.setText(this.filechooser.getSelectedFile().getPath());
        }else if(choose==JFileChooser.CANCEL_OPTION){
            filechooser.cancelSelection();
        }
    }
}
