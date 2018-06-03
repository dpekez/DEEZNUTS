package de.hsa.games.deeznutz.botimpls.dpekez;

import de.hsa.games.deeznutz.Launcher;
import de.hsa.games.deeznutz.botapi.BotController;
import de.hsa.games.deeznutz.botapi.ControllerContext;
import de.hsa.games.deeznutz.botapi.OutOfViewException;
import de.hsa.games.deeznutz.core.EntityType;
import de.hsa.games.deeznutz.core.XY;
import de.hsa.games.deeznutz.core.XYsupport;
import java.util.logging.Logger;

public class DpekezMini implements BotController {

    private int refreshSelector;
    private int selectedQ;

    public DpekezMini() {
        refreshSelector = 0;
        selectedQ = 1;
    }

    @Override
    public void nextStep(ControllerContext context) {

        // check implode condition
        //if (implodeCondition(context, 5)) {
        //    context.implode(5);
        //}

        // set quadrant selector refresh rate
        if (refreshSelector <= 0) {
            refreshSelector = 30;
            selectedQ = qSelector(context);
        }
        else
            refreshSelector--;

        XY nearestEntity = null;
        switch (selectedQ) {
            case 0:
                nearestEntity = nearestEntityFinder(context, context.getViewLowerLeft().getX(), context.getViewUpperRight().getX(), context.getViewUpperRight().getY(), context.getViewLowerLeft().getY());
                break;
            case 1:
                nearestEntity = nearestEntityFinder(context, context.getViewLowerLeft().getX(), context.locate().getX(), context.getViewUpperRight().getY(), context.locate().getY());
                break;
            case 2:
                nearestEntity = nearestEntityFinder(context, context.locate().getX(), context.getViewUpperRight().getX(), context.getViewUpperRight().getY(), context.locate().getY());
                break;
            case 3:
                nearestEntity = nearestEntityFinder(context, context.getViewLowerLeft().getX(), context.locate().getX(), context.locate().getY(), context.getViewLowerLeft().getY());
                break;
            case 4:
                nearestEntity = nearestEntityFinder(context, context.locate().getX(), context.getViewUpperRight().getX(), context.locate().getY(), context.getViewLowerLeft().getY());
                break;
        }

        Logger.getLogger(Launcher.class.getName()).fine("start: "+ context.locate() + " ziel: " + nearestEntity);

        XY moveVector;

        // no entity in sight
        if (nearestEntity == null) {
            switch (selectedQ) {
                case 0:
                    Logger.getLogger(Launcher.class.getName()).fine("nicht gut");
                    moveVector = XYsupport.generateRandomMoveVector();
                    break;
                case 1:
                    moveVector = XY.LEFT_UP;
                    break;
                case 2:
                    moveVector = XY.RIGHT_UP;
                    break;
                case 3:
                    moveVector = XY.LEFT_DOWN;
                    break;
                case 4:
                    moveVector = XY.RIGHT_DOWN;
                    break;
                default:
                    moveVector = XYsupport.generateRandomMoveVector();
            }
            //refreshSelector = 0;
        } else {
            moveVector = XYsupport.decreaseDistance(context.locate(), nearestEntity);
        }

        // bad beast evasion
        if (context.getEntityAt(context.locate().addVector(moveVector)) == EntityType.BAD_BEAST) {
            refreshSelector = -5;
            moveVector = badBeastEvader(moveVector);
        }

        // bad plant evasion
        else if (context.getEntityAt(context.locate().addVector(moveVector)) == EntityType.BAD_PLANT) {
            refreshSelector = -5;
            moveVector = badPlantEvader(moveVector);
        }

        // wall evasion
        else if (context.getEntityAt(context.locate().addVector(moveVector)) == EntityType.WALL) {
            refreshSelector = -5;
            moveVector = wallEvader(moveVector);
        }

        // enemy evasion
        else if (context.getEntityAt(context.locate().addVector(moveVector)) == EntityType.MASTER_SQUIRREL) {
            refreshSelector = -5;
            moveVector = enemyEvader(moveVector);
        }

        context.move(moveVector);
    }

    private int qSelector(ControllerContext context) {
        int highest = quantifier(context, context.getViewLowerLeft().getX(), context.getViewUpperRight().getX(), context.getViewUpperRight().getY(), context.getViewLowerLeft().getY());
        int q1 = quantifier(context, context.getViewLowerLeft().getX(), context.locate().getX(), context.getViewUpperRight().getY(), context.locate().getY());
        int q2 = quantifier(context, context.locate().getX(), context.getViewUpperRight().getX(), context.getViewUpperRight().getY(), context.locate().getY());
        int q3 = quantifier(context, context.getViewLowerLeft().getX(), context.locate().getX(), context.locate().getY(), context.getViewLowerLeft().getY());
        int q4 = quantifier(context, context.locate().getX(), context.getViewUpperRight().getX(), context.locate().getY(), context.getViewLowerLeft().getY());

        int selected = 0;

        if (q1 > highest) {
            selected = 2;
            highest = q2;
        }
        if (q2 > highest) {
            selected = 2;
            highest = q2;
        }
        if (q3 > highest) {
            selected = 3;
            highest = q3;
        }
        if (q4 > highest) {
            selected = 4;
        }

        Logger.getLogger(Launcher.class.getName()).fine(selected + " selected");
        return selected;
    }

    private int quantifier(ControllerContext context, int startX, int stopX, int startY, int stopY) {
        int quantity = 0;
        for (int x = startX; x < stopX; x++) {
            for (int y = startY; y < stopY; y++) {
                if (context.getEntityAt(new XY(x, y)) == EntityType.GOOD_BEAST)
                    quantity += 4;
                if (context.getEntityAt(new XY(x, y)) == EntityType.GOOD_PLANT)
                    quantity += 3;
                if (context.getEntityAt(new XY(x, y)) == EntityType.BAD_BEAST)
                    quantity -= 2;
                if (context.getEntityAt(new XY(x, y)) == EntityType.BAD_PLANT)
                    quantity -= 1;
                if (context.getEntityAt(new XY(x, y)) == EntityType.WALL)
                    quantity -= 1;
                if (context.getEntityAt(new XY(x, y)) == EntityType.NOTHING)
                    quantity += 1;
            }
        }
        Logger.getLogger(Launcher.class.getName()).fine("quantity: " + quantity);
        return quantity;
    }

    private XY nearestEntityFinder(ControllerContext context, int startX, int stopX, int startY, int stopY) {
        XY nearestEntity = null;
        for (int x = startX; x < stopX; x++) {
            for (int y = startY; y < stopY; y++) {
                if (context.getEntityAt(new XY(x, y)) != EntityType.GOOD_PLANT && context.getEntityAt(new XY(x, y)) != EntityType.GOOD_BEAST) {
                    continue;
                }
                if (nearestEntity == null) {
                    Logger.getLogger(Launcher.class.getName()).fine("nulled");
                    nearestEntity = new XY(x, y);
                } else if (context.locate().distanceFrom(new XY(x, y)) < context.locate().distanceFrom(nearestEntity)) {
                    Logger.getLogger(Launcher.class.getName()).fine("nearer");
                    nearestEntity = new XY(x, y);
                }
            }
        }

        return nearestEntity;
    }

    private XY enemyEvader(XY nextLocation) {
        //todo
        return wallEvader(nextLocation);
    }

    private XY badBeastEvader(XY nextLocation) {
        //todo
        return wallEvader(nextLocation);
    }

    private XY badPlantEvader(XY nextLocation) {
        //todo
        return wallEvader(nextLocation);
    }

    private XY wallEvader(XY nextLocation) {
        if (nextLocation == XY.RIGHT)
            return XY.RIGHT_DOWN;
        else if (nextLocation == XY.RIGHT_DOWN)
            return XY.DOWN;
        else if (nextLocation == XY.DOWN)
            return XY.LEFT_DOWN;
        else if (nextLocation == XY.LEFT_DOWN)
            return XY.LEFT;
        else if (nextLocation == XY.LEFT)
            return XY.LEFT_DOWN;
        else if (nextLocation == XY.LEFT_UP)
            return XY.UP;
        else if (nextLocation == XY.UP)
            return XY.RIGHT_UP;
        else if (nextLocation == XY.RIGHT_UP)
            return XY.RIGHT;
        else
            return XYsupport.generateRandomMoveVector();
    }

    private boolean implodeCondition(ControllerContext context, int impactRadius) {
        int entitiesInsideImpactRadius = 0;

        int startX = context.locate().getX() - (impactRadius - 1) / 2;
        int startY = context.locate().getY() - (impactRadius - 1) / 2;
        int stopX = context.locate().getX() + (impactRadius - 1) / 2;
        int stopY = context.locate().getY() + (impactRadius - 1) / 2;

        if (startX < 0)
            startX = 0;
        if (startY < 0)
            startY = 0;
        if (stopX > context.getViewUpperRight().getX())
            stopX = context.getViewUpperRight().getX();
        if (stopY > context.getViewLowerLeft().getY())
            stopY = context.getViewLowerLeft().getY();

        Logger.getLogger(Launcher.class.getName()).finest("startX:" + startX + " stopX:" + stopX + " startY:" + startY + " stopY:" + stopY);

        for (int x = startX; x < stopX; x++) {
            for (int y = startY; y < stopY; y++) {
                try {
                    EntityType checkEntity = context.getEntityAt(new XY(x, y));
                    if (checkEntity == EntityType.GOOD_BEAST || checkEntity == EntityType.GOOD_PLANT) {
                        Logger.getLogger(Launcher.class.getName()).fine("Found Entity inside implosion vector.");
                        entitiesInsideImpactRadius++;
                    }
                } catch (OutOfViewException e) {
                    Logger.getLogger(Launcher.class.getName()).fine("No Entity inside of implode search vector.");
                }
            }
        }
        Logger.getLogger(Launcher.class.getName()).fine("Entities inside impact radius: " + entitiesInsideImpactRadius);
        return (entitiesInsideImpactRadius >= 1);
    }

}
