package app.ohmysaudi.themealdb.models;

public class Ingredients {
    private String ingrTitle;
    private String ingrImage;

    public Ingredients(String ingrTitle, String ingrImage) {
        this.ingrTitle = ingrTitle;
        this.ingrImage = ingrImage;
    }

    public String getIngrTitle() {
        return ingrTitle;
    }

    public String getIngrImage() {
        return ingrImage;
    }
}
