package warcaby.com.miloszmomot.view;

import warcaby.com.miloszmomot.Controller;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class CheckersWindow extends Application {

    private Controller controller;


    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {

        controller=new Controller(primaryStage);
        controller.setLogicBoard();
        controller.setLogicBoardPieces();
        controller.setBoardGUI();
        controller.setBoardGUIPieces();

        primaryStage.setTitle("Warcaby");
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image("/biała damka.png"));

        BoardGUI boardGUI = controller.getBoardGUI();
        boardGUI.startGame();

        //Width and height set to 790 instead of 800 due to additional borders added by the window. Board Size is 800x800
        Scene scene = new Scene(boardGUI.getRoot(), 790, 790);

        primaryStage.setScene(scene);
        primaryStage.show();
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.R) {
                    controller.restartGame();
                    System.out.println("Gra zrestartowana");
                }
            }
        });

    }

    public Controller getController(){
        return this.controller;
    }

}
