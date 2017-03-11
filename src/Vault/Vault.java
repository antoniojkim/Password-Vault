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
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.event.MouseInputAdapter;

/**
*
* @author Antonio's Laptop
*/
public class Vault extends GUI{
    
    private Window w;
    private String path = "";
    private String username = "", password = "";
    private JTextField searchBar;
    private String before = "";
    private boolean defaultShowFavourite = false;
    private boolean favouritesSelected = false, favouritesHovered = false,  allHovered = false, favouritesPressed = false, allPressed = false;
    
    public Vault(String path){
        this.path = path;
        readFile(path);
        favouritesSelected = defaultShowFavourite;
    }
    
    public void open(){
        w = new Window("Password Vault by Antonio Kim - "+username, 900, 600);
        addContent();
        w.add(this);
        w.setVisible(true);
    }
    
    public void addContent(){
        setLayout(null);
        searchBar = JTextField(w.convertX(50), w.convertY(15), w.convertX(300), "");
        titleArea = JTextArea(w.convertX(380), w.convertY(75), w.convertX(300), w.convertY(40), w.getFontSize(35), false, false);
        dataPane = JScrollPane(w.convertX(400), w.convertY(150), w.convertX(460), 100);
        details = new DetailsPane();
        addButtonX = w.convertX(780);     optionsButtonX = w.convertX(830);
        deleteButtonX = w.convertX(825); deleteButtonY = w.convertY(70); favouriteButtonX = w.convertX(785); favouriteButtonY = w.convertY(73);
        editButtonX = w.convertX(740); editButtonY = w.convertY(71); addFieldX = w.convertX(698); addFieldY = w.convertY(77);
        entryDetailsSetup();
        leftMenuSetup();
        addListeners();
    }
    
    private static List<Entry> entries = new ArrayList<>();
    private JList entryList = new JList();
    private DefaultListModel model = new DefaultListModel();
    private static int index = -1;
    private JScrollPane pane;
    public void leftMenuSetup(){
        searchBar.setBackground(this.getBackground());
        searchBar.setBorder(border(0));
        searchBar.setFont(p.font.calibri(w.getFontSize(25)));
        searchBar.setSize(searchBar.getWidth(), w.convertY(30));
        searchBar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent ke) {
                if (!searchBar.getText().equals(before)){
                    before = searchBar.getText();
                    entryList.setSelectedIndex(-1);
                    titleArea.setText("");
                    details.setVisible(false);
                    updateModel();
                    updateDetails();
                }
            }
        });
        add(searchBar);
        pane = new JScrollPane();
        pane.setLocation(w.convertX(15), w.convertY(110));
        pane.setSize(w.convertX(330), w.getHeight()-w.convertY(20)-pane.getY());
        pane.setBorder(border(0));
        add(pane);
        pane.setViewportView(entryList);
        entryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        entryList.setBackground(this.getBackground());
        entryList.setFont(p.font.calibri(w.getFontSize(30)));
        entryList.setModel(model);
        entryList.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed (MouseEvent me){
                updateDetails();
            }
            @Override
            public void mouseReleased (MouseEvent me){
                details.mouseDragToDefault();
            }
        });
        updateModel();
//        entryList.setSelectedIndex(-1);
//        updateDetails();
    }
    
    public void updateDetails(){
        repaint();
        if (entryList.getSelectedIndex() != -1){
            index = indexOf(model.get(entryList.getSelectedIndex()).toString());
            for (int a = w.getFontSize(35); a>10; a--){
                titleArea.setFont(p.font.calibri(a));
                if (p.stringWidth(entries.get(index).title, titleArea.getFont()) <= titleArea.getWidth()){
                    break;
                }
            }
            titleArea.setText(entries.get(index).title);
            details.setVisible(true);
            details.switchEntry(entries.get(index));
        }
        else{
            titleArea.setText("");
            details.setVisible(false);
        }
    }
    public void updateIndexDetails(){
        repaint();
        details.switchEntry(entries.get(index));
    }
    
    JTextArea titleArea;
    JScrollPane dataPane;
    DetailsPane details;
    JPopupMenu popup = new JPopupMenu();
    JMenuItem item;
    JPopupMenu addPopup = new JPopupMenu();
    JMenuItem addItem1, addItem2;
    private void entryDetailsSetup(){
        add(titleArea);
        titleArea.addKeyListener(details);
        popup.add(item = new JMenuItem("Edit"));
        item.setHorizontalTextPosition(JMenu.RIGHT);
        item.setFont(p.font.calibri(w.getFontSize(20)));
        item.addActionListener((ActionEvent ae) -> {
            toggleEditting();
        });
        addPopup.add(addItem1 = new JMenuItem("Add Username and Password"));
        addItem1.setHorizontalTextPosition(JMenu.RIGHT);
        addItem1.setFont(p.font.calibri(w.getFontSize(20)));
        addItem1.addActionListener((ActionEvent ae) -> {
            new AddWindow(entries.get(indexOf(titleArea.getText())), true).open();
        });
        addPopup.add(addItem2 = new JMenuItem("Add Other"));
        addItem2.setHorizontalTextPosition(JMenu.RIGHT);
        addItem2.setFont(p.font.calibri(w.getFontSize(20)));
        addItem2.addActionListener((ActionEvent ae) -> {
            new AddWindow(entries.get(indexOf(titleArea.getText())), false).open();
        });
        dataPane.setSize(new Dimension(dataPane.getWidth(), w.getHeight()-w.convertY(20)-dataPane.getY()));
        dataPane.getVerticalScrollBar().setUnitIncrement(w.convertY(8));
        dataPane.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed (MouseEvent me){
                if (me.getButton() == 3){
                    if (!editting){
                        popup.show(dataPane, me.getX()-w.convertX(55), me.getY());
                    }
                    else{
                        popup.show(dataPane, me.getX()-w.convertY(210), me.getY());
                    }
                }
            }
        });
        dataPane.setViewportView(details);
        dataPane.setBorder(new LineBorder(Color.BLACK, 0));
        add(dataPane);
    }
    public void addEntry(Entry entry){
        entries.add(entry);
    }
    
    private void addListeners(){
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent me) {
                int mx = me.getX(), my = me.getY();
                favouritesHovered = false;
                allHovered = false;
                if (favouritesSelected && mx > w.convertX(15) && mx < w.convertX(175) && my > w.convertY(60) && my < w.convertY(90)){
                    allHovered = true;
                }
                else if (!favouritesSelected && mx > w.convertX(175) && mx < w.convertX(335) && my > w.convertY(60) && my < w.convertY(90)){
                    favouritesHovered = true;
                }
                addButtonHover = false;
                if (mx > addButtonX-w.convertX(2) && mx < addButtonX+w.convertX(37) && my > w.convertY(12) && my < w.convertY(51)){
                    addButtonHover = true;
                }
                optionsButtonHover = false;
                if (mx > optionsButtonX-w.convertX(2) && mx < optionsButtonX+w.convertX(37) && my > w.convertY(12) && my < w.convertY(51)){
                    optionsButtonHover = true;
                }
                if (entryList.getSelectedIndex() >= 0){
                    deleteButtonHover = false;
                    editButtonHover = false;
                    favouriteButtonHover = false;
                    addFieldHover = false;
                    if (mx > deleteButtonX+w.convertX(3) && mx<deleteButtonX+w.convertX(42) && my > deleteButtonY+w.convertY(3) && my< deleteButtonY+w.convertY(43)){
                        deleteButtonHover = true;
                    }
                    else if (mx > favouriteButtonX && mx< favouriteButtonX+w.convertX(28) && my > favouriteButtonY && my< favouriteButtonY+w.convertY(40)){
                        favouriteButtonHover = true;
                    }
                    else if (mx > editButtonX && mx< editButtonX+w.convertX(39) && my > editButtonY+w.convertY(2) && my< editButtonY+w.convertY(42)){
                        editButtonHover = true;
                    }
                    else if (editting &&  mx > addFieldX-w.convertX(3) && mx < addFieldX+w.convertX(35) && my > addFieldY-w.convertY(4) && my < addFieldY+w.convertY(36)){
                        addFieldHover = true;
                    }
                }
                repaint();
            }
        });
        addMouseListener(new MouseListener(){
            @Override  public void mouseClicked(MouseEvent me) {            } @Override    public void mouseEntered(MouseEvent me) {            } @Override    public void mouseExited(MouseEvent me) { }
            
            @Override
            public void mousePressed (MouseEvent me){
                allPressed = false;
                favouritesPressed = false;
                if (favouritesSelected && allHovered){
                    allPressed = true;
                }
                else if (!favouritesSelected && favouritesHovered){
                    favouritesPressed = true;
                }
                addButtonPressed = addButtonHover;
                optionsButtonPressed =optionsButtonHover;
                deleteButtonPressed = deleteButtonHover;
                favouriteButtonPressed = favouriteButtonHover;
                editButtonPressed = editButtonHover;
                addFieldPressed = addFieldHover;
                repaint();
            }
            @Override
            public void mouseReleased (MouseEvent me){
                if (allPressed){
                    allPressed = false;
                    favouritesSelected = false;
                    updateModel();
                }
                else if (favouritesPressed){
                    favouritesPressed = false;
                    favouritesSelected = true;
                    updateModel();
                }
                if (addButtonPressed){
                    addButtonPressed = false;
                    addButtonHover = false;
                    new AddWindow().open();
                }
                if (favouriteButtonPressed){
                    favouriteButtonPressed = false;
                    entries.get(index).toggleFavourite();
                    print();
                    updateModel();
                }
                else if (deleteButtonPressed){
                    deleteButtonPressed = false;
                    int confirm = showConfirmDialog("Are you sure you want to delete this entry?", "Delete Entry?", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
                    if (confirm == JOptionPane.OK_OPTION){
                        entries.remove(index);
                        updateModel();
                        print();
                    }
                }
                else if (editButtonPressed){
                    editButtonPressed = false;
                    toggleEditting();
                }
                else if (addFieldPressed){
                    addFieldPressed = false;
                    addPopup(me.getX(), me.getY());
                }
                else if (optionsButtonPressed){
                    optionsButtonPressed = false;
                    openSettings();
                }
                repaint();
            }
        });
    }
    
    private void addPopup(int x, int y){
        addPopup.show(this, x-w.convertX(230), y);
    }
    
    private void openSettings(){
        new Settings(this).Open();
    }
    
    private boolean debug = false;
    private void readFile(String path){
        BufferedReader br = p.filereader(path);
        try {
            String decrypted = Main.encrpytor.decrypt(br.readLine());
            String[] lines = decrypted.split("\\$p1l7");
            username = lines[0];
            password = lines[1];
            if (lines[2].equals("false")){
                defaultShowFavourite = false;
            }
            else{
                defaultShowFavourite = true;
            }
            int numEntries = Integer.parseInt(lines[3]);
            if (debug){
                System.out.println(numEntries);
            }
            int lineNumber = 4;
            for (int a = 0; a<numEntries; a++){
                Entry entry = new Entry(lines[lineNumber]);
                lineNumber++;
                if (lines[lineNumber].equals("true")){
                    entry.favourite = true;
                }
                lineNumber++;
                int numfields = Integer.parseInt(lines[lineNumber]);
                lineNumber++;
                for (int b = 0; b<numfields; b++){
                    entry.add(lines[lineNumber], lines[lineNumber+1]);
                    lineNumber+=2;
                }
                entries.add(entry);
            }
        } catch (IOException ex) {        }
        if (debug){
            System.out.println(username);
            System.out.println(password);
            System.out.println(entries.size());
        }
    }
    
    public void print(){
        String encrypted = "";
        encrypted += username+"$p1l7";
        encrypted += password+"$p1l7";
        encrypted += defaultShowFavourite+"$p1l7";
        encrypted += entries.size()+"$p1l7";
        for (int a = 0; a<entries.size(); a++){
            encrypted += entries.get(a).title+"$p1l7";
            encrypted += entries.get(a).favourite+"$p1l7";
            encrypted += entries.get(a).fieldTitles.size()+"$p1l7";
            for (int b = 0; b<entries.get(a).fieldTitles.size(); b++){
                encrypted += entries.get(a).fieldTitles.get(b)+"$p1l7";
                encrypted += entries.get(a).fieldData.get(b)+"$p1l7";
            }
        }
        PrintWriter pr = p.printwriter(path);
        pr.print(Main.encrpytor.encrypt(encrypted));
        pr.close();
        if (debug){
            System.out.println(username);
            System.out.println(password);
            System.out.println(entries.size());
        }
    }
    
    public void toggleEditting(){
        String title = titleArea.getText();
        if (editting){
            editting = false;
            details.setEditting(editting);
            entries.get(index).copy(details.entry);
            titleArea.setFocusable(false);
            titleArea.setFocusable(true);
            entries.get(index).setTitle(title);
            print();
            updateModel();
            item.setText("Edit");
        }
        else{
            editting = true;
            item.setText("Stop Editting and Save");
            details.setEditting(editting);
        }
        titleArea.setEditable(editting);
    }
    public void toggleFavouriteSelected(boolean selected){
        favouritesSelected = selected;
    }
    
    public void updateModel(){
        final String title = titleArea.getText();
        model.clear();
        List<String> elements = new ArrayList<>();
        elements.addAll(getTitles());
        p.sort(elements);
        String keyword = searchBar.getText().toLowerCase();
        for (int a = 0; a<elements.size(); a++) {
            if (elements.get(a).toLowerCase().contains(keyword)) {
                int index1 = indexOf(elements.get(a));
                if (favouritesSelected && entries.get(index1).favourite == true) {
                    model.addElement(elements.get(a));
                } else if (!favouritesSelected){
                    model.addElement(elements.get(a));
                }
            }
        }
        if (model.size() == 0){
            model.addElement("No Items");
            entryList.setEnabled(false);
            details.setVisible(false);
        }
        else{
            entryList.setEnabled(true);
            index = indexOfModel(title);
            if (index != -1){
                entryList.setSelectedIndex(index);
            }
            else if (searchBar.getText().trim().equals("")){
                details.setVisible(true);
            }
        }
        updateDetails();
        
    }
    public void updateModel(List<String> list){
        model.clear();
        List<String> elements = new ArrayList<>();
        elements.addAll(list);
        p.sort(elements);
        for (int a = 0; a<elements.size(); a++){
            model.addElement(elements.get(a));
        }
        if (model.size() == 0){
            model.addElement("No Items");
            entryList.setEnabled(false);
            details.switchEntry(null);
        }
        else{
            entryList.setEnabled(true);
        }
    }
    
    public List<String> getTitles(){
        List<String> list = new ArrayList<>();
        for (int a = 0; a<entries.size(); a++){
            list.add(entries.get(a).title);
        }
        return list;
    }
    public int indexOf(String str){
        for (int a = 0; a<entries.size(); a++){
            if (entries.get(a).title.equals(str)){
                return a;
            }
        }
        return -1;
    }
    public int indexOfModel(String str){
        for (int a = 0; a<model.size(); a++){
            if (model.get(a).toString().equals(str)){
                return a;
            }
        }
        return -1;
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawSearchBar(g);
        drawTabs(g);
        drawOptionBar(g);
        if (entryList.getSelectedIndex()  >= 0){
            drawDetailsPane(g);
        }
    }
    public void drawSearchBar(Graphics g){
        g.drawLine(w.convertX(50), w.convertY(45), w.convertX(340), w.convertY(45));
        drawImage(g, Main.images.search, w.convertX(15), w.convertY(15), w.getImageScale(0.25));
    }
    public void drawTabs(Graphics g){
        Graphics2D g2 = (Graphics2D)g;
        g.setColor(Color.GRAY);
        if (!favouritesSelected){
            g.fillRect(w.convertX(15), w.convertY(60), w.convertX(160), w.convertY(30));
            if (favouritesPressed){
                g.setColor(Color.WHITE);
                g.fillRect(w.convertX(175), w.convertY(60), w.convertX(160), w.convertY(30));
            }
            else if (favouritesHovered){
                g.setColor(Color.LIGHT_GRAY);
                g.fillRect(w.convertX(175), w.convertY(60), w.convertX(160), w.convertY(30));
            }
        }
        else{
            g.fillRect(w.convertX(175), w.convertY(60), w.convertX(160), w.convertY(30));
            if (allPressed){
                g.setColor(Color.WHITE);
                g.fillRect(w.convertX(15), w.convertY(60), w.convertX(160), w.convertY(30));
            }
            else if (allHovered){
                g.setColor(Color.LIGHT_GRAY);
                g.fillRect(w.convertX(15), w.convertY(60), w.convertX(160), w.convertY(30));
            }
        }
        g.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(w.getFontSize(2)));
        g.drawRect(w.convertX(15), w.convertY(60), w.convertX(320), w.convertY(30));
        g.drawLine(w.convertX(175), w.convertY(60), w.convertX(175), w.convertY(90));
        g.setFont(p.font.calibriBold(w.getFontSize(25)));
        g.drawString("All", w.convertX(80), w.convertY(82));
        g.drawString("Favourites", w.convertX(205), w.convertY(82));
        //dividing line
        g.setColor(Color.GRAY);
        g.drawLine(w.convertX(350), w.convertY(60), w.convertX(350), w.convertY(640));
    }
    int addButtonX, optionsButtonX;
    boolean addButtonHover = false, addButtonPressed = false, optionsButtonHover = false, optionsButtonPressed = false;
    public void drawOptionBar(Graphics g){
        g.setColor(Color.LIGHT_GRAY);
        g.fillRoundRect(w.convertX(370), w.convertY(5), w.convertX(505), w.convertY(50), w.convertX(20), w.convertY(20));
        g.setColor(Color.BLACK);
        g.setFont(p.font.calibri(w.getFontSize(35)));
        g.drawString(username, w.convertX(380), w.convertY(40));
        if (addButtonHover){
            if (addButtonPressed){
                g.setColor(Color.WHITE);
            }
            else{
                g.setColor(this.getBackground());
            }
            g.fillRoundRect(addButtonX-w.convertX(2), w.convertY(10), w.convertX(39), w.convertY(39), w.convertX(20), w.convertY(20));
            g.setColor(Color.BLACK);
            g.setFont(p.font.calibri(w.getFontSize(15)));
            g.drawString("Add", addButtonX+w.convertX(5), w.convertY(60));
            
        }
        drawImage(g, Main.images.add, addButtonX, w.convertY(12), w.getImageScale(0.275));
        if (optionsButtonHover){
            if (optionsButtonPressed){
                g.setColor(Color.WHITE);
            }
            else{
                g.setColor(this.getBackground());
            }
            g.fillRoundRect(optionsButtonX-w.convertX(2), w.convertY(10), w.convertX(39), w.convertY(39), w.convertX(20), w.convertY(20));
            g.setColor(Color.BLACK);
            g.setFont(p.font.calibri(w.getFontSize(15)));
            g.drawString("Options", optionsButtonX-w.convertX(5), w.convertY(60));
        }
        drawImage(g, Main.images.options, optionsButtonX, w.convertY(12), w.getImageScale(0.275));
    }
    int deleteButtonX, deleteButtonY, favouriteButtonX, favouriteButtonY, editButtonX, editButtonY, addFieldX, addFieldY;
    boolean deleteButtonHover = false, deleteButtonPressed = false, favouriteButtonHover = false, favouriteButtonPressed = false,
            editButtonHover = false, editButtonPressed = false, editting = false, addFieldHover = false, addFieldPressed = false;
    public void drawDetailsPane(Graphics g){
        g.setColor(Color.LIGHT_GRAY);
        g.setFont(p.font.calibri(w.getFontSize(20)));
        if (deleteButtonHover){
            if (deleteButtonPressed){
                g.setColor(Color.GRAY);
            }
            g.fillRoundRect(deleteButtonX+w.convertX(3), deleteButtonY+w.convertX(3), w.convertX(39), w.convertY(40), w.convertX(20), w.convertY(20));
            g.setColor(Color.BLACK);
            g.drawString("Delete", deleteButtonX-w.convertX(5), deleteButtonY+w.convertY(55));
        }
        else if (favouriteButtonHover){
            if (favouriteButtonPressed){
                g.setColor(Color.GRAY);
            }
            g.fillRoundRect(favouriteButtonX, favouriteButtonY, w.convertX(38), w.convertY(40), w.convertX(20), w.convertY(20));
            g.setColor(Color.BLACK);
            if (entries.get(index).favourite == true){
                g.drawString("Unfavourite", favouriteButtonX-w.convertX(30) ,favouriteButtonY+w.convertY(55));
            }
            else{
                g.drawString("Favourite", favouriteButtonX-w.convertX(20), favouriteButtonY+w.convertY(55));
            }
        }
        else if (editButtonHover){
            if (editButtonPressed){
                g.setColor(Color.GRAY);
            }
            g.fillRoundRect(editButtonX, editButtonY+w.convertY(2), w.convertX(35), w.convertY(40), w.convertX(20), w.convertY(20));
            g.setColor(Color.BLACK);
            if (editting){
                g.drawString("Save", editButtonX+w.convertX(3), editButtonY+w.convertY(55));
            }
            else{
                g.drawString("Edit", editButtonX+w.convertX(3), editButtonY+w.convertY(55));
            }
        }
        else if (addFieldHover){
            if (addFieldPressed){
                g.setColor(Color.GRAY);
            }
            g.fillRoundRect(addFieldX-w.convertX(3), addFieldY-w.convertY(4), w.convertX(38), w.convertY(40), w.convertX(20), w.convertY(20));
            g.setColor(Color.BLACK);
            g.drawString("Add", addFieldX, addFieldY+w.convertY(50));
        }
        drawImage(g, Main.images.delete, deleteButtonX, deleteButtonY, w.getImageScale(0.95));
        if (entries.get(index).favourite == true){
            drawImage(g, Main.images.favouriteSelected, favouriteButtonX, favouriteButtonY, w.getImageScale(0.3));
        }
        else{
            drawImage(g, Main.images.favouriteUnselected, favouriteButtonX, favouriteButtonY, w.getImageScale(0.3));
        }
        if (editting){
            drawImage(g, Main.images.save, editButtonX+w.convertX(3), editButtonY+w.convertY(5), w.getImageScale(0.28));
        }
        else{
            drawImage(g, Main.images.edit, editButtonX, editButtonY, w.getImageScale(0.35));
        }
        if (editting){
            drawImage(g, Main.images.addField, addFieldX, addFieldY, w.getImageScale(0.25));
        }
    }
    
    public String getUsername(){
        return username;
    }
    public String getPasssword(){
        return password;
    }
    public void setPassword(String newPassword){
        password = newPassword;
    }
    public boolean getdefaultShowFavourite(){
        return defaultShowFavourite;
    }
    public void setdefaultShowFavourite (boolean isDefault){
        defaultShowFavourite = isDefault;
    }
    
    private class DetailsPane extends GUI implements KeyListener{
        
        private boolean editting = false;
        private Entry entry = new Entry("");
        private List<JTextArea> titles = new ArrayList<>();
        private List<JPasswordField> data = new ArrayList<>();
        private List<Boolean> removeHover = new ArrayList<>();
        private List<Boolean> removePressed = new ArrayList<>();
        
        public DetailsPane(){
            addContent();
        }
        public DetailsPane(Entry entry){
            this.entry.copy(entry);
            addContent();
        }
        
        private void addContent(){
            setLayout(null);
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
                //data.get(a).setBackground(this.getBackground());
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
                    int width = (int)(1.15*p.stringWidth(entry.fieldTitles.get(a), p.font.calibri(w.getFontSize(25))));
                    if (width > highest){
                        highest = width;
                    }
                    width = w.convertX(40)+(int)(1.15*p.stringWidth(entry.fieldData.get(a), p.font.calibri(w.getFontSize(25))));
                    if (width > highest){
                        highest = width;
                    }
                }
                setPreferredSize(new Dimension(Math.max(highest, getWidth())+w.convertX(5), w.convertY(30)+entry.size()*w.convertY(90)));
                for (int a = 0; a<entry.size(); a++){
                    titles.add(JTextArea(w.convertX(10), w.convertY(5)+a*w.convertY(90), w.convertX(395), w.convertY(30), w.getFontSize(25), editting, false));
                    titles.get(a).setText(entry.fieldTitles.get(a));
                    titles.get(a).setSize(Math.max((int)(1.15*p.stringWidth(titles.get(a).getText(), titles.get(a).getFont())), w.convertX(395)), titles.get(a).getHeight());
                    if (titles.get(a).getWidth() > highest){
                        highest = titles.get(a).getWidth();
                    }
                    add(titles.get(a));
                    titles.get(a).addKeyListener(this);
                    data.add(JPasswordField(w.convertX(40), w.convertY(40)+a*w.convertY(90), w.convertX(395), w.convertY(30), w.getFontSize(25), editting, false));
                    data.get(a).setText(entry.fieldData.get(a));
                    data.get(a).setSize(Math.max((int)(1.15*p.stringWidth(data.get(a).getText(), data.get(a).getFont())), w.convertX(395)), data.get(a).getHeight());
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
                        g.fillRoundRect(w.convertX(408), w.convertY(3)+a*w.convertY(90), w.convertX(34), w.convertY(35), w.getFontSize(20), w.getFontSize(20));
                    }
                    drawImage(g, Main.images.remove, w.convertX(405), a*w.convertY(90), w.getImageScale(0.32));
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
                    if (mx > w.convertX(408) && mx < w.convertX(442) && my > w.convertY(3)+a*w.convertY(90) && my < w.convertY(38)+a*w.convertY(90)){
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
    
}
