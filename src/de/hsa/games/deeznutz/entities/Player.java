package de.hsa.games.deeznutz.entities;

import de.hsa.games.deeznutz.core.EntityContext;
import de.hsa.games.deeznutz.core.XY;

public abstract class Player extends Character {

    private int stunnedRounds = 0;

    Player(int energy, XY xy) {
        super(energy, xy);
    }

    public void stun() {
        stunnedRounds = 3;
    }

    @Override
    public void nextStep(EntityContext context) {
        if (stunnedRounds > 0)
            stunnedRounds--;
    }

    boolean isStunned() {
        return stunnedRounds > 0;
    }

    public boolean isStunnedNextRound() {
        return stunnedRounds > 1;
    }

    @Override
    public String toString() {
        return "Player{" +
                " stunnedRounds=" + stunnedRounds +
                " " + super.toString() +
                '}';
    }
}