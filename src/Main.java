import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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
        ViewLoader<AnchorPane, ChatController> viewLoader = new ViewLoader<>("Chat.fxml");
        viewLoader.getController().setUserName(getUserName());
        viewLoader.getController().setHost("localhost");
        viewLoader.getController().setPort(9001);
        viewLoader.getController().run();
        Scene scene = new Scene(viewLoader.getLayout());
        primaryStage.setScene(scene);
        primaryStage.setTitle(appName);
        primaryStage.setOnHiding( e -> primaryStage_Hiding(e, viewLoader.getController()));
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }

    private void primaryStage_Hiding(WindowEvent e, ChatController controller) {
        try {
            controller.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
