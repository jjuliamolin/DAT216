
package recipesearch;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import se.chalmers.ait.dat215.lab2.Recipe;
import se.chalmers.ait.dat215.lab2.RecipeDatabase;


public class RecipeSearchController implements Initializable {

    private RecipeDatabase db = RecipeDatabase.getSharedInstance();
    private RecipeBackendController backendController = new RecipeBackendController();

    @FXML private SplitPane searchPane;

    @FXML private FlowPane resultFlowPane;
    @FXML private ComboBox mainIngredient, cuisine;

    @FXML private RadioButton allaRadio, easyRadio, mediumRadio, hardRadio;

    @FXML private Spinner maxPrice;

    @FXML private Slider maxTime;
    @FXML private Label maxTimeSliderLabel;

    // Detailed view
    @FXML private Label detailedLabel;
    @FXML private ImageView detailedImage;
    @FXML private AnchorPane detailedPane;


    private Map<String, RecipeListItem> recipeListItemMap = new HashMap<String, RecipeListItem>();


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Init recipeListItemMap
        for (Recipe recipe : backendController.getRecipes()) {
            RecipeListItem recipeListItem = new RecipeListItem(recipe, this);
            recipeListItemMap.put(recipe.getName(), recipeListItem);
        }

        // INGREDIENT
        mainIngredient.getItems().addAll("Visa alla", "Kött", "Fisk", "Kyckling", "Vegetarisk");
        mainIngredient.getSelectionModel().select("Visa alla");
        mainIngredient.getSelectionModel().selectedItemProperty().addListener((ChangeListener<String>) (observable, oldValue, newValue) -> {
            backendController.setMainIngredient(newValue);
            updateRecipeList();
        });

        // CUISINE
        cuisine.getItems().addAll("Visa alla", "Sverige", "Grekland", "Indien", "Asien", "Afrika", "Frankrike");
        cuisine.getSelectionModel().select("Visa alla");
        cuisine.getSelectionModel().selectedItemProperty().addListener((ChangeListener<String>) (observable, oldValue, newValue) -> {
            backendController.setCuisine(newValue);
            updateRecipeList();
        });

        // DIFFICULITY
        ToggleGroup difficultyToggleGroup = new ToggleGroup();
        allaRadio.setToggleGroup(difficultyToggleGroup);
        easyRadio.setToggleGroup(difficultyToggleGroup);
        mediumRadio.setToggleGroup(difficultyToggleGroup);
        hardRadio.setToggleGroup(difficultyToggleGroup);
        allaRadio.setSelected(true);
        difficultyToggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {

            if (difficultyToggleGroup.getSelectedToggle() != null) {
                RadioButton selected = (RadioButton) difficultyToggleGroup.getSelectedToggle();
                backendController.setDifficulty(selected.getText());
                updateRecipeList();
            }
        });

        // MAX PRICE
        SpinnerValueFactory valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 300, 0, 5);
        maxPrice.setValueFactory(valueFactory);
        maxPrice.valueProperty().addListener((ChangeListener<Integer>) (observable, oldValue, newValue) -> {
            backendController.setMaxPrice(newValue);

            updateRecipeList();
        });
        maxPrice.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue){
                Integer value = Integer.valueOf(maxPrice.getEditor().getText());
                backendController.setMaxPrice(value);
                updateRecipeList();
            }
        });

        // MAX TIME
        maxTimeSliderLabel.setText(((int) maxTime.getValue()) + " minuter"); // Init with current value
        maxTime.valueProperty().addListener((ChangeListener<Number>) (observable, oldValue, newValue) -> {
            if(newValue != null && !newValue.equals(oldValue) && !maxTime.isValueChanging()) {
                Integer value = newValue.intValue();

                maxTimeSliderLabel.setText(value + " minuter");

                backendController.setMaxTime(value);

                updateRecipeList();
            }
        });

        updateRecipeList();
    }

    private void updateRecipeList(){
        resultFlowPane.getChildren().clear();

        // Get search
        for(Recipe result : backendController.getRecipes()){
            resultFlowPane.getChildren().add(recipeListItemMap.get(result.getName()));
        }
    }

    private void populateRecipeDetailView(Recipe recipe){
        detailedLabel.setText(recipe.getName());
        detailedImage.setImage(recipe.getFXImage());
    }

    @FXML public void closeRecipeView(){
        searchPane.toFront();
    }

    public void openRecipeView(Recipe recipe){
        populateRecipeDetailView(recipe);
        detailedPane.toFront();
    }
}