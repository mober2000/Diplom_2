package testcases;

import api.Api;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import pojo.createdorderdata.CreatedOrderData;
import pojo.ingridientdata.Ingridient;
import pojo.userinfodata.CreatedOrderMyUserData;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;

public class GetOrder {
    Api api = new Api();

    @Step("Создаем заказ для авторизованного пользователя, получаем список заказов авторизованного пользователя, сравниваем значения из ответа с введенными нами и убеждаемся что они совпадают")
    public void getOrderListAuthorization(List<String> hashIngredients, String bearerToken) {
        ValidatableResponse createOrderRequest = api.createOrderRequest(new Ingridient(hashIngredients), bearerToken);
        createOrderRequest.statusCode(200).assertThat().body("success", equalTo(true));
        CreatedOrderData getOrderDataRequest = createOrderRequest.extract().as(CreatedOrderData.class);

        String idOrder = getOrderDataRequest.getOrder().get_id();
        String statusOrder = getOrderDataRequest.getOrder().getStatus();
        String nameOrder = getOrderDataRequest.getOrder().getName();
        String createdAtOrder = getOrderDataRequest.getOrder().getCreatedAt();
        String updatedAtOrder = getOrderDataRequest.getOrder().getUpdatedAt();
        int numberOrder = getOrderDataRequest.getOrder().getNumber();

        ValidatableResponse getUserOrdersRequest = api.getCreatedOrdersRequest();
        getUserOrdersRequest.statusCode(200).assertThat().body("success", equalTo(true))
                .and().body("total", notNullValue());
        CreatedOrderMyUserData getOrderUserDataRequest = getUserOrdersRequest.extract().as(CreatedOrderMyUserData.class);

        assertEquals(idOrder, getOrderUserDataRequest.getOrders().get(0).get_id());
        assertEquals(hashIngredients, getOrderUserDataRequest.getOrders().get(0).getIngredients());
        assertEquals(statusOrder, getOrderUserDataRequest.getOrders().get(0).getStatus());
        assertEquals(nameOrder, getOrderUserDataRequest.getOrders().get(0).getName());
        assertEquals(createdAtOrder, getOrderUserDataRequest.getOrders().get(0).getCreatedAt());
        assertEquals(updatedAtOrder, getOrderUserDataRequest.getOrders().get(0).getUpdatedAt());
        assertEquals(numberOrder, getOrderUserDataRequest.getOrders().get(0).getNumber());
    }

    @Step("Создаем заказ для неавторизованного пользователя")
    public void getOrderListUnauthorized() {
        ValidatableResponse getUserOrdersRequest = api.getCreatedOrdersRequest();
        getUserOrdersRequest.statusCode(401).assertThat().body("success", equalTo(false)).and().body("message", equalTo("You should be authorised"));
    }
}
