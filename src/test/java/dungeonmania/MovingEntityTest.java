package dungeonmania;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class MovingEntityTest {

    // Checks the movement of the player
    @Test 
    public void testPlayerMovement() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse newDungeon = controller.newGame("advanced", "Peaceful");
        Position toBePos = new Position(1,1);
        assertTrue(entityInPlace(toBePos, newDungeon, "player"));

        DungeonResponse tickHolder = null;

        tickHolder =  controller.tick(null, Direction.DOWN);
        Position moveDown = new Position(1,2);
        assertTrue(entityInPlace(moveDown, tickHolder, "player"));

        tickHolder =  controller.tick(null, Direction.RIGHT);
        Position moveRight = new Position(2,2);
        assertTrue(entityInPlace(moveRight, tickHolder, "player"));
    }
    
    
    // Check if newGame imports the json data properly and adds everything into the entitieslist
    @Test
    public void testMercenarySpawn() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse newDungeon = controller.newGame("advanced", "Peaceful");
        Position toBePos = new Position(1,1);
        assertTrue(entityInPlace(toBePos, newDungeon, "player"));
        DungeonResponse tickHolder = null;
        for (int i = 0; i < 75; i++) {
            tickHolder =  controller.tick(null, Direction.DOWN);
        }

        assertTrue(entityInPlace(toBePos, tickHolder, "mercenary"));

    }

    // Check if zombie spawns in every 20 tick on Standard Difficulty
    @Test
    public void testZombieSpawnStandard() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse newDungeon = controller.newGame("testRoom", "Standard");
        Position toBePos = new Position(1,1);
        assertTrue(entityInPlace(toBePos, newDungeon, "player"));
        DungeonResponse tickHolder = null;
        
        for (int i = 0; i < 20; i++) {
            tickHolder =  controller.tick(null, Direction.DOWN);
        }

        Position zombieSpawn = new Position(3,3);

        assertTrue(entityInPlace(zombieSpawn, tickHolder, "zombie_toast"));
    }

    // Check if zombie spawns in every 15 tick on Hard Difficulty
    @Test
    public void testZombieSpawnHard() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse newDungeon = controller.newGame("testRoom", "Hard");
        Position toBePos = new Position(1,1);
        assertTrue(entityInPlace(toBePos, newDungeon, "player"));
        DungeonResponse tickHolder = null;
        
        for (int i = 0; i < 15; i++) {
            tickHolder =  controller.tick(null, Direction.DOWN);
        }

        Position zombieSpawn = new Position(3,3);

        assertTrue(entityInPlace(zombieSpawn, tickHolder, "zombie_toast"));
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

}
