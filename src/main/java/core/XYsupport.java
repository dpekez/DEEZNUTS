package core;

import entities.Entity;

import java.util.Random;

public class XYsupport {

    /**
     * Generates a random move vector, skips zero vectors (0, 0).
     * Generates vectors like  1, -1
     * -1,  0
     * 0,  1
     * 1,  1
     * ...
     *
     * @return the generated vector
     */

    public static XY generateRandomMoveVector() {
        Random random = new Random();
        int x, y;
        do {
            x = random.nextInt(3) - 1;
            y = random.nextInt(3) - 1;
        } while (x == 0 && y == 0);
        return new XY(x, y);
    }

    /**
     * Generates and returns a random and empty location inside of the
     * walled game board.
     *
     * @param boardSize the board size
     * @param entities  an array of every entity currently on the board
     * @return the random location
     */

    static XY generateRandomLocation(XY boardSize, Entity[] entities) {
        Random random = new Random();
        boolean isNotEmpty;
        XY xy;
        do {
            isNotEmpty = false;
            int randomX = random.nextInt(boardSize.getX() - 2) + 1;
            int randomY = random.nextInt(boardSize.getY() - 2) + 1;
            xy = new XY(randomX, randomY);
            for (Entity entity : entities)
                if (entity != null && entity.getLocation().getX() == xy.getX() &&
                        entity.getLocation().getY() == xy.getY()) {
                    isNotEmpty = true;
                }
        } while (isNotEmpty);
        return xy;
    }

    public static boolean isInRange(XY m, XY lL, XY uR) {
        return (m.getX() >= lL.getX()) && (m.getX() <= uR.getX()) && (m.getY() >= lL.getY()) && (m.getY() <= uR.getY());
    }

    public static XY assignMoveVector(XY xy) {
        XY moveVector = XY.ZERO_ZERO;
        int oldX = xy.getX();
        int oldY = xy.getY();

        switch (oldX) {
            case 0:
                switch (oldY) {
                    case 0:
                        break;
                    case 1:
                        moveVector = XY.UP;
                        break;
                    case -1:
                        moveVector = XY.DOWN;
                        break;
                }
                break;
            case 1:
                switch (oldY) {
                    case 0:
                        moveVector = XY.RIGHT;
                        break;
                    case 1:
                        moveVector = XY.RIGHT_UP;
                        break;
                    case -1:
                        moveVector = XY.RIGHT_DOWN;
                        break;
                }
                break;
            case -1:
                switch (oldY) {
                    case 0:
                        moveVector = XY.LEFT;
                        break;
                    case 1:
                        moveVector = XY.LEFT_UP;
                        break;
                    case -1:
                        moveVector = XY.LEFT_DOWN;
                        break;
                }
                break;
        }
        return moveVector;
    }
}