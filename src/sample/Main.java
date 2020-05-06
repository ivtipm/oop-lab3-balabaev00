package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class Main extends Application {

    private static int width = 343;
    private static int height = 484;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("greeting.fxml"));
        primaryStage.initStyle(StageStyle.UNDECORATED); // Убираем кнопки (Закрыть,свернуть и т.п)
        primaryStage.setTitle("Боб помощник");
        primaryStage.setScene(new Scene(root, width, height));
        Image applicationIcon = new Image(getClass().getResourceAsStream("/image/icon.png"));
        primaryStage.getIcons().add(applicationIcon);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
