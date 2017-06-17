/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package Main;

import Encryptor.Encryptor;
import Vault.Vault;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
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
        JLabel instructions = JCenterLabel(Main.w.getWidth()/2, w.getHeight()-w.convertScreenY(60), "Press Enter to Create Account", w.getFontSize(45));
        add(instructions);
        
        KeyAdapter key = new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent ke){
                if (ke.getKeyCode() == KeyEvent.VK_ENTER){
                    if (!username.getText().equals("") && !password.getText().equals("")){
                        w.dispose();
                        p.delay(150);
                        String[] fileList = new File("./Vault Files").list();
                        String file = Main.encrpytor.getSimpleEncryption(username.getText())+Main.encrpytor.generateRandom(Math.max(10-username.getText().length(), 0));
                        for (int a = 0; a<fileList.length; a++){
                            if (fileList[a].equals(file)){
                                instructions.setText("Username already Exists!");
                                return;
                            }
                        }
                        PrintWriter pr = p.printwriter("./Vault Files/"+file+Encryptor.fileFormat);
                        pr.println(Main.encrpytor.getAdvancedEncryption(username.getText()+Encryptor.salt+Main.encrpytor.getSimpleEncryption(password.getText())));
                        pr.println(Main.encrpytor.getAdvancedEncryption("false"+Encryptor.salt+"0"+Encryptor.salt));
                        pr.close();
                        Main.login.vault = new Vault("./Vault Files/"+file+".txt");
                        Main.login.vault.open();
                    }
                    else if (username.getText().equals("") || password.getText().equals("")){
                        instructions.setText("Fields cannot be Blank");
                    }
                }
                else if (username.getText().equals("") || password.getText().equals("")){
                    instructions.setText("Fields cannot be Blank");
                }
                else{
                    instructions.setText("Press Enter to Create Account");
                }
            }
        };
        username.addKeyListener(key);
        password.addKeyListener(key);
    }
    
}
