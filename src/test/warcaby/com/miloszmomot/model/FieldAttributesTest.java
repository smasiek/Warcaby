package warcaby.com.miloszmomot.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FieldAttributesTest extends TestFXBase{

    @Test
    public void isFieldPlaceable() {
        assertFalse(boardLogic.getBoard()[0][0].getPlaceable());
        assertFalse(boardLogic.isFieldPlaceable(0,0));

        assertFalse(boardLogic.getBoard()[7][7].getPlaceable());
        assertFalse(boardLogic.isFieldPlaceable(7,7));

        assertTrue(boardLogic.getBoard()[0][1].getPlaceable());
        assertTrue(boardLogic.isFieldPlaceable(1,0));

        assertTrue(boardLogic.getBoard()[1][0].getPlaceable());
        assertTrue(boardLogic.isFieldPlaceable(0,1));

        assertTrue(boardLogic.getBoard()[6][7].getPlaceable());
        assertTrue(boardLogic.isFieldPlaceable(7,6));

        assertTrue(boardLogic.getBoard()[7][6].getPlaceable());
        assertTrue(boardLogic.isFieldPlaceable(6,7));
    }

    @Test
    public void isFieldOccupied() {
        assertNull(boardLogic.getBoard()[0][0].getPiece());
        assertFalse(boardLogic.isFieldOccupied(0,0));

        assertNull(boardLogic.getBoard()[7][7].getPiece());
        assertFalse(boardLogic.isFieldOccupied(7,7));

        assertNotNull(boardLogic.getBoard()[0][1].getPiece());
        assertTrue(boardLogic.isFieldOccupied(1,0));

        assertNotNull(boardLogic.getBoard()[1][0].getPiece());
        assertTrue(boardLogic.isFieldOccupied(0,1));

        assertNotNull(boardLogic.getBoard()[6][7].getPiece());
        assertTrue(boardLogic.isFieldOccupied(7,6));

        assertNotNull(boardLogic.getBoard()[7][6].getPiece());
        assertTrue(boardLogic.isFieldOccupied(6,7));
    }
}