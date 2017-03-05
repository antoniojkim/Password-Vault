/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package Encryptor;

import Main.p;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**

@author Antonio
*/
public final class Encryptor {
    
    public Encryptor(){
        open();
//        for (int a = 0; a<normalCharacters.length; a++){
//            System.out.print(normalCharacters[a]+" ");
//        }
//        System.out.println("");
//        for (int a = 0; a<specialCharacters.length; a++){
//            System.out.print(specialCharacters[a]+" ");
//        }
//        System.out.println("");
    }
    
    private boolean opened = false;
    
    private String[] normalCharacters;
    private String[] specialCharacters;
    
    private String[][] cipher;
    
    private void open(){
        if (!opened){
            BufferedReader br = new BufferedReader (new InputStreamReader(getClass().getResourceAsStream("keys.txt")));
            List<Thread> threads = new ArrayList<>();
            try{
                List<String> lines = new ArrayList<>();
                String line = br.readLine();
                while (line != null){
                    lines.add(line);
                    line = br.readLine();
                }
                threads.add(new Thread(()->{
                    String normal = lines.get(0);
                    List<String> characters = new ArrayList<>();
                    for (int a = 0; a<normal.length(); a+=4){
                        characters.add(normal.substring(a, a+1));
                    }
                    normalCharacters = new String[characters.size()];
                    for (int a = 0; a<normalCharacters.length; a++){
                        normalCharacters[a] = characters.get(a);
                    }
                }));
                threads.add(new Thread(()->{
                    String special = lines.get(1);
                    List<String> characters = new ArrayList<>();
                    for (int a = 0; a<special.length(); a+=4){
                        characters.add(special.substring(a, a+1));
                    }
                    specialCharacters = new String[characters.size()];
                    for (int a = 0; a<specialCharacters.length; a++){
                        specialCharacters[a] = characters.get(a);
                    }
                }));
                threads.add(new Thread(()->{
                    try{
                        List<List<String>> characters = new ArrayList<>();
                        String caesar = br.readLine();
                        for (int b = 2; b<lines.size(); b++){
                            characters.add(new ArrayList<>());
                            for (int a = 0; a<lines.get(b).length(); a+=4){
                                characters.get(characters.size()-1).add(lines.get(b).substring(a, a+1));
                            }
                        }
                        cipher = new String[characters.size()][characters.get(0).size()];
                        for (int a = 0; a<cipher.length; a++){
                            for (int b = 0; b<cipher[a].length; b++){
                                cipher[a][b] = characters.get(a).get(b);
                            }
                        }
                    }catch (IOException e){}
                }));
                for (int a = 0; a<threads.size(); a++){
                    threads.get(a).start();
                }
                for (int a = 0; a<threads.size(); a++){
                    try{
                        threads.get(a).join();
                    }catch(InterruptedException e2){}
                }
                opened = true;
            }catch(IOException e){}
        }
    }
    
    private final int numlayers = 5;
    public String multipleEncrypt(String str){
        return multipleEncrypt(str, numlayers);
    }
    public String multipleEncrypt(String str, int layerNum){
        String encrypted = str;
        for (int a = 0; a<layerNum; a++){
            encrypted = encrypt(encrypted);
        }
        return encrypted;
    }
    public String multipleDecrypt(String str){
        return multipleDecrypt(str, numlayers);
    }
    public String multipleDecrypt(String str, int layerNum){
        String decrypted = str;
        for (int a = 0; a<layerNum; a++){
            decrypted = getDecryption(decrypted);
        }
        return decrypted;
    }
    
    public String encrypt(String str){
        return getAdvancedEncryption(str.trim());
    }
    
    private String getAdvancedEncryption(String str){
        if (!opened){
            open();
        }
        if (opened){
            if (str.equals("")){
                return "";
            }
            str = str.replaceAll(" ", "");
            str = str.replaceAll("/", "<s>");
            str = str.replaceAll("'", "<apost>");
            str = str.replaceAll("\"", "<quot>");
            str = str.replaceAll("\n", "<br>");
            String key = "";
            String encrypted = "";
            List<String> list = new ArrayList<>(Arrays.asList(normalCharacters));
            list.addAll(Arrays.asList(specialCharacters));
            int length = list.size();
            int index = p.randomint(0, length-1);
            if (index%4 == 0){
                for (int a = 0; a<str.length(); a++){
                    int r = p.randomint(0, length-1);
                    key += list.get(r);
                    try{
                        encrypted += cipher[r][list.indexOf(str.substring(a, a+1))];
                    }catch(ArrayIndexOutOfBoundsException e){
                        System.out.println("Could not Find:  \""+str.substring(a, a+1)+"\"");
                        System.exit(0);
                    }
                }
                return list.get(index)+key+encrypted;
            }
            else if (index%4 == 1){
                for (int a = 0; a<str.length(); a++){
                    int r = p.randomint(0, length-1);
                    key += list.get(r);
                    try{
                        encrypted += cipher[r][list.indexOf(str.substring(a, a+1))];
                    }catch(ArrayIndexOutOfBoundsException e){
                        System.out.println("Could not Find:  \""+str.substring(a, a+1)+"\"");
                        System.exit(0);
                    }
                }
                return list.get(index)+encrypted+key;
            }
            else if (index%4 == 2){
                for (int a = 0; a<str.length(); a++){
                    try{
                        int r = p.randomint(0, Math.min(list.size(), cipher.length)-1);
                        encrypted += list.get(r)+cipher[r][list.indexOf(str.substring(a, a+1))];
                    }catch(ArrayIndexOutOfBoundsException e){
                        System.out.println("Could not Find:  \""+str.substring(a, a+1)+"\"");
                        System.exit(0);
                    }
                }
                return list.get(index)+encrypted;
            }
            else{
                for (int a = 0; a<str.length(); a++){
                    try{
                        int r = p.randomint(0, Math.min(list.size(), cipher.length)-1);
                        encrypted += cipher[r][list.indexOf(str.substring(a, a+1))]+list.get(r);
                    }catch(ArrayIndexOutOfBoundsException e){
                        System.out.println("Could not Find:  \""+str.substring(a, a+1)+"\"");
                        System.exit(0);
                    }
                }
                return list.get(index)+encrypted;
            }
        }
        return "Failed to Encrypt";
    }
    
    public String decrypt(String str){
        String decrypted = getDecryption(str);
        return decrypted.replaceAll("", " ").replaceAll("<apost>", "'").replaceAll("<quot>", "\"").replaceAll("<br>", "\n").replaceAll("<s>", "/").trim();
    }
    
    private String getDecryption (String str){
        if (!opened){
            open();
        }
        if (opened){
            if (str.equals("")){
                return "";
            }
            String decrypted = "";
            List<String> list = new ArrayList<>(Arrays.asList(normalCharacters));
            list.addAll(Arrays.asList(specialCharacters));
            List<List<String>> ciphers = new ArrayList<>();
            for (int a = 0; a<cipher.length; a++){
                ciphers.add(Arrays.asList(cipher[a]));
            }
            if (str.length()%2 != 0){
                int index = list.indexOf(str.substring(0, 1));
                str = str.substring(1);
                if (index%4 == 0){
                    int half = str.length()/2;
                    String key = str.substring(0, half);
                    String encrypted = str.substring(half);
                    for (int a = 0; a<encrypted.length(); a++){
                        decrypted += list.get(ciphers.get(list.indexOf(key.substring(a, a+1))).indexOf(encrypted.substring(a, a+1)));
                    }
                }
                else if (index%4 == 1){
                    int half = str.length()/2;
                    String key = str.substring(half);
                    String encrypted = str.substring(0, half);
                    for (int a = 0; a<encrypted.length(); a++){
                        decrypted += list.get(ciphers.get(list.indexOf(key.substring(a, a+1))).indexOf(encrypted.substring(a, a+1)));
                    }
                }
                else if (index%4 == 2){
                    for (int a = 0; a+1<str.length(); a+=2){
                        decrypted += list.get(ciphers.get(list.indexOf(str.substring(a, a+1))).indexOf(str.substring(a+1, a+2)));
                    }
                }
                else {
                    for (int a = 0; a+1<str.length(); a+=2){
                        decrypted += list.get(ciphers.get(list.indexOf(str.substring(a+1, a+2))).indexOf(str.substring(a, a+1)));
                    }
                }
                return decrypted;
            }
        }
        return "Failed to Decrypt";
    }
    
    public String createFilename(String filename){
        filename = filename.trim();
        String encrypted = "";
        boolean contains = true;
        while(contains){
            encrypted = encrypt(filename);
            contains = false;
            for (int a = 0; a<specialCharacters.length; a++){
                if (encrypted.contains(specialCharacters[a])){
                    contains = true;
                    break;
                }
            }
        }
        return encrypted;
    }
    
    /*
    public static void main (String[] args){
    Encryptor encryptor = new Encryptor();
    System.out.println(encryptor.createFilename("antoniok"));
    System.out.println(encryptor.createFilename("settings"));
    }
    /*
    */
    
    
}
