import PresentationLayer.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(LoginPage.showLoginPage());
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}