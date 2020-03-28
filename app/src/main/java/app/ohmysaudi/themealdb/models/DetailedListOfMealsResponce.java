package app.ohmysaudi.themealdb.models;

import java.util.List;

public class DetailedListOfMealsResponce {

    private List<Meal> meals;

    public DetailedListOfMealsResponce(List<Meal> meals) {
        this.meals = meals;
    }

    public List<Meal> getMeals() {
        return meals;
    }

}
