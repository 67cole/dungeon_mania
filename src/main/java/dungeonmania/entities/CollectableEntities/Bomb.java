package dungeonmania.entities.CollectableEntities;

import dungeonmania.entities.Entity;
import dungeonmania.entities.Character;
import dungeonmania.entities.CollectableEntity;
import dungeonmania.util.Position;
import dungeonmania.Dungeon;

import java.util.List;

public class Bomb extends CollectableEntity{
    /**
     * Status of the bomb
     */
    private boolean activated;

    /**
     * Creates the bomb
     * @param position - the current position in the dungeon
     * @param type - the type of entity
     * @param ID - the ID of entity
     * @param isInteractable - check if the entity is interactable
     */
    public Bomb(Position position, String type, String ID, boolean IsInteractable) {
        super(position,type, ID, IsInteractable);
        this.activated = false;
    }

    /**
     * Checks the status of the bomb
     * @return boolean
     */
    public boolean isActivated() {
        return this.activated;
    }

    /**
     * Sets the status of the bomb
     */
    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    /**
     * checks if a bomb is activated
     * @param interactingEntities
     * @return boolean
     */
    public static boolean checkBomb(List<Entity> interactingEntities) {
        for (Entity interactingEntity: interactingEntities) {
            if (interactingEntity != null) {
                if (isBombActivated(interactingEntity)) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * Checks if the bomb is activated
     * @param entity - the item
     * @return boolean 
     */
    public static boolean isBombActivated(Entity entity) {
        if (entity.getType().equals("bomb")) {
            Bomb bomb = (Bomb) entity;
            if (bomb.isActivated()) {
                return true;
            }
        }
        return false;
    }


    /**
     * Checks if the bomb is activated
     * @param player - the character
     * @param main - the dungeon
     * @param itemUsed - the item that is used
     * @return Bomb
     */
    public static Bomb useBomb(Character player, Dungeon main, String itemUsed) {
        if (itemUsed != null) {
            for (CollectableEntity entity2: main.inventory) {
                if (entity2.getID().equals(itemUsed)) {
                    if (entity2.getType().equals("bomb")) {
                        Bomb bomb = (Bomb) entity2;
                        Bomb newBomb = new Bomb(player.getPosition(), bomb.getType(), bomb.getID(), bomb.getIsInteractable());
                        newBomb.setActivated(true);
                        main.inventory.remove(bomb);
                        return newBomb;
                    }
                }
            }
        }
        return null;
    }

}
