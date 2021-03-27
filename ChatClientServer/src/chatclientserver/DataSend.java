/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatclientserver;

import java.io.Serializable;

/**
 *
 * @author TRONG DAT
 */
public class DataSend implements Serializable{
    String text;
    SendFile.StorageFile data;
    String receiver;
    String sender;
//    public DataSend(String text,SendFile.StorageFile data){
//        this.text=text;
//        this.data=data;
//    }
    public DataSend(String text,SendFile.StorageFile data, String receiver,String sender){
        this.text=text;
        this.data=data;
        this.receiver=receiver;
        this.sender=sender;
    }
}
