
package recipesearch;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import se.chalmers.ait.dat215.lab2.Recipe;
import se.chalmers.ait.dat215.lab2.RecipeDatabase;
import se.chalmers.ait.dat215.lab2.SearchFilter;


public class RecipeSearchController implements Initializable {


    @FXML // annotation marks a protected or private class member as accessible to FXML
    AnchorPane recipeDetailPane;
    SplitPane searchPane;

    RecipeDatabase db = RecipeDatabase.getSharedInstance();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //init with an "empty search"
        List<Recipe> recipes = db.search(new SearchFilter(null, 0, null, 0, null));

    }

}