package com.miloszmomot.warcaby;

import com.miloszmomot.warcaby.model.BoardLogic;
import com.miloszmomot.warcaby.view.BoardGUI;
import javafx.stage.Stage;

public class Controller {
    private BoardLogic board = new BoardLogic(this);
    private BoardGUI boardGUI = new BoardGUI(this);
    private Stage primaryStage;

    public Controller(Stage stage){
        primaryStage=stage;
    }

    public BoardLogic getBoard() {
        return board;
    }

    public BoardGUI getBoardGUI() {
        return boardGUI;
    }

    public boolean isFieldPlaceable(int x, int y) {
        return board.isFieldPlaceable(x, y);
    }

    public PieceColor placedPieceColor(int x, int y) {
        return board.placedPieceColor(x, y);
    }

    public boolean isFieldOccupied(int x, int y) {
        return board.isFieldOccupied(x, y);
    }

    public MoveType isMoveValid(int newIndexX, int newIndexY, int oldIndexX, int oldIndexY) {
        return board.isMoveValid(newIndexX, newIndexY, oldIndexX, oldIndexY);
    }

    public void move(int newIndexX, int newIndexY, int oldIndexX, int oldIndexY, MoveType currentMoveType) {
        board.move(newIndexX, newIndexY, oldIndexX, oldIndexY, currentMoveType);
    }

    public boolean killEnemy(int enemyX, int enemyY) {
        return boardGUI.killEnemy( enemyX, enemyY);
    }

    public void startGame() {
        board.startGame();
        System.out.println("Game started");
    }

    public void setRound(PieceColor round) {
        boardGUI.setRound(round);
    }

    public void changeRound() {
        boardGUI.changeRound();
    }

    public void makeKing(int oldIndexX, int oldIndexY, PieceColor color){
        boardGUI.makeKing( oldIndexX,  oldIndexY,color);
    }

    public void restartGame() {
        board.restartGame();
        boardGUI.restartGame();
        board.startGame();
    }

    public void win(PieceColor gameResult) {
        boardGUI.win(gameResult);
    }

    public void close(){
        primaryStage.close();
    }
}
