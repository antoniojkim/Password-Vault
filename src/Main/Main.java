package Main;

import Encryptor.Encryptor;
import Images.Images;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

/**
*
* @author Antonio's Laptop
*/
public class Main {
    
    public static Window w = new Window();
    public static Encryptor encrpytor = new Encryptor();
    public static File vault;
    public static Images images = new Images();
    public static Login login;
    
    public static void main(String[] args) {
        vault = new File("./Vault Files");
        if (!vault.exists()){
            vault.mkdir();
        }
        if (Files.isDirectory(Paths.get("./Vault Files"))){
            login = new Login();
            w.add(login);
            w.setVisible(true);
            login.readSettings();
            if (!login.username.getText().equals("")){
                try {
                    Robot robot = new Robot();
                    robot.keyPress(KeyEvent.VK_ENTER);
                    robot = null;
                } catch (AWTException ex) {            }
            }
        }
        
    }
    
}
