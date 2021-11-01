package dungeonmania;
import static org.junit.jupiter.api.Assertions.assertTrue;


import dungeonmania.entities.*;
import dungeonmania.entities.Character;
import dungeonmania.entities.Spider;
import dungeonmania.entities.MovingEntity;
import dungeonmania.entities.BuildableEntities.*;
import dungeonmania.entities.CollectableEntities.*;
import dungeonmania.entities.RareCollectableEntities.*;
import org.junit.jupiter.api.Test;
import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import java.util.List;
import java.util.ArrayList;

public class MovingEntityTest {
    // Checks the movement of the player
    @Test 
    public void testPlayerMovement() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse newDungeon = controller.newGame("testRoom", "Standard");
        Position toBePos = new Position(1,1);
        assertTrue(entityInPlace(toBePos, newDungeon, "player"));

        DungeonResponse tickHolder = null;

        tickHolder =  controller.tick(null, Direction.DOWN);
        Position moveDown = new Position(1,2);
        assertTrue(entityInPlace(moveDown, tickHolder, "player"));

        tickHolder =  controller.tick(null, Direction.RIGHT);
        Position moveRight = new Position(2,2);
        assertTrue(entityInPlace(moveRight, tickHolder, "player"));

        tickHolder =  controller.tick(null, Direction.RIGHT);
        Position moveRight2 = new Position(3,2);
        assertTrue(entityInPlace(moveRight2, tickHolder, "player"));

        tickHolder =  controller.tick(null, Direction.UP);
        Position moveUp = new Position(3,1);
        assertTrue(entityInPlace(moveUp, tickHolder, "player"));

        tickHolder =  controller.tick(null, Direction.LEFT);
        Position moveLeft = new Position(2,1);
        assertTrue(entityInPlace(moveLeft, tickHolder, "player"));
    }
    
    
    // Check if newGame imports the json data properly and adds everything into the entitieslist
    @Test
    public void testMercenarySpawn() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse newDungeon = controller.newGame("advanced-2", "Peaceful");
        Position toBePos = new Position(1,1);
        assertTrue(entityInPlace(toBePos, newDungeon, "player"));
        DungeonResponse tickHolder = null;
        for (int i = 0; i < 75; i++) {
            tickHolder =  controller.tick(null, Direction.DOWN);
        }

        assertTrue(entityInPlace(toBePos, tickHolder, "mercenary"));

    }

    @Test
    public void testSpiderSpawn() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse newDungeon = controller.newGame("advanced-2", "Standard");
        DungeonResponse tickHolder = null;
        for (int i = 0; i < 27; i++) {
            tickHolder =  controller.tick(null, Direction.LEFT);
        }
        Dungeon dung = controller.getCurrDungeon();
        List<Entity> entities = dung.getEntities();
        Position spiderPos = new Position(0, 0);
        for (Entity currEnt : entities) {
            if (currEnt.getType().equals("spider")) {
                spiderPos = currEnt.getPosition();
            }
        }
        assertTrue(spiderPos.getX() != 0);
        assertTrue(spiderPos.getY() != 0);
        System.out.printf("%d, %d", spiderPos.getX(), spiderPos.getY());
        assertTrue(entityInPlace(spiderPos, tickHolder, "spider"));
        
    }

    /**
     * Helper function that returns true if the entity type is on the same position
     * as whats given in the param 
     * @param pos
     * @param main
     * @param type
     * @return boolean
     */
    public boolean entityInPlace(Position pos, DungeonResponse main, String type) {
        for (EntityResponse entity: main.getEntities()) {
            if (entity.getPosition().equals(pos) && entity.getType().equals(type)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Helper to find the itemUsed id
     */
    public String findItemID(DungeonResponse main, String type) {
        for (ItemResponse items : main.getInventory()) {
            if (items.getType().equals(type)) {
                return items.getId();
            }
        }
        
        return null;
    }

}
