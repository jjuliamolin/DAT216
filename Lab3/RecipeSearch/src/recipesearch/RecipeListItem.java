package recipesearch;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import se.chalmers.ait.dat215.lab2.Recipe;

import java.io.IOException;

public class RecipeListItem extends AnchorPane {
    private RecipeSearchController parentController;
    private Recipe recipe;

    @FXML private Label recipeLabel;
    @FXML private Label recipeDesc;
    @FXML private Label recipeTime;
    @FXML private Label recipeCost;

    @FXML private ImageView recipeDiff;
    @FXML private ImageView recipeIngredient;
    @FXML private ImageView recipeCuisine;

    @FXML private ImageView recipeImage;



    public RecipeListItem(Recipe recipe, RecipeSearchController recipeSearchController){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("recipe_listitem.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.recipe = recipe;
        this.parentController = recipeSearchController;

        // Change label & image
        this.recipeLabel.setText(recipe.getName());
        this.recipeDesc.setText(recipe.getDescription());
        this.recipeTime.setText(recipe.getTime() + " minuter");
        this.recipeCost.setText(recipe.getPrice() + " kr");

        this.recipeImage.setImage(parentController.getSquareImage(recipe.getFXImage()));
        this.recipeIngredient.setImage(parentController.getIngredientImage(recipe.getMainIngredient()));
        this.recipeCuisine.setImage(parentController.getIngredientImage(recipe.getCuisine()));
        this.recipeDiff.setImage(parentController.getDifficultyImage(recipe.getDifficulty()));
    }


    @FXML
    protected void onClick(Event event){
        parentController.openRecipeView(recipe);
    }
}
