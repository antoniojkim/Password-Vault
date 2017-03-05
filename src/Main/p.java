/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package Main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

/**
*
* @author Antonio's Laptop
*/
public class p {
    
    public static  Color defaultcolor = UIManager.getColor ( "Panel.background" );
    public static  Border border = new LineBorder(Color.BLACK, 1);
    public final static List<String> letters = new ArrayList<>(Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"));
    public final static String[] symbols = {"", "!", "@", "#", "$", "%", "^", "*"};
    
//    public static void main (String[] args){
//        System.out.println(p.encrypt("true"));
//    }
    
    public static double random(double low, double high){
        return (high-low+1)*Math.random()+low;
    }
    public static int randomint(int low, int high){
        return (int)((high-low+1)*Math.random()+low);
    }
    
    //JComponents
    public static Border border (double size){
        return new LineBorder(Color.BLACK, (int)size);
    }
    public static JLabel JLabel (double xcoordinate, double ycoordinate, String text, int fontsize){
        JLabel label = new JLabel();
        AffineTransform affinetransform = new AffineTransform();
        FontRenderContext frc = new FontRenderContext(affinetransform,true,true);
        Font font = new Font("Calibri", Font.PLAIN, fontsize);
        int width = (int)(font.getStringBounds(text, frc).getWidth());
        int height = (int)(font.getStringBounds(text, frc).getHeight());
        label.setLocation((int)xcoordinate, (int)ycoordinate);
        label.setSize(width+5, height+5);
        label.setFont(font);
        label.setText(text);
        return label;
    }
    public static JButton JButton (double xcoordinate, double ycoordinate, String text, int fontsize){
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
    public static JTextField JTextField (double xcoordinate, double ycoordinate, String text){
        JTextField field = new JTextField();
        field.setLocation((int)xcoordinate, (int)ycoordinate);
        field.setSize(400, 30);
        field.setText(text);
        return field;
    }
    public static JTextField JTextField (double xcoordinate, double ycoordinate, double width, String text){
        JTextField field = new JTextField();
        field.setLocation((int)xcoordinate, (int)ycoordinate);
        field.setSize((int)width, 30);
        field.setText(text);
        return field;
    }
    public static JTextField JTextField (double xcoordinate, double ycoordinate, double width, int height, String text){
        JTextField field = new JTextField();
        field.setLocation((int)xcoordinate, (int)ycoordinate);
        field.setSize((int)width, height);
        field.setText(text);
        return field;
    }
    public static JPasswordField JPasswordField (double xcoordinate, double ycoordinate){
        JPasswordField field = new JPasswordField();
        field.setLocation((int)xcoordinate, (int)ycoordinate);
        field.setSize(400, 35);
        return field;
    }
    public static JPasswordField JPasswordField (double xcoordinate, double ycoordinate, double width, int height){
        JPasswordField field = new JPasswordField();
        field.setLocation((int)xcoordinate, (int)ycoordinate);
        field.setSize((int)width, height);
        return field;
    }
    public static JPasswordField JPasswordField (double xcoordinate, double ycoordinate, double width, int height, int fontsize, boolean edittable, boolean visible){
        JPasswordField field = new JPasswordField();
        field.setLocation((int)xcoordinate, (int)ycoordinate);
        field.setSize((int)width, height);
        field.setBackground(defaultcolor);
        field.setBorder(new LineBorder(Color.BLACK, 0));
        field.setFont(new Font("Calibri", Font.PLAIN, fontsize));
        field.setEditable(edittable);
        return field;
    }
    public static JTextArea JTextArea (double xcoordinate, double ycoordinate, int width, int height, int fontsize, boolean edittable, boolean visible){
        JTextArea area = new JTextArea();
        area.setLocation((int)xcoordinate, (int)ycoordinate);
        area.setSize(width, height);
        if (!visible){
            area.setBackground(defaultcolor);
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
    public static JScrollPane JScrollPane (double xcoordinate, double ycoordinate, int width, int height){
        JScrollPane pane = new JScrollPane();
        pane.setLocation((int)xcoordinate, (int)ycoordinate);
        pane.setSize(width, height);
        return pane;
    }
    public static void JMessagePane(String message, String header, int type){
        JOptionPane.showMessageDialog (null, message, header, type);
    }
    public static int showConfirmDialog(String message, String header, int mode, int type){
        return JOptionPane.showConfirmDialog(null, message, header, mode, type);
    }
    public static void drawImage(Graphics g, BufferedImage image, int x, int y, double scale){
        g.drawImage(image, x, y, (int)(image.getWidth()*scale), (int)(image.getHeight()*scale), null);
    }
    
    //String functions
    public static int stringWidth(String string, Font f){
        AffineTransform affinetransform = new AffineTransform();
        FontRenderContext frc = new FontRenderContext(affinetransform,true,true);
        Font font = f;
        int width = (int)(font.getStringBounds(string, frc).getWidth());
        return width;
    }
    public static int stringHeight(String string, Font f){
        AffineTransform affinetransform = new AffineTransform();
        FontRenderContext frc = new FontRenderContext(affinetransform,true,true);
        Font font = f;
        int height = (int)(font.getStringBounds(string, frc).getHeight());
        return height;
    }
    
    //fonts
    public static Fonts font = new Fonts();
    public static class Fonts{
        public Font calibri (int size){
            return new Font("calibri", Font.PLAIN, size);
        }
        public Font calibriBold (int size){
            return new Font("calibri", Font.BOLD, size);
        }
    }
    
    //Buffered Input/Output
    public static BufferedReader filereader(String path){
        try {
            return new BufferedReader (new FileReader (path));
        } catch (FileNotFoundException ex) {        }
        return null;
    }
    public static PrintWriter printwriter(String path){
        try {
            return new PrintWriter (new FileWriter (path));
        } catch (IOException ex) {        }
        return null;
    }
    
    //Delay
    public static void delay (double time){
        try{
            Thread.sleep((long)Math.floor(time), (int)((time-Math.floor(time))*1000000));
//            System.out.println("Delayed "+(long)Math.floor(time)+"Milliseconds and "+(int)((time-Math.floor(time))*100000)+" Nanoseconds");
        }catch(InterruptedException e){}
    }
    
    //Sort
    public static boolean sort(List<String> list){
        for (int a = 0; a<list.size(); a++){
            for (int b = a+1; b<list.size(); b++){
                if (list.get(b).compareTo(list.get(a)) < 0){
                    String switch1 = list.get(b);
                    list.set(b, list.get(a));
                    list.set(a, switch1);
                    return sort(list);
                }
            }
        }
        return true;
    }
    
    
    //ToolKit
    public static double getScreenWidth(){
        return Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    }
    public static double getScreenHeight(){
        return Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    }
    public static int getFontSizeSmall(double old){
        if (old == 0){
            return 0;
        }
        return (int)(Main.w.getWindowDiagonal()/(565.685424949238/old));
    }
    public static int getFontSize(double old){
        if (old == 0){
            return 0;
        }
        return (int)(Main.w.getWindowDiagonal()/(1140.175425099138/old));
    }
    public static double getImageScale (double old){
        if (old == 0){
            return 0;
        }
        return (Main.w.getWindowDiagonal()/(1140.175425099138/old));
    }
    public static int convertX(double old){
        if (old == 0){
            return 0;
        }
        return (int)(Main.w.getWidth()/(900.0/old));
    }
    public static int convertY(double old){
        if (old == 0){
            return 0;
        }
        return (int)(Main.w.getHeight()/(600.0/old));
    }
    public static int convertXSmall(double old){
        return (int)(Main.w.getHeight()/(400.0/old));
    }
    public static int convertYSmall(double old){
        return (int)(Main.w.getHeight()/(400.0/old));
    }
    public static int convertScreenX(double old){
        return (int)(getScreenWidth()/(1200.0/old));
    }
    public static int convertScreenY(double old){
        return (int)(getScreenHeight()/(700.0/old));
    }
    
}
