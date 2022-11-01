package org.example.core.api;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import net.serenitybdd.core.environment.EnvironmentSpecificConfiguration;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.util.EnvironmentVariables;
import org.example.core.common.HelperUtility;
import org.example.core.common.JsonBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.skyscreamer.jsonassert.JSONAssert;

import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ApiBaseStepDefinitions {

    private Response response;
    private String payload;
    private HashMap<String, Object> queryParameters = null;
    private EnvironmentVariables environmentVariables;
    private String apiBaseUrl =  null;

    private HashMap<String,Object> storedVariables =  new HashMap<>();


    @Before
    public void setUp(){

        apiBaseUrl =  EnvironmentSpecificConfiguration.from(environmentVariables)
                .getProperty("api.base.url");

    }


    /**
     * This step definition method store variable values for later use in other steps.
     * @param value This is the first parameter , value to store
     * @param variable This is the second parameter, variable assign to value
     * @return void
     */
    @Given("Store a value:{string} in Variable:{string}")
    public void addVariableValue(Object value,String variable)  {
        variable = variable.replace("[","")
                            .replace("]","");
        this.storedVariables.put(variable,value);
    }
    /**
     * This step definition method to prepare JSON Object string and assign it to payload variable.
     * @param table Cucumber DataTable type
     * @return void
     */
    @Given("Prepare Json Object payload")
    public void payload(DataTable table)  {
        List<List<String>> rows = HelperUtility.replaceVariables(table,this.storedVariables);
        this.payload = JsonBuilder.generateJsonObjectString(rows);
    }

    /**
     * This step definition method to assign JSON Object string to payload variable.
     * @param jsonString String type
     * @return void
     */
    @Given("^\\Set Json Object String as payload:'(.*)'$")
    public void payload(String jsonString)  {
        this.payload = jsonString;
    }


    /**
     * This step definition method to prepare query parameter.
     * @param table Cucumber DataTable type
     * @return void
     */
    @Given("Prepare Query Parameters")
    public void prepareQueryParameter(DataTable table)  {
        this.queryParameters = new HashMap<>();
        List<List<String>> rows = HelperUtility.replaceVariables(table,this.storedVariables);
        for(List<String> columns : rows){
            String key = columns.get(0);
            String value = (String)columns.get(1);

            if(value.contains("int~")){
                value = value.replace("int~","");
                this.queryParameters.put(key,Integer.parseInt(value));
            }else{
                this.queryParameters.put(key,value);
            }
        }
    }

    /**
     * This step definition method to send POST Api call.
     * @param endpoint String
     * @return void
     */
    @Then("Send POST Request to endpoint:{string}")
    public void sendPostRequest(String endpoint) {
        response = SerenityRest.given()
                    .contentType("application/json")
                    .header("Content-Type", "application/json")
                    .body(this.payload)
                    .when()
                    .post(apiBaseUrl+endpoint);
    }

    /**
     * This step definition method to send PUT Api call.
     * @param endpoint String
     * @return void
     */
    @Then("Send PUT Request to endpoint:{string}")
    public void sendPutRequest(String endpoint) {
        response = SerenityRest.given()
                .contentType("application/json")
                .header("Content-Type", "application/json")
                .body(this.payload)
                .when()
                .put(apiBaseUrl+endpoint);
    }

    /**
     * This step definition method to send GET Api call.
     * @param endpoint String
     * @return void
     */
    @Then("Send GET Request to endpoint:{string}")
    public void sendGetRequest(String endpoint) {
        if(this.queryParameters == null){
            this.queryParameters = new HashMap<>();
        }
        response = SerenityRest.given()
                .contentType("application/json")
                .header("Content-Type", "application/json")
                .queryParams(this.queryParameters)
                .when()
                .get(apiBaseUrl+endpoint);
    }

    /**
     * This step definition method to send DELETE Api call.
     * @param endpoint String
     * @return void
     */
    @Then("Send DELETE Request with payload to endpoint:{string}")
    public void sendDELETERequest(String endpoint) {
        response = SerenityRest.given()
                .contentType("application/json")
                .header("Content-Type", "application/json")
                .body(this.payload)
                .when()
                .delete(apiBaseUrl+endpoint);
    }

    /**
     * This step definition method to check HttpStatus Code.
     * @param status Integer
     * @return void
     */
    @And("The API should return status {int}")
    public void verifyResponse(int status) {
        System.out.println(this.response.body().asString());
        SerenityRest.restAssuredThat(response -> response.statusCode(status));
    }

    /**
     * This step definition method to verify API JSON Object response from Cucumber DataTable.
     * @param dataTable
     * @return void
     */
    @And("Validate the JSON Object Response")
    public void verifyJsonObjResponseContent(DataTable dataTable) {

        List<List<String>> expectedResults = null;
        String responseStr = null;
        try {
            expectedResults = HelperUtility.replaceVariables(dataTable,this.storedVariables);
            responseStr = this.response.body().asString();
            JSONObject actualResponse = new JSONObject(responseStr);
            for (List<String> columns : expectedResults) {
                if(columns.get(1).contains("int~")){
                    String expectedValueStr = columns.get(1).replace("int~","");
                    assertTrue (Integer.parseInt(expectedValueStr) ==
                            actualResponse.getInt(columns.get(0)));
                }
            }
        } catch (JSONException e) {
            assertTrue(false);
        }

    }

    /**
     * This step definition method to verify API String response from Cucumber DataTable.
     * @param dataTable
     * @return void
     */
    @And("Validate the String Response")
    public void verifyStringResponseContent(DataTable dataTable){
        List<List<String>> rows = null;
        String responseStr = null;

        rows = dataTable.asLists(String.class);
        responseStr = this.response.body().asString();

        for (List<String> columns : rows) {
            String expectedStr = HelperUtility.replaceVariables(columns.get(0),this.storedVariables);
            expectedStr = expectedStr.replace("int~","");
            assertEquals (expectedStr, responseStr);
        }
    }

    /**
     * This step definition method to verify expected records in API JSON Object response from Cucumber DataTable.
     * @param dataTable
     * @return void
     */
    @And("Validate JSON Object inline records in api response")
    public void validateInlineRecordsInJSONObejct(DataTable dataTable){
        String expectedResult =JsonBuilder.prepareJSONObjectFromDataTable(dataTable,
                this.storedVariables);
        String responseStr = this.response.body().asString();
        JSONAssert.assertEquals(expectedResult, responseStr, false);
    }

    /**
     * This step definition method to verify API JSON Array response from Cucumber DataTable.
     * @param dataTable
     * @return void
     */
    @And("Validate JSON Array inline records in api response")
    public void validateInlineRecordsInJSONArray(DataTable dataTable){
        String expectedResult =JsonBuilder.prepareJSONArrayFromDataTable(dataTable,
                this.storedVariables);
        String responseStr = this.response.body().asString();
        Boolean resultFlag =JsonBuilder.compareTwoJSONArrayInlineRecordExists(expectedResult,responseStr);
        assertTrue(resultFlag);
    }

    /**
     * This step definition method to verify API JSON Array response count from Cucumber DataTable.
     * @param count integer
     * @return void
     */
    @And("Validate JSON Array Response count:{int}")
    public void validateIJSONArrayCount(int count){
        String responseStr = this.response.body().asString();
        JSONArray actualJsonArray = new JSONArray(responseStr);
        if(actualJsonArray.length() == count){
            assertTrue(true);
        }else{
            assertTrue(false);
        }
    }
}
