package pojo.ingridientdata;

import java.util.List;

public class Ingridient {
    private List<String> ingredients;

    public Ingridient(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }
}
