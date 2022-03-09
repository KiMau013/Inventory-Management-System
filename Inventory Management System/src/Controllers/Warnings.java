/**
 Class for Warnings.java
 */

/**
 @author Ki Mau
 */

package Controllers;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import java.util.Optional;

/**
 Class for the alert warnings for the Controllers.
 FUTURE ENHANCEMENTS: Would add even more functionality to the Warnings system.  There are still places I hard-coded in Alerts and warnings.
 LOGICAL/RUNTIME ERROR: Had the wrong cases in a few places, so I moved them around in here.
 */
public class Warnings {

    /**
     @param errorNum Generates part error based on passed in number
     @param field Cause of Error
     */
    public static void partErrors(int errorNum, TextField field)
    {
        Alert warning = new Alert(Alert.AlertType.ERROR);
        warning.setTitle("Part Add Error");
        warning.setHeaderText("Error!  Unable to add part!");
        switch (errorNum)
        {
            case 1:
            {
                warning.setContentText("Text Field is Empty");
                break;
            }
            case 2:
            {
                warning.setContentText("Please select either InHouse or Outsource radio button at top.");
                break;
            }
            case 3:
            {
                warning.setContentText("Invalid Format");
                break;
            }
            case 4:
            {
                warning.setContentText("Invalid Name");
                break;
            }
            case 5:
            {
                warning.setContentText("Values unable to be 0 or below.");
                break;
            }
            case 6:
            {
                warning.setContentText("Inventory Issue - Can not be lower than minimum.");
                break;
            }
            case 7:
            {
                warning.setContentText("Inventory Issue - Can not be higher than maximum.");
                break;
            }
            case 8:
            {
                warning.setContentText("Min is higher than Max");
                break;
            }
            case 9:
            {
                warning.setContentText("Machine ID must be a valid number.");
                break;
            }
            default:
            {
                warning.setContentText("UNKNOWN ERROR");
                break;
            }
        }
        warning.showAndWait();
    }

    /**
     @param errorNum Generates part error based on passed in number.
     @param field Cause of Error.
     FUTURE ENHANCEMENTS: Get rid of this section, and just have a large Warning list instead.
     LOGICAL/RUNTIME ERROR: N/A
     */
    public static void productErrors(int errorNum, TextField field)
    {
        Alert warning = new Alert(Alert.AlertType.ERROR);
        warning.setTitle("Product Add Error");
        warning.setHeaderText("Error!  Unable to add product!");
        switch (errorNum)
        {
            case 1:
            {
                warning.setContentText("Text Field is Empty");
                break;
            }
            case 2:
            {
                warning.setContentText("This part is already associated with that product.");
                break;
            }
            case 3:
            {
                warning.setContentText("Invalid Format");
                break;
            }
            case 4:
            {
                warning.setContentText("Invalid Name");
                break;
            }
            case 5:
            {
                warning.setContentText("Values unable to be 0 or below.");
                break;
            }
            case 6:
            {
                warning.setContentText("Product can not be cheaper than the sum of its parts.");
                break;
            }
/*            case 7:
            {
                warning.setContentText("At least 1 part must belong to a product.");
                break;
            }*/
            case 8:
            {
                warning.setContentText("Inventory Issue - Can not be lower than minimum.");
                break;
            }
            case 9:
            {
                warning.setContentText("Inventory Issue - Can not be higher than maximum.");
                break;
            }
            case 10:
            {
                warning.setContentText("Min is higher than Max");
                break;
            }
            default:
            {
                warning.setContentText("UNKNOWN ERROR");
                break;
            }
        }
        warning.showAndWait();
    }

    /**
     @param name Confirm delete of passed in name
     @return Returns the answer
     */
    public static boolean confirmDelete(String name)
    {
        Alert warning = new Alert(Alert.AlertType.CONFIRMATION);
        warning.setTitle("Delete Part");
        warning.setHeaderText("Confirm delete: " + name);
        warning.setContentText("Click OK to confirm");
        Optional<ButtonType> answer = warning.showAndWait();
        return answer.get() == ButtonType.OK;
    }

    /**
    @return Returns Button answer for Cancellation
     */
    public static boolean cancelWarning()
    {
        Alert warning = new Alert(Alert.AlertType.CONFIRMATION);
        warning.setTitle("Cancel");
        warning.setHeaderText("Are you sure you'd like to cancel?");
        warning.setContentText("Click OK to confirm");
        Optional<ButtonType> answer = warning.showAndWait();
        return answer.get() == ButtonType.OK;
    }

    /**
     @param errorNum Generates part error based on passed in number
     @param name Cause of Error
     */
    public static void informationDialog(int errorNum, String name)
    {
        if (errorNum != 2)
        {
            Alert warning = new Alert(Alert.AlertType.INFORMATION);
            warning.setTitle("Verified");
            warning.setHeaderText(null);
            warning.setContentText(name + " has been removed");
            warning.showAndWait();
        }
        else
        {
            Alert warning = new Alert(Alert.AlertType.INFORMATION);
            warning.setTitle("Verified");
            warning.setHeaderText(null);
            warning.setContentText("ERROR");
        }
    }
}
