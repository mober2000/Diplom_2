package testcasessteps;

import api.Api;
import io.restassured.response.ValidatableResponse;
import pojo.createdorderdata.CreatedOrderData;
import pojo.ingridientdata.IngredientData;
import pojo.ingridientdata.Ingridient;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class CreateOrder {

    List<String> hashIngredients = new ArrayList<>();
    Api api = new Api();
    String idFirstIngredient;
    String idSecondIngredient;
    String idThirdIngredient;

    public void addIngredients(){
        IngredientData getIngredientRequest = api.getIngredientListRequest();
        idFirstIngredient = getIngredientRequest.getData().get(0).get_id();
        idSecondIngredient = getIngredientRequest.getData().get(1).get_id();
        idThirdIngredient = getIngredientRequest.getData().get(2).get_id();
        hashIngredients.add(idFirstIngredient);
        hashIngredients.add(idSecondIngredient);
        hashIngredients.add(idThirdIngredient);
    }

    public void createAuthorizedOrder(String mail, String name, String bearerToken){
        ValidatableResponse createOrderRequest = api.createOrderRequest(new Ingridient(hashIngredients),bearerToken);
        createOrderRequest.statusCode(200).assertThat().body("success", equalTo(true));
        CreatedOrderData getOrderDataRequest = createOrderRequest.extract().as(CreatedOrderData.class);

        assertEquals(idFirstIngredient, getOrderDataRequest.getOrder().getIngredients().get(0).get_id());
        assertEquals(idSecondIngredient, getOrderDataRequest.getOrder().getIngredients().get(1).get_id());
        assertEquals(idThirdIngredient, getOrderDataRequest.getOrder().getIngredients().get(2).get_id());
        assertEquals(mail, getOrderDataRequest.getOrder().getOwner().getEmail());
        assertEquals(name, getOrderDataRequest.getOrder().getOwner().getName());
        assertEquals("done", getOrderDataRequest.getOrder().getStatus());
    }

    public void createUnauthorizedOrder(){
        ValidatableResponse createOrderRequest = api.createOrderRequest(new Ingridient(hashIngredients),"");
        createOrderRequest.statusCode(200).assertThat()
                .body("success", equalTo(true))
                .and().body("name", notNullValue())
                .and().body("order.number", notNullValue());
        int orderNumber = createOrderRequest.extract().path("order.number");
        assertTrue(orderNumber < 10000);
    }

    public void createNotIngredient(String bearerToken){
        ValidatableResponse createOrderRequest = api.createOrderRequest(new Ingridient(hashIngredients), bearerToken);
        createOrderRequest.statusCode(400).assertThat().body("success", equalTo(false)).and().body("message", equalTo("Ingredient ids must be provided"));
    }

    public void notCorrectedHashIngredient(String bearerToken){
        hashIngredients.add("11111111111111111111111111111111");
        ValidatableResponse createOrderRequest = api.createOrderRequest(new Ingridient(hashIngredients), bearerToken);
        createOrderRequest.statusCode(500);
    }

    public List<String> getHashIngredients() {
        return hashIngredients;
    }

}