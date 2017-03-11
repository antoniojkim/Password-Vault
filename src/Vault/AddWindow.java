/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package Vault;

import Main.GUI;
import Main.Main;
import Main.Window;
import Main.p;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**

@author Antonio
*/
public class AddWindow extends GUI{
    
    private Entry entry;
    private JLabel enter;
    private Window w;
    
    public AddWindow (){
        w = new Window("Password Vault by Antonio Kim - Add Entry", 350, 135);
        addContent();
        addEntry();
    }
    
    public AddWindow (Entry entry, boolean addingStandard){ // adding standard means adding username and password
        this.entry = entry;
        if (addingStandard){
            w = new Window("Password Vault by Antonio Kim - Add Username and Password", 350, 250);
            addContent();
            addStandard();
        }
        else{
            w = new Window("Password Vault by Antonio Kim - Add Field", 350, 250);
            addContent();
            addRegular();
        }
    }
    
    public void open(){
        w.add(this);
        w.setVisible(true);
    }
    
    private void addContent(){
        w.setLocationRelativeTo(null);
        w.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        w.setResizable(false);
        setLayout(null);
    }
    
    private void addEntry(){
        if (Main.login.vault.editting == true){
            Main.login.vault.toggleEditting();
        }
        add(JLabel(w.getWidth()/20, w.getHeight()/15, "Enter the name of the entry:", w.getFontSize(65)));
        JTextField name = JTextField(w.getWidth()/15, w.getHeight()*5/15, (int)(w.getWidth()*13.0/15.0), w.getHeight()/3, "");
        name.setFont(p.font.calibri(w.getFontSize(55)));
        name.setSize(name.getWidth(), (int)(1.25*p.stringHeight("PLACE TEXT HERE", name.getFont())));
        name.addKeyListener(new KeyAdapter(){
            @Override
            public void keyReleased(KeyEvent ke){
                String nameText = name.getText().trim();
                if (nameText.equals("")){
                    enter.setText("");
                }
                else if (!nameText.equals("")){
                    if (ke.getKeyCode() == KeyEvent.VK_ENTER){
                        entry = new Entry(nameText);
                        Main.login.vault.addEntry(entry);
                        Main.login.vault.print();
                        Main.login.vault.titleArea.setText(nameText);
                        Main.login.vault.updateModel();
                        //Main.login.vault.updateIndexDetails();
                        w.dispose();
                    }
                    else{
                        enter.setText("Press Enter to Continue");
                    }
                }
            }
        });
        add(name);
        add(enter = JCenterLabel(w.getWidth()/2, w.getHeight()-w.convertScreenY(45), "Press Enter to Continue", w.getFontSize(55)));
        enter.setText("");
    }
    
    public void addStandard(){
        add(JLabel(w.getWidth()/20, w.getHeight()/15, "Username:", w.getFontSize(60)));
        JTextField username = JTextField(w.getWidth()/15, (int)(w.getHeight()*3/15), (int)(w.getWidth()*9.0/10.0), w.getHeight()/5, "");
        username.setFont(p.font.calibri(w.getFontSize(55)));
        username.setSize(username.getWidth(), (int)(1.25*p.stringHeight("PLACE TEXT HERE", username.getFont())));
        add(username);
        add(JLabel(w.getWidth()/20, w.getHeight()*5.5/15, "Password:", w.getFontSize(60)));
        JTextField password = JTextField(w.getWidth()/15, (int)(w.getHeight()*7.5/15), (int)(w.getWidth()*9.0/10.0), w.getHeight()/5, "");
        password.setFont(p.font.calibri(w.getFontSize(55)));
        password.setSize(password.getWidth(), (int)(1.25*p.stringHeight("PLACE TEXT HERE", password.getFont())));
        add(password);
        KeyAdapter key = new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent ke){
                String usernameText = username.getText().trim(), passwordText = password.getText().trim();
                if (usernameText.equals("") || passwordText.equals("")){
                    enter.setText("");
                }
                else if (!usernameText.equals("") && !passwordText.equals("")){
                    if (ke.getKeyCode() == KeyEvent.VK_ENTER){
                        entry.add("Username:", usernameText);
                        entry.add("Password:", passwordText);
                        Main.login.vault.print();
                        //            Main.login.vault.updateModel();
                        Main.login.vault.updateIndexDetails();
                        w.dispose();
                    }
                    else{
                        enter.setText("Press Enter to Continue");
                    }
                }
            }
        };
        username.addKeyListener(key);
        password.addKeyListener(key);
        add(enter = JCenterLabel(w.getWidth()/2, w.getHeight()-w.convertScreenY(45), "Press Enter to Continue", w.getFontSize(55)));
        enter.setText("");
    }
    
    public void addRegular(){
        add(JLabel(w.getWidth()/20, w.getHeight()/15, "Field Title:", w.getFontSize(60)));
        JTextField username = JTextField(w.getWidth()/15, (int)(w.getHeight()*3/15), (int)(w.getWidth()*9.0/10.0), w.getHeight()/5, "");
        username.setFont(p.font.calibri(w.getFontSize(55)));
        username.setSize(username.getWidth(), (int)(1.25*p.stringHeight("PLACE TEXT HERE", username.getFont())));
        add(username);
        add(JLabel(w.getWidth()/20, w.getHeight()*5.5/15, "Field Data:", w.getFontSize(60)));
        JTextField password = JTextField(w.getWidth()/15, (int)(w.getHeight()*7.5/15), (int)(w.getWidth()*9.0/10.0), w.getHeight()/5, "");
        password.setFont(p.font.calibri(w.getFontSize(55)));
        password.setSize(password.getWidth(), (int)(1.25*p.stringHeight("PLACE TEXT HERE", password.getFont())));
        add(password);
        KeyAdapter key = new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent ke){
                String usernameText = username.getText().trim(), passwordText = password.getText().trim();
                if (usernameText.equals("") || passwordText.equals("")){
                    enter.setText("");
                }
                else if (!usernameText.equals("") && !passwordText.equals("")){
                    if (ke.getKeyCode() == KeyEvent.VK_ENTER){
                        boolean another = false;
                        if (ke.isControlDown()){
                            another = true;
                        }
                        entry.add(usernameText, passwordText);
                        Main.login.vault.print();
                        //            Main.login.vault.updateModel();
                        Main.login.vault.updateIndexDetails();
                        if (another){
                            username.setText("");
                            password.setText("");
                            username.requestFocus();
                        }
                        else{
                            w.dispose();
                        }
                    }
                    else{
                        enter.setText("Press Enter to Continue");
                    }
                }
            }
        };
        username.addKeyListener(key);
        password.addKeyListener(key);
        add(enter = JCenterLabel(w.getWidth()/2, w.getHeight()-w.convertScreenY(45), "Press Enter to Continue", w.getFontSize(55)));
        enter.setText("");
    }
    
}
