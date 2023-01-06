package order;

import order.ingridientdata.IngredientData;

import static io.restassured.RestAssured.given;

public class CreateOrder extends RestClient{

    public IngredientData getIngredientList()  {
        return reqSpec
                .get("ingredients")
                .body()
                .as(IngredientData.class);
    }


}
