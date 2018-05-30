package botimpls.mozartuss;


import botapi.ControllerContext;
import botapi.OutOfViewException;
import core.EntityType;
import core.XY;
import core.XYsupport;

import java.util.logging.Level;
import java.util.logging.Logger;

public class BotBrain {
    private final static Logger logger = Logger.getLogger(BotBrain.class.getName());

    static XY moveToNearestGoodEntity(ControllerContext context) {
        XY moveDirection = XY.ZERO_ZERO;
        XY nearestBP = BotBrain.nearestEntity(context, EntityType.BAD_PLANT);
        XY nearestBB = BotBrain.nearestEntity(context, EntityType.BAD_BEAST);
        XY nearestGB = BotBrain.nearestEntity(context, EntityType.GOOD_BEAST);
        XY nearestGP = BotBrain.nearestEntity(context, EntityType.GOOD_PLANT);
        XY nearestWW = BotBrain.nearestEntity(context, EntityType.WALL);
        assert nearestGB != null;
        assert nearestGP != null;
        assert nearestWW != null;
        assert nearestBP != null;
        assert nearestBB != null;
        XY nearestPositive;
        if (nearestGB.distanceFrom(context.locate()) < nearestGP.distanceFrom(context.locate()))
            nearestPositive = nearestGB;
        else nearestPositive = nearestGP;

        if (context.locate().distanceFrom(nearestBB) < 3) {
            if ((context.locate().distanceFrom(nearestPositive) > context.locate().distanceFrom(nearestBB))) {
                moveDirection = XYsupport.assignMoveVector(context.locate().reduceVector(nearestBB));
            }
        } else if ((context.locate().distanceFrom(nearestPositive)) < 16) {
            moveDirection = XYsupport.assignMoveVector(nearestPositive.reduceVector(context.locate()));
        } else if ((context.locate().distanceFrom(nearestBP)) < 2) {
            moveDirection = XYsupport.assignMoveVector(context.locate().reduceVector(nearestBP));
        } else if ((context.locate().distanceFrom(nearestWW)) < 2) {
            moveDirection = XYsupport.assignMoveVector(context.locate().reduceVector(nearestWW));
        } else {
            moveDirection = XYsupport.generateRandomMoveVector();
        }
        return moveDirection;
    }

    public static XY nearestEntity(ControllerContext context, EntityType type) {
        XY position = context.locate();
        int minX = context.getViewLowerLeft().getX();
        int minY = context.getViewLowerLeft().getY();
        int maxX = context.getViewUpperRight().getX();
        int maxY = context.getViewUpperRight().getY();

        try {
            XY nearestEntity = new XY(16, 16);
            for (int x = minX; x < maxX; x++) {
                for (int y = minY; y < maxY; y++) {
                    if (context.getEntityAt(new XY(x, y)) == type) {
                        double distanceTo = position.distanceFrom(new XY(x, y));
                        if (distanceTo < position.distanceFrom(nearestEntity)) {
                            nearestEntity = new XY(x, y);
                        }
                    }
                }
            }
            return nearestEntity;
        } catch (OutOfViewException e) {
            logger.log(Level.WARNING, "No Entity in the searchVector");
        }
        return null;
    }

    static boolean checkSpawnField(ControllerContext context, XY location) {
        try {
            EntityType entityType = context.getEntityAt(location);
            switch (entityType) {
                case NOTHING:
                    return true;
                default:
                    return false;
            }
        } catch (OutOfViewException e) {
            logger.log(Level.WARNING, "No Entity in the searchVector");
        }
        return false;
    }
}