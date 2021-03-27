/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emoji;

import java.io.File;
import java.util.ArrayList;
import javax.swing.JTextArea;

/**
 *
 * @author TRONG DAT
 */
public class emojiList extends ArrayList<emoji>{
    final String path=".\\emoji";
    public emojiList(JTextArea textArea){
        super();
        loadFile(textArea);
    }
    void loadFile(JTextArea textArea){
        File f=new File(path);
        if(f.isDirectory()){
            File[] listFile=f.listFiles();
            for (File file : listFile) {
                String filename=file.getName();
                if(filename.matches(".+[.][p][n][g]")){
                    this.add(new emoji("@"+filename.substring(0, filename.length()-4)+"@", file.getPath(),textArea));
                }
            }
        }
    }
}
