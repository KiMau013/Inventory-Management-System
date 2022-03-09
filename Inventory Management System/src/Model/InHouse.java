/**
 Class for InHouse.java.
 */

/**
 @author Ki Mau
 */
package Model;

/**
 Class for InHouse extension of Part.
 FUTURE ENHANCEMENTS: Same as Outsource; Find a better way to not have 2 instances of classes so similar.
 LOGICAL/RUNTIME ERROR: N/A
 */
public class InHouse extends Part
{
    private int MachineId;

    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId)
    {
        super(id, name, price, stock, min, max);
        this.MachineId = machineId;
    }

    /**
     @param machineId Sets MachineID
     */
    public void setMachineId(int machineId)
    {
        this.MachineId = machineId;
    }

    /**
     @return Returns MachineID
     */
    public int getMachineId()
    {
        return MachineId;
    }
}