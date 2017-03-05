/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package Vault;

import java.util.ArrayList;
import java.util.List;

/**
*
* @author Antonio's Laptop
*/
public class Entry {
    
    String title = "";
    List<String> fieldTitles = new ArrayList<>();
    List<String> fieldData = new ArrayList<>();
    boolean favourite = false;
    
    public Entry(String title){
        this.title = title;
    }
    public Entry (Entry entry){
        copy(entry);
    }
    
    public boolean contains(String substring){
        return title.toLowerCase().contains(substring.toLowerCase());
    }
    
    public void setTitle(String title){
        this.title = title;
    }
    
    public void copy(Entry entry){
        this.title = entry.title;
        fieldTitles.clear();
        fieldTitles.addAll(entry.fieldTitles);
        fieldData.clear();
        fieldData.addAll(entry.fieldData);
        this.favourite = entry.favourite;
    }
    public void add(String title, String data){
        fieldTitles.add(title);
        fieldData.add(data);
    }
    public void remove(int index){
        fieldTitles.remove(index);
        fieldData.remove(index);
    }
    public int size(){
        return fieldTitles.size();
    }
    public void editTitle(int index, String title){
        fieldTitles.set(index, title);
    }
    public void editData(int index, String data){
        fieldData.set(index, data);
    }
    public void toggleFavourite(){
        if (favourite){
            favourite = false;
        }
        else {
            favourite = true;
        }
    }
    
    public void edit(){
        System.out.println("Editting");
//            JFrame frame = new JFrame("Edit Entry");
//            frame.setSize(400, 500);
//            frame.setLocationRelativeTo(Main.w);
//            frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
//            frame.setVisible(true);
    }
}
