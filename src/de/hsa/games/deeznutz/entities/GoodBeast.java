package de.hsa.games.deeznutz.entities;

import de.hsa.games.deeznutz.core.EntityContext;
import de.hsa.games.deeznutz.core.XY;
import de.hsa.games.deeznutz.core.XYsupport;

public class GoodBeast extends Character {

    private static final int DEFAULT_ENERGY = 200;
    private static final String DEFAULT_NAME = "goodbeast";
    private int stepCount = 0;

    public GoodBeast(XY location) {
        super(DEFAULT_ENERGY, location, DEFAULT_NAME);
    }

    @SuppressWarnings("Duplicates")
    @Override
    public void nextStep(EntityContext entityContext) {
        stepCount++;

        if (stepCount == entityContext.getWaitingTimeBeast()) {
            stepCount = 0;
            Player nearestPlayer = entityContext.nearestPlayerEntity(getLocation());
            if (nearestPlayer == null) {
                entityContext.tryMove(this, XYsupport.generateRandomMoveVector());
                return;
            }
            int xDiff = nearestPlayer.getLocation().getX() - getLocation().getX();
            int yDiff = nearestPlayer.getLocation().getY() - getLocation().getY();
            int moveX, moveY;
            moveX = Integer.compare(0, xDiff);
            moveY = Integer.compare(0, yDiff);
            entityContext.tryMove(this, new XY(moveX, moveY));
        }
    }

    @Override
    public String toString() {
        return "GoodBeast{ " + super.toString() + " }";
    }

}
