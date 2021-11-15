/**
<<<<<<< HEAD

=======
>>>>>>> 76b047e9d7b23bcf7fcc9d3abc061642a6d620c6
 * A remake of the Acquire board game in java
 *
 * @author Michael Collier
 * @author Emily Elzinga
 * @author Benjamin Keninger
 */

package AcquireProject;

import UserInterface.UserInterface;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application{

    static UserInterface ui = new UserInterface();

    public static void main(String[] args) {

        Application.launch();

    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Acquire");
        stage.show();
        ui.setStage(stage);
        ui.goToMainMenu();
    }



}
