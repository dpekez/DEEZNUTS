package de.hsa.games.deeznutz.entities;

import de.hsa.games.deeznutz.Launcher;
import de.hsa.games.deeznutz.core.EntityType;
import de.hsa.games.deeznutz.core.XY;

import java.util.logging.Logger;

public abstract class Entity {
    private final static Logger logger = Logger.getLogger(Launcher.class.getName());

    private static int lastID = -1;
    private int id;
    private int energy;
    private XY location;
    private String name;

    public Entity(int energy, XY location, String name) {
        this.id = ++lastID;
        this.energy = energy;
        this.location = location;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public int getEnergy() {

        return energy;
    }

    public XY getLocation() {
        return location;
    }

    public void setLocation(XY location) {
        this.location = location;
    }

    public void updateEnergy(int energy) {
        logger.finer("Entity with ID " + id + " (" + location + ") got: " + energy);
        this.energy += energy;
    }

    public void move(XY vector) {
        location = location.addVector(vector);
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return "Entity{ " + "id=" + id + ", energy=" + energy + ", location=" + location + '}';
    }

    EntityType getEntityType() {
        if (this instanceof BadBeast)
            return EntityType.BAD_BEAST;
        else if (this instanceof GoodBeast)
            return EntityType.GOOD_BEAST;
        else if (this instanceof BadPlant)
            return EntityType.BAD_PLANT;
        else if (this instanceof GoodPlant)
            return EntityType.GOOD_PLANT;
        else if (this instanceof Wall)
            return EntityType.WALL;
        else if (this instanceof MiniSquirrel)
            return EntityType.MINI_SQUIRREL;
        else if (this instanceof MasterSquirrel)
            return EntityType.MASTER_SQUIRREL;
        else if (this instanceof MasterSquirrelBot)
            return EntityType.MASTER_SQUIRREL_BOT;
        else if (this instanceof MiniSquirrelBot)
            return EntityType.MINI_SQUIRREL_BOT;
        else
            return EntityType.NOTHING;
    }

}
