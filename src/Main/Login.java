/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package Main;

import Vault.Vault;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;

/**
*
* @author Antonio's Laptop
*/
public class Login extends JPanel implements MouseListener{
    
    private List<String> paths = new ArrayList<>();
    private List<String> usernames = new ArrayList<>();
    private List<String> passwords = new ArrayList<>();
    private boolean traversed = false;
    protected JPopupMenu popup = new JPopupMenu();
    public JTextField username;
    private JPasswordField password;
    public Vault vault;
    
    
    public Login(){
        setLayout(null);
        //setBackground(new Color(00, 66, 99));
        add(p.JLabel(Main.w.getWidth()/2-p.stringWidth("Password Vault", p.font.calibri(p.getFontSizeSmall(40)))/2, p.convertYSmall(10), "Password Vault", p.getFontSizeSmall(40)));
        add(p.JLabel(Main.w.getWidth()/2-p.stringWidth("by Antonio Kim", p.font.calibri(p.getFontSizeSmall(30)))/2, p.convertYSmall(50), "by Antonio Kim", p.getFontSizeSmall(30)));
        int y = p.convertYSmall(110)-(p.convertYSmall(400)-Main.w.getHeight());
        add(p.JLabel(Main.w.getWidth()/12, y, "Username:", p.getFontSizeSmall(25)));
        username = p.JTextField(Main.w.getWidth()/9, y+p.convertYSmall(35), Main.w.getWidth()*(7.0/9.0), p.convertYSmall(30), "");
        username.setFont(p.font.calibri(p.getFontSizeSmall(20)));
        add(username);
        add(p.JLabel(Main.w.getWidth()/12, y+p.convertYSmall(80), "Password:", p.getFontSizeSmall(25)));
        password = p.JPasswordField(Main.w.getWidth()/9, y+p.convertYSmall(115), Main.w.getWidth()*(7.0/9.0), p.convertYSmall(30));
        password.setFont(p.font.calibri(p.getFontSizeSmall(20)));
        username.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed (KeyEvent ke){
                if (ke.getKeyCode() == KeyEvent.VK_ENTER || ke.getKeyCode() == KeyEvent.VK_DOWN){
                    password.requestFocus();
                }
            }
        });
        password.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed (KeyEvent ke){
                if (ke.getKeyCode() == KeyEvent.VK_ENTER){
                    loginAction(username, password);
                }
            }
        });
        add(password);
        JButton login = p.JButton(Main.w.getWidth()/3, y+p.convertYSmall(190), "Login", p.getFontSizeSmall(25));
        login.setLocation(Main.w.getWidth()/2-login.getWidth()/2, y+p.convertYSmall(190));
        readFiles();
        login.addActionListener((ActionEvent ae) -> {
            loginAction(username, password);
        });
        add(login);
        addListeners();
        JMenuItem item;
        popup.add(item = new JMenuItem("Create Account"));
        item.setHorizontalTextPosition(JMenu.RIGHT);
        item.setFont(p.font.calibri(p.getFontSizeSmall(20)));
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                Main.w.dispose();
                p.delay(150);
                Main.w = new Window("Password Vault by Antonio Kim - Create Account", 400, 450);
                Main.w.add(new CreateAccount(usernames));
                Main.w.setVisible(true);
            }
        });
//        readSettings();
    }
    
    public void readSettings(){
        String path = "./Vault Files/PDJ67O55lB95jRED7.txt";
        File settings = new File(path);
        if (settings.exists()){
            BufferedReader br = p.filereader(path);
            try {
                String line = br.readLine();
                if (line != null){
                    line = Main.encrpytor.decrypt(line);
                    if (!line.equals("Failed to Decrypt")){
                        String[] lines = line.split("\\$p1l7");
                        if (lines.length >= 1){
                            if (!lines[0].trim().equals("")){
                                username.setText(lines[0].trim());
                                password.setText("");
                            }
                        }
                    }
                }
            } catch (IOException ex) {            }
        }
        else{
            PrintWriter pr = p.printwriter(path);
            pr.print(Main.encrpytor.encrypt("$p1l7$p1l7$p1l7"));
            pr.close();
        }
    }
    
    public void loginAction(JTextField username, JPasswordField password){
        if (!usernames.isEmpty() && userExists(username.getText(), password.getText())){
            Main.w.dispose();
            p.delay(150);
            Main.w = new Window("Password Vault by Antonio Kim - Login", 900, 600);
            vault = new Vault(paths.get(usernames.indexOf(username.getText())));
            Main.w.add(vault);
            Main.w.setVisible(true);
        }
        else{
            p.JMessagePane("Could not find either the username\nor the password in the database", "Incorrect username or password", JOptionPane.ERROR_MESSAGE);
            password.setText("");
        }
    }
    
    public void addListeners(){
        addMouseListener (this);
        addMouseMotionListener(new MouseMotionAdapter(){
            @Override
            public void mouseMoved(MouseEvent me) {
                int mx = me.getX(), my = me.getY();
                if (mx > dotLocationX-p.convertXSmall(2) && mx < dotLocationX+p.convertXSmall(3)+dotDiameter && my > dotLocationY-p.convertYSmall(2) && my < dotLocationY+3*dotDiameter+p.convertXSmall(3)){
                    hover = true;
                }
                else{
                    hover = false;
                }
                repaint();
            }
        });
    }
    
    public boolean userExists(String username, String password){
        int index = usernames.indexOf(username);
        if (index != -1){
            if (passwords.get(index).equals(password)){
                return true;
            }
        }
        return false;
    }
    
    public void readFiles(){
        File[] files = new File("./Vault Files").listFiles();
        for (int a = 0; a<files.length; a++){
            if (!files[a].getName().equals("PDJ67O55lB95jRED7.txt") && readable(files[a].getPath())){
                BufferedReader br = p.filereader(files[a].getPath());
                try {
                    String line = br.readLine();
                    if (line != null){
                        line = Main.encrpytor.decrypt(line);
                        if (!line.equals("Failed to Decrypt")){
                            String[] entries = line.split("\\$p1l7");
                            usernames.add(entries[0]);
                            passwords.add(entries[1]);
                            paths.add(files[a].getPath());
                        }
                    }
                } catch (IOException ex) { }
            }
//            else{
//                System.out.println("Not readable");
//                System.out.println(files[a].getPath());
//            }
        }
    }
    
    public boolean readable(String path){
        BufferedReader br = p.filereader(path);
        String line = "";
        try {
            line = br.readLine();
            if (line != null && !line.equals("Failed to Decrypt")){
                return true;
            }
        } catch (IOException ex) {    }
        return false;
    }
    
    
    boolean hover = false;
    int dotDiameter = p.getFontSizeSmall(7), dotLocationX = Main.w.getWidth()-p.convertXSmall(22), dotLocationY = p.convertYSmall(5), dotDistance = p.convertYSmall(9), arcWidth = p.getFontSizeSmall(5);
    @Override
    public void paintComponent (Graphics g){
        super.paintComponent(g);
        if (hover){
            g.setColor(Color.LIGHT_GRAY);
            g.fillRoundRect(dotLocationX-p.convertXSmall(2), dotLocationY-p.convertYSmall(2), dotDiameter+p.convertXSmall(5), p.convertYSmall(30), arcWidth, arcWidth);
        }
        g.setColor(Color.DARK_GRAY);
        g.fillOval(dotLocationX, dotLocationY, dotDiameter, dotDiameter);
        g.fillOval(dotLocationX, dotLocationY+dotDistance, dotDiameter, dotDiameter);
        g.fillOval(dotLocationX, dotLocationY+2*dotDistance, dotDiameter, dotDiameter);
    }
    
    @Override
    public void mouseClicked(MouseEvent me) {    }
    
    @Override
    public void mousePressed(MouseEvent me) {
        int mx = me.getX(), my = me.getY();
        if (mx > dotLocationX-p.convertXSmall(2) && mx < dotLocationX+p.convertXSmall(3)+dotDiameter && my > dotLocationY-p.convertYSmall(2) && my < dotLocationY+3*dotDiameter+2*dotDistance+p.convertXSmall(3)){
            popup.show(this, dotLocationX-p.convertXSmall(130), dotLocationY+p.convertYSmall(10));
        }
    }
    
    @Override
    public void mouseReleased(MouseEvent me) {    }
    
    @Override
    public void mouseEntered(MouseEvent me) {    }
    
    @Override
    public void mouseExited(MouseEvent me) {    }
    
    
}