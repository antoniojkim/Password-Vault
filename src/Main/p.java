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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
            return new BufferedReader(new InputStreamReader(
                    new FileInputStream(path), "UTF-8"));
        } catch (UnsupportedEncodingException ex) {        } catch (FileNotFoundException ex) { }
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
    
}
