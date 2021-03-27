/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SendFile;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.Serializable;
import javax.swing.JButton;
import javax.swing.JOptionPane;

/**
 *
 * @author TRONG DAT
 */
public class StorageFile implements Serializable{
    String name;
    byte[]data;
    public StorageFile(String name,byte[]data){
        this.name=name;
        this.data=data;
        
    }
}
