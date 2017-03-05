/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package Images;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
*
* @author Antonio's Laptop
*/
public class Images {
    
    public static BufferedImage search;
    public static BufferedImage add;
    public static BufferedImage options;
    public static BufferedImage favouriteSelected;
    public static BufferedImage favouriteUnselected;
    public static BufferedImage favouriteHovered;
    public static BufferedImage edit;
    public static BufferedImage delete;
    public static BufferedImage remove;
    public static BufferedImage addField;
    public static BufferedImage save;
    
    public Images(){
        try {
            search = ImageIO.read(getClass().getResourceAsStream("Search.png"));
            add = ImageIO.read(getClass().getResourceAsStream("Add.png"));
            options = ImageIO.read(getClass().getResourceAsStream("Options.png"));
            favouriteSelected = ImageIO.read(getClass().getResourceAsStream("Favourite Selected.png"));
            favouriteUnselected = ImageIO.read(getClass().getResourceAsStream("Favourite Unselected.png"));
            favouriteHovered = ImageIO.read(getClass().getResourceAsStream("Favourite Hovered.png"));
            edit = ImageIO.read(getClass().getResourceAsStream("Edit.png"));
            delete = ImageIO.read(getClass().getResourceAsStream("Delete.png"));
            remove = ImageIO.read(getClass().getResourceAsStream("Remove.png"));
            addField = ImageIO.read(getClass().getResourceAsStream("Add Field.png"));
            save = ImageIO.read(getClass().getResourceAsStream("Save.png"));
        } catch (IOException ex) {        }
    }
    
}
