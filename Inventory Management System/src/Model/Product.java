/**
 Class for Product.java.
 */

/**
 @author Ki Mau
 */

package Model;

import java.util.ArrayList;

/**
 Class for Product.
 FUTURE ENHANCEMENTS: Would probably treat Products as enhanced Parts.  Products can be made up of parts, but a part could technically also be a product.  Adding the "must have 1 part" limits this.
 LOGICAL/RUNTIME ERROR: Had issues using the ObservableList and getting back all items.  Switched to a simpler Array (and doing some other fixes) fixed all the issues I was having.
 */
public class Product
{
    private ArrayList<Part> associatedParts = new ArrayList<>();
    private int id;
    private String name;
    private double price = 0.0;
    private int stock = 0;
    private int min;
    private int max;
    public Product(int id, String name, double price, int stock, int min, int max)
    {
        setId(id);
        setName(name);
        setPrice(price);
        setStock(stock);
        setMin(min);
        setMax(max);
    }
    /**
    @param id Set ID
     */
    public void setId(int id)
    {
        this.id = id;
    }

    /**
     @param name Set Name
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     @param price Set Price
     */
    public void setPrice(double price)
    {
        this.price = price;
    }

    /**
     @param stock Set Stock
     */
    public void setStock(int stock)
    {
        this.stock = stock;
    }

    /**
     @param min Set Min
     */
    public void setMin(int min)
    {
        this.min = min;
    }

    /**
     @param max Set Max
     */
    public void setMax(int max)
    {
        this.max = max;
    }

    /**
    @return Product ID
     */
    public int getId()
    {
        return id;
    }

    /**
     @return Product Name
     */
    public String getName()
    {
        return name;
    }

    /**
     @return Product Price
     */
    public double getPrice()
    {
        return price;
    }

    /**
     @return Product Stock
     */
    public int getStock()
    {
        return stock;
    }

    /**
     @return Product Min
     */
    public int getMin()
    {
        return min;
    }

    /**
     @return Product Max
     */
    public int getMax()
    {
        return max;
    }

    /**
     @param part Adds part to associated parts
     */
    public void addAssociatedPart(Part part)
    {
        associatedParts.add(part);
    }

    /**
     @param partToRemove Removes part from associated Product.
     @return True if successful else False.
     FUTURE ENHANCEMENTS: Would find a better way to remove parts visually.
     LOGICAL/RUNTIME ERROR: Originally was passing in full parts, but was running into it giving me back much more than the specific ID I was looking for.  Changed to Int.
     */
    public boolean removeAssociatedPart(int partToRemove) {
        int i;
        for (i = 0; i < associatedParts.size(); i++) {
            if (associatedParts.get(i).getId() == partToRemove) {
                associatedParts.remove(i);
                return true;
            }
        }

        return false;
    }

    /**
     @param searchPart Finds parts associated with product.
     @return Associated Parts or Null.
     FUTURE ENHANCEMENTS: Would find a better way to connect Associated Parts.
     LOGICAL/RUNTIME ERROR: Same as above; Changed to Int.
     */
    public Part lookupAssociatedPart(int searchPart) {
        for (int i = 0; i < associatedParts.size(); i++) {
            if (associatedParts.get(i).getId() == searchPart) {
                return associatedParts.get(i);
            }
        }
        return null;
    }

    /**
     @return Associated Parts
     */
    public ArrayList<Part> getAssociatedParts()
    {
        return associatedParts;
    }

    /**
     @param associatedParts Set Associated Parts
     */
    public void setAssociatedParts(ArrayList<Part> associatedParts) { this.associatedParts = associatedParts; }

    /**
     * @return Size of Associated Parts
     */
    public int getPartsSize()
    {
        return associatedParts.size();
    }

}