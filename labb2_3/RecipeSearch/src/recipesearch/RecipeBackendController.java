package recipesearch;

import se.chalmers.ait.dat215.lab2.Recipe;
import se.chalmers.ait.dat215.lab2.SearchFilter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecipeBackendController {

    String cuisine;
    String mainIngredient;
    String difficulty;
    int maxPrice;
    int maxTime;

    //ska denna ha en instans av RecipeSearchController --> rsController.db


    public List<Recipe> getRecipes(){
        //creates searchfilter
        SearchFilter searchFilter = new SearchFilter(difficulty, maxTime, cuisine, maxPrice, mainIngredient);

        //TODO change return statement
        //returnerar en sorterad lista på recepten
        return new ArrayList<Recipe>();
    }

    public void setCuisine(String cuisine){

        switch(cuisine){
            case "Sverige":
                this.cuisine ="Sverige";
                break;

            case "Grekland":
                this.cuisine ="Grekland";
                break;

            case "Indien":
                this.cuisine ="Indien";
                break;

            case "Asien":
                this.cuisine ="Asien";
                break;

            case "Afrika":
                this.cuisine ="Afrika";
                break;

            case "Frankrike":
                this.cuisine ="Frankrike";
                break;
            }
        }



    public void setMainIngredient(String mainIngredient){
        switch (mainIngredient){
            case "meat":
                this.mainIngredient="meat";
                break;

            case "fish":
                this.mainIngredient="fish";
                break;

            case "chicken":
                this.mainIngredient="chicken";
                break;

            case "veg":
                this.mainIngredient="veg";
                break;
        }
    }


    public void setDifficulty(String difficulty){
        switch(difficulty){
            case "easy":
                break;

            case "medium":
                break;

            case "hard":
                break;


        }
    }

    public void setMaxPrice(int maxPrice){
        if(maxPrice > 0){
            //do something
        }
    }

    public void setMaxTime(int maxTime){
         ArrayList<Integer> times = new ArrayList<Integer>(Arrays.asList(10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 110, 120, 130, 140, 150));

    if(times.contains(maxTime)){
        //do something
    }


    }


}
