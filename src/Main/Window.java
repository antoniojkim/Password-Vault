package Main;


import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class Window extends JFrame{
    
    public int width = p.convertScreenX(400), height = p.convertScreenY(400);
    
    public Window(){
        super("Password Vault by Antonio Kim");
        setLayout(new BorderLayout());
        setSize(width, height);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        addWindowListener(new java.awt.event.WindowAdapter() {
//            @Override
//            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
//                System.exit(0);
//            }
//        });
        setResizable(false);
    }
    public Window(String title, double sizeX, double sizeY){
        super("Password Vault by Antonio Kim");
        setLayout(new BorderLayout());
        setSize((int)p.convertScreenX(sizeX), (int)p.convertScreenY(sizeY));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        addWindowListener(new java.awt.event.WindowAdapter() {
//            @Override
//            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
//                System.exit(0);
//            }
//        });
        setResizable(false);
    }
    
    public void Switch(JPanel panel1, JPanel panel2){
        panel1.setVisible(false);
        remove(panel1);
        add(panel2, BorderLayout.CENTER);
        panel2.setVisible(true);
        panel2.setFocusable(true);
        panel2.requestFocusInWindow();
    }
    
    public double getWindowDiagonal(){
        double diagonal = Math.sqrt(Math.pow(getWidth(), 2) + Math.pow(getHeight(), 2));
        return diagonal;
    }
}
