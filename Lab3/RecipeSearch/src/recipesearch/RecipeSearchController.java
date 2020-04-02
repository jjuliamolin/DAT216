
package recipesearch;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.util.Callback;
import se.chalmers.ait.dat215.lab2.Ingredient;
import se.chalmers.ait.dat215.lab2.Recipe;
import se.chalmers.ait.dat215.lab2.RecipeDatabase;


public class RecipeSearchController implements Initializable {

    private RecipeDatabase db = RecipeDatabase.getSharedInstance();
    private RecipeBackendController backendController = new RecipeBackendController();

    @FXML
    private SplitPane searchPane;

    @FXML
    private FlowPane resultFlowPane;
    @FXML
    private ComboBox mainIngredient, cuisine;

    @FXML
    private RadioButton allaRadio, easyRadio, mediumRadio, hardRadio;

    @FXML
    private Spinner maxPrice;

    @FXML
    private Slider maxTime;
    @FXML
    private Label maxTimeSliderLabel;

    // Detailed view
    @FXML private Label detailedLabel;
    @FXML private Label detailedTime;
    @FXML private Label detailedPrice;
    @FXML private Label detailedServings;
    @FXML private ImageView detailedImage;
    @FXML private ImageView closeImage;
    @FXML private ImageView detailedDiff;
    @FXML private ImageView detailedCuisine;
    @FXML private ImageView detailedIngredient;
    @FXML private TextArea detailedIngredients;
    @FXML private TextArea detailedDesc;
    @FXML private TextArea detailedInfo;
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
        populateMainIngredientComboBox();

        // CUISINE
        cuisine.getItems().addAll("Visa alla", "Sverige", "Grekland", "Indien", "Asien", "Afrika", "Frankrike");
        cuisine.getSelectionModel().select("Visa alla");
        cuisine.getSelectionModel().selectedItemProperty().addListener((ChangeListener<String>) (observable, oldValue, newValue) -> {
            backendController.setCuisine(newValue);
            updateRecipeList();
        });
        populateCuisineComboBox();

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
            if (!newValue) {
                Integer value = Integer.valueOf(maxPrice.getEditor().getText());
                backendController.setMaxPrice(value);
                updateRecipeList();
            }
        });

        // MAX TIME
        maxTimeSliderLabel.setText(((int) maxTime.getValue()) + " minuter"); // Init with current value
        maxTime.valueProperty().addListener((ChangeListener<Number>) (observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.equals(oldValue) && !maxTime.isValueChanging()) {
                Integer value = newValue.intValue();

                maxTimeSliderLabel.setText(value + " minuter");

                backendController.setMaxTime(value);

                updateRecipeList();
            }
        });


        // Set focus
        Platform.runLater(() -> mainIngredient.requestFocus());

        // Get initial recipe list
        updateRecipeList();
    }

    private void updateRecipeList() {
        resultFlowPane.getChildren().clear();

        // Get search
        for (Recipe result : backendController.getRecipes()) {
            resultFlowPane.getChildren().add(recipeListItemMap.get(result.getName()));
        }
    }

    private void populateRecipeDetailView(Recipe recipe) {
        detailedLabel.setText(recipe.getName());
        detailedTime.setText(recipe.getTime() + " minuter");
        detailedPrice.setText(recipe.getPrice() + " kr");
        detailedInfo.setText(recipe.getInstruction());
        detailedDesc.setText(recipe.getDescription());

        detailedServings.setText(recipe.getServings() + " portioner");

        String ingredients = "";
        for(Ingredient ing : recipe.getIngredients()){
            ingredients += ing.getAmount() + " " + ing.getUnit() + " " + ing.getName() + "\n";
        }

        detailedIngredients.setText(ingredients);

        detailedDiff.setImage(getDifficultyImage(recipe.getDifficulty()));
        detailedCuisine.setImage(getCuisine(recipe.getCuisine()));
        detailedIngredient.setImage(getIngredientImage(recipe.getMainIngredient()));

        detailedImage.setImage(getSquareImage(recipe.getFXImage()));
    }

    @FXML
    public void closeRecipeView() {
        searchPane.toFront();
        detailedPane.toBack(); // Added since toFront didnt work
    }

    @FXML
    public void mouseTrap(Event event){
        event.consume();
    }

    @FXML
    public void closeButtonMouseEntered(){
        closeImage.setImage(new Image(getClass().getClassLoader().getResourceAsStream(
                "RecipeSearch/resources/icon_close_hover.png")));
    }

    @FXML
    public void closeButtonMousePressed(){
        //samma princip som ovan, ta rätt bild
        closeImage.setImage(new Image(getClass().getClassLoader().getResourceAsStream(
                "RecipeSearch/resources/icon_close_pressed.png")));
    }

    @FXML
    public void closeButtonMouseExited(){
        closeImage.setImage(new Image(getClass().getClassLoader().getResourceAsStream(
                "RecipeSearch/resources/icon_close.png")));
    }

    @FXML
    public void openRecipeView(Recipe recipe) {
        populateRecipeDetailView(recipe);
        detailedPane.toFront();
    }

    private void populateCuisineComboBox() {
        Callback<ListView<String>, ListCell<String>> cellFactory = new Callback<>() {

            @Override
            public ListCell<String> call(ListView<String> p) {

                return new ListCell<>() {

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);

                        setText(item);

                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            Image icon = getCuisine(item);

                            ImageView iconImageView = new ImageView(icon);
                            iconImageView.setFitHeight(12);
                            iconImageView.setPreserveRatio(true);
                            setGraphic(iconImageView);
                        }
                    }
                };
            }
        };
        cuisine.setButtonCell(cellFactory.call(null));
        cuisine.setCellFactory(cellFactory);
    }

    private void populateMainIngredientComboBox() {
        Callback<ListView<String>, ListCell<String>> cellFactory = new Callback<>() {

            @Override
            public ListCell<String> call(ListView<String> p) {

                return new ListCell<>() {

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);

                        setText(item);

                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            Image icon = getIngredientImage(item);

                            ImageView iconImageView = new ImageView(icon);
                            iconImageView.setFitHeight(12);
                            iconImageView.setPreserveRatio(true);
                            setGraphic(iconImageView);
                        }
                    }
                };
            }
        };
        mainIngredient.setButtonCell(cellFactory.call(null));
        mainIngredient.setCellFactory(cellFactory);
    }

    // HELPER FUNCTIONS
    public Image getCuisine(String cuisine) {
        Image icon = null;
        String iconPath;
        try {
            switch (cuisine) {
                case "Sverige":
                    iconPath = "RecipeSearch/resources/icon_flag_sweden.png";
                    icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                    break;
                case "Grekland":
                    iconPath = "RecipeSearch/resources/icon_flag_greece.png";
                    icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                    break;
                case "Indien":
                    iconPath = "RecipeSearch/resources/icon_flag_india.png";
                    icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                    break;
                case "Asien":
                    iconPath = "RecipeSearch/resources/icon_flag_asia.png";
                    icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                    break;
                case "Afrika":
                    iconPath = "RecipeSearch/resources/icon_flag_africa.png";
                    icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                    break;
                case "Frankrike":
                    iconPath = "RecipeSearch/resources/icon_flag_france.png";
                    icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                    break;
            }
        } catch (NullPointerException ex) {
            //This should never happen in this lab but could load a default image in case of a NullPointer
        }

        return icon;
    }

    public Image getIngredientImage(String ingredient) {
        Image icon = null;
        String iconPath;
        try {
            switch (ingredient) {
                case "Kött":
                    iconPath = "RecipeSearch/resources/icon_main_meat.png";
                    icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                    break;
                case "Fisk":
                    iconPath = "RecipeSearch/resources/icon_main_fish.png";
                    icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                    break;
                case "Kyckling":
                    iconPath = "RecipeSearch/resources/icon_main_chicken.png";
                    icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                    break;
                case "Vegetarisk":
                    iconPath = "RecipeSearch/resources/icon_main_veg.png";
                    icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                    break;
            }
        } catch (NullPointerException ex) {
            //This should never happen in this lab but could load a default image in case of a NullPointer
        }

        return icon;
    }

    public Image getDifficultyImage(String diff) {
        Image icon = null;
        String iconPath;
        try {
            switch (diff) {
                case "Lätt":
                    iconPath = "RecipeSearch/resources/icon_difficulty_easy.png";
                    icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                    break;
                case "Mellan":
                    iconPath = "RecipeSearch/resources/icon_difficulty_medium.png";
                    icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                    break;
                case "Svår":
                    iconPath = "RecipeSearch/resources/icon_difficulty_hard.png";
                    icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                    break;
            }
        } catch (NullPointerException ex) {
            //This should never happen in this lab but could load a default image in case of a NullPointer
        }

        return icon;
    }

    public Image getSquareImage(Image image){

        int x = 0;
        int y = 0;
        int width = 0;
        int height = 0;

        if(image.getWidth() > image.getHeight()){
            width = (int) image.getHeight();
            height = (int) image.getHeight();
            x = (int)(image.getWidth() - width)/2;
            y = 0;
        }

        else if(image.getHeight() > image.getWidth()){
            width = (int) image.getWidth();
            height = (int) image.getWidth();
            x = 0;
            y = (int) (image.getHeight() - height)/2;
        }

        else{
            //Width equals Height, return original image
            return image;
        }
        return new WritableImage(image.getPixelReader(), x, y, width, height);
    }
}