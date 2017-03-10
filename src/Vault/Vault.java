/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package Vault;

import Main.GUI;
import Main.Main;
import Main.p;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;

/**
*
* @author Antonio's Laptop
*/
public class Vault extends GUI{
    
    private String path = "";
    private String username = "", password = "";
    private JTextField searchBar = JTextField(p.convertX(50), p.convertY(15), p.convertX(300), "");
    private String before = "";
    private boolean defaultShowFavourite = false;
    private boolean favouritesSelected = false, favouritesHovered = false,  allHovered = false, favouritesPressed = false, allPressed = false;
    
    public Vault(String path){
        this.path = path;
        readFile(path);
        favouritesSelected = defaultShowFavourite;
        setLayout(null);
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
        searchBar.setFont(p.font.calibri(p.getFontSize(25)));
        searchBar.setSize(searchBar.getWidth(), p.convertY(30));
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
        pane.setLocation(p.convertX(15), p.convertY(110));
        pane.setSize(p.convertX(330), Main.w.getHeight()-p.convertY(20)-pane.getY());
        pane.setBorder(border(0));
        add(pane);
        pane.setViewportView(entryList);
        entryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        entryList.setBackground(this.getBackground());
        entryList.setFont(p.font.calibri(p.getFontSize(30)));
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
            for (int a = p.getFontSize(35); a>10; a--){
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
        }
    }
    public void updateIndexDetails(){
        repaint();
        details.switchEntry(entries.get(index));
    }
    
    JTextArea titleArea = JTextArea(p.convertX(380), p.convertY(75), p.convertX(300), p.convertY(40), p.getFontSize(35), false, false);
    JScrollPane dataPane = JScrollPane(p.convertX(400), p.convertY(150), p.convertX(460), 100);
    DetailsPane details = new DetailsPane();
    JPopupMenu popup = new JPopupMenu();
    JMenuItem item;
    JPopupMenu addPopup = new JPopupMenu();
    JMenuItem addItem1, addItem2;
    private void entryDetailsSetup(){
        add(titleArea);
        titleArea.addKeyListener(details);
        popup.add(item = new JMenuItem("Edit"));
        item.setHorizontalTextPosition(JMenu.RIGHT);
        item.setFont(p.font.calibri(p.getFontSize(20)));
        item.addActionListener((ActionEvent ae) -> {
            toggleEditting();
        });
        addPopup.add(addItem1 = new JMenuItem("Add Username and Password"));
        addItem1.setHorizontalTextPosition(JMenu.RIGHT);
        addItem1.setFont(p.font.calibri(p.getFontSize(20)));
        addItem1.addActionListener((ActionEvent ae) -> {
            new AddWindow(entries.get(indexOf(titleArea.getText())), true).setVisible(true);
        });
        addPopup.add(addItem2 = new JMenuItem("Add Other"));
        addItem2.setHorizontalTextPosition(JMenu.RIGHT);
        addItem2.setFont(p.font.calibri(p.getFontSize(20)));
        addItem2.addActionListener((ActionEvent ae) -> {
            new AddWindow(entries.get(indexOf(titleArea.getText())), false).setVisible(true);
        });
        dataPane.setSize(new Dimension(dataPane.getWidth(), Main.w.getHeight()-p.convertY(20)-dataPane.getY()));
        dataPane.getVerticalScrollBar().setUnitIncrement(p.convertY(8));
        dataPane.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed (MouseEvent me){
                if (me.getButton() == 3){
                    if (!editting){
                        popup.show(dataPane, me.getX()-p.convertX(55), me.getY());
                    }
                    else{
                        popup.show(dataPane, me.getX()-p.convertY(210), me.getY());
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
                if (favouritesSelected && mx > p.convertX(15) && mx < p.convertX(175) && my > p.convertY(60) && my < p.convertY(90)){
                    allHovered = true;
                }
                else if (!favouritesSelected && mx > p.convertX(175) && mx < p.convertX(335) && my > p.convertY(60) && my < p.convertY(90)){
                    favouritesHovered = true;
                }
                addButtonHover = false;
                if (mx > addButtonX-p.convertX(2) && mx < addButtonX+p.convertX(37) && my > p.convertY(12) && my < p.convertY(51)){
                    addButtonHover = true;
                }
                optionsButtonHover = false;
                if (mx > optionsButtonX-p.convertX(2) && mx < optionsButtonX+p.convertX(37) && my > p.convertY(12) && my < p.convertY(51)){
                    optionsButtonHover = true;
                }
                if (entryList.getSelectedIndex() >= 0){
                    deleteButtonHover = false;
                    editButtonHover = false;
                    favouriteButtonHover = false;
                    addFieldHover = false;
                    if (mx > deleteButtonX+p.convertX(3) && mx<deleteButtonX+p.convertX(42) && my > deleteButtonY+p.convertY(3) && my< deleteButtonY+p.convertY(43)){
                        deleteButtonHover = true;
                    }
                    else if (mx > favouriteButtonX && mx< favouriteButtonX+p.convertX(28) && my > favouriteButtonY && my< favouriteButtonY+p.convertY(40)){
                        favouriteButtonHover = true;
                    }
                    else if (mx > editButtonX && mx< editButtonX+p.convertX(39) && my > editButtonY+p.convertY(2) && my< editButtonY+p.convertY(42)){
                        editButtonHover = true;
                    }
                    else if (editting &&  mx > addFieldX-p.convertX(3) && mx < addFieldX+p.convertX(35) && my > addFieldY-p.convertY(4) && my < addFieldY+p.convertY(36)){
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
                    new AddWindow().setVisible(true);
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
        addPopup.show(this, x-p.convertX(230), y);
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
        Thread thread = new Thread(() -> {
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
        });
        thread.start();
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
        //        Thread thread = new Thread(() -> {
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
        
        //        });
        //        thread.start();
        
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
        g.drawLine(p.convertX(50), p.convertY(45), p.convertX(340), p.convertY(45));
        drawImage(g, Main.images.search, p.convertX(15), p.convertY(15), p.getImageScale(0.25));
    }
    public void drawTabs(Graphics g){
        Graphics2D g2 = (Graphics2D)g;
        g.setColor(Color.GRAY);
        if (!favouritesSelected){
            g.fillRect(p.convertX(15), p.convertY(60), p.convertX(160), p.convertY(30));
            if (favouritesPressed){
                g.setColor(Color.WHITE);
                g.fillRect(p.convertX(175), p.convertY(60), p.convertX(160), p.convertY(30));
            }
            else if (favouritesHovered){
                g.setColor(Color.LIGHT_GRAY);
                g.fillRect(p.convertX(175), p.convertY(60), p.convertX(160), p.convertY(30));
            }
        }
        else{
            g.fillRect(p.convertX(175), p.convertY(60), p.convertX(160), p.convertY(30));
            if (allPressed){
                g.setColor(Color.WHITE);
                g.fillRect(p.convertX(15), p.convertY(60), p.convertX(160), p.convertY(30));
            }
            else if (allHovered){
                g.setColor(Color.LIGHT_GRAY);
                g.fillRect(p.convertX(15), p.convertY(60), p.convertX(160), p.convertY(30));
            }
        }
        g.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(p.getFontSize(2)));
        g.drawRect(p.convertX(15), p.convertY(60), p.convertX(320), p.convertY(30));
        g.drawLine(p.convertX(175), p.convertY(60), p.convertX(175), p.convertY(90));
        g.setFont(p.font.calibriBold(p.getFontSize(25)));
        g.drawString("All", p.convertX(80), p.convertY(82));
        g.drawString("Favourites", p.convertX(205), p.convertY(82));
        //dividing line
        g.setColor(Color.GRAY);
        g.drawLine(p.convertX(350), p.convertY(60), p.convertX(350), p.convertY(640));
    }
    int addButtonX = p.convertX(780), optionsButtonX = p.convertX(830);
    boolean addButtonHover = false, addButtonPressed = false, optionsButtonHover = false, optionsButtonPressed = false;
    public void drawOptionBar(Graphics g){
        g.setColor(Color.LIGHT_GRAY);
        g.fillRoundRect(p.convertX(370), p.convertY(5), p.convertX(505), p.convertY(50), p.convertX(20), p.convertY(20));
        g.setColor(Color.BLACK);
        g.setFont(p.font.calibri(p.getFontSize(35)));
        g.drawString(username, p.convertX(380), p.convertY(40));
        if (addButtonHover){
            if (addButtonPressed){
                g.setColor(Color.WHITE);
            }
            else{
                g.setColor(this.getBackground());
            }
            g.fillRoundRect(addButtonX-p.convertX(2), p.convertY(10), p.convertX(39), p.convertY(39), p.convertX(20), p.convertY(20));
            g.setColor(Color.BLACK);
            g.setFont(p.font.calibri(p.getFontSize(15)));
            g.drawString("Add", addButtonX+p.convertX(5), p.convertY(60));
            
        }
        drawImage(g, Main.images.add, addButtonX, p.convertY(12), p.getImageScale(0.275));
        if (optionsButtonHover){
            if (optionsButtonPressed){
                g.setColor(Color.WHITE);
            }
            else{
                g.setColor(this.getBackground());
            }
            g.fillRoundRect(optionsButtonX-p.convertX(2), p.convertY(10), p.convertX(39), p.convertY(39), p.convertX(20), p.convertY(20));
            g.setColor(Color.BLACK);
            g.setFont(p.font.calibri(p.getFontSize(15)));
            g.drawString("Options", optionsButtonX-p.convertX(5), p.convertY(60));
        }
        drawImage(g, Main.images.options, optionsButtonX, p.convertY(12), p.getImageScale(0.275));
    }
    int deleteButtonX = p.convertX(825), deleteButtonY = p.convertY(70), favouriteButtonX = p.convertX(785), favouriteButtonY = p.convertY(73),
            editButtonX = p.convertX(740), editButtonY = p.convertY(71), addFieldX = p.convertX(698), addFieldY = p.convertY(77);
    boolean deleteButtonHover = false, deleteButtonPressed = false, favouriteButtonHover = false, favouriteButtonPressed = false,
            editButtonHover = false, editButtonPressed = false, editting = false, addFieldHover = false, addFieldPressed = false;
    public void drawDetailsPane(Graphics g){
        g.setColor(Color.LIGHT_GRAY);
        g.setFont(p.font.calibri(p.getFontSize(20)));
        if (deleteButtonHover){
            if (deleteButtonPressed){
                g.setColor(Color.GRAY);
            }
            g.fillRoundRect(deleteButtonX+p.convertX(3), deleteButtonY+p.convertX(3), p.convertX(39), p.convertY(40), p.convertX(20), p.convertY(20));
            g.setColor(Color.BLACK);
            g.drawString("Delete", deleteButtonX-p.convertX(5), deleteButtonY+p.convertY(55));
        }
        else if (favouriteButtonHover){
            if (favouriteButtonPressed){
                g.setColor(Color.GRAY);
            }
            g.fillRoundRect(favouriteButtonX, favouriteButtonY, p.convertX(38), p.convertY(40), p.convertX(20), p.convertY(20));
            g.setColor(Color.BLACK);
            if (entries.get(index).favourite == true){
                g.drawString("Unfavourite", favouriteButtonX-p.convertX(30) ,favouriteButtonY+p.convertY(55));
            }
            else{
                g.drawString("Favourite", favouriteButtonX-p.convertX(20), favouriteButtonY+p.convertY(55));
            }
        }
        else if (editButtonHover){
            if (editButtonPressed){
                g.setColor(Color.GRAY);
            }
            g.fillRoundRect(editButtonX, editButtonY+p.convertY(2), p.convertX(35), p.convertY(40), p.convertX(20), p.convertY(20));
            g.setColor(Color.BLACK);
            if (editting){
                g.drawString("Save", editButtonX+p.convertX(3), editButtonY+p.convertY(55));
            }
            else{
                g.drawString("Edit", editButtonX+p.convertX(3), editButtonY+p.convertY(55));
            }
        }
        else if (addFieldHover){
            if (addFieldPressed){
                g.setColor(Color.GRAY);
            }
            g.fillRoundRect(addFieldX-p.convertX(3), addFieldY-p.convertY(4), p.convertX(38), p.convertY(40), p.convertX(20), p.convertY(20));
            g.setColor(Color.BLACK);
            g.drawString("Add", addFieldX, addFieldY+p.convertY(50));
        }
        drawImage(g, Main.images.delete, deleteButtonX, deleteButtonY, p.getImageScale(0.95));
        if (entries.get(index).favourite == true){
            drawImage(g, Main.images.favouriteSelected, favouriteButtonX, favouriteButtonY, p.getImageScale(0.3));
        }
        else{
            drawImage(g, Main.images.favouriteUnselected, favouriteButtonX, favouriteButtonY, p.getImageScale(0.3));
        }
        if (editting){
            drawImage(g, Main.images.save, editButtonX+p.convertX(3), editButtonY+p.convertY(5), p.getImageScale(0.28));
        }
        else{
            drawImage(g, Main.images.edit, editButtonX, editButtonY, p.getImageScale(0.35));
        }
        if (editting){
            drawImage(g, Main.images.addField, addFieldX, addFieldY, p.getImageScale(0.25));
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
    
}
