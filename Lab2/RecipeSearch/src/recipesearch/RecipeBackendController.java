package recipesearch;

import se.chalmers.ait.dat215.lab2.Recipe;
import se.chalmers.ait.dat215.lab2.RecipeDatabase;
import se.chalmers.ait.dat215.lab2.SearchFilter;

import java.util.Arrays;
import java.util.List;

public class RecipeBackendController {
    private final static String[] CUISINE = {"Sverige", "Grekland", "Indien", "Asien", "Afrika", "Frankrike"};
    private final static String[] MAININGREDIENT = {"Kött", "Fisk", "Kyckling", "Vegetarisk"};
    private final static String[] DIFFICULITY = {"Lätt", "Mellan", "Svår"};
    private final static Integer[] MAXTIME = {10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 110, 120, 130, 140, 150};

    private String cuisine = null,
            mainIngredient = null,
            difficulty = null;

    private int maxPrice = 0,
            maxTime = 0;

    public List<Recipe> getRecipes(){
        RecipeDatabase db = RecipeDatabase.getSharedInstance();
        SearchFilter search = new SearchFilter(difficulty, maxTime, cuisine, maxPrice, mainIngredient);

        return db.search(search);
    }

    public void setCuisine(String cuisine){
        this.cuisine = (Arrays.asList(CUISINE).contains(cuisine)) ? cuisine : null;
    }

    public void setMainIngredient(String mainIngredient){
        this.mainIngredient = (Arrays.asList(MAININGREDIENT).contains(mainIngredient)) ? mainIngredient : null;
    }

    public void setDifficulty(String difficulty){
        this.difficulty = (Arrays.asList(DIFFICULITY).contains(difficulty)) ? difficulty : null;
    }

    public void setMaxPrice(int maxPrice){
        this.maxPrice = Math.max(maxPrice, 0);
    }

    public void setMaxTime(Integer maxTime){
        this.maxTime = (Arrays.asList(MAXTIME).contains(maxTime)) ? maxTime : 0;
    }
}
