package app.ohmysaudi.themealdb.models;

import java.util.List;

public class CategoryResponce {
    private final List<Category> categories;

    public CategoryResponce(List<Category> categories) {
        this.categories = categories;
    }

    public List<Category> getCategories() {
        return categories;
    }
}

