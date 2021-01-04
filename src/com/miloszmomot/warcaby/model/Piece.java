package com.miloszmomot.warcaby.model;

import com.miloszmomot.warcaby.PieceColor;
import com.miloszmomot.warcaby.PieceType;

public class Piece {
    private PieceColor pieceColor;
    private PieceType pieceType;
    private int moveDirection;

    public Piece(PieceColor pieceColor, PieceType pieceType){
        this.pieceColor=pieceColor;
        this.pieceType=pieceType;
        if(pieceColor.equals(PieceColor.LIGHT)){
            moveDirection=1;
        } else{
            moveDirection=-1;
        }
    }

    public PieceColor getPieceColor() {
        return pieceColor;
    }

    public int getMoveDirection() {
        return moveDirection;
    }

    public PieceType getType() {
        return pieceType;
    }
    public void makeKing(){
        this.pieceType=PieceType.KING;
    }
}
