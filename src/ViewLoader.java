import javafx.fxml.FXMLLoader;

import java.io.IOException;

public class ViewLoader<T,U> {
    private T fxmlLayout = null;
    private U fxmlController = null;
    public ViewLoader(String fxml){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(fxml));
            fxmlLayout = fxmlLoader.load();
            fxmlController = fxmlLoader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
        }
        public T getLayout(){ return fxmlLayout; }
        public U getController() {
            return fxmlController;
        }
    }
