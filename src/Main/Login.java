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
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;

/**
*
* @author Antonio's Laptop
*/
public class Login extends GUI implements MouseListener{
    
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
        add(JCenterLabel(Main.w.getWidth()/2, Main.w.convertYSmall(10), "Password Vault", Main.w.getFontSizeSmall(40)));
        add(JLabel(Main.w.getWidth()/2-p.stringWidth("by Antonio Kim", p.font.calibri(Main.w.getFontSizeSmall(30)))/2, Main.w.convertYSmall(50), "by Antonio Kim", Main.w.getFontSizeSmall(30)));
        int y = Main.w.convertYSmall(110)-(Main.w.convertYSmall(400)-Main.w.getHeight());
        add(JLabel(Main.w.getWidth()/12, y, "Username:", Main.w.getFontSizeSmall(25)));
        username = JTextField(Main.w.getWidth()/9, y+Main.w.convertYSmall(35), Main.w.getWidth()*(7.0/9.0), Main.w.convertYSmall(30), "");
        username.setFont(p.font.calibri(Main.w.getFontSizeSmall(20)));
        add(username);
        add(JLabel(Main.w.getWidth()/12, y+Main.w.convertYSmall(80), "Password:", Main.w.getFontSizeSmall(25)));
        password = JPasswordField(Main.w.getWidth()/9, y+Main.w.convertYSmall(115), Main.w.getWidth()*(7.0/9.0), Main.w.convertYSmall(30));
        password.setFont(p.font.calibri(Main.w.getFontSizeSmall(20)));
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
        JButton login = JButton(Main.w.getWidth()/3, y+Main.w.convertYSmall(190), "Login", Main.w.getFontSizeSmall(25));
        login.setLocation(Main.w.getWidth()/2-login.getWidth()/2, y+Main.w.convertYSmall(190));
        readFiles();
        login.addActionListener((ActionEvent ae) -> {
            loginAction(username, password);
        });
        add(login);
        addListeners();
        JMenuItem item;
        popup.add(item = new JMenuItem("Create Account"));
        item.setHorizontalTextPosition(JMenu.RIGHT);
        item.setFont(p.font.calibri(Main.w.getFontSizeSmall(20)));
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                Main.w.dispose();
                p.delay(150);
                new CreateAccount(usernames).open();
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
            vault = new Vault(paths.get(usernames.indexOf(username.getText())));
            vault.open();
        }
        else{
            JMessagePane("Could not find either the username\nor the password in the database", "Incorrect username or password", JOptionPane.ERROR_MESSAGE);
            password.setText("");
        }
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
            if (!files[a].getName().equals("PDJ67O55lB95jRED7.txt") && !files[a].getName().equals("$6oØvE[=XW;{µR.txt") &&
                    readable(files[a].getPath())){
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
        } catch (IOException | NullPointerException ex) {    }
        return false;
    }
    
    
    private boolean hover = false;
    private int menuLocationX = Main.w.getWidth()-Main.w.convertScreenX(30), menuLocationY = Main.w.convertScreenY(12.5), 
            menuWidth = Main.w.convertScreenX(20), menuHeight = Main.w.convertScreenY(20), arcWidth = Main.w.getFontSize(5);
    @Override
    public void paintComponent (Graphics g){
        super.paintComponent(g);
        if (hover){
            g.setColor(Color.LIGHT_GRAY);
            g.fillRoundRect(menuLocationX-Main.w.convertX(2), menuLocationY-Main.w.convertY(2), menuWidth, menuHeight, arcWidth, arcWidth);
        }
        g.setColor(Color.DARK_GRAY);
        g.setFont(p.font.calibri(Main.w.getFontSize(70)));
        g.drawString("≡", menuLocationX, menuLocationY+menuHeight-Main.w.convertScreenY(2));
    }
    
    
    public void addListeners(){
        addMouseListener (this);
        addMouseMotionListener(new MouseMotionAdapter(){
            @Override
            public void mouseMoved(MouseEvent me) {
                int mx = me.getX(), my = me.getY();
                if (mx > menuLocationX && mx < menuLocationX+menuWidth &&
                        my > menuLocationY && my < menuLocationY+menuHeight){
                    hover = true;
                }
                else{
                    hover = false;
                }
                repaint();
            }
        });
    }
    
    @Override
    public void mouseClicked(MouseEvent me) {    }
    
    @Override
    public void mousePressed(MouseEvent me) {
        int mx = me.getX(), my = me.getY();
        if (hover){
            popup.show(this, menuLocationX-Main.w.convertXSmall(130), menuLocationY+Main.w.convertYSmall(10));
        }
    }
    
    @Override
    public void mouseReleased(MouseEvent me) {    }
    
    @Override
    public void mouseEntered(MouseEvent me) {    }
    
    @Override
    public void mouseExited(MouseEvent me) {    }
    
    
}
