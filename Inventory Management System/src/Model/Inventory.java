/**
 Class for Inventory.java.
 */

/**
 @author Ki Mau
 */
package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 Class for Inventory.
 FUTURE ENHANCEMENTS: There's a lot going on in the Inventory class, including things that might fit better under the Part/Product models.
 LOGICAL/RUNTIME ERROR: Trying to pass full parts and products through had problems for me, so I switched to comparing against their IDs.
 */
public class Inventory {
    private ObservableList<Part> allParts = FXCollections.observableArrayList();
    private ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     @param newPart Adds Part to AllParts
     */
    public void addPart(Part newPart) {
        if (newPart != null) {
            allParts.add(newPart);
        }
    }

    /**
     @param newProduct Adds Product to AllProducts
     */
    public void addProduct(Product newProduct) {
        if (newProduct != null) {
            this.allProducts.add(newProduct);
        }
    }

    /**
     @param partId Looks up Part by ID
     @return Returns all parts that match search
     */
    public Part lookupPart(int partId) {
        if (!allParts.isEmpty()) {
            for (int i = 0; i < allParts.size(); i++) {
                if (allParts.get(i).getId() == partId) {
                    return allParts.get(i);
                }
            }
        }
        return null;
    }

    /**
     @param productId Looks up Product by ID
     @return Returns all products that match search
     */
    public Product lookupProduct(int productId) {
        if (!allProducts.isEmpty()) {
            for (int i = 0; i < allProducts.size(); i++) {
                if (allProducts.get(i).getId() == productId) {
                    return allProducts.get(i);
                }
            }
        }
        return null;
    }

    /**
     @param partName Looks up Part by Name
     @return Returns all parts that match search
     */
    public ObservableList<Part> lookupPart(String partName) {
        if (!allParts.isEmpty()) {
            ObservableList searchParts = FXCollections.observableArrayList();
            for (Part p : getAllParts()) {
                if (p.getName().contains(partName)) {
                    searchParts.add(p);
                }
            }
            return searchParts;
        }
        return null;
    }

    /**
     @param productName Looks up Product by Name
     @return Returns all products that match search
     */
    public ObservableList<Product> lookupProduct(String productName) {
        if (!allProducts.isEmpty()) {
            ObservableList searchProducts = FXCollections.observableArrayList();
            for (Product p : getAllProducts()) {
                if (p.getName().contains(productName)) {
                    searchProducts.add(p);
                }
            }
            return searchProducts;
        }
        return null;
    }

    /**
     @param selectedPart Updates AllParts with Part
     */
    public void updatePart(Part selectedPart)
{
    for (int i = 0; i < allParts.size(); i++)
    {
        if (allParts.get(i).getId() == selectedPart.getId())
        {
            allParts.set(i, selectedPart);
            break;
        }
    }
    return;
}

    /**
     @param selectedProduct Updates AllProducts with Product
     */
    public void updateProduct(Product selectedProduct)
    {
        for (int i = 0; i < allProducts.size(); i++)
        {
            if (allProducts.get(i).getId() == selectedProduct.getId())
            {
                allProducts.set(i, selectedProduct);
                break;
            }
        }
        return;
    }

    /**
     @param selectedPart Deletes Part from AllParts.
     @return True if successful else False.
     LOGICAL/RUNTIME ERROR: Deleting by Part instead of the ID of the part threw errors, switched to using Int compared to ID.
     */
    public boolean deletePart(Part selectedPart)
    {
        for (int i = 0; i < allParts.size(); i++)
        {
            if(allParts.get(i).getId() == selectedPart.getId())
            {
                allParts.remove(i);
                return true;
            }
        }
        return false;
    }

    /**
     @param selectedProduct Deletes Product from AllProducts.
     @return True if successful else False.
     LOGICAL/RUNTIME ERROR: Deleted by product instead of ID threw some errors, so switched to using Int and comparing to ID.
     */
    public boolean deleteProduct(int selectedProduct)
   {
        for (int i = 0; i < allProducts.size(); i++)
        {
            if(allProducts.get(i).getId() == selectedProduct)
            {
                allProducts.remove(i);
                return true;
            }
        }
        return false;
    }

    /**
    @return All Parts
     */
    public ObservableList<Part> getAllParts()
    {
        return allParts;
    }


    /**
     @return Size of List of All Parts
     */
    public int partsSize()
    {
        return allParts.size();
    }


    /**
     @return All Products
     */
    public ObservableList<Product> getAllProducts()
    {
        return allProducts;
    }


    /**
     @return Size of List of All Products
     */
    public int productsSize()
    {
        return allProducts.size();
    }

}
