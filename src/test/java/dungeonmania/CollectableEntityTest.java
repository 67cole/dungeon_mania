package dungeonmania;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class CollectableEntityTest {
    // Check if inventory actually contains the items
    @Test

    // Testing pickup of bomb and sword
    public void testCollectableItem() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("collectableTest", "peaceful");
        DungeonResponse tickHolder = null;
        tickHolder =  controller.tick(null, Direction.RIGHT);
        // Testing arrow pickup
        int arrow = 0;
        for (ItemResponse item: tickHolder.getInventory()) {
            if (item.getType().equals("arrow")) {
                arrow = 1;
            }
        }
        assertTrue(arrow == 1);
        // Testing bomb pickup
        tickHolder =  controller.tick(null, Direction.RIGHT);
        int bomb = 0;
        for (ItemResponse item: tickHolder.getInventory()) {
            if (item.getType().equals("bomb")) {
                bomb = 1;
            }
        }
        assertTrue(bomb == 1);
        // Testing healthPotion pickup
        tickHolder =  controller.tick(null, Direction.RIGHT);
        int healthPotion = 0;
        for (ItemResponse item: tickHolder.getInventory()) {
            if (item.getType().equals("health_potion")) {
                healthPotion = 1;
            }
        } 
        assertTrue(healthPotion == 1);
        // Testing invincibilityPotion pickup
        tickHolder =  controller.tick(null, Direction.RIGHT);
        int invincibilityPotion = 0;
        for (ItemResponse item: tickHolder.getInventory()) {
            if (item.getType().equals("invincibility_potion")) {
                invincibilityPotion = 1;
            }
        } 
        assertTrue(invincibilityPotion == 1);
        // Testing invisibilityPotion pickup
        tickHolder =  controller.tick(null, Direction.RIGHT);
        int invisibilityPotion = 0;
        for (ItemResponse item: tickHolder.getInventory()) {
            if (item.getType().equals("invisibility_potion")) {
                invisibilityPotion = 1;
            }
        } 
        assertTrue(invisibilityPotion == 1);
        // Testing key pickup
        tickHolder =  controller.tick(null, Direction.RIGHT);
        int key = 0;
        for (ItemResponse item: tickHolder.getInventory()) {
            if (item.getType().equals("key")) {
                key = 1;
            }
        } 
        assertTrue(key == 1);
        // Testing sword pickup
        tickHolder =  controller.tick(null, Direction.RIGHT);
        int sword = 0;
        for (ItemResponse item: tickHolder.getInventory()) {
            if (item.getType().equals("sword")) {
                sword = 1;
            }
        } 
        assertTrue(sword == 1);
        // Testing treasure pickup
        tickHolder =  controller.tick(null, Direction.RIGHT);
        int treasure = 0;
        for (ItemResponse item: tickHolder.getInventory()) {
            if (item.getType().equals("treasure")) {
                treasure = 1;
            }
        } 
        assertTrue(treasure == 1);
        // Testing wood pickup
        tickHolder =  controller.tick(null, Direction.RIGHT);
        int wood = 0;
        for (ItemResponse item: tickHolder.getInventory()) {
            if (item.getType().equals("wood")) {
                wood = 1;
            }
        } 
        assertTrue(wood == 1);
    }
    // Testing pickup and dropping of bomb
    @Test

    public void testBombDropPickUp() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("advanced-2", "peaceful");
        DungeonResponse tickHolder = null;  
        // Moving the character to the bomb
        for (int i = 0; i < 5; i++) {
            tickHolder =  controller.tick(null, Direction.RIGHT);
        }
        for (int i = 0; i < 5; i++) {
            tickHolder =  controller.tick(null, Direction.RIGHT);
        }
        for (int i = 0; i < 3; i++) {
            tickHolder =  controller.tick(null, Direction.DOWN);
        }
        for (int i = 0; i < 2; i++) {
            tickHolder =  controller.tick(null, Direction.RIGHT);
        }
        // Checking to see if bomb is picked up
        int bomb = 0;
        String bombID = null;
        for (ItemResponse item: tickHolder.getInventory()) {
            if (item.getType().equals("bomb")) {
                bomb = 1;
                bombID = item.getId();
            }
        }
        // Checking to see if bomb is dropped
        assertTrue(bomb == 1);
        tickHolder =  controller.tick(bombID, Direction.NONE);
        bomb = 0;
        for (ItemResponse item: tickHolder.getInventory()) {
            if (item.getType().equals("bomb")) {
                bomb = 1;
            }
        }
        assertTrue(bomb == 0);
        // Checking to see if the bomb is now in the map
        for (EntityResponse entity: tickHolder.getEntities()) {
            if (entity.getType().equals("bomb")) {
                bomb = 1;
            }
        }
        assertTrue(bomb == 1);
    }
}
