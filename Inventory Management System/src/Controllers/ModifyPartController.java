/**
 Class for ModifyPartController.java
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
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 Class for controlling ModifyPartController.fxml.
 FUTURE ENHANCEMENTS: Duplication with AddPartController.
 LOGICAL/RUNTIME ERROR: Passed the ID in for 3 fields at the beginning, but that was a simple Text Field fix.
 */
public class ModifyPartController implements Initializable {

    Inventory inv;
    Part part;

    @FXML
    private RadioButton modifyPartInHouseRad;

    @FXML
    private ToggleGroup ToggleGroup;

    @FXML
    private RadioButton modifyPartOutsourceRad;

    @FXML
    private Label modifyPartMCText;

    @FXML
    private TextField modifyPartID;

    @FXML
    private TextField modifyPartName;

    @FXML
    private TextField modifyPartInventory;

    @FXML
    private TextField modifyPartPrice;

    @FXML
    private TextField modifyPartMax;

    @FXML
    private TextField modifyPartMachineCompany;

    @FXML
    private TextField modifyPartMin;

    /**
     @param inv Sets the inventory for this controller
     @param part Sets the part for this controller
     */
    public ModifyPartController(Inventory inv, Part part)
    {
        this.inv = inv;
        this.part = part;
    }
    /**
     Initialize Class
     @param url URL
     @param rb ResourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        addData();
    }

    /**
    Adds current Part Data to Text Fields
     */
    public void addData()
    {
        if (part instanceof InHouse)
        {
            InHouse iPart = (InHouse) part;
            modifyPartInHouseRad.setSelected(true);
            modifyPartMCText.setText("Machine ID");
            this.modifyPartName.setText(iPart.getName());
            this.modifyPartID.setText((Integer.toString(iPart.getId())));
            this.modifyPartInventory.setText((Integer.toString(iPart.getStock())));
            this.modifyPartPrice.setText((Double.toString(iPart.getPrice())));
            this.modifyPartMin.setText((Integer.toString(iPart.getMin())));
            this.modifyPartMax.setText((Integer.toString(iPart.getMax())));
            this.modifyPartMachineCompany.setText((Integer.toString(iPart.getMachineId())));
        }
        if (part instanceof Outsource)
        {
            Outsource oPart = (Outsource) part;
            modifyPartOutsourceRad.setSelected(true);
            modifyPartMCText.setText("Company Name");
            this.modifyPartName.setText(oPart.getName());
            this.modifyPartID.setText((Integer.toString(oPart.getId())));
            this.modifyPartInventory.setText((Integer.toString(oPart.getStock())));
            this.modifyPartPrice.setText((Double.toString(oPart.getPrice())));
            this.modifyPartMin.setText((Integer.toString(oPart.getMin())));
            this.modifyPartMax.setText((Integer.toString(oPart.getMax())));
            this.modifyPartMachineCompany.setText(oPart.getCompany());
        }
    }

    /**
     @param event Returns to Main if Cancelled.
     */
    @FXML
    void modifyPartCancel(ActionEvent event)
    {
        Alert warning = new Alert(Alert.AlertType.CONFIRMATION);
        warning.setTitle("Cancel");
        warning.setHeaderText("Are you sure you'd like to cancel?");
        warning.setContentText("Click OK to confirm");
        Optional<ButtonType> answer = warning.showAndWait();
        if (answer.get() == ButtonType.OK) {
            returnToMain(event);
        } else {
            return;
        }
    }

    /**
     @param event Sets Label to Machine ID
     */
    @FXML
    void modifyPartInHouseSelected(ActionEvent event)
    {
        modifyPartMCText.setText("Machine ID");
    }

    /**
     @param event Sets Label to Company Name
     */
    @FXML
    void modifyPartOutsourceSelected(ActionEvent event)
    {
        modifyPartMCText.setText("Company Name");
    }

    /**
     @param event Saves Part.
     FUTURE ENHANCEMENTS: Warnings are repetitive.  Would try and cut down on some of them.
     */
    @FXML
    void modifyPartSave(ActionEvent event)
    {
        boolean finished = false;
        TextField[] fields = {modifyPartInventory, modifyPartPrice, modifyPartMin, modifyPartMax};
        if (modifyPartInHouseRad.isSelected() || modifyPartOutsourceRad.isSelected()) {
            for (int i = 0; i < fields.length; i++)
            {
                boolean errorValue = valueCheck(fields[i]);
                if (errorValue) {
                    finished = true;
                    break;
                }
                boolean errorType = typeCheck(fields[i]);
                if (errorType) {
                    finished = true;
                    break;
                }
            }
            if (modifyPartName.getText().trim().isEmpty() || modifyPartName.getText().trim().toLowerCase().equals("")) {
                Warnings.partErrors(4, modifyPartName);
                return;
            }
            if (Integer.parseInt(modifyPartMin.getText().trim()) > Integer.parseInt(modifyPartMax.getText().trim())) {
                Warnings.partErrors(8, modifyPartMin);
                return;
            }
            if (Integer.parseInt(modifyPartInventory.getText().trim()) < Integer.parseInt(modifyPartMin.getText().trim())) {
                Warnings.partErrors(6, modifyPartInventory);
                return;
            }
            if (Integer.parseInt(modifyPartInventory.getText().trim()) > Integer.parseInt(modifyPartMax.getText().trim())) {
                Warnings.partErrors(7, modifyPartInventory);
                return;
            }
            if (finished) {
                return;
            } else if (modifyPartOutsourceRad.isSelected() && modifyPartMachineCompany.getText().trim().isEmpty()) {
                Warnings.partErrors(1, modifyPartMachineCompany);
                return;
            } else if (modifyPartInHouseRad.isSelected() && !modifyPartMachineCompany.getText().trim().matches("[0-9]*")) {
                Warnings.partErrors(9, modifyPartMachineCompany);
                return;
            } else if (modifyPartInHouseRad.isSelected() & part instanceof InHouse) {
                modifyPartInHouseFinished();
            } else if (modifyPartInHouseRad.isSelected() & part instanceof Outsource) {
                modifyPartInHouseFinished();
            } else if (modifyPartOutsourceRad.isSelected() & part instanceof InHouse) {
                modifyPartOutsourceFinished();
            } else if (modifyPartOutsourceRad.isSelected() & part instanceof Outsource) {
                modifyPartOutsourceFinished();
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
     Updates InHouse Saved Part to Inventory
     */
    private void modifyPartInHouseFinished()
    {
        inv.updatePart(new InHouse(Integer.parseInt(modifyPartID.getText().trim()), modifyPartName.getText().trim(), Double.parseDouble(modifyPartPrice.getText().trim()), Integer.parseInt(modifyPartInventory.getText().trim()), Integer.parseInt(modifyPartMin.getText().trim()), Integer.parseInt(modifyPartMax.getText().trim()), Integer.parseInt(modifyPartMachineCompany.getText().trim())));
    }

    /**
     Updates Outsource Saved Part to Inventory
     */
    private void modifyPartOutsourceFinished()
    {
        inv.updatePart(new Outsource(Integer.parseInt(modifyPartID.getText().trim()), modifyPartName.getText().trim(), Double.parseDouble(modifyPartPrice.getText().trim()), Integer.parseInt(modifyPartInventory.getText().trim()), Integer.parseInt(modifyPartMin.getText().trim()), Integer.parseInt(modifyPartMax.getText().trim()), modifyPartMachineCompany.getText().trim()));
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
            if (field == modifyPartPrice && Double.parseDouble(field.getText().trim()) < 0)
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
        if (field == modifyPartPrice & !field.getText().trim().matches("\\d+(\\.\\d+)?"))
        {
            Warnings.partErrors(3, field);
            return true;
        }
        if (field != modifyPartPrice & !field.getText().trim().matches("[0-9]*"))
        {
            Warnings.partErrors(3, field);
            return true;
        }
        return false;
    }

}
