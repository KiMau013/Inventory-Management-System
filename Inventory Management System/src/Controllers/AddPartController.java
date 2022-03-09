/**
 Class for AddPartController.java
 */

/**
 @author Ki Mau
 */
package Controllers;

import Model.InHouse;
import Model.Inventory;
import Model.Outsource;
import Model.Part;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

/**
 Class for controlling AddPart.fxml.
 FUTURE ENHANCEMENTS: Lots of duplication with the ModifyPartController.  Would mash these two together.
 LOGICAL/RUNTIME ERROR: Had to put in a verifier for the ID to not already exist.  Carried over from incrementing ID, but kept it in just in case.
 */
public class AddPartController implements Initializable {

    Inventory inv;

    @FXML
    private RadioButton addPartInHouseRad;

    @FXML
    private ToggleGroup ToggleGroup;

    @FXML
    private RadioButton addPartOutsourceRad;

    @FXML
    private Label addPartMCText;

    @FXML
    private TextField addPartID;

    @FXML
    private TextField addPartName;

    @FXML
    private TextField addPartInventory;

    @FXML
    private TextField addPartPrice;

    @FXML
    private TextField addPartMax;

    @FXML
    private TextField addPartMachineCompany;

    @FXML
    private TextField addPartMin;

    /**
     @param inv Sets the inventory for this controller
     */
    public AddPartController(Inventory inv) {
        this.inv = inv;
    }

    /**
     Initialize Class
     @param url URL
     @param rb ResourceBundle
     */
    @Override
    public void initialize (URL url, ResourceBundle rb)
    {
        partIDGenerator();
        emptyFields();
    }
    /**
     @param event Returns to Main if cancelled
     */
    @FXML
    void addPartCancel(ActionEvent event) {
        boolean cancel = Warnings.cancelWarning();
        if(cancel)
        {
            returnToMain(event);
        }
    }

    /**
     @param event Sets Label for Machine ID

     */
    @FXML
    void addPartInHouseSelected(ActionEvent event) {
        addPartMCText.setText("Machine ID");
    }

    /**
     @param event Sets Label for Comapany Name.
     LOGICAL/RUNTIME ERROR: Did not have Toggle Group selected correctly in Scene Builder, so set that correctly.
     */
    @FXML
    void addPartOutsourceSelected(ActionEvent event) {
        addPartMCText.setText("Company Name");
    }

    /**
     @param event Saves the Part
     */
    @FXML
    void addPartSave(ActionEvent event) {
        boolean finished = false;
        TextField[] fields = {addPartInventory, addPartPrice, addPartMin, addPartMax};
        if (addPartInHouseRad.isSelected() || addPartOutsourceRad.isSelected()) {
            for (TextField fields1 : fields) {
                boolean errorValue = valueCheck(fields1);
                if (errorValue) {
                    finished = true;
                    break;
                }
                boolean errorType = typeCheck(fields1);
                if (errorType) {
                    finished = true;
                    break;
                }
            }
            if (addPartName.getText().trim().isEmpty() || addPartName.getText().trim().toLowerCase().equals("")) {
                Warnings.partErrors(4, addPartName);
                return;
            }
            if (Integer.parseInt(addPartMin.getText().trim()) > Integer.parseInt(addPartMax.getText().trim())) {
                Warnings.partErrors(8, addPartMin);
                return;
            }
            if (Integer.parseInt(addPartInventory.getText().trim()) < Integer.parseInt(addPartMin.getText().trim())) {
                Warnings.partErrors(6, addPartInventory);
                return;
            }
            if (Integer.parseInt(addPartInventory.getText().trim()) > Integer.parseInt(addPartMax.getText().trim())) {
                Warnings.partErrors(7, addPartInventory);
                return;
            }
            if (finished) {
                return;
            } else if (addPartMachineCompany.getText().trim().isEmpty() || addPartMachineCompany.getText().trim().equals("")) {
                Warnings.partErrors(3, addPartMachineCompany);
                return;
            } else if (addPartInHouseRad.isSelected() && !addPartMachineCompany.getText().trim().matches("[0-9]*")) {
                Warnings.partErrors(9, addPartMachineCompany);
                return;
            } else if (addPartInHouseRad.isSelected()) {
                addPartInHouseFinished();
            } else if (addPartOutsourceRad.isSelected()) {
                addPartOutsourceFinished();
            }
        }
        else
        {
            Warnings.partErrors(2, null);
            return;
        }
        returnToMain(event);
    }

    /**
     Adds InHouse Saved Part to Inventory
     */
    private void addPartInHouseFinished()
    {
        inv.addPart(new InHouse(Integer.parseInt(addPartID.getText().trim()), addPartName.getText().trim(), Double.parseDouble(addPartPrice.getText().trim()), Integer.parseInt(addPartInventory.getText().trim()), Integer.parseInt(addPartMin.getText().trim()), Integer.parseInt(addPartMax.getText().trim()), (Integer.parseInt(addPartMachineCompany.getText().trim()))));
    }
    /**
     Adds Outsource Saved Part to Inventory
     */
    private void addPartOutsourceFinished()
    {
        inv.addPart(new Outsource(Integer.parseInt(addPartID.getText().trim()), addPartName.getText().trim(), Double.parseDouble(addPartPrice.getText().trim()), Integer.parseInt(addPartInventory.getText().trim()), Integer.parseInt(addPartMin.getText().trim()), Integer.parseInt(addPartMax.getText().trim()), addPartMachineCompany.getText().trim()));
    }

    /**
     Empties Text Fields
     */
    private void emptyFields()
    {
        addPartName.setText("");
        addPartInventory.setText("");
        addPartPrice.setText("");
        addPartMin.setText("");
        addPartMax.setText("");
        addPartMachineCompany.setText("");
        addPartMCText.setText("Machine ID");
        addPartInHouseRad.setSelected(true);
    }

    /**
     Generates an ID for New Part
     */
    private void partIDGenerator()
        {
            boolean match;
            Random randomNum = new Random();
            Integer num = randomNum.nextInt(1000);

            if (inv.partsSize() == 0)
            {
                addPartID.setText(num.toString());
            }
            if (inv.partsSize() == 1000)
            {
                Warnings.partErrors(3, null);
            }
            else
            {
                match = numVerify(num);
                if(match == false)
                {
                    addPartID.setText(num.toString());
                }
                else
                {
                    partIDGenerator();
                }
            }
    }

    /**
     @param num Verifies ID doesn't already exist
     */
    private boolean numVerify(Integer num) {
        Part pair = inv.lookupPart(num);
        return pair != null;
    }

    /**
     @param event Returns to Main Screen
     */
    private void returnToMain(Event event)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/Main.fxml"));
            MainController controller = new MainController(inv);
            loader.setController(controller);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        }
        catch (IOException e) {}
    }

    /**
     @param field Checks Text Field Values
     */
    private boolean valueCheck(TextField field)
    {
        boolean warning = false;
        try
        {
            if (field.getText().trim().isEmpty() || field.getText().trim() == null)
            {
                Warnings.partErrors(1, field);
                return true;
            }
            if (field == addPartPrice && Double.parseDouble(field.getText().trim()) < 0)
            {
                Warnings.partErrors(5, field);
                warning = true;
            }
        }
        catch (Exception e)
        {
            warning = true;
            Warnings.partErrors(3, field);
            System.out.println(e);
        }
        return warning;
    }

    /**
     @param field Checks Text Field Types
     */
    private boolean typeCheck(TextField field)
    {
        if (field == addPartPrice & !field.getText().trim().matches("\\d+(\\.\\d+)?"))
        {
            Warnings.partErrors(3, field);
            return true;
        }
        if (field != addPartPrice & !field.getText().trim().matches("[0-9]*"))
        {
            Warnings.partErrors(3, field);
            return true;
        }
        return false;
    }

}
