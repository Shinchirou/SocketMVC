import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.Optional;

public class Main extends Application {

    public static final String appName = "Sockets-JavaFX-MVC";

    private String getUserName(){
        TextInputDialog textInputDialog = new TextInputDialog("Anonymous");
        textInputDialog.setTitle("Skajpaj");
        textInputDialog.setHeaderText("Header");
        textInputDialog.setContentText("Content");
        Optional<String> result = textInputDialog.showAndWait();
        return result.orElse("Anonymous");
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        ViewLoader<AnchorPane, CalculatorController> viewLoader = new ViewLoader<>("Calculator.fxml");
        Scene scene = new Scene(viewLoader.getLayout());
        primaryStage.setScene(scene);
        primaryStage.setTitle(appName);
        primaryStage.setResizable(false);
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }


}
