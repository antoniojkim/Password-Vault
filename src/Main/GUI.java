/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package Main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

/**

@author Antonio
*/
public class GUI extends JPanel{
    
    
    public Border border (double size){
        return new LineBorder(Color.BLACK, (int)size);
    }
    public JLabel JCenterLabel(double xcoordinate, double ycoordinate, String text, int fontsize){
        return JLabel(xcoordinate, ycoordinate, text, fontsize, true);
    }
    public JLabel JLabel (double xcoordinate, double ycoordinate, String text, int fontsize){
        return JLabel(xcoordinate, ycoordinate, text, fontsize, false);
    }
    public JLabel JLabel (double xcoordinate, double ycoordinate, String text, int fontsize, boolean center){
        JLabel label = new JLabel();
        AffineTransform affinetransform = new AffineTransform();
        FontRenderContext frc = new FontRenderContext(affinetransform,true,true);
        Font font = new Font("Calibri", Font.PLAIN, fontsize);
        int width = (int)(font.getStringBounds(text, frc).getWidth());
        int height = (int)(font.getStringBounds(text, frc).getHeight());
        label.setSize(width+5, height+5);
        if (center){
            label.setLocation((int)xcoordinate-label.getWidth()/2, (int)ycoordinate);
        }
        else{
            label.setLocation((int)xcoordinate, (int)ycoordinate);
        }
        label.setFont(font);
        label.setText(text);
        return label;
    }
    public JButton JButton (double xcoordinate, double ycoordinate, String text, int fontsize){
        JButton button = new JButton();
        AffineTransform affinetransform = new AffineTransform();
        FontRenderContext frc = new FontRenderContext(affinetransform,true,true);
        Font font = new Font("Calibri", Font.PLAIN, fontsize);
        int width = (int)(font.getStringBounds(text, frc).getWidth());
        int height = (int)(font.getStringBounds(text, frc).getHeight());
        button.setBounds((int)xcoordinate, (int)ycoordinate, (int)(width*2.5), (int)(height*1.25));
        button.setFont(new Font("Calibri", Font.PLAIN, fontsize));
        button.setText(text);
        return button;
    }
    public JTextField JTextField (double xcoordinate, double ycoordinate, String text){
        JTextField field = new JTextField();
        field.setLocation((int)xcoordinate, (int)ycoordinate);
        field.setSize(400, 30);
        field.setText(text);
        return field;
    }
    public JTextField JTextField (double xcoordinate, double ycoordinate, double width, String text){
        JTextField field = new JTextField();
        field.setLocation((int)xcoordinate, (int)ycoordinate);
        field.setSize((int)width, 30);
        field.setText(text);
        return field;
    }
    public JTextField JTextField (double xcoordinate, double ycoordinate, double width, int height, String text){
        JTextField field = new JTextField();
        field.setLocation((int)xcoordinate, (int)ycoordinate);
        field.setSize((int)width, height);
        field.setText(text);
        return field;
    }
    public JPasswordField JPasswordField (double xcoordinate, double ycoordinate){
        JPasswordField field = new JPasswordField();
        field.setLocation((int)xcoordinate, (int)ycoordinate);
        field.setSize(400, 35);
        return field;
    }
    public JPasswordField JPasswordField (double xcoordinate, double ycoordinate, double width, int height){
        JPasswordField field = new JPasswordField();
        field.setLocation((int)xcoordinate, (int)ycoordinate);
        field.setSize((int)width, height);
        return field;
    }
    public JPasswordField JPasswordField (double xcoordinate, double ycoordinate, double width, int height, int fontsize, boolean edittable, boolean visible){
        JPasswordField field = new JPasswordField();
        field.setLocation((int)xcoordinate, (int)ycoordinate);
        field.setSize((int)width, height);
        field.setBackground(this.getBackground());
        field.setBorder(new LineBorder(Color.BLACK, 0));
        field.setFont(new Font("Calibri", Font.PLAIN, fontsize));
        field.setEditable(edittable);
        return field;
    }
    public JTextArea JTextArea (double xcoordinate, double ycoordinate, int width, int height, int fontsize, boolean edittable, boolean visible){
        JTextArea area = new JTextArea();
        area.setLocation((int)xcoordinate, (int)ycoordinate);
        area.setSize(width, height);
        if (!visible){
            area.setBackground(getBackground());
        }
        else{
            area.setBorder(new LineBorder(Color.BLACK, 1));
        }
        area.setFont(new Font("Calibri", Font.PLAIN, fontsize));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setEditable(edittable);
        area.setText("");
        return area;
    }
    public JScrollPane JScrollPane (double xcoordinate, double ycoordinate, int width, int height){
        JScrollPane pane = new JScrollPane();
        pane.setLocation((int)xcoordinate, (int)ycoordinate);
        pane.setSize(width, height);
        return pane;
    }
    public void JMessagePane(String message, String header, int type){
        JOptionPane.showMessageDialog (null, message, header, type);
    }
    public int showConfirmDialog(String message, String header, int mode, int type){
        return JOptionPane.showConfirmDialog(null, message, header, mode, type);
    }
    public void drawImage(Graphics g, BufferedImage image, int x, int y, double scale){
        g.drawImage(image, x, y, (int)(image.getWidth()*scale), (int)(image.getHeight()*scale), null);
    }
    
}
