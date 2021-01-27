package warcaby.com.miloszmomot.model;

import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationTest;
import warcaby.com.miloszmomot.Controller;
import warcaby.com.miloszmomot.view.BoardGUI;
import warcaby.com.miloszmomot.view.CheckersWindow;

import java.util.concurrent.TimeoutException;

public class TestFXBase extends ApplicationTest {

    BoardLogic boardLogic;
    BoardGUI boardGUI;
    Controller controller;

    @Override
    public void start(Stage stage) throws Exception {
        controller=new Controller(stage);
        controller.setLogicBoard();
        controller.setBoardGUI();

        boardLogic=controller.getBoard();
        boardGUI=controller.getBoardGUI();

        Scene scene = new Scene(boardGUI.getRoot(), 790, 790);

        stage.setScene(scene);
    }

    @AfterEach
    void afterEach() throws TimeoutException {
        FxToolkit.cleanupStages();
    }


    @Before
    public void setUp() throws Exception {
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                boardLogic.getBoard()[y][x] = new Field(x, y);
            }
        }
    }
}
