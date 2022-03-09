/**
 Class for ModifyProductController.java
 */

/**
 @author Ki Mau
 */
package Controllers;

import Model.Inventory;
import Model.Part;
import Model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 Class for controlling ModifyProductController.fxml.
 FUTURE ENHANCEMENTS: It duplicates quite a bit of the AddProductController, so probably finding a way to not duplicate so much stuff.  Maybe even pass in needed data to AddProductController instead of spinning up a whole new Modify Class.
 LOGICAL/RUNTIME ERROR: BIGGEST NIGHTMARE -- CellValueFactory and PropertyValueFactory.  Multiple of the recorded sessions weren't showing the biggest hurdle -- they put them in the .fxml files themselves, not calling from here.  So trying to figure that out (since SinceBuilder won't create those automatically).
 */
public class ModifyProductController implements Initializable {

    Inventory inv;
    Product product;

    @FXML
    private TextField modifyProductID;

    @FXML
    private TextField modifyProductName;

    @FXML
    private TextField modifyProductInventory;

    @FXML
    private TextField modifyProductPrice;

    @FXML
    private TextField modifyProductMax;

    @FXML
    private TextField modifyProductMin;

    @FXML
    private TextField modifyProductSearchField;

    @FXML
    private TableView<Part> partTable;

    @FXML
    private TableView<Part> addedPartTable;

    private ObservableList<Part> partInventory = FXCollections.observableArrayList();
    private ObservableList<Part> partSearch = FXCollections.observableArrayList();
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();

    /**
     @param inv Sets the inventory for this controller
     @param product Sets the product for this controller
     */
    public ModifyProductController(Inventory inv, Product product)
    {
        this.inv = inv;
        this.product = product;
    }

    /**
     Initialize Class
     @param url URL
     @param rb ResourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        partFillTable();
        addData();
    }

    /**
     Fills part table
     */
    private void partFillTable()
    {
        partInventory.setAll(inv.getAllParts());
        TableColumn<Part, Double> partPrice = priceFormatting();
        partTable.getColumns().addAll(partPrice);
        partTable.setItems(partInventory);
        partTable.refresh();
    }

    /**
     Adds Associated Parts to Table and Product Data to Text Fields.
     LOGICAL/RUNTIME ERROR: Wouldn't add the data by passing in a Product.  Had to do the Int building method.
     */
    private void addData()
    {
        for (int i = 0; i < 1000; i++)
        {
            Part part = product.lookupAssociatedPart(i);
            if (part != null)
            {
                associatedParts.add(part);
            }
        }
        TableColumn<Part, Double> partPrice = priceFormatting();
        addedPartTable.getColumns().addAll(partPrice);
        addedPartTable.setItems(associatedParts);

        this.modifyProductName.setText(product.getName());
        this.modifyProductID.setText((Integer.toString(product.getId())));
        this.modifyProductInventory.setText((Integer.toString(product.getStock())));
        this.modifyProductPrice.setText((Double.toString(product.getPrice())));
        this.modifyProductMin.setText((Integer.toString(product.getMin())));
        this.modifyProductMax.setText((Integer.toString(product.getMax())));
    }

    /**
     @param event Adds part from table to associated parts table
     */
    @FXML
    void modifyProductAdd(ActionEvent event)
    {
        Part part = partTable.getSelectionModel().getSelectedItem();
        boolean repeated = false;
        if (part == null)
        {
            return;
        }
        else
        {
            int id = part.getId();
            for (int i = 0; i < associatedParts.size(); i++)
            {
                if (associatedParts.get(i).getId() == id)
                {
                    Warnings.productErrors(2, null);
                    repeated = true;
                }
            }
            if (!repeated)
            {
                associatedParts.add(part);
            }
            addedPartTable.setItems(associatedParts);
        }
    }

    /**
     @param event Returns to main if cancelled.
     FUTURE ENHANCEMENTS: This is repeated all over the Controllers.  Would just call a function from one place instead.
     */
    @FXML
    void modifyProductCancel(ActionEvent event)
    {
        boolean cancelled = Warnings.cancelWarning();
        if (cancelled)
        {
            returnToMain(event);
        }
        else
        {
            return;
        }
    }

    /**
     @param event Removes selected part from associated parts
     LOGICAL/RUNTIME ERROR: This had just 1 line that messed up the local associated parts list.  It was calling the removeAssociatedPart method from product that was updating the actual list of items.
     */
    @FXML
    void modifyProductRemovePart(ActionEvent event)
    {
        Part part = addedPartTable.getSelectionModel().getSelectedItem();
        boolean removed = false;
        if (part != null)
        {
            boolean delete = Warnings.confirmDelete(part.getName());
            if (delete)
            {
                associatedParts.remove(part);
                addedPartTable.refresh();
            }
        }
    }

    /**
     @param event Saves Product with associated parts to Inventory
     */
    @FXML
    void modifyProductSave(ActionEvent event)
    {
        boolean finished = false;
        TextField[] fields = {modifyProductInventory, modifyProductPrice, modifyProductMin, modifyProductMax};
        double cost = 0;
        for (int i = 0; i < associatedParts.size(); i++) {
            cost += associatedParts.get(i).getPrice();
        }
        if (modifyProductName.getText().trim().isEmpty() || modifyProductName.getText().trim().toLowerCase().equals("")) {
            Warnings.productErrors(4, modifyProductName);
            return;
        }
        for (int i = 0; i < fields.length; i++)
        {
            boolean errorValue = valueCheck(fields[i]);
            if (errorValue)
            {
                finished = true;
                break;
            }
            boolean errorType = typeCheck(fields[i]);
            if (errorType)
            {
                finished = true;
                break;
            }
        }
        if (Integer.parseInt(modifyProductMin.getText().trim()) > Integer.parseInt(modifyProductMax.getText().trim())) {
            Warnings.productErrors(10, modifyProductMin);
            return;
        }
        if (Integer.parseInt(modifyProductInventory.getText().trim()) < Integer.parseInt(modifyProductMin.getText().trim())) {
            Warnings.productErrors(8, modifyProductInventory);
            return;
        }
        if (Integer.parseInt(modifyProductInventory.getText().trim()) > Integer.parseInt(modifyProductMax.getText().trim())) {
            Warnings.productErrors(9, modifyProductInventory);
            return;
        }
        if (Double.parseDouble(modifyProductPrice.getText().trim()) < cost)
        {
            Warnings.productErrors(6, modifyProductPrice);
            return;
        }
        if (associatedParts.size() == 0) {
            Alert partZero = new Alert(Alert.AlertType.WARNING);
            partZero.setTitle("Product Has No Parts");
            partZero.setHeaderText("Warning!  Product had no parts associated.");
            partZero.setContentText("Product Saved");
            partZero.showAndWait();

            Product product = new Product(Integer.parseInt(modifyProductID.getText().trim()), modifyProductName.getText().trim(), Double.parseDouble(modifyProductPrice.getText().trim()), Integer.parseInt(modifyProductInventory.getText().trim()), Integer.parseInt(modifyProductMin.getText().trim()), Integer.parseInt(modifyProductMax.getText().trim()));
            for (int i = 0; i < associatedParts.size(); i++) {
                product.addAssociatedPart(associatedParts.get(i));
            }
            inv.updateProduct(product);
            returnToMain(event);
        }
        else {
            Product product = new Product(Integer.parseInt(modifyProductID.getText().trim()), modifyProductName.getText().trim(), Double.parseDouble(modifyProductPrice.getText().trim()), Integer.parseInt(modifyProductInventory.getText().trim()), Integer.parseInt(modifyProductMin.getText().trim()), Integer.parseInt(modifyProductMax.getText().trim()));
            for (int i = 0; i < associatedParts.size(); i++)
            {
                product.addAssociatedPart(associatedParts.get(i));
            }
            inv.updateProduct(product);
            returnToMain(event);
        }
    }

    /**
     @param event Searches for Parts that match criteria to Add
     */
    @FXML
    void modifyProductSearch(ActionEvent event)
    {
        if (modifyProductSearchField.getText() != null && modifyProductSearchField.getText().trim().length() != 0)
        {
            partSearch.clear();
            for (Part p : inv.getAllParts())
            {
                if (p.getName().contains(modifyProductSearchField.getText().trim()))
                {
                    partSearch.add(p);
                }
            }
            partTable.setItems(partSearch);
        }
    }

    /**
     @param event Clears Text Field for Search on Click.
     LOGICAL/RUNTIME ERROR: Originally didn't have the field reset, so after searching it would just stay to the last search.  So had to add this method.
     */
    @FXML
    void clearSearchBar(MouseEvent event)
    {
        Object portal = event.getSource();
        TextField field = (TextField) portal;
        field.setText("");
        partInventory.setAll(inv.getAllParts());
        partTable.setItems(partInventory);
        partTable.refresh();
    }

    /**
     @param <T> TableColumn for formatting Price.
     FUTURE ENHANCEMENTS: Saw this through the recorded sessions.  Not sure exactly how useful it is to separate out the price just so you can format and add a dollar sign; would probably remove.
     */
    private <T> TableColumn<T, Double> priceFormatting()
    {
        TableColumn<T, Double> priceColumn = new TableColumn("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("Price"));
        priceColumn.setCellFactory((TableColumn<T, Double> column) ->
        {
            return new TableCell<T, Double>()
            {
                @Override
                protected void updateItem(Double item, boolean empty)
                {
                    if (!empty)
                    {
                        setText("$" + String.format("%10.2f", item));
                    }
                    else
                    {
                        setText("");
                    }
                }
            };
        });
        return priceColumn;
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
                Warnings.productErrors(1, field);
                return true;
            }
            if (field == modifyProductPrice && Double.parseDouble(field.getText().trim()) < 0)
            {
                Warnings.productErrors(5, field);
                warning = true;
            }
        }
        catch (Exception e)
        {
            warning = true;
            Warnings.productErrors(3, field);
            System.out.println(e);
        }
        return warning;
    }

    /**
     @param field Checks Text Field Types.
     FUTURE ENHANCEMENTS: Not sure how much regex is needed here, would probably find a better way to check the input format.
     */
    private boolean typeCheck(TextField field)
    {
        if (field == modifyProductPrice & !field.getText().trim().matches("\\d+(\\.\\d+)?"))
        {
            Warnings.productErrors(3, field);
            return true;
        }
        if (field != modifyProductPrice & !field.getText().trim().matches("[0-9]*"))
        {
            Warnings.productErrors(3, field);
            return true;
        }
        return false;
    }
}

