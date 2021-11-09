package dungeonmania.entities;

import java.util.List;
import dungeonmania.entities.CollectableEntities.Sword;
import dungeonmania.util.Position;
import dungeonmania.Dungeon;

public class ZombieToastSpawner extends StaticEntity{
    public ZombieToastSpawner(Position position, String type, String ID, boolean isInteractable) {
        super(position,type, ID, isInteractable);
    }

    /**
     * Interacts with the spawner - destroying it and removing a durability off the sword
     * @param inventory - the player's inventory
     * @param interaction - the interactable entity
     */
    public static void interactWithSpawner(List<CollectableEntity> inventory, Entity interaction, Dungeon currDungeon) {
        // Remove the spawner
        currDungeon.removeEntity(interaction);

        // Take off one durability of the sword
        for (CollectableEntity item : inventory) {
            if (item.getType().equals("sword")) {
                Sword sword = (Sword) interaction;
                sword.reduceDurability();

                if (sword.checkDurability() != null) {
                    currDungeon.inventory.remove(item);
                }

                return;
            }
        }

    }
}
