package Main;


import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class Window extends JFrame{
    
    public int width = convertScreenX(400), height = convertScreenY(400);
    
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
    public Window(double sizeX, double sizeY){
        super("Password Vault by Antonio Kim");
        setLayout(new BorderLayout());
        setSize((int)convertScreenX(sizeX), (int)convertScreenY(sizeY));
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
        super(title);
        setLayout(new BorderLayout());
        setSize((int)convertScreenX(sizeX), (int)convertScreenY(sizeY));
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
    
    public double getWindowDiagonal(){
        double diagonal = Math.sqrt(Math.pow(getWidth(), 2) + Math.pow(getHeight(), 2));
        return diagonal;
    }
    
    
    public int getFontSizeSmall(double old){
        if (old == 0){
            return 0;
        }
        return (int)(getWindowDiagonal()/(565.685424949238/old));
    }
    public int getFontSize(double old){
        if (old == 0){
            return 0;
        }
        return (int)(getWindowDiagonal()/(1140.175425099138/old));
    }
    public double getImageScale (double old){
        if (old == 0){
            return 0;
        }
        return (getWindowDiagonal()/(1140.175425099138/old));
    }
    public int convertX(double old){
        if (old == 0){
            return 0;
        }
        return (int)(getWidth()/(900.0/old));
    }
    public int convertY(double old){
        if (old == 0){
            return 0;
        }
        return (int)(getHeight()/(600.0/old));
    }
    public int convertXSmall(double old){
        return (int)(getHeight()/(400.0/old));
    }
    public int convertYSmall(double old){
        return (int)(getHeight()/(400.0/old));
    }
    public int convertScreenX(double old){
        return (int)(p.getScreenWidth()/(1200.0/old));
    }
    public int convertScreenY(double old){
        return (int)(p.getScreenHeight()/(700.0/old));
    }
}
