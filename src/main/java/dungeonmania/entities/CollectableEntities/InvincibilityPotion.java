package dungeonmania.entities.CollectableEntities;

import dungeonmania.entities.MovingEntity;
import dungeonmania.entities.Entity;
import dungeonmania.entities.CollectableEntity;
import dungeonmania.util.Position;
import dungeonmania.entities.Character;
import dungeonmania.util.Direction;

import java.util.List;

public class InvincibilityPotion extends CollectableEntity{
    /**
     * Creates the invincibility potion
     * @param position - the current position in the dungeon
     * @param type - the type of entity
     * @param ID - the ID of entity
     * @param isInteractable - check if the entity is interactable
     */
    public InvincibilityPotion(Position position, String type, String ID, boolean IsInteractable) {
        super(position,type, ID, IsInteractable);
    }   

    /**
     * Moves the enemies around if the character is invincible
     * @param character - The character class
     * @param entities - The list of all entities
     */
    public static void invincibilityPhase(Character character, List<Entity> entities, Direction direction) {
        Position player = Character.getPlayerPosition(entities);
        player = player.translateBy(direction);

        for (Entity entity : entities) {
            if (entity.getType().equals("zombie_toast") || entity.getType().equals("spider") || entity.getType().equals("mercenary")) {
                MovingEntity temp = (MovingEntity) entity;
                temp.runEnemy(entities, player);
            }
        }
    }
}