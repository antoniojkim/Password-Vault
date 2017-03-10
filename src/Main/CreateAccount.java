/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package Main;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
*
* @author Antonio's Laptop
*/
public class CreateAccount extends GUI{
    
    List<String> existingUsernames = new ArrayList<>();
    
    public CreateAccount(List<String> usernames){
        setLayout(null);
        existingUsernames.addAll(usernames);
        int y = p.convertYSmall(65);
        add(JLabel(p.convertXSmall(20), p.convertYSmall(10), "Create an Account", p.getFontSizeSmall(30)));
        add(JLabel(Main.w.getWidth()/12, y, "Username:", p.getFontSizeSmall(25)));
        JTextField username = JTextField(Main.w.getWidth()/9, y+p.convertYSmall(35), Main.w.getWidth()*(7.0/9.0), p.convertYSmall(30), "");
        username.setFont(p.font.calibri(p.getFontSizeSmall(25)));
        add(username);
        add(JLabel(Main.w.getWidth()/12, y+p.convertYSmall(80), "Password:", p.getFontSizeSmall(25)));
        JTextField password = JTextField(Main.w.getWidth()/9, y+p.convertYSmall(115), Main.w.getWidth()*(7.0/9.0), p.convertYSmall(30), "");
        password.setFont(p.font.calibri(p.getFontSizeSmall(25)));
        add(password);
        JLabel filetext = JLabel(Main.w.getWidth()/12, y+p.convertYSmall(160), "Filename:", p.getFontSizeSmall(25));
        filetext.setToolTipText("(Optional) This is the name of the file that will show up under the Vault Files directory");
        add(filetext);
        JTextField filename = JTextField(Main.w.getWidth()/9, y+p.convertYSmall(195), Main.w.getWidth()*(7.0/9.0), p.convertYSmall(30), "");
        filename.setFont(p.font.calibri(p.getFontSizeSmall(25)));
        add(filename);
        add(JCenterLabel(Main.w.getWidth()/2, Main.w.getHeight()-p.convertScreenY(30), "Press Enter to Create Account", p.getFontSizeSmall(25)));
//        JButton login = p.JButton(Main.w.getWidth()/3, y+p.convertYSmall(190), "Create Account", p.getFontSizeSmall(20));
//        login.setLocation(Main.w.getWidth()/2-login.getWidth()/2, y+p.convertYSmall(255));
//        login.addActionListener((ActionEvent ae) -> {
//            if (!username.getText().equals("") && !password.getText().equals("")){
//                Main.w.dispose();
//                p.delay(150);
//                Main.w = new Window("Password Vault by Antonio Kim - Create Account", 900, 600);
//                String file = filename.getText().trim();
//                if (file.equals("")){
//                    file = username.getText();
//                }
//                PrintWriter pr = p.printwriter("./Vault Files/"+file+".txt");
//                pr.print(Main.encrpytor.encrypt(username.getText()+"$p1l7"+password.getText()+"$p1l70$p1l7"));
//                pr.close();
//                Main.w.add(new Vault("./Vault Files/"+filename.getText()+".txt"));
//                Main.w.setVisible(true);
//            }
//            else{
//                p.JMessagePane("None of the above fields can be left blank", "Invalid Entry", JOptionPane.ERROR_MESSAGE);
//            }
//        });
//        add(login);
    }
    
}
