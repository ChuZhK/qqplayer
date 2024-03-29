package MyQQPlayer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Objects;

public class QQPlayer extends Application {

    private Scene scene;
    private double offsetX,offsetY;

    public void start(Stage primaryStage) throws Exception{

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/player.fxml")));

        scene=new Scene(root,null);
        primaryStage.setScene(scene);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.getIcons().add(new Image(getClass().getResource("/img/logo.png").toExternalForm()));
        primaryStage.show();

        scene.setOnMousePressed(event -> {
            offsetX = event.getSceneX();
            offsetY = event.getSceneY();
        });

        scene.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX()-offsetX);
            primaryStage.setY(event.getScreenY()-offsetY);
        });

    }

    public static void main(String[] args){
        launch(args);
    }
}
