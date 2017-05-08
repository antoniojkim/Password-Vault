/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package Encryptor;

/**

@author Antonio
*/
public class Search {
    
    public static int binarySearch(String[] array, String item){
        if (array.length > 1){
            if (array[1].compareTo(array[0]) > 0){
                return binarySearch(array, 0, array.length-1, item, true);
            }
            return binarySearch(array, 0, array.length-1, item, false);
        }
        return 0;
    }
    private static int binarySearch(String[] array, int low, int high, String item, boolean ascending){
//        count++;
        if (high < low){
            return -1;
        }
        int mid = low+(high-low)/2;
        if (array[mid].equals(item)){
            return mid;
        }
        else if (array[mid].compareTo(item) < 0){
            if (ascending){
                return binarySearch(array, mid+1, high, item, true);
            }
            else{
                return binarySearch(array, low, mid-1, item, false);
            }
        }
        else{
            if (ascending){
                return binarySearch(array, low, mid-1, item, true);
            }
            else{
                return binarySearch(array, mid+1, high, item, false);
            }
        }
    }
    
}
