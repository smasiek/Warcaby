package com.miloszmomot.warcaby.model;

public class Field {
    private Piece piece;
    private boolean placeable;

    public Field(Field field){
        //copy constructor
        piece=field.getPiece();
        placeable=field.getPlaceable();
    }

    public Field(int x, int y) {
        piece = null;
        placeable = (x + y) % 2 != 0;
    }

    public boolean getPlaceable() {
        return placeable;
    }
    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public Piece getPiece() {
        return piece;
    }
}
