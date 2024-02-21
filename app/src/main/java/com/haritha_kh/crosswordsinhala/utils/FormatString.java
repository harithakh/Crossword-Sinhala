package com.haritha_kh.crosswordsinhala.utils;

public class FormatString {

    // turning a 2d array into a linear String. places "n" for empty strings.
    public String LinearStringMaker(String[][] userInputs){
        String linearString = "";

        for(int i=0; i<7; i++){
            for(int j=0; j<7; j++){
                if(userInputs[i][j].isEmpty()){
                    linearString = linearString.concat("n,");
                } else {
                    linearString = linearString.concat(userInputs[i][j] + ",");
                }
            }
        }
        return linearString.substring(0,linearString.length()-1); //remove the last comma.
    }

    // create a 1D string array using the String.
    public String[] stringArrayMaker(String linearString){
        String[] temp= linearString.split(",");
        // replacing "n" with empty string.
        for(int i=0; i<49; i++){
            if(temp[i].equals("n")){
                temp[i] = "";
            }
        }
        return temp;
    }
}
