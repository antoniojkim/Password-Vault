/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package Encryptor;

import Main.p;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**

@author Antonio
*/
public final class Encryptor {
    
    public static final String keyName = "ΞYΟlρLαβzX";
    public static final String settingsName = "-$aCνjVehE";
    public static final String salt = "σΣ#~5";
    public static final String backupEnd = "-$aCνjVehE";
    public static final String fileFormat = ".txt";
    
    public Encryptor(){
        open(true);
    }
    public Encryptor(boolean extra){
        open(extra);
    }
    
    private boolean opened = false;
    
    private String[] characters;
    
    private String[][] cipher;
    
    private BufferedReader br;
    private void open(boolean extra){
        if (!opened){
//            File file = new File("./Vault Files/"+keyName+fileFormat);
//            if (file.exists()){
if (extra){
    try{
        br = new BufferedReader (new FileReader("./Vault Files/"+keyName+fileFormat));
        if (br.readLine() == null){
            br = new BufferedReader (new InputStreamReader(getClass().getResourceAsStream("keys"+fileFormat), "UTF-8"));
        }
        else{
            br = new BufferedReader (new FileReader("./Vault Files/"+keyName+fileFormat));
        }
    }catch(FileNotFoundException e){
        try {
            br = new BufferedReader (new InputStreamReader(getClass().getResourceAsStream("keys"+fileFormat), "UTF-8"));
        } catch (UnsupportedEncodingException ex) {}
    } catch (IOException ex) {
        try {
            br = new BufferedReader (new InputStreamReader(getClass().getResourceAsStream("keys"+fileFormat), "UTF-8"));
        } catch (UnsupportedEncodingException ex1) {}
    }
}
else{
    br = new BufferedReader (new InputStreamReader(getClass().getResourceAsStream("keys"+fileFormat)));
}
try{
    List<String> lines = new ArrayList<>();
    String line = br.readLine();
    while (line != null){
        lines.add(line.replaceAll("\uFFFD", "").replaceAll(" ", ""));
        line = br.readLine();
    }
    characters = lines.get(0).split("");
    cipher = new String[characters.length][characters.length];
    for (int a = 1; a<lines.size(); a++){
        cipher[a-1] = lines.get(a).split("");
    }
    opened = true;
}catch(IOException e){}
//            }
        }
    }
    
    public String getEncryption(String str){
        return encrypt(replaceExtra(str));
    }
    public String getSimpleEncryption(String str){
        return hash(replaceExtra(str));
    }
    public String getAdvancedEncryption(String str){
        return encrypt(hash(replaceExtra(str)));
    }
    public String replaceExtra(String str){
        str = str.trim();
        str = str.replaceAll("\\uFFFD", "");
        str = str.replaceAll(" ", "");
        str = str.replaceAll("/", "<s>");
        str = str.replaceAll("\"", "<q>");
        str = str.replaceAll("\n", "<n>");
        str = str.replaceAll("\r", "<r>");
        return str;
    }
    private String encrypt(String str){
        if (!opened){
            open(true);
        }
        if (opened){
            if (str.equals("")){
                return "";
            }
            String key = "";
            String encrypted = "";
            List<String> list = new ArrayList<>(Arrays.asList(characters));
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
                        System.exit(1);
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
                        System.exit(1);
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
                        System.exit(1);
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
                        System.exit(1);
                    }
                }
                return list.get(index)+encrypted;
            }
        }
        return "Failed to Encrypt - "+str;
    }
    
    public String getDecryption(String str){
        return returnNormal(decrypt(replaceExtra(str)));
    }
    public String getSimpleDecryption(String str){
        return returnNormal(unhash(replaceExtra(str)));
    }
    public String getAdvancedDecryption(String str){
        return returnNormal(unhash(decrypt(replaceExtra(str))));
    }
    public String returnNormal(String str){
        str = str.trim();
        str = str.replaceAll("", " ");
        str = str.replaceAll("<s>", "/");
        str = str.replaceAll("<q>", "\"");
        str = str.replaceAll("<n>", "\n");
        str = str.replaceAll("<r>", "\r");
        return str;
    }
    private String decrypt (String str){
        if (!opened){
            open(true);
        }
        if (opened){
            if (str.equals("")){
                return "";
            }
            String decrypted = "";
            List<String> list = new ArrayList<>(Arrays.asList(characters));
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
                        try{
                            decrypted += list.get(ciphers.get(list.indexOf(str.substring(a, a+1))).indexOf(str.substring(a+1, a+2)));
                        }catch(ArrayIndexOutOfBoundsException e){
                            System.out.println(str);
                            System.out.println(str.substring(a, a+2));
                            System.exit(1);
                        }
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
    
    private String hash(String str){
        if (!opened){
            open(true);
        }
        if (opened){
            String hash = "";
            List<String> list = new ArrayList<>(Arrays.asList(characters));
            for (int a = 0; a<str.length(); a++){
                try{
                    hash += cipher[a%list.size()][list.indexOf(str.substring(a, a+1))];
                }catch(ArrayIndexOutOfBoundsException e){
                    System.out.println(str);
                    System.out.println(str.substring(a, a+1));
                    System.exit(1);
                }
            }
            return hash;
        }
        return "Could not Hash - "+str;
    }
    private String unhash(String hash){
        if (!opened){
            open(true);
        }
        if (opened){
            String str = "";
            List<String> list = new ArrayList<>(Arrays.asList(characters));
            for (int a = 0; a<hash.length(); a++){
                for (int b = 0; b<cipher.length; b++){
                    if (cipher[a%list.size()][b].equals(hash.substring(a, a+1))){
                        str += list.get(b);
                        break;
                    }
                }
            }
            return str;
        }
        return "Could not Unhash - "+hash;
    }
    
    final private String[] forbidden = {"\"", "*", "/", ":", "<", ">", "?", "\\", "|"};
    public String generateRandom(int size){
        String encrypted = "";
        while(encrypted.length() < size){
            int r = p.randomint(0, characters.length-1);
            if (Search.binarySearch(forbidden, characters[r]) == -1){
                encrypted += characters[r];
            }
        }
        return encrypted;
    }
    
    public void resetDefault(){
        File file = new File("./Vault Files/"+keyName+fileFormat);
        if (file.exists()){
            Encryptor encrypt = new Encryptor();
            Encryptor encryptdef = new Encryptor(false);
            String[] files = new File("./Vault Files").list();
            for (int a = 0; a<files.length; a++){
                if (files[a].endsWith(fileFormat) && !files[a].equals(keyName+fileFormat)){
                    try{
                        BufferedReader br = p.filereader("./Vault Files/"+files[a]);
                        String login = br.readLine();
                        String data = br.readLine();
                        br.close();
                        PrintWriter pr = p.printwriter("./Vault Files/"+files[a]);
                        login = encrypt.getAdvancedDecryption(login);
                        if (!files[a].equals(""+settingsName+fileFormat)){
                            String[] logins = login.split(salt);
                            logins[1] = encrypt.getSimpleDecryption(logins[1]);
                            logins[1] = encryptdef.getSimpleEncryption(logins[1]);
                            login = logins[0]+salt+logins[1];
                        }
                        login = encryptdef.getAdvancedEncryption(login);
                        pr.println(login);
                        if (data != null){
                            data = encrypt.getAdvancedDecryption(data);
                            data = encryptdef.getAdvancedEncryption(data);
                            pr.println(data);
                        }
                        pr.close();
                    }catch(IOException e){}
                }
            }
            if (!file.delete()){
                PrintWriter pr = p.printwriter(file.getPath());
                pr.close();
            }
        }
        else{
            file = new File("./Vault Files/Backup/");
            if (file.isDirectory()){
                File[] files = file.listFiles();
                for (int a = 0; a<files.length; a++){
                    if (!files[a].getName().equals(keyName+fileFormat)){
                        try{
                            BufferedReader br = p.filereader("./Vault Files/Backup/"+files[a].getName());
                            String login = br.readLine();
                            String data = br.readLine();
                            PrintWriter pr = p.printwriter("./Vault Files/"+files[a].getName().replaceAll(backupEnd, ""));
                            pr.println(login);
                            if (data != null){
                                pr.println(data);
                            }
                            pr.close();
                        }catch(IOException e){}
                    }
                }
            }
        }
    }
    public void reset(){
        changeKeys();
    }
    private void changeKeys(){
        Encryptor encryptorOld = new Encryptor(false);
        boolean backup = !new File("./Vault Files/"+keyName+fileFormat).exists();
        generateKey("./Vault Files/"+keyName+fileFormat);
        Encryptor encryptorNew = new Encryptor();
        File file = new File("./Vault Files");
        String[] fileList = file.list();
        for (int a = 0; a<fileList.length; a++){
            if (fileList[a].endsWith(fileFormat) && !fileList[a].equals(""+keyName+fileFormat)){
                try{
                    BufferedReader br = p.filereader("./Vault Files/"+fileList[a]);
                    String login = br.readLine();
                    String data = br.readLine();
                    br.close();
                    if (backup){
                        file = new File("./Vault Files/Backup/");
                        file.mkdirs();
                        PrintWriter pr1 = p.printwriter("./Vault Files/Backup/"+fileList[a].substring(0, fileList[a].length()-4)+"9οΗΑΥ∀ςΖλθ"+fileFormat);
                        pr1.println(login);
                        if (data != null){
                            pr1.println(data);
                        }
                        pr1.println(data);
                        pr1.close();
                    }
                    PrintWriter pr2 = p.printwriter("./Vault Files/"+fileList[a]);
                    login = encryptorOld.getAdvancedDecryption(login);
                    if (!fileList[a].equals(""+settingsName+fileFormat)){
                        String[] logins = login.split(salt);
                        logins[1] = encryptorOld.getSimpleDecryption(logins[1]);
                        logins[1] = encryptorNew.getSimpleEncryption(logins[1]);
                        login = logins[0]+salt+logins[1];
                    }
                    login = encryptorNew.getAdvancedEncryption(login);
                    pr2.println(login);
                    if (data != null){
                        data = encryptorOld.getAdvancedDecryption(data);
                        data = encryptorNew.getAdvancedEncryption(data);
                        pr2.println(data);
                    }
                    pr2.close();
                }catch(IOException e){}
            }
        }
    }
    public void generateKey(String path, String... additional){
        try {
            String line = new BufferedReader (new InputStreamReader(getClass().getResourceAsStream("All Characters"+fileFormat), "UTF-8")).readLine().replaceAll("\u00E4", "");
            if (!line.substring(0, 1).equals("a")){
                line = line.substring(1);
            }
            String[] characters = line.split("");
            System.out.println(characters.length);
            System.out.println(line);
            PrintWriter pr = p.printwriter(path);
            List<String> list = new ArrayList<>();
            for (int a = -1; a<characters.length; a++){
                list.addAll(Arrays.asList(characters));
                list.addAll(Arrays.asList(additional));
                while(!list.isEmpty()){
                    int r = p.randomint(0, list.size()-1);
                    pr.print(list.get(r));
                    list.remove(r);
                }
                pr.println();
            }
            pr.close();
        } catch (IOException ex) {}
    }
    public String createSalt(int size){
        String salt = "";
        if (!opened){
            open(true);
        }
        if (opened){
            for (int a = 0; a<size; a++){
                salt += characters[p.randomint(0, characters.length-1)];
            }
        }
        return salt;
    }
    
    
}
