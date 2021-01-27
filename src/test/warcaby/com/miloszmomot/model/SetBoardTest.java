package warcaby.com.miloszmomot.model;

import javafx.stage.Stage;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import warcaby.com.miloszmomot.Controller;
import warcaby.com.miloszmomot.PieceColor;
import warcaby.com.miloszmomot.PieceType;

import static org.junit.Assert.*;

public class SetBoardTest  extends TestFXBase{

    @Test
    public void setBoard() {
        //part of typical startup routine, not included in TestFXBase to make testing pieces easier
        controller.setLogicBoardPieces();
        controller.setBoardGUIPieces();

        //Check distinctive fields
        assertFalse(boardLogic.getBoard()[0][0].getPlaceable());
        assertNull(boardLogic.getBoard()[0][0].getPiece());

        assertFalse(boardLogic.getBoard()[7][7].getPlaceable());
        assertNull(boardLogic.getBoard()[7][7].getPiece());

        assertTrue(boardLogic.getBoard()[0][1].getPlaceable());
        Piece testedPiece=boardLogic.getBoard()[0][1].getPiece();
        assertNotNull(testedPiece);
        assertEquals(testedPiece.getPieceColor(), PieceColor.DARK);
        assertEquals(testedPiece.getType(), PieceType.MEN);
        assertEquals(testedPiece.getMoveDirection(), -1);

        assertTrue(boardLogic.getBoard()[1][0].getPlaceable());
        testedPiece=boardLogic.getBoard()[1][0].getPiece();
        assertNotNull(testedPiece);

        assertTrue(boardLogic.getBoard()[6][7].getPlaceable());
         testedPiece=boardLogic.getBoard()[6][7].getPiece();
        assertNotNull(testedPiece);
        assertEquals(testedPiece.getPieceColor(), PieceColor.LIGHT);
        assertEquals(testedPiece.getType(), PieceType.MEN);
        assertEquals(testedPiece.getMoveDirection(), 1);

        assertTrue(boardLogic.getBoard()[7][6].getPlaceable());
        testedPiece=boardLogic.getBoard()[7][6].getPiece();
        assertNotNull(testedPiece);
    }
}