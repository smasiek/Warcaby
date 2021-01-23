package com.miloszmomot.warcaby.view;

import com.miloszmomot.warcaby.PieceColor;
import javafx.event.Event;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;


public class Piece extends StackPane {
    private int x;
    private int y;
    private PieceColor pieceColor;
    private boolean round;
    private ImageView image = new ImageView();

    //mouse behaviour saved to piece dragging
    private double mouseX, mouseY;
    private double newMouseX, newMouseY;

    public Piece(int x, int y, PieceColor pieceColor) {
        int height = 100;
        this.x = x * height;
        int width = 100;
        this.y = y * width;
        this.pieceColor = pieceColor;

        move(this.x, this.y);

        if (pieceColor == PieceColor.LIGHT) {
            Image image = new Image("com/miloszmomot/warcaby/resources/bialy pionek.png");
            this.image.setImage(image);
        } else if (pieceColor == PieceColor.DARK) {
            Image image = new Image("com/miloszmomot/warcaby/resources/ciemny pionek.png");
            this.image.setImage(image);
        }
        getChildren().addAll(image);

        setOnMousePressed(e -> {
            if(round) {
                newMouseUpdate(e);
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

    private void newMouseUpdate(MouseEvent e){
        newMouseX = e.getSceneX();
        newMouseY = e.getSceneY();
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
             image= new Image("com/miloszmomot/warcaby/resources/biała damka.png");
        } else if (color==PieceColor.DARK){
            image=new Image("com/miloszmomot/warcaby/resources/ciemna damka.png");
        }
        this.image.setImage(image);
    }

}
