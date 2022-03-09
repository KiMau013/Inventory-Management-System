/**
 Class for MainController.java
 */

/**
 @author Ki Mau
 */
package Controllers;

import Model.Part;
import Model.Product;
import Model.Inventory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 Class for controlling MainController.fxml.
 FUTURE ENHANCEMENTS: Still hardcoded a few Alerts, so would pass those off to the Warnings class.  Also so many repeated FXMLLoader... Would find a way to pass that through a function instead, too.
 LOGICAL/RUNTIME ERROR: Once the GUI was set up, didn't have very many issues connecting everything.  I did give all kinds of id's that ended up being unnecessary, so I deleted all of those.
 */
public class MainController implements Initializable {

    Inventory inv;

    @FXML
    private TableView<Product> mainProductTable;

    @FXML
    private TextField mainProductSearchField;

    @FXML
    private TableView<Part> mainPartTable;

    @FXML
    private TextField mainPartSearchField;

    @FXML
    private ObservableList<Product> productList = FXCollections.observableArrayList();
    private ObservableList<Product> productSearch = FXCollections.observableArrayList();
    private ObservableList<Part> partInventory = FXCollections.observableArrayList();
    private ObservableList<Part> partSearch = FXCollections.observableArrayList();

    /**
    @param inv Sets inventory for the Main Controller.
     */
    public MainController(Inventory inv)
    {
        this.inv = inv;
    }

    /**
     @param event Escape!
     */
    @FXML
    private void mainExit(ActionEvent event) {
        System.exit(0);
    }

    /**
     Initialize Class
     @param url URL
     @param rb ResourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        getPartsTable();
        getProductsTable();
    }

    /**
     Sets the Parts Table
     */
    private void getPartsTable()
    {
        partInventory.setAll(inv.getAllParts());
        TableColumn<Part, Double> priceColumn = priceFormatting();
        mainPartTable.getColumns().addAll(priceColumn);
        mainPartTable.setItems(partInventory);
        mainPartTable.refresh();
    }

    /**
     Sets the Products Table
     */
    private void getProductsTable()
    {
        productList.setAll(inv.getAllProducts());
        TableColumn<Product, Double> priceColumn = priceFormatting();
        mainProductTable.getColumns().addAll(priceColumn);
        mainProductTable.setItems(productList);
        mainProductTable.refresh();
    }

    /**
     @param event Clears Search field on click
     */
    @FXML
    void clearSearchBar(MouseEvent event)
    {
        Object portal = event.getSource();
        TextField field = (TextField) portal;
        field.setText("");
        if (mainPartSearchField == field)
        {
            if (partInventory.size() != 0)
            {
                mainPartTable.setItems(partInventory);
            }
        }
        if (mainProductSearchField == field) {
            if (productList.size() != 0)
            {
                mainProductTable.setItems(productList);
            }
        }
    }


    /**
     @param <T> Formatting for Price.
    LOGICAL/RUNTIME ERROR: Took a minute to get this going properly.  Originally had updateItem incorrect it didn't work properly.
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
     @param event Loads AddPart.fxml
     */
    @FXML
    private void mainPartAdd(ActionEvent event)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/AddPart.fxml"));
            AddPartController controller = new AddPartController(inv);
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
     @param event Delete a part from the Part Table
     */
    @FXML
    private void mainPartDelete(ActionEvent event)
    {
        Part deletedPart = mainPartTable.getSelectionModel().getSelectedItem();
        if (partInventory.isEmpty())
        {
            Alert warning = new Alert(Alert.AlertType.ERROR);
            warning.setTitle("Part Error");
            warning.setHeaderText("Inventory is Empty");
            warning.setContentText("There is nothing to select.");
            warning.showAndWait();
            return;
        }
        if (!partInventory.isEmpty() && deletedPart == null)
        {
            Alert warning = new Alert(Alert.AlertType.ERROR);
            warning.setTitle("Part Error");
            warning.setHeaderText("Selection Invalid");
            warning.setContentText("A Part must be selected to continue.");
            warning.showAndWait();
            return;
        }
        if (partCheck(deletedPart))
        {
            productsNparts(deletedPart.getName());
            return;
        }
        else
        {
            boolean confirmation = verifyDeletePart(deletedPart.getName());
            if (!confirmation)
            {
                return;
            }
            inv.deletePart(deletedPart);
            partInventory.remove(deletedPart);
            mainPartTable.refresh();
        }
    }

    /**
     @param part Checks that part doesn't exist in an associated parts list
     @return True if successful else False
     */
    public boolean partCheck(Part part)
    {
        boolean Check = false;
        for (int i = 0; i < productList.size(); i++)
        {
            if (productList.get(i).getAssociatedParts().contains(part))
            {
                Check = true;
            }
        }
        return Check;
    }

    /**
     @param event Loads ModifyPart.fxml
     */
    @FXML
    private void mainPartModify(ActionEvent event)
    {
        try
        {
            Part current = mainPartTable.getSelectionModel().getSelectedItem();
            if (partInventory.isEmpty())
            {
                Alert warning = new Alert(Alert.AlertType.ERROR);
                warning.setTitle("Part Error");
                warning.setHeaderText("Inventory is Empty");
                warning.setContentText("There is nothing to select.");
                warning.showAndWait();
                return;
            }
            if (!partInventory.isEmpty() && current == null)
            {
                Alert warning = new Alert(Alert.AlertType.ERROR);
                warning.setTitle("Part Error");
                warning.setHeaderText("Selection Invalid");
                warning.setContentText("A Part must be selected to continue.");
                warning.showAndWait();
                return;
            }
            else
            {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/ModifyPart.fxml"));
                ModifyPartController controller = new ModifyPartController(inv, current);
                loader.setController(controller);
                Parent root = loader.load();
                Scene scene = new Scene(root);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.setResizable(false);
                stage.show();
            }
        }
        catch (IOException e) {}
    }

    /**
     @param event Search Button for searching what's in the Part Search Field
     */
    @FXML
    void mainPartSearch(ActionEvent event)
    {
        if (!mainPartSearchField.getText().trim().isEmpty())
        {
            mainPartTable.setItems(inv.lookupPart(mainPartSearchField.getText().trim()));
            mainPartTable.refresh();
        }
    }

    /**
     @param event Loads AddProduct.fxml
     */
    @FXML
    private void mainProductAdd(ActionEvent event)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/AddProduct.fxml"));
            AddProductController controller = new AddProductController(inv);
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
     @param event Delete a Product from the Product Table
     */
    @FXML
    private void mainProductDelete(ActionEvent event)
    {
        boolean isDeleted = false;
        Product deletedProduct = mainProductTable.getSelectionModel().getSelectedItem();
        if (productList.isEmpty())
        {
            Alert warning = new Alert(Alert.AlertType.ERROR);
            warning.setTitle("Part Error");
            warning.setHeaderText("Inventory is Empty");
            warning.setContentText("There is nothing to select.");
            warning.showAndWait();
            return;
        }
        if (!productList.isEmpty() && deletedProduct == null)
        {
            Alert warning = new Alert(Alert.AlertType.ERROR);
            warning.setTitle("Part Error");
            warning.setHeaderText("Selection Invalid");
            warning.setContentText("A Part must be selected to continue.");
            warning.showAndWait();
            return;
        }
        if (deletedProduct.getPartsSize() == 0)
        {
            boolean confirmation = verifyDeleteProduct(deletedProduct.getName());
            if (!confirmation)
            {
                return;
            }
        }
        else
        {
            partsNproducts(deletedProduct.getName());
            return;
        }
        inv.deleteProduct(deletedProduct.getId());
        productList.remove(deletedProduct);
        mainProductTable.setItems(productList);
        mainProductTable.refresh();
    }

    /**
     @param event Loads ModifyProduct.fxml
     */
    @FXML
    private void mainProductModify(ActionEvent event)
    {
        try
        {
            Product selectedProduct = mainProductTable.getSelectionModel().getSelectedItem();
            if (productList.isEmpty())
            {
                Alert warning = new Alert(Alert.AlertType.ERROR);
                warning.setTitle("Product Error");
                warning.setHeaderText("Inventory is Empty");
                warning.setContentText("There is nothing to select.");
                warning.showAndWait();
                return;
            }
            if (!productList.isEmpty() && selectedProduct == null)
            {
                Alert warning = new Alert(Alert.AlertType.ERROR);
                warning.setTitle("Product Error");
                warning.setHeaderText("Selection Invalid");
                warning.setContentText("A Product must be selected to continue.");
                warning.showAndWait();
                return;
            }
            else
            {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/ModifyProduct.fxml"));
                ModifyProductController controller = new ModifyProductController(inv, selectedProduct);
                loader.setController(controller);
                Parent root = loader.load();
                Scene scene = new Scene(root);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.setResizable(false);
                stage.show();
            }
        }
        catch (IOException e) {}
    }

    /**
     @param event Search Button for Product Search Field
     */
    @FXML
    private void mainProductSearch(ActionEvent event)
    {
        if (!mainProductSearchField.getText().trim().isEmpty())
        {
            productSearch.clear();
            for (Product product : inv.getAllProducts())
            {
                if (product.getName().contains(mainProductSearchField.getText().trim()))
                {
                    productSearch.add(product);
                }
            }
            mainProductTable.setItems(productSearch);
            mainProductTable.refresh();
        }
    }

    /**
     @param name Verify that you want to delete Part
     */
    private boolean verifyDeletePart(String name)
    {
        Alert warning = new Alert(Alert.AlertType.CONFIRMATION);
        warning.setTitle("Delete Part");
        warning.setHeaderText("Confirm Delete of " + name);
        warning.setContentText("Click OK to Confirm");
        Optional<ButtonType> answer = warning.showAndWait();
        if (answer.get() == ButtonType.OK)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     @param name Verify that you want to delete Product
     */
    private boolean verifyDeleteProduct(String name)
    {
        Alert warning = new Alert(Alert.AlertType.CONFIRMATION);
        warning.setTitle("Delete Product");
        warning.setHeaderText("Confirm Delete of " + name);
        warning.setContentText("Click OK to Confirm");
        Optional<ButtonType> answer = warning.showAndWait();
        if (answer.get() == ButtonType.OK)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     @param name warning that product still has parts
     */
    private void partsNproducts(String name)
    {
        Alert warning = new Alert(Alert.AlertType.INFORMATION);
        warning.setTitle("Confirmed");
        warning.setHeaderText(null);
        warning.setContentText(name + " still has parts that are assigned to it and has not been deleted.");
        warning.showAndWait();
    }

    /**
     @param name warning that part still belongs to at least one product
     */
    private void productsNparts(String name)
    {
        Alert warning = new Alert(Alert.AlertType.INFORMATION);
        warning.setTitle("Confirmed");
        warning.setHeaderText(null);
        warning.setContentText(name + " is still assigned to at least one product and has not been deleted.");
        warning.showAndWait();
    }

}
