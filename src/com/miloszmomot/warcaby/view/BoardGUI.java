package com.miloszmomot.warcaby.view;

import com.miloszmomot.warcaby.Controller;
import com.miloszmomot.warcaby.MoveType;
import com.miloszmomot.warcaby.PieceColor;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import static java.lang.Math.pow;
import static java.lang.Math.round;

public class BoardGUI extends Parent {
    private Color moccasin = new Color(1, 0.8941176, 0.7098, 1);
    private Color chocolate = new Color(0.8235, 0.4117, 0.117647, 1);
    private Group board = new Group();
    private Group pieces = new Group();
    private int fieldSize = 100;
    private Pane root;
    private int windowSize = 800;

    private Controller controller;

    public BoardGUI(Controller controller) {
        this.controller = controller;
        root = new Pane();
        root.setPrefSize(windowSize, windowSize);
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Rectangle rectangle = paintField(i, j);
                board.getChildren().add(rectangle);
            }
        }
        root.getChildren().add(board);
        setPieces();
        root.getChildren().add(pieces);
    }

    public void setPieces(){
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if (controller.isFieldOccupied(x, y)) {
                    if (controller.placedPieceColor(x, y).equals(PieceColor.DARK)) {
                        Piece piece = new Piece(x, y, PieceColor.DARK);
                        piece.setOnMouseReleased(e -> {
                            tryMove(piece);
                        });
                        pieces.getChildren().add(piece);
                    } else if (controller.placedPieceColor(x, y).equals(PieceColor.LIGHT)) {
                        Piece piece = new Piece(x, y, PieceColor.LIGHT);
                        piece.setOnMouseReleased(e -> {
                            tryMove(piece);
                        });
                        pieces.getChildren().add(piece);
                    }
                }
            }
        }
    }

    private void tryMove(Piece piece) {

        //Rounded coords
        double newX = roundCoords(piece.getLayoutX());
        double newY = roundCoords(piece.getLayoutY());

        int newIndexX = getIndexFromCoords(newX);
        int newIndexY = getIndexFromCoords(newY);
        int oldIndexX = getIndexFromCoords(piece.getOldMouseCoords()[0]);
        int oldIndexY = getIndexFromCoords(piece.getOldMouseCoords()[1]);

        if (newX >= 800 || newY >= 800 || newX < 0 || newY < 0) {
            double[] oldCoords = piece.getOldMouseCoords();
            piece.setLayoutX(oldCoords[0]);
            piece.setLayoutY(oldCoords[1]);
            return;
        }

        MoveType currentMoveType = controller.isMoveValid(newIndexX, newIndexY, oldIndexX, oldIndexY);
        if (currentMoveType != MoveType.INVALID) {
            piece.setLayoutX(newX);
            piece.setLayoutY(newY);
            piece.setNewCoords(newX, newY);
            controller.move(newIndexX, newIndexY, oldIndexX, oldIndexY, currentMoveType);
        } else {
            double[] oldCoords = piece.getOldMouseCoords();
            piece.setLayoutX(oldCoords[0]);
            piece.setLayoutY(oldCoords[1]);
        }
    }

    private double roundCoords(double coords) {
        int digits = String.valueOf(fieldSize).length() - 1;
        return round(coords / pow(10, digits)) * pow(10, digits);
    }

    private int getIndexFromCoords(double coords) {
        int digits = String.valueOf(fieldSize).length() - 1;
        return (int) (coords / pow(10, digits));
    }

    private Rectangle paintField(int y, int x) {
        Rectangle rectangle = new Rectangle();
        rectangle.setX(x * fieldSize);
        rectangle.setY(y * fieldSize);
        rectangle.setWidth(100);
        rectangle.setHeight(100);
        if (controller.isFieldPlaceable(x, y)) {
            rectangle.setFill(chocolate);
        } else {
            rectangle.setFill(moccasin);
        }
        return rectangle;
    }

    public Parent getRoot() {
        return root;
    }

    public boolean killEnemy(int enemyX, int enemyY) {
        Node node = getNodeUsingIndex(enemyX, enemyY);
        if (node != null) {
            pieces.getChildren().remove(node);
            return true;
        }
        return false;
    }

    public Node getNodeUsingIndex(int x, int y) {
        for (Node node : pieces.getChildren()) {
            if (node.getLayoutX() == x * fieldSize && node.getLayoutY() == y * fieldSize) {
                return node;
            }
        }
        return null;
    }

    public void startGame() {
        controller.startGame();
    }

    public void setRound(PieceColor round) {
        for (Node node : pieces.getChildren()) {
            if (node instanceof Piece) {
                Piece tempPiece = (Piece) node;
                tempPiece.setRound(tempPiece.getPieceColor().equals(round));
            }
        }
    }

    public void changeRound() {
        for (Node node : pieces.getChildren()) {
            if (node instanceof Piece) {
                Piece tempPiece = (Piece) node;
                tempPiece.changeRound();
            }
        }
    }

    public void makeKing(int x, int y, PieceColor color) {
        Node node = getNodeUsingIndex(x, y);
        if (node != null) {
            pieces.getChildren().remove(node);
        }

        Piece piece = new Piece(x, y, color);
        piece.makeKing(color);
        piece.setRound(true);
        piece.setOnMouseReleased(e -> {
            tryMove(piece);
        });
        pieces.getChildren().add(piece);


    }

    public void restartGame() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Node node = getNodeUsingIndex(i, j);
                if (node != null) {
                    pieces.getChildren().remove(node);
                }
            }
        }
        setPieces();
    }

    public void win(PieceColor gameResult) {

        Stage dialogStage=new Stage();
        Pane dialogBox=new Pane();
        Text whoWon=new Text();
        Button playAgain=new Button("Play again");
        Button exit=new Button("Exit");

        dialogStage.setTitle("Wynik");
        dialogStage.setResizable(false);
        dialogStage.getIcons().add(new Image("com/miloszmomot/warcaby/resources/biała damka.png"));

        dialogBox.setPrefSize(200,100);

        whoWon.setLayoutX(25);
        whoWon.setLayoutY(40);
        whoWon.setFont(new Font("Arial",25));

        playAgain.setLayoutX(5);
        playAgain.setLayoutY(60);
        playAgain.setPrefWidth(90);

        exit.setLayoutX(105);
        exit.setLayoutY(60);
        exit.setPrefWidth(90);

        switch(gameResult){
            case LIGHT: {
                whoWon.setText("Wygrały białe!");
                break;
            }
            case DARK: {
                whoWon.setText("Wygrały brązowe!");
                break;
            }
        }

        dialogBox.getChildren().addAll(whoWon,playAgain,exit);

        playAgain.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                controller.restartGame();
                dialogStage.close();
            }
        });

        exit.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                dialogStage.close();
                controller.close();
            }
        });


        Scene scene = new Scene(dialogBox, 190, 90);

        dialogStage.setScene(scene);
        dialogStage.show();
    }
}
