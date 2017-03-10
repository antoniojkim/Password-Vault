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
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.io.PrintWriter;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JFrame;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;

/**

@author Antonio
*/
public class Settings extends GUI{
    
    private Vault vault;
    private Window w;
    private Button defaultFavourite, defaultUsername, resetKey, defaultKey;
    
    
    /*
    public static void main (String[] args){
        String path = "./Vault Files/asdf.txt";
        if (!new File(path).exists()){
            PrintWriter pr = p.printwriter(path);
            pr.print(Main.encrpytor.encrypt("asdf$p1l7password$p1l7false$p1l70$p1l7"));
            pr.close();
        }
        new Settings(new Vault(path)).Open();
    }
    /*
    */
    
    public Settings(Vault vault){
        this.vault = vault;
    }
    
    public void Open(){
        w = new Window("Password Vault by Antonio Kim - Settings", 500, 500);
        w.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);
        addContent();
        w.add(this);
        w.setVisible(true);
    }
    
    private void addContent(){
        int fontSize40 = (int)(w.getWindowDiagonal()/(1140.175425099138/40));
        int fontSize45 = (int)(w.getWindowDiagonal()/(1140.175425099138/45));
        int fontSize60 = (int)(w.getWindowDiagonal()/(1140.175425099138/60));
        add(JLabel(w.getWidth()/2-p.stringWidth("Settings", p.font.calibri(fontSize60))/2, p.convertScreenY(5), "Settings", fontSize60));
        add(JLabel(p.convertScreenX(20), p.convertScreenY(70), "Username:     "+vault.getUsername(),fontSize45));
        add(JLabel(p.convertScreenX(20), p.convertScreenY(120), "Password:", fontSize45));
        JPasswordField field = JPasswordField(p.convertScreenX(141), p.convertScreenY(125), p.convertScreenX(200), p.convertScreenY(20), fontSize45, false, false);
        field.setText(vault.getPasssword());
        field.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent me){
                field.setEchoChar((char)0);
            }
            @Override
            public void mouseExited(MouseEvent me){
                field.setEchoChar('•');
            }
        });
        add(field);
        
        add(JLabel(p.convertScreenX(50), p.convertScreenY(165), "New Password:", fontSize40));
        add(JLabel(p.convertScreenX(238), p.convertScreenY(166), "_____________________", fontSize40));
        JPasswordField newPassword = JPasswordField(p.convertScreenX(240), p.convertScreenY(166), p.convertScreenX(200), p.convertScreenY(20), fontSize45, true, true);
        newPassword.setText("");
        newPassword.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent me){
                newPassword.setEchoChar((char)0);
            }
            @Override
            public void mouseExited(MouseEvent me){
                newPassword.setEchoChar('•');
            }
        });
        add(newPassword);
        add(JLabel(p.convertScreenX(50), p.convertScreenY(205), "Confirm Password:", fontSize40));
        add(JLabel(p.convertScreenX(238), p.convertScreenY(206), "_____________________", fontSize40));
        JPasswordField confirmPassword = JPasswordField(p.convertScreenX(240), p.convertScreenY(206), p.convertScreenX(200), p.convertScreenY(20), fontSize45, true, true);
        confirmPassword.setText("");
        confirmPassword.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent me){
                confirmPassword.setEchoChar((char)0);
            }
            @Override
            public void mouseExited(MouseEvent me){
                confirmPassword.setEchoChar('•');
            }
        });
        add(confirmPassword);
        JTextArea instructions = JTextArea(p.convertScreenX(240), p.convertScreenY(240), p.convertScreenX(300), p.convertScreenY(20), fontSize40, true, false);
        instructions.setText("");
        add(instructions);
        KeyAdapter key = new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent ke){
                String newString = newPassword.getText();
                String confirmString = confirmPassword.getText();
                if (!newString.equals("") && !confirmString.equals("")){
                    if (newString.equals(confirmString)){
                        if (ke.getKeyCode() == KeyEvent.VK_ENTER){
                            vault.setPassword(newString);
                            vault.print();
                            newPassword.setText("");
                            confirmPassword.setText("");
                            instructions.setText("Saved!");
                            Timer timer = new Timer();
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    instructions.setText("");
                                }
                            }, 1250);
                        }
                        else{
                            instructions.setText("Press Enter to Continue");
                        }
                    }
                    else{
                        instructions.setText("Fields do not Match");
                    }
                }
                else{
                    instructions.setText("");
                }
            }
        };
        newPassword.addKeyListener(key);
        confirmPassword.addKeyListener(key);
        
        defaultFavourite = new Button(20, 295, 20, "Set Favourite as Default Tab", 40);
        if (vault.getdefaultShowFavourite()){
            defaultFavourite.setText("Unset Favourite as Default Tab");
        }
        defaultUsername = new Button(20, 340, 20, "Set this User as Default User", 40);
        resetKey = new Button(20, 385, 20, "Reset Encryption Key", 40);
        defaultKey = new Button(20, 430, 20, "Revert to Default Encryption Key", 40);
        repaint();
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me){
                int mx = me.getX(), my = me.getY();
                defaultFavourite.press(mx, my);
                defaultUsername.press(mx, my);
                resetKey.press(mx, my);
                defaultKey.press(mx, my);
                repaint();
            }
            @Override
            public void mouseReleased(MouseEvent me){
                if (defaultFavourite.isPressed()){
                    if (defaultFavourite.getText().equals("Unset Favourite as Default Tab")){
                        vault.setdefaultShowFavourite(false);
                        vault.print();
                        defaultFavourite.setText("Set Favourite as Default Tab");
                    }
                    else{
                        vault.setdefaultShowFavourite(true);
                        vault.print();
                        defaultFavourite.setText("Unset Favourite as Default Tab");
                    }
                    defaultFavourite.release();
                }
                else if (defaultUsername.isPressed()){
                    PrintWriter pr = p.printwriter("./Vault Files/PDJ67O55lB95jRED7.txt");
                    pr.print(Main.encrpytor.encrypt(vault.getUsername()+"$p1l7$p1l7$p1l7"));
                    pr.close();
                    defaultUsername.release();
                }
                else if (resetKey.isPressed()){
                    Main.encrpytor.reset();
                    resetKey.release();
                }
                else if (defaultKey.isPressed()){
                    Main.encrpytor.resetDefault();
                    defaultKey.release();
                }
                repaint();
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent me){
                int mx = me.getX(), my = me.getY();
                defaultFavourite.hover(mx, my);
                defaultUsername.hover(mx, my);
                resetKey.hover(mx, my);
                defaultKey.hover(mx, my);
                repaint();
            }
        });
    }
    
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        if (defaultFavourite != null){
            defaultFavourite.draw(g);
        }
        if (defaultUsername != null){
            defaultUsername.draw(g);
        }
        if (resetKey != null){
            resetKey.draw(g);
        }
        if (defaultKey != null){
            defaultKey.draw(g);
        }
    }
    
    private class Button{
        
        private int x, y, width, height;
        private String text;
        private Font font;
        private boolean hovering, pressed;
        private boolean visible = true;
        
        public Button(double x, double y, double height, String text, double fontSize){
            this.x = p.convertScreenX(x);
            this.y = p.convertScreenY(y);
            font = p.font.calibri((int)(w.getWindowDiagonal()/(1140.175425099138/fontSize)));
            this.text = text;
            this.width = (int)Math.max(w.getWidth()*9/10, p.stringWidth(text, font))+p.convertScreenX(10);
            this.height = (int)Math.max(p.convertScreenY(height), p.stringHeight(text, font))+p.convertScreenY(5);
        }
        public Button(double x, double y, double width, double height, String text, double fontSize){
            this.x = p.convertScreenX(x);
            this.y = p.convertScreenY(y);
            font = p.font.calibri((int)(w.getWindowDiagonal()/(1140.175425099138/fontSize)));
            this.text = text;
            this.width = (int)Math.max(p.convertScreenX(width), p.stringWidth(text, font))+p.convertScreenX(10);
            this.height = (int)Math.max(p.convertScreenY(height), p.stringHeight(text, font))+p.convertScreenY(5);
        }
        
        public boolean hover(int mx, int my){
            if (visible){
                if (mx > x && mx < x+width && my > y && my < y+height){
                    hovering = true;
                }
                else{
                    hovering = false;
                }
            }
            return hovering;
        }
        public void press(int mx, int my){
            if (visible){
                if (!pressed && mx > x && mx < x+width && my > y && my < y+height){
                    pressed = true;
                }
                else{
                    pressed = false;
                }
            }
        }
        public void release(){
            pressed = false;
        }
        public String getText(){
            return text;
        }
        public void setText(String text){
            this.text = text;
        }
        
        public boolean isPressed(){
            return pressed;
        }
        
        public void draw(Graphics graphic){
            if (visible){
                Graphics2D g = (Graphics2D)graphic;
                g.setStroke(new BasicStroke(p.getFontSize(3)));
                if (hovering){
                    if (pressed){
                        g.setColor(Color.GRAY);
                        g.fillRect(x, y, width, height);
                    }
                    else{
                        g.setColor(Color.LIGHT_GRAY);
                        g.fillRect(x, y, width, height);
                    }
                }
                g.setColor(Color.BLACK);
                g.drawRect(x, y, width, height);
                g.setFont(font);
                g.drawString(text, (x+width)/2-p.stringWidth(text, font)/2, (y+height)-p.stringHeight(text, font)/3);
            }
        }
        
    }
    
}
