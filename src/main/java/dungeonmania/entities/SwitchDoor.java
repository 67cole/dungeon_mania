package dungeonmania.entities;

import java.util.List;

import dungeonmania.Dungeon;
import dungeonmania.util.Position;

public class SwitchDoor extends StaticEntity{


    public SwitchDoor(Position position, String type, String ID, boolean isInteractable) {
        super(position,type, ID, isInteractable);
    }

    /**
     * Unlocks door
     */
    public void doorUnlock() {
        this.setType("switchdoor_unlocked");
    }

    /**
     * Locks door
     */
    public void doorLock() {
        this.setType("switch_door");
    }

    /**
     * Checks for switches and boulders cardinally adjacent to the lightbulbs
     * @param main
     * @param entities
     * @return boolean
     */
    public boolean checkSwitchBoulder(Dungeon main) {
        //Get all entities that are cardinally adjacent to the lightbulb
        Position N = super.getPosition().translateBy(0, -1);
        Position E = super.getPosition().translateBy(1, 0);
        Position S = super.getPosition().translateBy(0, 1);
        Position W = super.getPosition().translateBy(-1, 0);
        
        List<Entity> entsAbove = main.getEntitiesAtPos(N);
        List<Entity> entsRight = main.getEntitiesAtPos(E);
        List<Entity> entsBelow = main.getEntitiesAtPos(S);
        List<Entity> entsLeft = main.getEntitiesAtPos(W);  
        
        if (boulderAndSwitch(entsAbove) || boulderAndSwitch(entsRight) || boulderAndSwitch(entsBelow) || boulderAndSwitch(entsLeft)) {
            return true;
        }
        return false;

    }

    /**
     * Checks for switches and boulders in a list of entities
     * @param posEntities
     * @return boolean
     */
    public boolean boulderAndSwitch(List<Entity> posEntities) {
        boolean switchExist = false;
        for (Entity entity: posEntities) {
            if (entity.getType().equals("switch")) {
                switchExist = true;
            }
        }
        if (switchExist == true) {
            for (Entity boulder: posEntities) {
                if (boulder.getType().equals("boulder")) {
                    return true;
                }
            }
        }
        
        return false;
    }

}
