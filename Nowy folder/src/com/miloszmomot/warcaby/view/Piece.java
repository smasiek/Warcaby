package com.miloszmomot.warcaby.view;

import com.miloszmomot.warcaby.PieceColor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.awt.*;

public class Piece extends StackPane {
    private int x;
    private int y;
    private int width = 100;
    private int height = 100;
    private PieceColor pieceColor;
    private boolean round;
    private ImageView image = new ImageView();

    //mouse behaviour saved to piece dragging
    private double mouseX, mouseY;
    private double newMouseX, newMouseY;

    public Piece(int x, int y, PieceColor pieceColor) {
        this.x = x * height;
        this.y = y * width;
        this.pieceColor = pieceColor;

        move(this.x, this.y);

        if (pieceColor == PieceColor.LIGHT) {
            Image image = new Image("main/java/warcaby/com/miloszmomot/resources/bialy pionek.png");
            this.image.setImage(image);
        } else if (pieceColor == PieceColor.DARK) {
            Image image = new Image("main/java/warcaby/com/miloszmomot/resources/ciemny pionek.png");
            this.image.setImage(image);
        }
        getChildren().addAll(image);

        setOnMousePressed(e -> {
            if(round) {
                newMouseX = e.getSceneX();
                newMouseY = e.getSceneY();
            }
        });

        setOnMouseDragged(event -> {
            if(round) {
                relocate(event.getSceneX() - newMouseX + mouseX, event.getSceneY() - newMouseY + mouseY);
            }
        });
    }

    public PieceColor getPieceColor() {
        return pieceColor;
    }

    public void move(int x, int y) {
        mouseX = x;
        mouseY = y;
        relocate(mouseX, mouseY);
    }

    public void setNewCoords(double x, double y) {
        this.x = (int) x;
        this.y = (int) y;
        move(this.x, this.y);
    }

   public double[] getOldMouseCoords() {
        return new double[]{mouseX, mouseY};
    }

    public void setRound(boolean isYourRound){
        round=isYourRound;
    }

    public void changeRound(){
        round=!round;
    }

    public void makeKing(PieceColor color){
        Image image=null;
        if(color==PieceColor.LIGHT){
             image= new Image("main/java/warcaby/com/miloszmomot/resources/biała damka.png");
        } else if (color==PieceColor.DARK){
            image=new Image("main/java/warcaby/com/miloszmomot/resources/ciemna damka.png");
        }
        this.image.setImage(image);
    }

}
