/**
 Class for AddProductController.java
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
import java.util.Locale;
import java.util.Random;
import java.util.ResourceBundle;

/**
 Class for controlling AddProductController.fxml.
 FUTURE ENHANCEMENTS: Duplicate data to ModifyProductController.  Would combine somehow.
 LOGICAL/RUNTIME ERROR: Had issues saving Associated Parts to a specific product.  Took a hot minute to get that figured out.
 */
public class AddProductController implements Initializable {

    Inventory inv;

    @FXML
    private TextField addProductID;

    @FXML
    private TextField addProductName;

    @FXML
    private TextField addProductInventory;

    @FXML
    private TextField addProductPrice;

    @FXML
    private TextField addProductMax;

    @FXML
    private TextField addProductMin;

    @FXML
    private TextField addProductSearchField;

    @FXML
    private TableView<Part> partTable;

    @FXML
    private TableView<Part> addedPartTable;

    private ObservableList<Part> partInventory = FXCollections.observableArrayList();
    private ObservableList<Part> partSearch = FXCollections.observableArrayList();
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();

    /**
     @param inv Sets the inventory for this controller
     */
    public AddProductController(Inventory inv)
    {
        this.inv = inv;
    }

    /**
     Initialize Class
     @param url URL
     @param rb ResourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        productIDGenerator();
        partFillTable();
    }

    /**
     Generates an ID for New Product.
     LOGICAL/RUNTIME ERROR: Originally had an incrementer, but ran into issues deleting and adding, so tried the num generator method.
     */
    public void productIDGenerator()
    {
        boolean match;
        Random randomNum = new Random();
        Integer num = randomNum.nextInt(1000);

        if (inv.productsSize() == 0)
        {
            addProductID.setText(num.toString());
        }
        if (inv.productsSize() == 1000)
        {
            Warnings.productErrors(3, null);
        }
        else
        {
            match = numVerify(num);
            if(match == false)
            {
                addProductID.setText(num.toString());
            }
            else
            {
                productIDGenerator();
            }
        }
    }

    /**
     @param num Verifies ID doesn't already exist
     */
    private boolean numVerify(Integer num)
    {
        Part pair = inv.lookupPart(num);
        return pair != null;
    }

    /**
     Fills part table and formats associated parts table.
     LOGICAL/RUNTIME ERROR: addedPartTable did not format Price originally.  So had to populate the format on load, even if it wasn't being used until a Part was added to the associated parts.
     */
    private void partFillTable()
    {
        partInventory.setAll(inv.getAllParts());
        TableColumn<Part, Double> partPrice = priceFormatting();
        partTable.getColumns().addAll(partPrice);
        partTable.setItems(partInventory);
        partTable.refresh();

        //Sets the price column for addedPartTable as well.
        TableColumn<Part, Double> priceForProduct = priceFormatting();
        addedPartTable.getColumns().addAll(priceForProduct);
        addedPartTable.refresh();
    }

    /**
     @param event Adds part from table to associated parts table
     */
    @FXML
    void addProductAdd(ActionEvent event)
    {
        Part part = partTable.getSelectionModel().getSelectedItem();
        boolean repeated = false;
        if (part != null)
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
     @param event Returns to main if cancelled
     */
    @FXML
    void addProductCancel(ActionEvent event)
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
     */
    @FXML
    void addProductRemovePart(ActionEvent event)
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
        else
        {
            return;
        }
        if (removed)
        {
            Warnings.informationDialog(1, part.getName());
        }
        else
        {
            Warnings.informationDialog(2,"");
        }
    }

    /**
     @param event Saves Product with associated parts to Inventory
     */
    @FXML
    void addProductSave(ActionEvent event) {
        boolean finished = false;
        TextField[] fields = {addProductInventory, addProductPrice, addProductMin, addProductMax};
        double cost = 0;
        for (int i = 0; i < associatedParts.size(); i++) {
            cost += associatedParts.get(i).getPrice();
        }
        if (addProductName.getText().trim().isEmpty() || addProductName.getText().trim().toLowerCase().equals("")) {
            Warnings.productErrors(4, addProductName);
            return;
        }
        for (TextField fields1 : fields)
        {
            boolean errorValue = valueCheck(fields1);
            if (errorValue)
            {
                finished = true;
                break;
            }
            boolean errorType = typeCheck(fields1);
            if (errorType)
            {
                finished = true;
                break;
            }
        }
        if (Integer.parseInt(addProductMin.getText().trim()) > Integer.parseInt(addProductMax.getText().trim())) {
            Warnings.productErrors(10, addProductMin);
            return;
        }
        if (Integer.parseInt(addProductInventory.getText().trim()) < Integer.parseInt(addProductMin.getText().trim())) {
            Warnings.productErrors(8, addProductInventory);
            return;
        }
        if (Integer.parseInt(addProductInventory.getText().trim()) > Integer.parseInt(addProductMax.getText().trim())) {
            Warnings.productErrors(9, addProductInventory);
            return;
        }
        if (Double.parseDouble(addProductPrice.getText().trim()) < cost)
        {
            Warnings.productErrors(6, addProductPrice);
            return;
        }
        if (associatedParts.isEmpty())
        {
            Alert partZero = new Alert(Alert.AlertType.WARNING);
            partZero.setTitle("Product Has No Parts");
            partZero.setHeaderText("Warning!  Product had no parts associated.");
            partZero.setContentText("Product Saved");
            partZero.showAndWait();

            Product product = new Product(Integer.parseInt(addProductID.getText().trim()), addProductName.getText().trim(), Double.parseDouble(addProductPrice.getText().trim()), Integer.parseInt(addProductInventory.getText().trim()), Integer.parseInt(addProductMin.getText().trim()), Integer.parseInt(addProductMax.getText().trim()));
            for (int i = 0; i < associatedParts.size(); i++)
            {
                product.addAssociatedPart(associatedParts.get(i));
            }
            inv.addProduct(product);
            returnToMain(event);
        }
        else {
            Product product = new Product(Integer.parseInt(addProductID.getText().trim()), addProductName.getText().trim(), Double.parseDouble(addProductPrice.getText().trim()), Integer.parseInt(addProductInventory.getText().trim()), Integer.parseInt(addProductMin.getText().trim()), Integer.parseInt(addProductMax.getText().trim()));
            for (int i = 0; i < associatedParts.size(); i++)
            {
                product.addAssociatedPart(associatedParts.get(i));
            }
            inv.addProduct(product);
            returnToMain(event);
        }
    }

    /**
     @param event Searches for Parts that match criteria to Add
     */
    @FXML
    void addProductSearch(ActionEvent event)
    {
        if (!addProductSearchField.getText().trim().isEmpty())
        {
            partInventory.clear();
            partTable.setItems(inv.lookupPart(addProductSearchField.getText().trim()));
            partTable.refresh();
        }
    }

    /**
     @param event Clears Text Field for Search on Click
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
     @param <T> TableColumn for formatting Price
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
            if (field == addProductPrice && Double.parseDouble(field.getText().trim()) < 0)
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
     @param field Checks Text Field Types
     */
    private boolean typeCheck(TextField field)
    {
        if (field == addProductPrice & !field.getText().trim().matches("\\d+(\\.\\d+)?"))
        {
            Warnings.productErrors(3, field);
            return true;
        }
        if (field != addProductPrice & !field.getText().trim().matches("[0-9]*"))
        {
            Warnings.productErrors(3, field);
            return true;
        }
        return false;
    }
}
