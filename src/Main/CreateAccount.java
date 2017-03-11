/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package Main;

import Vault.Vault;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
*
* @author Antonio's Laptop
*/
public class CreateAccount extends GUI{
    
    private Window w;
    private List<String> existingUsernames = new ArrayList<>();
    
    public CreateAccount(List<String> usernames){
        setLayout(null);
        existingUsernames.addAll(usernames);
    }
    
    public void open(){
        w = new Window("Password Vault by Antonio Kim - Create Account", 400, 300);
        addContent();
        w.add(this);
        w.setVisible(true);
    }
    
    private void addContent(){
        int y = w.convertYSmall(65);
        add(JLabel(w.convertXSmall(20), w.convertYSmall(10), "Create an Account", w.getFontSizeSmall(30)));
        add(JLabel(w.getWidth()/12, y, "Username:", w.getFontSizeSmall(25)));
        JTextField username = JTextField(w.getWidth()/9, y+w.convertYSmall(35), w.getWidth()*(7.0/9.0), w.convertYSmall(30), "");
        username.setFont(p.font.calibri(w.getFontSizeSmall(25)));
        add(username);
        add(JLabel(w.getWidth()/12, y+w.convertYSmall(80), "Password:", w.getFontSizeSmall(25)));
        JTextField password = JTextField(w.getWidth()/9, y+w.convertYSmall(115), w.getWidth()*(7.0/9.0), w.convertYSmall(30), "");
        password.setFont(p.font.calibri(w.getFontSizeSmall(25)));
        add(password);
        add(JCenterLabel(Main.w.getWidth()/2, w.getHeight()-w.convertScreenY(60), "Press Enter to Create Account", w.getFontSize(45)));
        
        KeyAdapter key = new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent ke){
                if (!username.getText().equals("") && !password.getText().equals("")){
                    Main.w.dispose();
                    p.delay(150);
                    String file = Main.encrpytor.createFilename(username.getText().trim());
                    if (file.equals("")){
                        file = username.getText();
                    }
                    PrintWriter pr = p.printwriter("./Vault Files/"+file+".txt");
                    pr.print(Main.encrpytor.encrypt(username.getText()+"$p1l7"+password.getText()+"$p1l70$p1l7"));
                    pr.close();
                    Main.login.vault = new Vault("./Vault Files/"+file+".txt");
                    Main.login.vault.open();
                }
                else{
                    JMessagePane("None of the above fields can be left blank", "Invalid Entry", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
    }
    
}
