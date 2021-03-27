/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SendFile;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import javax.swing.JButton;
import javax.swing.JOptionPane;

/**
 *
 * @author TRONG DAT
 */
public class StorageFileButton extends JButton{
    StorageFile data;
    public StorageFileButton(StorageFile data){
        super();
        this.data=data;
        initComponent();
        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnActionPerformed();
            }
        });
    }
    void initComponent(){
        this.setText(data.name);
        this.setPreferredSize(new Dimension(120, 25));
    }
    void btnActionPerformed(){
        try {
           String filepath=".\\downloads";    
        File f=new File(filepath);
        if(!f.isDirectory()) f.mkdir();
        int indexOfDot=data.name.lastIndexOf(".");
        String tailFile="";
        String onlyName=data.name;
        if(indexOfDot!=-1){
        onlyName=data.name.substring(0, indexOfDot);
        tailFile=data.name.substring(indexOfDot, data.name.length());
        }
        f=new File(filepath+"\\"+onlyName);
        int num=0;
          while(!f.createNewFile() && num<10){
            num++;
             f=new File(filepath+"\\"+onlyName+num+tailFile);            
        }    
        FileOutputStream fos = null;
        try {
            fos=new FileOutputStream(f);
            fos.write(data.data);
        } catch (Exception e) {
            //JOptionPane.showMessageDialog(this, "Fail to download");
            e.printStackTrace();
        }finally{
            if(fos!=null) fos.close();
        } 
            JOptionPane.showMessageDialog(this, "Done download");
        } catch (Exception e) {
            //JOptionPane.showMessageDialog(this, "Fail to download");
             e.printStackTrace();
        }
        
    }
}
