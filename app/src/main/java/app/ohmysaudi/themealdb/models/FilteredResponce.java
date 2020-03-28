package app.ohmysaudi.themealdb.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class FilteredResponce {

    @SerializedName("meals")
    @Expose
    private List<ListItemLink> meals = null;

    public List<ListItemLink> getListofLinks() {
        return meals;
    }

    public void setListofLinks(List<ListItemLink> meals) {
        this.meals = meals;
    }


}
