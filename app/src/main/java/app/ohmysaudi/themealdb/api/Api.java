package app.ohmysaudi.themealdb.api;


import app.ohmysaudi.themealdb.models.CategoryResponce;
import app.ohmysaudi.themealdb.models.FilteredResponce;
import app.ohmysaudi.themealdb.models.DetailedListOfMealsResponce;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api {


    /*
    * Get Latest 10 Meal Object as responce
    * https://www.themealdb.com/api/json/v1/1/latest.php
    * */
    @GET("latest.php")
    Call<DetailedListOfMealsResponce> getLatestMeals();

    /*
    * Return all 12 categories with thumbnail and details.
    * https://www.themealdb.com/api/json/v1/1/categories.php
    * */
    @GET("categories.php")
    Call<CategoryResponce> getCategories();

    /*
    * Return List of links with ids of filtered by one category;
    * https://www.themealdb.com/api/json/v1/1/filter.php?c=Seafood
    * */
    @GET("filter.php")
    Call<FilteredResponce> getItemsFilterByCategory(
            @Query("c") String category
    );

    /*
    * Return a random Meal List Object
    * https://www.themealdb.com/api/json/v1/1/random.php
    * */
    @GET("random.php")
    Call<DetailedListOfMealsResponce> getRandomMeal();

    /*
     * Return The Details of the meals find by id;
     * https://www.themealdb.com/api/json/v1/1/lookup.php?i=52772
     * */
    @GET("lookup.php")
    Call<DetailedListOfMealsResponce> findItemByID(
            @Query("i") String id
    );

    /*
    * Return the Details of Meals find by name
    * https://www.themealdb.com/api/json/v1/1/search.php?s=Arrabiata
    * */
    @GET("search.php")
    Call<DetailedListOfMealsResponce> findItemByName(
            @Query("s") String name
    );

    /*
    * Return List of Links with ids of filtered by one main ingredient
    * https://www.themealdb.com/api/json/v1/1/filter.php?i=chicken
    * */
    @GET("filter.php")
    Call<FilteredResponce> getItemsFilterByIngredient(
            @Query("i") String ingredient
    );

    /*
    * Return List of Links with ids of filtered by one main area name
    * https://www.themealdb.com/api/json/v1/1/filter.php?a=Canadian
    * */
    @GET("filter.php")
    Call<FilteredResponce> getItemsFilterByArea(
            @Query("a") String ingredient
    );

}
