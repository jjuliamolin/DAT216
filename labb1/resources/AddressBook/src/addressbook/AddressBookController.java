
package addressbook;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import se.chalmers.cse.dat215.lab1.*;


public class AddressBookController implements Initializable {
    
    @FXML private MenuBar menuBar;
    @FXML private Button newButton;
    @FXML private Button deleteButton;
    @FXML private ListView contactsList;

    @FXML private TextField textField_FirstName,textField_LastName,textField_Phone, textField_Email, textField_Address,textField_PostCode, textField_City;

    private Presenter presenter;
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        presenter = new Presenter(
                contactsList,
                textField_FirstName,
                textField_LastName,
                textField_Phone,
                textField_Email,
                textField_Address,
                textField_PostCode,
                textField_City);

        presenter.init();


        //adding change listener, so the model know which contact
        contactsList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {

            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                presenter.contactsListChanged();
            }

        });


        textField_FirstName.focusedProperty().addListener(new TextFieldListener(textField_FirstName));
        textField_LastName.focusedProperty().addListener(new TextFieldListener(textField_LastName));
        textField_Phone.focusedProperty().addListener(new TextFieldListener(textField_Phone));
        textField_Email.focusedProperty().addListener(new TextFieldListener(textField_Email));
        textField_Address.focusedProperty().addListener(new TextFieldListener(textField_Address));
        textField_PostCode.focusedProperty().addListener(new TextFieldListener(textField_PostCode));
        textField_City.focusedProperty().addListener(new TextFieldListener(textField_City));
        
    }
    
    @FXML 
    protected void openAboutActionPerformed(ActionEvent event) throws IOException{
    
        ResourceBundle bundle = java.util.ResourceBundle.getBundle("addressbook/resources/AddressBook");
        Parent root = FXMLLoader.load(getClass().getResource("address_book_about.fxml"), bundle);
        Stage aboutStage = new Stage();
        aboutStage.setScene(new Scene(root));
        aboutStage.setTitle(bundle.getString("about.title.text"));
        aboutStage.initModality(Modality.APPLICATION_MODAL);
        aboutStage.setResizable(false);
        aboutStage.showAndWait();
    }

    @FXML
    protected void textFieldActionPerformed(ActionEvent event){
        presenter.textFieldActionPerformed(event);
    }
    
    @FXML 
    protected void closeApplicationActionPerformed(ActionEvent event) throws IOException{
        
        Stage addressBookStage = (Stage) menuBar.getScene().getWindow();
        addressBookStage.hide();
    }

    @FXML
    protected void newButtonActionPerformed(ActionEvent event) {
        presenter.newContact();
    }

    @FXML
    protected void deleteButtonActionPerformed(ActionEvent event) {
        presenter.removeCurrentContact();
    }

    @FXML //lite onödigt att kalla på identiska funktioner, kan kalla på ovanstående för olika knappar också.
    protected void newContactActionPerformed(ActionEvent event){
        presenter.newContact();
    }

    @FXML
    protected void removeContactActionPerformed(ActionEvent event){
        presenter.removeCurrentContact();
    }




    private class TextFieldListener implements ChangeListener<Boolean>{

        private TextField textField;

        public TextFieldListener(TextField textField){
            this.textField = textField;
        }

        @Override
        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
            if(newValue){
                presenter.textFieldFocusGained(textField);

            }
            else{
                presenter.textFieldFocusLost(textField);
            }
        }
    }


}
