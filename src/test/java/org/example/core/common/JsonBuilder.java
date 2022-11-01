package org.example.core.common;

import io.cucumber.datatable.DataTable;
import org.json.JSONArray;
import org.json.JSONObject;
import org.skyscreamer.jsonassert.JSONAssert;
import java.util.HashMap;
import java.util.List;

public class JsonBuilder {

    /**
     * This method to generate JSON Object String.
     * @param rows as Datatype:List<List<String>>
     * @return String
     */
    public static String generateJsonObjectString(List<List<String>> rows){
        JSONObject jsonObject = new JSONObject();
        //List<List<String>> rows = table.asLists(String.class);

        for (List<String> columns : rows) {
            Object value = null;
            if(columns.get(1).contains("int~")){
                value = Integer.parseInt(
                        columns.get(1).replace("int~","")
                );
            }else{
                value = columns.get(1);
            }
            jsonObject.put(columns.get(0),value);
        }

        return jsonObject.toString();
    }

    /**
     * This method to generate JSON Object String, by replacing variable values stored
     * @param table as Datatype:DataTable
     * @param storedVariables as Datatype:HashMap<String,Object>
     * @return String
     */
    public static String prepareJSONObjectFromDataTable(DataTable table,
                                                                    HashMap<String,Object> storedVariables){
        List<List<String>> rows = table.asLists(String.class);
        HashMap<Integer,String> columnsOrderMapping = new HashMap<Integer, String>();
        JSONObject jsonObject = new JSONObject();

        for(int i=0;i<rows.size();i++){

            if(i == 0){
                for(int j=0;j<rows.get(i).size();j++){
                    columnsOrderMapping.put(j,rows.get(i).get(j));
                }
            }else{
                List<String> record = rows.get(i);
                for(int k=0;k<record.size();k++){
                    Object actualValue = null;
                    String key = columnsOrderMapping.get(k);
                    String value = record.get(k);
                    value = HelperUtility.replaceVariables(value,
                            storedVariables);

                    if(value.contains("int~")){
                        value = value.replace("int~","");
                        actualValue = Integer.parseInt(value);
                    }else{
                        actualValue = value;
                    }

                    jsonObject.put(key,actualValue);
                }
            }
        }
        return jsonObject.toString();
    }

    /**
     * This method to generate JSON Array String, by replacing variable values stored
     * @param table as Datatype:DataTable
     * @param storedVariables as Datatype:HashMap<String,Object>
     * @return String
     */
    public static String prepareJSONArrayFromDataTable(DataTable table,
                                                        HashMap<String,Object> storedVariables){
        List<List<String>> rows = table.asLists(String.class);
        HashMap<Integer,String> columnsOrderMapping = new HashMap<Integer, String>();
        JSONObject jsonObject = null;
        JSONArray jsonArray = new JSONArray();

        for(int i=0;i<rows.size();i++){

            if(i == 0){
                for(int j=0;j<rows.get(i).size();j++){
                    columnsOrderMapping.put(j,rows.get(i).get(j));
                }
            }else{
                List<String> record = rows.get(i);
                jsonObject = new JSONObject();
                for(int k=0;k<record.size();k++){
                    Object actualValue = null;
                    String key = columnsOrderMapping.get(k);
                    String value = record.get(k);
                    value = HelperUtility.replaceVariables(value,
                            storedVariables);

                    if(value.contains("int~")){
                        value = value.replace("int~","");
                        actualValue = Integer.parseInt(value);
                    }else{
                        actualValue = value;
                    }
                    jsonObject.put(key,actualValue);
                }
                jsonArray.put(jsonObject);
            }
        }
        return jsonArray.toString();
    }

    /**
     * This method to check expected JSON Array records exists in actual JSON Array
     * @param expectedJsonArrayStr first parameter, as Datatype:DataTable
     * @param actualJsonArrayStr second parameter,as Datatype:HashMap<String,Object>
     * @return Boolean
     */
    public static Boolean compareTwoJSONArrayInlineRecordExists(String expectedJsonArrayStr,
                                                             String actualJsonArrayStr){

        JSONArray expectedJsonArray = new JSONArray(expectedJsonArrayStr);
        JSONArray actualJsonArray = new JSONArray(actualJsonArrayStr);
        Boolean resultFlag = false;

        for(int i=0;i<expectedJsonArray.length();i++){
            JSONObject expJsonObj = expectedJsonArray.getJSONObject(i);
            for(int j=0;j<actualJsonArray.length();j++){
                JSONObject actualJsonObj = actualJsonArray.getJSONObject(j);
                try{
                    JSONAssert.assertEquals(expJsonObj,actualJsonObj,false);
                    resultFlag = true;
                    break;
                }catch (java.lang.AssertionError je){
                    resultFlag = false;
                }

            }
        }
        return resultFlag;

    }
}
