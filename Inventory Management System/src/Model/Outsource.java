/**
 Class for Outsource.java.
 */

/**
 @author Ki Mau
 */
package Model;

/**
 Class for Outsource extension of Part.
 FUTURE ENHANCEMENTS: Maybe treat InHouse and Outsource as the same, but with 1 change, instead of 2 separate classes.
 LOGICAL/RUNTIME ERROR: N/A
 */
public class Outsource extends Part
{
    private String Company;

    public Outsource(int id, String name, double price, int stock, int min, int max, String company)
    {
        super(id, name, price, stock, min, max);
        this.Company = company;
    }

    /**
     @param company Sets Company
     */
    public void setCompany(String company)
    {
        this.Company = company;
    }

    /**
     @return Returns Company
     */
    public String getCompany()
    {
        return Company;
    }
}