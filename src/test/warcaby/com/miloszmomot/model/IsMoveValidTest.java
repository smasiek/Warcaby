package warcaby.com.miloszmomot.model;

import org.junit.jupiter.api.Test;
import warcaby.com.miloszmomot.MoveType;
import warcaby.com.miloszmomot.PieceColor;
import warcaby.com.miloszmomot.PieceType;

import static org.junit.Assert.*;

public class IsMoveValidTest extends TestFXBase {

    @Test
    public void normalMoveDarkMen() {
        //Set board to simulate normal move
        Piece darkPiece=new Piece(PieceColor.DARK, PieceType.MEN);
        boardLogic.getBoard()[4][3].setPiece(darkPiece);

        assertEquals(boardLogic.isMoveValid(4,5,3,4), MoveType.NORMAL);
        assertEquals(boardLogic.isMoveValid(2,5,3,4), MoveType.NORMAL);
        assertEquals(boardLogic.isMoveValid(1,6,3,4), MoveType.INVALID);
        assertEquals(boardLogic.isMoveValid(5,6,3,4), MoveType.INVALID);
        assertEquals(boardLogic.isMoveValid(2,3,3,4), MoveType.INVALID);
        assertEquals(boardLogic.isMoveValid(4,3,3,4), MoveType.INVALID);
        assertEquals(boardLogic.isMoveValid(1,2,3,4), MoveType.INVALID);
        assertEquals(boardLogic.isMoveValid(5,2,3,4), MoveType.INVALID);

        boardLogic.setRound(PieceColor.DARK);
        boardLogic.move(4,5,3,4,MoveType.NORMAL);
        assertNotNull(boardLogic.getBoard()[5][4].getPiece());
    }

    @Test
    public void normalMoveLightMen() {
        //Set board to simulate normal move
        Piece lightPiece=new Piece(PieceColor.LIGHT, PieceType.MEN);
        boardLogic.getBoard()[4][3].setPiece(lightPiece);

        assertEquals(boardLogic.isMoveValid(2,3,3,4), MoveType.NORMAL);
        assertEquals(boardLogic.isMoveValid(4,3,3,4), MoveType.NORMAL);
        assertEquals(boardLogic.isMoveValid(4,5,3,4), MoveType.INVALID);
        assertEquals(boardLogic.isMoveValid(2,5,3,4), MoveType.INVALID);
        assertEquals(boardLogic.isMoveValid(1,6,3,4), MoveType.INVALID);
        assertEquals(boardLogic.isMoveValid(5,6,3,4), MoveType.INVALID);
        assertEquals(boardLogic.isMoveValid(1,2,3,4), MoveType.INVALID);
        assertEquals(boardLogic.isMoveValid(5,2,3,4), MoveType.INVALID);

        boardLogic.setRound(PieceColor.LIGHT);
        boardLogic.move(4,3,3,4,MoveType.NORMAL);
        assertNotNull(boardLogic.getBoard()[3][4].getPiece());
    }

    @Test
    public void normalMoveLightKing() {
        //Set board to simulate normal move
        Piece lightPiece=new Piece(PieceColor.LIGHT, PieceType.KING);
        boardLogic.getBoard()[4][3].setPiece(lightPiece);

        assertEquals(boardLogic.isMoveValid(2,3,3,4), MoveType.NORMAL);
        assertEquals(boardLogic.isMoveValid(4,3,3,4), MoveType.NORMAL);
        assertEquals(boardLogic.isMoveValid(4,5,3,4), MoveType.NORMAL);
        assertEquals(boardLogic.isMoveValid(2,5,3,4), MoveType.NORMAL);
        assertEquals(boardLogic.isMoveValid(1,6,3,4), MoveType.NORMAL);
        assertEquals(boardLogic.isMoveValid(5,6,3,4), MoveType.NORMAL);
        assertEquals(boardLogic.isMoveValid(1,2,3,4), MoveType.NORMAL);
        assertEquals(boardLogic.isMoveValid(5,2,3,4), MoveType.NORMAL);

        boardLogic.setRound(PieceColor.LIGHT);
        boardLogic.move(5,2,3,4,MoveType.NORMAL);
        assertNotNull(boardLogic.getBoard()[2][5].getPiece());
    }

    @Test
    public void findOptimalMoveMen(){
        Piece lightPiece=new Piece(PieceColor.LIGHT, PieceType.MEN);
        boardLogic.getBoard()[4][3].setPiece(lightPiece);
        boardGUI.setPiece(3,4,PieceColor.LIGHT);

        Piece darkPiece=new Piece(PieceColor.DARK, PieceType.MEN);
        boardLogic.getBoard()[2][5].setPiece(darkPiece);
        boardGUI.setPiece(5,2,PieceColor.DARK);

        //set round
        boardLogic.setRound(PieceColor.LIGHT);
        //make white move towards dark to calculate optimal dark's move
        boardLogic.move(4,3,3,4,MoveType.NORMAL);
        //use killEnemy to clear old visualisation of piece
        boardGUI.killEnemy(3,4);
        boardGUI.setPiece(4,3,PieceColor.LIGHT);

        assertNull(boardGUI.getNodeUsingIndex(3,4));
        assertNotNull(boardGUI.getNodeUsingIndex(4,3));

        assertEquals(boardLogic.getRound(),PieceColor.DARK);
        assertTrue(boardLogic.isBestMoveFound());
        assertTrue(boardLogic.isBestPieceFound());

        assertTrue(boardLogic.isKillAvailable());

        assertEquals(boardLogic.getBestPieceToMove(),darkPiece);

        assertEquals(boardLogic.getBestMoveDirectionX(),-1);
        assertEquals(boardLogic.getBestMoveDirectionY(),1);

    }

    @Test
    public void killMen() {
        //Set board to simulate normal move
        Piece lightPiece=new Piece(PieceColor.LIGHT, PieceType.MEN);
        boardLogic.getBoard()[4][3].setPiece(lightPiece);
        boardGUI.setPiece(3,4,PieceColor.LIGHT);

        Piece darkPiece=new Piece(PieceColor.DARK, PieceType.MEN);
        boardLogic.getBoard()[2][5].setPiece(darkPiece);
        boardGUI.setPiece(5,2,PieceColor.DARK);

        //set round
        boardLogic.setRound(PieceColor.LIGHT);
        //make white move towards dark to calculate optimal dark's move
        boardLogic.move(4,3,3,4,MoveType.NORMAL);
        //use killEnemy to clear old visualisation of piece
        boardGUI.killEnemy(3,4);
        boardGUI.setPiece(4,3,PieceColor.LIGHT);

        assertEquals(boardLogic.getBestPieceToMove(),darkPiece);

        assertEquals(boardLogic.isMoveValid(6,3,5,2), MoveType.INVALID);
        assertEquals(boardLogic.isMoveValid(4,3,5,2), MoveType.INVALID);
        assertEquals(boardLogic.isMoveValid(3,4,5,2), MoveType.KILL);
    }

    @Test
    public void killSequenceMen() {
        //Set board to simulate normal move
        Piece lightPiece=new Piece(PieceColor.LIGHT, PieceType.MEN);
        boardLogic.getBoard()[4][3].setPiece(lightPiece);
        boardGUI.setPiece(3,4,PieceColor.LIGHT);
        boardLogic.getBoard()[5][2].setPiece(lightPiece);
        boardGUI.setPiece(2,5,PieceColor.LIGHT);

        Piece darkPiece=new Piece(PieceColor.DARK, PieceType.MEN);
        boardLogic.getBoard()[2][5].setPiece(darkPiece);
        boardGUI.setPiece(5,2,PieceColor.DARK);

        //set round
        boardLogic.setRound(PieceColor.LIGHT);
        //make white move towards dark to calculate optimal dark's move
        boardLogic.move(4,3,3,4,MoveType.NORMAL);
        //use killEnemy to clear old visualisation of piece
        boardGUI.killEnemy(3,4);
        boardGUI.setPiece(4,3,PieceColor.LIGHT);


        assertEquals(boardLogic.getBestPieceToMove(),darkPiece);

        assertEquals(boardLogic.isMoveValid(6,3,5,2), MoveType.INVALID);
        assertEquals(boardLogic.isMoveValid(4,3,5,2), MoveType.INVALID);
        assertEquals(MoveType.KILL, boardLogic.isMoveValid(3,4,5,2));
        boardLogic.move(3,4,5,2,MoveType.KILL);

        assertEquals(boardLogic.getRound(),PieceColor.DARK);

        assertTrue(boardLogic.isBestMoveFound());
        assertTrue(boardLogic.isBestPieceFound());

        assertTrue(boardLogic.isKillAvailable());

        assertEquals(boardLogic.getBestPieceToMove(),darkPiece);

        assertEquals(boardLogic.getBestMoveDirectionX(),-1);
        assertEquals(boardLogic.getBestMoveDirectionY(),1);

        assertEquals(MoveType.INVALID,boardLogic.isMoveValid(5,2,3,4));
        assertEquals(MoveType.INVALID,boardLogic.isMoveValid(4,5,3,4));
        assertNotNull(boardLogic.getBoard()[5][2].getPiece());
        assertEquals(MoveType.KILL,boardLogic.isMoveValid(1,6,3,4));
    }

    @Test
    public void betterKillSequenceMen() {
        //Set board to simulate normal move
        Piece lightPiece=new Piece(PieceColor.LIGHT, PieceType.MEN);
        boardLogic.getBoard()[2][5].setPiece(lightPiece);
        boardGUI.setPiece(5,2,PieceColor.LIGHT);
        boardLogic.getBoard()[3][4].setPiece(lightPiece);
        boardGUI.setPiece(4,3,PieceColor.LIGHT);
        boardLogic.getBoard()[5][2].setPiece(lightPiece);
        boardGUI.setPiece(2,5,PieceColor.LIGHT);
        boardLogic.getBoard()[3][6].setPiece(lightPiece);
        boardGUI.setPiece(6,3,PieceColor.LIGHT);


        Piece darkPiece=new Piece(PieceColor.DARK, PieceType.MEN);
        boardLogic.getBoard()[0][7].setPiece(darkPiece);
        boardGUI.setPiece(7,0,PieceColor.DARK);

        //set round
        boardLogic.setRound(PieceColor.LIGHT);
        //make white move towards dark to calculate optimal dark's move
        boardLogic.move(6,1,5,2,MoveType.NORMAL);
        //use killEnemy to clear old visualisation of piece
        boardGUI.killEnemy(5,2);
        boardGUI.setPiece(6,1,PieceColor.LIGHT);

        assertTrue(boardLogic.isBestMoveFound());
        assertTrue(boardLogic.isBestPieceFound());

        assertEquals(darkPiece,boardLogic.getBestPieceToMove());

        assertEquals(boardLogic.getBestMoveDirectionX(),-1);
        assertEquals(boardLogic.getBestMoveDirectionY(),1);

        assertEquals(MoveType.KILL, boardLogic.isMoveValid(5,2,7,0));

        boardLogic.move(5,2,7,0,MoveType.KILL);
        boardGUI.killEnemy(7,0);
        boardGUI.setPiece(5,2,PieceColor.DARK);

        assertEquals(boardLogic.getRound(),PieceColor.DARK);

        assertTrue(boardLogic.isBestMoveFound());
        assertTrue(boardLogic.isBestPieceFound());

        assertTrue(boardLogic.isKillAvailable());

        //attempt to chose worse kill sequence
        assertEquals(MoveType.INVALID,boardLogic.isMoveValid(7,4,5,2));
        assertEquals(MoveType.KILL,boardLogic.isMoveValid(3,4,5,2));

        boardLogic.move(3,4,5,2,MoveType.KILL);
        boardGUI.killEnemy(5,2);
        boardGUI.setPiece(3,4,PieceColor.DARK);

        assertEquals(boardLogic.getRound(),PieceColor.DARK);

        assertEquals(MoveType.INVALID,boardLogic.isMoveValid(3,6,3,4));
        assertEquals(MoveType.KILL,boardLogic.isMoveValid(1,6,3,4));
    }

    @Test
    public void menWithBetterSequence(){
        Piece lightPiece=new Piece(PieceColor.LIGHT, PieceType.MEN);
        boardLogic.getBoard()[4][3].setPiece(lightPiece);
        boardGUI.setPiece(3,4,PieceColor.LIGHT);

        boardLogic.getBoard()[5][2].setPiece(lightPiece);
        boardGUI.setPiece(2,5,PieceColor.LIGHT);

        Piece darkPiece=new Piece(PieceColor.DARK, PieceType.MEN);
        boardLogic.getBoard()[2][3].setPiece(darkPiece);
        boardGUI.setPiece(3,2,PieceColor.DARK);

        boardLogic.getBoard()[2][5].setPiece(darkPiece);
        boardGUI.setPiece(5,2,PieceColor.DARK);

        //set round
        boardLogic.setRound(PieceColor.LIGHT);
        assertEquals(MoveType.NORMAL, boardLogic.isMoveValid(4,3,3,4));
        boardLogic.move(4,3,3,4,MoveType.NORMAL);
        boardGUI.killEnemy(3,4);
        boardGUI.setPiece(4,3,PieceColor.LIGHT);

        assertEquals(PieceColor.DARK,boardLogic.getRound());
        //try to move piece with worse kill score
        assertEquals(MoveType.INVALID, boardLogic.isMoveValid(5,4,3,2));
        //proper kill execution
        assertEquals(MoveType.KILL, boardLogic.isMoveValid(3,4,5,2));
    }

    @Test
    public void killKing(){
        Piece lightPiece=new Piece(PieceColor.LIGHT, PieceType.MEN);
        boardLogic.getBoard()[4][3].setPiece(lightPiece);
        boardGUI.setPiece(3,4,PieceColor.LIGHT);

        Piece darkPiece=new Piece(PieceColor.DARK, PieceType.KING);
        boardLogic.getBoard()[0][7].setPiece(darkPiece);
        boardGUI.setPiece(7,0,PieceColor.DARK);

        //set round
        boardLogic.setRound(PieceColor.LIGHT);
        //make white move towards dark to calculate optimal dark's move
        boardLogic.move(4,3,3,4,MoveType.NORMAL);
        //use killEnemy to clear old visualisation of piece
        boardGUI.killEnemy(3,4);
        boardGUI.setPiece(4,3,PieceColor.LIGHT);

        //check if optimal move is found
        assertTrue(boardLogic.isBestMoveFound());
        assertTrue(boardLogic.isBestPieceFound());

        assertTrue(boardLogic.isKillAvailable());

        assertEquals(boardLogic.getBestPieceToMove(),darkPiece);
        assertEquals(boardLogic.getBestPieceToMove().getType(),darkPiece.getType());

        //make normal move instead of killing
        assertEquals(MoveType.INVALID,boardLogic.isMoveValid(4,3,7,0));

        //move onto enemy piece
        assertEquals(MoveType.INVALID,boardLogic.isMoveValid(4,3,7,0));


        //kill against the rules
        assertEquals(MoveType.INVALID,boardLogic.isMoveValid(0,7,7,0));

        //execute kill
        assertEquals(MoveType.KILL,boardLogic.isMoveValid(3,4,7,0));
    }

    @Test
    public void  sequenceKillKing() {

        Piece lightPiece = new Piece(PieceColor.LIGHT, PieceType.MEN);

        boardLogic.getBoard()[3][4].setPiece(lightPiece);
        boardGUI.setPiece(4, 3, PieceColor.LIGHT);

        boardLogic.getBoard()[1][2].setPiece(lightPiece);
        boardGUI.setPiece(2, 1, PieceColor.LIGHT);

        Piece darkPiece=new Piece(PieceColor.DARK, PieceType.KING);
        boardLogic.getBoard()[0][7].setPiece(darkPiece);
        boardGUI.setPiece(7,0,PieceColor.DARK);

        //set round
        boardLogic.setRound(PieceColor.LIGHT);
        //make white move towards dark to calculate optimal dark's move
        assertEquals(MoveType.NORMAL,boardLogic.isMoveValid(5,2,4,3));
        boardLogic.move(5,2,4,3,MoveType.NORMAL);
        //use killEnemy to clear old visualisation of piece
        boardGUI.killEnemy(3,4);
        boardGUI.setPiece(4,3,PieceColor.LIGHT);

        assertEquals(PieceColor.DARK,boardLogic.getRound());

        //normal move instead of killing
        assertEquals(MoveType.INVALID,boardLogic.isMoveValid(6,1,7,0));

        //execute kill
        assertEquals(MoveType.KILL,boardLogic.isMoveValid(4,3,7,0));

        boardLogic.move(4,3,7,0,MoveType.KILL);
        //use killEnemy to clear old visualisation of piece
        boardGUI.killEnemy(7,0);
        boardGUI.setPiece(4,3,PieceColor.DARK);

        assertEquals(PieceColor.DARK,boardLogic.getRound());

        //try to move light piece in dark's round
        assertEquals(MoveType.INVALID,boardLogic.isMoveValid(3,0,2,1));

        //execute 2nd kill
        assertEquals(MoveType.KILL,boardLogic.isMoveValid(1,0,4,3));
    }

}
