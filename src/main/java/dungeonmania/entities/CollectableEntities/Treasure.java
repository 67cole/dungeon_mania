package dungeonmania.entities.CollectableEntities;

import dungeonmania.entities.CollectableEntity;
import dungeonmania.util.Position;

import java.util.List;

public class Treasure extends CollectableEntity{
    /**
     * Creates the treasure
     * @param position - the current position in the dungeon
     * @param type - the type of entity
     * @param ID - the ID of entity
     * @param isInteractable - check if the entity is interactable
     */
    public Treasure(Position position, String type, String ID, boolean IsInteractable) {
        super(position,type, ID, IsInteractable);
    }   

    /**
     * Checks whether the player has enough gold to bribe the mercenary
     * @param inventory - the player's inventory
     */
    public static boolean playerHasEnoughGold(List<CollectableEntity> inventory) {
        // Gold counter
        int totalGold = 0;

        // Checks for gold in the inventory
        for (CollectableEntity item : inventory) {
            if (item.getType().equals("treasure")) {
                totalGold++;
            }
        }

        if (totalGold >= 2) return true;

        return false;
    }
}
