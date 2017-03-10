/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package Vault;

import Main.GUI;
import Main.p;
import Main.Main;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;

/**
*
* @author Antonio's Laptop
*/
public class DetailsPane extends GUI implements KeyListener{
    
    boolean editting = false;
    Entry entry = new Entry("");
    List<JTextArea> titles = new ArrayList<>();
    List<JPasswordField> data = new ArrayList<>();
    List<Boolean> removeHover = new ArrayList<>();
    List<Boolean> removePressed = new ArrayList<>();
    
    /*
    public static void main (String[] args){
    p.JFrame(900, 700, new Vault("./Vault Files/asdf.txt")).setVisible(true);
    }
    /*
    */
    
    public DetailsPane(){
        setLayout(null);
        Mouse mouse = new Mouse();
        addMouseMotionListener(mouse);
        addMouseListener(mouse);
        addKeyListener(this);
    }
    public DetailsPane(Entry entry){
        setLayout(null);
        this.entry.copy(entry);
        Mouse mouse = new Mouse();
        addMouseMotionListener(mouse);
        addMouseListener(mouse);
        addKeyListener(this);
    }
    
    public void setEditting(boolean editting){
        this.editting = editting;
        for (int a = 0; a<titles.size(); a++){
            titles.get(a).setEditable(editting);
            data.get(a).setEditable(editting);
            if (!editting){
                entry.fieldTitles.set(a, titles.get(a).getText());
                entry.fieldData.set(a, data.get(a).getText());
            }
        }
        if (editting){
            requestFocusInWindow();
        }
        repaint();
    }
    
    public void switchEntry(Entry entry){
        titles.clear();
        data.clear();
        removeHover.clear();
        removePressed.clear();
        removeAll();
        if (entry != null){
            this.entry.copy(entry);
            int highest = 0;
            for (int a = 0; a<entry.size(); a++){
                int width = (int)(1.15*p.stringWidth(entry.fieldTitles.get(a), p.font.calibri(p.getFontSize(25))));
                if (width > highest){
                    highest = width;
                }
                width = p.convertX(40)+(int)(1.15*p.stringWidth(entry.fieldData.get(a), p.font.calibri(p.getFontSize(25))));
                if (width > highest){
                    highest = width;
                }
            }
            setPreferredSize(new Dimension(Math.max(highest, getWidth())+p.convertX(5), p.convertY(30)+entry.size()*p.convertY(90)));
            for (int a = 0; a<entry.size(); a++){
                titles.add(JTextArea(p.convertX(10), p.convertY(5)+a*p.convertY(90), p.convertX(395), p.convertY(30), p.getFontSize(25), editting, false));
                titles.get(a).setText(entry.fieldTitles.get(a));
                titles.get(a).setSize(Math.max((int)(1.15*p.stringWidth(titles.get(a).getText(), titles.get(a).getFont())), p.convertX(395)), titles.get(a).getHeight());
                if (titles.get(a).getWidth() > highest){
                    highest = titles.get(a).getWidth();
                }
                add(titles.get(a));
                titles.get(a).addKeyListener(this);
                data.add(JPasswordField(p.convertX(40), p.convertY(40)+a*p.convertY(90), p.convertX(395), p.convertY(30), p.getFontSize(25), editting, false));
                data.get(a).setText(entry.fieldData.get(a));
                data.get(a).setSize(Math.max((int)(1.15*p.stringWidth(data.get(a).getText(), data.get(a).getFont())), p.convertX(395)), data.get(a).getHeight());
                if (data.get(a).getWidth() > highest){
                    highest = data.get(a).getWidth();
                }
                add(data.get(a));
                data.get(a).addKeyListener(this);
                final int A = a;
                data.get(a).addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent me){
                        data.get(A).setEchoChar((char)0);
                    }
                    @Override
                    public void mouseExited(MouseEvent me){
                        data.get(A).setEchoChar('•');
                    }
                    @Override
                    public void mouseReleased(MouseEvent me){
                            StringSelection stringSelection = new StringSelection(data.get(A).getText());
                            Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
                            clpbrd.setContents(stringSelection, null);
                    }
                });
                data.get(a).addKeyListener(new KeyAdapter(){
                    @Override
                    public void keyPressed(KeyEvent ke){
                        try{
                            if (ke.isControlDown() && ke.getKeyCode() == KeyEvent.VK_C && (data.get(A).getSelectedText().equals(""))){
                                StringSelection stringSelection = new StringSelection(data.get(A).getText());
                                Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
                                clpbrd.setContents(stringSelection, null);
                            }
                        }catch(NullPointerException e){
                            StringSelection stringSelection = new StringSelection(data.get(A).getText());
                            Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
                            clpbrd.setContents(stringSelection, null);
                        }
                    }
                });
                removeHover.add(false);
                removePressed.add(false);
            }
        }
    }
    
    public void mouseDrag(int x, int y){
        JViewport viewPort = (JViewport) SwingUtilities.getAncestorOfClass(JViewport.class, this);
        if (viewPort != null) {
            Rectangle view = viewPort.getViewRect();
            view.x += x;
            view.y += y;
            scrollRectToVisible(view);
        }
    }
    public void mouseDragToDefault(){
        JViewport viewPort = (JViewport) SwingUtilities.getAncestorOfClass(JViewport.class, this);
        if (viewPort != null) {
            Rectangle view = viewPort.getViewRect();
            view.x = 0;
            view.y = 0;
            scrollRectToVisible(view);
        }
    }
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        if (editting){
            for (int a = 0; a<entry.size(); a++){
                if (removeHover.get(a) == true){
                    if (removePressed.get(a) == false){
                        g.setColor(Color.LIGHT_GRAY);
                    }
                    else{
                        g.setColor(Color.GRAY);
                    }
                    g.fillRoundRect(p.convertX(408), p.convertY(3)+a*p.convertY(90), p.convertX(34), p.convertY(35), p.getFontSize(20), p.getFontSize(20));
                }
                drawImage(g, Main.images.remove, p.convertX(405), a*p.convertY(90), p.getImageScale(0.32));
            }
        }
    }
    
    @Override
    public void keyTyped(KeyEvent e) { }
    
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_S && Main.login.vault.editting){
            Main.login.vault.toggleEditting();
        }
    }
    
    @Override
    public void keyReleased(KeyEvent e) {  }
    
    private class Mouse extends MouseInputAdapter{
        
        private Point origin;
        
        @Override
        public void mouseDragged(MouseEvent me) {
            mouseDrag(origin.x - me.getX(), origin.y - me.getY());
        }
        
        @Override
        public void mouseMoved(MouseEvent me) {
            int mx = me.getX(), my = me.getY();
            for (int a = 0; a<removeHover.size(); a++){
                if (mx > p.convertX(408) && mx < p.convertX(442) && my > p.convertY(3)+a*p.convertY(90) && my < p.convertY(38)+a*p.convertY(90)){
                    removeHover.set(a, true);
                }
                else{
                    removeHover.set(a, false);
                }
            }
            repaint();
        }
        
        @Override
        public void mousePressed (MouseEvent me){
            origin = me.getPoint();
            int mx = me.getX(), my = me.getY();
            for (int a = 0; a<removeHover.size(); a++){
                removePressed.set(a, removeHover.get(a));
            }
            repaint();
        }
        
        @Override
        public void mouseReleased (MouseEvent me){
            for (int a = 0; a<removePressed.size(); a++){
                if (removePressed.get(a) == true){
                    removePressed.set(a, false);
                    entry.remove(a);
                    switchEntry(new Entry(entry));
                    final int num = a;
                    javax.swing.SwingUtilities.invokeLater(() -> {
                        Main.login.vault.dataPane.getVerticalScrollBar().setValue(num);
                    });
                }
            }
            repaint();
        }
    }
    
    
}
