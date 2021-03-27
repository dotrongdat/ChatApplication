/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emoji;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 *
 * @author TRONG DAT
 */
public class emojiChoosen extends JLayeredPane{
    emojiList emojis;
    public emojiChoosen(JTextArea textArea){
        super();
        initComponent();
        emojis=new emojiList(textArea);
        for (emoji emoji : emojis) {
            this.add(emoji);
        }
    }
    void initComponent(){
        this.setLayout(new FlowLayout(5, 5, 5));
        this.setPreferredSize(new Dimension(200, 200));
        this.setVisible(false);
    }
    public Icon getIcon(String S){
        for (emoji emoji : emojis) {
            if(emoji.name.equals(S)) return emoji.getIcon();
        }
        return null;
    }
}
