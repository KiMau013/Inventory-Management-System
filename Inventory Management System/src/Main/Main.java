/**
@author Ki Mau
*/

package Main;

import Model.InHouse;
import Model.Outsource;
import Model.Inventory;
import Model.Part;
import Model.Product;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 Start of Program.
 FUTURE ENHANCEMENTS: Adding more varied data to the sampleData in order to show off search function better.
 LOGICAL/RUNTIME ERROR: N/A
 */
public class Main extends Application
{
    /**
     Main method
     @param args for main
     */
    public static void main(String[] args) { launch(args); }

    /**
      Start method.
      @param mainStage Stage to set.
      @throws Exception load.
      LOGICAL/RUNTIME ERROR: Did not originally initialize Inventory into a new Inventory and it kept overwriting itself.  Instantiated new.
     */
    @Override
    public void start(Stage mainStage) throws Exception
    {
        Inventory inv = new Inventory();
        this.sampleData(inv);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/Main.fxml"));
        Controllers.MainController controller = new Controllers.MainController(inv);
        loader.setController(controller);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        mainStage.setScene(scene);
        mainStage.setResizable(false);
        mainStage.show();
    }

    /**
     Method to fill tables with generic data
     @param inv generic inventory
     */
    void sampleData(Inventory inv)
     {
        Part addPart1 = new InHouse(1, "Test Part 1", 10.99, 10, 1, 15, 1);
        inv.addPart(addPart1);
        Part addPart2 = new InHouse(2, "Test Part 2", 12.55, 12, 2, 25, 3);
        inv.addPart(addPart2);
        Part addPart3 = new Outsource(3, "Test Part 3", 1.99, 1, 1, 10, "Fake Company");
        inv.addPart(addPart3);
        Part addPart4 = new Outsource(4, "Test Part 4", 4.99, 3, 1, 8, "Test LLC");
        inv.addPart(addPart4);
        Part addPart5 = new Outsource(5, "Test Part 5", 9.99, 14, 1, 20, "Programming Inc");
        inv.addPart(addPart5);

        Product addProduct1 = new Product(1, "Test Product 1",11.00,5,1,7);
        addProduct1.addAssociatedPart(addPart1);
        addProduct1.addAssociatedPart(addPart4);
        inv.addProduct(addProduct1);
        Product addProduct2 = new Product(2, "Test Product 2",22.22,1,1,3);
        addProduct2.addAssociatedPart(addPart2);
        addProduct2.addAssociatedPart(addPart4);
        addProduct2.addAssociatedPart(addPart3);
        inv.addProduct(addProduct2);
        Product addProduct3 = new Product(3, "Test Product 3",33.99,6,5,6);
        addProduct3.addAssociatedPart(addPart5);
        inv.addProduct(addProduct3);
    }
}
