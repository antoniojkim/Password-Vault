/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package Vault;

import Main.Main;
import Main.p;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

/**

@author Antonio
*/
public class AddWindow extends JFrame{
    
    Entry entry;
    JButton ok;
    JButton cancel;
    
    public AddWindow (){ // adding standard means adding username and password
        this.entry = entry;
        setSize(p.convertScreenX(350), p.convertScreenY(135));
        setLocationRelativeTo(Main.w);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setLayout(null);
        setTitle("Add Entry");
        addEntry();
    }
    public AddWindow (Entry entry, boolean addingStandard){ // adding standard means adding username and password
        this.entry = entry;
        setSize(p.convertScreenX(350), p.convertScreenY(200));
        setLocationRelativeTo(Main.w);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setLayout(null);
        if (addingStandard){
            setTitle("Add Username and Password");
            addStandard();
        }
        else{
            setTitle("Add Field");
            addRegular();
        }
    }
    
    public void addEntry(){
        if (Main.login.vault.editting == true){
            Main.login.vault.toggleEditting();
        }
        add(p.JLabel(getWidth()/20, getHeight()/15, "Enter the name of the entry:", p.getFontSizeSmall(10)));
        JTextField name = p.JTextField(getWidth()/15, getHeight()*4/15, (int)(getWidth()*9.0/10.0), getHeight()/5, "");
        name.setFont(p.font.calibri(p.getFontSizeSmall(9)));
        name.setSize(name.getWidth(), (int)(1.25*p.stringHeight("Anything", name.getFont())));
        name.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent ke){
                if (ke.getKeyCode() == KeyEvent.VK_ENTER){
                    addEntryAction(name.getText().trim());
                }
            }
        });
        add(name);
        addButtons();
        ok.addActionListener((ActionEvent) -> {
            addEntryAction(name.getText().trim());
        });
    }
    
    public void addEntryAction(String nameText){
        if (!nameText.equals("")){
            entry = new Entry(nameText);
            Main.login.vault.addEntry(entry);
            Main.login.vault.print();
            Main.login.vault.titleArea.setText(nameText);
            Main.login.vault.updateModel();
            Main.login.vault.updateIndexDetails();
            dispose();
        }
    }
    
    public void addStandard(){
        add(p.JLabel(getWidth()/20, getHeight()/15, "Username:", p.getFontSizeSmall(10)));
        JTextField username = p.JTextField(getWidth()/15, (int)(getHeight()*3/15), (int)(getWidth()*9.0/10.0), getHeight()/5, "");
        username.setFont(p.font.calibri(p.getFontSizeSmall(9)));
        username.setSize(username.getWidth(), (int)(1.25*p.stringHeight("Anything", username.getFont())));
        add(username);
        add(p.JLabel(getWidth()/20, getHeight()*5.5/15, "Password:", p.getFontSizeSmall(10)));
        JTextField password = p.JTextField(getWidth()/15, (int)(getHeight()*7.5/15), (int)(getWidth()*9.0/10.0), getHeight()/5, "");
        password.setFont(p.font.calibri(p.getFontSizeSmall(9)));
        password.setSize(password.getWidth(), (int)(1.25*p.stringHeight("Anything", password.getFont())));
        add(password);
        KeyAdapter key = new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent ke){
                if (ke.getKeyCode() == KeyEvent.VK_ENTER){
                    addStandardAction(username.getText().trim(), password.getText().trim());
                }
            }
        };
        username.addKeyListener(key);
        password.addKeyListener(key);
        addButtons();
        ok.addActionListener((ActionEvent) -> {
            addStandardAction(username.getText().trim(), password.getText().trim());
        });
    }
    
    public void addStandardAction(String username, String password){
        if (!username.equals("") && !password.equals("")){
            entry.add("Username:", username);
            entry.add("Password:", password);
            Main.login.vault.print();
//            Main.login.vault.updateModel();
            Main.login.vault.updateIndexDetails();
            dispose();
        }
    }
    
    public void addRegular(){
        add(p.JLabel(getWidth()/20, getHeight()/15, "Field Title:", p.getFontSizeSmall(10)));
        JTextField username = p.JTextField(getWidth()/15, (int)(getHeight()*3/15), (int)(getWidth()*9.0/10.0), getHeight()/5, "");
        username.setFont(p.font.calibri(p.getFontSizeSmall(9)));
        username.setSize(username.getWidth(), (int)(1.25*p.stringHeight("Anything", username.getFont())));
        add(username);
        add(p.JLabel(getWidth()/20, getHeight()*5.5/15, "Field Data:", p.getFontSizeSmall(10)));
        JTextField password = p.JTextField(getWidth()/15, (int)(getHeight()*7.5/15), (int)(getWidth()*9.0/10.0), getHeight()/5, "");
        password.setFont(p.font.calibri(p.getFontSizeSmall(9)));
        password.setSize(password.getWidth(), (int)(1.25*p.stringHeight("Anything", password.getFont())));
        add(password);
        KeyAdapter key = new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent ke){
                if (ke.getKeyCode() == KeyEvent.VK_ENTER){
                    boolean another = false;
                    if (ke.isControlDown()){
                        another = true;
                    }
                    addRegularAction(username, password, another);
                }
            }
        };
        username.addKeyListener(key);
        password.addKeyListener(key);
        addButtons();
        ok.addActionListener((ActionEvent) -> {
            addRegularAction(username, password, false);
        });
    }
    
    public void addRegularAction(JTextField title, JTextField data, boolean another){
        if (!title.equals("")){
            entry.add(title.getText().trim(), data.getText().trim());
            Main.login.vault.print();
//            Main.login.vault.updateModel();
            Main.login.vault.updateIndexDetails();
            if (another){
                title.setText("");
                data.setText("");
                title.requestFocus();
            }
            else{
                dispose();
            }
        }
    }
    
    public void addButtons(){
        int fontSize = p.getFontSizeSmall(10);
        int width = (int)(1.25*p.stringWidth("Cancel", p.font.calibri(fontSize)));
        double spacing = (getWidth()-2*width-getWidth()/15.0)/2.0;
        ok = p.JButton(100, 100, "Ok", fontSize);
        ok.setSize(width, ok.getHeight());
        ok.setLocation((int)spacing, getHeight()-(int)(2*ok.getHeight()));
        add(ok);
        cancel = p.JButton(100, 100, "Cancel", fontSize);
        cancel.setSize(width, cancel.getHeight());
        cancel.setLocation((int)(spacing+width+getWidth()/15.0), getHeight()-(int)(2*cancel.getHeight()));
        add(cancel);
        cancel.addActionListener((ActionEvent e) -> {
            dispose();
        });
    }
    
}
