/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package Encryptor;

import Main.p;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
        open(true);
    }
    public Encryptor(boolean extra){
        open(extra);
    }
    
    private boolean opened = false;
    
    private String[] normalCharacters;
    private String[] specialCharacters;
    
    private String[][] cipher;
    
    private BufferedReader br;
    private void open(boolean extra){
        if (!opened){
            if (extra){
                try{
                    br = new BufferedReader (new FileReader("./Vault Files/$6oØvE[=XW;{µR.txt"));
                }catch(FileNotFoundException e){
                    br = new BufferedReader (new InputStreamReader(getClass().getResourceAsStream("keys.txt")));
                }
            }
            else{
                br = new BufferedReader (new InputStreamReader(getClass().getResourceAsStream("keys.txt")));
            }
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
            open(true);
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
            open(true);
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
    
    public void reset(){
        changeKeys();
    }
    private void changeKeys(){
        File file = new File("./Vault Files/$6oØvE[=XW;{µR.txt");
        boolean backup = !file.exists();
        Encryptor encryptorOld = new Encryptor(false);
        PrintWriter pr = p.printwriter("./Vault Files/$6oØvE[=XW;{µR.txt");
        for (int a = 0; a<normalCharacters.length; a++){
            pr.print(normalCharacters[a]+createSalt());
        }
        pr.println();
        for (int a = 0; a<specialCharacters.length; a++){
            pr.print(specialCharacters[a]+createSalt());
        }
        int length = 0;
        if (!specialCharacters[specialCharacters.length-1].equals("'")){
            pr.print("'"+createSalt());
            length = normalCharacters.length+specialCharacters.length+1;
        }
        else{
            length = normalCharacters.length+specialCharacters.length;
        }
        pr.println();
        List<String> list = new ArrayList<>();
        for (int a = 0; a<length; a++){
            list.addAll(Arrays.asList(normalCharacters));
            list.addAll(Arrays.asList(specialCharacters));
            if (!specialCharacters[specialCharacters.length-1].equals("'")){
                list.add("'");
            }
            while(!list.isEmpty()){
                int r = p.randomint(0, list.size()-1);
                pr.print(list.get(r)+createSalt());
                list.remove(r);
            }
            pr.println();
        }
        pr.close();
        Encryptor encryptorNew = new Encryptor();
        file = new File("./Vault Files");
        String[] fileList = file.list();
        for (int a = 0; a<fileList.length; a++){
            if (fileList[a].endsWith(".txt") && !fileList[a].equals("$6oØvE[=XW;{µR.txt")){
                try{
                    BufferedReader br = p.filereader("./Vault Files/"+fileList[a]);
                    String data = br.readLine();
                    br.close();
                    if (backup){
                        file = new File("./Vault Files/Backup/");
                        file.mkdirs();
                        PrintWriter pr1 = p.printwriter("./Vault Files/Backup/"+fileList[a].substring(0, fileList[a].length()-4)+"RqfSFfpZlv8F.txt");
                        pr1.println(data);
                        pr1.close();
                    }
                    data = encryptorOld.decrypt(data);
                    data = encryptorNew.encrypt(data);
                    PrintWriter pr2 = p.printwriter("./Vault Files/"+fileList[a]);
                    pr2.println(data);
                    pr2.close();
                }catch(IOException e){}
            }
        }
    }
    private String createSalt(){
        String salt = "";
        for (int a = 0; a<3; a++){
            if (Math.random() > 0.5){
                salt += specialCharacters[p.randomint(0, specialCharacters.length-1)];
            }
            else{
                salt += normalCharacters[p.randomint(0, normalCharacters.length-1)];
            }
        }
        return salt;
    }
    
    public void resetDefault(){
        File file = new File("./Vault Files/$6oØvE[=XW;{µR.txt");
        if (file.exists()){
            Encryptor encrypt = new Encryptor();
            Encryptor encryptdef = new Encryptor(false);
            String[] files = new File("./Vault Files").list();
            for (int a = 0; a<files.length; a++){
                if (files[a].endsWith(".txt") && !files[a].equals("$6oØvE[=XW;{µR.txt")){
                    try{
                        BufferedReader br = p.filereader("./Vault Files/"+files[a]);
                        String data = br.readLine();
                        data = encrypt.decrypt(data);
                        data = encryptdef.encrypt(data);
                        PrintWriter pr = p.printwriter("./Vault Files/"+files[a]);
                        pr.println(data);
                        pr.close();
                    }catch(IOException e){}
                }
            }
            file.delete();
        }
        else{
            file = new File("./Vault Files/Backup/");
            if (file.isDirectory()){
                File[] files = file.listFiles();
                for (int a = 0; a<files.length; a++){
                    if (!files[a].getName().equals("$6oØvE[=XW;{µR.txt")){
                        try{
                            BufferedReader br = p.filereader("./Vault Files/Backup/"+files[a].getName());
                            String data = br.readLine();
                            PrintWriter pr = p.printwriter("./Vault Files/"+files[a].getName().replaceAll("RqfSFfpZlv8F", ""));
                            pr.println(data);
                            pr.close();
                        }catch(IOException e){}
                    }
                }
            }
        }
    }
    
    /*
    public static void main (String[] args){
        Encryptor encrypt = new Encryptor();
        encrypt.reset();
//        encrypt.resetDefault();
    }
    /*
    */
    
    
}
