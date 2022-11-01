package org.example.core.common;

import io.cucumber.datatable.DataTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HelperUtility {

    /**
     * This method to replace already variable values in data from DataTable
     * @param table first parameter, as Datatype:DataTable
     * @param storedVariables second parameter,as Datatype:HashMap<String,Object>
     * @return List<List<String>>
     */
    public static List<List<String>> replaceVariables(DataTable table, HashMap<String,Object> storedVariables){
        List<List<String>> rows = table.asLists(String.class);
        List<List<String>> result = new ArrayList<>();

        for(int i=0;i<rows.size();i++){
            String key = rows.get(i).get(0);
            String value = rows.get(i).get(1);
            String storedKey = value.replace("[","").replace("]","");
            String storedValue =  (String)storedVariables.get(storedKey);
            if (value.equals("["+storedKey+"]")){
                //value = value.replace("["+value+"]",storedValue);
                List<String> temp = new ArrayList<>();
                temp.add(key);
                temp.add(storedValue);
                //rows.remove(i);
                result.add(temp);
            }else{
                List<String> temp = new ArrayList<>();
                temp.add(key);
                temp.add(value);
                result.add(temp);
            }
        }
        return result;
    }

    /**
     * This method to replace already variable values in String
     * @param stringToReplace first parameter, as Datatype:String
     * @param storedVariables second parameter,as Datatype:HashMap<String,Object>
     * @return String
     */
    public static String replaceVariables(String stringToReplace, HashMap<String,Object> storedVariables){

        for(String key:storedVariables.keySet()){
            String value = (String)storedVariables.get(key);
            stringToReplace = stringToReplace.replace("["+key+"]",value);
        }
        return stringToReplace;
    }


}
