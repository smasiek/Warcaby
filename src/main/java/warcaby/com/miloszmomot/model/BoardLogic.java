package warcaby.com.miloszmomot.model;

import warcaby.com.miloszmomot.Controller;
import warcaby.com.miloszmomot.MoveType;
import warcaby.com.miloszmomot.PieceColor;
import warcaby.com.miloszmomot.PieceType;

import static warcaby.com.miloszmomot.MoveType.INVALID;
import static warcaby.com.miloszmomot.MoveType.KILL;
import static java.lang.Math.abs;

public class BoardLogic {
    private int boardSize = 8;

    private Field[][] board = new Field[boardSize][boardSize];
    private Controller controller;
    private PieceColor round;

    private Piece bestPieceToMove;
    private boolean bestPieceFound;

    private int bestMoveDirectionX;
    private int bestMoveDirectionY;
    private boolean bestMoveFound;

    private boolean killAvailable;

    private int lightCounter;
    private int darkCounter;

    public BoardLogic(Controller controller) {
        this.controller = controller;
    }

    public void setBoard() {
        //set all variables to default state
        bestPieceToMove = null;
        bestPieceFound = false;
        bestMoveDirectionX = 0;
        bestMoveDirectionY = 0;
        bestMoveFound = false;
        killAvailable = false;
        lightCounter = 12;
        darkCounter = 12;
        for (int y = 0; y < boardSize; y++) {
            for (int x = 0; x < boardSize; x++) {
                board[y][x] = new Field(x, y);
            }
        }
    }

    public void setPieces() {
        for (int y = 0; y < boardSize; y++) {
            for (int x = 0; x < boardSize; x++) {
                if (y < 3 && board[y][x].getPlaceable()) {
                    board[y][x].setPiece(new Piece(PieceColor.DARK, PieceType.MEN));
                } else if (y > 4 && board[y][x].getPlaceable()) {
                    board[y][x].setPiece(new Piece(PieceColor.LIGHT, PieceType.MEN));
                }
            }
        }
    }

    public boolean isFieldPlaceable(int x, int y) {
        return board[y][x].getPlaceable();
    }

    public boolean isFieldOccupied(int x, int y) {
        if (board[y][x].getPiece() == null) {
            return false;
        } else {
            return true;
        }
    }

    public PieceColor placedPieceColor(int x, int y) {
        return board[y][x].getPiece().getPieceColor();
    }

    public MoveType isMoveValid(int newIndexX, int newIndexY, int oldIndexX, int oldIndexY) {
        //checks user's move and decide which type of move it was

        if (!isFieldPlaceable(newIndexX, newIndexY) || isFieldOccupied(newIndexX, newIndexY)) {
            return INVALID;
        }

        if (bestPieceFound) {
            if (bestPieceToMove != board[oldIndexY][oldIndexX].getPiece()) {
                //check if chosen piece is the best piece to move
                return INVALID;
            }
            if (bestMoveFound) {
                int yDir = (newIndexY - oldIndexY < 0) ? -1 : 1;
                int xDir = (newIndexX - oldIndexX < 0) ? -1 : 1;
                if (xDir != bestMoveDirectionX || yDir != bestMoveDirectionY) {
                    return INVALID;
                }
            }
        }

        if (board[oldIndexY][oldIndexX].getPiece().getType().equals(PieceType.MEN)) {
            if (abs(newIndexY - oldIndexY) == 2) {
                //kill attempt
                int enemyY = newIndexY - ((newIndexY - oldIndexY) / 2);
                int enemyX = newIndexX - ((newIndexX - oldIndexX) / 2);

                if (isEnemyKilled(enemyX, enemyY, board)) {
                    //check if kill will be executed properly
                    board[enemyY][enemyX].setPiece(null);
                    //controller.killEnemy(enemyX, enemyY);
                    if (controller.killEnemy(enemyX, enemyY)) {
                        //update view after killing
                        if (round == PieceColor.LIGHT) {
                            darkCounter -= 1;
                        } else {
                            lightCounter -= 1;
                        }
                        return KILL;
                    } else {
                        return INVALID;
                    }
                }
            }

            //TODO: program mozna ulepszyc: krol i pionek dziedziczą z bierki, kazdy na swoj sposob zabija i sie porusza

            if (board[oldIndexY][oldIndexX].getPiece().getMoveDirection() == (oldIndexY - newIndexY)) {
                //check if men moves in right direction
                if (killAvailable) {
                    //if user execute normal move when kill is available, move is invalid
                    return INVALID;
                }
                return MoveType.NORMAL;
            }
        } else if (board[oldIndexY][oldIndexX].getPiece().getType().equals(PieceType.KING)) {

            if (abs(newIndexX - oldIndexX) == abs(newIndexY - oldIndexY)) {
                //check if king moves diagonally

                int yDir = (newIndexY - oldIndexY < 0) ? -1 : 1;
                int xDir = (newIndexX - oldIndexX < 0) ? -1 : 1;

                if (piecesInKingsWay(oldIndexX, oldIndexY, newIndexX, newIndexY, xDir, yDir) == 1)
                    //check if king is able to kill piece in its way
                    if (isEnemyKilled(newIndexX - xDir, newIndexY - yDir, board)) {
                        //check if king is placed just behind piece and try to kill
                        return killUsingKing(newIndexX, newIndexY, xDir, yDir, board);
                    } else {
                        return INVALID;
                    }
                else {
                    if (piecesInKingsWay(oldIndexX, oldIndexY, newIndexX, newIndexY, xDir, yDir) > 1) {
                        //if more pieces in kings way, it cant move over them
                        return INVALID;
                    }
                }


                if (newIndexX != oldIndexX && newIndexY != oldIndexY) {
                    //every other possibility is checked, if place is changed it is normal move
                    if (killAvailable) {
                        return INVALID;
                    }
                    return MoveType.NORMAL;
                }
            }
        }

        return MoveType.INVALID;
    }

    private MoveType killUsingKing(int newIndexX, int newIndexY, int xDir, int yDir, Field[][] board) {
        int enemyY = newIndexY - yDir;
        int enemyX = newIndexX - xDir;

        if (isEnemyKilled(enemyX, enemyY, board)) {
            board[enemyY][enemyX].setPiece(null);
            if (board == this.board) {
                //don't update GUI if passed board is temporary board
                if (controller.killEnemy(enemyX, enemyY)) {
                    if (round == PieceColor.LIGHT) {
                        darkCounter -= 1;
                    } else {
                        lightCounter -= 1;
                    }
                }
            }
            return KILL;
        }
        return INVALID;
    }

    private int piecesInKingsWay(int oldIndexX, int oldIndexY, int newIndexX, int newIndexY, int xDir, int yDir) {

        int piecesCount = 0;
        int xInKingsWay = oldIndexX + xDir;
        int yInKingsWay = oldIndexY + yDir;
        while (xInKingsWay != newIndexX && yInKingsWay != newIndexY) {
            if (board[yInKingsWay][xInKingsWay].getPiece() != null) {
                //check fields in kings way and count pieces
                piecesCount++;
            }
            xInKingsWay += xDir;
            yInKingsWay += yDir;
        }
        return piecesCount;
    }

    private boolean isEnemyKilled(int enemyX, int enemyY, Field[][] board) {
        if (board[enemyY][enemyX].getPiece() != null) {
            return !board[enemyY][enemyX].getPiece().getPieceColor().equals(round);
        }
        return false;
    }

    public void move(int newIndexX, int newIndexY, int oldIndexX, int oldIndexY, MoveType currentMoveType) {
        board[newIndexY][newIndexX].setPiece(board[oldIndexY][oldIndexX].getPiece());
        board[oldIndexY][oldIndexX].setPiece(null);

        if (reachedEnemyBase(newIndexY)) {
            //make piece a king in logic board
            board[newIndexY][newIndexX].getPiece().makeKing();
            //change its icon
            controller.makeKing(newIndexX, newIndexY, round);
        }

        //sequence of kills
        if (currentMoveType == KILL) {
            findOptimalMove(newIndexX, newIndexY);

            if (killAvailable) {

                bestPieceToMove = board[newIndexY][newIndexX].getPiece();
                bestPieceFound = true;
                return;
            }

            PieceColor gameResult = winCheck();
            //game can end only by killing someone
            if (gameResult != null) {
                controller.win(gameResult);
            }
        }

        changeRound();
        chooseBestPiece();
        if (controller != null) {
            controller.changeRound();
        }
    }

    private void changeRound() {
        if (round.equals(PieceColor.LIGHT)) {
            round = PieceColor.DARK;
        } else {
            round = PieceColor.LIGHT;
        }
    }

    private void findOptimalMove(int x, int y) {
        Field field = board[y][x];

        int[] directionKillCount = new int[]{0, 0, 0, 0};//0=top left, 1 = top right, 2 = down left, 3 = down right
        int cornerIndex = 0;

        Field[][] tempBoard = new Field[boardSize][boardSize];
        //create tempBoard to simulate kills and choose  optimal move
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                tempBoard[i][j] = new Field(board[i][j]);
            }
        }

        Piece pieceBackup;

        if (field.getPiece() != null) {
            if (field.getPiece().getType() == PieceType.MEN) {
                for (int i = -1; i <= 1; i += 2) {
                    for (int j = -1; j <= 1; j += 2) {
                        //iterate to check corners around piece
                        if (y + 2 * i >= 0 && y + 2 * i < boardSize && x + 2 * j >= 0 && x + 2 * j < boardSize) {
                            pieceBackup = tempBoard[y + i][x + j].getPiece();//save piece that will be killed abstractly
                            if (pieceBackup != null && tempBoard[y + 2 * i][x + 2 * j].getPiece() == null) {
                                if (pieceBackup.getPieceColor() != round) {

                                    tempBoard[y + 2 * i][x + 2 * j].setPiece(tempBoard[y][x].getPiece());

                                    tempBoard[y + i][x + j].setPiece(null);
                                    directionKillCount[cornerIndex] = countAvailableKills(x + 2 * j, y + 2 * i, 1, tempBoard);
                                    //recover abstract kill
                                    tempBoard[y + i][x + j].setPiece(pieceBackup);
                                    tempBoard[y + 2 * i][x + 2 * j].setPiece(null);

                                }
                            }
                        }
                        cornerIndex += 1;
                    }
                }
            }
            if (field.getPiece().getType() == PieceType.KING) {
                for (int i = -1; i <= 1; i += 2) {
                    for (int j = -1; j <= 1; j += 2) {
                        int tempCornerCount;
                        for (int k = 2; k < boardSize; k++) {
                            //checking available kills at every field in single direction
                            if (y + k * i >= 0 && y + k * i < boardSize && x + k * j >= 0 && x + k * j < boardSize) {
                                pieceBackup = tempBoard[y + k * i - i][x + k * j - j].getPiece();
                                //-i and -j to backup piece that's gonna be killed
                                int pikw = piecesInKingsWay(x, y, x + k * j, y + k * i, j, i);
                                if (pikw == 1) {
                                    if (pieceBackup != null && tempBoard[y + k * i][x + k * j].getPiece() == null) {

                                        if (pieceBackup.getPieceColor() != round) {

                                            tempBoard[y + k * i][x + k * j].setPiece(tempBoard[y][x].getPiece());
                                            tempBoard[y + i][x + j].setPiece(null);

                                            tempCornerCount = countAvailableKills(x + k * j, y + k * i, 1, tempBoard);
                                            directionKillCount[cornerIndex] = Math.max(tempCornerCount, directionKillCount[cornerIndex]);
                                            //recover abstract kill
                                            tempBoard[y + k * i][x + k * j].setPiece(null);
                                            tempBoard[y + i][x + j].setPiece(pieceBackup);
                                        }
                                    }
                                }
                                if (pikw > 1) {
                                    //if more than one stop checking this direction
                                    break;
                                }
                            }
                        }
                        cornerIndex += 1;
                    }
                }
            }

            killAvailable = true;
            if (directionKillCount[0] > directionKillCount[1] && directionKillCount[0] > directionKillCount[2] && directionKillCount[0] > directionKillCount[3]) {
                bestMoveDirectionX = -1;
                bestMoveDirectionY = -1;
                bestMoveFound = true;
            } else if (directionKillCount[1] > directionKillCount[0] && directionKillCount[1] > directionKillCount[2] && directionKillCount[1] > directionKillCount[3]) {
                bestMoveDirectionX = 1;
                bestMoveDirectionY = -1;
                bestMoveFound = true;
            } else if (directionKillCount[2] > directionKillCount[1] && directionKillCount[2] > directionKillCount[0] && directionKillCount[2] > directionKillCount[3]) {
                bestMoveDirectionX = -1;
                bestMoveDirectionY = 1;
                bestMoveFound = true;
            } else if (directionKillCount[3] > directionKillCount[1] && directionKillCount[3] > directionKillCount[2] && directionKillCount[3] > directionKillCount[0]) {
                bestMoveDirectionX = 1;
                bestMoveDirectionY = 1;
                bestMoveFound = true;
            } else if (directionKillCount[2] > 0 || directionKillCount[3] > 0 || directionKillCount[0] > 0 || directionKillCount[1] > 0) {

                bestMoveFound = false;
                bestPieceFound = false;
            } else {
                killAvailable = false;
                bestMoveFound = false;
                bestPieceFound = false;
            }
        }

    }

    private void chooseBestPiece() {
        //check if pieces can kill, pick piece with best score, set bestPieceFound

        int maxKillCount = 0;
        int tempKillCount;
        int piecesWithEqualMaxCount = 0;

        int bestPieceX = 0;
        int bestPieceY = 0;

        for (int y = 0; y < boardSize; y++) {
            for (int x = 0; x < boardSize; x++) {
                //check every piece
                Field field = board[y][x];
                if (field.getPiece() != null && field.getPiece().getPieceColor() == round) {
                    tempKillCount = countAvailableKills(x, y, 0, this.board);
                    //count piece kill score
                    if (tempKillCount > maxKillCount) {
                        maxKillCount = tempKillCount;
                        piecesWithEqualMaxCount = 1;
                        bestPieceX = x;
                        bestPieceY = y;
                    } else if (tempKillCount == maxKillCount) {
                        piecesWithEqualMaxCount += 1;
                    }
                }
            }
        }


        if (piecesWithEqualMaxCount == 1 && maxKillCount != 0) {
            bestPieceToMove = board[bestPieceY][bestPieceX].getPiece();
            bestPieceFound = true;

            findOptimalMove(bestPieceX, bestPieceY);

            System.out.println("Best piece found, kill score = " + maxKillCount);
        } else if (piecesWithEqualMaxCount > 1) {
            bestPieceToMove = null;
            bestPieceFound = false;

            if (maxKillCount == 0) {
                //if every piece has same kill score equal to 0 there is no kill available
                killAvailable = false;
                System.out.println("No piece can kill");
            } else {
                killAvailable = true;
                System.out.println("Some pieces has same kill score = " + maxKillCount + ", user can choose which to move");
            }
        }
    }

    private int countAvailableKills(int x, int y, int killCount, Field[][] board) {
        int tempKillCount = killCount;
        Field[][] tempBoard = new Field[boardSize][boardSize];
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                tempBoard[i][j] = new Field(board[i][j]);
            }
        }
        if (tempBoard[y][x].getPiece() != null) {
            if (tempBoard[y][x].getPiece().getType() == PieceType.MEN) {
                for (int i = -1; i <= 1; i += 2) {
                    for (int j = -1; j <= 1; j += 2) {
                        if (y + 2 * i >= 0 && y + 2 * i < boardSize && x + 2 * j >= 0 && x + 2 * j < boardSize) {
                            if (board[y + i][x + j].getPiece() != null && board[y + i][x + j].getPiece().getPieceColor() != round) {
                                if (board[y + 2 * i][x + 2 * j].getPiece() == null) {
                                    tempBoard[y + 2 * i][x + 2 * j].setPiece(tempBoard[y][x].getPiece());
                                    tempBoard[y + i][x + j].setPiece(null);
                                    tempBoard[y][x].setPiece(null);
                                    tempKillCount += 1 + countAvailableKills(x + 2 * j, y + 2 * i, tempKillCount, tempBoard);
                                    //check kills at new men's place recursively
                                    break;
                                }
                            }
                        }

                    }
                }
            } else {
                int xDir;
                int yDir;
                boolean isLoopStoped = false;
                for (int i = -7; i < boardSize; i++) {
                    for (int j = -7; j < boardSize; j++) {
                        if (abs((x + j) - x) == abs((y + i) - y) && i != 0 && j != 0) {
                            if (y + i >= 0 && y + i < boardSize && x + j >= 0 && x + j < boardSize) {
                                yDir = (i < 0) ? -1 : 1;
                                xDir = (j < 0) ? -1 : 1;
                                if (board[y + i - yDir][x + j - xDir].getPiece() != null && board[y + i - yDir][x + j - xDir].getPiece().getPieceColor() != round) {
                                    if (piecesInKingsWay(x, y, x + j, y + i, xDir, yDir) == 1 && board[y + i][x + j].getPiece() == null) {
                                        if (killUsingKing(x + j, y + i, xDir, yDir, tempBoard) == KILL) {
                                            tempBoard[y + i][x + j].setPiece(tempBoard[y][x].getPiece());
                                            tempBoard[y][x].setPiece(null);
                                            tempKillCount += 1 + countAvailableKills(x + j, y + i, tempKillCount, tempBoard);
                                            //check kills at new king's place recursively
                                            isLoopStoped = true; //if king killed someone, stop counting in that direction and leave loops
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (isLoopStoped) {
                        //if inner loop stopped, stop outer loop
                        break;
                    }
                }
            }
        }
        return tempKillCount;
    }

    private boolean reachedEnemyBase(int newY) {
        if (round == PieceColor.LIGHT && newY == 0) {
            return true;
        } else if (round == PieceColor.DARK && newY == 7) {
            return true;
        }
        return false;
    }

    private PieceColor winCheck() {

        if (lightCounter == 0) return PieceColor.DARK;
        if (darkCounter == 0) return PieceColor.LIGHT;
        return null;
    }

    public void startGame() {
        round = PieceColor.LIGHT;
        if (controller != null) {
            controller.setRound(round);
        }
    }

    public void restartGame() {
        setBoard();
        setPieces();
    }

    public Field[][] getBoard() {
        return board;
    }

    public Piece getBestPieceToMove() {
        return bestPieceToMove;
    }

    public boolean isBestPieceFound() {
        return bestPieceFound;
    }

    public int getBestMoveDirectionX() {
        return bestMoveDirectionX;
    }

    public int getBestMoveDirectionY() {
        return bestMoveDirectionY;
    }

    public boolean isBestMoveFound() {
        return bestMoveFound;
    }

    public boolean isKillAvailable() {
        return killAvailable;
    }

    public void setRound(PieceColor color) {
        round = color;
    }

    public PieceColor getRound() {
        return round;
    }
}
